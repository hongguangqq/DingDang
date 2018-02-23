package com.zdlw.demo.dingdang.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.utils.UIUtils;

/**
 * Created by Administrator on 2017/5/18.
 */
public class ITView extends RelativeLayout {

    private ImageView iv_pic;
    private TextView tv_msg;

    public ITView(Context context) {
        super(context);
        init(context);
    }

    public ITView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ITView);
        String msg=typedArray.getString(R.styleable.ITView_msg);
        int pic=typedArray.getResourceId(R.styleable.ITView_pic, 0);
        typedArray.recycle();
        tv_msg.setText(msg);
        iv_pic.setImageResource(pic);
    }

    public ITView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context,R.layout.view_itview,this);
        iv_pic = (ImageView) this.findViewById(R.id.iv_itview_pic);
        tv_msg = (TextView) this.findViewById(R.id.tv_itview_msg);
    }
}
