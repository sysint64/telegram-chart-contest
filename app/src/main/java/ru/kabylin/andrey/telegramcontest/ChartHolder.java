package ru.kabylin.andrey.telegramcontest;

import android.content.Context;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;

import ru.kabylin.andrey.telegramcontest.chart.ChartData;
import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.chart.ChartView;
import ru.kabylin.andrey.telegramcontest.views.RecyclerItemHolder;

public class ChartHolder extends RecyclerItemHolder<ChartState> implements View.OnClickListener {
    private final ChartView chartView;
    private final AppCompatCheckBox[] checkBoxes = new AppCompatCheckBox[5];
    private final View[] hrs = new View[4];

    ChartHolder(Context context, View view) {
        super(context, view);

        chartView = view.findViewById(R.id.chartView);
        checkBoxes[0] = view.findViewById(R.id.checkbox1).findViewById(R.id.checkbox);
        checkBoxes[1] = view.findViewById(R.id.checkbox2).findViewById(R.id.checkbox);
        checkBoxes[2] = view.findViewById(R.id.checkbox3).findViewById(R.id.checkbox);
        checkBoxes[3] = view.findViewById(R.id.checkbox4).findViewById(R.id.checkbox);
        checkBoxes[4] = view.findViewById(R.id.checkbox5).findViewById(R.id.checkbox);
        hrs[0] = view.findViewById(R.id.hr1);
        hrs[1] = view.findViewById(R.id.hr2);
        hrs[2] = view.findViewById(R.id.hr3);
        hrs[3] = view.findViewById(R.id.hr4);

        for (int i = 0; i < 5; ++i) {
            checkBoxes[i].setOnClickListener(this);
        }
    }

    @Override
    public void bind(ChartState data) {
        chartView.setChartState(data);

        for (int i = 0; i < data.charts.size(); ++i) {
            final ChartData chart = data.charts.get(i);
            checkBoxes[i].setText(chart.name);
            checkBoxes[i].setVisibility(View.VISIBLE);
            checkBoxes[i].setChecked(chart.isVisible);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //noinspection ConstantConditions
                DrawableCompat.setTint(checkBoxes[i].getButtonDrawable(), chart.color);
            }

            if (i != data.charts.size() - 1) {
                hrs[i].setVisibility(View.VISIBLE);
            } else {
                hrs[i].setVisibility(View.GONE);
            }
        }

        for (int i = data.charts.size(); i < 5; ++i) {
            checkBoxes[i].setVisibility(View.GONE);

            if (i < 4) {
                hrs[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final CheckBox checkBox = (CheckBox) v;
        chartView.setChartVisibilityByName(checkBox.getText().toString(), checkBox.isChecked());
    }
}
