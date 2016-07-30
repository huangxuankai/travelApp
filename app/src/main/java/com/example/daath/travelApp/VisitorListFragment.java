package com.example.daath.travelApp;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.example.daath.travelApp.customClass.UserAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisitorListFragment extends Fragment
        implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    private View view;
    private PullToRefreshListView visitorListView;
    private List<User> visitorList = new ArrayList<User>();
    private UserAdapter visitorAdapter;

    private AppRestClient client = new AppRestClient();
    private int skip, limit;                //分页

    private double latitude;
    private double longitude;

    private MainHomeActivity mainHomeActivity;

    public VisitorListFragment() {
        // Required empty public constructor
    }

//    public static List<User> getVisitorList() {
//        return visitorList;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Tag_UserListFragment", "onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_visitor_list, container, false);
            initPullToRefreshListView();
            mainHomeActivity = (MainHomeActivity) getActivity();
        }
        if (visitorList.isEmpty()) {
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
            longitude = location.getDouble(0);
            latitude = location.getDouble(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initPullToRefreshListView() {
        visitorListView = (PullToRefreshListView) view.findViewById(R.id.fragment_visitorListView);
        visitorListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新");
        visitorListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        visitorListView.getLoadingLayoutProxy(true, true).setRefreshingLabel("加载中");
        visitorListView.getLoadingLayoutProxy(true, true).setReleaseLabel("释放更新");
        visitorListView.setOnRefreshListener(this);
        visitorAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_item, visitorList);
        visitorListView.setAdapter(visitorAdapter);
        visitorListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = visitorList.get(position - 1);
        RongIM.getInstance().startPrivateChat(getActivity(), user.get_id(), null);
//        GuideDetailActivity.anotherActionStart(getActivity(), user);
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
        client.post("user/getVisitorLists/", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("result");
                    for (int i=0;i<result.length();i++) {
                        JSONObject guideJson = result.getJSONObject(i);
                        User user = new User();
                        user.set_id(guideJson.getString("_id"));
                        user.setRealName(guideJson.getString("realName"));
                        user.setAvatar(guideJson.getString("avatar"));
                        user.setDescription(guideJson.getString("description"));
                        visitorList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                visitorAdapter.setUser(visitorList);
                visitorAdapter.notifyDataSetChanged();
                mainHomeActivity.setFriends(visitorList);
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
        visitorList = new ArrayList<User>();
        client.saveCookie(getActivity());
        client.post("user/getVisitorLists/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("result");
                    for (int i=0;i<result.length();i++) {
                        JSONObject guideJson = result.getJSONObject(i);
                        User user = new User();
                        user.set_id(guideJson.getString("_id"));
                        user.setRealName(guideJson.getString("realName"));
                        user.setAvatar(guideJson.getString("avatar"));
                        user.setDescription(guideJson.getString("description"));
                        visitorList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                visitorAdapter.setUser(visitorList);
                visitorAdapter.notifyDataSetChanged();
                visitorListView.onRefreshComplete();
                mainHomeActivity.setFriends(visitorList);
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
        client.post("user/getVisitorLists/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("result");
                    if (result.length() == 0) {
                        Toast.makeText(getActivity(), "已经是最后了", Toast.LENGTH_SHORT).show();
                        visitorListView.onRefreshComplete();
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
                        visitorList.add(user);
                    }
                    visitorAdapter.setUser(visitorList);
                    visitorAdapter.notifyDataSetChanged();
                    visitorListView.onRefreshComplete();
                    limit = skip + i;
                    mainHomeActivity.setFriends(visitorList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
