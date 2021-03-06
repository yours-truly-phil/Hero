package io.horrorshow.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.horrorshow.objects.tiles.InteractiveDoor;

import java.util.HashMap;
import java.util.Map;

import static io.horrorshow.Hero.PPM;

public class B2DWorldCreator {

    public static final String LAYER_STATIC_COLLISION = "walls";

    public TiledMap loadTmxMap(String tmxFile) {
        TmxMapLoader mapLoader = new TmxMapLoader();
        return mapLoader.load(tmxFile);
    }

    public World createWorld(TiledMap map, Vector2 gravity) {
        World world = new World(gravity, true);

        createDoors(map, world);

        addStaticWallsToWorld(map, world);

        return world;
    }

    private void createDoors(TiledMap map, World world) {
        Map<String, InteractiveDoor> doorMap = new HashMap<>();
        Array.ArrayIterator<RectangleMapObject> interactIter = new Array.ArrayIterator<>(
                map.getLayers().get("interact").getObjects().getByType(RectangleMapObject.class));
        while (interactIter.hasNext()) {
            var rectMapObj = interactIter.next();
            var props = rectMapObj.getProperties();
            if (props.containsKey("name")) {
                var name = props.get("name", String.class);
                var door = new InteractiveDoor(world, map, rectMapObj);
                doorMap.put(name, door);
            }
        }
        interactIter = new Array.ArrayIterator<>(
                map.getLayers().get("mutables").getObjects().getByType(RectangleMapObject.class));
        while (interactIter.hasNext()) {
            var rectMapObj = interactIter.next();
            var props = rectMapObj.getProperties();
            if (props.containsKey("name")) {
                var name = props.get("name", String.class);
                doorMap.get(name).setBoundsCells(rectMapObj.getRectangle());
            }
        }
    }

    private void addStaticWallsToWorld(TiledMap map, World world) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject object : map.getLayers().get(LAYER_STATIC_COLLISION).getObjects()) {
            if (object instanceof RectangleMapObject) {
                fdef.shape = getRectangle((RectangleMapObject) object);
            } else if (object instanceof EllipseMapObject) {
                fdef.shape = getEllipse((EllipseMapObject) object);
                if (fdef.shape.getRadius() == 0) {
                    Gdx.app.error("mapobject", "invalid ellipse object, radius 0");
                    continue;
                }
            } else if (object instanceof PolygonMapObject) {
                fdef.shape = getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                fdef.shape = getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                fdef.shape = getCircle((CircleMapObject) object);
            } else {
                continue;
            }

            bdef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bdef);

            body.createFixture(fdef);
        }
    }

    private Shape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        return polygon;
    }

    private Shape getEllipse(EllipseMapObject ellipseObject) {
        Ellipse ellipse = ellipseObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0f);
        if (ellipse.width != ellipse.height) {
            return circleShape;
        }
        circleShape.setRadius(ellipse.width / PPM / 2);
        circleShape.setPosition(new Vector2((ellipse.width / 2 + ellipse.x) / PPM,
                (ellipse.height / 2 + ellipse.y) / PPM));
        return circleShape;
    }

    private Shape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / PPM);
        circleShape.setPosition(new Vector2(circle.x / PPM, circle.y / PPM));
        return circleShape;
    }

    private Shape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private Shape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
