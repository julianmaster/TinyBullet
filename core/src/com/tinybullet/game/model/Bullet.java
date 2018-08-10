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
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.network.json.client.RequestFireBulletJson;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Asset;

public class Bullet extends Entity {

	private final TinyBullet game;
	private final World world;

	private BulletColor color;
	private Vector2 direction = null;
	private final Vector2 size;
	private Body body = null;
	private boolean lock = false;
	private boolean move = false;
	private boolean dropped = false;
	private boolean sourceOfFire = false;
	private boolean playerFire = false;

	public Bullet(BulletColor color, World world, TinyBullet game) {
		this.game = game;
		this.world = world;
		this.color = color;
		this.size = new Vector2(2f, 1f);
	}

	public void fire(Vector2 position, float angle, Vector2 direction, boolean playerFire) {
		this.playerFire = playerFire;
		if(body == null) {
			this.body = PhysicManager.createBox(position.x, position.y, 2f, 1f, angle, Constants.BULLETS_CATEGORY, Constants.BULLETS_MASK, false, true, this, world);
		}
		body.setTransform(position.x, position.y, angle);
		this.direction = direction;
		this.direction.scl(Constants.BULLET_SPEED);
		move = true;
		dropped = false;
		game.getGameScreen().getEntitiesToAdd().add(this);

		RequestFireBulletJson requestFireBulletJson = new RequestFireBulletJson();
		requestFireBulletJson.position = body.getPosition();
		requestFireBulletJson.color = color;
		requestFireBulletJson.angle = angle;
		requestFireBulletJson.direction = direction;
		game.getClient().send(requestFireBulletJson);
	}

	public void pickUp() {
		dropped = false;
		lock = true;
		game.getGameScreen().getBodiesToRemove().add(body);
		game.getGameScreen().getEntities().remove(this);
		body = null;
	}

	@Override
	public void update(float delta) {
		if(move && !dropped) {
			body.setLinearVelocity(direction);
		}
		else if(dropped) {
			body.setLinearVelocity(Vector2.Zero);
			body.setTransform(body.getPosition().x, body.getPosition().y, body.getAngle()+delta);
		}
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		if(!move && dropped) {
			Color saveColor = batch.getColor();
			batch.setColor(color.halo);
			batch.draw(assetManager.get(Asset.GRADIENT.filename, Texture.class), body.getPosition().x - 2f/2f, body.getPosition().y - 1f/2f - 3f, 2f/2f, 1f/2f,
					2f, 16f, 1f, 1f, 0f, 0, 0, 2, 16, false, false);
			batch.setColor(saveColor);
		}
		if(move || dropped) {
			batch.draw(assetManager.get(color.bullet.filename, Texture.class), body.getPosition().x - 2f/2f, body.getPosition().y - 1f/2f, 2f/2f, 1f/2f,
					2f, 1f, 1f, 1f, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 2, 1, false, false);
		}
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {
		if(move || dropped) {
			batch.draw(assetManager.get(Asset.PLAYER_BULLET_SHADOW.filename, Texture.class), body.getPosition().x - 2f / 2f, body.getPosition().y - 1f / 2f - 3f, 2f / 2f, 1f / 2f,
					2f, 1f, 1f, 1f, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 2, 1, false, false);
		}
	}

	@Override
	public int compareTo(Object o) {
		if(body != null && o instanceof Entity) {
			Entity e = (Entity)o;
			return -Float.compare(body.getPosition().y - size.y/2f, e.getPosition().y - e.getSize().y/2f);
		}
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		if(body != null) {
			return body.getPosition();
		}
		return Vector2.Zero;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public BulletColor getColor() {
		return color;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public boolean isMove() {
		return move;
	}

	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}

	public boolean isDropped() {
		return dropped;
	}

	public boolean isSourceOfFire() {
		return sourceOfFire;
	}

	public void setSourceOfFire(boolean sourceOfFire) {
		this.sourceOfFire = sourceOfFire;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Body getBody() {
		return body;
	}
}
