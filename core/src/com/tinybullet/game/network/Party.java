package com.tinybullet.game.network;

import com.badlogic.gdx.math.Vector2;
import com.tinybullet.game.model.BulletColor;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.server.ResponsePartyStateJson;
import com.tinybullet.game.network.json.server.ResponsePositionsPlayersPartyJson;
import com.tinybullet.game.util.Pair;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Party {
	private PartyState state = PartyState.LOBBY;
	private Map<ServerWebSocket, Pair<PlayerColor, Boolean>> players = new HashMap<>();
	private HashMap<PlayerColor, Vector2> positions = new LinkedHashMap<>();
	private HashMap<PlayerColor, BulletColor> bulletsTaked = new LinkedHashMap<>();

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
		players.put(webSocket, new Pair<>(playerColor, false));
		Vector2 playerPosition = new Vector2(playerStartPosition.position.x, playerStartPosition.position.y);
		positions.put(playerColor, playerPosition);
	}

	public ResponsePositionsPlayersPartyJson waitStartParty() {
		state = PartyState.WAIT_START;
		ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = new ResponsePositionsPlayersPartyJson();
		responsePositionsPlayersPartyJson.playerColors = positions.keySet().toArray(new PlayerColor[positions.size()]);
		responsePositionsPlayersPartyJson.positions = positions.values().toArray(new Vector2[positions.size()]);
		return responsePositionsPlayersPartyJson;
	}

	public ResponsePartyStateJson startParty() {
		state = PartyState.PLAY;
		ResponsePartyStateJson responsePartyStateJson = new ResponsePartyStateJson();
		responsePartyStateJson.partyState = state;
		return responsePartyStateJson;
	}

	public ResponsePositionsPlayersPartyJson changePlayerPosition(ServerWebSocket webSocket, Vector2 position) {
		if(!players.containsKey(webSocket)) {
			return null;
		}

		PlayerColor playerColor = players.get(webSocket).key;
		positions.get(playerColor).set(position);

		ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = new ResponsePositionsPlayersPartyJson();
		responsePositionsPlayersPartyJson.playerColors = positions.keySet().toArray(new PlayerColor[positions.size()]);
		responsePositionsPlayersPartyJson.positions = positions.values().toArray(new Vector2[positions.size()]);
		return responsePositionsPlayersPartyJson;
	}

	public boolean playerStatus(ServerWebSocket webSocket, boolean ready) {
		if(players.containsKey(webSocket)) {
			players.get(webSocket).value = ready;
		}

		boolean allReady = true;
		for(Pair<PlayerColor, Boolean> pair : players.values()) {
			allReady = allReady && pair.value;
		}
		return allReady;
	}

	public void removePlayer(ServerWebSocket webSocket) {
		Pair<PlayerColor, Boolean> value = players.remove(webSocket);
		if(value != null) {
			positions.remove(value.key);
		}
	}

	public void setState(PartyState state) {
		this.state = state;
	}

	public PartyState getState() {
		return state;
	}

	public Map<ServerWebSocket, Pair<PlayerColor, Boolean>> getPlayers() {
		return players;
	}
}
