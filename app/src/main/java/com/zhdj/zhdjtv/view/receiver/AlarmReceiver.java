package com.zhdj.zhdjtv.view.receiver;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.blankj.utilcode.util.DeviceUtils;
import com.hisilicon.android.tvapi.impl.SystemManagerImpl;
import com.zhdj.zhdjtv.global.MyRequestCode;
import com.zhdj.zhdjtv.retrofit.RetrofitUtils;
import com.zhdj.zhdjtv.rxjava.BaseObserver;
import com.zhdj.zhdjtv.rxjava.CommonSchedulers;
import com.zhdj.zhdjtv.view.service.GetMessageService;
import com.zhdj.zhdjtv.view.service.GetSkinService;
import com.zhdj.zhdjtv.view.service.GetTimeService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AlarmReceiver
 * @Author dongxueqiang
 * @Date 2020/7/13 19:52
 * @Title
 */
public class AlarmReceiver extends BroadcastReceiver {
    private DevicePolicyManager policyManager;
    private ComponentName adminReceiver;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        Log.i("www", "action = " + action);
        if (action == MyRequestCode.INTENT_ALARM_COLSE) {
//            Log.i("www", "我熄屏了");
            //设备使用状态上传
            setTerminalStatus(0);
            SystemManagerImpl.getInstance(context).shutDown();
        } else if (action == MyRequestCode.INTENT_ALARM_OPEN) {
//            GalleryNative.setOnTime_RTC(1000);
//            //设备使用状态上传
//            setTerminalStatus(1);
        } else if (action == MyRequestCode.INTENT_ALARM_TIME) {
            context.startService(new Intent(context, GetTimeService.class));
        } else if (action == MyRequestCode.INTENT_ALARM_MESSAGE) {
            context.startService(new Intent(context, GetMessageService.class));
        } else if (action == MyRequestCode.INTENT_ALARM_SKIN) {
            context.startService(new Intent(context, GetSkinService.class));
        }
    }

    /**
     * @param function_type 1=开机，0=关机
     */
    public void setTerminalStatus(int function_type) {
        Map<String, Object> map = new HashMap<>();
        map.put("s", "App.PublishFunction.FunctionNotice");
        map.put("function_type", function_type);
        RetrofitUtils.getApiService()
                .setTerminalStatus(map)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object data) {

                    }

                    @Override
                    protected boolean showExceptionMsg() {
                        return false;
                    }

                    @Override
                    protected boolean showErrorMsg() {
                        return false;
                    }
                });
    }


    public void shutdown(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        Class<PowerManager> clz = PowerManager.class;
        try {
            @SuppressLint("SoonBlockedPrivateApi") Method method = clz.getDeclaredMethod("shutdown", boolean.class, String.class, boolean.class);
            method.invoke(powerManager, false, null, false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
