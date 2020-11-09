package io.horrorshow.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import io.horrorshow.objects.Guy;
import io.horrorshow.objects.Potty;
import io.horrorshow.objects.tiles.InteractiveTileObject;

import static io.horrorshow.Hero.*;
import static text.formic.Stringf.format;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        var fixA = contact.getFixtureA();
        var fixB = contact.getFixtureB();

        var bitsA = fixA.getFilterData().categoryBits;
        var bitsB = fixB.getFilterData().categoryBits;

        Gdx.app.log("BeginContact", format("BeginContact between %s and %s%n", getNameByBits(bitsA), getNameByBits(bitsB)));

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case POTTY_BIT | DOOR_BIT:
                if (bitsA == POTTY_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onContact((Potty) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onContact((Potty) fixB.getUserData());
                break;
            case GUY_BIT | DOOR_BIT:
                if (bitsA == GUY_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onContact((Guy) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onContact((Guy) fixB.getUserData());
                break;
        }
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
            case GUY_BIT:
                return "guy";
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
