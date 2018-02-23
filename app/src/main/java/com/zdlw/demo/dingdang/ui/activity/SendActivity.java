package com.zdlw.demo.dingdang.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.view.SettingItemView;
import com.zdlw.demo.dingdang.utils.DateUtils;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    private OptionsPickerView age_Options;
    private OptionsPickerView course_Options;
    private SettingItemView mItem_age;
    private SettingItemView mItem_course;
    private SettingItemView mItem_time;
    private SettingItemView mItem_money;
    private EditText met_msg;
    private Button mBtn_send;
    private TimePickerDialog time_options;
    private String PostAge;
    private String PostCourse;
    private String PostTime;
    private String PostMoney;
    private String PostMsg;
    private AlertDialog money_dialog;
    private ImageView mIv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();
        initOptions();
        initListener();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_send_back);
        mItem_age = (SettingItemView) findViewById(R.id.st_send_age);
        mItem_course = (SettingItemView) findViewById(R.id.st_send_course);
        mItem_time = (SettingItemView) findViewById(R.id.st_send_time);
        mItem_money = (SettingItemView) findViewById(R.id.st_send_money);
        met_msg= (EditText) findViewById(R.id.et_send_msg);
        mBtn_send = (Button) findViewById(R.id.btn_send_submit);
    }

    private void initOptions() {
        String[] courseString= UIUtils.getStringArray(R.array.course);
        String[] ageString=UIUtils.getStringArray(R.array.tab_name);
        final List<String> agelist= Arrays.asList(ageString);
        final List<String> courselist= Arrays.asList(courseString);


        age_Options = new  OptionsPickerView.Builder(SendActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mItem_age.setRightText(agelist.get(options1));
                PostAge=(options1+1)+"";
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("学级")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.red))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        age_Options.setPicker(agelist);
        //课程
        course_Options = new  OptionsPickerView.Builder(SendActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mItem_course.setRightText(courselist.get(options1));
                PostCourse=courselist.get(options1);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("课程")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.red))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        course_Options.setPicker(courselist);
        //时间选择器
        time_options = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        mItem_time.setRightText(DateUtils.timetByOrginStr(String.valueOf(millseconds)));
                        PostTime=DateUtils.timetByOrginStr(String.valueOf(millseconds));
                    }
                })
                .setTitleStringId("")
                .setCyclic(false)
                .setSelectorMillseconds(System.currentTimeMillis())
                .setType(Type.ALL)
                .setWheelItemTextSize(12)
                .build();
        //酬金
        View dialogView=View.inflate(UIUtils.getContext(),R.layout.dialog_issue,null);
        final EditText et_money= (EditText) dialogView.findViewById(R.id.et_dialog_money);
        Button btn_submit= (Button) dialogView.findViewById(R.id.btn_dialog_submit);
        Button btn_cancel= (Button) dialogView.findViewById(R.id.btn_dialog_cancel);
        AlertDialog.Builder builder=new AlertDialog.Builder(SendActivity.this);
        builder.setTitle("请输入课程的酬金");// 80 化学专业本科生
        builder.setCancelable(false);
        builder.setView(dialogView);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                money_dialog.dismiss();
                et_money.setText("");

                return true;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostMoney=et_money.getText().toString().trim();
                money_dialog.dismiss();
                et_money.setText("");
                if (!StringUtils.isEmpty(PostMoney)){
                    mItem_money.setRightText(PostMoney);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money_dialog.dismiss();
                et_money.setText("");
            }
        });
        money_dialog=builder.create();

    }

    private void initListener() {
        mItem_age.setOnClickListener(this);
        mItem_course.setOnClickListener(this);
        mItem_time.setOnClickListener(this);
        mItem_money.setOnClickListener(this);
        mBtn_send.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
    }

    private void PostHttp(){
        if (!CheckIsNull()){
            OkHttpUtils
                    .post()
                    .addParams("aid",UIUtils.getSpInt(Const.USER_ID)+"")
                    .addParams("author",UIUtils.getSpString(Const.USER_NAME))
                    .addParams("age",PostAge)
                    .addParams("course",PostCourse)
                    .addParams("time",PostTime)
                    .addParams("money",PostMoney)
                    .addParams("msg",PostMsg)
                    .url(Const.URL_IssuePublish)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    UIUtils.makeText("发布失败，请重试");
                                }
                            });
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (!StringUtils.isEmpty(response)&&"1".equals(response.trim())){
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIUtils.makeText("发布成功");
                                        finish();
                                        overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                                        Const.isPlilsh=true;
                                        Const.isModify=true;
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private boolean CheckIsNull(){
        int i=0;
        PostMsg=met_msg.getText().toString().trim();
        if (!StringUtils.isEmpty(PostAge)){
            i++;
        }else {
            UIUtils.makeText("您未选择学级");
        }

        if (!StringUtils.isEmpty(PostCourse)){
            i++;
        }else {
            UIUtils.makeText("您未选择课程");
        }

        if (!StringUtils.isEmpty(PostTime)){
            i++;
        }else {
            UIUtils.makeText("您未选择课程时间");
        }

        if (!StringUtils.isEmpty(PostMoney)){
            i++;
        }else {
            UIUtils.makeText("您未输入酬金");
        }
        if (!StringUtils.isEmpty(PostMsg)){
            i++;
        }else {
            UIUtils.makeText("您未填写简介");
        }
        if (i==5){
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.st_send_age:
                age_Options.show();
                break;
            case R.id.st_send_course:
                course_Options.show();
                break;
            case R.id.st_send_time:
                time_options.show(getSupportFragmentManager(),"");
                break;
            case R.id.st_send_money:
                money_dialog.show();
                break;
            case R.id.btn_send_submit:
                PostHttp();
                break;
            case R.id.iv_send_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);

    }
}
