package framgia.vn.weatherforecast.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.ui.fragment.WeatherCityFragment;

/**
 * Created by toannguyen201194 on 23/05/2016.
 */
public class ViewpagerAdapter extends FragmentPagerAdapter {
    private final List<WeatherForeCast> mDatas;

    public ViewpagerAdapter(FragmentManager fm, List<WeatherForeCast> datas) {
        super(fm);
        mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return new WeatherCityFragment(mDatas.get(position));
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return  POSITION_NONE;
    }

}
