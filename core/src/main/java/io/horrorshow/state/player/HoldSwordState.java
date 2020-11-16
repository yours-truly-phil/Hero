package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.horrorshow.objects.Guy;

public class HoldSwordState extends CanMoveState implements PlayerState {
    private float stateTimer = 0.0f;
    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        super.update(guy, dt);
        if(!Gdx.input.isKeyPressed(Input.Keys.J)) {
            guy.sword360State.enterState(guy);
        }
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", HoldSwordState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
    }
}
