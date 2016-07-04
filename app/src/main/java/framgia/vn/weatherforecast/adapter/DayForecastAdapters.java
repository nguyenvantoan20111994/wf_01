package framgia.vn.weatherforecast.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import framgia.vn.weatherforecast.AppConfigs;
import framgia.vn.weatherforecast.R;
import framgia.vn.weatherforecast.data.model.Data;
import framgia.vn.weatherforecast.util.ResourcesUtils;
import framgia.vn.weatherforecast.util.WeatherUtils;

/**
 * Created by toannguyen201194 on 22/06/2016.
 */
public class DayForecastAdapters
    extends RecyclerView.Adapter<DayForecastAdapters.DayOfWeekHolders> {
    List<Data> mDailies;
    Context mContext;
    LayoutInflater mLayoutInflater;
    private SharedPreferences mSettings;

    public DayForecastAdapters(List<Data> dailies, Context context) {
        mDailies = dailies;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mSettings = mContext.getSharedPreferences(AppConfigs.WEATHER_SETTINGS, Context
            .MODE_PRIVATE);
    }

    @Override
    public DayOfWeekHolders onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_day_of_week_weather, viewGroup, false);
        return new DayOfWeekHolders(view);
    }

    @Override
    public void onBindViewHolder(DayOfWeekHolders holder, int position) {
        Data daily = mDailies.get(position);
        holder.text_day.setText(daily.getDayOfTheWeek());
        holder.image_icon.setImageResource(ResourcesUtils.getResources(mContext, daily.getIcon(),
            AppConfigs.DRAWABLE));
        holder.text_temperatureminmax.setText(WeatherUtils.getDailyTemperatue(mSettings,daily
            .getTemperatureMin(),daily.getTemperatureMax()));
    }

    @Override
    public int getItemCount() {
        return mDailies == null ? 0 : mDailies.size();
    }

    public class DayOfWeekHolders extends RecyclerView.ViewHolder {
        @Bind(R.id.text_day)
        TextView text_day;
        @Bind(R.id.image_icon)
        ImageView image_icon;
        @Bind(R.id.text_temperatureminmax)
        TextView text_temperatureminmax;

        public DayOfWeekHolders(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
