package com.zdlw.demo.dingdang.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zdlw.demo.dingdang.MainActivity;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.activity.LoginActivity;
import com.zdlw.demo.dingdang.ui.activity.PersonActivity;
import com.zdlw.demo.dingdang.ui.view.CircleImageView;
import com.zdlw.demo.dingdang.ui.view.ITView;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Administrator on 2017/5/18.
 */
public class MenuFragment extends MainFragment implements View.OnClickListener {

    private ITView it_news;//发布板
    private ITView it_chat;//聊天
    private ITView it_follow;//关注
    private ITView it_issue;//我的发布
    private ITView it_entrust;//接受的委托
    private TextView tv_name;
    private Button btn_logout;
    private CircleImageView civ_head;
    private UpdataPicReceiver mReceiver;

    @Override
    public View initui() {
        View view=UIUtils.inflate(R.layout.fragment_menu);
        civ_head= (CircleImageView) view.findViewById(R.id.civ_leftmenu_headpic);
        tv_name= (TextView) view.findViewById(R.id.tv_leftmenu_username);
        it_news = (ITView) view.findViewById(R.id.it_leftmenu_news);
        it_follow = (ITView) view.findViewById(R.id.it_leftmenu_follow);
        it_chat = (ITView) view.findViewById(R.id.it_leftmenu_chat);
        it_issue = (ITView) view.findViewById(R.id.it_leftmenu_issue);
        it_entrust = (ITView) view.findViewById(R.id.it_leftmenu_entrust);
        btn_logout = (Button) view.findViewById(R.id.btn_leftmenu_unregister);
        return view;
    }

    @Override
    public void initdata() {
        IntentFilter filter=new IntentFilter();
        filter.addAction(Const.ACTION2);
        mReceiver=new UpdataPicReceiver();
        getActivity().registerReceiver(mReceiver,filter);
        civ_head.setOnClickListener(this);
        it_news.setOnClickListener(this);
        it_follow.setOnClickListener(this);
        it_chat.setOnClickListener(this);
        it_issue.setOnClickListener(this);
        it_entrust.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        tv_name.setText(UIUtils.getSpString(Const.USER_NAME));
        Glide.get(getActivity()).clearMemory();
        Glide.with(mActivity)
                .load(Const.URL_Pic+UIUtils.getSpInt(Const.USER_ID)+".jpg")
                .error(R.drawable.tou)
                .into(civ_head);
    }

    /**
     * 切换主界面的Fragment
     * @param position
     */
    private void switchContentFragment(int position){
        MainActivity mainActivity= (MainActivity) mActivity;
        ContentFragment contentFragment=mainActivity.getContentMenuFragment();
        contentFragment.switchContentFragment(position);
    }

    private void toggleMenu(){
        MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu menu=mainActivity.getSlidingMenu();
        menu.toggle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_leftmenu_headpic:
                startActivity(new Intent(mActivity, PersonActivity.class));
                break;
            case R.id.it_leftmenu_news:
                switchContentFragment(0);
                toggleMenu();
                break;
            case R.id.it_leftmenu_chat:
                switchContentFragment(1);
                toggleMenu();
                break;
            case R.id.it_leftmenu_follow:
                switchContentFragment(2);
                toggleMenu();
                break;
            case R.id.it_leftmenu_issue:
                switchContentFragment(3);
                toggleMenu();
                break;
            case R.id.it_leftmenu_entrust:
                switchContentFragment(4);
                toggleMenu();
                break;
            case R.id.btn_leftmenu_unregister:
                UIUtils.makeText("已退出登录");
                UIUtils.setSpNumInt(Const.USER_ID,-1);//清除ID号保存
                UIUtils.setSpString(Const.USER_NAME, "");//清除用户名保存
                JMessageClient.logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));;
                FactoryFragment.Logout();
                getActivity().finish();
                break;
        }

    }

    class UpdataPicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.ACTION2.equals(intent.getAction())){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getActivity()).clearDiskCache();
                        SystemClock.sleep(1000);
                        UIUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                File file=new File(getActivity().getFilesDir(),"pic.jpg");
                                Glide.get(getActivity()).clearMemory();
                                Glide.with(mActivity)
                                        //                                .load(Const.URL_Pic+UIUtils.getSpInt(Const.USER_ID)+".jpg")
                                        .load(file)
                                        .error(R.drawable.tou)
                                        .into(civ_head);
                            }
                        });
                    }
                }).start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
