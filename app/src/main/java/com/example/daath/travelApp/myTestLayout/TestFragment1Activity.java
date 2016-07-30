package com.example.daath.travelApp.myTestLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.daath.travelApp.BellViewPageFragment;
import com.example.daath.travelApp.PersonDataFragment;
import com.example.daath.travelApp.R;
import com.example.daath.travelApp.SceneListFragment;
import com.example.daath.travelApp.UserViewPageFragment;

public class TestFragment1Activity extends AppCompatActivity implements View.OnClickListener{

    private ImageView tabHome, tabPeople, tabBell, tabPerson;
    private FrameLayout f1;
    private Fragment currentFragment = new Fragment();
    private SceneListFragment sceneListFragment = new SceneListFragment();
    private UserViewPageFragment userViewPageFragment = new UserViewPageFragment();
    private BellViewPageFragment bellViewPageFragment = new BellViewPageFragment();
    private PersonDataFragment personDataFragment = new PersonDataFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment1);
        initView();
    }

    private void initView() {
        f1 = (FrameLayout) findViewById(R.id.bottomTab_f1);
        tabHome = (ImageView) findViewById(R.id.bottomTab_home);
        tabPeople = (ImageView) findViewById(R.id.bottomTab_people);
        tabBell = (ImageView) findViewById(R.id.bottomTab_bell);
        tabPerson = (ImageView) findViewById(R.id.bottomTab_person);
        tabHome.setOnClickListener(this);
        tabPeople.setOnClickListener(this);
        tabBell.setOnClickListener(this);
        tabPerson.setOnClickListener(this);
        setCurrentFragment(sceneListFragment);
        setBtnStatus(tabPerson);
    }
    private void setCurrentFragment(Fragment fragment) {
        if (currentFragment == fragment) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.hide(currentFragment).add(R.id.bottomTab_f1,fragment).commit();
        }   else    {
            fragmentTransaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    private void setBtnStatus(ImageView imageView) {
        tabHome.setSelected(false);
        tabPeople.setSelected(false);
        tabBell.setSelected(false);
        tabPerson.setSelected(false);
        imageView.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomTab_home:
                setCurrentFragment(new SceneListFragment());
                setBtnStatus(tabHome);
                break;
            case R.id.bottomTab_people:
                setCurrentFragment(userViewPageFragment);
                setBtnStatus(tabPeople);
                break;
            case R.id.bottomTab_bell:
                setCurrentFragment(bellViewPageFragment);
                setBtnStatus(tabBell);
                break;
            case R.id.bottomTab_person:
                setCurrentFragment(personDataFragment);
                setBtnStatus(tabPerson);
                break;
            default: break;
        }
    }
}
