package com.zhdj.zhdjtv.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.zhdj.zhdjtv.event.LiveEvent;
import com.zhdj.zhdjtv.global.MyRequestCode;
import com.zhdj.zhdjtv.global.SpConstant;
import com.zhdj.zhdjtv.model.BaseModel;
import com.zhdj.zhdjtv.model.LiveMessageModel;
import com.zhdj.zhdjtv.retrofit.RetrofitUtils;
import com.zhdj.zhdjtv.rxjava.BaseObserver;
import com.zhdj.zhdjtv.rxjava.CommonSchedulers;
import com.zhdj.zhdjtv.view.receiver.AlarmReceiver;

import java.util.ArrayList;


/**
 * @ClassName GetMessageService
 * @Author dongxueqiang
 * @Date 2020/7/16 19:15
 * @Title
 */
public class GetMessageService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.e("api", "getMessage");
                //执行获取排播信息的接口
                RetrofitUtils.getApiService()
                        .getPaibo()
                        .compose(CommonSchedulers.observableIo2Main())
                        .subscribe(new BaseObserver<LiveMessageModel>() {
                            @Override
                            protected void onSuccess(LiveMessageModel data) {
                                if (data != null && data.getList().size() != 0) {
//                                    if (data.get)
//                                    messageDao.deleteAll();
//                                    FileUtils.deleteAllInDir(UploadViewModel.getFileDirName());
                                    LiveEventBus.get(LiveEvent.REFRESH_MESSAGE).post(data);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                LiveMessageModel data = new LiveMessageModel();
                                data.setIs_change(1);
                                data.setRunning_state(0);
                                data.setRotation_time(SPUtils.getInstance().getInt(SpConstant.ROTATION_TIME, 3000));
                                data.setList(new ArrayList<>());
                                LiveEventBus.get(LiveEvent.REFRESH_MESSAGE).post(data);
                            }

                            @Override
                            protected void onFailure(BaseModel<LiveMessageModel> model) {

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
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 60 * 1000; // 这是十分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction(MyRequestCode.INTENT_ALARM_MESSAGE);
        PendingIntent pi = PendingIntent.getBroadcast(this, MyRequestCode.MESSAGE, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
