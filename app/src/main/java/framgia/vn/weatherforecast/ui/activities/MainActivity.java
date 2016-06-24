package framgia.vn.weatherforecast.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.ViewpagerAdapter;
import framgia.vn.weatherforecast.ui.fragment.WeatherCityFragment;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.linear_layout_settings)
    LinearLayout mLinearLayoutSettings;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    private ActionBarDrawerToggle mToggle;
    private static ViewpagerAdapter mViewpagerAdapter;
    private double lat = 15.735394;
    private double lon = 96.7372145;
    private String city = "America/Los_Angeles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        setupViewPager(mViewPager, city, lat, lon);
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

    public void setupViewPager(ViewPager viewPage, String city, double latitude, double longitude) {
        mViewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.addFragment(WeatherCityFragment.newIntance(city, latitude, longitude));
        viewPage.setAdapter(mViewpagerAdapter);
    }

    // TODO: 23/06/2016  use for remove fragment
    public void removeFragment() {
        int position = mViewPager.getCurrentItem();
        mViewpagerAdapter.remove(position);
    }

    // TODO: 23/06/2016 add fragment when click button toolbar
    public static void  addFragment(String city, double latitude, double longitude) {
        mViewpagerAdapter.addFragment(WeatherCityFragment.newIntance(city, latitude, longitude));
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
}
