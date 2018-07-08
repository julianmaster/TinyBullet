package com.tinybullet.game.network;

import com.github.czyzby.websocket.WebSocket;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.PlayerInfoJson;

import java.util.HashMap;
import java.util.Map;

public class Party {
	private boolean change = false;
	private PartyState state = PartyState.INIT;
	private Map<PlayerColor, PlayerPosition> positions = new HashMap<>();
	private Map<PlayerColor, WebSocket> websockets = new HashMap<>();
//	private Map<BulletColor, PlayerColor> bulletTaked = new HashMap<>();

	public PlayerInfoJson addPlayer() {
		if(!positions.containsKey(PlayerColor.RED)) {
			return initPlayerPosition(PlayerColor.RED, PlayerStartPosition.RED);
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			return initPlayerPosition(PlayerColor.GREEN, PlayerStartPosition.GREEN);
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			return initPlayerPosition(PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
			return initPlayerPosition(PlayerColor.PURPLE, PlayerStartPosition.PURPLE);
		}
		return null;
	}

	private PlayerInfoJson initPlayerPosition(PlayerColor playerColor, PlayerStartPosition playerStartPosition) {
		PlayerPosition playerPosition = new PlayerPosition(playerStartPosition.position.x, playerStartPosition.position.y);
		positions.put(playerColor, playerPosition);

		PlayerInfoJson playerInfoJson = new PlayerInfoJson();
		playerInfoJson.playerColor = playerColor;
		playerInfoJson.x = playerPosition.x;
		playerInfoJson.y = playerPosition.y;
		return playerInfoJson;
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
