package framgia.vn.weatherforecast.data.service;

import android.util.Log;

import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by framgia on 04/07/2016.
 */
public class WeatherRepository {
    private static final String CITY_FIELD = "mCity";
    private Realm mRealm;

    public WeatherRepository(Realm realm) {
        mRealm = realm;
    }

    public RealmResults<WeatherForeCast> getAllWeatherForecast() {
        return mRealm.where(WeatherForeCast.class).findAll();
    }

    public WeatherForeCast getWeatherForecastBycity(String city) {
        return mRealm.where(WeatherForeCast.class).equalTo(CITY_FIELD, city).findFirst();
    }

    public boolean isExists(WeatherForeCast weatherForeCast) {
        return mRealm.where(WeatherForeCast.class).equalTo(CITY_FIELD, weatherForeCast.getCity())
            .count() != 0;
    }

    public void clearDatabase() {
        mRealm.beginTransaction();
        mRealm.delete(WeatherForeCast.class);
        mRealm.commitTransaction();
    }

    public void deleteWeatherForecastByCity(String city) {
        getWeatherForecastBycity(city).deleteFromRealm();
    }

    public void insertWeatherForecast(WeatherForeCast weatherForeCast) {
//        if (isExists(weatherForeCast)) {
//            updateWeatherForecast(weatherForeCast);
//        } else {
//            mRealm.beginTransaction();
//            mRealm.copyToRealm(weatherForeCast);
//            mRealm.commitTransaction();
//        }
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(weatherForeCast);
        mRealm.commitTransaction();
    }

    public void updateWeatherForecast(WeatherForeCast newWeatherForeCast) {
        WeatherForeCast oldWeatherForecast = mRealm.where(WeatherForeCast.class).equalTo(CITY_FIELD,
            newWeatherForeCast.getCity()).findFirst();
        mRealm.beginTransaction();
        oldWeatherForecast.deleteFromRealm();
        mRealm.copyToRealm(newWeatherForeCast);
        mRealm.commitTransaction();
    }
}
