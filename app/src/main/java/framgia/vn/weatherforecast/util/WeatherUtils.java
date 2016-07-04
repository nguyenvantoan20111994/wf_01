package framgia.vn.weatherforecast.util;

import android.app.Activity;
import android.content.SharedPreferences;

import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.ui.activities.MainActivity;

/**
 * Created by framgia on 01/07/2016.
 */
public class WeatherUtils {
    public static Double toCelsius(Double fahrenheit) {
        return 5 * (fahrenheit - 32) / 9;
    }

    private static Double toFahrenheit(Double celsius) {
        return 32 + (celsius * 9 / 5);
    }

    public static Double mphToMs(Double mph) {
        return mph * 0.44704;
    }

    public static Double mphToKmh(Double mph) {
        return mph * 1.60934;
    }

    public static String getDailyTemperatue(SharedPreferences settings, double temperatureMin,
                                            double
                                                temperatureMax) {
        String dailytem;
        switch (settings.getInt(AppConfigs.TEMPERATURE, AppConfigs.DEFAULT_VALUE)) {
            case AppConfigs.TEMPERATURE_UNIT_CELSIUS:
                dailytem = String.format("%.0f%c / %.0f%c", WeatherUtils.toCelsius(temperatureMin),
                    (char) 0x00B0,
                    WeatherUtils.toCelsius(temperatureMax),
                    (char) 0x00B0);
                break;
            default:
                dailytem = String.format("%.0f%c / %.0f%c", temperatureMin,
                    (char) 0x00B0,
                    temperatureMax,
                    (char) 0x00B0);
        }
        return dailytem;
    }

    public static String getCurrentTemperatue(SharedPreferences settings, double temperature) {
        String currentTem;
        switch (settings.getInt(AppConfigs.TEMPERATURE, AppConfigs.DEFAULT_VALUE)) {
            case AppConfigs.TEMPERATURE_UNIT_CELSIUS:
                currentTem = String.format("%.0f%c", WeatherUtils.toCelsius(temperature), (char)
                    0x00B0);
                break;
            default:
                currentTem = String.format("%.0f%c", temperature, (char) 0x00B0);
        }
        return currentTem;
    }

    public static String getWinSpeed(SharedPreferences settings, double windSpeed) {
        String currentwindSpeed;
        switch (settings.getInt(AppConfigs.WIND_SPEED, AppConfigs.DEFAULT_VALUE)) {
            case AppConfigs.WIND_SPEED_UNIT_MS:
                currentwindSpeed = String.format("%.2f %s", WeatherUtils.mphToMs(windSpeed),
                    AppConfigs.UNIT_WIND_SPEED_MS);
                break;
            case AppConfigs.WIND_SPEED_UNIT_KMH:
                currentwindSpeed = String.format("%.2f %s", WeatherUtils.mphToKmh
                    (windSpeed), AppConfigs.UNIT_WIND_SPEED_KMH);
                break;
            default:
                currentwindSpeed = String.format("%.2f %s",windSpeed,AppConfigs.UNIT_WIND_SPEED_MPH);
        }
        return currentwindSpeed;
    }

    public static void setBackgroudActivity(String icon, Activity activity) {
        switch (icon) {
            case AppConfigs.CLEAR_NIGHT:
            case AppConfigs.PARTLY_CLOUDY_NIGHT:
                ((MainActivity) activity)
                    .changeBackgroudDrawer(R.drawable.backgroud_clear_night);
                break;
            case AppConfigs.RAIN:
                ((MainActivity) activity)
                    .changeBackgroudDrawer(R.drawable.backgroud_rain);
                break;
            default:
                ((MainActivity) activity)
                    .changeBackgroudDrawer(R.drawable.background_clear_day);
                break;
        }
    }
}
