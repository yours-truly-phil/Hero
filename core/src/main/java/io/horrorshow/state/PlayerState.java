package io.horrorshow.state;

import io.horrorshow.objects.Guy;

public class PlayerState {
    // TODO proper state machine
    private final Guy player;
    private State state;
    private float stateTimer = 0.0f;

    public PlayerState(Guy player) {
        this.player = player;
        this.state = State.STAND;
    }

    public float stateTimer() {
        return stateTimer;
    }

    public void update(float dt) {
        stateTimer += dt;
        if (isDefaultState()) {
            selectDefaultState();
        }
        if (stateTimer > state.duration) {
            if (state == State.LIFT) {
                carry();
            } else {
                resetToDefault();
            }
        }
    }

    private void carry() {
        state = State.CARRY;
        stateTimer = 0.0f;
    }

    public boolean canMove() {
        return isDefaultState() || state == State.CARRY;
    }

    private boolean isDefaultState() {
        return state == State.WALK || state == State.STAND;
    }

    private void resetToDefault() {
        stateTimer = 0.0f;
        selectDefaultState();
    }

    private void selectDefaultState() {
        if (isMoving()) {
            state = State.WALK;
        } else {
            state = State.STAND;
        }
    }

    public boolean isMoving() {
        var vel = player.b2body.getLinearVelocity();
        return (Math.abs(vel.x) + Math.abs(vel.y)) > 0.4;
    }

    public void swordHit() {
        state = State.SWORD;
        stateTimer = 0.0f;
    }

    public State getState() {
        return state;
    }

    public void lift() {
        if (state == State.CARRY) {
            resetToDefault();
        } else {
            state = State.LIFT;
            stateTimer = 0.0f;
        }
    }

    public enum State {
        STAND(Float.MAX_VALUE),
        WALK(Float.MAX_VALUE),
        SWORD(0.4f),
        LIFT(0.9f),
        CARRY(Float.MAX_VALUE);

        public float duration;

        State(float duration) {
            this.duration = duration;
        }
    }
}
