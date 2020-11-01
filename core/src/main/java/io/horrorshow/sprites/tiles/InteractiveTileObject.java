package io.horrorshow.sprites.tiles;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import io.horrorshow.Hero;
import io.horrorshow.sprites.Potty;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected RectangleMapObject object;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, RectangleMapObject object) {
        this.object = object;
        this.world = world;
        this.map = map;
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Hero.PPM,
                (bounds.getY() + bounds.getHeight() / 2) / Hero.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Hero.PPM, bounds.getHeight() / 2 / Hero.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onContact(Potty potty);

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(String layer) {
        var tileLayer = (TiledMapTileLayer) map.getLayers().get(layer);
        return tileLayer.getCell((int) body.getPosition().x, (int) body.getPosition().y);
    }
}
