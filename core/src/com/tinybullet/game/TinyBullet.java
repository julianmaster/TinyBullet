package com.tinybullet.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybullet.game.view.Assets;
import com.tinybullet.game.view.MainScreen;

public class TinyBullet extends Game {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
	private AssetManager assetManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, camera);
		assetManager = new AssetManager();

		load();

		stage = new Stage(viewport, batch);

		this.setScreen(new MainScreen(this));
	}

	private void load() {
//		assetManager.load(Assets.TEST.filename, Texture.class);

		assetManager.load(Assets.GROUND.filename, Texture.class);
		assetManager.load(Assets.WALL1.filename, Texture.class);
		assetManager.load(Assets.WALL1_SHADOW.filename, Texture.class);
		assetManager.load(Assets.WALL2.filename, Texture.class);
		assetManager.load(Assets.WALL2_SHADOW.filename, Texture.class);
		assetManager.load(Assets.PILLAR1.filename, Texture.class);
		assetManager.load(Assets.PILLAR1_SHADOW.filename, Texture.class);
		assetManager.load(Assets.PILLAR2.filename, Texture.class);
		assetManager.load(Assets.PILLAR2_SHADOW.filename, Texture.class);
		assetManager.load(Assets.PLAYER1.filename, Texture.class);
		assetManager.load(Assets.PLAYER2.filename, Texture.class);
		assetManager.load(Assets.PLAYER_SHADOW.filename, Texture.class);
		assetManager.load(Assets.PLAYER1_BULLET.filename, Texture.class);
		assetManager.load(Assets.PLAYER_BULLET_SHADOW.filename, Texture.class);

		assetManager.load(Assets.GRADIENT.filename, Texture.class);

		assetManager.finishLoading();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		super.render();
		stage.draw();
	}
	
	@Override
	public void dispose () {
		stage.dispose();
		screen.dispose();
		batch.dispose();
		assetManager.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Stage getStage() {
		return stage;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
