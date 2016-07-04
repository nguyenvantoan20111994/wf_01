package framgia.vn.weatherforecast.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.adapter.ViewpagerAdapter;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import framgia.vn.weatherforecast.service.GPSTrackerService;

/**
 * Created by toannguyen201194 on 05/07/2016.
 */
public class DialogUtils {
    public static void showSnackbar(final Activity activity, View view, int notification,
                                    int action) {
        Snackbar.make(view, notification,
            Snackbar.LENGTH_LONG)
            .setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConfigs.MY_PERMISSIONS_REQUEST);
                }
            }).show();
    }

    public static void PermissionRequest(Activity activity) {
        ActivityCompat.requestPermissions(activity,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            AppConfigs.MY_PERMISSIONS_REQUEST);
    }

    public static boolean checkPermission(Activity activity) {
        return (
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }

    public static void getlocation(Activity activity, GPSTrackerService gpsTrackerService,
                                   List<WeatherForeCast> weatherForeCastsList,
                                   ViewpagerAdapter viewpagerAdapter) {
        gpsTrackerService = new GPSTrackerService(activity);
        if (gpsTrackerService.canGetLocation()) {
            double latitude = gpsTrackerService.getLatitude();
            double longitude = gpsTrackerService.getLongitude();
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAdminArea();
                WeatherForeCast weatherForeCast =
                    new WeatherForeCast(cityName, latitude, longitude);
                weatherForeCastsList.add(weatherForeCast);
                viewpagerAdapter.notifyDataSetChanged();
                gpsTrackerService.stopUsingGPS();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            gpsTrackerService.showSettingsAlert();
        }
    }
}
