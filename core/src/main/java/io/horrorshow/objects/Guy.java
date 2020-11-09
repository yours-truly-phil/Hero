package io.horrorshow.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.horrorshow.events.AttackEvent;
import io.horrorshow.events.Component;
import io.horrorshow.events.ComponentEventListener;
import io.horrorshow.events.Registration;
import io.horrorshow.screens.PlayScreen;

import static io.horrorshow.Hero.GUY_BIT;
import static io.horrorshow.Hero.PPM;

public class Guy extends Component implements Disposable {

    private final Sprite sprite = new Sprite();
    private final Vector2 buf_vector2 = new Vector2();
    private final TextureRegion[] guyStand = new TextureRegion[4];
    private final Animation<TextureRegion>[] guyRun = new Animation[4];
    private final Animation<TextureRegion>[] guySword = new Animation[4];
    private final PlayScreen screen;
    public World world;
    public Body b2body;
    public boolean isSwordSwing = false;
    public float swordTimer = 0;
    private int direction = 0;
    private float stateTimer = 0;
    private int swordDirection = 0;

    public Guy(World world, TextureAtlas atlas, PlayScreen screen) {
        this.screen = screen;
        var guyRegion = atlas.findRegion("character");
        this.world = world;

        defineGuy();
        guyStand[0] = new TextureRegion(guyRegion, 0, 0, 32, 32);
        guyStand[1] = new TextureRegion(guyRegion, 0, 32, 32, 32);
        guyStand[2] = new TextureRegion(guyRegion, 0, 2 * 32, 32, 32);
        guyStand[3] = new TextureRegion(guyRegion, 0, 3 * 32, 32, 32);

        Array<TextureRegion> frames = new Array<>();
        for (int y = 0; y < 4; y++) {
            for (int i = 1; i <= 3; i++) {
                frames.add(new TextureRegion(guyRegion, i * 32, y * 32, 32, 32));
            }
            guyRun[y] = new Animation<>(0.1f, frames);
            frames.clear();
            for (int i = 0; i <= 3; i++) {
                frames.add(new TextureRegion(guyRegion, i * 32, 4 * 32 + y * 32, 32, 32));
            }
            guySword[y] = new Animation<>(0.1f, frames);
            frames.clear();
        }

        sprite.setBounds(0, 0, 32 / PPM, 32 / PPM);
        sprite.setRegion(guyStand[0]);
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
        sprite.setPosition(b2body.getPosition().x - sprite.getWidth() / 2,
                b2body.getPosition().y - sprite.getHeight() / 2 + 6 / PPM);

        int dirPreference = direction;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            buf_vector2.x = 0;
            buf_vector2.y = 1;
            move(buf_vector2);
            dirPreference = 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            buf_vector2.x = 0;
            buf_vector2.y = -1;
            move(buf_vector2);
            dirPreference = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            buf_vector2.x = -1;
            buf_vector2.y = 0;
            move(buf_vector2);
            dirPreference = 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            buf_vector2.x = 1;
            buf_vector2.y = 0;
            move(buf_vector2);
            dirPreference = 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isTouched()) {
            var pos = b2body.getPosition();
            if (dirPreference == 2) {
                pos.y += 1.5;
            } else if (dirPreference == 0) {
                pos.y -= 1.5;
            } else if (dirPreference == 3) {
                pos.x -= 1.5;
            } else if (dirPreference == 1) {
                pos.x += 1.5;
            }
            screen.pe.setPosition(pos.x, pos.y);
            screen.pe.start();
            isSwordSwing = true;
            swordTimer = 0;
            swordDirection = dirPreference;

            fireEvent(new AttackEvent(this, pos));
        }

        stateTimer = direction == dirPreference ? stateTimer + dt : 0;

        swordTimer += dt;
        if (guySword[direction].isAnimationFinished(swordTimer)) {
            isSwordSwing = false;
        }
        direction = dirPreference;

        sprite.setRegion(getFrame());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private TextureRegion getFrame() {
        var vel = b2body.getLinearVelocity();
        if (isSwordSwing) {
            return guySword[swordDirection].getKeyFrame(swordTimer, false);
        } else {
            if (Math.abs(vel.x) + Math.abs(vel.y) < 0.4) {
                return guyStand[direction];
            } else {
                return guyRun[direction].getKeyFrame(stateTimer, true);
            }
        }
    }

    public void move(Vector2 linearImpulse) {
        if (!isSwordSwing) {
            b2body.applyLinearImpulse(
                    linearImpulse, b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void dispose() {

    }
}
