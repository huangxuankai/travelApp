package com.example.daath.travelApp;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.Comment;
import com.example.daath.travelApp.customClass.CommentAdapter;
import com.example.daath.travelApp.customClass.FileOperation;
import com.example.daath.travelApp.customClass.Scene;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SceneDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ScrollView scrollView;
    private ImageView sceneImage;
    private TextView sceneName, sceneDescription;
    private ImageView commentDisplay;
    private ImageView commentBack, commentSend;
    private LinearLayout commentFrame;
    private EditText commentInput;
    private Scene scene;
    private CommentListFragment commentListFragment;

    public ImageView getCommentDisplay() {
        return this.commentDisplay;
    }
    /**
     * 设置一个EditView.setError()的警告图标
     * @return
     */
    private Drawable getWarningIcon() {
        Drawable icon = getResources().getDrawable(R.drawable.warning_display);
        icon.setBounds(0, 0, 100, 55);
        return  icon;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        scene = (Scene) getIntent().getSerializableExtra("scene");
        initViewId();
        setViewEvents();
    }

    public void initViewId() {
        commentListFragment = (CommentListFragment) getFragmentManager().findFragmentById(R.id.sceneDetail_commentList);
        scrollView = (ScrollView) findViewById(R.id.sceneDetail_scrollView);
        sceneImage = (ImageView) findViewById(R.id.sceneDetail_sceneImage);
        sceneName = (TextView) findViewById(R.id.sceneDetail_sceneName);
        sceneDescription = (TextView) findViewById(R.id.sceneDetail_sceneDescription);
        commentDisplay = (ImageView) findViewById(R.id.sceneDetail_commentDisplay);
        commentBack = (ImageView) findViewById(R.id.sceneDetail_commentBack);
        commentSend = (ImageView) findViewById(R.id.sceneDetail_commentSend);
        commentFrame = (LinearLayout) findViewById(R.id.sceneDetail_commentFrame);
        commentInput = (EditText) findViewById(R.id.sceneDetail_commentInput);
    }


    public void setViewEvents() {
        scrollView.smoothScrollTo(0, 0);
        sceneName.setText(scene.getName());
        sceneDescription.setText(scene.getDescription());
        Glide.with(this)
                .load(scene.getImage())
                .placeholder(R.drawable.default_scene_image)
                .centerCrop()
                .into(sceneImage);
        commentDisplay.setOnClickListener(this);
        commentBack.setOnClickListener(this);
        commentSend.setOnClickListener(this);
        commentDisplay.setVisibility(View.GONE);
        commentFrame.setVisibility(View.GONE);
    }

    public static void anotherActionStart(Context context, Scene scene) {
        Intent intent = new Intent(context, SceneDetailActivity.class);
        Log.d("Tag___se", scene.getName());
        intent.putExtra("scene", scene);
        intent.putExtra("commentType", 0);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sceneDetail_commentDisplay:
                commentFrame.setVisibility(View.VISIBLE);
                commentDisplay.setVisibility(View.GONE);
                break;
            case R.id.sceneDetail_commentBack:
                commentFrame.setVisibility(View.GONE);
                commentDisplay.setVisibility(View.VISIBLE);
                break;
            case R.id.sceneDetail_commentSend:
                if (commentInput.length() == 0) {
                    Toast.makeText(this, "请输入评论", Toast.LENGTH_SHORT).show();
                }
                RequestParams params = new RequestParams();
                params.put("id", scene.get_id());
                params.put("content", commentInput.getText().toString());
                AppRestClient client = new AppRestClient();
                client.saveCookie(this);
                client.post("comment/commentScene/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tag_commentScene", "" + response);
                        try {
                            JSONObject result = response.optJSONObject("result");
                            if (result != null) {
                                Comment comment = new Comment();
                                comment.set_id(result.getString("_id"));
                                comment.setSceneId(result.getString("sceneId"));
                                comment.setUserId(result.getString("userId"));
                                comment.setContent(result.getString("content"));
                                FileOperation fileOperation = new FileOperation();
                                String Data = fileOperation.fileLoad(SceneDetailActivity.this);
                                JSONObject currentUser = new JSONObject(Data);
                                comment.setUserName(currentUser.getString("nickname"));
                                comment.setUserAvatar(currentUser.getString("avatar"));
                                List<Comment> commentList = commentListFragment.getCommentList();
                                commentList.add(comment);
                                commentListFragment.getCommentAdapter().setCommentList(commentList);
                                commentListFragment.getCommentAdapter().notifyDataSetChanged();
                                Toast.makeText(SceneDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                commentFrame.setVisibility(View.GONE);
                                hideInput(SceneDetailActivity.this, commentInput);

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

    private void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
