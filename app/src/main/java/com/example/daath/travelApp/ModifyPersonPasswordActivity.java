package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ModifyPersonPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backArrow;
    private EditText oldPassword, newPassword, repeatPassword;
    private TextView save;
    private String _id;

    /**
     * 设置一个EditView.setError()的警告图标
     * @return
     */
    private Drawable getWarningIcon() {
        Drawable icon = getResources().getDrawable(R.drawable.warning_display);
        icon.setBounds(0, 0, 100, 55);
        return  icon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_person_password);
        _id = getIntent().getStringExtra("modifyPasswordId");
        initViewId();
    }

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, ModifyPersonPasswordActivity.class);
        context.startActivity(intent);
    }

    private void initViewId() {
        backArrow = (ImageView) findViewById(R.id.modifyPassword_backArrow);
        oldPassword = (EditText) findViewById(R.id.modifyPassword_old);
        newPassword = (EditText) findViewById(R.id.modifyPassword_new);
        repeatPassword = (EditText) findViewById(R.id.modifyPassword_repeat);
        save = (TextView) findViewById(R.id.modifyPassword_save);
        backArrow.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modifyPassword_backArrow:
                finish();
                break;
            case R.id.modifyPassword_save:
                oldPassword.clearFocus();
                newPassword.clearFocus();
                repeatPassword.clearFocus();
                if (oldPassword.length() == 0) {
                    oldPassword.setError("旧密码不能为空", getWarningIcon());
                    oldPassword.requestFocus();
                    break;
                }
                if (newPassword.length() == 0) {
                    newPassword.setError("新密码不能为空", getWarningIcon());
                    newPassword.requestFocus();
                    break;
                }
                if (repeatPassword.length() == 0) {
                    repeatPassword.setError("重复密码不能为空", getWarningIcon());
                    repeatPassword.requestFocus();
                    break;
                }
                final String oldPass = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                final String repeatPass = repeatPassword.getText().toString();
                if (!newPass.equals(repeatPass)) {
                    repeatPassword.setError("重复密码不一致", getWarningIcon());
                    repeatPassword.requestFocus();
                    break;
                }
                AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                RequestParams params = new RequestParams();
                params.put("oldPassword", oldPass);
                params.put("newPassword", newPass);
                client.post("user/modifyPassword/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject result = response.optJSONObject("result");
                            if (result != null) {
                                Log.d("Tag_modifyPassword", "" + result);
                                Toast.makeText(ModifyPersonPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                switch (response.getInt("msgCode")) {
                                    case 1003:
                                        oldPassword.setError("旧密码不正确", getWarningIcon());
                                        oldPassword.requestFocus();
                                        break;
                                    default: break;
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default: break;
        }
    }
}
