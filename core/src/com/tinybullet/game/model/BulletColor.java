package com.tinybullet.game.model;

import com.badlogic.gdx.graphics.Color;
import com.tinybullet.game.Constants;
import com.tinybullet.game.view.Asset;

public enum BulletColor {
	RED(Asset.PLAYER1_BULLET, Constants.BULLET_PLAYER1_HALO),
	GREEN(Asset.PLAYER2_BULLET, Constants.BULLET_PLAYER2_HALO),
	YELLOW(Asset.PLAYER3_BULLET, Constants.BULLET_PLAYER3_HALO),
	PURPLE(Asset.PLAYER4_BULLET, Constants.BULLET_PLAYER4_HALO);

	public Asset bullet;
	public Color halo;

	BulletColor(Asset bullet, Color halo) {
		this.bullet = bullet;
		this.halo = halo;
	}
}
