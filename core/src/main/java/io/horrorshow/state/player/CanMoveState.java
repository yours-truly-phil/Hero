package io.horrorshow.state.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.horrorshow.objects.Guy;
import io.horrorshow.state.Direction;

import static com.badlogic.gdx.Input.Keys.*;

abstract class CanMoveState implements PlayerState {
    protected Vector2 tmpV = new Vector2(0, 0);

    @Override
    public void update(Guy guy, float dt) {
        tmpV.set(0, 0);
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            tmpV.y += guy.WALK_IMPULSE;
            guy.orientation = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            tmpV.y -= guy.WALK_IMPULSE;
            guy.orientation = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            tmpV.x -= guy.WALK_IMPULSE;
            guy.orientation = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            tmpV.x += guy.WALK_IMPULSE;
            guy.orientation = Direction.RIGHT;
        }
        guy.move(tmpV);
    }
}
