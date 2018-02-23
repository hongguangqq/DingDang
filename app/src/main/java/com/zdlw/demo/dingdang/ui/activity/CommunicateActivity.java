package com.zdlw.demo.dingdang.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.CommuniData;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.http.BeanCallback;
import com.zdlw.demo.dingdang.ui.adapter.CommucaotrAdapter;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zdlw.demo.dingdang.utils.PinYinUtil;
import com.zdlw.demo.dingdang.utils.StringUtils;
import com.zdlw.demo.dingdang.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class CommunicateActivity extends AppCompatActivity {

    private UserInfo mMyinfo;
    private String fromUser;
    private String jiguangName;
    private String pic;
    private List<CommuniData> mList;
    private RecyclerView mRecyclerView;
    private EditText et_send;
    private Button btn_send;
    private CommucaotrAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicat);
        fromUser = getIntent().getStringExtra("fromUser");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_communicator);
        btn_send = (Button) findViewById(R.id.btn_communicator_send);
        et_send = (EditText) findViewById(R.id.et_communicator_send);
        jiguangName = PinYinUtil.getPinYin(fromUser).toLowerCase();
        Const.ComuName =jiguangName;
        mList = new ArrayList<>();
        mAdapter = new CommucaotrAdapter(mList,this, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0,20));
        mRecyclerView.setAdapter(mAdapter);
        JMessageClient.registerEventReceiver(this);
        Log.e("@#","fromuser="+fromUser);
        OkHttpUtils
                .get()
                .url(Const.URL_SearchUser)
                .addParams("name",fromUser)
                .build()
                .execute(new BeanCallback<PersonData>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PersonData response, int id) {
                        Log.e("@#","result="+response);
                        if (response.data!=null){
                            pic=response.data.get(0).pic;
                        }
                        notifyData();
                    }
                });



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = et_send.getText().toString().trim();
                if (StringUtils.isEmpty(text)){
                    UIUtils.makeText("请输入发送内容");
                    return;
                }

                Message m =JMessageClient.createSingleTextMessage(jiguangName,"17fbb0d95bd81d09fdb5883a",text);
                m.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i==0){
                            Log.e("@#","success");
                            mList.add(new CommuniData(true,Const.URL_Pic+Const.getUserID()+".jpg",text));
                            mAdapter.notifyData(mList);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                        }else {
                            Log.e("@#","fail");
                        }
                    }
                });
                JMessageClient.sendMessage(m);

            }
        });


    }

    private void notifyData(){
        Log.e("@#",jiguangName);
        Conversation conversation=JMessageClient.getSingleConversation(jiguangName);
        if (conversation.getAllMessage()==null){
            return;
        }
        List<Message> list=conversation.getAllMessage();
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
//            Log.e("@#",list.get(i).getContent().toJson());
            CommuniData bean =null;
            String text =null;

            try {
                text =new JSONObject(list.get(i).getContent().toJson().toString()).getString("text");
                Log.e("@#",list.get(i).getFromUser().getUserName()+": "+text);
                if (Const.getUserName().equals(list.get(i).getFromUser().getUserName())){
                    bean = new CommuniData(true,Const.URL_Pic+Const.getUserID()+".jpg",text);
//                    Log.e("@#","Const.getUserName(): "+bean.getText());
                }else {
                    Log.e("@#",Const.URL_Pic+pic);
                    bean = new CommuniData(false,Const.URL_Pic+pic,text);
//                    Log.e("@#",fromUser+": "+bean.getText());
                }
                mList.add(bean);
                mAdapter.notifyData(mList);
                mAdapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                // 处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
//                fromUser = msg.getFromUser().getUserName();
//                Log.e("@#","fromuser="+fromUser+"---text="+textContent.getText());
                notifyData();
                break;
        }
    }

    public void onEvent(NotificationClickEvent event) {
//        Intent notificationIntent = new Intent(this, CommunicateActivity.class);
//        notificationIntent.putExtra("fromUser",fromUser);
//        this.startActivity(notificationIntent);// 自定义跳转到指定页面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Const.ComuName="";
    }


}
