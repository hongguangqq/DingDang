package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.FocusData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.activity.OtherActivity;
import com.zdlw.demo.dingdang.ui.adapter.FocusAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/29.
 */
public class FocusFragment extends BaseFragment implements OnRecyclerItemClickListener {
    private RecyclerView rv_list;
    private FocusAdapter mAdapter;
    private Button btn_error;
    private FocusReceiver mReceiver;
    private List<FocusData.Data> mData;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    btn_error.setVisibility(View.GONE);
                    rv_list.setVisibility(View.VISIBLE);
                    mData= (List<FocusData.Data>) msg.obj;
                    mAdapter.notifyData(mData);
                    mAdapter.notifyDataSetChanged();
                    break;
                case Const.STATE_Fail:
                    UIUtils.makeText("联网失败，请点击重试");
                    btn_error.setVisibility(View.VISIBLE);
                    rv_list.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };


    public FocusFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View onCreatSuccessView() {
        View view= UIUtils.inflate(R.layout.fragment_focus);
        TextView tv_title= (TextView) view.findViewById(R.id.tv_title_title);
        tv_title.setText("我的关注");
        ImageView iv_back= (ImageView) view.findViewById(R.id.iv_title_left);
        iv_back.setVisibility(View.GONE);
        btn_error= (Button) view.findViewById(R.id.btn_focus_error);
        rv_list= (RecyclerView) view.findViewById(R.id.rv_focus_list);
        mData=new ArrayList<>();
        mReceiver=new FocusReceiver();
        mAdapter=new FocusAdapter(mData,mactivity,this);
        initSetting();
        GetHttp();
        return view;
    }

    @Override
    public LoadingPage.ResultState iniData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    private void initSetting(){
        rv_list.setLayoutManager(new LinearLayoutManager(mactivity,LinearLayoutManager.VERTICAL,false));
        rv_list.setAdapter(mAdapter);
        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetHttp();
            }
        });
        IntentFilter filter=new IntentFilter();
        filter.addAction(Const.ACTION3);
        getActivity().registerReceiver(mReceiver,filter);
    }




    private void GetHttp(){
        OkHttpUtils
                .get()
                .url(Const.URL_Focus)
                .addParams("_id",UIUtils.getSpInt(Const.USER_ID)+"")
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
                            try {
                                message.obj=new Gson().fromJson(response,FocusData.class).data;
                                message.what=Const.STATE_Success;
                                mHandler.sendMessage(message);
                            } catch (Exception e){
                                message.what=Const.STATE_Fail;
                                mHandler.sendMessage(message);
                            }
                        }

                    }
                });
    }

    @Override
    public void OnClick(View view, int position) {
        Intent intent =new Intent(getActivity(), OtherActivity.class);
        intent.putExtra("id",mData.get(position)._id);
        startActivity(intent);
    }

    @Override
    public void OnDetailClick(View view, int position) {

    }

    public class FocusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            GetHttp();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
