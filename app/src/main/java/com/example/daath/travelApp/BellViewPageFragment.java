package com.example.daath.travelApp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daath.travelApp.customClass.FragmentAdapter;
import com.example.daath.travelApp.myTestLayout.Test2Fragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * 这个布局没用了
 */
public class BellViewPageFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();


    public BellViewPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_bell_view_page, container, false);
            initViewPage();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    private void initViewPage() {
        tabLayout = (TabLayout) view.findViewById(R.id.viewPage_bellTabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPage_bell);

        fragments.add(new ChatListFragment());
//        fragments.add(new Test1Fragment());
        fragments.add(new Test2Fragment());

        titles.add("聊天");
        titles.add("通知");

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager());
        fragmentAdapter.setFragments(fragments);
        fragmentAdapter.setTitles(titles);
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);

    }
}
