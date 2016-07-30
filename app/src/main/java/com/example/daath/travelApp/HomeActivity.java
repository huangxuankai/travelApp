package com.example.daath.travelApp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daath.travelApp.customClass.ActivityCollector;

import java.io.Serializable;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signIn;
    private Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signIn = (Button) findViewById(R.id.home_signIn);
        signUp = (Button) findViewById(R.id.home_signUp);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        ActivityCollector.addActivity(this);
    }

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home_signIn:
                intent = new Intent();
                intent.setClass(HomeActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.home_signUp:
                intent = new Intent(HomeActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            default: break;
        }
    }
}
