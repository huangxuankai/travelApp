package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daath.travelApp.customClass.ActivityCollector;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener{

    private LinearLayout wholeLayout;
    private EditText signInAccount;
    private EditText signInPassword;
    private TextView signUp;
    private Button signIn;

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
        setContentView(R.layout.activity_sign_in);
        wholeLayout = (LinearLayout) findViewById(R.id.signIn_wholeLayout);
        signInAccount = (EditText) findViewById(R.id.signIn_account);
        signInPassword = (EditText) findViewById(R.id.signIn_password);
        signInAccount.setOnFocusChangeListener(this);
        signInPassword.setOnFocusChangeListener(this);
        Intent intent = getIntent();
        String account = intent.getStringExtra("Account");
        Log.d("Tag", "" + account);
        if (account != null) {
            signInAccount.setText(account);
        }
        signIn = (Button) findViewById(R.id.button_signIn);
        signUp = (TextView) findViewById(R.id.text_signUp);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        ActivityCollector.addActivity(this);
    }

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }

    //让EditText点击hint预设字消失
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText et = (EditText) v;
        Log.d("Tag_Focus", "" + v);
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
            case R.id.button_signIn:
                signInAccount.clearFocus();
                signInPassword.clearFocus();
                // TODO 可能需进一步校验，例如长度等
                if (signInAccount.length() == 0) {
                    signInAccount.setError("账号为空", getWarningIcon());
                    signInAccount.requestFocus();
                    break;
                }
                if (signInPassword.length() == 0) {
                    signInPassword.setError("密码为空", getWarningIcon());
                    signInPassword.requestFocus();
                    break;
                }
                final String inputAccount = signInAccount.getText().toString();
                String inputPassword = signInPassword.getText().toString();
                RequestParams params = new RequestParams();
                params.put("account", inputAccount);
                params.put("password", inputPassword);
                AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                client.post("user/signIn/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody){
                        try {
                            JSONObject result = responseBody.optJSONObject("result");
                            if (result == null) {
                                switch (responseBody.getInt("msgCode")) {
                                    case 1002:
                                        Log.d("Tag_msg", "" + responseBody);
                                        signInAccount.setError("未注册用户", getWarningIcon());
                                        signInAccount.requestFocus();
                                        break;
                                    case 1003:
                                        Log.d("Tag_msg", "" + responseBody);
                                        signInPassword.setError("密码错误", getWarningIcon());
                                        signInPassword.requestFocus();
                                        break;
                                    default: break;
                                }
                            } else {
                                Log.d("Tag_result", "" + result);
                                FileOperation fileOperation = new FileOperation();
                                fileOperation.fileSave(SignInActivity.this, result.toString());
                                MainHomeActivity.anotherActionStart(SignInActivity.this);
                                ActivityCollector.finishAll();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Tag_failure", "无效请求");
                        Log.d("Tag_failure", "" + errorResponse);
                    }
                });
                break;
            case R.id.text_signUp:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            default: break;
        }
    }
}
