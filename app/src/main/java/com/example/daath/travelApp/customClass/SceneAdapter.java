package com.example.daath.travelApp.customClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daath.travelApp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daath on 16-3-19.
 */
public class SceneAdapter extends ArrayAdapter<Scene>{

    private int resourceId;
    private Context context;
    private List<Scene> sceneList;

    public SceneAdapter(Context context, int resourceId, List<Scene> object) {
        super(context, resourceId, object);
        this.context = context;
        this.resourceId = resourceId;
        sceneList = object;
    }

    public void setSceneList(List<Scene> sceneList) {
        this.sceneList = sceneList;
    }

    @Override
    public int getCount() {
        return sceneList.size();
    }


    @Override
    public Scene getItem(int position) {
        return sceneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public  void changeData(List<Scene> list) {
//       sceneList = list;
//       this.notifyDataSetChanged();
//   }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        Scene scene= (Scene) getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        viewHolder.sceneImage = (ImageView) view.findViewById(R.id.fragment_sceneImage);
        viewHolder.sceneName = (TextView) view.findViewById(R.id.fragment_sceneName);
        viewHolder.sceneName.setText(scene.getName());
        Glide.with(context)
                .load(scene.getImage())
                .placeholder(R.drawable.default_scene_image)
                .centerCrop()
                .into(viewHolder.sceneImage);

        return view;
    }

    class ViewHolder {
        private ImageView sceneImage;
        private TextView sceneName;
    }
}
