package com.example.daath.travelApp.customClass;

import android.content.Context;

import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;

/**
 * Created by daath on 16-3-16.
 */
public class AppRestClient {
    // 以下访问后台地址自己视情况而定
//    private static final String BASE_URL = "http://192.168.0.106:8003/api/";        //个人电脑apache2端口
    private static final String BASE_URL = "http://192.168.2.107:8099/api/";          //django自带服务器端口
    private static List<Cookie> cookies;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public AppRestClient() {

        client.setConnectTimeout(10);//30s超时
        if (getCookies() != null) {//每次请求都要带上cookie
            BasicCookieStore bcs = new BasicCookieStore();
            bcs.addCookies(getCookies().toArray(
                    new Cookie[getCookies().size()]));
            client.setCookieStore(bcs);
        }
    }

    public static List<Cookie> getCookies() {
        return cookies != null? cookies: new ArrayList<Cookie>();
    }

    public static void setCookies(List<Cookie> cookies) {
        AppRestClient.cookies = cookies;
    }


    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void saveCookie(Context context) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);
    }

    public List<Cookie> getCookie(Context context){
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        List<Cookie> cookies = cookieStore.getCookies();
        return cookies;
    }

    public void clearCookie(Context context){
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        cookieStore.clear();
    }

}
