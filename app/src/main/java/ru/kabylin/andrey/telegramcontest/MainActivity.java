package ru.kabylin.andrey.telegramcontest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.helpers.JsonUtils;
import ru.kabylin.andrey.telegramcontest.views.HolderFactory;
import ru.kabylin.andrey.telegramcontest.views.RecyclerItemHolder;
import ru.kabylin.andrey.telegramcontest.views.SingleItemRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HolderFactory<ChartState> {
    private boolean nightMode = false;
    private ArrayList<ChartState> charts = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
        nightMode = preferences.getBoolean("night_mode", false);

        setTheme(nightMode ? R.style.AppNightTheme : R.style.AppTheme);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey("charts")) {
            charts = savedInstanceState.getParcelableArrayList("charts");
        } else {
            loadCharts();
        }

        initRecyclerView();
    }

    private void loadCharts() {
        try {
            final String json = JsonUtils.readResourceJson(getResources(), R.raw.chart_data);

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final ChartState chartState = ChartJsonLoader.loadCharts(jsonObject);
                charts.add(chartState);
            }
        } catch (IOException | JSONException e) {
            Log.e("MainActivity", "JSON LOAD ERROR");
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        final ChartViewLayoutManager layoutManager = new ChartViewLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SingleItemRecyclerAdapter<ChartState> recyclerAdapter = new SingleItemRecyclerAdapter<>(this, charts, R.layout.item_chart, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_base, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @SuppressLint("ApplySharedPref")
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

    @Override
    public RecyclerItemHolder<ChartState> create(Context context, View view) {
        return new ChartHolder(context, view);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("charts", charts);
        outState.putFloat("scroll", recyclerView.getScrollY());
    }
}
