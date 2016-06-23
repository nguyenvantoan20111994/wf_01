package framgia.vn.weatherforecast.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toannguyen201194 on 20/06/2016.
 */
public class DayForecast {
    private long mTime;
    private String mSummary;
    private double mTemperatureMin;
    private double mTemperatureMax;
    private String mIcon;

    public DayForecast(long mTime, double mTemperatureMin, String mSummary, double mTemperatureMax,
                       String mIcon) {
        this.mTime = mTime;
        this.mTemperatureMin = mTemperatureMin;
        this.mSummary = mSummary;
        this.mTemperatureMax = mTemperatureMax;
        this.mIcon = mIcon;
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

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }
}
