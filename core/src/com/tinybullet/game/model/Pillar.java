package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;
import com.tinybullet.game.view.Asset;

public class Pillar extends Entity {

	private final World world;
	private final Asset asset;
	private final Asset assetShadow;
	private final Rectangle rectangle;
	private final Vector2 size;
	private Body body;
	private Body bulletCollisionBody;

	public Pillar(World world, Asset asset, Asset assetShadow, float x, float y, float width, float height) {
		this.world = world;
		this.asset = asset;
		this.assetShadow = assetShadow;
		this.rectangle = new Rectangle(x - width/2, y - height/2, width, height);
		this.size = new Vector2(width, height);
		this.body = PhysicManager.createBox(x, y, width, height, 0, Constants.PLAYER_WALL_CATEGORY, Constants.WALL_MASK, true, false, this, world);
		this.bulletCollisionBody = PhysicManager.createBox(x, y - 3f, width, height - 2f, 0, Constants.BULLET_WALL_CATEGORY, Constants.BULLET_WALL_MASK, true, false, this, world);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(asset.filename, Texture.class), body.getPosition().x - Constants.WALL_COLLISION_WIDTH_OFFSET - rectangle.width / 2f,
				body.getPosition().y - Constants.WALL_COLLISION_HEIGHT_OFFSET - rectangle.height / 2f);
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(assetShadow.filename, Texture.class), body.getPosition().x - Constants.WALL_COLLISION_WIDTH_OFFSET - rectangle.width / 2f,
				body.getPosition().y - Constants.WALL_COLLISION_HEIGHT_OFFSET - rectangle.height / 2f);
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
}
