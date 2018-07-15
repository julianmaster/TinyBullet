package com.tinybullet.game.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;

public class TextActionButton extends Actor {

	private final TinyBullet game;
	private String str;

	private GlyphLayout layout = new GlyphLayout();

	public TextActionButton(String str, TinyBullet game) {
		this.str = str;
		this.game = game;
		sizeChanged();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		BitmapFontCache cache = game.getFont().getCache();

		cache.setText(str, getX() + 1, getY() + layout.height / 2 + 1);
		cache.draw(batch);
	}

	public void setStr(String str) {
		this.str = str;
		sizeChanged();
	}

	public String getStr() {
		return str;
	}

	@Override
	protected void sizeChanged() {
		BitmapFont font = game.getFont();
		layout.setText(font, str);
		this.setSize(layout.width, layout.height);
	}
}
