package com.example.daath.travelApp.myTestLayout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daath.travelApp.R;
import com.example.daath.travelApp.customClass.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Test3Fragment extends Fragment {

    private View view;

    public Test3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test3, container, false);
//        if (view == null)
//        {
//            view = inflater.inflate(R.layout.activity_my_test, container, false);
//            List<Fragment> fragments = new ArrayList<Fragment>();
//            fragments.add(new SceneListFragment());
//            fragments.add(new UserListFragment());
//            FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
//            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPage_home);
//            viewPager.setAdapter(fragmentAdapter);
//
//        }
//        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
//        ViewGroup parent = (ViewGroup) view.getParent();
//        if (parent != null)
//        {
//            parent.removeView(view);
//        }
//        return view;
    }

}
