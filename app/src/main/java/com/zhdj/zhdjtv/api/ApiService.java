package com.zhdj.zhdjtv.api;

import com.zhdj.zhdjtv.model.BaseModel;
import com.zhdj.zhdjtv.model.LiveMessageModel;
import com.zhdj.zhdjtv.model.MenuModel;
import com.zhdj.zhdjtv.model.MessageModel;
import com.zhdj.zhdjtv.model.SkinModel;
import com.zhdj.zhdjtv.model.TimeModel;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @ClassName ApiService
 * @Author dongxueqiang
 * @Date 2020/7/2 14:42
 * @Title
 */
public interface ApiService {
//    String API = "http://qd.zhihuiqilu.cn/";
    String API = "http://dj.qsmedia.org.cn/";

    /**
     * 获取开关机时间
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/?s=App.PublishFunction.FunctionDetail")
    Observable<BaseModel<List<TimeModel>>> getTime();
    /**
     * 获取是否是排播设备
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/?s=App.Terminal.TerminalUsed")
    Observable<BaseModel<Integer>> isPaibo();
    /**
     * 获取排播信息
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/?s=App.Terminal.TerminalDetail")
    Observable<BaseModel<LiveMessageModel>> getPaibo();
    /**
     * 获取主屏皮肤
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/?s=App.Module.DetailImgs")
    Observable<BaseModel<SkinModel>> getSkin();
    /**
     * 获取主界面菜单
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/?s=App.Module.ModuleList")
    Observable<BaseModel<List<MenuModel>>> getMainMenu();
    /**
     * 获取资讯
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/")
    Observable<BaseModel<List<MessageModel>>> getMessage(@QueryMap Map<String, Object> map);
    /**
     * 获取资讯
     *
     * @param
     * @return
     */
    @GET("http://qd.zhihuiqilu.cn/")
    Observable<BaseModel<MessageModel>> getMessageDetail(@QueryMap Map<String, Object> map);
    /**
     * 获取资讯
     *
     * @param
     * @return
     */
    @POST("http://qd.zhihuiqilu.cn/")
    Observable<BaseModel<Object>> setPlayTerminal(@QueryMap Map<String, Object> map);
    /**
     * 设置终端状态
     *
     * @param
     * @return
     */
    @POST("http://qd.zhihuiqilu.cn/")
    Observable<BaseModel<Object>> setTerminalStatus(@QueryMap Map<String, Object> map);

    /**
     * 下载文件
     */
    @GET
    @Streaming
    Observable<ResponseBody> downFile(@Url String fileUrl);
}
