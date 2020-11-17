package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import io.horrorshow.objects.Guy;

public class LiftState implements PlayerState {

    private float stateTimer = 0.0f;
    private float duration = 0.9f;

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        if (stateTimer > duration) {
            guy.carryState.enterState(guy);
        }
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", LiftState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
        guy.lift();
        guy.swordState.isActive = false;
    }
}
