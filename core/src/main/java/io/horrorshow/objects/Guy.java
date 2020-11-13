package io.horrorshow.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.horrorshow.events.*;
import io.horrorshow.state.Direction;
import io.horrorshow.state.player.*;

import static io.horrorshow.Hero.GUY_BIT;

public class Guy extends Observable {

    public float LINEAR_DAMPING = 11f;
    public float RANGE = 1.5f;
    public float WALK_IMPULSE = 1.2f;
    public float B2BODY_RADIUS = 0.7f;

    public Body b2body;
    public Direction orientation = Direction.UP;

    public DefaultState defaultState = new DefaultState();
    public MeleeAtkState meleeAtkState = new MeleeAtkState();
    public LiftState liftState = new LiftState();
    public CarryState carryState = new CarryState();

    public PlayerState currentState = defaultState;

    public Guy(World world) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        b2body.setLinearDamping(LINEAR_DAMPING);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(B2BODY_RADIUS);
        fdef.shape = shape;

        fdef.filter.categoryBits = GUY_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }

    public Registration addAttackListener(GameEventListener<AttackEvent> listener) {
        return addListener(AttackEvent.class, listener);
    }

    public Registration addLiftListener(GameEventListener<LiftEvent> listener) {
        return addListener(LiftEvent.class, listener);
    }

    public void update(float dt) {
        currentState.update(this, dt);
    }

    public boolean isMoving() {
        var vel = b2body.getLinearVelocity();
        return (Math.abs(vel.x) + Math.abs(vel.y)) > 0.4;
    }

    public void lift() {
        fireEvent(new LiftEvent(this, getAbilityPosition()));
    }

    public void swordAttack() {
        fireEvent(new AttackEvent(this, getAbilityPosition()));
    }

    private Vector2 getAbilityPosition() {
        var pos = b2body.getPosition();
        switch (orientation) {
            case UP:
                pos.y += RANGE;
                break;
            case DOWN:
                pos.y -= RANGE;
                break;
            case LEFT:
                pos.x -= RANGE;
                break;
            case RIGHT:
                pos.x += RANGE;
                break;
        }
        return pos;
    }

    public void move(Vector2 linearImpulse) {
        b2body.applyLinearImpulse(
                linearImpulse, b2body.getWorldCenter(), true);
    }
}
