package com.zdlw.demo.dingdang.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zdlw.demo.dingdang.R;


/**
 * @author LinWei on 2017/8/18 13:35
 */
public class MyProgressDialog extends AlertDialog {
    public MyProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.AlertDialogStyle);
        setCancelable(false);
        setView(View.inflate(context, R.layout.myprogressview,null));
    }
}
