package com.example.daath.travelApp.myTestLayout;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.daath.travelApp.R;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FragmentAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MyTestActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button load, send;
    private LayoutInflater layoutInflater;
    private List<String> titles = new ArrayList<String >();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
//        List<Fragment> fragments = new ArrayList<Fragment>();
//        fragments.add(new SceneListFragment());
//        fragments.add(new UserListFragment());
//        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPage_home);
//        viewPager.setAdapter(fragmentAdapter);
//        initViewId();
//        initViewPage();
        load = (Button) findViewById(R.id.test_loadCookie);
        send = (Button) findViewById(R.id.test_sendCookie);
        load.setOnClickListener(this);
        send.setOnClickListener(this);


//        sendHttpRequest();
//        locationHttpRequest();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = locationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = locationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有开启定位", Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation(location);
        }
//        showLocation(location);
        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
    }


    public void showLocation(Location location) {
        Log.d("Tag_locationDetial", "----------");
        Log.d("Tag_latitude", "" + location.getLatitude());
        Log.d("Tag_longitude", "" + location.getLongitude());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void locationHttpRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        double latitude = 22.996013;
        double lontitude = 116.192608;
//        private String url = "http://api.map.baidu.com/geocoder/v2/?" +
//                "ak=?&callback=renderReverse&location=39.983424,116.322987&output=json&pois=1";
        String url = "http://api.map.baidu.com/geocoder/v2/?";
        url += "ak=Ea9L40vxVHRZ7TDsRKAp86KbrqQu3M01&";
        url += "location=" + latitude + "," + lontitude;
        url += "&output=json&pois=0";
        Log.d("Tag_locationUrl", url);
//        String xx = "http://api.map.baidu.com/geocoder/v2/?ak=Ea9L40vxVHRZ7TDsRKAp86KbrqQu3M01&callback=renderReverse&location=23.138768,113.377773&output=json&pois=0";
//        String ss = "http://api.map.baidu.com/geocoder/v2/?ak=Ea9L40vxVHRZ7TDsRKAp86KbrqQu3M01&callback=renderReverse&location=23.138768,113.377773&output=json&pois=0";
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Tag_location", "" + response);

            }
        });

    }

    public void sendHttpRequest() {
        AppRestClient client = new AppRestClient();
        client.post("comment/test/", null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Tag_test_resl", "" + response);
            }
        });
    }

//    private void initViewId () {
//        tabLayout = (TabLayout) findViewById(R.id.myTest_viewPageTabs);
//        viewPager = (ViewPager) findViewById(R.id.myTest_viewPageHome);
//    }

//    private void initViewPage(){
//
//        fragments.add(new Test1Fragment());
//        fragments.add(new Test2Fragment());
////        fragments.add(new Test3Fragment());
//        titles.add("标题1");
//        titles.add("标题2");
////        titles.add("标题3");
//        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
//        fragmentAdapter.setFragments(fragments);
//        fragmentAdapter.setTitles(titles);
//
//        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));//添加tab选项卡
//        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
////        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
//        viewPager.setAdapter(fragmentAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
//
//    }

    @Override
    public void onClick(View v) {
        AppRestClient client = new AppRestClient();
//        client.clearCookie(this);
        client.saveCookie(this);
        switch (v.getId()) {
            case R.id.test_sendCookie:
                client.post("comment/test/", null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tag_test", "" + response);

                    }
                });
                Log.d("Tag——Coo_loadCookie", "" + client.getCookie(this));
                break;
            case R.id.test_loadCookie:
                client.post("comment/test1/", null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tag_test1", "" + response);
                    }
                });
                Log.d("Tag——Coo_sendCookie", "" + client.getCookie(this));
                break;
            default: break;
        }
    }
}


