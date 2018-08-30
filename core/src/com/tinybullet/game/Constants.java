package com.tinybullet.game;

import com.badlogic.gdx.graphics.Color;

public class Constants {
	public static final int CAMERA_WIDTH = 64;
	public static final int CAMERA_HEIGHT = 64;
	public static final int WINDOM_ZOOM = 8;
	public static final int WINDOW_WIDTH = CAMERA_WIDTH * WINDOM_ZOOM;
	public static final int WINDOW_HEIGHT = CAMERA_HEIGHT * WINDOM_ZOOM;

	public static final float WALL_COLLISION_WIDTH_OFFSET = 2f;
	public static final float WALL_COLLISION_HEIGHT_OFFSET = 8f;
	public static final float PLAYER_COLLISION_WIDTH_OFFSET = 0f;
	public static final float PLAYER_COLLISION_HEIGHT_OFFSET = 6f;

	public static final float PLAYER_COLLISION_WIDTH = 5f;
	public static final float PLAYER_COLLISION_HEIGHT = 1f;
	public static final float PLAYER_SPEED = 48f;

	public static final float BULLET_SPEED = 128f;
//	public static final float BULLET_SPEED = 1f;

	public static final short PLAYER_CATEGORY = 1;
	public static final short OTHER_PLAYER_CATEGORY = 2;
	public static final short PLAYER_WALL_CATEGORY = 4;
	public static final short BULLET_MOVE_CATEGORY = 8;
	public static final short OTHER_BULLET_CATEGORY = 16;
	public static final short BULLET_DROPPED_CATEGORY = 32;
	public static final short BULLET_WALL_CATEGORY = 64;
	public static final short BULLET_PLAYER_CATEGORY = 128;
	public static final short BULLET_PLAYER_WITHOUT_BULLET_CATEGORY = 256;

	public static final short PLAYER_MASK = OTHER_PLAYER_CATEGORY | PLAYER_WALL_CATEGORY | OTHER_BULLET_CATEGORY;
	public static final short OTHER_PLAYER_MASK = PLAYER_CATEGORY;
	public static final short WALL_MASK = PLAYER_CATEGORY;

	public static final short BULLET_MOVE_MASK = BULLET_WALL_CATEGORY;
	public static final short OTHER_BULLET_MOVE_MASK = BULLET_WALL_CATEGORY | PLAYER_CATEGORY;
	public static final short BULLET_DROPPED_MASK = BULLET_WALL_CATEGORY | BULLET_PLAYER_WITHOUT_BULLET_CATEGORY;
	public static final short BULLET_WALL_MASK = BULLET_MOVE_CATEGORY | OTHER_BULLET_CATEGORY | BULLET_DROPPED_CATEGORY;
	public static final short BULLET_PLAYER_MASK = BULLET_MOVE_CATEGORY | OTHER_BULLET_CATEGORY;
	public static final short BULLET_PLAYER_WITHOUT_BULLET_MASK = BULLET_MOVE_CATEGORY | OTHER_BULLET_CATEGORY | BULLET_DROPPED_CATEGORY;

	public static final Color BULLET_PLAYER1_HALO = new Color(0xc42c36b4);
	public static final Color BULLET_PLAYER2_HALO = new Color(0x7bcf5cb4);
	public static final Color BULLET_PLAYER3_HALO = new Color(0xfbdf6bb4);
	public static final Color BULLET_PLAYER4_HALO = new Color(0xe38dd6b4);

	// Network
	public static final int PORT = 8465;
	public static final String HOST = "localhost";
}
