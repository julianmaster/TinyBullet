package com.tinybullet.game.view;

public enum Assets {

	// Assets
	GROUND("ground.png"),
	WALL1("wall1.png"),
	WALL1_SHADOW("wall1_shadow.png"),
	WALL2("wall2.png"),
	WALL2_SHADOW("wall2_shadow.png"),
	PILLAR1("pillar1.png"),
	PILLAR1_SHADOW("pillar1_shadow.png"),
	PILLAR2("pillar2.png"),
	PILLAR2_SHADOW("pillar2_shadow.png"),
	PLAYER1("player1.png"),
	PLAYER2("player2.png"),
	PLAYER_SHADOW("player_shadow.png"),

	// Test
	TEST("badlogic.jpg");

	public String filename;

	Assets(String filename) {
		this.filename = filename;
	}
}
