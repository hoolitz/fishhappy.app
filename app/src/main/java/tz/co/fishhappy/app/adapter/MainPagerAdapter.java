package tz.co.fishhappy.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import tz.co.fishhappy.app.fragment.ListFavoriteFragment;
import tz.co.fishhappy.app.fragment.ListFishFragment;
import tz.co.fishhappy.app.fragment.ListOrderFragment;

/**
 * Created by Simon on 30-Apr-17.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ListFishFragment();
            case 1:
                return new ListOrderFragment();
            case 2:
                return new ListFavoriteFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
