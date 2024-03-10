package tz.co.fishhappy.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
