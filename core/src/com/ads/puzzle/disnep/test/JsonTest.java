package com.ads.puzzle.disnep.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Administrator on 2014/8/12.
 */
public class JsonTest {

    public static void main(String[] args) {
        try {
            Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
