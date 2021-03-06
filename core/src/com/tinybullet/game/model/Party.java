package com.tinybullet.game.model;

import com.badlogic.gdx.math.Vector2;
import com.tinybullet.game.network.json.server.ResponsePartyStateJson;
import com.tinybullet.game.network.json.server.ResponsePositionsPlayersPartyJson;
import com.tinybullet.game.util.InvertedIntegerComparator;
import com.tinybullet.game.util.MapUtil;
import com.tinybullet.game.util.Pair;
import io.vertx.core.http.ServerWebSocket;

import java.util.*;

public class Party {
	private State state = State.PARTY;
	private Map<ServerWebSocket, Pair<PlayerColor, Boolean>> players = new HashMap<>();
	private Map<PlayerColor, Vector2> positions = new HashMap<>();
	private Map<PlayerColor, BulletColor> bulletsTaked = new HashMap<>();
	private Map<PlayerColor, Integer> scores = new LinkedHashMap<>();
	private List<PlayerColor> playerOrder = new LinkedList<>();

	public boolean addPlayer(ServerWebSocket webSocket) {
		if(state != State.PARTY) {
			return false;
		}

		if(!positions.containsKey(PlayerColor.RED)) {
			initPlayerInParty(webSocket, PlayerColor.RED, PlayerStartPosition.RED);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			initPlayerInParty(webSocket, PlayerColor.GREEN, PlayerStartPosition.GREEN);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			initPlayerInParty(webSocket, PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
			return true;
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
			initPlayerInParty(webSocket, PlayerColor.PURPLE, PlayerStartPosition.PURPLE);
			return true;
		}
		return false;
	}

	private void initPlayerInParty(ServerWebSocket webSocket, PlayerColor playerColor, PlayerStartPosition playerStartPosition) {
		players.put(webSocket, new Pair<>(playerColor, false));
		scores.put(playerColor, 0);
		initPlayerPosition(playerColor, playerStartPosition);
	}

	private void initPlayerPosition(PlayerColor playerColor, PlayerStartPosition playerStartPosition) {
		Vector2 playerPosition = new Vector2(playerStartPosition.position.x, playerStartPosition.position.y);
		positions.put(playerColor, playerPosition);
	}

	public void restartParty() {
		if(!positions.containsKey(PlayerColor.RED)) {
			initPlayerPosition(PlayerColor.RED, PlayerStartPosition.RED);
		}
		else if(!positions.containsKey(PlayerColor.GREEN)) {
			initPlayerPosition(PlayerColor.GREEN, PlayerStartPosition.GREEN);
		}
		else if(!positions.containsKey(PlayerColor.YELLOW)) {
			initPlayerPosition(PlayerColor.YELLOW, PlayerStartPosition.YELLOW);
		}
		else if(!positions.containsKey(PlayerColor.PURPLE)) {
			initPlayerPosition(PlayerColor.PURPLE, PlayerStartPosition.PURPLE);
		}
	}

	public ResponsePositionsPlayersPartyJson waitStartParty() {
		state = State.WAIT_START;
		bulletsTaked.put(PlayerColor.RED, BulletColor.RED);
		bulletsTaked.put(PlayerColor.GREEN, BulletColor.GREEN);
		bulletsTaked.put(PlayerColor.YELLOW, BulletColor.YELLOW);
		bulletsTaked.put(PlayerColor.PURPLE, BulletColor.PURPLE);
		ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = new ResponsePositionsPlayersPartyJson();
		responsePositionsPlayersPartyJson.playerColors = positions.keySet().toArray(new PlayerColor[positions.size()]);
		responsePositionsPlayersPartyJson.positions = positions.values().toArray(new Vector2[positions.size()]);
		return responsePositionsPlayersPartyJson;
	}

	public ResponsePartyStateJson startParty() {
		state = State.PLAY;
		ResponsePartyStateJson responsePartyStateJson = new ResponsePartyStateJson();
		responsePartyStateJson.state = state;
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

	public boolean fireBullet(ServerWebSocket webSocket, BulletColor color) {
		if(bulletsTaked.get(players.get(webSocket).key) != null && bulletsTaked.get(players.get(webSocket).key) == color) {
			bulletsTaked.remove(players.get(webSocket).key);
			return true;
		}
		return false;
	}

	public boolean isFiredBullet(BulletColor color) {
		return !bulletsTaked.values().contains(color);
	}

	public boolean pickUpBullet(ServerWebSocket webSocket, BulletColor color) {
		if(!bulletsTaked.values().contains(color)) {
			bulletsTaked.put(players.get(webSocket).key, color);
			return true;
		}
		return false;
	}

	public void playerDie(PlayerColor playerColor) {
		playerOrder.add(playerColor);
		if(playerOrder.size() == players.size() - 1) {
			state = State.SCORE;

			List<PlayerColor> lastPlayerSearch = new ArrayList<>(scores.keySet());
			lastPlayerSearch.removeAll(playerOrder);
			playerOrder.addAll(lastPlayerSearch);

			for(int i = 0; i < playerOrder.size(); i++) {
				scores.put(playerOrder.get(i), scores.get(playerOrder.get(i))+i);
			}

			MapUtil.orderByValue((LinkedHashMap) scores, new InvertedIntegerComparator());

			for(Map.Entry<PlayerColor, Integer> score : scores.entrySet()) {
				if(score.getValue() >= 10) {
					state = State.MENU;
				}
			}
		}
	}

	public void removePlayer(ServerWebSocket webSocket) {
		Pair<PlayerColor, Boolean> value = players.remove(webSocket);
		if(value != null) {
			positions.remove(value.key);
		}
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public Map<ServerWebSocket, Pair<PlayerColor, Boolean>> getPlayers() {
		return players;
	}

	public Map<PlayerColor, Integer> getScores() {
		return scores;
	}
}
