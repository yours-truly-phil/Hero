package io.horrorshow.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.horrorshow.events.*;
import io.horrorshow.state.Direction;
import io.horrorshow.state.player.*;

import static io.horrorshow.Hero.GUY_BIT;

public class Guy extends Observable {

    public final float LINEAR_DAMPING = 9f;
    public final double RANGE = 1.5;
    public final int WALK_IMPULSE = 1;
    public final float B2BODY_RADIUS = 0.7f;

//    public final PlayerState state;
//    private final Vector2 buf_vector2 = new Vector2();

    public Body b2body;
    public Direction orientation = Direction.UP;

    public DefaultState defaultState = new DefaultState();
    public MeleeAtkState meleeAtkState = new MeleeAtkState();
    public LiftState liftState = new LiftState();
    public CarryState carryState = new CarryState();

    public PlayerState currentState = defaultState;

    public Guy(World world) {
        defineGuy(world);

//        state = new PlayerState(this);
    }

    private void defineGuy(World world) {
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

    public Registration addAttackListener(ComponentEventListener<AttackEvent> listener) {
        return addListener(AttackEvent.class, listener);
    }

    public Registration addLiftListener(ComponentEventListener<LiftEvent> listener) {
        return addListener(LiftEvent.class, listener);
    }

    public void update(float dt) {
        currentState.update(this, dt);
//        state.update(dt);
//
//        buf_vector2.set(0, 0);
//        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
//            buf_vector2.y += WALK_IMPULSE;
//            orientation = Direction.UP;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//            buf_vector2.y -= WALK_IMPULSE;
//            orientation = Direction.DOWN;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//            buf_vector2.x -= WALK_IMPULSE;
//            orientation = Direction.LEFT;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//            buf_vector2.x += WALK_IMPULSE;
//            orientation = Direction.RIGHT;
//        }
//        move(buf_vector2);
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isTouched()) {
//            swordAttack();
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
//            lift();
//        }
    }

    public boolean isMoving() {
        var vel = b2body.getLinearVelocity();
        return (Math.abs(vel.x) + Math.abs(vel.y)) > 0.4;
    }

    public void lift() {
//        state.lift();
        fireEvent(new LiftEvent(this, getAbilityPosition()));
    }

    public void swordAttack() {
//        state.swordHit();
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
//        if (state.canMove()) {
            b2body.applyLinearImpulse(
                    linearImpulse, b2body.getWorldCenter(), true);
//        }
    }
}
