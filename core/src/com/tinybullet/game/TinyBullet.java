package com.tinybullet.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybullet.game.model.State;
import com.tinybullet.game.network.TinyBulletClient;
import com.tinybullet.game.view.*;

public class TinyBullet extends Game {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
	private AssetManager assetManager;
	private BitmapFont font;
	private GlyphLayout layout;

	// Screens
	private MenuScreen menuScreen;
	private PartyScreen partyScreen;
	private GameScreen gameScreen;

	// Network
	private TinyBulletClient client;

	// Game State
	private State state = State.MENU;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, camera);
		assetManager = new AssetManager();

		menuScreen = new MenuScreen(this);
		partyScreen = new PartyScreen(this);
		gameScreen = new GameScreen(this);

		client = new TinyBulletClient(this);

		CustomColor.reset();
		load();

		stage = new Stage(viewport, batch);
		Gdx.input.setInputProcessor(stage);

		// Define Font
		font = assetManager.get(Asset.FONT.filename, BitmapFont.class);
		font.getData().markupEnabled = true;
		layout = new GlyphLayout();

		this.setScreen(menuScreen);
	}

	private void load() {
//		assetManager.load(Asset.TEST.filename, Texture.class);

		assetManager.load(Asset.GROUND.filename, Texture.class);
		assetManager.load(Asset.WALL1.filename, Texture.class);
		assetManager.load(Asset.WALL1_SHADOW.filename, Texture.class);
		assetManager.load(Asset.WALL2.filename, Texture.class);
		assetManager.load(Asset.WALL2_SHADOW.filename, Texture.class);
		assetManager.load(Asset.PILLAR1.filename, Texture.class);
		assetManager.load(Asset.PILLAR1_SHADOW.filename, Texture.class);
		assetManager.load(Asset.PILLAR2.filename, Texture.class);
		assetManager.load(Asset.PILLAR2_SHADOW.filename, Texture.class);
		assetManager.load(Asset.PLAYER1.filename, Texture.class);
		assetManager.load(Asset.PLAYER2.filename, Texture.class);
		assetManager.load(Asset.PLAYER3.filename, Texture.class);
		assetManager.load(Asset.PLAYER4.filename, Texture.class);
		assetManager.load(Asset.PLAYER_SHADOW.filename, Texture.class);
		assetManager.load(Asset.PLAYER1_BULLET.filename, Texture.class);
		assetManager.load(Asset.PLAYER2_BULLET.filename, Texture.class);
		assetManager.load(Asset.PLAYER3_BULLET.filename, Texture.class);
		assetManager.load(Asset.PLAYER4_BULLET.filename, Texture.class);
		assetManager.load(Asset.PLAYER_BULLET_SHADOW.filename, Texture.class);
		assetManager.load(Asset.GRADIENT.filename, Texture.class);

		assetManager.load(Asset.FONT.filename, BitmapFont.class);

		assetManager.finishLoading();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(CustomColor.BLUE2.r, CustomColor.BLUE2.g, CustomColor.BLUE2.b, CustomColor.BLUE2.a);
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
		batch.dispose();
		assetManager.dispose();
		menuScreen.dispose();
		gameScreen.dispose();
		client.dispose();
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

	public TinyBulletClient getClient() {
		return client;
	}

	public BitmapFont getFont() {
		return font;
	}

	public GlyphLayout getLayout() {
		return layout;
	}

	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public PartyScreen getPartyScreen() {
		return partyScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}
}
