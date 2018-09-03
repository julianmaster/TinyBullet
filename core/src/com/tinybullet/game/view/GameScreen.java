package com.tinybullet.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.*;
import com.tinybullet.game.model.State;
import com.tinybullet.game.physic.EntityContactListener;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class GameScreen extends ScreenAdapter {

	private final TinyBullet game;

	private ReentrantLock lock = new ReentrantLock();

	private Arena arena;
	private Player player;
	private List<Entity> entities = new ArrayList<>();
	private List<Entity> entitiesToAdd = new ArrayList<>();
	private Map<PlayerColor, OtherPlayer> otherPlayers = new HashMap<>();
	private Map<BulletColor, Bullet> bullets = new HashMap<>();

	// Box2D
	private World world;
	private List<Body> bodiesToRemove = new ArrayList<>();
	private boolean showDebugPhysics = false;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	// Score
	private Map<PlayerColor, Integer> scores = new LinkedHashMap<>();

	// Timer
	private float timer;

	public GameScreen(TinyBullet game) {
		this.game = game;
		lock.lock();
		world = new World(new Vector2(), false);
		world.setContactListener(new EntityContactListener(game));
		arena = new Arena(world);

		player = new Player(game);
		entities.add(new Pillar(world, Asset.PILLAR1, Asset.PILLAR1_SHADOW, 19,43, 8, 6));
		entities.add(new Pillar(world, Asset.PILLAR1, Asset.PILLAR1_SHADOW, 45,21, 8, 6));
		entities.add(new Pillar(world, Asset.PILLAR2, Asset.PILLAR2_SHADOW, 17,21, 12, 6));
		entities.add(new Pillar(world, Asset.PILLAR2, Asset.PILLAR2_SHADOW, 47,43, 12, 6));

		bullets.put(BulletColor.RED, new Bullet(BulletColor.RED, world, game));
		bullets.put(BulletColor.GREEN, new Bullet(BulletColor.GREEN, world, game));
		bullets.put(BulletColor.YELLOW, new Bullet(BulletColor.YELLOW, world, game));
		bullets.put(BulletColor.PURPLE, new Bullet(BulletColor.PURPLE, world, game));

		for(Bullet bullet : bullets.values()) {
			entities.add(bullet);
		}

		lock.unlock();
	}

	public void init(PlayerColor color, Vector2 position) {
		player.init();
		player.setColor(color);
		player.setPosition(position);
		player.setBullet(bullets.get(color.initBullet));
		entities.add(player);

		for(OtherPlayer otherPlayer : otherPlayers.values()) {
			world.destroyBody(otherPlayer.getBody());
			entities.remove(otherPlayer);
		}
		otherPlayers.clear();

		timer = 3.99f;
	}

	@Override
	public void show() {
	}

	public void update(PlayerColor[] playerColors, Vector2[] positions) {
		lock.lock();
		for(int i = 0; i < playerColors.length; i++) {
			if(playerColors[i] != player.getColor()) {
				if(otherPlayers.containsKey(playerColors[i])) {
					otherPlayers.get(playerColors[i]).setPosition(positions[i]);
				}
				else {
					OtherPlayer otherPlayer = new OtherPlayer(playerColors[i], positions[i], game);
					otherPlayers.put(playerColors[i], otherPlayer);
					entities.add(otherPlayer);
				}
			}
		}
		lock.unlock();
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		BitmapFont font = game.getFont();
		GlyphLayout layout = game.getLayout();
		BitmapFontCache cache = game.getFont().getCache();
		AssetManager assetManager = game.getAssetManager();

		if(game.getState() == State.WAIT_START) {
			timer -= delta;
			if(timer < 1) {
				timer = 1.0f;
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showDebugPhysics = !showDebugPhysics;
		}

		lock.lock();
		for(Entity entity : entitiesToAdd) {
			entities.add(entity);
		}
		entitiesToAdd.clear();

		if(game.getState() == State.PLAY) {
			for(Entity entity : entities) {
				entity.update(delta);
			}
		}

		Collections.sort(entities);

		batch.begin();
		String info;

		// Ground
		batch.draw(game.getAssetManager().get(Asset.GROUND.filename, Texture.class), 0, 0);

		// Shadows
		batch.draw(game.getAssetManager().get(Asset.WALL1_SHADOW.filename, Texture.class), 0, 0);
		for(Entity entity : entities) {
			entity.renderShadow(batch, assetManager);
		}
		batch.draw(game.getAssetManager().get(Asset.WALL2_SHADOW.filename, Texture.class), 0, 0);

		// Entities
		batch.draw(game.getAssetManager().get(Asset.WALL1.filename, Texture.class), 0, 0);
		for(Entity entity : entities) {
			entity.render(batch, assetManager);
		}
		batch.draw(game.getAssetManager().get(Asset.WALL2.filename, Texture.class), 0, 0);

		if(game.getState() == State.SCORE) {
			info = "[WHITE]SCORE";
			layout.setText(font, info);
			cache.setText(info, Constants.CAMERA_WIDTH / 2 - layout.width / 2, Constants.CAMERA_HEIGHT - layout.height / 2);
			cache.draw(batch);

			int i = 0;
			for(Map.Entry<PlayerColor, Integer> score : scores.entrySet()) {
				if(score.getKey() == player.getColor()) {
					info = "["+score.getKey().color+"]"+"[WHITE](You) - "+score.getValue();
				}
				else {
					info = "["+score.getKey().color+"]"+"[WHITE] - "+score.getValue();
				}
				layout.setText(font, info);
				cache.setText(info, Constants.CAMERA_WIDTH / 2 - layout.width / 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
				i++;
			}
		}

		if(game.getState() == State.WAIT_START) {
			info = "[WHITE]"+(int)timer;
			layout.setText(font, info);
			cache.setText(info, Constants.CAMERA_WIDTH / 2 - layout.width / 2, Constants.CAMERA_HEIGHT / 2 - layout.height / 2);
			cache.draw(batch);
		}

		batch.end();

		if(showDebugPhysics) {
			debugRenderer.render(world, game.getCamera().combined);
		}

		for(Body body : bodiesToRemove){
			world.destroyBody(body);
		}
		bodiesToRemove.clear();

		world.step(1/60f, 6, 2);
		lock.unlock();
	}

	@Override
	public void dispose() {
		if(world != null) {
			world.dispose();
		}
	}

	public World getWorld() {
		return world;
	}

	public Player getPlayer() {
		return player;
	}

	public Map<PlayerColor, OtherPlayer> getOtherPlayers() {
		return otherPlayers;
	}

	public List<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public List<Entity> getEntitiesToAdd() {
		return entitiesToAdd;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Map<BulletColor, Bullet> getBullets() {
		return bullets;
	}

	public Map<PlayerColor, Integer> getScores() {
		return scores;
	}
}
