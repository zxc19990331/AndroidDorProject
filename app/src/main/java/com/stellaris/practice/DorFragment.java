package com.stellaris.practice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import com.stellaris.adapter.BaseFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DorFragment extends Fragment {


    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;

    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    private BaseFragmentPagerAdapter mFragmentPagerAdapter;

    private List<Fragment> mFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dor, null);
        View rootView = inflater.inflate(R.layout.fragment_dor, container, false);
        ButterKnife.bind(this, rootView);
        initFragment();
        initTabAndPager();
        return rootView;
    }

    public void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(new DorBuildingFragment());
        mFragments.add(new DorMyDorFragment());
        mFragmentPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(),mFragments);
    }

    private void initTabAndPager() {

        mContentViewPager.setAdapter(mFragmentPagerAdapter);

        mContentViewPager.setCurrentItem(0,false);

        //tab样式
        mTabSegment.setHasIndicator(true);
        //宿舍楼
        mTabSegment.addTab(new QMUITabSegment.Tab(getString(R.string.dor_building)));
        //我的宿舍
        mTabSegment.addTab(new QMUITabSegment.Tab(getString(R.string.dor_my_dor)));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);

        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {

            //隐藏小红点
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });

    }






    /*以下是自动生成的代码*/
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DorFragment() {
        // Required empty public constructor
    }


    public static DorFragment newInstance(String param1, String param2) {
        DorFragment fragment = new DorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
