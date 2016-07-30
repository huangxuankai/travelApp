package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AuthorityApplyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button applyGuide;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_apply);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        applyGuide = (Button) findViewById(R.id.authority_applyGuide);
        applyGuide.setOnClickListener(this);
        applyGuide.setClickable(false);
        if (currentUser.getStatus().equals("user")) {
            applyGuide.setClickable(true);
            applyGuide.setBackgroundResource(R.drawable.button_round_corner);
        }
    }

    public static void anotherActionStart(Context context, User user) {
        Intent intent = new Intent(context, AuthorityApplyActivity.class);
        intent.putExtra("currentUser", user);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        AppRestClient client = new AppRestClient();
        client.saveCookie(this);
        switch (v.getId()) {
            case R.id.authority_applyGuide:
                client.post("user/applyGuide/", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            FileOperation fileOperation = new FileOperation();
                            fileOperation.fileSave(AuthorityApplyActivity.this, result.toString());
                            Toast.makeText(AuthorityApplyActivity.this, "导游申请已经提交，等待审核", Toast.LENGTH_SHORT).show();
                            applyGuide.setClickable(false);
                            applyGuide.setBackgroundResource(R.drawable.button_round_corner_disable);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
            default: break;
        }
    }
}
