package com.tinybullet.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.*;
import com.tinybullet.game.physic.EntityContactListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainScreen extends ScreenAdapter {
	private final TinyBullet game;

	private Arena arena;
	private List<Entity> entities = new ArrayList<>();
	private List<Entity> newEntities = new ArrayList<>();
	private List<Entity> entitiesForRemoval = new ArrayList<>();

	private Bullet redBullet;
	private Bullet greenBullet;

	// Box2D
	private World world;
	private List<Body> bodiesScheduledForRemoval = new ArrayList<>();
	private boolean showDebugPhysics = true;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public MainScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		world = new World(new Vector2(), false);
		world.setContactListener(new EntityContactListener(this, world));
		arena = new Arena(world);

		Player redPlayer = new Player(this, world);
		entities.add(redPlayer);
		entities.add(new Pillar(world, Assets.PILLAR1, Assets.PILLAR1_SHADOW, 19,43, 8, 6));
		entities.add(new Pillar(world, Assets.PILLAR1, Assets.PILLAR1_SHADOW, 45,21, 8, 6));
		entities.add(new Pillar(world, Assets.PILLAR2, Assets.PILLAR2_SHADOW, 17,21, 12, 6));
		entities.add(new Pillar(world, Assets.PILLAR2, Assets.PILLAR2_SHADOW, 47,43, 12, 6));

		redBullet = new Bullet(world);
		greenBullet = new Bullet(world);

		redPlayer.setBullet(redBullet);

		entities.add(redBullet);
		entities.add(greenBullet);
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		AssetManager assetManager = game.getAssetManager();

		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showDebugPhysics = !showDebugPhysics;
		}

		entities.removeAll(entitiesForRemoval);
		entitiesForRemoval.clear();
		for(Entity entity : entities) {
			entity.update(delta);
		}
		entities.addAll(newEntities);
		newEntities.clear();

		Collections.sort(entities);

		batch.begin();
		// Ground
		batch.draw(game.getAssetManager().get(Assets.GROUND.filename, Texture.class), 0, 0);

		// Shadows
		batch.draw(game.getAssetManager().get(Assets.WALL1_SHADOW.filename, Texture.class), 0, 0);
		for(Entity entity : entities) {
			entity.renderShadow(batch, assetManager);
		}
		batch.draw(game.getAssetManager().get(Assets.WALL2_SHADOW.filename, Texture.class), 0, 0);

		// Entities
		batch.draw(game.getAssetManager().get(Assets.WALL1.filename, Texture.class), 0, 0);
		for(Entity entity : entities) {
			entity.render(batch, assetManager);
		}
		batch.draw(game.getAssetManager().get(Assets.WALL2.filename, Texture.class), 0, 0);
		batch.end();

		if(showDebugPhysics) {
			debugRenderer.render(world, game.getCamera().combined);
		}

		for(Body body : bodiesScheduledForRemoval) {
			world.destroyBody(body);
		}
		bodiesScheduledForRemoval.clear();
		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose() {
		world.dispose();
	}

	public TinyBullet getGame() {
		return game;
	}

	public List<Entity> getNewEntities() {
		return newEntities;
	}

	public List<Entity> getEntitiesForRemoval() {
		return entitiesForRemoval;
	}

	public List<Body> getBodiesScheduledForRemoval() {
		return bodiesScheduledForRemoval;
	}
}
