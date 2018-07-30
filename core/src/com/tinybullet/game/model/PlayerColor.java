package com.tinybullet.game.model;

import com.tinybullet.game.view.Asset;

public enum PlayerColor {
	RED(Asset.PLAYER1, "RED1", BulletColor.RED),
	GREEN(Asset.PLAYER2, "GREEN1", BulletColor.GREEN),
	YELLOW(Asset.PLAYER3, "YELLOW2", BulletColor.YELLOW),
	PURPLE(Asset.PLAYER4, "PURPLE1", BulletColor.PURPLE);

	public Asset player;
	public String color;
	public BulletColor initBullet;

	PlayerColor(Asset player, String color, BulletColor initBullet) {
		this.player = player;
		this.color = color;
		this.initBullet = initBullet;
	}

	public PlayerColor[] order() {
		return new PlayerColor[]{RED, GREEN, YELLOW, PURPLE};
	}
}
