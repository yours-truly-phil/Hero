package io.horrorshow.state;

import com.badlogic.gdx.math.Vector2;

public interface NPC {
    Vector2 getPosition();

    boolean isInMotion();

    Direction getDirection();

    float stateTimer();
}
