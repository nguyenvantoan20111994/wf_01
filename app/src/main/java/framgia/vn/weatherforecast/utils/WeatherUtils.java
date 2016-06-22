package framgia.vn.weatherforecast.utils;

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
}
