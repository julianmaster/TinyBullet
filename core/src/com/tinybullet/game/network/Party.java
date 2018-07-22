package com.tinybullet.game.network;

import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.client.RequestPlayerStatusPartyJson;
import com.tinybullet.game.network.json.server.ResponseJoinPartyJson;
import com.tinybullet.game.network.json.server.ResponseStartPartyJson;
import com.tinybullet.game.util.Pair;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;
import java.util.Map;

public class Party {
	private PartyState state = PartyState.WAIT_START;
	private Map<PlayerColor, Pair<ServerWebSocket, Boolean>> players = new HashMap<>();
	private Map<PlayerColor, PlayerPosition> positions = new HashMap<>();
//	private Map<BulletColor, PlayerColor> bulletTaked = new HashMap<>();

	public boolean addPlayer(ServerWebSocket webSocket) {
		if(!positions.containsKey(PlayerColor.RED)) {
			initPlayerPosition(webSocket, PlayerColor.RED, PlayerStartPosition.RED);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			initPlayerPosition(webSocket, PlayerColor.GREEN, PlayerStartPosition.GREEN);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			initPlayerPosition(webSocket, PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
			initPlayerPosition(webSocket, PlayerColor.PURPLE, PlayerStartPosition.PURPLE);
			return true;
		}
		return false;
	}

	private void initPlayerPosition(ServerWebSocket webSocket, PlayerColor playerColor, PlayerStartPosition playerStartPosition) {
		players.put(playerColor, new Pair<>(webSocket, false));
		PlayerPosition playerPosition = new PlayerPosition(playerStartPosition.position.x, playerStartPosition.position.y);
		positions.put(playerColor, playerPosition);
	}

	private ResponseStartPartyJson startParty(PlayerColor playerColor) {
		PlayerPosition playerPosition = positions.get(playerColor);

		ResponseStartPartyJson responseStartPartyJson = new ResponseStartPartyJson();
		responseStartPartyJson.playerColor = playerColor;
		responseStartPartyJson.x = playerPosition.x;
		responseStartPartyJson.y = playerPosition.y;

		return responseStartPartyJson;
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

	public Map<PlayerColor, Pair<ServerWebSocket, Boolean>> getPlayers() {
		return players;
	}
}
