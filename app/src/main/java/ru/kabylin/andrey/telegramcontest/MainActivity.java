package ru.kabylin.andrey.telegramcontest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.chart.JsonDataProvider;
import ru.kabylin.andrey.telegramcontest.helpers.ResourcesUtils;
import ru.kabylin.andrey.telegramcontest.views.HolderFactory;
import ru.kabylin.andrey.telegramcontest.views.RecyclerItemHolder;
import ru.kabylin.andrey.telegramcontest.views.SingleItemRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HolderFactory<ChartRecyclerItem> {
    private boolean nightMode = false;
    private ArrayList<ChartRecyclerItem> charts = new ArrayList<>();
    private ArrayList<ChartState> chartsStates = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChartViewLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
        nightMode = preferences.getBoolean("night_mode", false);

        setTheme(nightMode ? R.style.AppNightTheme : R.style.AppTheme);
        setContentView(R.layout.activity_main);

        layoutManager = new ChartViewLayoutManager(this);

        if (savedInstanceState != null && savedInstanceState.containsKey("charts")) {
            chartsStates = savedInstanceState.getParcelableArrayList("charts");
            fillCharts();
        } else {
            loadCharts();
        }

        initRecyclerView();
    }

    private void loadCharts() {
        try {
            JsonDataProvider dataProvider = new JsonDataProvider();
            chartsStates.add(dataProvider.getOverview(getAssets(), 3));
            fillCharts();
        } catch (IOException | JSONException e) {
            Log.e("MainActivity", "JSON LOAD ERROR");
        }
    }

    private void fillCharts() {
        int counter = 0;
        for (ChartState chartState : chartsStates) {
            ++counter;
            charts.add(new ChartRecyclerItem(layoutManager, chartState, "Chart " + counter));
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        SingleItemRecyclerAdapter<ChartRecyclerItem> recyclerAdapter = new SingleItemRecyclerAdapter<>(this, charts, R.layout.item_chart, this);
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
    public RecyclerItemHolder<ChartRecyclerItem> create(Context context, View view) {
        return new ChartHolder(context, view);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("charts", chartsStates);
        outState.putFloat("scroll", recyclerView.getScrollY());
    }
}
