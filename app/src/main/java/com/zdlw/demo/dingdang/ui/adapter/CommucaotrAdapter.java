package com.zdlw.demo.dingdang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.CommuniData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.holder.BaseHolder;
import com.zdlw.demo.dingdang.ui.holder.FromHolder;
import com.zdlw.demo.dingdang.ui.holder.HostHolder;

import java.util.List;

/**
 * @author LinWei on 2017/12/6 14:29
 */
public class CommucaotrAdapter  extends BaseAdapter<CommuniData>{
    private final int Type_Host = 1;
    private final int Type_From = 2;
    public CommucaotrAdapter(List<CommuniData> data, Context context, OnRecyclerItemClickListener listener) {
        super(data, context, listener);
    }

    @Override
    public BaseHolder getHolder(LayoutInflater mInflater, ViewGroup parent, int viewType) {
        BaseHolder holder =null;
        View itemView = null;
        if (viewType == Type_Host){
            itemView=mInflater.inflate(R.layout.item_host,parent,false);
            holder = new HostHolder(itemView,mlistener);
        }else {
            itemView=mInflater.inflate(R.layout.item_from,parent,false);
            holder = new FromHolder(itemView,mlistener);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isHost()){
            return Type_Host;
        }else {
            return Type_From;
        }
    }


}
