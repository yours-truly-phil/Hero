package io.horrorshow.sprites.tiles;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.physics.box2d.World;
import io.horrorshow.Hero;
import io.horrorshow.sprites.Potty;

public class Door extends InteractiveTileObject {
    private TiledMapTileSet tileSet;
    public Door(World world, TiledMap map, RectangleMapObject object) {
        super(world, map, object);
        tileSet = map.getTileSets().getTileSet("overworld-tileset");
        fixture.setUserData(this);
        setCategoryFilter(Hero.DOOR_BIT);
    }

    @Override
    public void onContact(Potty potty) {

    }
}
