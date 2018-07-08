package com.tinybullet.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.network.PlayerPosition;
import com.tinybullet.game.network.json.PlayerInfoJson;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Asset;
import com.tinybullet.game.view.MainScreen;

public class Player extends Entity {

	private final MainScreen screen;
	private final World world;
	private final Vector2 size;
	private final PlayerPosition playerPosition;
	private Body body;
	private Body bulletCollisionBody;

	private Bullet bullet;

	public Player(MainScreen screen, World world) {
		this.screen = screen;
		this.world = world;
		this.size = new Vector2(Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT);
		this.body = PhysicManager.createBox(48, 52, Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT, 0, Constants.PLAYER_CATEGORY, Constants.PLAYER_MASK, false, this, world);
		body.setLinearVelocity((float)0f, (float)0f);
		this.bulletCollisionBody = PhysicManager.createBox(48, 52 - 2f, Constants.PLAYER_COLLISION_WIDTH, 5f, 0, Constants.BULLETS_PLAYER_CATEGORY, Constants.BULLETS_PLAYERS_MASK, false, this, world);
		this.playerPosition = new PlayerPosition(48, 52);
	}

	@Override
	public void update(float delta) {

		if(Gdx.input.justTouched() && bullet != null) {
			Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
			Vector3 worldCoords = screen.getGame().getCamera().unproject(screenCoords);

			float angle = MathUtils.atan2(worldCoords.y - body.getPosition().y + 3f, worldCoords.x - body.getPosition().x);
			Vector2 direction = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));

			bullet.fire(body.getPosition().x, body.getPosition().y - 3f, angle, direction);
			bullet = null;
		}



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
		bulletCollisionBody.setTransform(body.getPosition().x, body.getPosition().y - 2f, 0f);
		if(y != 0.0f || x != 0.0f) {
			playerPosition.x = body.getPosition().x;
			playerPosition.y = body.getPosition().y;
//			screen.getClient().send(playerPosition);
		}
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Asset.PLAYER1.filename, Texture.class), body.getPosition().x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				body.getPosition().y - Constants.PLAYER_COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Asset.PLAYER_SHADOW.filename, Texture.class), body.getPosition().x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				body.getPosition().y - Constants.PLAYER_COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
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

	public void setPosition(PlayerInfoJson info) {
		System.out.println(info.x+" - "+info.y);
		body.setTransform(info.x, info.y, 0f);
		bulletCollisionBody.setTransform(body.getPosition().x, body.getPosition().y - 2f, 0f);
	}

	public void setColor() {

	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public Body getBody() {
		return body;
	}

	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}

	public Bullet getBullet() {
		return bullet;
	}
}
