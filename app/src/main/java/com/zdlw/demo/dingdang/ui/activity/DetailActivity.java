package com.zdlw.demo.dingdang.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.data.PageData;
import com.zdlw.demo.dingdang.domin.Const;
import com.zdlw.demo.dingdang.ui.adapter.DetailAdapter;
import com.zdlw.demo.dingdang.ui.fragment.page.BoardFragment;
import com.zdlw.demo.dingdang.ui.fragment.page.DetailFragment;
import com.zdlw.demo.dingdang.ui.fragment.page.EntryFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ViewPager mVp_page;
    private List<DetailFragment> flist;
    private PageData.Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        data = (PageData.Data) getIntent().getSerializableExtra(Const.Extra_Detail);
        mVp_page= (ViewPager) findViewById(R.id.vp_detail_page);
        flist=new ArrayList<>();
        initFragment();
        DetailAdapter pageAdapter=new DetailAdapter(getSupportFragmentManager(),flist);
        mVp_page.setOffscreenPageLimit(1);
        mVp_page.setAdapter(pageAdapter);

    }


    private void initFragment(){
        //消息
        DetailFragment entryFragment=new EntryFragment(this);
        Bundle entrybundle=new Bundle();
        entrybundle.putSerializable(Const.Extra_Detail,data);
        entryFragment.setArguments(entrybundle);
        flist.add(entryFragment);
        //评论
        DetailFragment boardFragment=new BoardFragment(this);
        Bundle boardBundle=new Bundle();
        boardBundle.putInt(Const.Extra_MessageID, data._id);
        boardFragment.setArguments(boardBundle);
        flist.add(boardFragment);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
    }
}
