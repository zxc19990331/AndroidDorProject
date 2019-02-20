package com.stellaris.practice;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.stellaris.functions.BottomNavigationViewHelper;
public class MainFrameActivity extends AppCompatActivity
        implements DorFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,MyInfoFragment.OnFragmentInteractionListener{

    //private Toolbar mToolbar;
    private TextView mTextMessage;
    private int lastIndex;
    List<Fragment> mFragments;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragmentPosition(0);
                    break;
                case R.id.navigation_dashboard:
                    setFragmentPosition(1);
                    break;
                case R.id.navigation_person:
                    setFragmentPosition(2);
                    break;
            }
            return true;
        }
    };

    public void initData() {
        //setSupportActionBar(mToolbar);
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new DorFragment());
        mFragments.add(new MyInfoFragment());
        // 初始化展示MessageFragment
        setFragmentPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        initData();
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.main_framlayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
