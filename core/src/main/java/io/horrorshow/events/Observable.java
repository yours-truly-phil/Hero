package io.horrorshow.events;

public class Observable {

    private ComponentEventBus eventBus = null;

    protected <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    protected ComponentEventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new ComponentEventBus(this);
        }
        return eventBus;
    }

    protected void fireEvent(ComponentEvent<?> componentEvent) {
        if (hasListener(componentEvent.getClass())) {
            getEventBus().fireEvent(componentEvent);
        }
    }

    protected boolean hasListener(Class<? extends ComponentEvent> eventType) {
        return eventBus != null && eventBus.hasListener(eventType);
    }
}
