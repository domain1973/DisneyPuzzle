package com.ads.puzzle.disnep.screen;

import com.ads.puzzle.disnep.Assets;
import com.ads.puzzle.disnep.Puzzle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Administrator on 2014/9/1.
 */
public class ReadmeScreen extends OtherScreen {
    private Image readmeImage;
    private MainScreen mainScreen;
    private GameScreen gameScreen;

    private ReadmeScreen(Puzzle game) {
        super(game);
        readmeImage = new Image(Assets.readme);
        float imageSize = Assets.WIDTH *20/ 21;
        readmeImage.setBounds((Assets.WIDTH - imageSize)/2, (Assets.HEIGHT - imageSize)/2, imageSize, imageSize);
    }

    public ReadmeScreen(Puzzle game, MainScreen ms) {
        this(game);
        mainScreen = ms;
    }

    public ReadmeScreen(Puzzle game, GameScreen gs) {
        this(game);
        gameScreen = gs;
    }

    @Override
    public void show() {
        super.show();
        addActor(readmeImage);
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gameScreen != null) {
                    getPuzzle().setScreen(gameScreen);
                } else if (mainScreen != null) {
                    getPuzzle().setScreen(mainScreen);
                } else if (gameScreen != null) {
                    getPuzzle().setScreen(gameScreen);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        getStarLabel().setText("总计:" + getStarNum());
    }
}
