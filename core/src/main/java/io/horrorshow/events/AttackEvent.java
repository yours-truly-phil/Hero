package io.horrorshow.events;

import com.badlogic.gdx.math.Vector2;
import io.horrorshow.objects.Guy;

public class AttackEvent extends GameEvent<Guy> {

    public final Vector2 attackPosition;

    /**
     * Constructs a prototypical Event.
     *
     * @param source
     *         the object on which the Event initially occurred
     *
     * @throws IllegalArgumentException
     *         if source is null
     */
    public AttackEvent(Guy source, Vector2 attackPosition) {
        super(source);
        this.attackPosition = attackPosition;
    }
}
