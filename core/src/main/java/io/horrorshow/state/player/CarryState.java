package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.horrorshow.objects.Guy;

public class CarryState extends CanMoveState implements PlayerState {

    private float stateTimer = 0.0f;

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void update(Guy guy, float dt) {
        super.update(guy, dt);
        stateTimer += dt;
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            guy.defaultState.enterState(guy);
        }
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", CarryState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
    }
}
