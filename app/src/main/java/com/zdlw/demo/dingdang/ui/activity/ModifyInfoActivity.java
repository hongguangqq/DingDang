package com.zdlw.demo.dingdang.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author LinWei on 2017/11/19 12:16
 */
public class ModifyInfoActivity extends AppCompatActivity {

    private EditText mEt_oldPwd;
    private EditText mEt_newPwd;
    private EditText mEt_checkPwd;
    private EditText mEt_phone;
    private EditText mEt_introduction;
    private ImageView mIv_back;
    private Button mBtn_modify;
    private PersonData.Data mInfo;
    private TextView mTv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyinfo);
        init();
        initData();
    }

    private void init() {
        mInfo = (PersonData.Data) getIntent().getSerializableExtra("data");
        mEt_oldPwd = (EditText) findViewById(R.id.et_old);
        mEt_newPwd = (EditText) findViewById(R.id.et_new);
        mEt_checkPwd = (EditText) findViewById(R.id.et_check);
        mEt_phone = (EditText) findViewById(R.id.et_phone);
        mEt_introduction = (EditText) findViewById(R.id.et_introduction);
        mBtn_modify = (Button) findViewById(R.id.btn_modify);
        mIv_back= (ImageView) findViewById(R.id.iv_title_left);
        mTv_title = (TextView) findViewById(R.id.tv_title_title);
        mTv_title.setText("修改信息");
        mEt_phone.setText(mInfo.phone);
        mEt_introduction.setText(mInfo.introduction);
    }

    private void initData(){
        mBtn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushInfo();
            }
        });
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void pushInfo(){
        String str_old=mEt_oldPwd.getText().toString().trim();
        String str_new=mEt_newPwd.getText().toString().trim();
        String str_check=mEt_checkPwd.getText().toString().trim();
        String str_phone=mEt_phone.getText().toString().trim();
        String str_introduction=mEt_introduction.getText().toString().trim();
        if (StringUtils.isEmpty(str_old)
                || StringUtils.isEmpty(str_new)
                ||StringUtils.isEmpty(str_check)
                ||StringUtils.isEmpty(str_phone)
                ||StringUtils.isEmpty(str_introduction)){
            UIUtils.makeText("参数缺失，请确认");
        }else {
            if (!mInfo.pwd.equals(str_old)){
                UIUtils.makeText("当前密码错误，请确认");
                return;
            }
            if (!str_new.equals(str_check)){
                UIUtils.makeText("密码前后不一致，请确认");
                return;
            }
            //提交数据
            OkHttpUtils
                    .post()
                    .url(Const.URL_ReInfo)
                    .addParams("_id",mInfo._id)
                    .addParams("pwd",str_new)
                    .addParams("phone",str_phone)
                    .addParams("introduction",str_introduction)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if ("1".equals(response.trim())){
                                UIUtils.makeText("修改成功");
                                finish();
                            }

                        }
                    });
        }
    }
}
