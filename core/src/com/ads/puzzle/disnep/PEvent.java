package com.ads.puzzle.disnep;

import com.ads.puzzle.disnep.screen.GameScreen;
import com.ads.puzzle.disnep.screen.MainScreen;

/**
 * Created by Administrator on 2014/9/10.
 */
public abstract class PEvent {

    public abstract void pay();

    public abstract void exit(MainScreen ms);

    public abstract void sos(GameScreen gs);

    public abstract void invalidateSos();

    public abstract void resetGame();

    public abstract void share();

    public abstract void install(Series series);

    public abstract boolean isNetworkEnable();

    public abstract void save();

    public abstract void about();

    public abstract void netSlowInfo();

}
