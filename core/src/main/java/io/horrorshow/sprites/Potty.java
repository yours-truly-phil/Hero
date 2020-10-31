package io.horrorshow.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static io.horrorshow.Hero.PPM;

public class Potty extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion[] pottyStand = new TextureRegion[4];
    private Animation<TextureRegion>[] pottyRun = new Animation[4];

    private int direction = 0;
    private float stateTimer;

    public Potty(World world, TextureAtlas atlas) {
        var logRegion = atlas.findRegion("log");
        this.world = world;

        stateTimer = 0;

        definePotty();
        pottyStand[0] = new TextureRegion(logRegion, 0, 0, 32, 32);
        pottyStand[1] = new TextureRegion(logRegion, 0, 32, 32, 32);
        pottyStand[2] = new TextureRegion(logRegion, 0, 2 * 32, 32, 32);
        pottyStand[3] = new TextureRegion(logRegion, 0, 3 * 32, 32, 32);

        Array<TextureRegion> frames = new Array<>();
        for (int y = 0; y < 4; y++) {
            for (int i = 1; i <= 3; i++) {
                frames.add(new TextureRegion(logRegion, i * 32, y * 32, 32, 32));
            }
            pottyRun[y] = new Animation<>(0.1f, frames);
            frames.clear();
        }

        setBounds(0, 0, 32 / PPM, 32 / PPM);
        setRegion(pottyStand[0]);
    }

    private void definePotty() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        b2body.setLinearDamping(7f);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1f);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2,
                b2body.getPosition().y - getHeight() / 2 + 3 / PPM);
        setRegion(getFrame(dt));

        int dirPreference = direction;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(new Vector2(0, 1));
            dirPreference = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(new Vector2(0, -1));
            dirPreference = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(new Vector2(-1, 0));
            dirPreference = 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(new Vector2(1, 0));
            dirPreference = 2;
        }
        stateTimer = direction == dirPreference ? stateTimer + dt : 0;
        direction = dirPreference;
    }

    private TextureRegion getFrame(float dt) {
        var vel = b2body.getLinearVelocity();
//        int row;
//        if (Math.abs(vel.y) * 0.9 > Math.abs(vel.x)) {
//            if(vel.y <= 0) row = 0;
//            else row = 1;
//        } else {
//            if(vel.x >= 0) row = 2;
//            else row = 3;
//        }
//
//        stateTimer = direction == row ? stateTimer + dt : 0;
//        direction = row;
        if (Math.abs(vel.x) + Math.abs(vel.y) < 0.3) {
            return pottyStand[direction];
        } else {
            return pottyRun[direction].getKeyFrame(stateTimer, true);
        }
    }

    public void move(Vector2 linearImpulse) {
        b2body.applyLinearImpulse(
                linearImpulse, b2body.getWorldCenter(), true);
    }
}