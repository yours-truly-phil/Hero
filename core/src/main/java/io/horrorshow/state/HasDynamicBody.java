package io.horrorshow.state;

import com.badlogic.gdx.physics.box2d.Body;

public interface HasDynamicBody {
    Body getBody();

    boolean isInMotion();

    Direction getDirection();

    float stateTimer();
}
