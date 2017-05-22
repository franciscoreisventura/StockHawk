package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.ui.MainActivity;

import timber.log.Timber;

/**
 * Created by fraven on 21-05-2017.
 */

public class StockHawkWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockHawkWidgetViewsFactory(getApplicationContext());
    }

    class StockHawkWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Cursor cursor;
        private Context context;

        public StockHawkWidgetViewsFactory(Context context){
            this.context = context;
        }

        @Override
        public void onCreate() {
            //we fill the data on onDataSetChanged;
        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) {
                cursor.close();
            }
            final long identityToken = Binder.clearCallingIdentity();
            cursor = getContentResolver().query(Contract.Quote.URI, null, null, null, null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        @Override
        public int getCount() {
            return cursor != null ? cursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.list_item_quote);
            cursor.moveToPosition(position);
            String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
            remoteViews.setTextViewText(R.id.symbol, symbol);
            remoteViews.setTextViewText(R.id.price, "$" + cursor.getFloat(Contract.Quote.POSITION_PRICE));
            float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            int backgroundDrawable;
            String absoluteChangeString = "";
            if (rawAbsoluteChange > 0) {
                backgroundDrawable = R.drawable.percent_change_pill_green;
                absoluteChangeString = "+";
            } else {
                backgroundDrawable = R.drawable.percent_change_pill_red;
            }
            absoluteChangeString = absoluteChangeString + rawAbsoluteChange;
            remoteViews.setTextViewText(R.id.change, absoluteChangeString);
            remoteViews.setInt(R.id.change, "setBackgroundResource", backgroundDrawable);
            final Intent fillInIntent = new Intent();
            fillInIntent.putExtra(getString(R.string.EXTRA_STOCK_SYMBOL), symbol);
            fillInIntent.putExtra(getString(R.string.EXTRA_STOCK_HISTORY), cursor.getString(Contract.Quote.POSITION_HISTORY));
            remoteViews.setOnClickFillInIntent(R.id.list_item_quote, fillInIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return cursor.moveToPosition(position) ? cursor.getLong(Contract.Quote.POSITION_ID) : position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
