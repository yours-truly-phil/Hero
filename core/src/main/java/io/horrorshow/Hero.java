package io.horrorshow;

import com.badlogic.gdx.Game;
import io.horrorshow.screens.MainMenu;

public class Hero extends Game {
    public static final int V_WIDTH = 256;
    public static final int V_HEIGHT = 192;
    public static final float PPM = 8;

    public static final short NOTHING_BIT = 0;
    public static final short STATIC_BIT = 1;
    public static final short POTTY_BIT = 2;
    public static final short DOOR_BIT = 4;

    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}