package com.example.android.sunshine;

import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class WatchSyncService extends WearableListenerService {
    // These constants are mirrored from WearUtils to avoid module dependencies
    private static final String WEATHER_UPDATE_PATH = "/weather";
    private static final String MIN_TEMP_NAME = "min";
    private static final String MAX_TEMP_NAME = "max";
    private static final String CONDITION_NAME = "condition";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().equals(WEATHER_UPDATE_PATH)) {
                    SunshineWatchFace.sMinTemp
                            = DataMapItem.fromDataItem(item).getDataMap().getFloat(MIN_TEMP_NAME);
                    SunshineWatchFace.sMaxTemp
                            = DataMapItem.fromDataItem(item).getDataMap().getFloat(MAX_TEMP_NAME);
                    int condition = getWeatherIconId(DataMapItem.fromDataItem(item).getDataMap()
                            .getInt(CONDITION_NAME));
                    SunshineWatchFace.sWeatherIconBitmap
                            = ((BitmapDrawable) getResources().getDrawable(condition, null))
                            .getBitmap();
                }
            }
        }
    }

    /**
     * See SunshineWeatherUtils::getSmallArtResourceIdForWeatherCondition, copied to avoid module
     * dependency and additional traffic for transferring bitmaps.
     */
    private static int getWeatherIconId(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.ic_clear;
        }

        return R.drawable.ic_storm;
    }
}
