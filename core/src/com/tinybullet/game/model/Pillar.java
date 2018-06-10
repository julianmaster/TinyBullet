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
import com.tinybullet.game.view.Assets;

public class Pillar extends Entity {

	private final World world;
	private final Assets assets;
	private Body body;

	public Pillar(World world, Assets assets, float x, float y, float width, float height) {
		this.world = world;
		this.assets = assets;
		this.body = PhysicManager.createBox(x, y, width, height, true, world);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		Rectangle rectangle = (Rectangle) body.getUserData();
		batch.draw(assetManager.get(assets.filename, Texture.class), body.getPosition().x - Constants.COLLISION_WIDTH_OFFSET - rectangle.width / 2f,
				body.getPosition().y - Constants.COLLISION_HEIGHT_OFFSET - rectangle.height / 2f);
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
