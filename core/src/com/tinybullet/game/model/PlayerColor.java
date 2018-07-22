package com.tinybullet.game.model;

import com.tinybullet.game.view.Asset;

public enum PlayerColor {
	RED(Asset.PLAYER1),
	GREEN(Asset.PLAYER2),
	YELLOW(Asset.PLAYER3),
	PURPLE(Asset.PLAYER4);

	public Asset player;

	PlayerColor(Asset player) {
		this.player = player;
	}

	public PlayerColor[] order() {
		return new PlayerColor[]{RED, GREEN, YELLOW, PURPLE};
	}
}
