package com.zdlw.demo.dingdang.ui.fragment.page;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;

/**
 * @author LinWei on 2017/8/23 16:47
 */
public abstract class DetailFragment extends Fragment {

    private ImageView mIv_back;
    private TextView mTv_title;
    private FrameLayout mFl_content;
    protected Activity mActivity;

    public DetailFragment(Activity activity){
        mActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_detail,container,false);
        mIv_back = (ImageView) rootView.findViewById(R.id.iv_detail_back);
        mTv_title = (TextView) rootView.findViewById(R.id.tv_detail_title);
        mFl_content = (FrameLayout) rootView.findViewById(R.id.fl_detail_content);
        mFl_content.addView(initView());
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
            }
        });
        initData();
        return rootView;
    }

    public abstract View initView();

    public abstract void initData();

    public void setTitle(String title){
        mTv_title.setText(title);
    }

    public void setVisibleBack(boolean isshow){
        if (isshow){
            mIv_back.setVisibility(View.VISIBLE);
        }else {
            mIv_back.setVisibility(View.GONE);
        }

    }
}
