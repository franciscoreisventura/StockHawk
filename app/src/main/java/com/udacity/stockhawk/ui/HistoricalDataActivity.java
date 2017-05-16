package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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
        String history = getIntent().getStringExtra(getString(R.string.EXTRA_STOCK_HISTORY));
        if (symbol == null && history == null) {
            Toast.makeText(this, this.getString(R.string.toast_history_chart_failure, symbol), Toast.LENGTH_LONG).show();
            finish();
        }
        LineDataSet dataSet = new LineDataSet(getHistoricalData(history), symbol);
        LineData lineData = new LineData(dataSet);
        historyChart.setBackgroundColor(Color.WHITE);
        historyChart.setData(lineData);
        historyChart.invalidate();
    }

    private List<Entry> getHistoricalData(String history) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Entry> entries = new ArrayList<>();
        String[] rows = history.split("\n");
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[rows.length - i - 1].split(",");
            entries.add(new Entry(i, new Float(values[1])));
        }
        return entries;
    }
}
