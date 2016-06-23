package framgia.vn.weatherforecast.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toannguyen201194 on 23/05/2016.
 */
public class ViewpagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFrgList = new ArrayList<>();

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFrgList.get(position);
    }

    @Override
    public int getCount() {
        return mFrgList.size();
    }

    public void addFragment(Fragment fragment) {
        mFrgList.add(fragment);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mFrgList.remove(position);
        notifyDataSetChanged();
    }
}
