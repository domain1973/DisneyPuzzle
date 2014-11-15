package com.ads.puzzle.disnep.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ads.puzzle.disnep.Settings;

import net.umipay.android.GameParamInfo;
import net.umipay.android.UmiPaySDKManager;
import net.umipay.android.UmiPaymentInfo;
import net.umipay.android.UmipayOrderInfo;
import net.umipay.android.UmipaySDKStatusCode;
import net.umipay.android.interfaces.InitCallbackListener;
import net.umipay.android.interfaces.OrderReceiverListener;
import net.umipay.android.interfaces.PayProcessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/10.
 */
public class PayImpl implements InitCallbackListener,
        OrderReceiverListener {
    private AndroidLauncher androidLauncher;
    private int buyStarNum = 10;

    public PayImpl(AndroidLauncher launcher) {
        androidLauncher = launcher;
        initPay();
        initPayProcessListener();
    }

    /**
     * TODO 替换初始化支付平台
     * 初始化安全支付sdk
     */
    private void initPay() {
        //初始化参数
        GameParamInfo gameParamInfo = new GameParamInfo();
        //您的应用的AppId
        gameParamInfo.setAppId("8a07bc49c7547523");
        //您的应用的AppSecret123fc859610f597d
        gameParamInfo.setAppSecret("e93c8fd55e9f13ba");
        //false 订单充值成功后是使用服务器通知 true 订单充值成功后使用客户端回调
        gameParamInfo.setSDKCallBack(true);
        //调用sdk初始化接口
        UmiPaySDKManager.initSDK(androidLauncher, gameParamInfo, this, this);
    }

    /**
     * TODO 替换初始化支付平台回调
     * 初始化支付动作回调接口
     */
    private void initPayProcessListener() {
        UmiPaySDKManager.setPayProcessListener(new PayProcessListener() {

            @Override
            public void OnPayProcess(int code) {
                switch (code) {
                    case UmipaySDKStatusCode.PAY_PROCESS_SUCCESS:
                        break;
                    case UmipaySDKStatusCode.PAY_PROCESS_FAIL:
                        break;
                }
            }
        });
    }

    /**
     * 初始化回调接口
     */
    @Override
    public void onInitCallback(int code, String msg) {
        if (code == UmipaySDKStatusCode.SUCCESS) {
            // 初始化成功后，即可正常调用充值
        } else if (code == UmipaySDKStatusCode.INIT_FAIL) {
            // 初始化失败，一般在这里提醒用户网络有问题，反馈，等等问题
            Toast.makeText(androidLauncher, "初始化失败", Toast.LENGTH_SHORT).show();
        } else if (code == 15) {
            // 网络错误
            Toast.makeText(androidLauncher, "网络连接错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * TODO 替换初始化支付平台返回订单
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
                    Settings.adManager = false;
                    Settings.helpNum = Settings.helpNum + buyStarNum;
                    androidLauncher.handler.post(new Runnable() {
                        public void run() {
                            new AlertDialog.Builder(androidLauncher).setTitle(Constant.SHARE_TITLE).setMessage("购买完成！请重新启动游戏,去广告才有效.")
                                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).setIcon(com.ads.puzzle.disnep.android.R.drawable.xiaozi).create().show();
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

    //TODO 替换不同的支付平台代码
    public void buy(int starNum, int  yuan) {
        //设置充值信息
        UmiPaymentInfo paymentInfo = new UmiPaymentInfo();
        //业务类型，SERVICE_TYPE_QUOTA(固定额度模式，充值金额在支付页面不可修改)，SERVICE_TYPE_RATE(汇率模式，充值金额在支付页面可修改）
        paymentInfo.setServiceType(UmiPaymentInfo.SERVICE_TYPE_QUOTA);
        //定额支付金额，单位RMB
        paymentInfo.setPayMoney(yuan);
        //订单描述
        buyStarNum = starNum;
        paymentInfo.setDesc(starNum + "颗智慧星 + 去广告");
        //调用支付接口
        UmiPaySDKManager.showPayView(androidLauncher, paymentInfo);
    }
}
