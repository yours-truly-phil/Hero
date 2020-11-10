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
import io.horrorshow.screens.PlayScreen;
import io.horrorshow.state.Direction;
import io.horrorshow.state.PlayerState;

import static io.horrorshow.Hero.GUY_BIT;

public class Guy extends Observable implements Disposable {

    public final PlayerState state;
    private final Vector2 buf_vector2 = new Vector2();

    private final PlayScreen screen;
    public World world;
    public Body b2body;
    public Direction orientation = Direction.UP;

    public Guy(World world, PlayScreen screen) {
        this.screen = screen;
        this.world = world;

        defineGuy();

        state = new PlayerState(this);
    }

    private void defineGuy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        b2body.setLinearDamping(9f);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.7f);
        fdef.shape = shape;

        fdef.filter.categoryBits = GUY_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }

    public Registration addAttackListener(ComponentEventListener<AttackEvent> listener) {
        return addListener(AttackEvent.class, listener);
    }

    public void update(float dt) {
        state.update(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            buf_vector2.x = 0;
            buf_vector2.y = 1;
            move(buf_vector2);
            orientation = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            buf_vector2.x = 0;
            buf_vector2.y = -1;
            move(buf_vector2);
            orientation = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            buf_vector2.x = -1;
            buf_vector2.y = 0;
            move(buf_vector2);
            orientation = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            buf_vector2.x = 1;
            buf_vector2.y = 0;
            move(buf_vector2);
            orientation = Direction.RIGHT;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isTouched()) {
            var pos = b2body.getPosition();
            switch (orientation) {
                case UP:
                    pos.y += 1.5;
                    break;
                case DOWN:
                    pos.y -= 1.5;
                    break;
                case LEFT:
                    pos.x -= 1.5;
                    break;
                case RIGHT:
                    pos.x += 1.5;
                    break;
            }
            screen.pe.setPosition(pos.x, pos.y);
            screen.pe.start();
            state.swordHit();

            fireEvent(new AttackEvent(this, pos));
        }
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
}
