package com.tinybullet.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.Arena;
import com.tinybullet.game.model.Player;

public class MainScreen extends ScreenAdapter {
	private final TinyBullet game;

	private Arena arena;
	private Player player;

	// Box2D
	private World world;
	private boolean showDebugPhysics = true;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public MainScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		world = new World(new Vector2(), false);
		arena = new Arena(world);
		player = new Player(world);
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();

		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showDebugPhysics = !showDebugPhysics;
		}

		player.update(delta);

		batch.begin();
		batch.draw(game.getAssetManager().get(Assets.GROUND.filename, Texture.class), 0, 0);
		batch.draw(game.getAssetManager().get(Assets.WALL1.filename, Texture.class), 0, 0);
		batch.draw(game.getAssetManager().get(Assets.PLAYER1.filename, Texture.class), player.getBody().getPosition().x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				player.getBody().getPosition().y - Constants.PLAYER_COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
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
