package ru.kabylin.andrey.telegramcontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import ru.kabylin.andrey.telegramcontest.chart.ChartView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ChartView chartView = null;
    private CheckBox checkBox0 = null;
    private CheckBox checkBox1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.chartView);
        checkBox0 = findViewById(R.id.checkBox0);
        checkBox1 = findViewById(R.id.checkBox1);

        checkBox0.setOnClickListener(this);
        checkBox1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox0:
                chartView.setChartVisibilityByName("#0", checkBox0.isChecked());
                break;

            case R.id.checkBox1:
                chartView.setChartVisibilityByName("#1", checkBox1.isChecked());
                break;
        }
    }
}
