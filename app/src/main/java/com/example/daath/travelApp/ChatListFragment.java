package com.example.daath.travelApp;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.daath.travelApp.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment implements View.OnClickListener{

    private View view;
    private ImageView chatBell;

    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO 聊天列表发起来聊天时候不会刷新你发的那个人的聊天，这里是有个BUG，这样会导致别人回复后，你看不到消息列表，智能重启app
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_chat_list, container, false);
            chatBell = (ImageView) view.findViewById(R.id.chat_bell);
            chatBell.setOnClickListener(this);
        }
        initChatList();
        return view;
    }


    public void initChatList() {
        ConversationListFragment fragment  = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_chatList);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();

        fragment.setUri(uri);
    }

    /**
     * 设置系统消息通知的点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_bell:
                SystemNotifyActivity.anotherActionStart(getActivity());
                break;
            default: break;
        }
    }
}
