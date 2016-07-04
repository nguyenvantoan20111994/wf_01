package framgia.vn.weatherforecast.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.adapter.OnItemClickListener;
import framgia.vn.weatherforecast.adapter.PlacesAutoCompleteAdapter;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.service.GPSTrackerService;
import framgia.vn.weatherforecast.util.DialogUtils;

/**
 * Created by framgia on 23/06/2016.
 */
public class AddCityActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
        new LatLng(-0, 0), new LatLng(0, 0));
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_text_search_city)
    EditText mEditTextSearchCity;
    @Bind(R.id.button_clear_text_search)
    Button mButtonClearTextSearch;
    @Bind(R.id.recycler_view_cities)
    RecyclerView mRecyclerViewCities;
    @Bind(android.R.id.content)
    View mView;
    @Bind(R.id.linear_layout_use_current_location)
    LinearLayout mLinearCurrentLocation;
    private GPSTrackerService mGpsTrackerService;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private List<WeatherForeCast> mWeatherForeCasts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mButtonClearTextSearch.setVisibility(View.GONE);
        buildGoogleApiClient();
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.item_search_city,
            mGoogleApiClient, BOUNDS_INDIA, null);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewCities.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewCities.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.setOnItemClickListener(
            new OnItemClickListener() {
                @Override
                public void onCLick(int position) {
                    PlacesAutoCompleteAdapter.PlaceAutocomplete item =
                        mAutoCompleteAdapter.getItem(position);
                    String placeId = String.valueOf(item.placeId);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getCount() == 1) {
                                Place selectedPlace = places.get(0);
                                LatLng latLng = selectedPlace.getLatLng();
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConfigs.CITY_BUND,
                                    selectedPlace.getName().toString());
                                bundle.putDouble(AppConfigs.LATITUDE, latLng.latitude);
                                bundle.putDouble(AppConfigs.LONGITUDE, latLng.longitude);
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.putExtras(bundle);
                                startActivityForResult(intent,
                                    AppConfigs.REQUEST_LATLONG_SUCCESSFULLY);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                    R.string.error_something_went_wrong, Toast
                                        .LENGTH_SHORT).show();
                            }
                            mAutoCompleteAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    @OnTextChanged(R.id.edit_text_search_city)
    void searchCIty(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals("")) {
            mButtonClearTextSearch.setVisibility(View.VISIBLE);
            mLinearCurrentLocation.setVisibility(View.GONE);
            if (mGoogleApiClient.isConnected()) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_api_not_connected,
                    Toast.LENGTH_SHORT).show();
            }
        } else {
            mButtonClearTextSearch.setVisibility(View.GONE);
            mLinearCurrentLocation.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.button_clear_text_search)
    void clearTextSearch() {
        mEditTextSearchCity.setText("");
    }

    @OnClick(R.id.linear_layout_use_current_location)
    void useCurrentLocation() {
        requestPermissions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .addApi(Places.GEO_DATA_API)
            .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(AddCityActivity.this, R.string.prompt_connection_done, Toast.LENGTH_SHORT)
            .show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(AddCityActivity.this, R.string.prompt_connection_suspended,
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(AddCityActivity.this, R.string.prompt_connection_failed, Toast.LENGTH_SHORT)
            .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void requestPermissions() {
        if (DialogUtils.checkPermission(this)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
                DialogUtils.showSnackbar(this, mView, R.string.permissions, R.string.settings);
            } else {
                DialogUtils.PermissionRequest(this);
            }
        } else {
            DialogUtils.getlocation(this, mGpsTrackerService, mWeatherForeCasts,
                MainActivity.mViewpagerAdapter);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConfigs.MY_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DialogUtils.getlocation(this, mGpsTrackerService, mWeatherForeCasts,
                    MainActivity.mViewpagerAdapter);
            }
        }
    }
}
