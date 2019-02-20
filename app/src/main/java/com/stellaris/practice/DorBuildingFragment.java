package com.stellaris.practice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DorBuildingFragment extends Fragment {


    @BindView(R.id.dor_bui_image_add)
    ImageView ImageAdd;

    @BindView(R.id.dor_bui_layout_allposts)
    LinearLayout LayoutAllposts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dor_building, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick(R.id.dor_bui_image_add)
    public void onViewClicked() {
        View itemPostView = View.inflate(getContext(),R.layout.item_posting,null);
        LayoutAllposts.addView(itemPostView);
    }
}

