package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/16 16:44
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    public List<T> data;
    public Context context;
    private LayoutInflater mInflater;
    protected OnRecyclerItemClickListener mlistener;

    public BaseAdapter(List<T> data, Context context, OnRecyclerItemClickListener listener){
        this.data=data;
        this.context=context;
        mlistener=listener;
        mInflater= LayoutInflater.from(context);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder=getHolder(mInflater,parent,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
       holder.onBindViewHolder(data,position);
    }



    @Override
    public int getItemCount() {
        return data.size()+getMorePosition(0);
    }

    public void  notifyData(List<T> data){
        this.data=data;

    }

    public void  LoadMoreData(List<T> data){
        this.data.addAll(data);
    }

    public abstract BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType);

    public int getMorePosition(int position){
        return position;
    }



}
