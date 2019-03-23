package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import ru.kabylin.andrey.telegramcontest.helpers.DateHelper;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ChartState implements Parcelable {
    ChartType chartType = ChartType.LINES;
    int minimapInitialPreviewSize = (int) MeasureUtils.convertDpToPixel(80);
    int minimapPreviewLeft = 0;
    int minimapPreviewRight = minimapInitialPreviewSize;
    int minimapPreviewRenderResizeAreaSize = (int) MeasureUtils.convertDpToPixel(8);
    int minimapPreviewResizeAreaSize = (int) MeasureUtils.convertDpToPixel(20);
    int minimapPreviewBorderHeight = (int) MeasureUtils.convertDpToPixel(2);
    Rect minimapRect = null;
    Rect previewRect = null;
    boolean isInitPreviewMinMaxY = false;
    boolean normilizeByMin = true;
    float statePreviewMaxY = 0f;
    float statePreviewMaxY2 = 0f;
    float statePreviewMinY = 0f;
    float statePreviewMinY2 = 0f;
    float previewMaxY = 0f;
    float previewMaxY2 = 0f;
    float previewMinY = 0f;
    float previewMinY2 = 0f;
    final boolean showTime;
    int chartIndex = 1;
    final List<ChartButton> buttons = new ArrayList<>();
    public String rangeTitle = "?";
    boolean normilizeToPercentage = false;
    public boolean zoomed = false;
    ChartState zoomedChartState = null;

    float previewMaxYChangeSpeed = 150f;
    float opacityChangeSpeed = 150f;
    float stackedScaleChangeSpeed = 150f;
    float areaStackedScaleChangeSpeed = 50f;
    float minimapMaxYChangeSpeed = 150f;
    float axisXOpacityChangeSpeed = 50f;
    float axisYOpacityChangeSpeed = 150f;
    float axisYOffsetChangeSpeed = 150f;
    float popupOpacityChangeSpeed = 150f;
    float zoomOpacityChangeSpeed = 50f;
    float zoomScaleChangeSpeed = 50f;
    int yAxisLines = 6;

    float chartsOpacity = 1f;
    float chartsOpacityState = 1f;
    float chartsScale = 0f;
    float chartsScaleState = 0f;

    float axisXDistance = MeasureUtils.convertDpToPixel(240);
    float axisXOffsetY = MeasureUtils.convertDpToPixel(20);
    float axisYTextOffsetX = MeasureUtils.convertDpToPixel(8);
    float axisYTextOffsetY = MeasureUtils.convertDpToPixel(5);

    boolean axisXIsInit = false;
    boolean axisYIsInit = false;
    boolean axisY2IsInit = false;

    float minAxisYDelta = MeasureUtils.convertDpToPixel(10);
    float intersectPointSize = MeasureUtils.convertDpToPixel(5);
    float intersectPointStrokeWidth = MeasureUtils.convertDpToPixel(3);

    public List<ChartData> charts = new ArrayList<>();
    List<Vertex> popupIntersectPoints = new ArrayList<>();
    float intersectX = 0;

    final List<AxisVertex> xAxis = new ArrayList<>();

    final List<AxisVertex> yAxis = new ArrayList<>();
    final List<AxisVertex> yAxisCurrent = new ArrayList<>();
    final List<AxisVertex> yAxisPast = new ArrayList<>();

    final List<AxisVertex> y2Axis = new ArrayList<>();
    final List<AxisVertex> y2AxisCurrent = new ArrayList<>();
    final List<AxisVertex> y2AxisPast = new ArrayList<>();

    // For Y axis animations
    float lastStatePreviewMaxY = 0f;
    float lastStatePreviewMaxY2 = 0f;

    final List<AxisVertex> previewAxisX = new ArrayList<>();
    final List<AxisVertex> previewAxisY = new ArrayList<>();
    final List<AxisVertex> previewAxisY2 = new ArrayList<>();

    final AxisVertex previewAxisYZero = new AxisVertex(0, 0, "0");
    final AxisVertex previewAxisY2Zero = new AxisVertex(0, 0, "0");

    final List<AxisVertex> axisXPool = new ArrayList<>();
    final List<AxisVertex> previewAxisXPool = new ArrayList<>();

    final List<Long> xValues = new ArrayList<>();

    final Popup popup = new Popup();

    boolean isInit = false;

    public ChartState(boolean showTime) {
        this.showTime = showTime;
    }

    protected ChartState(Parcel in) {
        minimapInitialPreviewSize = in.readInt();
        minimapPreviewLeft = in.readInt();
        minimapPreviewRight = in.readInt();
        minimapPreviewRenderResizeAreaSize = in.readInt();
        minimapPreviewResizeAreaSize = in.readInt();
        minimapPreviewBorderHeight = in.readInt();
        minimapRect = in.readParcelable(Rect.class.getClassLoader());
        previewRect = in.readParcelable(Rect.class.getClassLoader());
        isInitPreviewMinMaxY = in.readByte() != 0;
        normilizeByMin = in.readByte() != 0;
        statePreviewMaxY = in.readFloat();
        statePreviewMaxY2 = in.readFloat();
        statePreviewMinY = in.readFloat();
        statePreviewMinY2 = in.readFloat();
        previewMaxY = in.readFloat();
        previewMaxY2 = in.readFloat();
        previewMinY = in.readFloat();
        previewMinY2 = in.readFloat();
        showTime = in.readByte() != 0;
        chartIndex = in.readInt();
        rangeTitle = in.readString();
        normilizeToPercentage = in.readByte() != 0;
        zoomed = in.readByte() != 0;
        zoomedChartState = in.readParcelable(ChartState.class.getClassLoader());
        previewMaxYChangeSpeed = in.readFloat();
        opacityChangeSpeed = in.readFloat();
        stackedScaleChangeSpeed = in.readFloat();
        minimapMaxYChangeSpeed = in.readFloat();
        axisXOpacityChangeSpeed = in.readFloat();
        axisYOpacityChangeSpeed = in.readFloat();
        axisYOffsetChangeSpeed = in.readFloat();
        popupOpacityChangeSpeed = in.readFloat();
        zoomOpacityChangeSpeed = in.readFloat();
        zoomScaleChangeSpeed = in.readFloat();
        yAxisLines = in.readInt();
        chartsOpacity = in.readFloat();
        chartsOpacityState = in.readFloat();
        chartsScale = in.readFloat();
        chartsScaleState = in.readFloat();
        axisXDistance = in.readFloat();
        axisXOffsetY = in.readFloat();
        axisYTextOffsetX = in.readFloat();
        axisYTextOffsetY = in.readFloat();
        axisXIsInit = in.readByte() != 0;
        axisYIsInit = in.readByte() != 0;
        axisY2IsInit = in.readByte() != 0;
        minAxisYDelta = in.readFloat();
        intersectPointSize = in.readFloat();
        intersectPointStrokeWidth = in.readFloat();
        intersectX = in.readFloat();
        lastStatePreviewMaxY = in.readFloat();
        lastStatePreviewMaxY2 = in.readFloat();
        isInit = in.readByte() != 0;
        lastXLength = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minimapInitialPreviewSize);
        dest.writeInt(minimapPreviewLeft);
        dest.writeInt(minimapPreviewRight);
        dest.writeInt(minimapPreviewRenderResizeAreaSize);
        dest.writeInt(minimapPreviewResizeAreaSize);
        dest.writeInt(minimapPreviewBorderHeight);
        dest.writeParcelable(minimapRect, flags);
        dest.writeParcelable(previewRect, flags);
        dest.writeByte((byte) (isInitPreviewMinMaxY ? 1 : 0));
        dest.writeByte((byte) (normilizeByMin ? 1 : 0));
        dest.writeFloat(statePreviewMaxY);
        dest.writeFloat(statePreviewMaxY2);
        dest.writeFloat(statePreviewMinY);
        dest.writeFloat(statePreviewMinY2);
        dest.writeFloat(previewMaxY);
        dest.writeFloat(previewMaxY2);
        dest.writeFloat(previewMinY);
        dest.writeFloat(previewMinY2);
        dest.writeByte((byte) (showTime ? 1 : 0));
        dest.writeInt(chartIndex);
        dest.writeString(rangeTitle);
        dest.writeByte((byte) (normilizeToPercentage ? 1 : 0));
        dest.writeByte((byte) (zoomed ? 1 : 0));
        dest.writeParcelable(zoomedChartState, flags);
        dest.writeFloat(previewMaxYChangeSpeed);
        dest.writeFloat(opacityChangeSpeed);
        dest.writeFloat(stackedScaleChangeSpeed);
        dest.writeFloat(minimapMaxYChangeSpeed);
        dest.writeFloat(axisXOpacityChangeSpeed);
        dest.writeFloat(axisYOpacityChangeSpeed);
        dest.writeFloat(axisYOffsetChangeSpeed);
        dest.writeFloat(popupOpacityChangeSpeed);
        dest.writeFloat(zoomOpacityChangeSpeed);
        dest.writeFloat(zoomScaleChangeSpeed);
        dest.writeInt(yAxisLines);
        dest.writeFloat(chartsOpacity);
        dest.writeFloat(chartsOpacityState);
        dest.writeFloat(chartsScale);
        dest.writeFloat(chartsScaleState);
        dest.writeFloat(axisXDistance);
        dest.writeFloat(axisXOffsetY);
        dest.writeFloat(axisYTextOffsetX);
        dest.writeFloat(axisYTextOffsetY);
        dest.writeByte((byte) (axisXIsInit ? 1 : 0));
        dest.writeByte((byte) (axisYIsInit ? 1 : 0));
        dest.writeByte((byte) (axisY2IsInit ? 1 : 0));
        dest.writeFloat(minAxisYDelta);
        dest.writeFloat(intersectPointSize);
        dest.writeFloat(intersectPointStrokeWidth);
        dest.writeFloat(intersectX);
        dest.writeFloat(lastStatePreviewMaxY);
        dest.writeFloat(lastStatePreviewMaxY2);
        dest.writeByte((byte) (isInit ? 1 : 0));
        dest.writeInt(lastXLength);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChartState> CREATOR = new Creator<ChartState>() {
        @Override
        public ChartState createFromParcel(Parcel in) {
            return new ChartState(in);
        }

        @Override
        public ChartState[] newArray(int size) {
            return new ChartState[size];
        }
    };

    Rect getMinimapPreviewRect() {
        if (minimapRect == null) {
            return null;
        } else {
            return new Rect(
                    minimapPreviewLeft,
                    minimapRect.top,
                    minimapPreviewRight,
                    minimapRect.bottom
            );
        }
    }

    int minimapPreviewSize() {
        return minimapPreviewRight - minimapPreviewLeft;
    }

    public void setX(List<Long> x) {
        if (!xValues.isEmpty()) {
            throw new AssertionError("xValues already set");
        }

        this.xValues.addAll(x);
    }

    void normalizeDataToPercentage() {
        for (int i = 0; i < xValues.size(); ++i) {
            float max = 0;

            for (final ChartData chart : charts) {
                Vertex vertex = chart.originalData.get(i);

                if (!isInit) {
                    if (max < vertex.y) {
                        max = vertex.y;
                    }
                } else {
                    if (max < vertex.yCopy * chart.scale) {
                        max = vertex.yCopy * chart.scale;
                    }
                }
            }

            for (final ChartData chart : charts) {
                final Vertex vertex = chart.originalData.get(i);
                final float percents;

                if (isInit) {
                    percents = (vertex.yCopy / max) * 100f;
                } else {
                    percents = (vertex.y / max) * 100f;
                }

                final String title = String.valueOf(Math.round(percents)) + "%";
                vertex.y = percents * chart.scale;

                if (!isInit) {
                    vertex.yCopy = vertex.y;
                }

                chart.originalData.get(i).yValue = title;
                chart.minimapPointsPool.get(i).yValue = title;
                chart.previewPointsPool.get(i).yValue = title;
                chart.minimapInnerPreviewPool.get(i).yValue = title;
            }
        }
    }

    void sortStackedArea() {
        for (final ChartData chart : charts) {
            float sum = 0;

            for (final Vertex vertex : chart.originalData) {
                sum += vertex.y;
            }

            chart.weight = sum;
        }

        Collections.sort(charts);
    }

    private int lastXLength = 0;

    void addX(List<Long> x) {
        lastXLength = this.xValues.size();
        this.xValues.addAll(x);
    }

    public void addChart(String color, String name, List<Long> y) {
        if (xValues.size() != y.size() + lastXLength) {
            throw new AssertionError(xValues.size() + " != " + y.size());
        }

        ChartData chart = null;

        for (ChartData chartData : charts) {
            if (chartData.name.equals(name)) {
                chart = chartData;
                break;
            }
        }

        if (chart == null) {
            chart = new ChartData(name, color);
        }

        final int parsedColor = Color.parseColor(color);

        for (int i = lastXLength; i < xValues.size(); ++i) {
            final String yValue = y.get(i - lastXLength).toString();
            final String xValue;

            if (showTime) {
                final String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(xValues.get(i)));
                xValue = DateHelper.humanizeDate(new Date(xValues.get(i)), true) + " " + time;
            } else {
                xValue = DateHelper.humanizeDate(new Date(xValues.get(i)), true);
            }

            chart.originalData.add(new Vertex(xValues.get(i), xValues.get(i), y.get(i - lastXLength), name, xValue, yValue, parsedColor));
            chart.minimapPointsPool.add(new Vertex(xValues.get(i), 0, 0, name, xValue, yValue, parsedColor));
            chart.previewPointsPool.add(new Vertex(xValues.get(i), 0, 0, name, xValue, yValue, parsedColor));
            chart.minimapInnerPreviewPool.add(new Vertex(xValues.get(i), 0, 0, name, xValue, yValue, parsedColor));
        }

        charts.add(chart);
    }
}
