package com.zdlw.demo.dingdang.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.http.HttpConnectionUtil;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.ui.view.SettingItemView;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView civ_head;
    private TextView tv_title;
    private ImageView iv_back;
    private ImageView iv_edit;
    private TextView tv_name;
    private SettingItemView item_school;
    private SettingItemView item_subject;
    private SettingItemView item_age;
    private SettingItemView item_phone;
    private TextView tv_introduction;
    private Button btn_modify;
    private AlertDialog mDialog;
    private TextView tv_hint;
    private EditText et_info;
    private EditText et_oldpwd;
    private EditText et_newpwd;
    private String modify_pwd;
    private String modify_phone;
    private String modify_introduction;
    private int STATE_MODIFY=-1;
    private final int STATE_Phone=2;
    private final int STATE_Pwd=3;
    private File pictureFile;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private PersonData mData;

    private Handler mHandler=new Handler(){



        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    mData = (PersonData) msg.obj;
                    tv_name.setText(UIUtils.getSpString(Const.USER_NAME));
                    modify_phone= mData.data.get(0).phone;
                    modify_introduction= mData.data.get(0).introduction;
                    item_phone.setRightText(modify_phone);
                    item_school.setRightText(mData.data.get(0).school);
                    item_subject.setRightText(mData.data.get(0).subject);
                    item_age.setRightText(mData.data.get(0).age);
                    tv_introduction.setText(modify_introduction);
                    Glide.get(PersonActivity.this).clearMemory();
                    Glide.with(PersonActivity.this)
                            .load(Const.URL_Pic+ UIUtils.getSpInt(Const.USER_ID)+".jpg")
                            .error(R.drawable.tou)
                            .into(civ_head);
                    break;

                default:
                    break;
            }
        }
    };
    private AlertDialog.Builder mBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initView();
        initFile();
        initData();
//        initDialog();
        initListener();
    }

    private void initView(){
        tv_title= (TextView) findViewById(R.id.tv_title_title);
        iv_back= (ImageView) findViewById(R.id.iv_title_left);
        iv_edit= (ImageView) findViewById(R.id.iv_title_right);
        civ_head= (CircleImageView) findViewById(R.id.civ_person_pic);
        tv_name= (TextView) findViewById(R.id.tv_person_name);
        item_school = (SettingItemView) findViewById(R.id.st_pinfo_school);
        item_subject = (SettingItemView) findViewById(R.id.st_pinfo_subject);
        item_age = (SettingItemView) findViewById(R.id.st_pinfo_age);
        item_phone= (SettingItemView) findViewById(R.id.st_pinfo_phone);
        tv_introduction= (TextView) findViewById(R.id.tv_pinfo_introduction);

        tv_title.setText("个人信息");
        iv_edit.setVisibility(View.VISIBLE);
        iv_edit.setImageDrawable(getResources().getDrawable(R.mipmap.icon_edit));
        item_school.setNext(false);
        item_subject.setNext(false);
        item_age.setNext(false);
        item_phone.setNext(false);

    }

    /**
     * 创建pic文件
     */
    private void initFile() {
        pictureFile = new File(getFilesDir(), "pic.jpg");
        if (!pictureFile.exists()) {
            try {
                pictureFile.getParentFile().mkdirs();
                pictureFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initData(){
        OkHttpUtils.get()
                .url(Const.URL_GetPersonData)
                .addParams("_id",UIUtils.getSpInt(Const.USER_ID)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PersonData data=new Gson().fromJson(response,PersonData.class);
                        if (data.data!=null && data.data.size()>0){
                            Message msg=new Message();
                            msg.obj=data;
                            msg.what=Const.STATE_Success;
                            mHandler.sendMessage(msg);
                        }

                    }
                });



    }

    private void initDialog() {
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(this);
        View dialogView=View.inflate(this,R.layout.dialog_modify,null);
        mBuilder.setView(dialogView);
        tv_hint= (TextView) dialogView.findViewById(R.id.tv_modify_hint);
        et_info= (EditText) dialogView.findViewById(R.id.et_modify_info);
        et_oldpwd= (EditText) dialogView.findViewById(R.id.et_modify_oldpwd);
        et_newpwd= (EditText) dialogView.findViewById(R.id.et_modify_newpwd);
        mBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                et_info.setText("");
                et_oldpwd.setText("");
                et_newpwd.setText("");
                dialog.dismiss();
                return true;
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_info.setText("");
                et_oldpwd.setText("");
                et_newpwd.setText("");
                dialog.dismiss();
            }
        });
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String modifyStr = et_info.getText().toString();
                if (!StringUtils.isEmpty(modifyStr)){
                    switch (STATE_MODIFY) {
                        case STATE_Pwd:

                            break;
                        case STATE_Phone:

                            break;
                        default:
                            break;
                    }
                }else {
                    UIUtils.makeText("修改信息参数不能空白");
                }

        }});
        mDialog=mBuilder.create();
    }

    private void initListener(){
        iv_back.setOnClickListener(this);
        civ_head.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.civ_person_pic:
                gallery();
                break;
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                if (mData.data.get(0)!=null){
                    Intent intent =new Intent(PersonActivity.this,ModifyInfoActivity.class);
                    intent.putExtra("data",mData.data.get(0));
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }



    public void showAlert(){
        et_info.setText("");
        et_oldpwd.setText("");
        et_newpwd.setText("");
        switch (STATE_MODIFY) {
            case STATE_Pwd:
                tv_hint.setText("修改密码");
                et_info.setHint("请输入新的密码");
                et_oldpwd.setVisibility(View.VISIBLE);
                et_newpwd.setVisibility(View.VISIBLE);
                break;
            case STATE_Phone:
                tv_hint.setText("修改电话");
                et_info.setHint("请输入新的电话");
                et_oldpwd.setVisibility(View.GONE);
                et_newpwd.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 按下头像的操作
     */
    public void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相册返回图片的相关处理
     */
    private void Logigallery(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            crop(uri);

        }
    }

    /*
    * 剪切图片
    */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                Logigallery(data);
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    savePicture(bitmap, pictureFile);
                }
                break;
        }
    }


    //保存图片到本地,以便今后显示头像
    public void savePicture(Bitmap bitmap, final File picture) {
//        if (picture.exists()) {
//            //删除上一张头像
//            picture.delete();
//        }
        BufferedOutputStream ops = null;

        try {
            ops = new BufferedOutputStream(new FileOutputStream(picture));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ops);

            ops.flush();
            final Map<String,Object> map=new HashMap<>();
            map.put("id",UIUtils.getSpInt(Const.USER_ID)+"");
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpConnectionUtil.doPostPicture(Const.URL_UpdataPic,map,picture);
                            Glide.get(PersonActivity.this)
                                    .clearDiskCache();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Glide.with(PersonActivity.this)
                        .load(picture)
                        .into(civ_head);
                Intent intent = new Intent();
                intent.setAction(Const.ACTION2);
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ops != null) {
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


//    private void PostModifyFormat(){
//        modify_phone=item_phone.getRightText();
//        modify_pwd=item_phone.getRightText();
//        modify_introduction=et_introduction.getText().toString().trim();
//        if (!StringUtils.isEmpty(modify_pwd)
//                & !StringUtils.isEmpty(modify_phone)
//                & !StringUtils.isEmpty(modify_introduction)){
//            OkHttpUtils
//                    .post()
//                    .addParams("pwd",modify_pwd)
//                    .addParams("phone",modify_phone)
//                    .addParams("introduction",modify_introduction)
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//
//                        }
//                    });
//        }
//    }

    private boolean isChange;
    @Override
    protected void onResume() {
        super.onResume();
        if (isChange){
            initData();
            isChange=false;
        }else {
            isChange=true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(PersonActivity.this)
                        .clearDiskCache();
            }
        }).start();

    }
}
