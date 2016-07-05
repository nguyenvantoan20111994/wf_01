package framgia.vn.weatherforecast.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.ViewpagerAdapter;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.data.service.WeatherRepository;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.linear_layout_settings)
    LinearLayout mLinearLayoutSettings;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.text_city)
    TextView mTvCity;
    private ActionBarDrawerToggle mToggle;
    private LocationManager mLocationManager;
    private static ViewpagerAdapter mViewpagerAdapter;
    private Realm mRealm;
    private List<WeatherForeCast> mWeatherForeCasts;
    private WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        checkLocationServiceEnabled();
        mRealm = Realm.getDefaultInstance();
        mWeatherRepository = new WeatherRepository(mRealm);
        mWeatherForeCasts = new ArrayList<>();
        mToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        mViewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), mWeatherForeCasts);
        mViewPager.setAdapter(mViewpagerAdapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            WeatherForeCast weatherForeCast = new WeatherForeCast(
                bundle.getString(AppConfigs.CITY_BUND),
                bundle.getDouble(AppConfigs.LATITUDE),
                bundle.getDouble(AppConfigs.LONGITUDE));
            mWeatherForeCasts.add(weatherForeCast);
            mViewpagerAdapter.notifyDataSetChanged();
        }
    }

    private void loadData() {
        RealmResults<WeatherForeCast> data = mWeatherRepository.getAllWeatherForecast();
        if (data.size() == 0) {
            WeatherForeCast weatherForeCast = new WeatherForeCast(AppConfigs.DEFAULT_CITY_NAME,
                AppConfigs.DEFAULT_LATITUDE, AppConfigs.DEFAULT_LONGITUDE);
            mWeatherForeCasts.add(weatherForeCast);
            mViewpagerAdapter.notifyDataSetChanged();
        } else {
            mWeatherForeCasts.addAll(data);
            mViewpagerAdapter.notifyDataSetChanged();
        }
    }

    public void changeTitle(String city) {
        mTvCity.setText(city);
    }

    private void checkLocationServiceEnabled() {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !networkEnabled) {
            final Snackbar snackbar = Snackbar
                .make(this.findViewById(android.R.id.content), R.string.location_services_disabled,
                    Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.setAction(R.string.action_enable_current_location, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(myIntent);
                }
            });
            snackbar.show();
        }
    }

    public void changeBackgroudDrawer(int uri) {
        mDrawerLayout.setBackgroundResource(uri);
    }

    @OnClick(R.id.linear_layout_settings)
    void openSettingsScreen() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_city) {
            Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationServiceEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationServiceEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}