package io.horrorshow.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.horrorshow.screens.PlayScreen;

import static io.horrorshow.Hero.V_HEIGHT;
import static io.horrorshow.Hero.V_WIDTH;

public class Hud implements Disposable {
    private final Viewport viewport;
    private final PlayScreen screen;

    Sprite hudSprite;

    public Hud(PlayScreen screen) {
        this.screen = screen;
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2,
                viewport.getWorldHeight() / 2, 0);
        hudSprite = new Sprite(new Texture(Gdx.files.internal("sprites/hud.png")));
    }

    public void render(float dt) {
        viewport.getCamera().update();
        screen.batch.setProjectionMatrix(viewport.getCamera().combined);
        screen.batch.begin();
        hudSprite.draw(screen.batch);
        screen.batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
    }
}
