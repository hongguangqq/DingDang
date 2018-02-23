package com.zdlw.demo.dingdang.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.PageIndicator;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.ui.fragment.page.PageFragment;
import com.zdlw.demo.dingdang.ui.view.MyViewPage;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.ArrayList;

public class TestActivity extends FragmentActivity {
    private PageIndicator pt_indicator;
    private MyViewPage vp_page;
    private ArrayList<PageFragment> pageFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        pt_indicator = (PageIndicator) findViewById(R.id.pt_test_indicator);
        vp_page = (MyViewPage) findViewById(R.id.vp_test_page);
        pageFragments=new ArrayList<>();
        pageFragments.add(new PageFragment(this,1));
        pageFragments.add(new PageFragment(this,2));
        pageFragments.add(new PageFragment(this,3));
        vp_page.setAdapter(new NewsAdapter(getSupportFragmentManager()));
        pt_indicator.setViewPager(vp_page);

    }

    class NewsAdapter extends FragmentPagerAdapter {
        private String[] pageName;

        public NewsAdapter(FragmentManager fm) {
            super(fm);
            pageName= UIUtils.getStringArray(R.array.tab_name);
        }

        @Override
        public Fragment getItem(int position) {

            return pageFragments.get(position);
        }

        @Override
        public int getCount() {
            return pageName.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageName[position];
        }
    }
}
