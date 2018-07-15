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
import com.tinybullet.game.network.json.client.RequestJoinPartyJson;
import com.tinybullet.game.network.json.client.RefreshListPartiesJson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MenuScreen extends ScreenAdapter {

	private final TinyBullet game;

	private ReentrantLock lock = new ReentrantLock();

	private GlyphLayout layout = new GlyphLayout();

	private int[] list;
	private List<TextActionButton> listButtons = new ArrayList<>();

	public MenuScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		game.getClient().send(new RefreshListPartiesJson());
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		BitmapFont font = game.getFont();
		BitmapFontCache cache = game.getFont().getCache();

		batch.begin();
		String info = "";
		info = "List parties:";
		layout.setText(font, info);
		cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2);
		cache.draw(batch);

		lock.lock();
		if(list != null) {
			for(int i = 0; i < list.length; i++) {
				info = "Party "+list[i];
				layout.setText(font, info);
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);
			}
		}
		lock.unlock();
		batch.end();
	}

	private void changeButtons() {
		for(TextActionButton button : listButtons) {
			button.remove();
		}
		listButtons.clear();

		for(int i = 0; i < list.length; i++) {
			TextActionButton button = new TextActionButton("join", game);
			button.setPosition(Constants.CAMERA_WIDTH - 14, Constants.CAMERA_HEIGHT - layout.height / 2 - 10 - 7*i);
			game.getStage().addActor(button);
			final int party = i;
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					RequestJoinPartyJson requestJoinPartyJson = new RequestJoinPartyJson();
					requestJoinPartyJson.party = list[party];
					game.getClient().send(requestJoinPartyJson);
					return false;
				}
			});
			listButtons.add(button);
		}
	}

	@Override
	public void hide() {
		for(TextActionButton button : listButtons) {
			button.remove();
		}
		listButtons.clear();
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

	public int[] getList() {
		return list;
	}

	public void setList(int[] list) {
		this.list = list;
		changeButtons();
	}
}
