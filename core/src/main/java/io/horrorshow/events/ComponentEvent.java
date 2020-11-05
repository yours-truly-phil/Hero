package io.horrorshow.events;

import java.util.EventObject;

public class ComponentEvent<T extends Component> extends EventObject {
    private Command unregisterListenerCommand = null;
    /**
     * Constructs a prototypical Event.
     *
     * @param source
     *         the object on which the Event initially occurred
     *
     * @throws IllegalArgumentException
     *         if source is null
     */
    public ComponentEvent(T source) {
        super(source);
    }


    @SuppressWarnings("unchecked")
    @Override
    public T getSource() {
        return (T)super.getSource();
    }

    void setUnregisterListenerCommand(Command unregisterListenerCommand) {
        this.unregisterListenerCommand = unregisterListenerCommand;
    }

    public void unregisterListener() {
        unregisterListenerCommand.execute();
    }
}
