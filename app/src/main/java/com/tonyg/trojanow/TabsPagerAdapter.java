package com.tonyg.trojanow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tonyG on 4/30/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new PostWallFragment();
            case 1:
                // Games fragment activity
                return new MessageFragment();
            case 2:
                // Movies fragment activity
                return new TrojansFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
