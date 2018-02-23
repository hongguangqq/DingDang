package com.zdlw.demo.dingdang.ui.fragment;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.zdlw.demo.dingdang.MainActivity;
import com.zdlw.demo.dingdang.R;
import com.zdlw.demo.dingdang.ui.fragment.page.PageFragment;
import com.zdlw.demo.dingdang.ui.view.LoadingPage;
import com.zdlw.demo.dingdang.ui.view.MyViewPage;
import com.zdlw.demo.dingdang.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/29.
 */
public class NewsFragment extends BaseFragment {

//    private PageIndicator pt_indicator;
    private TabLayout tb_indicator;
    private MyViewPage vp_page;
    private String[] pageName;
    private ArrayList<PageFragment> pageFragments;
    private MainActivity mMainActivity;

    public NewsFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View onCreatSuccessView() {

        mMainActivity = (MainActivity) mactivity;
        View view= View.inflate(mactivity,R.layout.fragment_news,null);
        tb_indicator= (TabLayout) view.findViewById(R.id.tb_news_indicator);
        vp_page = (MyViewPage) view.findViewById(R.id.vp_news_page);
        pageFragments=new ArrayList<>();
        pageName=UIUtils.getStringArray(R.array.tab_name);
        vp_page.setOffscreenPageLimit(3);//设置缓存页数
        initpage();
        return view;
    }



    /**
     * 初始化三个学级页面
     */
    private void initpage(){
        PageFragment pageFragmentX=new PageFragment(mactivity,1);
        PageFragment pageFragmentC=new PageFragment(mactivity,2);
        PageFragment pageFragmentG=new PageFragment(mactivity,3);
        pageFragments.add(pageFragmentX);
        pageFragments.add(pageFragmentC);
        pageFragments.add(pageFragmentG);
        tb_indicator.setTabMode(TabLayout.MODE_FIXED);
        tb_indicator.addTab(tb_indicator.newTab().setText(pageName[0]));
        tb_indicator.addTab(tb_indicator.newTab().setText(pageName[1]));
        tb_indicator.addTab(tb_indicator.newTab().setText(pageName[2]));
        NewsAdapter adapter=new NewsAdapter(mMainActivity.getSupportFragmentManager());
        vp_page.setAdapter(adapter);
        tb_indicator.setupWithViewPager(vp_page);
        tb_indicator.setTabsFromPagerAdapter(adapter);
    }


    @Override
    public LoadingPage.ResultState iniData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    class NewsAdapter extends FragmentPagerAdapter{


        public NewsAdapter(FragmentManager fm) {
            super(fm);
            pageName=UIUtils.getStringArray(R.array.tab_name);
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
