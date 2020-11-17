package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.horrorshow.objects.Guy;

public class MeleeAtkState implements PlayerState {

    public float coolDown = 0.15f;
    public float duration = 0.4f;
    private float stateTimer = 0.0f;
    private boolean keyReleased = false;

    @Override
    public float getStateTimer() {
        return stateTimer;
    }

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            if (stateTimer > coolDown) {
                enterState(guy);
            }
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.J)) {
            keyReleased = true;
        }
        if (stateTimer > duration) {
            if (keyReleased) {
                guy.defaultState.enterState(guy);
            } else {
                guy.holdSwordState.enterState(guy);
            }
        }
        guy.swordState.update(guy.b2body.getPosition(), guy.orientation, 1.5f);
    }

    @Override
    public void enterState(Guy guy) {
        Gdx.app.log("EnterState", MeleeAtkState.class.getSimpleName());
        stateTimer = 0.0f;
        keyReleased = false;
        guy.currentState = this;
        guy.swordAttack();
        guy.swordState.isActive = true;
    }
}
