package com.ads.puzzle.disnep.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ads.puzzle.disnep.Answer;
import com.ads.puzzle.disnep.PEvent;
import com.ads.puzzle.disnep.Puzzle;
import com.ads.puzzle.disnep.Settings;
import com.ads.puzzle.disnep.screen.GameScreen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import net.umipay.android.GameParamInfo;
import net.umipay.android.UmiPaySDKManager;
import net.umipay.android.UmiPaymentInfo;
import net.umipay.android.UmipayOrderInfo;
import net.umipay.android.UmipaySDKStatusCode;
import net.umipay.android.interfaces.InitCallbackListener;
import net.umipay.android.interfaces.OrderReceiverListener;
import net.umipay.android.interfaces.PayProcessListener;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements InitCallbackListener,
        OrderReceiverListener {
    private Handler handler;
    private boolean initFail;
    private String title = "您好,我是小智";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // 创建libgdx视图
        View gameView = initializeForView(new Puzzle(new PEvent() {

            @Override
            public void pay() {
                buyStar();
            }

            @Override
            public void exit() {
                handler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(AndroidLauncher.this).setTitle(title).setMessage("确定要退出游戏吗?")
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                save();
                                AndroidLauncher.this.exit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNeutralButton("爱迪出品", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
                    }
                });
            }

            @Override
            public void sos(final GameScreen gs) {
                handler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(AndroidLauncher.this).setTitle(title).setMessage("您还有" + Settings.helpNum + "次机会,需要帮助吗?")
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.helpNum = Settings.helpNum - 1;
                                gs.useSos();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
                    }
                });
            }

            @Override
            public void invalidateSos() {
                handler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(AndroidLauncher.this).setTitle(title).setMessage("智慧星不够,点击分享可以获取智慧星哦!?")
                                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setNeutralButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("购买智慧星", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buyStar();
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
                    }
                });
            }

            @Override
            public void resetGame() {
                handler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(AndroidLauncher.this).setTitle(title).setMessage("是否确定重置您的进度?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.unlockGateNum = 0;
                                Settings.helpNum = 3;
                                Answer.gateStars.clear();
                                Answer.gateStars.add(0);
                                save();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
                    }
                });
            }

            @Override
            public void spotAM() {
                spot();
            }
        }), config);
        // 创建布局
        RelativeLayout layout = new RelativeLayout(this);
        // 添加libgdx视图
        layout.addView(gameView);
        setContentView(layout);
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
        handler = new Handler();
        initSDK();
        initPayProcessListener();

        spot();
        load();
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
        ); // //
    }

    /**
     * 初始化安全支付sdk
     */
    private void initSDK() {
        //初始化参数
        GameParamInfo gameParamInfo = new GameParamInfo();
        //您的应用的AppId
        gameParamInfo.setAppId("123fc859610f597d");
        //您的应用的AppSecret
        gameParamInfo.setAppSecret("13be68a226b9f94c");

        //false 订单充值成功后是使用服务器通知 true 订单充值成功后使用客户端回调
        gameParamInfo.setSDKCallBack(true);

        //调用sdk初始化接口
        UmiPaySDKManager.initSDK(this, gameParamInfo, this, this);

    }

    /**
     * 初始化支付动作回调接口
     */
    private void initPayProcessListener() {
        UmiPaySDKManager.setPayProcessListener(new PayProcessListener() {

            @Override
            public void OnPayProcess(int code) {
                switch (code) {
                    case UmipaySDKStatusCode.PAY_PROCESS_SUCCESS:
                        //充值完成，不等于充值成功，实际充值结果以订单回调接口为准
                        Toast.makeText(getApplicationContext(), "充值完成！请耐心等候充值结果",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case UmipaySDKStatusCode.PAY_PROCESS_FAIL:
                        Toast.makeText(getApplicationContext(), "取消充值！",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }


    /**
     * 初始化回调接口
     */
    @Override
    public void onInitCallback(int code, String msg) {
        initFail = false;
        if (code == UmipaySDKStatusCode.SUCCESS) {
            // 初始化成功后，即可正常调用充值
            Toast.makeText(this, "初始化成功",
                    Toast.LENGTH_SHORT).show();
        } else if (code == UmipaySDKStatusCode.INIT_FAIL) {
            // 初始化失败，一般在这里提醒用户网络有问题，反馈，等等问题
            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
        } else if (code == 15) {
            initFail = true;
            // 网络错误
            Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接收到服务器返回的订单信息
     * ！！！注意，该返回是在非ui线程中回调，如果需要更新界面，需要手动使用主线刷新
     */
    @Override
    public List onReceiveOrders(List list) {
        //未处理的订单
        List<UmipayOrderInfo> newOrderList = list;
        //已处理的订单
        List<UmipayOrderInfo> doneOrderList = new ArrayList<UmipayOrderInfo>();
        //TODO 出来服务器返回的订单信息newOrderList，并将已经处理好充值的订单返回给sdk
        //TODO sdk将已经处理完的订单通知给服务器。服务器下次将不再返回游戏客户端已经处理过的订单
        for (UmipayOrderInfo newOrder : newOrderList) {
            try {
                //TODO 对订单order进行结算
                if (newOrder.getStatus() == 1) {
                    //注意，转成毫秒需要 乘以长整型1000L
                    String payTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", newOrder.getPayTime_s() * 1000L).toString();
                    final String tips = String.format(
                            "支付订单号:%s \n" +
                                    "外部订单号:%s \n" +
                                    "用户标记:%s \n" +
                                    "订单状态:%s \n" +
                                    "支付通道:%s \n" +
                                    "获得金币:%s \n" +
                                    "金额:%s￥ \n" +
                                    "支付时间:%s \n" +
                                    "自定义数据:%s \n",
                            newOrder.getOid(),
                            newOrder.getTradeNo(),
                            newOrder.getRid(),
                            newOrder.getStatus() + "",
                            newOrder.getPayType(),
                            newOrder.getAmount() + "",
                            newOrder.getRealMoney() + "",
                            payTime,
                            newOrder.getCData()
                    );
                    //在主线程更新界面
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
//                            String newtext = tips + mOrderResult_tv.getText().toString();
//                            mOrderResult_tv.setText(newtext);
                        }
                    });
                    //添加到已处理订单列表
                    doneOrderList.add(newOrder);
                }
            } catch (Exception e) {

            }
        }
        return doneOrderList;   //将已经处理过的订单返回给sdk，下次服务器不再返回这些订单
    }

    public void buyStar() {
        if (initFail) {
            // 网络错误
            // Toast.makeText(AndroidLauncher.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            handler.post(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(AndroidLauncher.this).setTitle(title).setMessage("连接不到网络,请检查哦!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).setIcon(R.drawable.xiaozi).create().show();
                }
            });
        } else {
            //设置充值信息
            UmiPaymentInfo paymentInfo = new UmiPaymentInfo();
            //业务类型，SERVICE_TYPE_QUOTA(固定额度模式，充值金额在支付页面不可修改)，SERVICE_TYPE_RATE(汇率模式，充值金额在支付页面可修改）
            paymentInfo.setServiceType(UmiPaymentInfo.SERVICE_TYPE_QUOTA);
            //定额支付金额，单位RMB
            paymentInfo.setPayMoney(1);
            //订单描述
            paymentInfo.setDesc("10颗智慧星");
            //调用支付接口
            UmiPaySDKManager.showPayView(this, paymentInfo);
        }
    }

    private void load() {
        SharedPreferences sharedata = getSharedPreferences("data", Context.MODE_PRIVATE);
        Settings.musicEnabled = sharedata.getBoolean("music", true);
        Settings.soundEnabled = sharedata.getBoolean("sound", true);
        Settings.unlockGateNum = sharedata.getInt("passNum", 0);
        Settings.helpNum = sharedata.getInt("helpNum", 3);
        String[] split = sharedata.getString("starNum", "0").split("[,]");
        for (String starNum : split) {
            if (!"".equals(starNum)) {
                Answer.gateStars.add(Integer.parseInt(starNum));
            }
        }
    }

    private void save() {
        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
        sharedata.putBoolean("music", Settings.musicEnabled);
        sharedata.putBoolean("sound", Settings.soundEnabled);
        sharedata.putInt("passNum", Settings.unlockGateNum);
        sharedata.putInt("helpNum", Settings.helpNum);
        StringBuffer sb = new StringBuffer();
        for (Integer starNum : Answer.gateStars) {
            sb.append(starNum).append(",");
        }
        sharedata.putString("starNum", sb.substring(0, sb.length() - 1));
        sharedata.commit();
    }

}
