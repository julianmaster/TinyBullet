package com.tinybullet.game.network;

public enum PlayerStartPosition {
	RED(new PlayerPosition(16, 14)),
	GREEN(new PlayerPosition(48, 52)),
	YELLOW(new PlayerPosition(0, 0)),
	PURPLE(new PlayerPosition(0, 0));

	public PlayerPosition position;

	PlayerStartPosition(PlayerPosition position) {
		this.position = position;
	}
}
