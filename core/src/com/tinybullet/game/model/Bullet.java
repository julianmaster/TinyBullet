package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.physic.PhysicManager;

public class Bullet extends Entity {
	private final World world;
	private Vector2 direction;
	private Body body;

	public Bullet(World world, float x, float y, float angle, Vector2 direction) {
		this.world = world;
		this.direction = direction;
		this.body = PhysicManager.createBox(x, y, 2, 2, 0, false, world);
	}

	@Override
	public void update(float delta) {
		body.setLinearVelocity(direction);
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		System.out.println(body.getPosition());
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
