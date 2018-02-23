package com.zdlw.demo.dingdang.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;

import java.util.List;

/**
 * @author LinWei on 2017/8/16 16:46
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected OnRecyclerItemClickListener mListener;
    public BaseHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView);
        mListener=listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener!=null){
            mListener.OnClick(v,getPosition());
        }
    }

    public  abstract void onBindViewHolder(List<T> data, int position);


}
