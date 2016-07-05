package framgia.vn.weatherforecast.ui.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.DayForecastAdapters;
import framgia.vn.weatherforecast.data.model.Data;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.data.service.WeatherRepository;
import framgia.vn.weatherforecast.service.BuilderService;
import framgia.vn.weatherforecast.ui.activities.MainActivity;
import framgia.vn.weatherforecast.util.CheckConnectionUtil;
import framgia.vn.weatherforecast.util.ResourcesUtils;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by toannguyen201194 on 23/06/2016.
 */
public class WeatherCityFragment extends Fragment implements Callback<WeatherForeCast> {
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
    @Bind(R.id.image_icon_large)
    ImageView mImIconLarge;
    @Bind(R.id.swiperefeshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.text_hours)
    TextView mTvhours;
    @Bind(R.id.text_update_time)
    TextView mTvUpdateTime;
    DayForecastAdapters mDayForecastAdapters;
    private String mCity;
    private double mLatitude;
    private double mLongitude;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Data> mDailyList = new ArrayList<>();
    private Realm mRealm;
    private WeatherForeCast mWeatherForeCast;
    private WeatherRepository mWeatherRepository;

    public WeatherCityFragment(WeatherForeCast mWeatherForeCast) {
        this.mWeatherForeCast = mWeatherForeCast;
        mCity = mWeatherForeCast.getCity();
        mLatitude = mWeatherForeCast.getLatitude();
        mLongitude = mWeatherForeCast.getLongitude();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        mWeatherRepository = new WeatherRepository(mRealm);
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
        mDayForecastAdapters =
            new DayForecastAdapters(mDailyList, getActivity());
        mRecyclerView.setAdapter(mDayForecastAdapters);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataWeather();
            }
        });
    }

    private void loadDataWeather() {
        isRefesh(true);
        if (CheckConnectionUtil.isInternetOn(getContext())) {
            String location = String.format("%s,%s", mLatitude, mLongitude);
            BuilderService.WeatherService service = BuilderService.getClient();
            Call<WeatherForeCast> call =
                service.getForecast(AppConfigs.API_KEY, location);
            call.enqueue(this);
        } else {
            getDataView(mWeatherForeCast);
            isRefesh(false);
        }
    }

    @Override
    public void onResponse(Call<WeatherForeCast> call, Response<WeatherForeCast> response) {
        if (response.isSuccessful()) {
            getDataView(response.body());
            isRefesh(false);
        } else {
            isRefesh(false);
            Toast.makeText(getContext(), getString(R.string.not_Found_Loction), Toast.LENGTH_SHORT)
                .show();
        }
    }

    @Override
    public void onFailure(Call<WeatherForeCast> call, Throwable t) {
        isRefesh(false);
        Toast.makeText(getContext(), getString(R.string.data_request), Toast.LENGTH_SHORT)
            .show();
    }

    public void isRefesh(final boolean showRefesh) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(showRefesh);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDataWeather();
    }

    public void getDataView(WeatherForeCast weatherForeCast) {
        mWeatherForeCast = weatherForeCast;
        mRealm.beginTransaction();
        mWeatherForeCast.setCity(mCity);
        mRealm.commitTransaction();
        mWeatherRepository.insertWeatherForecast(mWeatherForeCast);
        ((MainActivity) getActivity()).changeTitle(mWeatherForeCast.getCity());
        mTvhours.setText(mWeatherForeCast.getFormattedTime());
        mTvTemperature.setText(
            String.valueOf(mWeatherForeCast.getCurrently().getTemperature()) + (char) 0x00B0);
        mImIconLarge.setImageResource(ResourcesUtils.getResources(getContext(), String.valueOf
            (mWeatherForeCast.getCurrently().getIcon()), AppConfigs.DRAWABLE));
        mTvShortSumary.setText(String.valueOf(mWeatherForeCast.getCurrently().getSummary()));
        mTvHumidity.setText(String.valueOf(mWeatherForeCast.getCurrently().getHumidity()));
        mTvDewPoint.setText(String.valueOf(mWeatherForeCast.getCurrently().getDewPoint()));
        mTvWindSpeed.setText(String.valueOf(mWeatherForeCast.getCurrently().getWindSpeed()));
        mDailyList.clear();
        for (int i = 1; i < mWeatherForeCast.getDaily().getData().size(); i++) {
            mDailyList.add(mWeatherForeCast.getDaily().getData().get(i));
        }
        mTvLongSummary.setText(String.valueOf(mWeatherForeCast.getDaily().getSummary()));
        mTvUpdateTime.setText(mWeatherForeCast.getCurrently().getTimeUpdate());
        mDayForecastAdapters.notifyDataSetChanged();
        if (mWeatherForeCast.getCurrently().getIcon().equals("clear_night")) {
            ((MainActivity) getActivity())
                .changeBackgroudDrawer(R.drawable.backgroud_clear_night);
        } else if (mWeatherForeCast.getCurrently().getIcon().equals("rain")) {
            ((MainActivity) getActivity()).changeBackgroudDrawer(R.drawable.backgroud_rain);
        } else {
            ((MainActivity) getActivity())
                .changeBackgroudDrawer(R.drawable.background_clear_day);
        }
    }
}
