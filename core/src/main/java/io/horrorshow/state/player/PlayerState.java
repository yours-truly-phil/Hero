package io.horrorshow.state.player;

import io.horrorshow.objects.Guy;

public interface PlayerState {
    float getStateTimer();

    void update(Guy guy, float dt);

    void enterState(Guy guy);
}
