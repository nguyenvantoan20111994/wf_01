package framgia.vn.weatherforecast.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

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
import framgia.vn.weatherforecast.service.GPSTrackerService;
import framgia.vn.weatherforecast.util.CheckConnectionUtil;
import framgia.vn.weatherforecast.util.DialogUtils;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public static ViewpagerAdapter mViewpagerAdapter;
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
    @Bind(android.R.id.content)
    View mView;
    private GPSTrackerService mGpsTrackerService;
    private ActionBarDrawerToggle mToggle;
    private LocationManager mLocationManager;
    private Realm mRealm;
    private List<WeatherForeCast> mWeatherForeCasts = new ArrayList<>();
    private WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        requestPermissions();
        loadData();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mRealm = Realm.getDefaultInstance();
        mWeatherRepository = new WeatherRepository(mRealm);
        mToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        mViewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), mWeatherForeCasts);
        mViewPager.setOffscreenPageLimit(10);
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
            if (CheckConnectionUtil.isInternetOn(this)) {
                WeatherForeCast weatherForeCast = new WeatherForeCast(AppConfigs.DEFAULT_CITY_NAME,
                    AppConfigs.DEFAULT_LATITUDE, AppConfigs.DEFAULT_LONGITUDE);
                mWeatherForeCasts.add(weatherForeCast);
                mViewpagerAdapter.notifyDataSetChanged();
            } else {
                mDrawerLayout.setBackgroundResource(R.drawable.background_clear_day);
                Toast.makeText(MainActivity.this, getString(R.string.not_connect_network),
                    Toast.LENGTH_SHORT)
                    .show();
            }
        } else {
            mWeatherForeCasts.addAll(data);
            mViewpagerAdapter.notifyDataSetChanged();
        }
    }

    public void changeTitle(String city) {
        mTvCity.setText(city);
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
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestPermissions() {
        if (DialogUtils.checkPermission(this)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
                DialogUtils.showSnackbar(this, mView, R.string.permissions, R.string.allow);
            } else {
                DialogUtils.PermissionRequest(this);
            }
        } else {
            DialogUtils.getlocation(this, mGpsTrackerService, mWeatherForeCasts, mViewpagerAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConfigs.MY_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DialogUtils
                    .getlocation(this, mGpsTrackerService, mWeatherForeCasts, mViewpagerAdapter);
            }
        }
    }
}
