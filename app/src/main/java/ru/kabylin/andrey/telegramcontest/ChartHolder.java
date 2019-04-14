package ru.kabylin.andrey.telegramcontest;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.chart.ChartView;
import ru.kabylin.andrey.telegramcontest.chart.OnChangeChartVisibleRangeListener;
import ru.kabylin.andrey.telegramcontest.chart.OnZoomListener;
import ru.kabylin.andrey.telegramcontest.views.RecyclerItemHolder;

public class ChartHolder extends RecyclerItemHolder<ChartRecyclerItem> implements View.OnClickListener, OnZoomListener, OnChangeChartVisibleRangeListener {
    private final ChartView chartView;
    private final TextView titleTextView;
    private final Button zoomOutButton;
    private final TextView rangeTitleTextView;

    ChartHolder(Context context, View view) {
        super(context, view);

        chartView = view.findViewById(R.id.chartView);
        titleTextView = view.findViewById(R.id.titleTextView);
        rangeTitleTextView = view.findViewById(R.id.rangeTitleTextView);

        zoomOutButton = view.findViewById(R.id.buttonZoomOut);
        zoomOutButton.setOnClickListener(this);
    }

    @Override
    public void bind(ChartRecyclerItem data) {
        final ChartState state = data.chartState;

        chartView.setOnZoomListener(this);
        chartView.setOnChangeChartVisibleRangeListener(this);
        chartView.setChartState(state);
        chartView.setLayoutManager(data.layoutManager);

        titleTextView.setText(data.title);
        rangeTitleTextView.setText(state.rangeTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonZoomOut:
                chartView.zoomOut();
                break;
        }
    }

    @Override
    public void onZoomIn() {
        zoomOutButton.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onZoomOut() {
        zoomOutButton.setVisibility(View.INVISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangeChartVisibleRangeListener(String rangeTitle) {
        rangeTitleTextView.setText(rangeTitle);
    }
}
