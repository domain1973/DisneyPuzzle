package com.ads.puzzle.disnep.test;

import com.ads.puzzle.disnep.Puzzle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Administrator on 2014/7/26.
 */
public class ParticleTest extends ScreenAdapter {
    private Puzzle game;
    SpriteBatch batch;
    Array emitters;
    ParticleEffect effect;
    InputProcessor inputProcessor;
    float Position_X, Position_Y;

    public ParticleTest(Puzzle game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/test.p"), Gdx.files.internal("data/"));

        inputProcessor = new InputProcessor() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                Position_X = screenX;
                Position_Y = Gdx.graphics.getHeight() - screenY;
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                // TODO Auto-generated method stub
                return false;
            }
        };


        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        effect.setPosition(Position_X, Position_Y);
        batch.begin();
        effect.draw(batch, delta);
        batch.end();
    }
}
