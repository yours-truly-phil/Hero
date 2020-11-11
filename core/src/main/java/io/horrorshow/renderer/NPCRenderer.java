package io.horrorshow.renderer;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.horrorshow.state.Direction;
import io.horrorshow.state.HasDynamicBody;

import java.util.HashMap;
import java.util.Map;

import static io.horrorshow.Hero.PPM;
import static io.horrorshow.state.Direction.DOWN;

public class NPCRenderer implements Renderer {

    private final Map<Direction, TextureRegion> standTexture = new HashMap<>();
    private final Map<Direction, Animation<TextureRegion>> walkAnimations = new HashMap<>();
    private final HasDynamicBody body;
    private final Sprite sprite = new Sprite();
    private final Vector2 offset;

    public NPCRenderer(HasDynamicBody body, TextureAtlas.AtlasRegion region,
                       Direction[] texYOrder, int width, int height, Vector2 offset) {
        this.body = body;
        this.offset = offset;

        for (int i = 0; i < texYOrder.length; i++) {
            standTexture.put(texYOrder[i],
                    new TextureRegion(region, 0, i * height, width, height));
        }

        Array<TextureRegion> frames = new Array<>();
        for (int y = 0; y < texYOrder.length; y++) {
            for (int x = 1; x < 4; x++) {
                frames.add(new TextureRegion(region, x * width, y * height, width, height));
            }
            walkAnimations.put(texYOrder[y], new Animation<>(0.1f, frames));
            frames.clear();
        }

        sprite.setBounds(0, 0, width / PPM, height / PPM);
        sprite.setRegion(standTexture.get(DOWN));
    }

    @Override
    public void render(SpriteBatch batch) {
        var pos = body.getBody().getPosition();
        sprite.setPosition(pos.x - sprite.getWidth() / 2 + offset.x / PPM,
                pos.y - sprite.getHeight() / 2 + offset.y / PPM);
        sprite.setRegion(getFrame());
        sprite.draw(batch);
    }

    private TextureRegion getFrame() {
        if (body.isInMotion()) {
            return walkAnimations.get(body.getDirection()).getKeyFrame(body.stateTimer(), true);
        } else {
            return standTexture.get(body.getDirection());
        }
    }
}
