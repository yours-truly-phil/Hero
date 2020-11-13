package io.horrorshow.events;

public class Observable {

    private EventBus eventBus = null;

    protected <T extends GameEvent<?>> Registration addListener(
            Class<T> eventType, GameEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    protected EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    protected void fireEvent(GameEvent<?> gameEvent) {
        if (hasListener(gameEvent.getClass())) {
            getEventBus().fireEvent(gameEvent);
        }
    }

    protected boolean hasListener(Class<? extends GameEvent> eventType) {
        return eventBus != null && eventBus.hasListener(eventType);
    }
}
