package io.horrorshow.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.horrorshow.Hero;
import io.horrorshow.model.B2DWorld;
import io.horrorshow.scenes.Hud;
import io.horrorshow.sprites.Potty;

import static io.horrorshow.Hero.*;

public class PlayScreen extends HeroScreen {

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;

    private final Hud hud;

    private final B2DWorld b2dWorld;

    private final Potty potty;

    private final TextureAtlas atlas;
    public SpriteBatch batch;

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

        potty = new Potty(b2dWorld.world, atlas);
    }

    @Override
    public void show() {
        super.show();
    }

    public void update(float dt) {
        b2dWorld.update(dt);
        handleInput(dt);
        potty.update(dt);
        var pottyPos = potty.b2body.getPosition();
        gameCam.position.x = pottyPos.x;
        gameCam.position.y = pottyPos.y;
        gameCam.update();
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            gameCam.zoom -= dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            gameCam.zoom += dt;
        }
    }

    @Override
    public void render(float dt) {
        long start = System.nanoTime();
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(dt);

        b2dWorld.renderBackground(gameCam);

        batch.begin();
        batch.setProjectionMatrix(gameCam.combined);
        potty.draw(batch);
        batch.end();

        b2dWorld.renderForeground(gameCam);

        b2dWorld.renderDebug(gameCam);

        hud.render(dt);
        long dur = System.nanoTime() - start;
        if (dur / 1_000_000_000f > 1 / 60f) System.out.println(dur / 1000 + "µs, ");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
        gamePort.update(width, height);
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
    }
}
