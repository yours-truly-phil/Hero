package io.horrorshow.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import io.horrorshow.objects.Guy;
import io.horrorshow.objects.Potty;
import text.formic.Stringf;

import static io.horrorshow.Hero.DOOR_BIT;
import static io.horrorshow.Hero.PPM;

public class InteractiveDoor extends InteractiveTileObject {

    private Rectangle boundsCells;

    public InteractiveDoor(World world, TiledMap map, RectangleMapObject object) {
        super(world, map, object);
        fixture.setUserData(this);
        setCategoryFilter(DOOR_BIT);
    }

    public void setBoundsCells(Rectangle rectangle) {
        this.boundsCells = rectangle;
    }

    @Override
    public void onContact(Potty potty) {

    }

    @Override
    public void onContact(Guy guy) {
        switchCells();
    }

    private void switchCells() {
        Gdx.app.log("onContact", "contact new door");
        var openLayer = (TiledMapTileLayer) map.getLayers().get("Open");
        var displayLayer = (TiledMapTileLayer) map.getLayers().get("Mutables");
        for (int y = (int) (boundsCells.y / PPM); y < boundsCells.y / PPM + boundsCells.height / PPM; y++) {
            for (int x = (int) (boundsCells.x / PPM); x < boundsCells.x / PPM + boundsCells.width / PPM; x++) {
                Gdx.app.log("x/y", Stringf.format("(%d,%d)", x, y));
                var openTile = openLayer.getCell(x, y).getTile();
                var closedTile = displayLayer.getCell(x, y).getTile();
                displayLayer.getCell(x, y).setTile(openTile);
                openLayer.getCell(x, y).setTile(closedTile);
            }
        }
    }
}
