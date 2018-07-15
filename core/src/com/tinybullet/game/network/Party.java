package com.tinybullet.game.network;

import com.github.czyzby.websocket.WebSocket;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.client.PlayerInfoJson;
import com.tinybullet.game.network.json.server.ResponseJoinPartyJson;

import java.util.HashMap;
import java.util.Map;

public class Party {
	private boolean change = false;
	private PartyState state = PartyState.WAIT_START;
	private Map<PlayerColor, PlayerPosition> positions = new HashMap<>();
	private Map<PlayerColor, WebSocket> websockets = new HashMap<>();
//	private Map<BulletColor, PlayerColor> bulletTaked = new HashMap<>();

	public ResponseJoinPartyJson addPlayer() {
		ResponseJoinPartyJson responseJoinPartyJson;

		if(!positions.containsKey(PlayerColor.RED)) {
			responseJoinPartyJson = initPlayerPosition(PlayerColor.RED, PlayerStartPosition.RED);
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			responseJoinPartyJson = initPlayerPosition(PlayerColor.GREEN, PlayerStartPosition.GREEN);
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			responseJoinPartyJson = initPlayerPosition(PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
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

	public void changePlayerPosition(PlayerInfoJson playerInfoJson) {
		PlayerPosition playerPosition = positions.get(playerInfoJson.playerColor);
		playerPosition.x = playerInfoJson.x;
		playerPosition.y = playerInfoJson.y;
		change = true;
	}

	public boolean isChange() {
		return change;
	}

	public void setState(PartyState state) {
		this.state = state;
	}

	public PartyState getState() {
		return state;
	}
}
