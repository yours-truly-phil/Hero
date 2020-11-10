package io.horrorshow.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.horrorshow.Hero;

import java.util.List;
import java.util.UUID;

import static io.horrorshow.Hero.*;

public class MainMenu extends HeroScreen {

    SpriteBatch batch;
    BitmapFont font;
    OrthographicCamera camera;
    OrthographicCamera hudCam;
    Viewport viewport;
    ShapeRenderer shapeRenderer;

    Vector2 pos;
    Sprite bolb;
    boolean right = true;
    float timer = 0;

    public MainMenu(Hero hero) {
        super(hero);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, camera);
        viewport.apply();
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        shapeRenderer = new ShapeRenderer();

        pos = new Vector2(0, 0);

        bolb = new Sprite(new Texture(Gdx.files.internal("sprites/8x8.png")));
        bolb.setBounds(0, 0, 1, 1);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timer += dt;
        camera.update();
        hudCam.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        bolb.draw(batch);

        batch.setProjectionMatrix(hudCam.combined);
        font.draw(batch, "HERO", 16, 16);
        font.draw(batch, "Tap anywhere to start", 16, 32);
        font.draw(batch, "pos " + pos.x + " | " + pos.y, 16, 16 * 3);
        bolb.setBounds(pos.x, pos.y, 1, 1);
        bolb.draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(0, 0, 1, 1);
        shapeRenderer.rect(V_WIDTH / PPM / 2 - 1, V_HEIGHT / PPM / 2 - 1, 2, 2);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(pos.x, pos.y, 1, 16);

        shapeRenderer.end();

        pos.x += (right) ? 1 * dt : -1 * dt;
        if (pos.x > V_WIDTH) right = false;
        if (pos.x < 0) right = true;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pos.x -= 1 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pos.x += 1 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pos.y += 1 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pos.y -= 1 * dt;
        }
        if (Gdx.input.isTouched() || timer > 5) {
            game.setScreen(new PlayScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
        bolb.getTexture().dispose();
    }
}
