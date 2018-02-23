package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.CommuniData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.List;

/**
 * @author LinWei on 2017/12/6 14:34
 */
public class FromHolder extends BaseHolder<CommuniData> {
    private ImageView iv_from;
    private TextView tv_from;

    public FromHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        iv_from = (ImageView) itemView.findViewById(R.id.iv_from);
        tv_from = (TextView) itemView.findViewById(R.id.tv_from);
    }

    @Override
    public void onBindViewHolder(List<CommuniData> data, int position) {
        Glide.with(UIUtils.getContext())
                .load(data.get(position).getPic())
                .error(R.drawable.tou)
                .into(iv_from);
        tv_from.setText(data.get(position).getText());
    }
}
