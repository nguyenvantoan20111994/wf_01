package framgia.vn.weatherforecast.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toannguyen201194 on 20/06/2016.
 */
public class WeatherForeCast {
    @SerializedName("latitude")
    private float mLatitude;
    @SerializedName("longitude")
    private float mLongitude;
    @SerializedName("timezone")
    private String mTimeZone;
    @SerializedName("currently")
    private Currently mCurrently;
    @SerializedName("daily")
    private Daily mDaily;

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        this.mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = longitude;
    }

    public Daily getDaily() {
        return mDaily;
    }

    public void setDaily(Daily daily) {
        this.mDaily = daily;
    }

    public Currently getCurrently() {
        return mCurrently;
    }

    public void setCurrently(Currently currently) {
        this.mCurrently = currently;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setmTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
    }
}
