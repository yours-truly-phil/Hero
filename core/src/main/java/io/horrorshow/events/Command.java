package io.horrorshow.events;

import java.io.Serializable;

@FunctionalInterface
public interface Command extends Serializable {
    void execute();
}
