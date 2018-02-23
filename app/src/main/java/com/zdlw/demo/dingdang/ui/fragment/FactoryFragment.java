package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/29.
 */
public class FactoryFragment {
    private static HashMap<String,BaseFragment> map=new HashMap<>();
    public static BaseFragment getFragment(int position,Activity activity){
        BaseFragment baseFragment=map.get("fragment"+position);
        if (baseFragment==null){
            switch (position){
                case 0:
                    baseFragment=new NewsFragment(activity);
                    break;
                case 1:
                    baseFragment=new ChatFragment(activity);
                    break;
                case 2:
                    baseFragment=new FocusFragment(activity);
                    break;
                case 3:
                    baseFragment=new IssueFragment(activity);
                    break;
                case 4:
                    baseFragment=new EntrustFragment(activity);
            }
            map.put("fragment"+position,baseFragment);
        }
        return baseFragment;
    }

    public static void Logout(){
        for (int i=0 ;i<5;i++){
            map.put("fragment"+i,null);
        }
    }
}
