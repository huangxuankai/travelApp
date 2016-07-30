package com.example.daath.travelApp;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.daath.travelApp.customClass.AppRestClient;
import com.example.daath.travelApp.customClass.Comment;
import com.example.daath.travelApp.customClass.CommentAdapter;
import com.example.daath.travelApp.customClass.Scene;
import com.example.daath.travelApp.customClass.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.*;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
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
public class CommentListFragment extends Fragment implements OnRefreshListener2{

    private View view;
    private ListView commentListView;
//    private PullToRefreshListView commentListView;
    private List<Comment> commentList = new ArrayList<Comment>();
    private CommentAdapter commentAdapter;
    private String id;
    private int commentType;

    public CommentListFragment() {
        // Required empty public constructor
    }

    public List<Comment> getCommentList() {
        return this.commentList;
    }

//    public PullToRefreshListView getCommentListView() {
//        return this.commentListView;
//    }

    public CommentAdapter getCommentAdapter() {
        return this.commentAdapter;
    }

    public ListView getCommentListView() {
        return this.commentListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        commentListView = (ListView) view.findViewById(R.id.fragment_commentListView);

        commentAdapter = new CommentAdapter(getActivity(), R.layout.fragment_comment_item, commentList);
        commentListView.setAdapter(commentAdapter);

//        initPullToRefreshListView();
        sendHttpRequest();
        return view;
    }


    // TODO 暂时用不了套不进ScrollView,明明取到数据，但是填充不了
//    public void initPullToRefreshListView() {
//        commentListView = (PullToRefreshListView) view.findViewById(R.id.fragment_commentListView);
//        commentListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新");
//        commentListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
//        commentListView.getLoadingLayoutProxy(true, true).setRefreshingLabel("加载中");
//        commentListView.getLoadingLayoutProxy(true, true).setReleaseLabel("释放更新");
//        commentListView.setOnRefreshListener(this);
//        commentAdapter = new CommentAdapter(getActivity(), R.layout.fragment_comment_item, commentList);
//        commentListView.setAdapter(commentAdapter);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        commentType = activity.getIntent().getIntExtra("commentType", 0);
        switch (commentType) {
            case 0:
                Scene scene = (Scene) activity.getIntent().getSerializableExtra("scene");
                id = scene.get_id();
                break;
            case 1:
                User user = (User) activity.getIntent().getSerializableExtra("user");
                id = user.get_id();
                break;
            default: break;
        }
    }

    private void sendHttpRequest() {
        /**
         * 根据类型，发送请求获取是景区评论或者导游评论
         */
        AppRestClient client = new AppRestClient();
        client.saveCookie(getActivity());
        RequestParams params = new RequestParams();
        params.put("id", id);
        switch (commentType) {
            case 0:
                client.post("comment/getSceneComment/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Tag_comment", "" + response);
                        try {
                            JSONArray result = response.getJSONArray("result");
                            for (int i=0; i<result.length();i++) {
                                JSONObject commentJson = result.getJSONObject(i);
                                JSONObject userInfo = commentJson.getJSONObject("userInfo");
                                Comment comment = new Comment();
                                comment.set_id(commentJson.getString("_id"));
                                comment.setUserId(commentJson.getString("userId"));
                                comment.setSceneId(commentJson.getString("sceneId"));
                                comment.setContent(commentJson.getString("content"));
                                comment.setUserAvatar(userInfo.getString("avatar"));
                                comment.setUserName(userInfo.getString("nickname"));
                                commentList.add(comment);
                            }
                            if (!response.getBoolean("haveComment")){
                                SceneDetailActivity parentActivity = (SceneDetailActivity) getActivity();
                                parentActivity.getCommentDisplay().setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        commentAdapter.setCommentList(commentList);
                        commentAdapter.notifyDataSetChanged();
//                        commentListView.setAdapter(commentAdapter);
                    }
                });
                break;
            case 1:
                client.post("comment/getGuideComment/", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("result");
                            for (int i=0;i<result.length();i++) {
                                JSONObject commentJson = result.getJSONObject(i);
                                JSONObject userInfo = commentJson.getJSONObject("userInfo");
                                Comment comment = new Comment();
                                comment.set_id(commentJson.getString("_id"));
                                comment.setUserId(commentJson.getString("userId"));
                                comment.setGuideId(commentJson.getString("guideId"));
                                comment.setContent(commentJson.getString("content"));
                                comment.setUserAvatar(userInfo.getString("avatar"));
                                comment.setUserName(userInfo.getString("nickname"));
                                commentList.add(comment);
                            }
                            if (!response.getBoolean("haveComment")){
                                GuideDetailActivity parentActivity = (GuideDetailActivity) getActivity();
                                parentActivity.getCommentDisplay().setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        commentAdapter.setCommentList(commentList);
                        commentAdapter.notifyDataSetChanged();
                    }
                });
                break;
            default: break;
        }

    }

    /**
     * 监听
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
