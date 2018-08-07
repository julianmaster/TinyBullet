package com.tinybullet.game.physic;

import com.badlogic.gdx.physics.box2d.*;
import com.tinybullet.game.Constants;
import com.tinybullet.game.model.Arena;
import com.tinybullet.game.model.Bullet;
import com.tinybullet.game.model.Pillar;
import com.tinybullet.game.model.Player;
import com.tinybullet.game.network.json.client.RequestPickUpBulletJson;
import com.tinybullet.game.view.GameScreen;

public class EntityContactListener implements ContactListener {

	private final GameScreen screen;
	private final World world;

	public EntityContactListener(GameScreen screen, World world) {
		this.screen = screen;
		this.world = world;
	}

	@Override
	public void beginContact(Contact contact) {
		Object objectA = contact.getFixtureA().getBody().getUserData();
		Object objectB = contact.getFixtureB().getBody().getUserData();

		if(objectA instanceof Bullet) {
			// Bullet vs Pillar/Arena
			if (objectB instanceof Pillar || objectB instanceof Arena) {
				Bullet bullet = (Bullet) objectA;
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
			// Bullet vs Player
			else if(objectB instanceof Player) {
				Bullet bullet = (Bullet) objectA;
				Player player = (Player) objectB;
				if(bullet.isDropped()) {
					screen.getLock().lock();
					bullet.pickUp();
					player.setBullet(bullet);
					screen.getEntities().remove(bullet);
					screen.getLock().unlock();

					RequestPickUpBulletJson requestPickUpBulletJson = new RequestPickUpBulletJson();
					requestPickUpBulletJson.color = bullet.getColor();
					screen.getGame().getClient().send(requestPickUpBulletJson);
				}
			}
		}
		else if(objectB instanceof Bullet) {
			// Bullet vs Pillar/Arena
			if (objectA instanceof Pillar || objectA instanceof Arena) {
				Bullet bullet = (Bullet)objectB;
				if(bullet.isMove()) {
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
			// Bullet vs Player
			else if(objectA instanceof Player) {
				Bullet bullet = (Bullet) objectB;
				Player player = (Player) objectA;
				if(bullet.isDropped()) {
					screen.getLock().lock();
					bullet.pickUp();
					player.setBullet(bullet);
					screen.getEntities().remove(bullet);
					screen.getLock().unlock();

					RequestPickUpBulletJson requestPickUpBulletJson = new RequestPickUpBulletJson();
					requestPickUpBulletJson.color = bullet.getColor();
					screen.getGame().getClient().send(requestPickUpBulletJson);
				}
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
