package com.example.daath.travelApp.customClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.R;
import com.my.greenDao.bean.Notify;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by daath on 16-4-25.
 */
public class NotifyAdapter extends ArrayAdapter<Notify> {

    private Context context;
    private int resourceId;
    private List<Notify> notifyList;

    public NotifyAdapter(Context context, int resourceId, List<Notify> object) {
        super(context, resourceId, object);
        this.context = context;
        this.resourceId = resourceId;
        notifyList = object;
    }

    public void setNotifyList(List<Notify> notifyList) {
        this.notifyList = notifyList;
    }

    @Override
    public int getCount() {
        return notifyList.size();
    }

    @Override
    public Notify getItem(int position) {
        return notifyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        Notify notify = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        viewHolder.notifyTime = (TextView) view.findViewById(R.id.notify_date);
        viewHolder.notifyTitle = (TextView) view.findViewById(R.id.notify_title);
        viewHolder.notifyDescription = (TextView) view.findViewById(R.id.notify_description);
        viewHolder.notifyTime.setText(DateFormat.getInstance().format(notify.getCreateDate()));
        viewHolder.notifyTitle.setText(notify.getTitle());
        viewHolder.notifyDescription.setText(notify.getDescription());
        return view;

    }


    class ViewHolder {
        private TextView notifyTime;
        private TextView notifyTitle;
        private TextView notifyDescription;
    }
}
