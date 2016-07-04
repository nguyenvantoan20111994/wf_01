package framgia.vn.weatherforecast.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;

/**
 * Created by toannguyen201194 on 04/07/2016.
 */
public class GPSTrackerService extends Service implements LocationListener {
    private final Context mContext;
    protected LocationManager mLocationManager;
    boolean canGetLocation = false;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;

    public GPSTrackerService(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    @SuppressWarnings("MissingPermission")
    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GPSTrackerService.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @SuppressWarnings("MissingPermission")
    public Location getLocation() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = mLocationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled || isNetworkEnabled) {
            this.canGetLocation = true;
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    AppConfigs.MIN_TIME_BW_UPDATES,
                    AppConfigs.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (mLocationManager != null) {
                    mLocation = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
            }
            if (isGPSEnabled) {
                if (mLocation == null) {
                    mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        AppConfigs.MIN_TIME_BW_UPDATES,
                        AppConfigs.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                }
            }
        }
        return mLocation;
    }

    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(R.string.title_gps_setting);
        alertDialog.setMessage(R.string.gps_notification);
        alertDialog.setPositiveButton(R.string.gps_setting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.gps_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
