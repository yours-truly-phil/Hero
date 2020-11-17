package io.horrorshow.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import io.horrorshow.tools.B2DWorldCreator;
import io.horrorshow.tools.OrthogonalTiledMapRendererBleeding;
import io.horrorshow.tools.WorldContactListener;

import static io.horrorshow.Hero.PPM;

public class B2DWorld implements Disposable {
    public World world;
    public TiledMap map;
    B2DWorldCreator worldCreator;
    OrthogonalTiledMapRenderer renderer;
    Box2DDebugRenderer b2dr;

    public B2DWorld(String tmxLevel) {
        worldCreator = new B2DWorldCreator();
        map = worldCreator.loadTmxMap(tmxLevel);
        renderer = new OrthogonalTiledMapRendererBleeding(map, 1 / PPM);
        world = worldCreator.createWorld(map, new Vector2(0, 0)); // no gravity along x/y
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();
    }

    public void update(float dt) {
        world.step(dt, 6, 2);
    }

    public void renderBackground(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 3}); // layer 2 hold tiles for objects in open state
    }

    public void renderForeground(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(new int[]{4});
    }

    public void renderDebug(OrthographicCamera camera) {
        b2dr.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        renderer.dispose();
        map.dispose();
        b2dr.dispose();
    }
}
