package com.ads.puzzle.disnep.controller;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.actors.Challenge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/7/5.
 */
public class ChallengeController extends IController {
    public static Map<Integer, Challenge> map = new HashMap<Integer, Challenge>();;
    private int level;
    private int gateNum;

    public ChallengeController(int level, int gateNum, String name) {
        setName(name);
        this.level = level;
        this.gateNum = gateNum;
        Challenge actor = getChallenge();
        actor.setDraw(true);
        addActor(actor);
    }

    @Override
    public void handler() {
        map.get(gateNum).setDraw(false);
        gateNum++;
        if (!map.containsKey(gateNum)) {
            if (Answer.isLasterSmallGate(gateNum)) {
                level++;
            }
            Challenge challenge = new Challenge(level, gateNum);
            map.put(gateNum, challenge);
            addActor(challenge);
        }
        map.get(gateNum).setDraw(true);
    }

    private Challenge getChallenge() {
        Challenge challenge = null;
        if (!map.containsKey(gateNum)) {
            challenge = new Challenge(level, gateNum);
            map.put(gateNum, challenge);
            addActor(challenge);
        } else {
            challenge = map.get(gateNum);
        }
        return challenge;
    }

    public int getGateNum() {
        return gateNum;
    }
}
