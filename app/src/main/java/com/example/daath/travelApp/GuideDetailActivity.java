package com.example.daath.travelApp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.CircleTransform;
import com.example.daath.travelApp.customClass.Comment;
import com.example.daath.travelApp.customClass.CommentAdapter;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;


public class GuideDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ScrollView scrollView;
    private TextView userName, userDescription, chatHim;
    private ImageView userAvatar;
    private LinearLayout commentFrame;
    private EditText commentInput;
    private ImageView commentDisplay, commentBack, commentSend;
    private User guide;
    private CommentListFragment commentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);
        guide = (User) getIntent().getSerializableExtra("user");

        initViewId();
        setViewEvent();

    }

    public ImageView getCommentDisplay() {
        return commentDisplay;
    }

    public void initViewId() {
        commentListFragment = (CommentListFragment) getFragmentManager().findFragmentById(R.id.guideDetail_commentList);
        scrollView = (ScrollView) findViewById(R.id.guideDetail_scrollView);
        userName = (TextView) findViewById(R.id.guideDetail_userName);
        userDescription = (TextView) findViewById(R.id.guideDetail_userDescription);
        chatHim = (TextView) findViewById(R.id.guideDetail_chatHim);
        userAvatar = (ImageView) findViewById(R.id.guideDetail_userImage);
        commentFrame = (LinearLayout) findViewById(R.id.guideDetail_commentFrame);
        commentInput = (EditText) findViewById(R.id.guideDetail_commentInput);
        commentDisplay = (ImageView) findViewById(R.id.guideDetail_commentDisplay);
        commentBack = (ImageView) findViewById(R.id.guideDetail_commentBack);
        commentSend = (ImageView) findViewById(R.id.guideDetail_commentSend);
    }

    public void setViewEvent() {
        scrollView.smoothScrollTo(0, 0);
        userName.setText(guide.getRealName());
        userDescription.setText(guide.getDescription());
        Glide.with(this)
                .load(guide.getAvatar())
                .placeholder(R.drawable.default_user_image_round)
                .centerCrop()
                .bitmapTransform(new CircleTransform(this))
                .into(userAvatar);
        commentDisplay.setVisibility(View.GONE);
        commentFrame.setVisibility(View.GONE);
        chatHim.setOnClickListener(this);
        commentDisplay.setOnClickListener(this);
        commentBack.setOnClickListener(this);
        commentSend.setOnClickListener(this);
    }

    public static void anotherActionStart(Context context, User user, String currentUserId) {
        Intent intent = new Intent(context, GuideDetailActivity.class);
        intent.putExtra("currentUserId", currentUserId);
        intent.putExtra("user", user);
        intent.putExtra("commentType", 1);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guideDetail_chatHim:
                String currentUserId = getIntent().getStringExtra("currentUserId");
                if (currentUserId.equals(guide.get_id())) {
                    Toast.makeText(this, "不能跟自己聊天", Toast.LENGTH_SHORT).show();
                } else {
                    RongIM.getInstance().startPrivateChat(this, guide.get_id(), null);
                }
                break;
            case R.id.guideDetail_commentDisplay:
                commentFrame.setVisibility(View.VISIBLE);
                commentDisplay.setVisibility(View.GONE);
                break;
            case R.id.guideDetail_commentBack:
                commentFrame.setVisibility(View.GONE);
                commentDisplay.setVisibility(View.VISIBLE);
                break;
            case R.id.guideDetail_commentSend:
                if (commentInput.length() == 0) {
                    Toast.makeText(this, "请输入评论", Toast.LENGTH_SHORT).show();
                }
                RequestParams params = new RequestParams();
                params.put("id", guide.get_id());
                params.put("content", commentInput.getText().toString());
                AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                client.post("comment/commentGuide/", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tag_commentScene", "" + response);
                        try {
                            JSONObject result = response.optJSONObject("result");
                            if (result != null) {
                                Comment comment = new Comment();
                                comment.set_id(result.getString("_id"));
                                comment.setGuideId(result.getString("guideId"));
                                comment.setUserId(result.getString("userId"));
                                comment.setContent(result.getString("content"));
                                FileOperation fileOperation = new FileOperation();
                                String Data = fileOperation.fileLoad(GuideDetailActivity.this);
                                JSONObject currentUser = new JSONObject(Data);
                                comment.setUserName(currentUser.getString("nickname"));
                                comment.setUserAvatar(currentUser.getString("avatar"));
                                List<Comment> commentList = commentListFragment.getCommentList();
                                commentList.add(comment);
                                commentListFragment.getCommentAdapter().setCommentList(commentList);
                                commentListFragment.getCommentAdapter().notifyDataSetChanged();
                                Toast.makeText(GuideDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                hideInput(GuideDetailActivity.this, commentInput);
                                commentFrame.setVisibility(View.GONE);
                            } else {
                                switch (response.getInt("msgCode")) {
                                    case 1000:
                                        // 一般不会出现此提示，默认会传id和content参数到后台
                                        break;
                                    case 1004:
                                        // TODO 可能会出现刚好登录用户过期的情况，就是重新登录，
                                        break;
                                    default: break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default: break;

        }
    }

    /**
     * 强制隐藏键盘
     * @param context
     * @param view
     */
    private void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
