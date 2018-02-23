package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.FocusData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.List;

/**
 * @author LinWei on 2017/8/29 14:27
 */
public class FocusHolder extends BaseHolder<FocusData.Data> {
    private CircleImageView civ_head;
    private TextView tv_name;
    public FocusHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        civ_head= (CircleImageView) itemView.findViewById(R.id.civ_focus_head);
        tv_name= (TextView) itemView.findViewById(R.id.tv_focus_name);
    }

    @Override
    public void onBindViewHolder(List<FocusData.Data> data, int position) {
        Glide.with(UIUtils.getContext())
                .load(Const.URL_Pic+data.get(position).pic)
                .error(R.drawable.tou)
                .into(civ_head);
        tv_name.setText(data.get(position).name);
    }
}
