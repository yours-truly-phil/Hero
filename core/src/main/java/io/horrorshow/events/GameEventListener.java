package io.horrorshow.events;

import java.util.EventListener;

public interface GameEventListener<T extends GameEvent<? extends Observable>>
        extends EventListener {
    void onGameEvent(T event);
}
