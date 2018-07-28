package com.tinybullet.game.network.json.server;

import com.tinybullet.game.model.PlayerColor;

public class ResponsePlayerStatusPartyJson {
	public boolean join;
	public PlayerColor[] players;
	public boolean[] readies;
	public PlayerColor playerColor;
}
