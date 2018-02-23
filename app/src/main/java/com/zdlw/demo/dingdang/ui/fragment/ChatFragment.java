package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PersonData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.http.BeanCallback;
import com.zdlw.demo.dingdang.ui.activity.OtherActivity;
import com.zdlw.demo.dingdang.ui.adapter.UserAdapter;
import com.zdlw.demo.dingdang.ui.faces.OnRecyclerItemClickListener;
import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.ui.view.SpacesItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/29.
 */
public class ChatFragment extends BaseFragment {

    private EditText mEt_input;
    private RecyclerView mRv_result;
    private UserAdapter mUserAdapter;
    private List<PersonData.Data> mData;

    public ChatFragment(Activity activity) {
        super(activity);
        mData = new ArrayList<>();

    }

    @Override
    public View onCreatSuccessView() {
        View view =View.inflate(mactivity, R.layout.fragment_chat,null);
        mEt_input = (EditText) view.findViewById(R.id.input_text);
        mRv_result = (RecyclerView) view.findViewById(R.id.rv_result);
        mUserAdapter = new UserAdapter(mData, getActivity(), new OnRecyclerItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Log.e("@#","ID="+mData.get(position)._id);
                Intent intent = new Intent(getActivity(), OtherActivity.class);
                intent.putExtra("id",Integer.valueOf(mData.get(position)._id));
                startActivity(intent);// 自定义跳转到指定页
            }

            @Override
            public void OnDetailClick(View view, int position) {

            }
        });
        mRv_result.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRv_result.setAdapter(mUserAdapter);
        mRv_result.addItemDecoration(new SpacesItemDecoration(0,5));
        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    OkHttpUtils
                            .get()
                            .url(Const.URL_SearchUser)
                            .addParams("name",s.toString())
                            .build()
                            .execute(new BeanCallback<PersonData>() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(PersonData response, int id) {
                                    if (response!=null && response.data!=null &&response.data.size()>0){
                                        mData=response.data;
                                    }else {
                                        mData.clear();
                                    }
                                    mUserAdapter.notifyData(mData);
                                    mUserAdapter.notifyDataSetChanged();
                                }
                            });
                }else {
                    mData.clear();
                    mUserAdapter.notifyData(mData);
                    mUserAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    @Override
    public LoadingPage.ResultState iniData() {

        return LoadingPage.ResultState.STATE_SUCCESS;
    }
}
