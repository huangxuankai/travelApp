package com.example.daath.travelApp.myTestLayout;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;

import com.example.daath.travelApp.R;
import com.example.daath.travelApp.SceneListFragment;
import com.example.daath.travelApp.UserListFragment;

public class TestFragmentActivity extends AppCompatActivity {

    private FragmentTabHost fragmentTabHost;
    private LayoutInflater layoutInflater;

    private Class mFragmentArray[] = { SceneListFragment.class, UserListFragment.class,
            Test3Fragment.class };

    private String mTextArray[] = { "首页", "消息", "好友", "搜索", "更多" };

    private int mImageArray[] = { R.drawable.tab_home_default, R.drawable.tab_people_default,
            R.drawable.tab_bell_default, R.drawable.tab_person_default};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        layoutInflater = LayoutInflater.from(this);

        // 找到TabHost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 得到fragment的个数
        fragmentTabHost.getTabWidget().setShowDividers(0);
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, mFragmentArray[i], null);

        }
        TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextArray[3]).setIndicator(getTabItemView(3));
        // 将Tab按钮添加进Tab选项卡中
        fragmentTabHost.addTab(tabSpec, mFragmentArray[1], null);
    }

    /**
     *
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.fragment_test_tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_tabImage);
        imageView.setImageResource(mImageArray[index]);
        return view;
    }
}
