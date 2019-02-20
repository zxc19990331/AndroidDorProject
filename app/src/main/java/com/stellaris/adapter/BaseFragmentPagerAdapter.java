package com.stellaris.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> listfragment; //创建一个List<Fragment>
    //定义构造带两个参数
    public BaseFragmentPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.listfragment=list;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        return listfragment.get(index); //返回第几个fragment
    }

    @Override
    public int getCount() {

        return listfragment.size(); //总共有多少个fragment
    }

}
