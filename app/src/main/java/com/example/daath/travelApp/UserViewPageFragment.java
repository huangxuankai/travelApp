package com.example.daath.travelApp;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.daath.travelApp.customClass.FragmentAdapter;

import com.example.daath.travelApp.customClass.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserViewPageFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles = new ArrayList<String >();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    FragmentAdapter fragmentAdapter;

    public UserViewPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_user_list_view_page, container, false);
            initViewPage();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }
        return view;
    }


    private void initViewPage(){
        viewPager = (ViewPager) view.findViewById(R.id.viewPage_user);
        tabLayout = (TabLayout) view.findViewById(R.id.viewPage_userTabs);
        titles.add("附近的导游");
        titles.add("附近的游客");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));//添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));


        fragments.add(new UserListFragment());
        fragments.add(new VisitorListFragment());
//        fragments.add(visitorListFragment);
//        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager());
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());

        fragmentAdapter.setFragments(fragments);
        fragmentAdapter.setTitles(titles);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }

}
