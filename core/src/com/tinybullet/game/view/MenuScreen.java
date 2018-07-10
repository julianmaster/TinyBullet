package com.tinybullet.game.view;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tinybullet.game.TinyBullet;

public class MenuScreen extends ScreenAdapter {

	private final TinyBullet game;

	public MenuScreen(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Batch batch = game.getBatch();

		batch.begin();
		game.getFont().draw(batch, "Hello World !", 0,10);
		batch.end();
	}

	@Override
	public void dispose() {
	}

	public TinyBullet getGame() {
		return game;
	}
}
