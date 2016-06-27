package framgia.vn.weatherforecast.adapter;
/**
 * Created by framgia on 23/06/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import framgia.vn.weatherforecast.R;

public class PlacesAutoCompleteAdapter
    extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {
    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;
    private Context mContext;
    private int mLayout;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter filter) {
        mContext = context;
        mLayout = resource;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
        mLayoutInflater =
            (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
            }
        };
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (!mGoogleApiClient.isConnected()) {
            return null;
        } else {
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi
                .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                    mBounds, mPlaceFilter);
            AutocompletePredictionBuffer autocompletePredictions = results
                .await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(mContext, R.string.erroe_contacting_api + status.toString(),
                    Toast.LENGTH_SHORT).show();
                autocompletePredictions.release();
                return null;
            }
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                    prediction.getFullText(null)));
            }
            autocompletePredictions.release();
            return resultList;
        }
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View convertView = mLayoutInflater.inflate(mLayout, viewGroup, false);
        return new PredictionHolder(convertView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(PredictionHolder predictionHolder, int i) {
        predictionHolder.mTextViewPredictedCity.setText(mResultList.get(i).description);
        predictionHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return mResultList == null ? 0 : mResultList.size();
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    public class PredictionHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_view_predicted_city)
        TextView mTextViewPredictedCity;
        @Bind(R.id.linear_layout_item_city)
        LinearLayout mLinearLayoutItemCity;
        private OnItemClickListener mOnItemClickListener;
        private int mPosition;

        public PredictionHolder(View itemView,
                                OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onCLick(mPosition);
                    }
                }
            });
        }

        public void setPosition(int position) {
            mPosition = position;
        }
    }

    public class PlaceAutocomplete {
        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}