package com.tinybullet.game;

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
	public static final float PLAYER_SPEED = 32f;

	public static final short BULLET_CATEGORY_MASK = 1;
	public static final short PLAYER_CATEGORY_MASK = 2;
	public static final short WALLS_CATEGORY_MASK = 4;

	public static final short BULLET_MASK = WALLS_CATEGORY_MASK;
	public static final short PLAYER_MASK = PLAYER_CATEGORY_MASK | WALLS_CATEGORY_MASK;
	public static final short WALLS_MASK = BULLET_CATEGORY_MASK | PLAYER_CATEGORY_MASK;
}
