package framgia.vn.weatherforecast.service;

import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.data.model.WeatherForeCast;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by toannguyen201194 on 24/06/2016.
 */
public class BuilderService {
    private static Retrofit mRetrofit;
    private static WeatherService mWeatherService;

    public static WeatherService getClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConfigs.BASE_URL).build();
            mWeatherService=mRetrofit.create(WeatherService.class);
        }
        return mWeatherService;
    }
    public interface WeatherService {
        @GET("{apiKey}/{location}")
        Call<WeatherForeCast> getForecast(@Path(value = "apiKey", encoded = true) String apiKey,
                                          @Path(value = "location", encoded = true) String location);
    }
}
