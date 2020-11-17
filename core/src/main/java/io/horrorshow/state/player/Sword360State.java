package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import io.horrorshow.objects.Guy;
import io.horrorshow.state.Direction;
import text.formic.Stringf;

import static io.horrorshow.state.Direction.*;

public class Sword360State implements PlayerState {

    private final Direction[] dirOrder = {UP, RIGHT, DOWN, LEFT};
    public float duration = 0.4f;
    private float stateTimer = 0.0f;

    @Override
    public void update(Guy guy, float dt) {
        stateTimer += dt;
        if (stateTimer > duration) {
            guy.defaultState.enterState(guy);
        }
        guy.swordState.update(guy.b2body.getPosition(),
                getCurSwordDirection(guy), 1.5f);
    }

    private Direction getCurSwordDirection(Guy guy) {
        int baseDirIdx = 0;
        for (int i = 1; i < 4; i++) {
            if (guy.orientation == dirOrder[i]) {
                baseDirIdx = i;
                break;
            }
        }
        var relIdx = (int) (Math.floor((stateTimer / duration) * dirOrder.length));
        var dirIdx = (baseDirIdx + relIdx) % dirOrder.length;
        Gdx.app.log("SwordState",
                Stringf.format("dirIdx: %d st: %.2f bdi: %d relIdx: %d",
                        dirIdx, stateTimer, baseDirIdx, relIdx));
        return dirOrder[dirIdx];
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
