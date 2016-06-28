package framgia.vn.weatherforecast.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.DayForecastAdapters;
import framgia.vn.weatherforecast.data.model.Data;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.service.BuilderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by toannguyen201194 on 23/06/2016.
 */
public class WeatherCityFragment extends Fragment implements Callback<WeatherForeCast> {
    private static final String CITY_BUND = "city";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    @Bind(R.id.recycler_day_of_week)
    RecyclerView mRecyclerView;
    @Bind(R.id.text_temperature)
    TextView mTvTemperature;
    @Bind(R.id.text_short_summary)
    TextView mTvShortSumary;
    @Bind(R.id.text_left)
    TextView mTvHumidity;
    @Bind(R.id.text_center)
    TextView mTvDewPoint;
    @Bind(R.id.text_right)
    TextView mTvWindSpeed;
    @Bind(R.id.text_long_summary)
    TextView mTvLongSummary;
    DayForecastAdapters mDayForecastAdapters;
    private String mCity;
    private double mLatitude;
    private double mLongitude;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Data> dailyList = new ArrayList<>();

    public static WeatherCityFragment newIntance(String city, double latitude, double longitude) {
        WeatherCityFragment weatherCityFragment = new WeatherCityFragment();
        Bundle args = new Bundle();
        args.putString(CITY_BUND, city);
        args.putDouble(LATITUDE, latitude);
        args.putDouble(LONGITUDE, longitude);
        weatherCityFragment.setArguments(args);
        return weatherCityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = getArguments().getString(CITY_BUND);
            mLatitude = getArguments().getDouble(LATITUDE);
            mLongitude = getArguments().getDouble(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        initViews();
        loadDataWeather();
        return view;
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDayForecastAdapters =
            new DayForecastAdapters(dailyList, getActivity());
        mRecyclerView.setAdapter(mDayForecastAdapters);
    }

    private void loadDataWeather() {
        String location = String.format(mLatitude + "," + mLongitude);
        BuilderService.WeatherService service = BuilderService.getClient();
        Call<WeatherForeCast> call =
            service.getForecast(AppConfigs.API_KEY, location);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<WeatherForeCast> call, Response<WeatherForeCast> response) {
        if (response.isSuccessful()) {
            for (int i = 0; i < response.body().getDaily().getData().size(); i++) {
                dailyList.add(response.body().getDaily().getData().get(i));
            }
            mTvTemperature.setText(String.valueOf(response.body().getCurrently().getTemperature()));
            mTvShortSumary.setText(String.valueOf(response.body().getCurrently().getSummary()));
            mTvHumidity.setText(String.valueOf(response.body().getCurrently().getHumidity()));
            mTvDewPoint.setText(String.valueOf(response.body().getCurrently().getDewPoint()));
            mTvWindSpeed.setText(String.valueOf(response.body().getCurrently().getWindSpeed()));
            mTvLongSummary.setText(String.valueOf(response.body().getDaily().getSummary()));
            mDayForecastAdapters.notifyDataSetChanged();
        } else {
        }
    }

    @Override
    public void onFailure(Call<WeatherForeCast> call, Throwable t) {
    }
}