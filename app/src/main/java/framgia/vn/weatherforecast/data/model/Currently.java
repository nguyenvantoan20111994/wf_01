package framgia.vn.weatherforecast.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

import framgia.vn.weatherforecast.AppConfigs;

/**
 * Created by toannguyen201194 on 24/06/2016.
 */
public class Currently {
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("temperature")
    private double mTemperature;
    @SerializedName("humidity")
    private float mHumidity;
    @SerializedName("windSpeed")
    private float mWindSpeed;
    @SerializedName("dewPoint")
    private float mDewPoint;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public String getIcon() {
        return mIcon.replace("-", "_");
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        this.mHumidity = humidity;
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.mWindSpeed = windSpeed;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        this.mTemperature = temperature;
    }

    public float getDewPoint() {
        return mDewPoint;
    }

    public void setdewPoint(float dewPoint) {
        this.mDewPoint = dewPoint;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        Date dateTime = new Date(getTime() * 1000);
        return formatter.format(dateTime);
    }

    public String getTimeUpdate() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss");
        long timeCurrentupdate = (System.currentTimeMillis() - mTime * 1000);
        Date date = new Date(timeCurrentupdate);
        return AppConfigs.UPDATE + formatter.format(date);
    }
}