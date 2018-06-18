package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Assets;

public class Bullet extends Entity {
	private final World world;
	private final Vector2 size;
	private Vector2 direction;
	private Body body;
	private boolean move = true;

	public Bullet(World world, float x, float y, float angle, Vector2 direction) {
		this.world = world;
		this.direction = direction;
		this.size = new Vector2(2f, 1f);
		this.body = PhysicManager.createBox(x, y, 2f, 1f, angle, Constants.BULLETS_CATEGORY, Constants.BULLETS_MASK, false, this, world);
		this.body.setBullet(true);

		direction.scl(Constants.BULLET_SPEED);
	}

	@Override
	public void update(float delta) {
		if(move) {
			body.setLinearVelocity(direction);
		}
		else {
			body.setLinearVelocity(Vector2.Zero);
			body.setTransform(body.getPosition().x, body.getPosition().y, body.getAngle()+delta);
		}
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		if(!move) {
			Color saveColor = batch.getColor();
			batch.setColor(Constants.BULLET_PLAYER1_HALO);
			batch.draw(assetManager.get(Assets.GRADIENT.filename, Texture.class), body.getPosition().x - 2f/2f, body.getPosition().y - 1f/2f - 3f, 2f/2f, 1f/2f,
					2f, 16f, 1f, 1f, 0f, 0, 0, 2, 16, false, false);
			batch.setColor(saveColor);
		}
		batch.draw(assetManager.get(Assets.PLAYER1_BULLET.filename, Texture.class), body.getPosition().x - 2f/2f, body.getPosition().y - 1f/2f, 2f/2f, 1f/2f,
				2f, 1f, 1f, 1f, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 2, 1, false, false);
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Assets.PLAYER_BULLET_SHADOW.filename, Texture.class), body.getPosition().x - 2f/2f, body.getPosition().y - 1f/2f - 3f, 2f/2f, 1f/2f,
				2f, 1f, 1f, 1f, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 2, 1, false, false);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Entity) {
			Entity e = (Entity)o;
			return -Float.compare(body.getPosition().y - size.y/2f, e.getPosition().y - e.getSize().y/2f);
		}
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public boolean isMove() {
		return move;
	}
}
