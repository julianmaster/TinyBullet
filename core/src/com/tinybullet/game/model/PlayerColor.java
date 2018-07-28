package com.tinybullet.game.model;

import com.tinybullet.game.view.Asset;

public enum PlayerColor {
	RED(Asset.PLAYER1, "RED1"),
	GREEN(Asset.PLAYER2, "GREEN1"),
	YELLOW(Asset.PLAYER3, "YELLOW2"),
	PURPLE(Asset.PLAYER4, "PURPLE1");

	public Asset player;
	public String color;

	PlayerColor(Asset player, String color) {
		this.player = player;
		this.color = color;
	}

	public PlayerColor[] order() {
		return new PlayerColor[]{RED, GREEN, YELLOW, PURPLE};
	}
}
