package com.tinybullet.game.view;

public enum Asset {

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
	PLAYER3("player3.png"),
	PLAYER4("player4.png"),
	PLAYER_SHADOW("player_shadow.png"),
	PLAYER1_BULLET("player1_bullet.png"),
	PLAYER2_BULLET("player2_bullet.png"),
	PLAYER3_BULLET("player3_bullet.png"),
	PLAYER4_BULLET("player4_bullet.png"),
	PLAYER_BULLET_SHADOW("player_bullet_shadow.png"),
	GRADIENT("gradient.png"),

	// Font
	FONT("TinyFont.fnt"),

	// Test
	TEST("badlogic.jpg");

	public String filename;

	Asset(String filename) {
		this.filename = filename;
	}
}
