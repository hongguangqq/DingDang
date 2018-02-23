package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PageData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;
import com.zdlw.demo.dingdang.ui.holder.PageHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/23 14:51
 */
public class PageAdapter extends BaseAdapter<PageData.Data> {
    public PageAdapter(List<PageData.Data> data, Context context, OnRecyclerItemClickListener listener) {
        super(data, context, listener);
    }

    @Override
    public BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_message,parent,false);
        return new PageHolder(itemView,mlistener);
    }
}
