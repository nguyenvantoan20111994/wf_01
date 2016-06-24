package framgia.vn.weatherforecast.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.DayForecastAdapters;
import framgia.vn.weatherforecast.data.model.DayForecast;

/**
 * Created by toannguyen201194 on 23/06/2016.
 */
public class WeatherCityFragment extends Fragment {
    private static final String CITY_BUND = "city";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    @Bind(R.id.recycler_day_of_week)
    RecyclerView mRecyclerView;
    private String mCity;
    private float mLatitude;
    private float mLongitude;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<DayForecast> dayForecastList = new ArrayList<>();

    public static WeatherCityFragment newIntance(String city, float latitude, float longitude) {
        WeatherCityFragment weatherCityFragment = new WeatherCityFragment();
        Bundle args = new Bundle();
        args.putString(CITY_BUND, city);
        args.putFloat(LATITUDE, latitude);
        args.putFloat(LONGITUDE, longitude);
        return weatherCityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = getArguments().getString(CITY_BUND);
            mLatitude = getArguments().getFloat(LATITUDE);
            mLongitude = getArguments().getFloat(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DayForecastAdapters dayForecastAdapters =
            new DayForecastAdapters(dayForecastList, getActivity());
        mRecyclerView.setAdapter(dayForecastAdapters);
    }
}
