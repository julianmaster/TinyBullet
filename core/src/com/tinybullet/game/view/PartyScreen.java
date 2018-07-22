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
import com.tinybullet.game.network.json.client.RefreshListPartiesJson;
import com.tinybullet.game.network.json.client.RequestJoinPartyJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class PartyScreen extends ScreenAdapter {

	private final TinyBullet game;

	private ReentrantLock lock = new ReentrantLock();

	private GlyphLayout layout = new GlyphLayout();

	private PlayerColor[] players;
	private PlayerColor playerColor = PlayerColor.GREEN;

	public PartyScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		BitmapFont font = game.getFont();
		BitmapFontCache cache = game.getFont().getCache();

		batch.begin();
		String info = "";
		info = "Party :";
		layout.setText(font, info);
		cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2);
		cache.draw(batch);

		lock.lock();

		for(int i = 0; i < players.length; i++) {
			PlayerColor partyPlayerColor = players[i];
			if(playerColor == partyPlayerColor) {
				info = "You";
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
			}
			else {
				info = "Player "+i;
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
			}
		}

		lock.unlock();
		batch.end();
	}

	@Override
	public void hide() {
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

	public void setPlayerColor(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}
}
