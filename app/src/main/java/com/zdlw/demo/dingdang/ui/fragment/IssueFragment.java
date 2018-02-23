package com.zdlw.demo.dingdang.ui.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.IssueData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.activity.SendActivity;
import com.zdlw.demo.dingdang.ui.adapter.IssueAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @author LinWei on 2017/8/24 18:24
 */
public class IssueFragment extends BaseFragment implements View.OnClickListener {
    private TwinklingRefreshLayout trl_or;
    private RecyclerView rv_contianer;
    private FloatingActionButton ftn_setting;
    private Button btn_delete;
    private ImageView iv_enter;
    private boolean isOpen;
    private List<IssueData.Data> mData;
    private IssueAdapter mIssueAdapter;
    private int STATE_CURRENT=Const.STATE_First;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    //刷新操作，非第一次进入界面
                    if (STATE_CURRENT==Const.STATE_Refresh){
                        trl_or.finishRefreshing();
                        if (msg.obj==null){
                            mData.clear();
                        }else {
                            mData= (List<IssueData.Data>) msg.obj;
                        }
                        mIssueAdapter.notifyData(mData);
                        mIssueAdapter.notifyDataSetChanged();
                        STATE_CURRENT=Const.STATE_Undo;
                    }else if (STATE_CURRENT==Const.STATE_First){
                        mData= (List<IssueData.Data>) msg.obj;
                        mIssueAdapter.notifyData(mData);
                        mIssueAdapter.notifyDataSetChanged();
                        STATE_CURRENT=Const.STATE_Undo;
                    }else if (STATE_CURRENT==Const.STATE_DELETE){
                        UIUtils.makeText("删除成功");
                        STATE_CURRENT=Const.STATE_Refresh;
                        Const.isModify=true;
                        initHttp();
                    }

                    break;
                case Const.STATE_Fail:
                    if (STATE_CURRENT==Const.STATE_DELETE){
                        UIUtils.makeText("删除失败，请重试");
                    }else {
                        UIUtils.makeText("数据加载失败，请刷新重试");
                    }
                    STATE_CURRENT=Const.STATE_Undo;
                    break;
                default:
                    break;
            }
        }
    };



    public IssueFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View onCreatSuccessView() {
        View root=View.inflate(mactivity, R.layout.fragment_issue,null);
        trl_or= (TwinklingRefreshLayout) root.findViewById(R.id.trl_issue_reload);
        rv_contianer= (RecyclerView) root.findViewById(R.id.rv_issue_container);
        ftn_setting= (FloatingActionButton) root.findViewById(R.id.ftn_issue_setting);
        btn_delete= (Button) root.findViewById(R.id.btn_issue_delete);
        iv_enter= (ImageView) root.findViewById(R.id.iv_issue_enter);
        mData=new ArrayList<>();
        initListener();
        initHttp();
        initSetting();
        return root;

    }

    @Override
    public LoadingPage.ResultState iniData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    private void initSetting(){
        trl_or.setEnableLoadmore(false);
        trl_or.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (STATE_CURRENT==Const.STATE_Refresh){
                    return;
                }
                STATE_CURRENT=Const.STATE_Refresh;
                initHttp();
            }
        });
        mIssueAdapter=new IssueAdapter(mData, mactivity, new OnRecyclerItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                if (Const.isShowCheck){
                    CheckBox checkBox= (CheckBox) view.findViewById(R.id.cb_issue_delete);
                    if (checkBox.isChecked()){
                        checkBox.setChecked(false);
                        mData.get(position).isCheck=false;
                    }else {
                        checkBox.setChecked(true);
                        mData.get(position).isCheck=true;
                    }
                    mIssueAdapter.notifyData(mData);
                }
            }

            @Override
            public void OnDetailClick(View view, int position) {

            }
        });
        rv_contianer.addItemDecoration(new SpacesItemDecoration(10,20));
        rv_contianer.setLayoutManager(new LinearLayoutManager(mactivity,LinearLayoutManager.VERTICAL,false));
        rv_contianer.setAdapter(mIssueAdapter);
    }

    private void initListener(){
        ftn_setting.setOnClickListener(this);
        iv_enter.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }


    private void initHttp(){
        OkHttpUtils
                .get()
                .addParams("aid", UIUtils.getSpInt(Const.USER_ID)+"")
                .url(Const.URL_Issue)
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
                       if (!TextUtils.isEmpty(response.trim())){
                           message.obj=new Gson().fromJson(response,IssueData.class).data;
                       }
                        message.what=Const.STATE_Success;
                        mHandler.sendMessage(message);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ftn_issue_setting:
                if (isOpen){
                    closeMenu(v);
                    isOpen=false;
                }else {
                    openMenu(v);
                    isOpen=true;
                }
                if (!Const.isShowCheck){
                    Const.isShowCheck=true;//显示CheckBox
                    btn_delete.setVisibility(View.VISIBLE);
                }else {
                    Const.isShowCheck=false;//隐藏CheckBox
                    btn_delete.setVisibility(View.GONE);
                    for(IssueData.Data data:mData){
                        data.isCheck=false;
                    }
                }
                mIssueAdapter.notifyData(mData);
                mIssueAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_issue_enter:
                startActivity(new Intent(mactivity, SendActivity.class));
                mactivity.overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                break;
            case R.id.btn_issue_delete:
                StringBuilder count= new StringBuilder();
                for(IssueData.Data d:mData){
                    if (d.isCheck){
                        count.append(d._id+"&");
                    }
                }
                if (count.length()<=0){
                    UIUtils.makeText("请勾选需要删除的条目");
                    return;
                }
                DeleteData(count.toString());
                break;

        }
    }

    private void DeleteData(String count){
        STATE_CURRENT=Const.STATE_DELETE;
        OkHttpUtils
                .get()
                .url(Const.URL_IssueDelete)
                .addParams("_id",count)
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
                        message.what=Const.STATE_Success;
                        mHandler.sendMessage(message);
                    }
                });
    }

    /*---------------------------------------按钮的显示与隐藏---------------------------------------*/

    //开启ftn按钮
    private void openMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        animator.setDuration(500);
        animator.start();
    }
    //关闭ftn按钮
    private void closeMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Const.isPlilsh){
            //发布了一条新信息，刷新界面
            initHttp();
            Const.isPlilsh=false;
        }
    }
}
