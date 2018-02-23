package com.zdlw.demo.dingdang.ui.fragment.page;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PageData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.activity.DetailActivity;
import com.zdlw.demo.dingdang.ui.activity.OtherActivity;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**具体的消息界面
 * @author LinWei on 2017/8/23 15:47
 */
public class EntryFragment extends DetailFragment {
    private DetailActivity mDetailActivity;
    private PageData.Data mData;
    private ImageView mIv_head;
    private TextView mTv_name;
    private TextView mTv_time;
    private TextView mTv_money;
    private TextView mTv_msg;
    private Button mBtn_reserve;
    private AlertDialog dialog;
    private int STATE_RESERVE;//消息的预定状态
    private final int RESERVE_A=1;//没有用户预定
    private final int RESERVE_B=2;//当前用户预定
    private final int RESERVE_C=3;//其他用户预定
    private EditText mEt_sms;

    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    if (STATE_RESERVE==RESERVE_A){
                        //点击前是未预定状态
                        UIUtils.makeText("预定成功");
                        dialog.dismiss();
                        STATE_RESERVE=RESERVE_B;
                    }else if (STATE_RESERVE==RESERVE_B){
                        //点击前已经被当前用户预定了
                        UIUtils.makeText("预定取消成功");
                        STATE_RESERVE=RESERVE_A;
                    }
                    JudgeReserve();
                    Const.isModify=true;
                    break;
                case Const.STATE_Fail:

                    break;
                default:
                    break;
            }
        }
    };


    public EntryFragment(Activity activity) {
        super(activity);
    }


    @Override
    public View initView() {
        mDetailActivity= (DetailActivity) getActivity();
        Bundle bundle=EntryFragment.this.getArguments();
        mData = (PageData.Data) bundle.getSerializable(Const.Extra_Detail);
        View view=View.inflate(mActivity, R.layout.fragment_entry,null);
        mIv_head = (ImageView) view.findViewById(R.id.civ_entry_head);
        mTv_name = (TextView) view.findViewById(R.id.tv_entry_name);
        mTv_time = (TextView) view.findViewById(R.id.tv_entry_time);
        mTv_money = (TextView) view.findViewById(R.id.tv_entry_money);
        mTv_msg = (TextView) view.findViewById(R.id.tv_entry_msg);
        mBtn_reserve = (Button) view.findViewById(R.id.btn_entrt_reserve);
        return view;
    }

    @Override
    public void initData() {
        setTitle("消息");
        Glide.with(mActivity)
                .load(Const.URL_Pic+mData.aid+".jpg")
                .error(R.drawable.tou)
                .into(mIv_head);
        mTv_name.setText(mData.author);
        mTv_time.setText(mData.time);
        mTv_money.setText("薪酬："+mData.money);
        mTv_msg.setText(mData.msg);
        mIv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), OtherActivity.class);
                intent.putExtra("id",mData._id);
                getActivity().startActivity(new Intent(getActivity(), OtherActivity.class));
            }
        });

        /*--------------------------------------预定的留言备注--------------------------------------*/
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        mEt_sms = new EditText(UIUtils.getContext());
        mEt_sms.setTextColor(getResources().getColor(R.color.black));
        builder.setTitle("请输入您希望传递给老师的留言");
        builder.setView(mEt_sms);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                dialog.dismiss();
                return true;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sms=mEt_sms.getText().toString().trim();
                if (mData.aid!=UIUtils.getSpInt(Const.USER_ID)){
                    initHttp(1,sms);
                    mEt_sms.setText("");
                }else {
                    UIUtils.makeText("不可接受自己的课程委托");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mEt_sms.setText("");
            }
        });
        dialog=builder.create();

        /*--------------------------------根据条目的信息，决定按钮的状态码----------------------------*/
        if (mData.rid==-1){
            STATE_RESERVE=RESERVE_A;
        }else if (mData.rid==UIUtils.getSpInt(Const.USER_ID)){
            STATE_RESERVE=RESERVE_B;
        }else {
            STATE_RESERVE=RESERVE_C;
        }
        JudgeReserve();
        /*--------------------------------按钮的点击处理--------------------------------------------*/
        mBtn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Const.IsChangeState=true;
                switch (STATE_RESERVE) {
                    case RESERVE_A:
                        dialog.show();
                        break;
                    case RESERVE_B:
                        initHttp(-1,null);
                        break;
                    default:
                        break;
                }
            }
        });
    }



    /**
     * ，根据3个状态给予不同响应
     */
    private void JudgeReserve(){
        switch (STATE_RESERVE) {
            case RESERVE_A:
                mBtn_reserve.setEnabled(true);
                mBtn_reserve.setText("预订");
                break;
            case RESERVE_B:
                mBtn_reserve.setEnabled(true);
                mBtn_reserve.setText("您已预订");
                break;
            case RESERVE_C:
                mBtn_reserve.setEnabled(false);
                mBtn_reserve.setText("已被其他用户预定");
                break;
            default:
                break;
        }
    }

    /**
     * 修改消息的预定状态
     * @param state 1为预定/-1取消预订
     * @param sms   接受者的留言
     */
    private void initHttp(int state,String sms){
        OkHttpUtils
                .get()
                .addParams("_id",mData._id+"")//消息ID
                .addParams("receiver", UIUtils.getSpString(Const.USER_NAME))//接受者名称
                .addParams("rid",UIUtils.getSpInt(Const.USER_ID)+"")//接受者ID
                .addParams("state",state+"")//接收状态 1为接收 -1为取消
                .addParams("sms",sms)//留言
                .url(Const.URL_Reserve)
                .build()
                .execute(new StringCallback() {
                    Message message=new Message();
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        message.what=Const.STATE_Fail;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if ("OK".endsWith(response.trim())){
                            message.what=Const.STATE_Success;
                        }
                        mHandler.sendMessage(message);
                    }
                });

    }

}
