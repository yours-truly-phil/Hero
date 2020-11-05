package io.horrorshow.events;

import java.io.Serializable;
import java.util.EventListener;

public interface ComponentEventListener<T extends ComponentEvent<?>>
        extends EventListener, Serializable {
    void onComponentEvent(T event);
}
