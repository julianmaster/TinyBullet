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
	public static final short PLAYER_WALLS_CATEGORY = 2;
	public static final short BULLETS_CATEGORY = 4;
	public static final short BULLETS_WALLS_CATEGORY = 8;
	public static final short BULLETS_PLAYER_CATEGORY = 16;
	public static final short BULLETS_DROPPED_CATEGORY = 32;

	public static final short PLAYER_MASK = PLAYER_CATEGORY | PLAYER_WALLS_CATEGORY;
	public static final short WALLS_MASK = PLAYER_CATEGORY;

	public static final short BULLETS_MASK = BULLETS_WALLS_CATEGORY;
	public static final short BULLETS_DROPPED_MASK = BULLETS_WALLS_CATEGORY | BULLETS_PLAYER_CATEGORY;
	public static final short BULLETS_WALLS_MASK = BULLETS_CATEGORY;
	public static final short BULLETS_PLAYERS_MASK = BULLETS_CATEGORY | BULLETS_DROPPED_CATEGORY;

	public static final Color BULLET_PLAYER1_HALO = new Color(0xc42c36b4);
	public static final Color BULLET_PLAYER2_HALO = new Color(0x7bcf5cb4);
}
