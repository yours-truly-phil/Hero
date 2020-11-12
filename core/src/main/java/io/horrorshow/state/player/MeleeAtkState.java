package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.horrorshow.objects.Guy;

public class MeleeAtkState implements PlayerState {

    private float coolDown = 0.15f;
    private float duration = 0.3f;
    private float stateTimer = 0.0f;

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        if (Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isTouched()) {
            if (stateTimer > coolDown) {
                enterState(guy);
            }
        }
        if (stateTimer > duration) {
            guy.defaultState.enterState(guy);
        }
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", MeleeAtkState.class.getSimpleName());
        stateTimer = 0.0f;
        guy.currentState = this;
        guy.swordAttack();
    }
}
