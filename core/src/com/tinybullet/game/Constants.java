package com.tinybullet.game;

public class Constants {
	public static final int CAMERA_WIDTH = 64;
	public static final int CAMERA_HEIGHT = 64;
	public static final int WINDOM_ZOOM = 8;
	public static final int WINDOW_WIDTH = CAMERA_WIDTH * WINDOM_ZOOM;
	public static final int WINDOW_HEIGHT = CAMERA_HEIGHT * WINDOM_ZOOM;

	public static final float COLLISION_WIDTH_OFFSET = 0f;
	public static final float COLLISION_HEIGHT_OFFSET = 5f;

	public static final float PLAYER_COLLISION_WIDTH = 5f;
	public static final float PLAYER_COLLISION_HEIGHT = 1f;
	public static final float PLAYER_SPEED = 30f;
}
