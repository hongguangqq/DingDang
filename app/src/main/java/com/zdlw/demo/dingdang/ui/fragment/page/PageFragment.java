package com.zdlw.demo.dingdang.ui.fragment.page;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PageData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.activity.DetailActivity;
import com.zdlw.demo.dingdang.ui.adapter.PageAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.MyProgressDialog;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/5/29.
 */
public class PageFragment extends Fragment {
    private int age;//学龄段
    private Activity mActivity;
    private TwinklingRefreshLayout trl_reload;
    private RecyclerView rv_container;
    private View rootview;
    private MyProgressDialog mLoadingDialog;
    private List<PageData.Data> mData;
    private int STATE_Current=Const.STATE_First;
    private PageAdapter mPageAdapter;
    private int position;
    private PageReceiver mReceiver;

    public PageFragment(Activity activity ,int age){
        mActivity=activity;
        this.age=age;
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    List<PageData.Data> moredata= (List<PageData.Data>) msg.obj;
                    if (STATE_Current==Const.STATE_Refresh){
                        //上拉刷新
                        trl_reload.finishRefreshing();
                        mData=moredata;
                        mPageAdapter.notifyData(mData);
                        UIUtils.makeText("刷新成功");
                    }else if (STATE_Current==Const.STATE_LoadMore){
                        //下拉加载更多
                        mData.addAll(moredata);
                        trl_reload.finishLoadmore();
                        mPageAdapter.LoadMoreData(mData);
                        UIUtils.makeText("加载成功");
                    }else {
                        //初次进入界面
                        mLoadingDialog.dismiss();
                        mData=moredata;
                        mPageAdapter.notifyData(mData);
                    }
                    STATE_Current=Const.STATE_Undo;
                    mPageAdapter.notifyDataSetChanged();
                    break;
                case Const.STATE_Fail:
                    //第一次访问数据可能会失败，此时也需要关闭进度条
                    if (mLoadingDialog.isShowing()){
                        mLoadingDialog.dismiss();
                    }
                    STATE_Current=Const.STATE_Undo;
                    UIUtils.makeText("操作失败，请重试");
                    break;
                case Const.STATE_Null:
                    STATE_Current=Const.STATE_Undo;
                    UIUtils.makeText("没有更多数据了");
                    trl_reload.finishLoadmore();
                default:
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootview ==null){
            rootview= UIUtils.inflate(R.layout.fragment_page_x);
            trl_reload= (TwinklingRefreshLayout) rootview.findViewById(R.id.trl_page_reload);
            rv_container= (RecyclerView) rootview.findViewById(R.id.rv_page_container);
            mLoadingDialog = new MyProgressDialog(getActivity(),0);
            mReceiver = new PageReceiver();
            mData=new ArrayList<>();
            mPageAdapter = new PageAdapter(mData, getActivity(), new OnRecyclerItemClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    //跳转到详细界面
                    PageFragment.this.position=position;//记录position，方便点击后数据的改变
                    Intent intent=new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(Const.Extra_Detail,mData.get(position));
                    startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }

                @Override
                public void OnDetailClick(View view, int position) {

                }
            });
            rv_container.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            rv_container.addItemDecoration(new SpacesItemDecoration(10,10));
            rv_container.setAdapter(mPageAdapter);

        }
        ViewGroup parent= (ViewGroup) rootview.getParent();
        if (parent!=null){
            parent.removeView(rootview);
        }
        return rootview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initdata();
    }


    private void initdata(){
        //刷新和加载的处理
        trl_reload.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (STATE_Current==Const.STATE_Refresh){
                    //不重复触发刷新
                    return;
                }
                STATE_Current=Const.STATE_Refresh;
                getData(0);

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (STATE_Current==Const.STATE_LoadMore){
                    //不重复触发加载
                    return;
                }
                STATE_Current=Const.STATE_LoadMore;
                getData(mData.get(mData.size()-1)._id);
            }
        });
        getData(0);
        //注册广播

        IntentFilter filter=new IntentFilter();
        filter.addAction(Const.ACTION1);
        mActivity.registerReceiver(mReceiver,filter);

    }

    private void getData(int index){
        OkHttpUtils
                .get()
                .url(Const.URL_Message)
                .addParams("id",index+"")
                .addParams("age",age+"")
                .build()
                .execute(new StringCallback() {
                    Message message=new Message();
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        if (STATE_Current==Const.STATE_First){
                            //小学界面&初次进入时触发Dialog
                            mLoadingDialog.show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UIUtils.setLoge("Error："+e.getMessage());
                        message.what=Const.STATE_Fail;
                        mHandler.sendMessageDelayed(message,500);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response.trim())){
                            //返回为空，没有更多数据了
                            message.what=Const.STATE_Null;
                        }else {
                            PageData gson=new Gson().fromJson(response,PageData.class);
                            message.what=Const.STATE_Success;
                            message.obj=gson.data;
                        }
                        mHandler.sendMessageDelayed(message,500);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Const.IsChangeState){
            getData(0);
            rv_container.scrollToPosition(position);
            Const.IsChangeState=false;
        }

    }

    class PageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.ACTION1.equals(intent.getAction())){
                getData(0);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mReceiver);
    }
}
