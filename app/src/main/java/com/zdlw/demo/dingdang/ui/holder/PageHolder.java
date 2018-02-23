package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PageData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.List;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PageHolder extends BaseHolder<PageData.Data> {
    private TextView tv_user;
    private TextView tv_time;
    private TextView tv_receive;
    private TextView tv_course;
    private TextView tv_money;
    private TextView tv_msg;
    public PageHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        tv_course= (TextView) itemView.findViewById(R.id.tv_item_Message_course);
        tv_time= (TextView) itemView.findViewById(R.id.tv_item_Message_time);
        tv_receive= (TextView) itemView.findViewById(R.id.tv_item_Message_reciver);
        tv_user= (TextView) itemView.findViewById(R.id.tv_item_Message_user);
        tv_money= (TextView) itemView.findViewById(R.id.tv_item_Message_money);
        tv_msg= (TextView) itemView.findViewById(R.id.tv_item_MEssage_msg);
    }

    @Override
    public void onBindViewHolder(List<PageData.Data> data, int position) {
        PageData.Data pdata=data.get(position);
        tv_course.setText(pdata.course);
        tv_time.setText(pdata.time);
        tv_receive.setText(pdata.receive==-1 ? "未接受":"已接受");
        tv_receive.setTextColor(pdata.receive==-1 ? UIUtils.getContext().getResources().getColor(R.color.gray)
                                                  :UIUtils.getContext().getResources().getColor(R.color.red));
        tv_user.setText(pdata.author);
        tv_money.setText("薪酬: "+pdata.money);
        tv_msg.setText(pdata.msg);

    }
}
