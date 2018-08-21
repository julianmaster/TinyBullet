package com.tinybullet.game.physic;

import com.badlogic.gdx.physics.box2d.*;
import com.tinybullet.game.Constants;
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.Arena;
import com.tinybullet.game.model.Bullet;
import com.tinybullet.game.model.Pillar;
import com.tinybullet.game.model.Player;
import com.tinybullet.game.network.json.client.RequestPickUpBulletJson;
import com.tinybullet.game.network.json.client.RequestPlayerDieJson;
import com.tinybullet.game.view.GameScreen;

public class EntityContactListener implements ContactListener {

	private final TinyBullet game;

	public EntityContactListener(TinyBullet game) {
		this.game = game;
	}

	@Override
	public void beginContact(Contact contact) {
		Object objectA = contact.getFixtureA().getBody().getUserData();
		Object objectB = contact.getFixtureB().getBody().getUserData();

		GameScreen gameScreen = game.getGameScreen();

		gameScreen.getLock().lock();
		if(objectA instanceof Bullet) {
			// Bullet vs Pillar/Arena
			if (objectB instanceof Pillar || objectB instanceof Arena) {
				bulletPillarArenaContact(objectA);
			}
			// Bullet vs Player
			else if(objectB instanceof Player) {
				bulletPlayerContact(objectA, objectB);
			}
		}
		else if(objectB instanceof Bullet) {
			// Bullet vs Pillar/Arena
			if (objectA instanceof Pillar || objectA instanceof Arena) {
				bulletPillarArenaContact(objectB);
			}
			// Bullet vs Player
			else if(objectA instanceof Player) {
				bulletPlayerContact(objectB, objectA);
			}
		}
		gameScreen.getLock().unlock();
	}

	private void bulletPillarArenaContact(Object bulletObject) {
		Bullet bullet = (Bullet) bulletObject;
		if (bullet.isMove()) {
			bullet.setMove(false);
			bullet.setDropped(true);
			for(Fixture fixture : bullet.getBody().getFixtureList()) {
				Filter filter = fixture.getFilterData();
				filter.categoryBits = Constants.BULLETS_DROPPED_CATEGORY;
				filter.maskBits = Constants.BULLETS_DROPPED_MASK;
				fixture.setFilterData(filter);
			}
		}
	}

	private void bulletPlayerContact(Object bulletObject, Object playerObject) {
		Bullet bullet = (Bullet) bulletObject;
		Player player = (Player) playerObject;
		if(bullet.isDropped()) {
			bullet.pickUp();
			player.setBullet(bullet);

			RequestPickUpBulletJson requestPickUpBulletJson = new RequestPickUpBulletJson();
			requestPickUpBulletJson.color = bullet.getColor();
			game.getClient().send(requestPickUpBulletJson);
		}
		else if(!bullet.isPlayerFire()) {
			player.setLife(player.getLife()-1);
			if(player.getLife() == 0) {
				player.die();

				RequestPlayerDieJson requestPlayerDieJson = new RequestPlayerDieJson();
				requestPlayerDieJson.color = player.getColor();
				game.getClient().send(requestPlayerDieJson);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
