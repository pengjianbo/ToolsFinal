package cn.finalteam.toolsfinal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/22 下午6:14
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<String> mTabList;
    private List<Fragment> mFragmentList;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        this(fm, list, null);
    }

    public FragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> tabList) {
        super(fm);
        this.mFragmentList = list;
        this.mTabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabText = mTabList.get(position);
        return tabText;
    }
}
