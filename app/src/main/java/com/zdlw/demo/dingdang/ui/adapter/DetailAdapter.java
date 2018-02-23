package com.zdlw.demo.dingdang.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zdlw.demo.dingdang.ui.fragment.page.DetailFragment;

import java.util.List;

/**
 * @author LinWei on 2017/8/23 16:04
 */
public class DetailAdapter extends FragmentPagerAdapter {
    private List<DetailFragment> data;
    public DetailAdapter(FragmentManager fm,List<DetailFragment> data) {
        super(fm);
        this.data=data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
