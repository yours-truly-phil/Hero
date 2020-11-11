package io.horrorshow.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import io.horrorshow.events.AttackEvent;
import io.horrorshow.events.ComponentEventListener;
import io.horrorshow.events.Observable;
import io.horrorshow.events.Registration;
import io.horrorshow.state.Direction;
import io.horrorshow.state.HasDynamicBody;
import io.horrorshow.state.PlayerState;

import static io.horrorshow.Hero.GUY_BIT;

public class Guy extends Observable implements Disposable, HasDynamicBody {

    public static final float LINEAR_DAMPING = 9f;
    public static final double RANGE = 1.5;
    public static final int WALK_IMPULSE = 1;
    public static final float B2BODY_RADIUS = 0.7f;

    public final PlayerState state;
    private final Vector2 buf_vector2 = new Vector2();

    public Body b2body;
    public Direction orientation = Direction.UP;

    public Guy(World world) {

        defineGuy(world);

        state = new PlayerState(this);
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

    public void update(float dt) {
        state.update(dt);

        buf_vector2.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            buf_vector2.y += WALK_IMPULSE;
            orientation = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            buf_vector2.y -= WALK_IMPULSE;
            orientation = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            buf_vector2.x -= WALK_IMPULSE;
            orientation = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            buf_vector2.x += WALK_IMPULSE;
            orientation = Direction.RIGHT;
        }
        move(buf_vector2);

        if (Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isTouched()) {
            swordAttack();
        }
    }

    private void swordAttack() {
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

        state.swordHit();

        fireEvent(new AttackEvent(this, pos));
    }

    public void move(Vector2 linearImpulse) {
        if (state.canMove()) {
            b2body.applyLinearImpulse(
                    linearImpulse, b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public Body getBody() {
        return b2body;
    }

    @Override
    public boolean isInMotion() {
        return state.isMoving();
    }

    @Override
    public Direction getDirection() {
        return orientation;
    }

    @Override
    public float stateTimer() {
        return state.stateTimer();
    }
}
