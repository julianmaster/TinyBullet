package com.tinybullet.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.tinybullet.game.Constants;
import com.tinybullet.game.network.PlayerPosition;
import com.tinybullet.game.view.Asset;

public class OtherPlayer extends Entity {

	private final PlayerColor color;
	private final Vector2 position;
	private final Vector2 size;
	private final PlayerPosition playerPosition;

	public OtherPlayer(PlayerColor color, int x, int y) {
		this.color = color;
		this.playerPosition = new PlayerPosition(x, y);
		this.size = new Vector2(Constants.PLAYER_COLLISION_WIDTH, Constants.PLAYER_COLLISION_HEIGHT);
		this.position = new Vector2(playerPosition.x, playerPosition.y);
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(color.player.filename, Texture.class), position.x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				position.y - Constants.PLAYER_COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
	}

	@Override
	public void renderShadow(Batch batch, AssetManager assetManager) {
		batch.draw(assetManager.get(Asset.PLAYER_SHADOW.filename, Texture.class), position.x - Constants.PLAYER_COLLISION_WIDTH_OFFSET - Constants.PLAYER_COLLISION_WIDTH / 2f,
				position.y - Constants.PLAYER_COLLISION_HEIGHT_OFFSET - Constants.PLAYER_COLLISION_HEIGHT /2f);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Entity) {
			Entity e = (Entity)o;
			return -Float.compare(position.y - size.y/2f, e.getPosition().y - e.getSize().y/2f);
		}
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}
}
