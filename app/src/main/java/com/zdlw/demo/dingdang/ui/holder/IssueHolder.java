package com.zdlw.demo.dingdang.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.IssueData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;

import java.util.List;

/**
 * @author LinWei on 2017/8/25 14:36
 */
public class IssueHolder extends BaseHolder<IssueData.Data> {
    private CheckBox cb_delete;
    private TextView tv_age;
    private TextView tv_time;
    private TextView tv_course;
    private TextView tv_receive;
    private TextView tv_receiver;
    private TextView tv_money;
    private TextView tv_sms;
    private TextView tv_msg;


    public IssueHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        cb_delete= (CheckBox) itemView.findViewById(R.id.cb_issue_delete);
        tv_age= (TextView) itemView.findViewById(R.id.tv_issue_author);
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
        if (Const.isShowCheck){
            cb_delete.setVisibility(View.VISIBLE);
        }else {
            cb_delete.setVisibility(View.GONE);
        }
        if (data.get(position).isCheck){
            cb_delete.setChecked(true);
        }else {
            cb_delete.setChecked(false);
        }

        if (1==data.get(position).age){
            tv_age.setText("学级: 小学");
        }else if (2==data.get(position).age){
            tv_age.setText("学级: 初中");
        }else if (3==data.get(position).age){
            tv_age.setText("学级: 高中");
        }

        tv_time.setText("上课时间: "+data.get(position).time);
        tv_money.setText("薪酬: "+data.get(position).money);
        tv_course.setText("课程: "+data.get(position).course);
        if (data.get(position).receive==-1){
            tv_receive.setText("状态: 未预定");
            tv_receiver.setText("预订用户: ");
            tv_sms.setText("留言：");
        }else {
            tv_receive.setText("状态: 预定");
            tv_receiver.setText("预订用户: "+data.get(position).receiver);
            tv_sms.setText("留言："+data.get(position).sms);
        }
        tv_msg.setText(data.get(position).msg);


    }
}
