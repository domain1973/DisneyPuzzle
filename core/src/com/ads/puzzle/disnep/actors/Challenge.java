package com.ads.puzzle.disnep.actors;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.Assets;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.List;

/**
 * Created by Administrator on 2014/7/5.
 */
public class Challenge extends Group {
    private final float imageSize = Assets.PIECE_SIZE / 3;

    public Challenge(int level, int gateNum) {
        float space = Assets.WIDTH / 6;
        Integer[] bmpIds = Answer.CHALLENGES[gateNum];
        List<Sprite> sprites = Assets.levelSpriteMap.get(level);
        float x_off = Assets.WIDTH / 40;
        float y_off = Assets.TOPBAR_HEIGHT;
        int off =  bmpIds.length - 1;
        for (int i = off; i >= 0; i--) {
            Image image = new Image(sprites.get(bmpIds[i]));
            float y = y_off + (- i / 3 + 2) * space;
            float x = x_off + i % 3 * space;
            image.setBounds(x, y, imageSize, imageSize);
            addActor(image);
        }
    }
}
