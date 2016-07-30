package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.jauker.widget.BadgeView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.LoadingDialogFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


public class MainHomeActivity extends AppCompatActivity
        implements RongIM.UserInfoProvider, RongIMClient.OnReceiveMessageListener,
        RongIM.OnReceiveUnreadCountChangedListener{

    private String baiDuPush_apiKey = "ljYNLqCZdMzY6DsyCyzXEgdss8YmtOFL";       //travelApp百度推送正式环境appKey


    private FragmentTabHost fragmentTabHost;
    private LayoutInflater layoutInflater;
    private BadgeView badgeView;

    private Class fragmentArray[] = {SceneListFragment.class, UserViewPageFragment.class,
            ChatListFragment.class, PersonDataFragment.class};
    private String textArray[] = {"景区", "朋友", "消息", "我的"};
    private int imageArray[] = {R.drawable.tab_home, R.drawable.tab_people,
            R.drawable.tab_chat, R.drawable.tab_person};


    private User currentUser;
    private List<User> friends = new ArrayList<User>();
    private List<User> guides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        initView();
        initIM();
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, baiDuPush_apiKey);
        /*  融云用户信息提供者 */
        RongIM.setOnReceiveMessageListener(this);
        RongIM.setUserInfoProvider(this, true);
//        RongIM.getInstance().setMessageAttachedUserInfo(true);


    }

    /**
     * 在主activity中获取到附近游客的信息，以便做用户信息提供者
     * @param friends
     */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setGuides(List<User> guides) {
        this.guides = guides;
    }

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, MainHomeActivity.class);
        context.startActivity(intent);
    }


    /**
     * 初始化组件
     */
    public void initView() {
        layoutInflater = LayoutInflater.from(this);

        // 找到TabHost
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.mainHome_fragmentTabHost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.mainHome_tabContent);
        // 得到fragment的个数
        fragmentTabHost.getTabWidget().setShowDividers(0);
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = fragmentTabHost.newTabSpec(textArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
        }

    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    public View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.layout_main_home_tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.mainHome_tabImage);
        imageView.setImageResource(imageArray[index]);
        if (index == 2) {
            badgeView = new BadgeView(this);
//            badgeView.setBadgeCount(badgeCount);
            badgeView.setText(" ...");
            badgeView.setTextSize(10);
            badgeView.setTargetView(imageView);
            badgeView.setBadgeGravity(Gravity.RIGHT);
            badgeView.setBadgeMargin(0, 0, 30, 0);
            badgeView.setVisibility(View.GONE);
        }
        return view;
    }


    /**
     * 从文本load到当前登录用户的数据
     */
    public void getCurrentUserLocation() {
        FileOperation fileOperation = new FileOperation();
        String Data = fileOperation.fileLoad(this);
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

    /**
     * 设置融云聊天
     */
    public void initIM() {
        getCurrentUserLocation();
        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        RongIM.connect(currentUser.getToken(), new RongIMClient.ConnectCallback() {
            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
                Log.d("Tag_Token", "-----token 过期");
                AppRestClient client = new AppRestClient();
                client.saveCookie(MainHomeActivity.this);
                client.post("user/tokenReload/", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                        JSONObject result = responseBody.optJSONObject("result");
                        FileOperation fileOperation = new FileOperation();
                        fileOperation.fileSave(MainHomeActivity.this, result.toString());
                        Log.e("Tag_token", "" + responseBody);
                        initIM();
                    }
                });
            }

            @Override
            public void onSuccess(String userId) {
                Log.d("Tag_Token", "-----token 连接成功" + userId);
                Toast.makeText(MainHomeActivity.this, "连接融云成功", Toast.LENGTH_SHORT).show();
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(currentUser.get_id(), currentUser.getNickname(), Uri.parse(currentUser.getAvatar())));
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(MainHomeActivity.this, Conversation.ConversationType.PRIVATE);
                // RongIM.getInstance().setMessageAttachedUserInfo(true);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("Tag_Token", "-----token 连接失败" + errorCode);
            }
        });
    }

    /**
     * 用户信息提供者
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        Log.e("Tag_userFFFFFFF", userId);
        for (User u: friends) {
            Log.e("Tag_userFFFFFFF", u.get_id() + "|||" + u.getRealName());
            if (u.get_id().equals(userId)) {
                return new UserInfo(u.get_id(), u.getRealName(), null);
            }
        }
        for (User g: guides) {
            if (g.get_id().equals(userId)) {
                return new UserInfo(g.get_id(), g.getRealName(), null);
            }
        }
        return new UserInfo(userId, "陌生人", null);
    }

    /**
     * 融云接受消息
     * @param message
     * @param i
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {

        return false;
    }

    @Override
    public void onMessageIncreased(int i) {
        // 设置未读消息的badgeView
        if (i == 0) {
            badgeView.setVisibility(View.GONE);
        } else {
            badgeView.setVisibility(View.VISIBLE);
        }

        Log.d("Tag_count", "" + i);
    }
}
