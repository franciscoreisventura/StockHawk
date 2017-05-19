package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by fraven on 19-05-2017.
 */

public class StockHawkIntentService extends IntentService{

    public StockHawkIntentService() {
        super(StockHawkIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
