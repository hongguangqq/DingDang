package com.zdlw.demo.dingdang;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.http.BeanCallback;
import com.zdlw.demo.dingdang.ui.activity.CommunicateActivity;
import com.zdlw.demo.dingdang.ui.fragment.ContentFragment;
import com.zdlw.demo.dingdang.ui.fragment.MenuFragment;
import com.zdlw.demo.dingdang.utils.PinYinUtil;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class MainActivity extends SlidingFragmentActivity {

    private UserInfo mMyinfo;
    private boolean isRun=true;
    private String mUserName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initui();
        initfragment();
    }

    private void init(){
        mMyinfo = JMessageClient.getMyInfo();

        Log.e("@#","username="+Const.getUserName());
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(Const.getUserName());
            if (m.find()) {
                mUserName = PinYinUtil.getPinYin(Const.getUserName()).toLowerCase();
            }else {
                mUserName = Const.getUserName();
            }
        //极光登录
        if (mMyinfo==null){
            JMessageClient.login(mUserName , "123456", new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.e("@#","code="+i);
                    mMyinfo = JMessageClient.getMyInfo();
                    Log.e("@#",mMyinfo.getUserName());
                    mMyinfo.setNickname(Const.getUserName());
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, mMyinfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {

                        }
                    });
                }
            });
        }
        JMessageClient.registerEventReceiver(this);
        OkHttpUtils
                .get()
                .url(Const.URL_GetPersonData)
                .addParams("_id",Const.getUserID()+"")
                .build()
                .execute(new BeanCallback<PersonData>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PersonData response, int id) {
                        if (response!=null){
                            UIUtils.setSpString(Const.USER_PHONE,response.data.get(0).phone);
                            UIUtils.setSpString(Const.USER_INTRODUCTION,response.data.get(0).introduction);
                        }
                    }
                });
    }

    private void initui() {
        setBehindContentView(R.layout.layout_leftmenu);//导入侧边栏布局
        SlidingMenu menu=getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);//设置Menu菜单左边左滑
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置滑动的模式
        menu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影的宽度
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.sliding);//设置菜单的宽度
        menu.setFadeDegree(0.35f);//设置画出时的渐入渐出效果


    }

    private void initfragment() {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, new ContentFragment(), Const.FRAGMENT_CONTENT);
        ft.replace(R.id.fl_leftmenu, new MenuFragment(), Const.FRAGMENT_LEFTMENU);
        ft.commit();
    }

    /**
     * 获取侧边栏对象
     * @return
     */
    public MenuFragment getLeftMenuFragment(){
        FragmentManager fm=getSupportFragmentManager();
        MenuFragment fragment= (MenuFragment) fm.findFragmentByTag(Const.FRAGMENT_LEFTMENU);
        return fragment;
    }

    /**
     * 获取主界面对象
     * @return
     */
    public ContentFragment getContentMenuFragment(){
        FragmentManager fm=getSupportFragmentManager();
        ContentFragment fragment= (ContentFragment) fm.findFragmentByTag(Const.FRAGMENT_CONTENT);
        return fragment;
    }

    private String fromUser;
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                // 处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                fromUser=msg.getFromUser().getNickname();
//                Log.e("@#",fromUser+"");
//                Log.e("@#", textContent.getText());
                break;
        }
    }
    public void onEvent(NotificationClickEvent event) {
        if (!Const.ComuName.equals(fromUser)){
            Intent notificationIntent = new Intent(this, CommunicateActivity.class);
            notificationIntent.putExtra("fromUser",fromUser);
            this.startActivity(notificationIntent);// 自定义跳转到指定页面
        }

    }


    private long back_time;
    //双击返回的逻辑处理
    @Override
    public void onBackPressed() {
        long progress= System.currentTimeMillis();
        if (progress-back_time>2000){
            Toast.makeText(UIUtils.getContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
            back_time=progress;
        }else {
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }
}
