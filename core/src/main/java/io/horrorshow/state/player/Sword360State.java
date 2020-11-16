package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import io.horrorshow.objects.Guy;

public class Sword360State implements PlayerState {

    private float stateTimer = 0.0f;
    public float duration = 0.4f;

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        if (stateTimer > duration) {
            guy.defaultState.enterState(guy);
        }
    }

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", Sword360State.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
    }
}
