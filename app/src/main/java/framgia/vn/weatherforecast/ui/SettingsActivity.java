package framgia.vn.weatherforecast.ui;

import android.content.DialogInterface;
import android.os.Bundle;
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
    }

    @OnClick(R.id.relative_layout_current_location)
    void enableCurrentLocation() {
        if (mSwitchCurrentLocation.isChecked()) {
            mSwitchCurrentLocation.setChecked(false);
            // TODO: 22/06/2016, turn location on/off, save to database
        } else {
            mSwitchCurrentLocation.setChecked(true);
        }
    }

    @OnClick(R.id.linear_layout_temperature_unit)
    void selectTemperatureUnits() {
        final String[] arrayTemperatureUnit = getResources().getStringArray(R.array.temperature_unit);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(R.string.temperature)
                .setSingleChoiceItems(arrayTemperatureUnit, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextViewTemperatureUnit.setText(arrayTemperatureUnit[which]);
                        dialog.dismiss();
                        // TODO: 22/06/2016, save to database
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
        builder.setTitle(R.string.wind_speed)
                .setSingleChoiceItems(arrayWindSpeedUnit, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextViewWindSpeedUnit.setText(arrayWindSpeedUnit[which]);
                        dialog.dismiss();
                        // TODO: 22/06/2016, save to database
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
        // TODO: 23/06/2016, show license
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
