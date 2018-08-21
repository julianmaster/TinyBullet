package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Asset;

public class OtherPlayer extends Entity {

	private final TinyBullet game;

	private final PlayerColor color;
	private final Vector2 size;
	private Body body;

	public OtherPlayer(PlayerColor color, Vector2 position, TinyBullet game) {
		this.color = color;
		this.game = game;
		this.size = new Vector2(Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT);
		this.body = PhysicManager.createBox(position.x, position.y, Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT, 0, Constants.OTHERS_PLAYER_CATEGORY, Constants.OTHERS_PLAYER_MASK, true, false, this, game.getGameScreen().getWorld());
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(color.player.filename, Texture.class), body.getPosition().x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
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

	public void die() {
		game.getGameScreen().getBodiesToRemove().add(body);
		game.getGameScreen().getEntities().remove(this);
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	public void setPosition(Vector2 position) {
		body.setTransform(position.x, position.y, 0f);
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public Body getBody() {
		return body;
	}
}
