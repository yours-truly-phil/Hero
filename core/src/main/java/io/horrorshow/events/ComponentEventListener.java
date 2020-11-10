package io.horrorshow.events;

import java.util.EventListener;

public interface ComponentEventListener<T extends ComponentEvent<?>>
        extends EventListener {
    void onComponentEvent(T event);
}
