package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import io.horrorshow.objects.Guy;

import static com.badlogic.gdx.Input.Keys.*;

public class DefaultState extends CanMoveState implements PlayerState {

    private float stateTimer = 0.0f;

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    public void update(Guy guy, float dt) {
        super.update(guy, dt);
        stateTimer += dt;
        if (Gdx.input.isKeyJustPressed(J) || Gdx.input.isTouched()) {
            guy.meleeAtkState.enterState(guy);
        }
        if (Gdx.input.isKeyJustPressed(C)) {
            guy.liftState.enterState(guy);
        }
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", DefaultState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
    }
}
