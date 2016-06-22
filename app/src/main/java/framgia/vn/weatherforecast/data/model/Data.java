package framgia.vn.weatherforecast.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by toannguyen201194 on 24/06/2016.
 */
public class Data extends RealmObject{
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("temperatureMin")
    private double mTemperatureMin;
    @SerializedName("temperatureMax")
    private double mTemperatureMax;

    public String getIcon() {
        return mIcon.replace("-","_");
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

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

    public double getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.mTemperatureMin = temperatureMin;
    }

    public double getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.mTemperatureMax = temperatureMax;
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }
}
