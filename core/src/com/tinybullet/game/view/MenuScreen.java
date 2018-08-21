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
import com.tinybullet.game.network.json.client.RequestListPartiesJson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MenuScreen extends ScreenAdapter {

	private final TinyBullet game;

	private ReentrantLock lock = new ReentrantLock();

	private int[] list;
	private boolean[] joinnable;
	private List<TextActionButton> listButtons = new ArrayList<>();
	private TextActionButton reloadButton;

	public MenuScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
		BitmapFont font = game.getFont();
		GlyphLayout layout = game.getLayout();

		game.getClient().send(new RequestListPartiesJson());

		reloadButton = new TextActionButton("reload", game);
		layout.setText(font, "reload");
		reloadButton.setPosition(Constants.CAMERA_WIDTH / 2 - layout.width / 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*7);
		reloadButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.getClient().send(new RequestListPartiesJson());
				return false;
			}
		});
		game.getStage().addActor(reloadButton);
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();
		BitmapFont font = game.getFont();
		GlyphLayout layout = game.getLayout();
		BitmapFontCache cache = game.getFont().getCache();

		batch.begin();
		String info = "";
		info = "[WHITE]List parties:";
		layout.setText(font, info);
		cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2);
		cache.draw(batch);

		lock.lock();
		if(list != null) {
			for(int i = 0; i < list.length; i++) {
				info = "[WHITE]Party "+list[i];
				layout.setText(font, info);
				cache.setText(info, 2, Constants.CAMERA_HEIGHT - layout.height / 2 - 7 - 7*i);
				cache.draw(batch);

				if(joinnable[i]) {
					listButtons.get(i).setPosition(Constants.CAMERA_WIDTH - 14, Constants.CAMERA_HEIGHT - layout.height / 2 - 10 - 7*i);
				}
				else {
					listButtons.get(i).setPosition(Constants.CAMERA_WIDTH - 11, Constants.CAMERA_HEIGHT - layout.height / 2 - 10 - 7*i);
				}
			}
		}
		lock.unlock();
		batch.end();
	}

	public void changeButtons() {
		GlyphLayout layout = game.getLayout();

		for(TextActionButton button : listButtons) {
			button.remove();
		}
		listButtons.clear();

		for(int i = 0; i < list.length; i++) {
			if(joinnable[i]) {
				TextActionButton button = new TextActionButton("join", CustomColor.GREEN1, game);
				button.setPosition(Constants.CAMERA_WIDTH - 14, Constants.CAMERA_HEIGHT - layout.height / 2 - 10 - 7*i);
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
				game.getStage().addActor(button);
			}
			else {
				TextActionButton button = new TextActionButton("in game", CustomColor.RED2, game);
				button.setPosition(Constants.CAMERA_WIDTH - 11, Constants.CAMERA_HEIGHT - layout.height / 2 - 10 - 7*i);
				button.setActivate(false);
				listButtons.add(button);
				game.getStage().addActor(button);
			}
		}
	}

	@Override
	public void hide() {
		for(TextActionButton button : listButtons) {
			button.remove();
		}
		reloadButton.remove();
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

	public void setList(int[] list) {
		this.list = list;
	}

	public void setJoinnable(boolean[] joinnable) {
		this.joinnable = joinnable;
	}
}
