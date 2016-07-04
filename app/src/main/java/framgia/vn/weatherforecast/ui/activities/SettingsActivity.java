package framgia.vn.weatherforecast.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;

/**
 * Created by framgia on 22/06/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    public static final String WEATHER_SETTINGS = "settings";
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
        mSettings = getSharedPreferences(AppConfigs.WEATHER_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        Integer selectedTemperatureItem =
            mSettings.getInt(AppConfigs.TEMPERATURE, AppConfigs.DEFAULT_VALUE);
        if (selectedTemperatureItem.equals(AppConfigs.DEFAULT_VALUE)) {
            mEditor.putInt(AppConfigs.TEMPERATURE, AppConfigs.TEMPERATURE_UNIT_FAHRENHEIT);
            mTextViewTemperatureUnit
                .setText(getResources().getStringArray(
                    R.array.temperature_unit)[AppConfigs.TEMPERATURE_UNIT_FAHRENHEIT]);
        } else {
            mTextViewTemperatureUnit.setText(getResources().getStringArray(R.array
                .temperature_unit)[selectedTemperatureItem]);
        }
        Integer selectedWindSpeedItem =
            mSettings.getInt(AppConfigs.WIND_SPEED, AppConfigs.DEFAULT_VALUE);
        if (selectedWindSpeedItem.equals(AppConfigs.DEFAULT_VALUE)) {
            mEditor.putInt(AppConfigs.WIND_SPEED, AppConfigs.WIND_SPEED_UNIT_MPH);
            mTextViewWindSpeedUnit
                .setText(getResources()
                    .getStringArray(R.array.wind_speed_unit)[AppConfigs.WIND_SPEED_UNIT_MPH]);
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
        Integer selectedItem = mSettings.getInt(AppConfigs.TEMPERATURE,
            AppConfigs.DEFAULT_VALUE);
        builder.setTitle(AppConfigs.TEMPERATURE)
            .setSingleChoiceItems(arrayTemperatureUnit, selectedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putInt(AppConfigs.TEMPERATURE, which);
                        mEditor.apply();
                        mTextViewTemperatureUnit.setText(arrayTemperatureUnit[which]);
                        dialog.dismiss();
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
            mSettings.getInt(AppConfigs.WIND_SPEED, AppConfigs.DEFAULT_VALUE);
        builder.setTitle(AppConfigs.WIND_SPEED)
            .setSingleChoiceItems(arrayWindSpeedUnit, selectedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putInt(AppConfigs.WIND_SPEED, which);
                        mEditor.apply();
                        mTextViewWindSpeedUnit.setText(arrayWindSpeedUnit[which]);
                        dialog.dismiss();
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
