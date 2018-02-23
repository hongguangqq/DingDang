package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.IssueData;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;

import java.util.List;

/**
 * @author LinWei on 2017/8/28 15:48
 */
public class EntrustHolder extends BaseHolder<IssueData.Data> {
    private CheckBox cb_delete;
    private TextView tv_author;
    private TextView tv_time;
    private TextView tv_course;
    private TextView tv_receive;
    private TextView tv_receiver;
    private TextView tv_money;
    private TextView tv_sms;
    private TextView tv_msg;
    public EntrustHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        cb_delete= (CheckBox) itemView.findViewById(R.id.cb_issue_delete);
        tv_author= (TextView) itemView.findViewById(R.id.tv_issue_author);
        tv_time= (TextView) itemView.findViewById(R.id.tv_issue_time);
        tv_course= (TextView) itemView.findViewById(R.id.tv_issue_course);
        tv_money= (TextView) itemView.findViewById(R.id.tv_issue_money);
        tv_receive= (TextView) itemView.findViewById(R.id.tv_issue_receive);
        tv_receiver= (TextView) itemView.findViewById(R.id.tv_issue_receiver);
        tv_sms= (TextView) itemView.findViewById(R.id.tv_issue_sms);
        tv_msg= (TextView) itemView.findViewById(R.id.tv_issue_msg);
    }

    @Override
    public void onBindViewHolder(List<IssueData.Data> data, int position) {
        cb_delete.setVisibility(View.GONE);
        tv_receive.setVisibility(View.GONE);
        tv_receiver.setVisibility(View.GONE);
        tv_author.setText("老师: "+data.get(position).author);
        tv_course.setText("课程: "+data.get(position).course);
        tv_time.setText("上课时间: "+data.get(position).time);
        tv_money.setText("薪酬: "+data.get(position).money);
        tv_sms.setText("留言："+data.get(position).sms);
        tv_msg.setText(data.get(position).msg);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.OnDetailClick(v,getPosition());
                return true;
            }
        });
    }
}
