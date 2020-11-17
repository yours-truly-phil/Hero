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
        if (!Gdx.input.isKeyPressed(Input.Keys.J)) {
            guy.sword360State.enterState(guy);
        }
        stateTimer += dt;
        super.update(guy, dt);
        guy.swordState.update(guy.b2body.getPosition(), guy.orientation, 1.5f);
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", HoldSwordState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
        guy.swordState.isActive = true;
    }
}
