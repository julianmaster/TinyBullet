package com.tinybullet.game;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.tinybullet.game.network.Party;
import com.tinybullet.game.network.json.client.RequestJoinPartyJson;
import com.tinybullet.game.network.json.client.PlayerInfoJson;
import com.tinybullet.game.network.json.client.RefreshListPartiesJson;
import com.tinybullet.game.network.json.server.ResponseJoinPartyJson;
import com.tinybullet.game.network.json.server.ListPartiesJson;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

			ListPartiesJson listPartiesJson = new ListPartiesJson();
			listPartiesJson.list = list;
			webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(listPartiesJson)));
		}
		else if(request instanceof RequestJoinPartyJson) {
			RequestJoinPartyJson requestJoinPartyJson = (RequestJoinPartyJson)request;
			lock.lock();
			Party party = parties.get(requestJoinPartyJson.party);

			ResponseJoinPartyJson responseJoinPartyJson;
			if(party == null) {
				responseJoinPartyJson = new ResponseJoinPartyJson();
				responseJoinPartyJson.join = false;
			}
			else {
				responseJoinPartyJson = party.addPlayer(webSocket);
			}
			lock.unlock();
			System.out.println("Join party "+requestJoinPartyJson.party+": "+responseJoinPartyJson.join);

			webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(responseJoinPartyJson)));

//			PartyStateJson partyStateJson = new PartyStateJson();
//			partyStateJson.partyState = PartyState.PLAY;
//			vertx.setTimer(4000L, new Handler<Long>() {
//				@Override
//				public void handle(Long event) {
//					webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(partyStateJson)));
//				}
//			});
		}
		else if(request instanceof PlayerInfoJson) {
//			PlayerInfoJson playerInfoJson = (PlayerInfoJson)request;
//			Party party = parties.get(playerInfoJson.party);
//			if(party == null) {
//				return;
//			}
//			party.changePlayerPosition(playerInfoJson);
//			// TODO send to others members of party
//			System.out.println(playerInfoJson.playerColor.name()+": ["+ playerInfoJson.x+"; "+ playerInfoJson.y+"]");
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

	public static void main (String[] arg) throws Exception {
		new TinyBulletServer().launch();
	}
}
