package com.tinybullet.game.network.json.server;

import com.badlogic.gdx.math.Vector2;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.PlayerPosition;

import java.util.Map;

public class ResponsePositionsPlayersPartyJson {
	public Map<PlayerColor, Vector2> positions;
}
