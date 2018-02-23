package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.BoardData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.List;

/**
 * @author LinWei on 2017/8/24 15:58
 */
public class BoardHolder extends BaseHolder<BoardData.Data> {
    private CircleImageView civ_head;
    private TextView tv_name;
    private TextView tv_info;
    private TextView tv_time;
    public BoardHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        civ_head= (CircleImageView) itemView.findViewById(R.id.civ_board_head);
        tv_name= (TextView) itemView.findViewById(R.id.tv_board_name);
        tv_info= (TextView) itemView.findViewById(R.id.tv_board_info);
        tv_time= (TextView) itemView.findViewById(R.id.tv_board_time);
    }

    @Override
    public void onBindViewHolder(List<BoardData.Data> data, int position) {
        BoardData.Data bdata=data.get(position);
        Glide.with(UIUtils.getContext())
                .load(Const.URL_Pic+bdata.cid+".jpg")
                .error(R.drawable.tou)
                .into(civ_head);
        tv_name.setText(bdata.commentator);
        tv_info.setText(bdata.info);
        tv_time.setText(bdata.time);
    }
}
