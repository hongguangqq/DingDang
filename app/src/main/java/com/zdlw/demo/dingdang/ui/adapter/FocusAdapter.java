package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.FocusData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;
import com.zdlw.demo.dingdang.ui.holder.FocusHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/29 14:27
 */
public class FocusAdapter extends BaseAdapter<FocusData.Data> {
    public FocusAdapter(List<FocusData.Data> data, Context context, OnRecyclerItemClickListener listener) {
        super(data, context, listener);
    }

    @Override
    public BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_focus,parent,false);
        return new FocusHolder(itemView,mlistener);
    }
}
