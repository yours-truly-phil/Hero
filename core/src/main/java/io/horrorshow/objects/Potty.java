package io.horrorshow.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.horrorshow.Hero;
import io.horrorshow.state.Direction;
import io.horrorshow.state.HasDynamicBody;

public class Potty implements HasDynamicBody {

    public final Body b2body;

    private float stateTimer = 0.0f;

    public Potty(World world) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        b2body.setLinearDamping(9f);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.7f);
        fdef.shape = shape;

        fdef.filter.categoryBits = Hero.POTTY_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt) {
        stateTimer += dt;
    }

    public void move(Vector2 dir) {
        if (Math.abs(b2body.getPosition().dst(dir)) < 3) {
            return;
        }
        var x = dir.x - b2body.getPosition().x;
        var y = dir.y - b2body.getPosition().y;
        var v = new Vector2(x, y);
        v.nor();
        b2body.applyLinearImpulse(
                v, b2body.getWorldCenter(), true);
    }

    @Override
    public Body getBody() {
        return b2body;
    }

    @Override
    public boolean isInMotion() {
        var vel = b2body.getLinearVelocity();
        return (Math.abs(vel.x) + Math.abs(vel.y)) > 0.4;
    }

    @Override
    public Direction getDirection() {
        var vel = b2body.getLinearVelocity();
        if (Math.abs(vel.x) > Math.abs(vel.y)) {
            if (vel.x > 0) return Direction.RIGHT;
            else return Direction.LEFT;
        } else {
            if (vel.y > 0) return Direction.UP;
            else return Direction.DOWN;
        }
    }

    @Override
    public float stateTimer() {
        return stateTimer;
    }
}