package io.horrorshow.events;

import java.util.ArrayList;
import java.util.HashMap;

public class ComponentEventBus {

    HashMap<Class<? extends ComponentEvent<?>>, ArrayList<ListenerWrapper<?>>> componentEventData =
            new HashMap<>(2);
    private Observable observable;

    public ComponentEventBus(Observable observable) {
        this.observable = observable;
    }

    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        ListenerWrapper<T> wrapper = new ListenerWrapper<>(listener);

        componentEventData.computeIfAbsent(eventType, t -> new ArrayList<>(1)).add(wrapper);

        return Registration.once(() -> removeListener(eventType, wrapper));
    }

    private <T extends ComponentEvent<?>> void removeListener(
            Class<T> eventType, ListenerWrapper<T> wrapper) {
        assert eventType != null;
        assert wrapper != null;
        assert wrapper.listener != null;

        ArrayList<ListenerWrapper<?>> eventData = componentEventData.get(eventType);
        if (eventData == null) {
            throw new IllegalArgumentException(
                    "No listener of the given type is registered");
        }

        if (!eventData.remove(wrapper)) {
            throw new IllegalArgumentException(
                    "The given listener is not registered");
        }
        if (eventData.isEmpty()) {
            componentEventData.remove(eventType);
        }
    }

    public void fireEvent(ComponentEvent event) {
        Class<? extends ComponentEvent> eventType = event.getClass();
        if (!hasListener(eventType)) {
            return;
        }

        // Copy the list to avoid ConcurrentModificationException
        for (ListenerWrapper wrapper : new ArrayList<>(
                componentEventData.get(event.getClass()))) {
            fireEventForListener(event, wrapper);
        }
    }

    private <T extends ComponentEvent<?>> void fireEventForListener(T event,
                                                                    ListenerWrapper<T> wrapper) {
        Class<T> eventType = (Class<T>) event.getClass();

        event.setUnregisterListenerCommand(() -> removeListener(eventType, wrapper));

        wrapper.listener.onComponentEvent(event);
        event.setUnregisterListenerCommand(null);
    }

    public boolean hasListener(Class<? extends ComponentEvent> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        return componentEventData.containsKey(eventType);
    }

    private static class ListenerWrapper<T extends ComponentEvent<?>> {
        private ComponentEventListener<T> listener;

        public ListenerWrapper(ComponentEventListener<T> listener) {
            this.listener = listener;
        }
    }
}
