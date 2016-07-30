package com.example.daath.travelApp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.CircleTransform;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonDataFragment extends Fragment
        implements View.OnClickListener, PullToRefreshBase.OnRefreshListener{

    private View view;
    private PullToRefreshScrollView scrollView;
    private LinearLayout Data, modifyPassword, authorityApply;
    private ImageView userAvatar, signOut;
    private TextView userName, userDescription;
    private User currentUser;

    public PersonDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Tag_personData", "onCreateView");
        getCurrentUserData();
        view = inflater.inflate(R.layout.fragment_person_data, container, false);
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.person_scrollView);
        userAvatar = (ImageView) view.findViewById(R.id.person_userAvatar);
        signOut = (ImageView) view.findViewById(R.id.person_signOut);
        userName = (TextView) view.findViewById(R.id.person_userName);
        userDescription = (TextView) view.findViewById(R.id.person_userDescription);
        Data = (LinearLayout) view.findViewById(R.id.person_Data);
        modifyPassword = (LinearLayout) view.findViewById(R.id.person_modifyPassword);
        authorityApply = (LinearLayout) view.findViewById(R.id.person_authorityApply);
        if (currentUser.getNickname().isEmpty()){
            userName.setText(currentUser.getAccount());
        } else {
            userName.setText(currentUser.getNickname());
        }
        userDescription.setText(currentUser.getDescription());
        Glide.with(getActivity())
                .load(currentUser.getAvatar())
                .placeholder(R.drawable.default_user_image_round)
                .centerCrop()
                .bitmapTransform(new CircleTransform(getActivity()))
                .into(userAvatar);
        userAvatar.setOnClickListener(this);
        signOut.setOnClickListener(this);
        Data.setOnClickListener(this);
        modifyPassword.setOnClickListener(this);
        authorityApply.setOnClickListener(this);
        scrollView.setOnRefreshListener(this);
        return view;
    }

    /**
     * 从文本load到当前登录用户的数据
     */
    public void getCurrentUserData() {
        FileOperation fileOperation = new FileOperation();
        String Data = fileOperation.fileLoad(getActivity());
        Log.d("Tag_Data", "" + Data);
        try {
            JSONObject result = new JSONObject(Data);
            currentUser = new User();
            currentUser.set_id(result.getString("_id"));
            currentUser.setAccount(result.getString("account"));
            currentUser.setNickname(result.getString("nickname"));
            currentUser.setRealName(result.getString("realName"));
            currentUser.setAvatar(result.getString("avatar"));
            currentUser.setDescription(result.getString("description"));
            currentUser.setStatus(result.getString("status"));
            currentUser.setPhone(result.getString("phone"));
            currentUser.setToken(result.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_Data:
                EditPersonDataActivity.anotherActionStart(getActivity(), currentUser);
                break;
            case R.id.person_modifyPassword:
                ModifyPersonPasswordActivity.anotherActionStart(getActivity());
                break;
            case R.id.person_authorityApply:
                AuthorityApplyActivity.anotherActionStart(getActivity(), currentUser);
                break;
            case R.id.person_userAvatar:
                SelectPictureActivity.anotherActionStart(getActivity());
                break;
            case R.id.person_signOut:
                AppRestClient client = new AppRestClient();
                client.saveCookie(getActivity());
                client.post("user/signOut/", null, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(getActivity(), "用户已经退出", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        SignInActivity.anotherActionStart(getActivity());
                    }
                });
                break;
            default: break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        Log.d("Tag_f", "dsadasad");
        getCurrentUserData();
        if (currentUser.getNickname().isEmpty()){
            userName.setText(currentUser.getAccount());
        } else {
            userName.setText(currentUser.getNickname());
        }
        userDescription.setText(currentUser.getDescription());
        Glide.with(getActivity())
                .load(currentUser.getAvatar())
                .placeholder(R.drawable.default_user_image_round)
                .centerCrop()
                .bitmapTransform(new CircleTransform(getActivity()))
                .into(userAvatar);
        Toast.makeText(getActivity(), "更新完成", Toast.LENGTH_SHORT).show();
        scrollView.onRefreshComplete();
    }
}
