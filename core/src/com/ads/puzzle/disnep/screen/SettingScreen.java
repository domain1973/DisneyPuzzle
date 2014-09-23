package com.ads.puzzle.disnep.screen;

import com.ads.puzzle.disnep.Assets;
import com.ads.puzzle.disnep.Puzzle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/6/22.
 */
public class SettingScreen extends OtherScreen {
    private Puzzle game;

    public SettingScreen(Puzzle game) {
        super(game);
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().setScreen(getPuzzle().getMainScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });

        float otherSize = Assets.WIDTH /5;
        ImageButton musicBtn = new ImageButton(new TextureRegionDrawable(Assets.music), new TextureRegionDrawable(Assets.music));
        float x = (Assets.WIDTH - otherSize) / 2;
        float btn_y = Assets.HEIGHT * 2 / 3;
        musicBtn.setBounds(x, btn_y, otherSize, otherSize);
        ImageButton soundBtn = new ImageButton(new TextureRegionDrawable(Assets.sound), new TextureRegionDrawable(Assets.sound));
        float btn_y1 = btn_y -otherSize * 3 / 2;
        soundBtn.setBounds(x, btn_y1, otherSize, otherSize);
        ImageButton aboutBtn = new ImageButton(new TextureRegionDrawable(Assets.about), new TextureRegionDrawable(Assets.about));
        float btn_y2 = btn_y - otherSize * 3;
        aboutBtn.setBounds(x, btn_y2, otherSize, otherSize);
        ImageButton resetGameBtn = new ImageButton(new TextureRegionDrawable(Assets.resetGameBtn), new TextureRegionDrawable(Assets.resetGameBtn));
        float setSize = Assets.WIDTH /4;
        float btn_y3 = btn_y - setSize * 3.5f;
        resetGameBtn.setBounds((Assets.WIDTH - setSize) / 2, btn_y3, setSize, setSize);

        musicBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        resetGameBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().getPEvent().resetGame();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        getStarLabel().setText("总计:" + getStarNum());
        addActor(musicBtn);
        addActor(soundBtn);
        addActor(resetGameBtn);
        addActor(aboutBtn);
    }
}
