package io.horrorshow.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import io.horrorshow.tools.B2DWorldCreator;

import static io.horrorshow.Hero.PPM;

public class B2DWorld implements Disposable {
    B2DWorldCreator worldCreator;
    public World world;
    OrthogonalTiledMapRenderer renderer;
    TiledMap map;
    Box2DDebugRenderer b2dr;

    public B2DWorld(String tmxLevel) {
        Box2D.init();
        worldCreator = new B2DWorldCreator();
        map = worldCreator.loadTmxMap(tmxLevel);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        world = worldCreator.createWorld(map, new Vector2(0, 0)); // no gravity along x/y
        b2dr = new Box2DDebugRenderer();
    }

    public void update(float dt) {
        world.step(dt, 6, 2);
    }

    public void render(OrthographicCamera camera, boolean debugRenderer) {
        renderer.setView(camera);
        renderer.render();

        if (debugRenderer) b2dr.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        renderer.dispose();
        map.dispose();
        b2dr.dispose();
    }
}
