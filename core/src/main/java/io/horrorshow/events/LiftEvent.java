package io.horrorshow.events;

import com.badlogic.gdx.math.Vector2;
import io.horrorshow.objects.Guy;

public class LiftEvent extends GameEvent<Guy> {
    public final Vector2 pos;

    public LiftEvent(Guy source, Vector2 pos) {
        super(source);
        this.pos = pos;
    }
}
