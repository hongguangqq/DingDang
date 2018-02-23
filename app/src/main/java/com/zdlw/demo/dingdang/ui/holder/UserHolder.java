package com.zdlw.demo.dingdang.ui.holder;


import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.List;

/**
 * @author LinWei on 2017/12/5 15:13
 */
public class UserHolder extends BaseHolder<PersonData.Data> {
    private CircleImageView civ;
    private TextView tv;

    public UserHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        civ = (CircleImageView) itemView.findViewById(R.id.civ_head);
        tv = (TextView) itemView.findViewById(R.id.tv_name);
    }

    @Override
    public void onBindViewHolder(List<PersonData.Data> data, int position) {
        Glide.with(UIUtils.getContext())
                .load(Const.URL_Pic+data.get(position).pic)
                .error(R.drawable.tou)
                .into(civ);
        tv.setText(data.get(position).name);
    }
}
