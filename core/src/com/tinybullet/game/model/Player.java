package com.tinybullet.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Assets;

public class Player extends Entity {

	private final World world;
	private Body body;

	public Player(World world) {
		this.world = world;
		this.body = PhysicManager.createBox(10, 10, Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT, false, world);
	}

	@Override
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

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Assets.PLAYER1.filename, Texture.class), body.getPosition().x - Constants.COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				body.getPosition().y - Constants.COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Entity) {
			Entity e = (Entity)o;
			return -Float.compare(body.getPosition().y, e.getPosition().y);
		}
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	public Body getBody() {
		return body;
	}
}
