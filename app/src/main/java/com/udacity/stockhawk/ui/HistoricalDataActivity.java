package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        String history = getIntent().getStringExtra(getString(R.string.EXTRA_STOCK_HISTORY));
        if (symbol == null && history == null) {
            Toast.makeText(this, getString(R.string.toast_history_chart_failure, symbol), Toast.LENGTH_LONG).show();
            finish();
        }
        String[] historyRows = history.split("\n");
        LineDataSet dataSet = new LineDataSet(getHistoricalValues(historyRows), symbol);
        LineData lineData = new LineData(dataSet);
        final String[] xAxisLabels = getXAxis(historyRows);
        historyChart.setBackgroundColor(Color.WHITE);
        historyChart.setData(lineData);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabels[(int) value];
            }
        };
        XAxis xAxis = historyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);
        YAxis yAxisRight = historyChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        historyChart.getDescription().setText(getString(R.string.history_chart_description));
        historyChart.invalidate();
    }

    private List<Entry> getHistoricalValues(String[] rows) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            entries.add(new Entry(i, new Float(rows[rows.length - i - 1].split(",")[1])));
        }
        return entries;
    }

    private String[] getXAxis(String[] rows){
        String[] xAxisLabels = new String[rows.length];
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
        for(int i = 0; i<xAxisLabels.length; i++){
            xAxisLabels[i] = dateFormat.format(new Date(Long.parseLong(rows[rows.length - i - 1].split(",")[0].trim())));
        }
        return xAxisLabels;
    }
}
