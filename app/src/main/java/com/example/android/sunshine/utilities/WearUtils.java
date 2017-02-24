package com.example.android.sunshine.utilities;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Static helper methods for Android Wear synchronisation
 */
public class WearUtils {
    private static final String WEATHER_UPDATE_PATH = "/weather";
    private static final String MIN_TEMP_NAME = "min";
    private static final String MAX_TEMP_NAME = "max";
    private static final String CONDITION_NAME = "condition";

    private static GoogleApiClient mGoogleApiClient;

    public static void updateWear(float minTemp, float maxTemp, int condition) {
        if (mGoogleApiClient == null) throw new IllegalStateException("WearUtils not initialized");

        PutDataMapRequest mapRequest = PutDataMapRequest.create(WEATHER_UPDATE_PATH);
        mapRequest.getDataMap().putFloat(MIN_TEMP_NAME, minTemp);
        mapRequest.getDataMap().putFloat(MAX_TEMP_NAME, maxTemp);
        mapRequest.getDataMap().putInt(CONDITION_NAME, condition);
        PutDataRequest dataRequest = mapRequest.asPutDataRequest();
        dataRequest.setUrgent();
        Wearable.DataApi.putDataItem(mGoogleApiClient, dataRequest);
    }

    public static void initClient(GoogleApiClient client) {
        mGoogleApiClient = client;
    }
}
