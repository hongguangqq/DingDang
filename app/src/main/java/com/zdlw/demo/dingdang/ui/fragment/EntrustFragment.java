package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.IssueData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.adapter.EntrustAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @author LinWei on 2017/8/28 15:16
 */
public class EntrustFragment extends BaseFragment implements OnRecyclerItemClickListener{
    private TwinklingRefreshLayout trl_rl;
    private RecyclerView rv_container;
    private ImageView iv_back;
    private TextView tv_title;
    private EntrustAdapter mAdapter;
    private List<IssueData.Data> mData;
    private int STATE_CURRENT=Const.STATE_Undo;
    private AlertDialog dialog_modify;
    private AlertDialog dialog_cancel;
    private int index;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    if (STATE_CURRENT==Const.STATE_Refresh){
                        trl_rl.finishRefreshing();
                        UIUtils.makeText("刷新成功");
                    }else if (STATE_CURRENT==Const.STATE_MODIFY){
                        UIUtils.makeText("修改留言成功");
                        GetHttp();//刷新自身界面，让用户看到修改结果
                    }else if (STATE_CURRENT==Const.STATE_DELETE){
                        UIUtils.makeText("取消预约成功");
                        //发送广播，告诉发布板界面刷新一次布局
                        Intent intent = new Intent();
                        intent.setAction(Const.ACTION1);
                        mactivity.sendBroadcast(intent);
                    }
                    if (msg.obj==null){
                        mData.clear();
                    }else {
                        mData=(List<IssueData.Data>) msg.obj;
                    }
                    mAdapter.notifyData(mData);
                    mAdapter.notifyDataSetChanged();
                    STATE_CURRENT=Const.STATE_Undo;
                    break;
                case Const.STATE_Fail:
                    if (STATE_CURRENT==Const.STATE_Refresh){
                        trl_rl.finishRefreshing();
                        UIUtils.makeText("刷新失败，请刷新重试");
                    }else if (STATE_CURRENT==Const.STATE_MODIFY){
                        UIUtils.makeText("修改留言失败，请重试");
                    }else if (STATE_CURRENT==Const.STATE_DELETE){
                        UIUtils.makeText("取消预约失败，请重试");
                    }
                    STATE_CURRENT=Const.STATE_Undo;
                    break;
                default:
                    break;
            }
        }
    };



    public EntrustFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View onCreatSuccessView() {
        View view=View.inflate(mactivity, R.layout.fragment_entrust,null);
        iv_back= (ImageView) view.findViewById(R.id.iv_title_left);
        tv_title= (TextView) view.findViewById(R.id.tv_title_title);
        trl_rl= (TwinklingRefreshLayout) view.findViewById(R.id.trl_entrust_reload);
        rv_container= (RecyclerView) view.findViewById(R.id.rv_entrust_container);
        mData=new ArrayList<>();
        mAdapter=new EntrustAdapter(mData,mactivity,this);
        initSetting();
        initListener();
        initDialog();
        GetHttp();
        return view;
    }

    @Override
    public LoadingPage.ResultState iniData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    private void initSetting(){
        iv_back.setVisibility(View.GONE);
        tv_title.setText("预约的课程");
        trl_rl.setEnableLoadmore(false);
        rv_container.setLayoutManager(new LinearLayoutManager(mactivity,LinearLayoutManager.VERTICAL,false));
        rv_container.addItemDecoration(new SpacesItemDecoration(10,20));
        rv_container.setAdapter(mAdapter);

    }

    private void initDialog() {
        //修改留言
        AlertDialog.Builder builder=new AlertDialog.Builder(mactivity);
        final EditText et_modify=new EditText(mactivity);
        et_modify.setTextColor(Color.BLACK);
        builder.setTitle("请输入您新的留言");
        builder.setView(et_modify);
        builder.setCancelable(false);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                et_modify.setText("");
                dialog_modify.dismiss();
                return true;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sms=et_modify.getText().toString().trim();
                if (!StringUtils.isEmpty(sms)){
                    OkHttpUtils
                            .get()
                            .url(Const.URL_EntrustModify)
                            .addParams("_id",index+"")
                            .addParams("sms",sms)
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
                                    if (!StringUtils.isEmpty(response.trim())&&"OK".equals(response.trim())){
                                        message.what=Const.STATE_Success;
                                        mHandler.sendMessage(message);
                                    }
                                }
                            });
                    et_modify.setText("");
                    dialog_modify.dismiss();
                }else {
                    UIUtils.makeText("请输入修改的内容");
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_modify.setText("");
                dialog_modify.dismiss();
            }
        });
        dialog_modify =builder.create();
        //取消预约
        AlertDialog.Builder builders=new AlertDialog.Builder(mactivity);
        builders.setTitle("您确定要取消课程预约？");
        builders.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                dialog_cancel.dismiss();
                return true;
            }
        });

        builders.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils
                        .get()
                        .url(Const.URL_EntrustDelete)
                        .addParams("_id",index+"")
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
                               if (!StringUtils.isEmpty(response)){
                                   message.what=Const.STATE_Success;
                                   mHandler.sendMessage(message);
                               }
                            }
                        });
            }
        });

        builders.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog_cancel.dismiss();
            }
        });
        dialog_cancel=builders.create();

    }

    private void initListener(){
        trl_rl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (STATE_CURRENT==Const.STATE_Refresh){
                    trl_rl.finishRefreshing();
                    return;
                }
                STATE_CURRENT=Const.STATE_Refresh;
                GetHttp();
            }
        });
    }

    private void GetHttp(){
        OkHttpUtils
                .get()
                .addParams("rid", UIUtils.getSpInt(Const.USER_ID)+"")
                .url(Const.URL_Entrust)
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
                        if (!StringUtils.isEmpty(response)){
                            message.obj=new Gson().fromJson(response,IssueData.class).data;
                        }
                        message.what=Const.STATE_Success;
                        mHandler.sendMessage(message);
                    }
                });
    }


    @Override
    public void OnClick(View view, int position) {
        //点击进入修改留言
        STATE_CURRENT=Const.STATE_MODIFY;
        index=mData.get(position)._id;
        dialog_modify.show();

    }

    @Override
    public void OnDetailClick(View view, int position) {
        //取消预约的操作
        STATE_CURRENT=Const.STATE_DELETE;
        index=mData.get(position)._id;
        dialog_cancel.show();
    }

}
