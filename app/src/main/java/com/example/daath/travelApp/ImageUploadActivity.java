package com.example.daath.travelApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.ActivityCollector;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.Scene;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class ImageUploadActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView display, backArrow;
    private Button upload;
    private Bitmap cropAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        ActivityCollector.addActivity(this);
        cropAvatar = getIntent().getParcelableExtra("cropAvatar");
        display = (ImageView) findViewById(R.id.imageUpload_display);
        backArrow = (ImageView) findViewById(R.id.imageUpload_backArrow);
        upload = (Button) findViewById(R.id.imageUpload_upload);
        display.setOnClickListener(this);
        upload.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        cropAvatar = getIntent().getParcelableExtra("cropAvatar");
        display.setImageBitmap(cropAvatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageUpload_upload:
                AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                // 将Bitmap转成base64字节流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                cropAvatar.compress(Bitmap.CompressFormat.JPEG, 100, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] buffer = out.toByteArray();
                byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
                String photo = new String(encode);

                RequestParams params = new RequestParams();
                params.put("file", photo);
                client.post("user/avatarUpload/", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            Log.d("Tag_result", "" + result);
                            FileOperation fileOperation = new FileOperation();
                            fileOperation.fileSave(ImageUploadActivity.this, result.toString());
                            Toast.makeText(ImageUploadActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                            ActivityCollector.finishAll();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
            case R.id.imageUpload_backArrow:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            default: break;
        }
    }

    public static void anotherActionStart(Context context, Bitmap bitmap) {
        Intent intent = new Intent(context, ImageUploadActivity.class);
        intent.putExtra("cropAvatar", bitmap);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.removeActivity(this);
    }
}
