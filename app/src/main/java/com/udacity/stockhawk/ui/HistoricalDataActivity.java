package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fventura on 15/05/17.
 */

public class HistoricalDataActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    LineChart historyChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);
        ButterKnife.bind(this);

        String symbol = getIntent().getStringExtra(getString(R.string.EXTRA_STOCK_SYMBOL));
        String history = getIntent().getStringExtra(getString(R.string.EXTRA_STOCK_HISTORY))
        if(symbol != null && history != null) {
            LineDataSet dataSet = new LineDataSet(getHistoricalData(history), symbol);
            LineData lineData = new LineData(dataSet);
            historyChart.setData(lineData);
            historyChart.invalidate();
        }
    }

    private List<Entry> getHistoricalData(String history) {
        //TODO;
        return new ArrayList<>();
    }
}
