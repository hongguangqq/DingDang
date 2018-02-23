package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.utils.UIUtils;

/**
 * Created by Administrator on 2017/5/18.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mactivity;
    private LoadingPage mLoadingPage;
    public BaseFragment(Activity activity){
        this.mactivity=activity;
        mLoadingPage = new LoadingPage(mactivity) {
            @Override
            public View onCreatSuccessView() {
                return BaseFragment.this.onCreatSuccessView();
            }

            @Override
            public ResultState initData() {
                return  BaseFragment.this.iniData();
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return mLoadingPage;
    }

    /**
     * 该方法执行顺序在iniData()之后，用于获取数据后的设置
     * @return
     */
    public abstract View onCreatSuccessView();

    /**
     * 改方法存在于子线程，只用于获取数据
     * @return
     */
    public abstract LoadingPage.ResultState iniData();
    //loaddata启动，带动initui，initPageData方法启动
    public void loadData(){
        if (mLoadingPage!=null){
            mLoadingPage.loadData();
        }
    }

}
