package io.horrorshow.events;

@FunctionalInterface
public interface Command {
    void execute();
}
