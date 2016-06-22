package framgia.vn.weatherforecast;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by toannguyen201194 on 29/06/2016.
 */
public class WeatherApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
            .deleteRealmIfMigrationNeeded()
            .build();
        Realm.setDefaultConfiguration(configuration);
    }

}
