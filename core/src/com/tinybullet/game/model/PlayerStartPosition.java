package com.tinybullet.game.model;

import com.badlogic.gdx.math.Vector2;

public enum PlayerStartPosition {
	RED(new Vector2(16f, 14f)),
	GREEN(new Vector2(48f, 52f)),
	YELLOW(new Vector2(0f, 0f)),
	PURPLE(new Vector2(0f, 0f));

	// TODO Finish to set player start position

	public Vector2 position;

	PlayerStartPosition(Vector2 position) {
		this.position = position;
	}
}
