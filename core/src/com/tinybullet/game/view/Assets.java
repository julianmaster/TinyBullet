package com.tinybullet.game.view;

public enum Assets {

	// Assets
	GROUND("ground.png"),
	WALL1("wall1.png"),
	WALL2("wall2.png"),
	PILLAR1("pillar1.png"),
	PILLAR2("pillar2.png"),
	PLAYER1("player1.png"),
	PLAYER2("player2.png"),

	// Test
	TEST("badlogic.jpg");

	public String filename;

	Assets(String filename) {
		this.filename = filename;
	}
}
