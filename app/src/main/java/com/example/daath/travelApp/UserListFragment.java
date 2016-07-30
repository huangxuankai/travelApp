package com.example.daath.travelApp;


import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.example.daath.travelApp.customClass.UserAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment
        implements OnItemClickListener, OnRefreshListener2{

    private View view;
    private PullToRefreshListView userListView;
    private List<User> userList = new ArrayList<User>();
    private UserAdapter userAdapter;

    private MainHomeActivity mainHomeActivity;

    private AppRestClient client = new AppRestClient();
    private int skip, limit;                //分页

    private double latitude;
    private double longitude;
    private String currentUserId;
    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Tag_UserListFragment", "onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_user_list, container, false);
            mainHomeActivity = (MainHomeActivity) getActivity();
            initPullToRefreshListView();
        }
        if (userList.isEmpty()) {
            getCurrentUserLocation();
            sendHttpRequest();
        }
        return view;
    }


    /**
     * 从文本load到当前登录用户的数据
     */
    public void getCurrentUserLocation() {
        FileOperation fileOperation = new FileOperation();
        String Data = fileOperation.fileLoad(getActivity());
        Log.d("Tag_Data", "" + Data);
        try {
            JSONObject result = new JSONObject(Data);
            JSONArray location = result.getJSONArray("location");
            currentUserId = result.getString("_id");
            Log.d("currentUserId", currentUserId);
            longitude = location.getDouble(0);
            latitude = location.getDouble(1);
            Log.d("Tag_lllll", longitude + "," + latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initPullToRefreshListView() {
        userListView = (PullToRefreshListView) view.findViewById(R.id.fragment_userListView);
        userListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新");
        userListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        userListView.getLoadingLayoutProxy(true, true).setRefreshingLabel("加载中");
        userListView.getLoadingLayoutProxy(true, true).setReleaseLabel("释放更新");
        userListView.setOnRefreshListener(this);
        userAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_item, userList);
        userListView.setAdapter(userAdapter);
        userListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Tag_user", "" + position);
        User user = userList.get(position - 1);
        GuideDetailActivity.anotherActionStart(getActivity(), user, currentUserId);
    }

    public void sendHttpRequest() {

        RequestParams params = new RequestParams();
        skip = 0;
        limit = 10;
        params.put("skip", skip);
        params.put("limit", limit);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        client.saveCookie(getActivity());
        client.post("user/getGuideLists/", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Tag_guidelist", "" + response);
                    JSONArray result = response.getJSONArray("result");
                    for (int i=0;i<result.length();i++) {
                        JSONObject guideJson = result.getJSONObject(i);
                        User user = new User();
                        user.set_id(guideJson.getString("_id"));
                        user.setRealName(guideJson.getString("realName"));
                        user.setAvatar(guideJson.getString("avatar"));
                        user.setDescription(guideJson.getString("description"));
                        userList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userAdapter.setUser(userList);
                userAdapter.notifyDataSetChanged();
                mainHomeActivity.setGuides(userList);
            }
        });
    }


    /**
     * PullToRefreshListView 的下拉刷新上拉加载的监听
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Log.d("Tag_refresh", "onPullDownToRefresh");
        RequestParams params = new RequestParams();
        params.put("skip", 0);
        params.put("limit", limit);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        userList = new ArrayList<User>();
        client.saveCookie(getActivity());
        client.post("user/getGuideLists/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Tag_guidelist", "" + response);
                    JSONArray result = response.getJSONArray("result");
                    for (int i=0;i<result.length();i++) {
                        JSONObject guideJson = result.getJSONObject(i);
                        User user = new User();
                        user.set_id(guideJson.getString("_id"));
                        user.setRealName(guideJson.getString("realName"));
                        user.setAvatar(guideJson.getString("avatar"));
                        user.setDescription(guideJson.getString("description"));
                        userList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userAdapter.setUser(userList);
                userAdapter.notifyDataSetChanged();
                userListView.onRefreshComplete();
                mainHomeActivity.setGuides(userList);
            }
        });

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        Log.d("Tag_refresh", "onPullUpToRefresh");
        skip += 10;
        limit += 10;
        RequestParams params = new RequestParams();
        params.put("skip", skip);
        params.put("limit", limit);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        client.saveCookie(getActivity());
        client.post("user/getGuideLists/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Tag_guidelist", "" + response);
                    JSONArray result = response.getJSONArray("result");
                    if (result.length() == 0) {
                        Toast.makeText(getActivity(), "已经是最后了", Toast.LENGTH_SHORT).show();
                        userListView.onRefreshComplete();
                        skip -= 10;
                        limit -= 10;
                        return;
                    }
                    int i;
                    for (i=0;i<result.length();i++) {
                        JSONObject guideJson = result.getJSONObject(i);
                        User user = new User();
                        user.set_id(guideJson.getString("_id"));
                        user.setRealName(guideJson.getString("realName"));
                        user.setAvatar(guideJson.getString("avatar"));
                        user.setDescription(guideJson.getString("description"));
                        userList.add(user);
                    }
                    userAdapter.setUser(userList);
                    userAdapter.notifyDataSetChanged();
                    userListView.onRefreshComplete();
                    limit = skip + i;
                    mainHomeActivity.setGuides(userList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
