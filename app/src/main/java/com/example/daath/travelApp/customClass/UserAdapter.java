package com.example.daath.travelApp.customClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.R;

import java.util.List;

/**
 * Created by daath on 16-3-21.
 */
public class UserAdapter extends ArrayAdapter<User>{

    private int resourceId;
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, int resourceId, List<User> object) {
        super(context, resourceId, object);
        this.context = context;
        this.resourceId = resourceId;
        this.userList = object;
    }

    public void setUser(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        View view;

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        viewHolder.userName = (TextView) view.findViewById(R.id.fragment_userName);
        viewHolder.userAvatar = (ImageView) view.findViewById(R.id.fragment_userImage);
        viewHolder.userName.setText(user.getRealName());
        Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.default_user_image_round)
                .centerCrop()
                .bitmapTransform(new CircleTransform(context))
                .into(viewHolder.userAvatar);
        return view;

    }

    class ViewHolder {
        private ImageView userAvatar;
        private TextView userName;
    }
}
