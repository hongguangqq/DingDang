package com.zdlw.demo.dingdang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.utils.ThreadManager;
import com.zdlw.demo.dingdang.utils.UIUtils;


/**
 * 根据状态不同显示不同的四个页面的自定义控件
 * 未加载
 * 加载中
 * 加载失败
 * 数据为空
 * 加载成功
 * Created by Administrator on 2016/11/26.
 */
public abstract class LoadingPage extends FrameLayout {
    private static int STATE_LOAD_UNDO=1;//未加载
    private static int STATE_LOAD_LOADING=2;//加载中
    private static int STATE_LOAD_ERROR=3;//加载失败
    private static int STATE_LOAD_EMRTY=4;//数据为空
    private static int STATE_LOAD_SUCCESS=5;//加载成功

    private  int mCURRENT=STATE_LOAD_UNDO;//当前状态

    private View mLoadingPage;//加载中的布局
    private View mErrorPage;//加载失败的布局
    private View mEmityPage;//加载为空的布局
    private View mSuccrssPage;//加载成功的布局

    public LoadingPage(Context context) {
        super(context);
        initview();
    }
    //由调用者调用
    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview();
    }

    private void initview(){
        //初始化加载中的布局
        if (mLoadingPage==null){
            mLoadingPage= UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }

        //初始化加载失败的布局
        if (mErrorPage==null){
            mErrorPage=UIUtils.inflate(R.layout.page_error);
            Button btn_error= (Button) mErrorPage.findViewById(R.id.btn_pager_error_b1);
            btn_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();//该方法本身就是加载页面,可在此调用。
                }
            });
            addView(mErrorPage);
        }

        //初始化加载为空的布局
        if (mEmityPage==null){
            mEmityPage=UIUtils.inflate(R.layout.page_empty);
            addView(mEmityPage);
        }

        showRightPage();
    };

    //根据当前状态去刷新界面
    private void showRightPage(){
        //什么都没做或正在加载中
        mLoadingPage.setVisibility((mCURRENT==STATE_LOAD_UNDO||mCURRENT==STATE_LOAD_LOADING )? VISIBLE:GONE);
        mErrorPage.setVisibility(mCURRENT == STATE_LOAD_ERROR ? VISIBLE : GONE);
        mEmityPage.setVisibility(mCURRENT==STATE_LOAD_EMRTY ? VISIBLE:GONE);

        if (mSuccrssPage==null&&mCURRENT==STATE_LOAD_SUCCESS){
            mSuccrssPage=onCreatSuccessView();
            if (mSuccrssPage!=null){
                addView(mSuccrssPage);
            }
        }

        if (mSuccrssPage!=null){
            mSuccrssPage.setVisibility(mCURRENT==STATE_LOAD_SUCCESS ? VISIBLE:GONE);
        }

    };

    //开始加载数据
    public void loadData(){
       if (mCURRENT!=STATE_LOAD_LOADING){//不是在读取状态才能执行
           mCURRENT=STATE_LOAD_LOADING;//进入读取状态
//           new Thread() {
//               @Override
//               public void run() {
//                    final ResultState resultState=initData();//回传数据获取的状态
//                   //运行在主线程
//                    UIUtils.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (resultState!=null){
//                                mCURRENT=resultState.getState();
//                                Log.d("Loading","开始加载界面");
//                                //加载结束后，更新网络状态，根据新的状态去刷新界面。
//                                showRightPage();
//                            }
//                        }
//                    });
//               }
//           }.start();

           ThreadManager.getThreadPool().execute(new Runnable() {
               @Override
               public void run() {
                   final ResultState resultState=initData();//回传数据获取的状态
                   //运行在主线程
                   UIUtils.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if (resultState!=null){
                               mCURRENT=resultState.getState();
                               Log.d("Loading","开始加载界面");
                               //加载结束后，更新网络状态，根据新的状态去刷新界面。
                               showRightPage();
                           }
                       }
                   });
               }
           });
       }
    }
    //加载成功的界面的逻辑
    public abstract View onCreatSuccessView();
    //加载网络数据，需要返回值，告知状态
    public abstract ResultState initData();

    public enum ResultState{
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_EMRTY(STATE_LOAD_EMRTY),
        STATE_ERROR(STATE_LOAD_ERROR);
        private int state;
        private ResultState(int state){
            this.state=state;
        };
        public int getState(){
            return state;
        }
    }
}
