package com.example.daath.travelApp.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by daath on 16-3-23.
 */
public class ListViewForScrollView extends ListView {
//public class ListViewForScrollView extends PullToRefreshListView {


//    public ListViewForScrollView(Context context) {
//        super(context);
//    }
//
//    public ListViewForScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public ListViewForScrollView(Context context, Mode mode) {
//        super(context, mode);
//    }
//    public ListViewForScrollView(Context context, Mode mode, AnimationStyle style) {
//        super(context, mode, style);
//    }



    public ListViewForScrollView(Context context) {

        super(context);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);    }
}
