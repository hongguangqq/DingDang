package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.BoardData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;
import com.zdlw.demo.dingdang.ui.holder.BoardHolder;

import java.util.List;

/**
 * @author LinWei on 2017/8/24 15:24
 */
public class BoardAdapter extends BaseAdapter<BoardData.Data> {
    public BoardAdapter(List<BoardData.Data> data, Context context, OnRecyclerItemClickListener listener) {
        super(data, context, listener);
    }

    @Override
    public BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_board,parent,false);
        return new BoardHolder(itemView,mlistener);
    }
}
