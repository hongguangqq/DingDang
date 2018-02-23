package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.IssueData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;
import com.zdlw.demo.dingdang.ui.holder.EntrustHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/28 15:48
 */
public class EntrustAdapter extends BaseAdapter<IssueData.Data> {
    public EntrustAdapter(List<IssueData.Data> data, Context context, OnRecyclerItemClickListener listener) {
        super(data, context, listener);
    }

    @Override
    public BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_issue,parent,false);
        return new EntrustHolder(itemView,mlistener);
    }
}
