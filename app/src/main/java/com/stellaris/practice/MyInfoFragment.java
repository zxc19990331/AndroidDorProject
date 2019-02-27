package com.stellaris.practice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.stellaris.manager.UsrManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInfoFragment extends Fragment {


    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @BindView(R.id.info_layout_banner)
    LinearLayout mLayoutBanner;

    @BindView(R.id.info_text_name)
    TextView infoTextName;

    @BindView(R.id.info_text_detail)
    TextView infoTextDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ButterKnife绑定fragment
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_info, null);
        ButterKnife.bind(this, root);
        initBanner();
        initGroupListView();
        setInfo();
        return root;
    }

    private void initBanner() {
        //这两行是把头像磨砂化后当做banner背景
        //Bitmap blurred = GaussianBlur.with(getContext()).render(R.drawable.login_banana_0);
        //mLayoutBanner.setBackground(new BitmapDrawable(getResources(),blurred));
    }

    //TODO: 更改图标样式和颜色
    private void initGroupListView() {
        //详细信息
        QMUICommonListItemView item_detail_info = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_info_black_24dp),
                "个人详情",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_detail_info.setTag(0);

        //宿舍缴费
        QMUICommonListItemView item_payment = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_sync_black_24dp),
                "宿舍缴费",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_payment.setTag(1);

        QMUICommonListItemView item_web = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_web_asset_black_24dp),
                "教务系统",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_web.setTag(2);

        //应用设置
        QMUICommonListItemView item_setting = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_settings_black_24dp),
                "应用设置",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_setting.setTag(3);

        //关于作者
        QMUICommonListItemView item_about = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_info_black_24dp),
                "关于作者",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_about.setTag(4);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 0: {
                        Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
                        startActivity(intent);
                    }break;
                    case 2:{
                        //跳转到到相应学校教务系统官网
                        String url = "";
                        switch (UsrManager.getCollegeId()){
                            case "01" :{
                                url = "http://jwc.njnu.edu.cn/";
                                Bundle bundle = new Bundle();
                                bundle.putString("URL",url);
                                bundle.putString("TITLE","菁林园");
                                Intent intent = new Intent(getActivity(),WebDetailActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent,bundle);
                            }break;
                            default:break;
                        }
                    }break;
                    case 3:{
                        //设置界面
                        Intent intent = new Intent(getActivity(), SettingActivity.class);
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(item_detail_info, onClickListener)
                .addItemView(item_payment, onClickListener)
                .addItemView(item_web,onClickListener)
                .addItemView(item_setting, onClickListener)
                .addItemView(item_about, onClickListener)
                .addTo(mGroupListView);
    }

    private void setInfo() {
        infoTextName.setText(UsrManager.getName());
        infoTextDetail.setText(UsrManager.getCollegeId()+"|"+UsrManager.getDorRoomId()+"|"+UsrManager.getStudentId());
    }

    /*---------------------------------------------------*/
    /*以下是编译器自动生成的*/
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyInfoFragment() {
    }

    public static MyInfoFragment newInstance(String param1, String param2) {
        MyInfoFragment fragment = new MyInfoFragment();
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
