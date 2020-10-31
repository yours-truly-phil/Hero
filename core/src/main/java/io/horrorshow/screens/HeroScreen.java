package io.horrorshow.screens;

import com.badlogic.gdx.Screen;
import io.horrorshow.Hero;

public abstract class HeroScreen implements Screen {

    protected Hero game;

    public HeroScreen(Hero game) {
        this.game = game;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
