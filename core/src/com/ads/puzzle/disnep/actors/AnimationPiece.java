package com.ads.puzzle.disnep.actors;

import com.ads.puzzle.disnep.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Administrator on 2014/7/31.
 */
public class AnimationPiece extends Actor {

    private Animation animation;
    private float stateTime;

    public AnimationPiece() {
        super();
        animation = new Animation(0.2f, Assets.cubes);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        setWidth(Assets.SMALL_PIECE_SIZE);
        setHeight(Assets.SMALL_PIECE_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > 3f) {
            stateTime = 0;
        }
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        batch.draw(keyFrame, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
    }
}
