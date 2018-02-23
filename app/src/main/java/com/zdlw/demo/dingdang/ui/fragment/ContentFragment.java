package com.zdlw.demo.dingdang.ui.fragment;


import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.zdlw.demo.dingdang.MainActivity;
import com.zdlw.demo.dingdang.R;

/**
 * Created by Administrator on 2017/5/18.
 */
public class ContentFragment extends MainFragment {
    private MainActivity activity;
    private  int mposition;

    @Override
    public View initui() {
        View view=View.inflate(mActivity,R.layout.fragment_content,null);
        activity = (MainActivity) mActivity;
        return view;
    }

    @Override
    public void initdata() {
        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();
        BaseFragment baseFragment=FactoryFragment.getFragment(0,activity);
        transaction.replace(R.id.fl_content_main,baseFragment).commit();
        baseFragment.loadData();
    }


    /**
     * 通过点击侧边栏，切换显示的Fragment
     * @param position
     */
    protected void s(int position){
        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();
        BaseFragment baseFragment=FactoryFragment.getFragment(position,activity);
        transaction.replace(R.id.fl_content_main,baseFragment);
        transaction.commit();
        baseFragment.loadData();
    }

    protected void switchContentFragment(int position){
        if (mposition==position){
            return;
        }
        BaseFragment newfragment=FactoryFragment.getFragment(position,activity);//需要显示的Fragment
        BaseFragment lastfragment=FactoryFragment.getFragment(mposition,activity);//需要隐藏的Fragment
        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();
        if (newfragment.isAdded()){
            transaction.hide(lastfragment).show(newfragment).commit();
        }else {
            transaction.hide(lastfragment).add(R.id.fl_content_main,newfragment).commit();
        }
        newfragment.loadData();
        mposition=position;
    }
    

}
