package io.horrorshow.events;

import java.util.Collection;
import java.util.Objects;

@FunctionalInterface
public interface Registration {

    static Registration once(Command command) {
        Objects.requireNonNull(command);
        return new Registration() {
            private boolean removed = false;

            @Override
            public void remove() {
                if (!removed) {
                    removed = true;
                    command.execute();
                }
            }
        };
    }

    static <T> Registration addAndRemove(Collection<T> collection,
                                         T item) {
        collection.add(item);
        return () -> collection.remove(item);
    }

    void remove();
}
