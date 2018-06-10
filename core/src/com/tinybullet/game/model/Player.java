package com.tinybullet.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;

public class Player {

	private final World world;
	private Body body;

	public Player(World world) {
		this.world = world;
		this.body = PhysicManager.createBox(10, 10, Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT, false, world);
	}

	public void update(float delta) {
		float y = 0f, x = 0f;
		if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
			y += 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			y -= 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			x -= 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			x += 1f;
		}

		if(x != 0f && y != 0f) {
			x *= 0.7f;
			y *= 0.7f;
		}

		body.setLinearVelocity((float)x * Constants.PLAYER_SPEED, (float)y * Constants.PLAYER_SPEED);
	}

	public Body getBody() {
		return body;
	}
}
