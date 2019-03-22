package ru.kabylin.andrey.telegramcontest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.chart.ChartView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ChartView chartView = null;
    private CheckBox checkBox0 = null;
    private CheckBox checkBox1 = null;

    private boolean nightMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
        nightMode = preferences.getBoolean("night_mode", false);

        setTheme(nightMode ? R.style.AppNightTheme : R.style.AppTheme);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.chartView);
        checkBox0 = findViewById(R.id.checkBox0);
        checkBox1 = findViewById(R.id.checkBox1);

        checkBox0.setOnClickListener(this);
        checkBox1.setOnClickListener(this);

        if (MainActivityState.INSTANCE.chartState == null) {
            try {
                final String json = readJson();

                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(1);

                MainActivityState.INSTANCE.chartState = ChartJsonLoader.loadCharts(jsonObject);
                chartView.setChartState(MainActivityState.INSTANCE.chartState);
            } catch (IOException | JSONException e) {
                Log.e("ChartView", "JSON LOAD ERROR");
            }
        } else {
            chartView.setChartState(MainActivityState.INSTANCE.chartState);
        }
    }


    private String readJson() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.chart_data)));
        String line = reader.readLine();
        final StringBuilder json = new StringBuilder();

        while (line != null) {
            json.append(line);
            line = reader.readLine();
        }

        return json.toString();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_base, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_toggle_night_mode:
                final SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
                final SharedPreferences.Editor preferencesEditor = preferences.edit();

                preferencesEditor.putBoolean("night_mode", !nightMode);
                preferencesEditor.commit();
                recreate();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
