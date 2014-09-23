package com.ads.puzzle.disnep.window;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.Assets;
import com.ads.puzzle.disnep.Puzzle;
import com.ads.puzzle.disnep.Settings;
import com.ads.puzzle.disnep.controller.AreaController;
import com.ads.puzzle.disnep.controller.ChallengeController;
import com.ads.puzzle.disnep.controller.IController;
import com.ads.puzzle.disnep.controller.PieceController;
import com.ads.puzzle.disnep.screen.GameScreen;
import com.ads.puzzle.disnep.screen.GateScreen;
import com.ads.puzzle.disnep.screen.LevelScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2014/7/20.
 */
public class ResultWin extends BaseWin {
    private GameScreen gameScreen;
    private float w;
    private float x;
    private float y;
    private float space;
    private Image[] star_nulls;
    private Image[] stars;
    private ScheduledExecutorService executStarCount;
    private ParticleEffect effect;
    private int starIndex;
    private int starNum;
    private boolean exeTimer = false;
    private boolean end = false;
    private ImageButton gateBtn;
    private ImageButton refresh;
    private ImageButton next;
    private Group group;
    private ChallengeController challengeCtrl;

    public ResultWin(GameScreen gs, int num) {
        super(Answer.TITLES[num], new Window.WindowStyle(gs.getGameFont(), Color.WHITE, new TextureRegionDrawable(
                Assets.resultBg)));
        starNum = num;
        gameScreen = gs;
        create();
    }

    public void create() {
        gameScreen.getStage().addActor(layerBg);
        w = Assets.WIDTH * 4 / 5;
        x = (Assets.WIDTH - w) / 2;
        y = (Assets.HEIGHT - w) / 2;
        space = w / 5;
        setBounds(x, y, w, w);
        addButtons();
        addListeners();
        addStars();
        addActor(gateBtn);
        addActor(refresh);
        if (starNum > 0) {
            addActor(next);
        }
        initEffect();
        group = gameScreen.getStage().getRoot();
        challengeCtrl = (ChallengeController) group.findActor(IController.CHALLENGE_CTRL);
    }

    private void addButtons() {
        float btn_size = space;
        float v = (w - btn_size) / 2;
        gateBtn = new ImageButton(new TextureRegionDrawable(Assets.gate), new TextureRegionDrawable(Assets.gate));
        gateBtn.setBounds(v - btn_size, 0, btn_size, btn_size);
        refresh = new ImageButton(new TextureRegionDrawable(Assets.refresh), new TextureRegionDrawable(Assets.refresh));
        refresh.setBounds(v, 0, btn_size, btn_size);
        next = new ImageButton(new TextureRegionDrawable(Assets.next), new TextureRegionDrawable(Assets.next));
        next.setBounds(v + btn_size, 0, btn_size, btn_size);
    }

    private void addListeners() {
        gateBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                layerBg.remove();
                gameScreen.getPuzzle().setScreen(new GateScreen(gameScreen.getPuzzle(), gameScreen.getLevel()));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        refresh.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                layerBg.remove();
                gameScreen.refreshGame();
                ResultWin.this.remove();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        next.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                layerBg.remove();
                ((AreaController) group.findActor(IController.AREA_CTRL)).handler();
                challengeCtrl.handler();
                ((PieceController) group.findActor(IController.PIECE_CTRL)).handler();
                int nextGateNum = challengeCtrl.getGateNum();
                gameScreen.setGateNum(nextGateNum);
                if (nextGateNum > Settings.unlockGateNum) {
                    Settings.unlockGateNum = nextGateNum;
                }
                if (Answer.gateStars.size() <= nextGateNum) {
                    Answer.gateStars.add(0);
                }
                ResultWin.this.remove();
                gameScreen.return2init();
                if (Answer.isLasterSmallGate(nextGateNum)) {
                    Puzzle puzzle = gameScreen.getPuzzle();
                    puzzle.setScreen(new LevelScreen(puzzle, nextGateNum / 11));
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void initEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/test.p"), Gdx.files.internal("data/"));
    }

    private void addStars() {
        float starW = space;
        float v = (w - starW) / 2;
        star_nulls = new Image[3];
        stars = new Image[3];
        float y1 = w * 3 / 5;
        for (int i = 0; i < 3; i++) {
            star_nulls[i] = new Image(Assets.star_null);
            stars[i] = new Image(Assets.star);
            int t = i - 1;
            float x1 = v + t * starW;
            star_nulls[i].setBounds(x1, y1, starW, starW);
            stars[i].setBounds(x1, y1, starW, starW);
            addActor(star_nulls[i]);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (starNum > 0) {
            if (!end) {
                if (!exeTimer) {
                    changeStar();
                    exeTimer = true;
                }
                if (starIndex < starNum) {
                    Image star = stars[starIndex];
                    float space = star.getWidth() / 2;
                    effect.setPosition(star.getX() + getX() + space, star.getY() + getY() + space);
                    effect.draw(batch, Gdx.graphics.getDeltaTime());
                    addActor(star);
                } else {
                    end = true;
                    Gdx.input.setInputProcessor(gameScreen.getStage());
                }
            }
        }
    }

    private void changeStar() {
        executStarCount = Executors.newSingleThreadScheduledExecutor();
        executStarCount.scheduleAtFixedRate( new Runnable() {
            public void run() {
                starIndex++; //
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }
}
