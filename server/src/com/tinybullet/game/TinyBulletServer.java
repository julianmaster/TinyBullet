package com.tinybullet.game;

import com.badlogic.gdx.math.Vector2;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.tinybullet.game.model.Player;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.Party;
import com.tinybullet.game.network.json.client.*;
import com.tinybullet.game.network.json.server.*;
import com.tinybullet.game.util.Pair;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class TinyBulletServer {

	private final Vertx vertx = Vertx.vertx();
	private final Serializer serializer = new JsonSerializer();
	private final ReentrantLock lock = new ReentrantLock();

	private List<ServerWebSocket> webSockets = new ArrayList<>();
	private Map<Integer, Party> parties = new LinkedHashMap<>();

	private void launch() {
		System.out.println("Launching web socket server...");
		final HttpServer server = vertx.createHttpServer();
		server.websocketHandler(webSocket -> {
			webSocket.frameHandler(frame -> handleFrame(webSocket, frame));
			webSocket.endHandler(frame -> handleSocketClosed(webSocket, frame));
		}).listen(Constants.PORT);
		parties.put(1, new Party());
		parties.put(2, new Party());
		parties.put(3, new Party());
		parties.put(5, new Party());
		System.out.println("Go");
	}

	private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
		final Object request = serializer.deserialize(frame.binaryData().getBytes());

		if(request instanceof RefreshListPartiesJson) {
			int[] list = new int[parties.size()];

			lock.lock();
			int i = 0;
			for(Map.Entry<Integer, Party> party : parties.entrySet()) {
				list[i] = party.getKey();
				i++;
			}
			lock.unlock();

			ResponseListPartiesJson responseListPartiesJson = new ResponseListPartiesJson();
			responseListPartiesJson.list = list;
			webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responseListPartiesJson)));
		}
		else if(request instanceof RequestJoinPartyJson) {
			RequestJoinPartyJson requestJoinPartyJson = (RequestJoinPartyJson)request;
			lock.lock();
			Party party = parties.get(requestJoinPartyJson.party);

			boolean playerAdded = party != null && party.addPlayer(webSocket);

			if(playerAdded) {
				sendResponsePlayerStatusPartyJson(party);
			}
			else {
				ResponsePlayerStatusPartyJson responsePlayerStatusPartyJson = new ResponsePlayerStatusPartyJson();
				responsePlayerStatusPartyJson.join = false;
				webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responsePlayerStatusPartyJson)));
			}

			lock.unlock();
		}
		else if(request instanceof RequestPlayerStatusPartyJson) {
			lock.lock();
			for(Party party : parties.values()) {
				if(party.getPlayers().containsKey(webSocket)) {

					party.playerStatus(webSocket, ((RequestPlayerStatusPartyJson)request).ready);

					// Check if all players are readies to start party
					boolean runParty = true;
					for(Pair<PlayerColor, Boolean> player : party.getPlayers().values()) {
						runParty = runParty && player.value;
					}

					if(runParty) {
						// TODO Verify if there is more than 1 player

						// All players are readies
						ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = party.waitStartParty();
						for(ServerWebSocket serverWebSocket : party.getPlayers().keySet()) {
							serverWebSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responsePositionsPlayersPartyJson)));
						}

						// Send a ResponsePartyStateJson for start the game after 3 seconds
						vertx.setTimer(3000L, new Handler<Long>() {
							@Override
							public void handle(Long event) {
								lock.lock();
								ResponsePartyStateJson responsePartyStateJson = party.startParty();
								for(ServerWebSocket serverWebSocket : party.getPlayers().keySet()) {
									serverWebSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responsePartyStateJson)));
								}
								lock.unlock();
							}
						});
					}
					else {
						// One or mores players aren't ready
						sendResponsePlayerStatusPartyJson(party);
					}

					break;
				}
			}
			lock.unlock();
		}
		else if(request instanceof RequestChangePositionPlayerJson) {
			lock.lock();
			for(Party party : parties.values()) {
				if(party.getPlayers().containsKey(webSocket)) {
					ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = party.changePlayerPosition(webSocket, ((RequestChangePositionPlayerJson)request).position);
					for(ServerWebSocket serverWebSocket : party.getPlayers().keySet()) {
						serverWebSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responsePositionsPlayersPartyJson)));
					}
					break;
				}
			}
			lock.unlock();
		}
		else if(request instanceof RequestFireBulletJson) {
			lock.lock();
			RequestFireBulletJson requestFireBulletJson = (RequestFireBulletJson)request;
			ResponseFireBulletJson responseFireBulletJson = new ResponseFireBulletJson();
			responseFireBulletJson.position = requestFireBulletJson.position;
			responseFireBulletJson.angle = requestFireBulletJson.angle;
			responseFireBulletJson.color = requestFireBulletJson.color;
			responseFireBulletJson.direction = requestFireBulletJson.direction;

			for(Party party : parties.values()) {
				if (party.getPlayers().containsKey(webSocket)) {
					for(ServerWebSocket serverWebSocket : party.getPlayers().keySet()) {
						serverWebSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responseFireBulletJson)));
					}
					break;
				}
			}
			lock.unlock();
		}





	}

	private void handleSocketClosed(final ServerWebSocket webSocket, final Void frame) {
		lock.lock();
		webSockets.remove(webSocket);
		for(Party party : parties.values()) {
			party.removePlayer(webSocket);
		}
		lock.unlock();
	}

	private void sendResponsePlayerStatusPartyJson(Party party) {
		for(Map.Entry<ServerWebSocket, Pair<PlayerColor, Boolean>> player : party.getPlayers().entrySet()) {
			ResponsePlayerStatusPartyJson responsePlayerStatusPartyJson = new ResponsePlayerStatusPartyJson();
			responsePlayerStatusPartyJson.join = true;
			responsePlayerStatusPartyJson.playerColor = player.getValue().key;

			PlayerColor[] playerColors = new PlayerColor[party.getPlayers().keySet().size()];
			boolean[] readies = new boolean[party.getPlayers().keySet().size()];
			int i = 0;
			for(Pair<PlayerColor, Boolean> playerInfo : party.getPlayers().values()) {
				playerColors[i] = playerInfo.key;
				readies[i] = playerInfo.value;
				i++;
			}
			responsePlayerStatusPartyJson.players = playerColors;
			responsePlayerStatusPartyJson.readies = readies;
			player.getKey().writeBinaryMessage(Buffer.buffer(serializer.serialize(responsePlayerStatusPartyJson)));
		}
	}

	public static void main (String[] arg) throws Exception {
		new TinyBulletServer().launch();
	}
}
