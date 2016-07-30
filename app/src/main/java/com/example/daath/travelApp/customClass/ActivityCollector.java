package com.example.daath.travelApp.customClass;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daath on 16-4-3.
 */
public class ActivityCollector {

    public static List<AppCompatActivity> activities = new ArrayList<AppCompatActivity>();

    public static void addActivity(AppCompatActivity appCompatActivity) {
        activities.add(appCompatActivity);
    }

    public static void removeActivity(AppCompatActivity appCompatActivity) {
        activities.remove(appCompatActivity);
    }

    public static void finishAll() {
        for (AppCompatActivity appCompatActivity : activities) {
            if (!appCompatActivity.isFinishing()) {
                appCompatActivity.finish();
            }
        }
    }
}
