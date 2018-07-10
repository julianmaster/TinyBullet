package com.tinybullet.game;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.tinybullet.game.network.Party;
import com.tinybullet.game.network.PartyState;
import com.tinybullet.game.network.json.AddPlayerJson;
import com.tinybullet.game.network.json.ListPartyJson;
import com.tinybullet.game.network.json.PartyStateJson;
import com.tinybullet.game.network.json.PlayerInfoJson;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TinyBulletServer {
	private final Vertx vertx = Vertx.vertx();
	private final Serializer serializer = new JsonSerializer();
	private List<WebSocket> webSockets = new ArrayList<>();
	private Map<Integer, Party> parties = new LinkedHashMap<>();

	private void launch() {
		System.out.println("Launching web socket server...");
		final HttpServer server = vertx.createHttpServer();
		server.websocketHandler(webSocket -> {
			webSocket.frameHandler(frame -> handleFrame(webSocket, frame));
			webSocket.endHandler(frame -> handleSocketClosed(webSocket, frame));
		}).listen(Constants.PORT);
		System.out.println("Go");
	}

	private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
		final Object request = serializer.deserialize(frame.binaryData().getBytes());
		if(request instanceof AddPlayerJson) {
			AddPlayerJson addPlayerJson = (AddPlayerJson)request;
			Party party = parties.get(addPlayerJson.party);
			if(party == null) {
				return;
			}
			PlayerInfoJson playerInfoJson = party.addPlayer();
			webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(playerInfoJson)));

			PartyStateJson partyStateJson = new PartyStateJson();
			partyStateJson.partyState = PartyState.PLAY;
			vertx.setTimer(4000L, new Handler<Long>() {
				@Override
				public void handle(Long event) {
					webSocket.writeBinaryMessage(Buffer.buffer(serializer.serialize(partyStateJson)));
				}
			});
		}
		if(request instanceof PlayerInfoJson) {
			PlayerInfoJson playerInfoJson = (PlayerInfoJson)request;
			Party party = parties.get(playerInfoJson.party);
			if(party == null) {
				return;
			}
			party.changePlayerPosition(playerInfoJson);
			// TODO send to others members of party
			System.out.println(playerInfoJson.playerColor.name()+": ["+ playerInfoJson.x+"; "+ playerInfoJson.y+"]");
		}
	}

	private void handleSocketClosed(final ServerWebSocket webSocket, final Void frame) {

	}

	private ListPartyJson listParties() {
		int[] list = new int[parties.size()];

		synchronized (parties) {
			int i = 0;
			for(Map.Entry<Integer, Party> party : parties.entrySet()) {
				list[i] = party.getKey();
				i++;
			}
		}
		ListPartyJson listPartyJson = new ListPartyJson();
		listPartyJson.list = list;
		return listPartyJson;
	}

	public static void main (String[] arg) throws Exception {
		new TinyBulletServer().launch();
	}
}
