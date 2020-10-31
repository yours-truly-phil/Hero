package io.horrorshow;

import com.badlogic.gdx.Game;
import io.horrorshow.screens.MainMenu;

public class Hero extends Game {
    public static final int V_WIDTH = 256;
    public static final int V_HEIGHT = 192;
    public static final float PPM = 8;

    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}