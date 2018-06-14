package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Assets;

public class Bullet extends Entity {
	private final World world;
	private Vector2 direction;
	private Body body;

	public Bullet(World world, float x, float y, float angle, Vector2 direction) {
		this.world = world;
		this.direction = direction;
		this.body = PhysicManager.createBox(x, y, 2, 1, angle, Constants.BULLET_CATEGORY_MASK, Constants.BULLET_MASK, false, world);
	}

	@Override
	public void update(float delta) {
		body.setLinearVelocity(direction);
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Assets.PLAYER1_BULLET.filename, Texture.class), body.getPosition().x, body.getPosition().y);
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {

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
}
