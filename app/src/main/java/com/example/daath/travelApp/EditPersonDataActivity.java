package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditPersonDataActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backArrow;
    private EditText nickname, realName, phone, description;
    private TextView save;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_data);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        initViewId();
        setViewEvent();
    }

    public static void anotherActionStart(Context context, User user) {
        Intent intent = new Intent(context, EditPersonDataActivity.class);
        intent.putExtra("currentUser", user);
        context.startActivity(intent);
    }

    public void initViewId() {
        backArrow = (ImageView) findViewById(R.id.editData_backArrow);
        nickname = (EditText) findViewById(R.id.editData_nickname);
        realName = (EditText) findViewById(R.id.editData_realName);
        phone = (EditText) findViewById(R.id.editData_phone);
        description = (EditText) findViewById(R.id.editData_description);
        save = (TextView) findViewById(R.id.editData_save);
    }

    public void setViewEvent() {
        nickname.setText(currentUser.getNickname());
        realName.setText(currentUser.getRealName());
        phone.setText(currentUser.getPhone());
        description.setText(currentUser.getDescription());
        backArrow.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editData_backArrow:
                finish();
                break;
            case R.id.editData_save:
                RequestParams params = new RequestParams();
                params.put("nickname", nickname.getText().toString());
                params.put("realName", realName.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("description", description.getText().toString());
                Log.d("Tag_params", params.toString());
                final AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                client.post("user/update/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            Log.d("Tag_result", "" + result);
                            FileOperation fileOperation = new FileOperation();
                            fileOperation.fileSave(EditPersonDataActivity.this, result.toString());
                            Toast.makeText(EditPersonDataActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                            finish();
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
