package com.ads.puzzle.disnep.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.Puzzle;
import com.ads.puzzle.disnep.Settings;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

public class AndroidLauncher extends AndroidApplication {
    private PayImpl pay;
    private PEventImpl pEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // 创建libgdx视图
        pEvent = new PEventImpl(this);
        View gameView = initializeForView(new Puzzle(pEvent), config);
        // 创建布局
        RelativeLayout layout = new RelativeLayout(this);
        // 添加libgdx视图
        layout.addView(gameView);
        setContentView(layout);

        loadGameConfig();
        AdManager.getInstance(getContext()).setUserDataCollect(true);
        if (Settings.adManager) {
            addAdManager();
            spot();
        }
        pay = new PayImpl(this);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void addAdManager() {
        //banner广告
        AdManager.getInstance(this).init("123fc859610f597d", "13be68a226b9f94c", false);
        // 实例化LayoutParams(重要)
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
        // 调用Activity的addContentView函数
        addContentView(new AdView(this, AdSize.FIT_SCREEN), layoutParams);
    }

    private void spot() {
        // 展示插播广告，可以不调用loadSpot独立使用
        SpotManager.getInstance(
                this).showSpotAds(
                this,
                new SpotDialogListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.i("YoumiAdDemo", "展示成功");
                    }

                    @Override
                    public void onShowFailed() {
                        Log.i("YoumiAdDemo", "展示失败");
                    }

                }
        );
    }

    private void loadGameConfig() {
        SharedPreferences sharedata = getSharedPreferences("data", Context.MODE_PRIVATE);
        Settings.musicEnabled = sharedata.getBoolean("music", true);
        Settings.soundEnabled = sharedata.getBoolean("sound", true);
        Settings.unlockGateNum = sharedata.getInt("passNum", 0);
        Settings.helpNum = sharedata.getInt("helpNum", 3);
        Answer.gateStars.clear();
        String[] split = sharedata.getString("starNum", "0").split("[,]");
        for (String starNum : split) {
            if (!"".equals(starNum)) {
                Answer.gateStars.add(Integer.parseInt(starNum));
            }
        }
        Settings.adManager = sharedata.getBoolean("adManager", true);
    }

    public PayImpl getPay() {
        return pay;
    }
}
