package com.tinybullet.game.physic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicManager {

	public static Body createBox(float x, float y, float width, float height, boolean isStatic, World world) {
		return createBox(x, y, width, height, 0f, isStatic, world);
	}

	public static Body createBox(float x, float y, float width, float height, float angle, boolean isStatic, World world) {
		return createBox(x, y, width, height, angle, (short)0x0001, (short)-1, isStatic, world);
	}

	public static Body createBox(float x, float y, float width, float height, float angle, short categoryBits, int maskBits, boolean isStatic, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = isStatic ? BodyDef.BodyType.KinematicBody : BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.angle = angle;
		bodyDef.fixedRotation = true;

		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.filter.categoryBits = categoryBits;
		fixture.filter.maskBits = (short)maskBits;

		body.createFixture(fixture);

		shape.dispose();

		body.setUserData(new Rectangle(x - width/2, y - height/2, width, height));

		return body;
	}
}
