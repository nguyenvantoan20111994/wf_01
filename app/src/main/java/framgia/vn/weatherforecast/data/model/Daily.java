package framgia.vn.weatherforecast.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by toannguyen201194 on 20/06/2016.
 */
public class Daily {
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("data")
    private List<Data> mData;
    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        this.mData = data;
    }
}