package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import ru.kabylin.andrey.telegramcontest.helpers.DateHelper;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ChartState implements Parcelable {
    int minimapPreviewLeft = 0;
    int minimapPreviewRight = (int) MeasureUtils.convertDpToPixel(80);
    int minimapPreviewRenderResizeAreaSize = (int) MeasureUtils.convertDpToPixel(8);
    int minimapPreviewResizeAreaSize = (int) MeasureUtils.convertDpToPixel(20);
    int minimapPreviewBorderHeight = (int) MeasureUtils.convertDpToPixel(2);
    Rect minimapRect = null;
    Rect previewRect = null;
    boolean isInitPreviewMaxY = false;
    float statePreviewMaxY = 0f;
    float statePreviewMaxY2 = 0f;
    float previewMaxY = 0f;
    float previewMaxY2 = 0f;

    float previewMaxYChangeSpeed = 150f;
    float opacityChangeSpeed = 150f;
    float minimapMaxYChangeSpeed = 150f;
    float axisXOpacityChangeSpeed = 50f;
    float axisYOpacityChangeSpeed = 150f;
    float axisYOffsetChangeSpeed = 150f;
    float popupOpacityChangeSpeed = 150f;

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

    public ChartState() {
    }

    public ChartState(Parcel in) {
        minimapPreviewLeft = in.readInt();
        minimapPreviewRight = in.readInt();
        minimapPreviewRenderResizeAreaSize = in.readInt();
        minimapPreviewResizeAreaSize = in.readInt();
        minimapPreviewBorderHeight = in.readInt();
        minimapRect = in.readParcelable(Rect.class.getClassLoader());
        previewRect = in.readParcelable(Rect.class.getClassLoader());
        isInitPreviewMaxY = in.readByte() != 0;
        statePreviewMaxY = in.readFloat();
        statePreviewMaxY2 = in.readFloat();
        previewMaxY = in.readFloat();
        previewMaxY2 = in.readFloat();
        previewMaxYChangeSpeed = in.readFloat();
        opacityChangeSpeed = in.readFloat();
        minimapMaxYChangeSpeed = in.readFloat();
        axisXOpacityChangeSpeed = in.readFloat();
        axisYOpacityChangeSpeed = in.readFloat();
        axisYOffsetChangeSpeed = in.readFloat();
        popupOpacityChangeSpeed = in.readFloat();
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
        lastStatePreviewMaxY = in.readFloat();
        lastStatePreviewMaxY2 = in.readFloat();
        isInit = in.readByte() != 0;
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
        if (minimapRect != null) {
            return new Rect(
                    minimapPreviewLeft,
                    minimapRect.top,
                    minimapPreviewRight,
                    minimapRect.bottom
            );
        } else {
            return null;
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
            final String xValue = DateHelper.humanizeDate(new Date(xValues.get(i)), true);

            chart.originalData.add(new Vertex(xValues.get(i), y.get(i - lastXLength), name, xValue, yValue, parsedColor));
            chart.minimapPointsPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
            chart.previewPointsPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
            chart.minimapInnerPreviewPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
        }

        charts.add(chart);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minimapPreviewLeft);
        dest.writeInt(minimapPreviewRight);
        dest.writeInt(minimapPreviewRenderResizeAreaSize);
        dest.writeInt(minimapPreviewResizeAreaSize);
        dest.writeInt(minimapPreviewBorderHeight);
        dest.writeParcelable(minimapRect, flags);
        dest.writeParcelable(previewRect, flags);
        dest.writeByte((byte) (isInitPreviewMaxY ? 1 : 0));
        dest.writeFloat(statePreviewMaxY);
        dest.writeFloat(statePreviewMaxY2);
        dest.writeFloat(previewMaxY);
        dest.writeFloat(previewMaxY2);
        dest.writeFloat(previewMaxYChangeSpeed);
        dest.writeFloat(opacityChangeSpeed);
        dest.writeFloat(minimapMaxYChangeSpeed);
        dest.writeFloat(axisXOpacityChangeSpeed);
        dest.writeFloat(axisYOpacityChangeSpeed);
        dest.writeFloat(axisYOffsetChangeSpeed);
        dest.writeFloat(popupOpacityChangeSpeed);
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
        dest.writeFloat(lastStatePreviewMaxY);
        dest.writeFloat(lastStatePreviewMaxY2);
        dest.writeByte((byte) (isInit ? 1 : 0));
    }
}
