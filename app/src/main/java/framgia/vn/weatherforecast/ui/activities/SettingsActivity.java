package framgia.vn.weatherforecast.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import framgia.vn.weatherforecast.R;

/**
 * Created by framgia on 22/06/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.relative_layout_current_location)
    RelativeLayout mRelativeLayoutCurrentLocation;
    @Bind(R.id.switch_current_location)
    Switch mSwitchCurrentLocation;
    @Bind(R.id.linear_layout_temperature_unit)
    LinearLayout mLinearLayoutTemperatureUnit;
    @Bind(R.id.text_view_temperature_unit)
    TextView mTextViewTemperatureUnit;
    @Bind(R.id.linear_layout_wind_speed_unit)
    LinearLayout mLinearLayoutWindSpeedUnit;
    @Bind(R.id.text_view_wind_speed_unit)
    TextView mTextViewWindSpeedUnit;
    @Bind(R.id.linear_layout_license)
    LinearLayout mLinearLayoutLicense;
    public static final String WEATHER_SETTINGS = "settings";
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private Integer mDefaultValue = -1;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mToolbar.setTitle(R.string.settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mSettings = getSharedPreferences(WEATHER_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        Integer selectedTemperatureItem = mSettings.getInt(getResources().getString(R.string
            .temperature), mDefaultValue);
        if (selectedTemperatureItem == mDefaultValue) {
            mEditor.putInt(getResources().getString(R.string.temperature), 0);
            mTextViewTemperatureUnit
                .setText(getResources().getStringArray(R.array.temperature_unit)[0]);
        } else {
            mTextViewTemperatureUnit.setText(getResources().getStringArray(R.array
                .temperature_unit)[selectedTemperatureItem]);
        }
        Integer selectedWindSpeedItem = mSettings.getInt(getResources().getString(R.string
            .wind_speed), mDefaultValue);
        if (selectedWindSpeedItem == mDefaultValue) {
            mEditor.putInt(getResources().getString(R.string.wind_speed), 0);
            mTextViewWindSpeedUnit
                .setText(getResources().getStringArray(R.array.wind_speed_unit)[0]);
        } else {
            mTextViewWindSpeedUnit.setText(getResources().getStringArray(R.array.wind_speed_unit)
                [selectedWindSpeedItem]);
        }
        mEditor.apply();
    }

    @OnClick(R.id.relative_layout_current_location)
    void enableCurrentLocation() {
        if (mSwitchCurrentLocation.isChecked()) {
            mSwitchCurrentLocation.setChecked(false);
        } else {
            mSwitchCurrentLocation.setChecked(true);
        }
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        SettingsActivity.this.startActivity(myIntent);
    }

    @OnClick(R.id.linear_layout_temperature_unit)
    void selectTemperatureUnits() {
        final String[] arrayTemperatureUnit =
            getResources().getStringArray(R.array.temperature_unit);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        Integer selectedItem = mSettings.getInt(getResources().getString(R.string.temperature),
            mDefaultValue);
        builder.setTitle(R.string.temperature)
            .setSingleChoiceItems(arrayTemperatureUnit, selectedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putInt(getResources().getString(R.string.temperature), which);
                        mEditor.apply();
                        mTextViewTemperatureUnit.setText(arrayTemperatureUnit[which]);
                        dialog.dismiss();
                        // TODO: 24/06/2016 change temperature unit
                    }
                })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        builder.create().show();
    }

    @OnClick(R.id.linear_layout_wind_speed_unit)
    void selectWindSpeedUnits() {
        final String[] arrayWindSpeedUnit = getResources().getStringArray(R.array.wind_speed_unit);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        Integer selectedItem =
            mSettings.getInt(getResources().getString(R.string.wind_speed), mDefaultValue);
        builder.setTitle(R.string.wind_speed)
            .setSingleChoiceItems(arrayWindSpeedUnit, selectedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putInt(getResources().getString(R.string.wind_speed), which);
                        mEditor.apply();
                        mTextViewWindSpeedUnit.setText(arrayWindSpeedUnit[which]);
                        dialog.dismiss();
                        // TODO: 24/06/2016 change wind speed unit
                    }
                })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
            );
        builder.create().show();
    }

    @OnClick(R.id.linear_layout_license)
    void viewLicense() {
        Toast.makeText(SettingsActivity.this, R.string.copyright, Toast.LENGTH_SHORT).show();
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
}
