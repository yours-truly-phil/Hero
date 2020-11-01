package io.horrorshow.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import static io.horrorshow.Hero.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        var fixA = contact.getFixtureA();
        var fixB = contact.getFixtureB();

        var bitsA = fixA.getFilterData().categoryBits;
        var bitsB = fixB.getFilterData().categoryBits;
        System.out.println("BeginContact between "
                + getNameByBits(bitsA) + " and " + getNameByBits(bitsB));

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
    }

    private String getNameByBits(short bitsA) {
        switch (bitsA) {
            case NOTHING_BIT:
                return "nothing";
            case STATIC_BIT:
                return "static";
            case POTTY_BIT:
                return "potty";
            case DOOR_BIT:
                return "door";
            default:
                return "unknown";
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
