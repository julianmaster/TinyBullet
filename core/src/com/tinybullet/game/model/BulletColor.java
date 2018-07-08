package com.tinybullet.game.model;

import com.tinybullet.game.view.Asset;

public enum BulletColor {
	RED(Asset.PLAYER1_BULLET),
	GREEN(Asset.PLAYER2_BULLET),
	YELLOW(Asset.PLAYER3_BULLET),
	PURPLE(Asset.PLAYER4_BULLET);

	public Asset bullet;

	BulletColor(Asset bullet) {
		this.bullet = bullet;
	}
}
