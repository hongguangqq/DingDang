package com.zdlw.demo.dingdang.ui.fragment.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.BoardData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.adapter.BoardAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**消息评论界面
 * @author LinWei on 2017/8/23 15:47
 */
public class BoardFragment  extends DetailFragment {
    private TwinklingRefreshLayout trl_or;
    private RecyclerView rv_container;
    private EditText et_send;
    private TextView tv_send;
    private List<BoardData.Data> mData;
    private int mMessageID;
    private BoardAdapter mBoardAdapter;
    private final int STATE_NULL=0;
    private final int STATE_DATA=1;
    private final int STATE_SEND=2;
    private int STATE_CURRENT=STATE_DATA;//默认为刷新状态
    private int i;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_Success:
                    if (STATE_CURRENT==STATE_DATA){
                        //刷新和初次进入界面
                        List<BoardData.Data> moreData= (List<BoardData.Data>) msg.obj;
                        if (moreData==null){
                            mData.clear();
                            Log.e("ZDLW","NULL");
                        }else {
                            mData=moreData;
                            Log.e("ZDLW","NULL");
                        }
                        mBoardAdapter.notifyData(mData);
                        mBoardAdapter.notifyDataSetChanged();
                        trl_or.finishRefreshing();
                        rv_container.smoothScrollToPosition(mData.size());
                        STATE_CURRENT=STATE_NULL;//不能放在STATE_SEND中，因为走完逻辑STATE=NULL
                    }else if (STATE_CURRENT==STATE_SEND){
                        //发布成功，联网刷新界面
                        UIUtils.makeText("发送成功");
                        closeKeybord(et_send,mActivity);
                        initHttp();
                    }

                    break;
                case Const.STATE_Fail:
                    if (STATE_CURRENT==STATE_DATA){
                        UIUtils.makeText("网络异常,请刷新重试");
                        trl_or.finishRefreshing();
                    }else if (STATE_CURRENT==STATE_SEND){
                        closeKeybord(et_send,mActivity);
                        UIUtils.makeText("发送失败，请重试");
                    }
                    STATE_CURRENT=STATE_NULL;
                    break;
                default:
                    break;
            }
        }
    };

    public BoardFragment(Activity activity) {
        super(activity);
    }


    @Override
    public View initView() {
        Bundle bundle=BoardFragment.this.getArguments();
        mMessageID = bundle.getInt(Const.Extra_MessageID);
        View view=View.inflate(mActivity, R.layout.fragment_board,null);
        trl_or= (TwinklingRefreshLayout) view.findViewById(R.id.trl_board_reload);
        rv_container= (RecyclerView) view.findViewById(R.id.rv_board_container);
        et_send= (EditText) view.findViewById(R.id.et_board_send);
        tv_send= (TextView) view.findViewById(R.id.tv_board_send);
        mData=new ArrayList<>();
        mBoardAdapter = new BoardAdapter(mData, mActivity, new OnRecyclerItemClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }

            @Override
            public void OnDetailClick(View view, int position) {

            }
        });
        rv_container.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));
        rv_container.addItemDecoration(new SpacesItemDecoration(2,8));
        rv_container.setAdapter(mBoardAdapter);
        return view;
    }

    @Override
    public void initData() {
        setTitle("评论");
        setVisibleBack(false);
        //刷新操作
        trl_or.setEnableLoadmore(false);
        trl_or.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (STATE_CURRENT==STATE_DATA){
                    return;
                }
                STATE_CURRENT=STATE_DATA;
                initHttp();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
        //发送的点击事件处理
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info=et_send.getText().toString().trim();
                if (!TextUtils.isEmpty(info)){
                    STATE_CURRENT=STATE_SEND;
                    String time=UIUtils.currentTime("yyyy/MM/d HH:mm");
                    OkHttpUtils
                            .get()
                            .url(Const.URL_CommentInsert)
                            .addParams("_id",mMessageID+"")
                            .addParams("cid",UIUtils.getSpInt(Const.USER_ID)+"")
                            .addParams("commentator",UIUtils.getSpString(Const.USER_NAME))
                            .addParams("info",info)
                            .addParams("time",time)
                            .build()
                            .execute(new StringCallback() {
                                Message mMessage=new Message();
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    mMessage.what=Const.STATE_Fail;
                                    mHandler.sendMessage(mMessage);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    if ("1".equals(response.trim())){
                                        mMessage.what=Const.STATE_Success;
                                        mHandler.sendMessage(mMessage);
                                    }

                                }
                            });

                }else {
                    UIUtils.makeText("内容不可为空");
                }
            }
        });
        initHttp();
    }

    /**
     * 联网获取数据
     */
    private void initHttp(){
        STATE_CURRENT=STATE_DATA;
        OkHttpUtils
                .get()
                .url(Const.URL_CommentAll)
                .addParams("_id",mMessageID+"")
                .build()
                .execute(new StringCallback() {
                    Message mMessage=new Message();
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mMessage.what=Const.STATE_Fail;
                        mHandler.sendMessage(mMessage);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response.trim())){
                            mMessage.obj=new Gson().fromJson(response,BoardData.class).data;

                        }
                        mMessage.what=Const.STATE_Success;
                        mHandler.sendMessage(mMessage);

                    }
                });
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);


        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

}
