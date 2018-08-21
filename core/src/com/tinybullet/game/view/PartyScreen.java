package com.tinybullet.game.view;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.client.RequestPlayerStatusPartyJson;

import java.util.concurrent.locks.ReentrantLock;

public class PartyScreen extends ScreenAdapter {

	private final TinyBullet game;

	private ReentrantLock lock = new ReentrantLock();

	private PlayerColor[] players;
	private boolean[] readies;
	private PlayerColor playerColor = PlayerColor.GREEN;

	private TextActionButton readyButton;

	public PartyScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		BitmapFont font = game.getFont();
		GlyphLayout layout = game.getLayout();

		readyButton = new TextActionButton("[RED1]Not Ready", game);
		layout.setText(font, "Not Ready");
		readyButton.setPosition(Constants.CAMERA_WIDTH/2 - layout.width/2, 1 + layout.height/2);
		readyButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				for(int i = 0; i < players.length; i++) {
					if(playerColor == players[i]) {
						readies[i] = !readies[i];
						if(readies[i]) {
							readyButton.setStr("[GREEN1]Ready");
							layout.setText(font, "Ready");
						}
						else {
							readyButton.setStr("[RED1]Not Ready");
							layout.setText(font, "Not Ready");
						}
						readyButton.setPosition(Constants.CAMERA_WIDTH/2 - layout.width/2, 1 + layout.height/2);
						RequestPlayerStatusPartyJson requestPlayerStatusPartyJson = new RequestPlayerStatusPartyJson();
						requestPlayerStatusPartyJson.ready = readies[i];
						game.getClient().send(requestPlayerStatusPartyJson);
						return false;
					}
				}
				return false;
			}
		});
		game.getStage().addActor(readyButton);
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		BitmapFont font = game.getFont();
		GlyphLayout layout = game.getLayout();
		BitmapFontCache cache = game.getFont().getCache();

		batch.begin();
		String info;

		info = "[WHITE]Party :";
		layout.setText(font, info);
		cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2);
		cache.draw(batch);

		lock.lock();

		for(int i = 0; i < players.length; i++) {
			PlayerColor partyPlayerColor = players[i];
			if(playerColor == partyPlayerColor) {
				info = "["+partyPlayerColor.color+"]"+partyPlayerColor.name()+"[WHITE] - You";
				layout.setText(font, info);
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
			}
			else {
				info = "["+partyPlayerColor.color+"]"+partyPlayerColor.name();
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
			}

			if(readies[i]) {
				info = "[GREEN1]R";
			}
			else {
				info = "[RED1]NR";
			}
			layout.setText(font, info);
			cache.setText(info, Constants.CAMERA_WIDTH - layout.width + 1, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
			cache.draw(batch);
		}

		lock.unlock();
		batch.end();
	}

	@Override
	public void hide() {
		readyButton.remove();
	}

	@Override
	public void dispose() {
	}

	public TinyBullet getGame() {
		return game;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public void setPlayers(PlayerColor[] players) {
		this.players = players;
	}

	public void setReadies(boolean[] readies) {
		this.readies = readies;
	}

	public void setPlayerColor(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}

	public PlayerColor getPlayerColor() {
		return playerColor;
	}
}
