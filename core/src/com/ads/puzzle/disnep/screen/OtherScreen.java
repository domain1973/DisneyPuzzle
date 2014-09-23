package com.ads.puzzle.disnep.screen;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.Assets;
import com.ads.puzzle.disnep.Puzzle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/7/27.
 */
public class OtherScreen extends BaseScreen {
    private ImageButton shareBtn;
    private Image star;
    private Label starLabel;

    public OtherScreen(Puzzle game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        createBtns();
    }

    protected void createBtns() {
        super.createBtns();
        shareBtn = new ImageButton(new TextureRegionDrawable(Assets.share));
        shareBtn.setBounds(Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        shareBtn.addListener(new InputListener() {
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
        star = new Image(new TextureRegionDrawable(Assets.star));
        star.setBounds(Assets.WIDTH - Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        Label.LabelStyle labelStyle = new Label.LabelStyle(getGameFont(), Color.WHITE); // 创建一个Label样式，使用默认白色字体
        starLabel = new Label("", labelStyle);
        starLabel.setBounds(Assets.WIDTH - 3*Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        addActor(starLabel);
        addActor(shareBtn);
        addActor(star);
    }

    protected Label getStarLabel() {
        return starLabel;
    }

    protected int getStarNum() {
        int starNum = 0;
        for (int num : Answer.gateStars) {
            starNum = starNum + num;
        }
        return starNum;
    }
}
