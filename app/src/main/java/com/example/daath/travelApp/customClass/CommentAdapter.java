package com.example.daath.travelApp.customClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.daath.travelApp.R;
import com.example.daath.travelApp.customView.CircleImageView;

import java.util.List;

/**
 * Created by daath on 16-3-22.
 */
public class CommentAdapter extends ArrayAdapter<Comment>{

    private int resourceId;
    private Context context;
    private List<Comment> commentList;


    public CommentAdapter(Context context, int resourceId, List<Comment> object) {
        super(context, resourceId, object);
        this.context = context;
        this.resourceId = resourceId;
        this.commentList = object;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Comment getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        viewHolder.commentUserName = (TextView) view.findViewById(R.id.comment_userName);
        viewHolder.commentContent = (TextView) view.findViewById(R.id.comment_content);
        viewHolder.commentUserImage = (ImageView) view.findViewById(R.id.comment_userImage);
        viewHolder.commentUserName.setText(comment.getUserName());
        viewHolder.commentContent.setText(comment.getContent());
        Glide.with(context)
                .load(comment.getUserAvatar())
                .centerCrop()
                .transform(new CircleTransform(context))
                .into(viewHolder.commentUserImage);
        return view;
    }



    class ViewHolder {
        private ImageView commentUserImage;
        private TextView commentUserName;
        private TextView commentContent;
    }
}
