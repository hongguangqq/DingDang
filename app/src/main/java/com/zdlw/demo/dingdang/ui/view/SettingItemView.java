package com.zdlw.demo.dingdang.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;


/**
 * @author LinWei on 2017/8/18 16:02
 */
public class SettingItemView extends RelativeLayout{

    private TextView mTv_left;
    private TextView mTv_right;
    private ImageView mIv_arrow;

    public SettingItemView(Context context) {
        super(context);
        init(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String left=typedArray.getString(R.styleable.SettingItemView_leftText);
        String right=typedArray.getString(R.styleable.SettingItemView_rightText);
        mIv_arrow = (ImageView) this.findViewById(R.id.iv_settingItem_arrow);
        mTv_left = (TextView) this.findViewById(R.id.tv_settingItem_left);
        mTv_right = (TextView) this.findViewById(R.id.tv_settingItem_right);
        mTv_left.setText(left);
        mTv_right.setText(right);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.settingitem,this);
    }

    public void setLeftText(String text){
        mTv_left.setText(text);
    }

    public void setRightText(String text){
        mTv_right.setText(text);
    }

    public String getRightText(){
        return mTv_right.getText().toString().trim();
    }

    public void setNext(boolean isVisibility){
        if (isVisibility){
            mIv_arrow.setVisibility(VISIBLE);
        }else {
            mIv_arrow.setVisibility(GONE);
        }
    }


}
