package com.tinybullet.game.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;
import com.tinybullet.game.physic.PhysicManager;

import java.util.ArrayList;
import java.util.List;

public class Arena {

	private final World world;
	private List<Body> body = new ArrayList<>();

	public Arena(World world) {
		this.world = world;
		body.add(PhysicManager.createBox(64f/2f,4f/2f, 64f, 4f, 0, Constants.PLAYER_WALL_CATEGORY, Constants.WALL_MASK, true, false, this, world));
		body.add(PhysicManager.createBox(64f/2f,4f/2f - 2f - 2f, 64f, 4f, 0, Constants.BULLET_WALL_CATEGORY, Constants.BULLET_WALL_MASK, true, false, this, world));
		body.add(PhysicManager.createBox(4f/2f,64f/2f, 4f, 64f, 0, Constants.PLAYER_WALL_CATEGORY, Constants.WALL_MASK,true, false, this, world));
		body.add(PhysicManager.createBox(4f/2f,64f/2f - 2f, 4f, 64f, 0, Constants.BULLET_WALL_CATEGORY, Constants.BULLET_WALL_MASK,true, false, this, world));
		body.add(PhysicManager.createBox(64f/2f,58f + 6f/2f, 64f, 6f, 0, Constants.PLAYER_WALL_CATEGORY, Constants.WALL_MASK,true, false, this, world));
		body.add(PhysicManager.createBox(64f/2f,58f + 6f/2f - 2f, 64f, 6f, 0, Constants.BULLET_WALL_CATEGORY, Constants.BULLET_WALL_MASK,true, false, this, world));
		body.add(PhysicManager.createBox(60f + 4/2f,64f/2f, 4f, 64f, 0, Constants.PLAYER_WALL_CATEGORY, Constants.WALL_MASK,true, false, this, world));
		body.add(PhysicManager.createBox(60f + 4/2f,64f/2f - 2f, 4f, 64f, 0, Constants.BULLET_WALL_CATEGORY, Constants.BULLET_WALL_MASK,true, false, this, world));
	}

	public List<Body> getBody() {
		return body;
	}
}
