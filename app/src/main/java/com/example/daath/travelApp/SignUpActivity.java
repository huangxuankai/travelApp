package com.example.daath.travelApp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.ActivityCollector;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener{

    private EditText account;
    private EditText password;
    private EditText verifyPassword;
    private TextView signIn;
    private Button signUp;

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
        setContentView(R.layout.activity_sign_up);
        account = (EditText) findViewById(R.id.signUp_account);
        password = (EditText) findViewById(R.id.signUp_password);
        verifyPassword = (EditText) findViewById(R.id.signUp_verifyPassword);
        account.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        verifyPassword.setOnFocusChangeListener(this);
        signIn = (TextView) findViewById(R.id.text_signIn);
        signUp = (Button) findViewById(R.id.button_signUp);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        ActivityCollector.addActivity(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText et = (EditText) v;
        if (!hasFocus) {// 失去焦点
            et.setHint(et.getTag().toString());
        } else {
            String hint=et.getHint().toString();
            et.setTag(hint);//保存预设字
            et.setHint(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_signUp:
                account.clearFocus();
                password.clearFocus();
                verifyPassword.clearFocus();
                // TODO 可能需进一步校验，例如长度等
                if (account.length() == 0) {
                    account.setError("账号为空", getWarningIcon());
                    account.requestFocus();
                    break;
                }
                if (password.length() == 0) {
                    password.setError("密码为空", getWarningIcon());
                    password.requestFocus();
                    break;
                }
                String inputAccount = account.getText().toString();
                String inputPassword = password.getText().toString();
                String inputVerifyPassword = verifyPassword.getText().toString();
                if (!inputPassword.equals(inputVerifyPassword)) {
                    verifyPassword.setError("重复密码错误", getWarningIcon());
                    verifyPassword.requestFocus();
                    break;
                }
                RequestParams params = new RequestParams();
                params.put("account", inputAccount);
                params.put("password", inputPassword);
                AppRestClient client = new AppRestClient();
                client.post("user/signUp/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            switch (response.getInt("msgCode")) {
                                case 1001:
                                    Log.d("Tag_msg", "" + response);
                                    account.setError("该账号已被注册", getWarningIcon());
                                    account.requestFocus();
                                    break;
                                case 0:
                                    Log.d("Tag_msg", "" + response);
                                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    intent.putExtra("Account", account.getText().toString());
                                    startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
                break;
            case R.id.text_signIn:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            default: break;
        }
    }
}
