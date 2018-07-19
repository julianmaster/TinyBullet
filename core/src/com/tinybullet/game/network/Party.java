package com.tinybullet.game.network;

import com.github.czyzby.websocket.WebSocket;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.client.PlayerInfoJson;
import com.tinybullet.game.network.json.client.RequestPlayerStatusPartyJson;
import com.tinybullet.game.network.json.server.ResponseJoinPartyJson;
import com.tinybullet.game.util.Pair;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;
import java.util.Map;

public class Party {
	private PartyState state = PartyState.WAIT_START;
	private Map<PlayerColor, PlayerPosition> positions = new HashMap<>();
	private Map<PlayerColor, Pair<ServerWebSocket, Boolean>> players = new HashMap<>();
//	private Map<BulletColor, PlayerColor> bulletTaked = new HashMap<>();

	public ResponseJoinPartyJson addPlayer(ServerWebSocket webSocket) {
		ResponseJoinPartyJson responseJoinPartyJson;

		if(!positions.containsKey(PlayerColor.RED)) {
			players.put(PlayerColor.RED, new Pair<>(webSocket, false));
			responseJoinPartyJson = initPlayerPosition(PlayerColor.RED, PlayerStartPosition.RED);
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			players.put(PlayerColor.GREEN, new Pair<>(webSocket, false));
			responseJoinPartyJson = initPlayerPosition(PlayerColor.GREEN, PlayerStartPosition.GREEN);
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			players.put(PlayerColor.YELLOW, new Pair<>(webSocket, false));
			responseJoinPartyJson = initPlayerPosition(PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
			players.put(PlayerColor.PURPLE, new Pair<>(webSocket, false));
			responseJoinPartyJson = initPlayerPosition(PlayerColor.PURPLE, PlayerStartPosition.PURPLE);
		}
		else {
			// No place in party
			responseJoinPartyJson = new ResponseJoinPartyJson();
			responseJoinPartyJson.join = false;
		}
		return responseJoinPartyJson;
	}

	private ResponseJoinPartyJson initPlayerPosition(PlayerColor playerColor, PlayerStartPosition playerStartPosition) {
		PlayerPosition playerPosition = new PlayerPosition(playerStartPosition.position.x, playerStartPosition.position.y);
		positions.put(playerColor, playerPosition);

		ResponseJoinPartyJson responseJoinPartyJson = new ResponseJoinPartyJson();
		responseJoinPartyJson.join = true;
		responseJoinPartyJson.playerColor = playerColor;
		responseJoinPartyJson.x = playerPosition.x;
		responseJoinPartyJson.y = playerPosition.y;
		return responseJoinPartyJson;
	}

//	public void changePlayerPosition(PlayerInfoJson playerInfoJson) {
//		PlayerPosition playerPosition = positions.get(playerInfoJson.playerColor);
//		playerPosition.x = playerInfoJson.x;
//		playerPosition.y = playerInfoJson.y;
//		change = true;
//	}

	public void playerReady(ServerWebSocket webSocket, RequestPlayerStatusPartyJson requestPlayerStatusPartyJson) {
		for(Pair<ServerWebSocket, Boolean> pair : players.values()) {
			if(webSocket == pair.key) {
				pair.value = requestPlayerStatusPartyJson.ready;
				return;
			}
		}
	}

	public void removePlayer(ServerWebSocket webSocket) {
		PlayerColor playerColor = null;
		for(Map.Entry<PlayerColor, Pair<ServerWebSocket, Boolean>> player : players.entrySet()) {
			if(player.getValue().key == webSocket) {
				playerColor = player.getKey();
			}
		}
		if(playerColor != null) {
			positions.remove(playerColor);
			players.remove(playerColor);
		}
	}

	public void setState(PartyState state) {
		this.state = state;
	}

	public PartyState getState() {
		return state;
	}
}
