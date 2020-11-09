package io.horrorshow.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.World;
import io.horrorshow.Hero;
import io.horrorshow.objects.Guy;
import io.horrorshow.objects.Potty;

public class Door extends InteractiveTileObject {
    private final TiledMapTileLayer.Cell[][] cells = new TiledMapTileLayer.Cell[4][4];

    public Door(World world, TiledMap map, RectangleMapObject object) {
        super(world, map, object);
        fixture.setUserData(this);
        setCategoryFilter(Hero.DOOR_BIT);

        setCells();
    }

    private void setCells() {
        var x1 = (int) body.getPosition().x - 2;
        var y1 = (int) body.getPosition().y - 2;
        var layer = ((TiledMapTileLayer) map.getLayers().get("objects"));
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                cells[x][y] = layer.getCell(x1 + x, y1 + y);
            }
        }
    }

    @Override
    public void onContact(Potty potty) {
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                if (cells[x][y] != null) {
                    cells[x][y].setTile(null);
                } else {
                    Gdx.app.log("contact", text.formic.Stringf.format("cells[%d][%d] is null", x, y));
                }
            }
        }
    }

    @Override
    public void onContact(Guy potty) {
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                if (cells[x][y] != null) {
                    cells[x][y].setTile(null);
                } else {
                    Gdx.app.log("contact", text.formic.Stringf.format("cells[%d][%d] is null", x, y));
                }
            }
        }
    }
}
