package com.zdlw.demo.dingdang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.ui.view.SettingItemView;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_bottom;
    private CircleImageView civ_head;
    private TextView tv_name;
    private TextView tv_title;
    private ImageView iv_back;
    private SettingItemView st_school;
    private SettingItemView st_subject;
    private SettingItemView st_age;
    private SettingItemView st_phone;
    private TextView tv_introduction;
    private TextView tv_fllower;
    private TextView tv_communicator;
    private TextView tv_report;
    private String mName;
    private int toid;
    private boolean isFocus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        init();
        initData();
        initListener();
    }

    private void init() {
        toid=getIntent().getIntExtra("id",0);
        Log.e("@#","toid="+toid);
        tv_title= (TextView) findViewById(R.id.tv_title_title);
        iv_back= (ImageView) findViewById(R.id.iv_title_left);
        civ_head= (CircleImageView) findViewById(R.id.civ_person_pic);
        tv_name= (TextView) findViewById(R.id.tv_person_name);
        rl_bottom= (RelativeLayout) findViewById(R.id.rl_other_news);
        rl_bottom.setVisibility(View.VISIBLE);
        st_school= (SettingItemView) findViewById(R.id.st_pinfo_school);
        st_subject= (SettingItemView) findViewById(R.id.st_pinfo_subject);
        st_age= (SettingItemView) findViewById(R.id.st_pinfo_age);
        st_phone= (SettingItemView) findViewById(R.id.st_pinfo_phone);
        tv_introduction= (TextView) findViewById(R.id.tv_pinfo_introduction);
        tv_fllower= (TextView) findViewById(R.id.tv_person_focus);
        tv_communicator= (TextView) findViewById(R.id.tv_person_communicator);
        tv_report= (TextView) findViewById(R.id.tv_person_report);

    }

    private void initData() {
        tv_name.setText("");
        st_school.setNext(false);
        st_subject.setNext(false);
        st_age.setNext(false);
        st_phone.setNext(false);
        OkHttpUtils
                .get()
                .url(Const.URL_GetPersonData)
                .addParams("_id",toid+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("@#","SS:"+response);
                        PersonData data=new Gson().fromJson(response,PersonData.class);
                        mName = data.data.get(0).name;
                        tv_title.setText(mName);
                        st_school.setRightText(data.data.get(0).school);
                        st_subject.setRightText(data.data.get(0).subject);
                        st_age.setRightText(data.data.get(0).age);
                        st_phone.setRightText(data.data.get(0).phone);
                        tv_introduction.setText(data.data.get(0).introduction);
                        Glide.with(OtherActivity.this).load(Const.URL_Pic+ toid+".jpg").error(getResources().getDrawable(R.drawable.tou)).into(civ_head);
                    }
                });
        SearchFocus();
    }

    private void initListener(){
        tv_fllower.setOnClickListener(this);
        tv_communicator.setOnClickListener(this);
    }

    private void SearchFocus(){
        OkHttpUtils
                .get()
                .url(Const.URL_SearchFous)
                .addParams("toid",toid+"")
                .addParams("id", UIUtils.getSpInt(Const.USER_ID)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!StringUtils.isEmpty(response)){
                            String[] result = response.split("&");
                            if ("exist".equals(result[0])){
                                //已关注
                                tv_fllower.setText("已关注("+result[1]+")");
                                if (isFocus){
                                    UIUtils.makeText("已关注该用户");
                                }
                            }else if ("unexist".equals(result[0])){
                                //未关注
                                tv_fllower.setText("关注");
                                if (isFocus){
                                    UIUtils.makeText("已取消该用户的关注");
                                }
                            }
                            Intent intent =new Intent();
                            intent.setAction(Const.ACTION3);
                            sendBroadcast(intent);
                        }
                    }
                });
    }

    private void toFocus(){
        OkHttpUtils
                .get()
                .url(Const.URL_ToFocus)
                .addParams("iget",toid+"")
                .addParams("meid", UIUtils.getSpInt(Const.USER_ID)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("@#",response);
                        isFocus=true;
                        SearchFocus();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_person_focus:
                toFocus();
                break;
            case R.id.tv_person_communicator:
                Intent intent = new Intent(OtherActivity.this,CommunicateActivity.class);
                intent.putExtra("fromUser",mName);
                startActivity(intent);
                break;
            case R.id.tv_person_report:
                break;

            default:
                break;
        }
    }
}
