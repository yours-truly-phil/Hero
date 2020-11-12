package io.horrorshow.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.horrorshow.Hero;
import io.horrorshow.events.Registration;
import io.horrorshow.model.B2DWorld;
import io.horrorshow.objects.Guy;
import io.horrorshow.objects.Potty;
import io.horrorshow.renderer.NPCRenderer;
import io.horrorshow.renderer.PlayerRenderer;
import io.horrorshow.scenes.Hud;
import io.horrorshow.state.Direction;
import io.horrorshow.state.player.MeleeAtkState;
import text.formic.Stringf;

import java.util.Arrays;

import static io.horrorshow.Hero.*;
import static text.formic.Stringf.format;

public class PlayScreen extends HeroScreen {

    public final ParticleEffect pe;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Hud hud;
    private final B2DWorld b2dWorld;
    private final Potty potty;
    private final Guy guy;
    private final TextureAtlas atlas;
    private final RayHandler rayHandler;
    private final PointLight myLight;
    private final PointLight myLight2;
    private final Vector2 mouse2D = new Vector2();
    private final Vector3 mouse3D = new Vector3();
    private final PlayerRenderer playerRenderer;
    private final NPCRenderer pottyRenderer;
    private final Registration listener;
    private final Registration liftListener;
    public SpriteBatch batch;

    private BenchBuf benchBuf = new BenchBuf();

    public PlayScreen(Hero game) {
        super(game);
        Box2D.init();

        atlas = new TextureAtlas("opengameart/zelda-like/hero_characters.atlas");

        batch = new SpriteBatch();

        hud = new Hud(this);

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, gameCam);

        b2dWorld = new B2DWorld("opengameart/zelda-like/level.tmx");

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        potty = new Potty(b2dWorld.world);
        pottyRenderer = new NPCRenderer(potty, atlas.findRegion("log"),
                new Direction[]{Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT},
                32, 32, new Vector2(0, 6));

        guy = new Guy(b2dWorld.world);
        playerRenderer = new PlayerRenderer(guy, atlas);

        rayHandler = new RayHandler(b2dWorld.world);
        rayHandler.setAmbientLight(0.5f);

        myLight = new PointLight(rayHandler, 200, Color.ORANGE, 16.f, 0, 0);
        myLight.setSoftnessLength(3);
        myLight.attachToBody(potty.b2body);
        myLight2 = new PointLight(rayHandler, 200, Color.TEAL, 16.f, 0, 0);
        myLight2.setSoftnessLength(3);
        myLight2.attachToBody(guy.b2body);

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("Particles.party"), Gdx.files.internal(""));
        pe.preAllocateParticles();

        listener = guy.addAttackListener(event -> {
            Gdx.app.log("AttackEvent",
                    format("x: %f, y: %f", event.attackPosition.x, event.attackPosition.y));
            pe.setPosition(event.attackPosition.x, event.attackPosition.y);
            pe.start();
        });

        liftListener = guy.addLiftListener(event -> Gdx.app.log("LiftEvent",
                format("x: %f, y: %f", event.pos.x, event.pos.y)));
    }

    @Override
    public void show() {
        super.show();
    }

    public void update(float dt) {
        b2dWorld.update(dt);
        handleInput(dt);
        potty.update(dt);
        guy.update(dt);
        updateGameCam();

        rayHandler.update();
        rayHandler.setCombinedMatrix(gameCam);

        if (guy.currentState instanceof MeleeAtkState) {
            myLight2.setActive(true);
            myLight2.setDistance(16 / (float) Math.pow(1 + guy.currentState.getStateTimer(), 4));
        } else {
            myLight2.setActive(false);
        }

        pe.update(dt);

//        potty.move(curMousePos());
        potty.move(new Vector2(guy.b2body.getPosition().x, guy.b2body.getPosition().y));
    }

    private void updateGameCam() {
        var guyPos = guy.b2body.getPosition();
        var mapProps = b2dWorld.map.getProperties();
        var minPosX = gamePort.getWorldWidth() / 2;
        var maxPosX = mapProps.get("width", Integer.class) - minPosX;
        var minPosY = gamePort.getWorldHeight() / 2;
        var maxPosY = mapProps.get("height", Integer.class) - minPosY;

        gameCam.position.x = (guyPos.x < minPosX) ? minPosX : Math.min(guyPos.x, maxPosX);
        gameCam.position.y = (guyPos.y < minPosY) ? minPosY : Math.min(guyPos.y, maxPosY);

        gameCam.update();
    }

    private Vector2 curMousePos() {
        mouse3D.x = Gdx.input.getX();
        mouse3D.y = Gdx.input.getY();
        mouse3D.z = 0;
        gameCam.unproject(mouse3D);
        mouse2D.x = mouse3D.x;
        mouse2D.y = mouse3D.y;
        return mouse2D;
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            gameCam.zoom -= dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            gameCam.zoom += dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            listener.remove();
            liftListener.remove();
        }
    }

    @Override
    public void render(float dt) {
        benchmark(() -> renderAndUpdate(dt));
    }

    private void renderAndUpdate(float dt) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(dt);

        b2dWorld.renderBackground(gameCam);

        rayHandler.render();

        batch.begin();
        batch.setProjectionMatrix(gameCam.combined);
        playerRenderer.render(batch);
        pottyRenderer.render(batch);
        pe.draw(batch);
        batch.end();

        b2dWorld.renderForeground(gameCam);

        b2dWorld.renderDebug(gameCam);

        hud.render(dt);
    }

    private void benchmark(Runnable renderMethod) {
        long start = TimeUtils.nanoTime();

        renderMethod.run();

        long dur = TimeUtils.timeSinceNanos(start);
        benchBuf.add(dur);
        if (benchBuf.idx == benchBuf.durs.length - 1) {
            Gdx.app.log("RenderBenchmark", Stringf.format("avg: %fµs (max: %fµs min: %fµs)",
                    benchBuf.avg() / 1000.0, benchBuf.max / 1000.0, benchBuf.min / 1000.0));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
        gamePort.update(width, height);

        fixBox2DLightsViewportResize(width, height);

    }

    private void fixBox2DLightsViewportResize(int width, int height) {
        int gutterW = gamePort.getLeftGutterWidth();
        int gutterH = gamePort.getTopGutterHeight();
        int rhWidth = width - (2 * gutterW);
        int rhHeight = height - (2 * gutterH);
        rayHandler.useCustomViewport(gutterW, gutterH, rhWidth, rhHeight);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        b2dWorld.dispose();
        hud.dispose();
        batch.dispose();
        atlas.dispose();
        rayHandler.dispose();
        listener.remove();
        liftListener.remove();
        pe.dispose();
        myLight.dispose();
        myLight2.dispose();
    }

    class BenchBuf {
        public long max = -1;
        public long min = Long.MAX_VALUE;
        long[] durs = new long[60];
        int idx = 0;

        int add(long dur) {
            max = Math.max(max, dur);
            min = Math.min(min, dur);
            idx++;
            if (idx == durs.length) {
                idx = 0;
                max = -1;
                min = Long.MAX_VALUE;
            }
            durs[idx] = dur;
            return idx;
        }

        double avg() {
            return Arrays.stream(durs).average().orElse(Double.NaN);
        }
    }
}
