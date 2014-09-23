package com.ads.puzzle.disnep.controller;

import com.ads.puzzle.disnep.Assets;
import com.ads.puzzle.disnep.actors.Piece;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Administrator on 2014/7/4.
 */
public class PieceController extends IController {

    private Rectangle fixArea;

    public PieceController(String name) {
        setName(name);
        for (int i = 0; i < 4; i++) {
            Piece piece = new Piece(i);
            if (i == 2) {
                fixArea = new Rectangle(piece.getX(), piece.getY(), Assets.SMALL_PIECE_SIZE * 2, Assets.SMALL_PIECE_SIZE * 2);
            }
            addActor(piece);
        }
    }

    @Override
    public void handler() {
        SnapshotArray<Actor> actors = getChildren();
        for (int i=0; i<actors.size; i++) {
            Actor actor = actors.get(i);
            Piece piece = (Piece) actor;
            piece.return2BeginArea();
        }
    }

    public Rectangle getFixAreaBounds() {
        return fixArea;
    }
}
