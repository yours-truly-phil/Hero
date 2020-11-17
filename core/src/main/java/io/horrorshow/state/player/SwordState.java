package io.horrorshow.state.player;

import com.badlogic.gdx.math.Vector2;
import io.horrorshow.state.Direction;

public class SwordState {
    public boolean isActive = false;
    public Vector2 position = new Vector2();

    public void update(Vector2 rootPos, Direction offsetDirection, float offset) {
        position.x = rootPos.x;
        position.y = rootPos.y;
        switch (offsetDirection) {
            case DOWN:
                position.y -= offset;
                break;
            case RIGHT:
                position.x += offset;
                break;
            case LEFT:
                position.x -= offset;
                break;
            case UP:
                position.y += offset;
                break;
        }
    }
}
