package com.example.daath.travelApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sendHttpRequest();
    }

    public void sendHttpRequest() {
        AppRestClient client = new AppRestClient();
        client.saveCookie(this);
        client.post("user/signCheck/", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    switch (response.getInt("msgCode")) {
                        case 2:
                            MainHomeActivity.anotherActionStart(WelcomeActivity.this);
                            finish();
                            break;
                        case 1004:
                            HomeActivity.anotherActionStart(WelcomeActivity.this);
                            finish();
                            break;
                        default: break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
