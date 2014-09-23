package com.ads.puzzle.disnep.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Administrator on 2014/7/5.
 */
public class Help extends ImageButton {

    public Help(Drawable imageUp) {
        super(imageUp);
        this.setPosition(0, Gdx.graphics.getHeight() - imageUp.getMinHeight());
    }

}
