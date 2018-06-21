package com.tinybullet.game.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.tinybullet.game.model.Arena;
import com.tinybullet.game.model.Bullet;
import com.tinybullet.game.model.Pillar;

public class EntityContactListener implements ContactListener {

	private final World world;

	public EntityContactListener(World world) {
		this.world = world;
	}

	@Override
	public void beginContact(Contact contact) {
		Object objectA = contact.getFixtureA().getBody().getUserData();
		Object objectB = contact.getFixtureB().getBody().getUserData();

		if(objectA instanceof Bullet) {
			if (objectB instanceof Pillar || objectB instanceof Arena) {
				Bullet bullet = (Bullet) objectA;
				if (bullet.isMove()) {
					bullet.setMove(false);
					bullet.setDropped(true);
				}
			}
		}
		else if(objectB instanceof Bullet) {
			if (objectA instanceof Pillar || objectA instanceof Arena) {
				Bullet bullet = (Bullet)objectB;
				if(bullet.isMove()) {
					bullet.setMove(false);
					bullet.setDropped(true);
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
