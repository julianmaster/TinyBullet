package com.tinybullet.game.physic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybullet.game.Constants;

public class PhysicManager {

	public static Body createBox(float x, float y, float width, float height, boolean isStatic, World world) {
		return createBox(x, y, width, height, 0f, isStatic, world);
	}

	public static Body createBox(float x, float y, float width, float height, float angle, boolean isStatic, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = isStatic ? BodyDef.BodyType.KinematicBody : BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.angle = angle;
		bodyDef.fixedRotation = true;

		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);

		body.createFixture(shape, 1.0f);

		shape.dispose();

		body.setUserData(new Rectangle(x - width/2, y - height/2, width, height));

		return body;
	}
}
