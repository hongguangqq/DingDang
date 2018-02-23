package com.zdlw.demo.dingdang.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/5/29.
 */
public class MyViewPage extends ViewPager {
    public MyViewPage(Context context) {
        super(context);
    }

    public MyViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不要拦截我的事件
        }else {//如果是第一个界面，请求拦截。
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
