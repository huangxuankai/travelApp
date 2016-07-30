package com.example.daath.travelApp.customReceiver;

import android.content.Context;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.example.daath.travelApp.DaoService.NotifyDaoService;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.my.greenDao.bean.Notify;
import com.my.greenDao.dao.NotifyDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by daath on 16-4-25.
 */
public class MyBaiduPushReceiver extends PushMessageReceiver {

    @Override
    public void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId) {
        Log.d("Tag_baiduPush", "errorCode:" + errorCode);
        Log.d("Tag_baiduPush", "appId:" + appId);
        Log.d("Tag_baiduPush", "userId:" + userId);
        Log.d("Tag_baiduPush", "channelId:" + channelId);
        Log.d("Tag_baiduPush", "requestId:" + requestId);
        /*  百度云推送连接成功返回channelId，保存到用户表中    */
        if (errorCode == 0) {
            AppRestClient client = new AppRestClient();
            client.saveCookie(context);
            RequestParams params = new RequestParams();
            params.put("channelId", channelId);
            client.post("user/saveChannelId/", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    Log.e("Tag_channel_response", responseBody.toString());
                }
            });
        }
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {

    }

    /**
     * 百度推送通知接收
     */
    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContent) {
        Log.e("Tag_onNotifi", "title:" + title);
        Log.e("Tag_onNotifi", "description:" + description);
        Log.e("Tag_onNotifi", "customContent:" + customContent);
        /*  每接收到一个通知就存入本地SQLite3数据库        */
        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setDescription(description);
        notify.setIsRead(false);
        notify.setCreateDate(new Date());
        if (customContent.length() != 0) {
            try {
                JSONObject customContentJson = new JSONObject(customContent);
                notify.setNotifyContent(customContentJson.getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NotifyDaoService.getInstance(context).getNotifyDao().insert(notify);
    }
}
