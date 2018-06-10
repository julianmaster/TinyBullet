package com.tinybullet.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.Arena;
import com.tinybullet.game.model.Entity;
import com.tinybullet.game.model.Pillar;
import com.tinybullet.game.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainScreen extends ScreenAdapter {
	private final TinyBullet game;

	private Arena arena;
	private List<Entity> entities = new ArrayList<>();

	// Box2D
	private World world;
	private boolean showDebugPhysics = false;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public MainScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		world = new World(new Vector2(), false);
		arena = new Arena(world);

		entities.add(new Player(world));
		entities.add(new Pillar(world, Assets.PILLAR1, 19,43, 8, 7));
		entities.add(new Pillar(world, Assets.PILLAR1, 45,21, 8, 7));
		entities.add(new Pillar(world, Assets.PILLAR2, 17,21, 12, 7));
		entities.add(new Pillar(world, Assets.PILLAR2, 47,43, 12, 7));
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		AssetManager assetManager = game.getAssetManager();

		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showDebugPhysics = !showDebugPhysics;
		}

		for(Entity entity : entities) {
			entity.update(delta);
		}
		Collections.sort(entities);

		batch.begin();
		batch.draw(game.getAssetManager().get(Assets.GROUND.filename, Texture.class), 0, 0);
		batch.draw(game.getAssetManager().get(Assets.WALL1.filename, Texture.class), 0, 0);
		for(Entity entity : entities) {
			entity.render(batch, assetManager);
		}
		batch.draw(game.getAssetManager().get(Assets.WALL2.filename, Texture.class), 0, 0);
		batch.end();

		if(showDebugPhysics) {
			debugRenderer.render(world, game.getCamera().combined);
		}

		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose() {
	}
}
