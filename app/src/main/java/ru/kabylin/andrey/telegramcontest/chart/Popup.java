package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.List;

final class Popup {
    float stateOpacity = 0f;
    float opacity = 0f;
    int left = 0;
    private int top = (int) MeasureUtils.convertDpToPixel(8);
    private final List<PopupItem> items = new ArrayList<>();
    private String title = "Sat, Feb 24";
    boolean isVisible = false;
    Drawable arrowIcon = null;

    private OnPopupEventsListener eventsListener = null;
    private PopupOnClickListener onClickListener = null;

    void setOnPopupEventsListener(OnPopupEventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }

    void setPopupOnClickListener(PopupOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    Popup() {
    }

    final static class PopupItem {
        final int color;
        final String value;
        final String title;

        final Rect valueBounds = new Rect();
        final Rect titleBounds = new Rect();
        float valueLeft = 0f;
        float valueTop = 0f;
        float titleLeft = 0f;
        float titleTop = 0f;

        PopupItem(int color, String value, String title) {
            this.color = color;
            this.value = value;
            this.title = title;
        }
    }

    void drop(String title, float x, float y, List<PopupItem> items) {
        this.items.clear();
        this.items.addAll(items);

        this.title = title;
        this.stateOpacity = 1f;
        this.left = (int) x;
        this.isVisible = true;

        if (eventsListener != null) {
            eventsListener.onPopupDrop();
        }
    }

    void hide() {
        this.stateOpacity = 0f;
        this.isVisible = false;

        if (eventsListener != null) {
            eventsListener.onPopupHide();
        }
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF backgroundRect = new RectF();
    private final RectF shadowRectLevel1 = new RectF();
    private final RectF shadowRectLevel2 = new RectF();
    private final float radius = MeasureUtils.convertDpToPixel(3);
    private float titleLeft = 0f;
    private float titleTop = 0f;
    private final Rect titleBounds = new Rect();
    private int iconLeft = 0;
    private int iconTop = 0;
    private int iconWidth = (int) MeasureUtils.convertDpToPixel(16);
    private int iconHeight = (int) MeasureUtils.convertDpToPixel(16);
    private int iconMarginTop = (int) MeasureUtils.convertDpToPixel(8);

    private final float titleTextSize = MeasureUtils.convertDpToPixel(14);
    private final float itemTitleTextSize = MeasureUtils.convertDpToPixel(14);
    private final float itemValueTextSize = MeasureUtils.convertDpToPixel(14);

    private final float itemTitleTopMargin = MeasureUtils.convertDpToPixel(10);
    private final float titleMarginTop = MeasureUtils.convertDpToPixel(8);
    private final float paddingHorizontal = MeasureUtils.convertDpToPixel(12);
    private final float paddingBottom = MeasureUtils.convertDpToPixel(8);
    private final float itemValueMarginLeft = MeasureUtils.convertDpToPixel(16);

    int chartColorPopupColor = Color.WHITE;
    int chartColorPopupTitleColor = Color.BLACK;
    int itemTitleColor = Color.BLACK;

    void draw(Canvas canvas, Rect rect) {
        measure(rect, 0, false);

        drawBackground(canvas);
        drawTitle(canvas);
        drawItems(canvas);
        drawArrow(canvas);
    }

    private void measure(Rect rect, float offsetX, boolean finalMeasure) {
        paint.setTextSize(titleTextSize);
        paint.getTextBounds(title, 0, title.length(), titleBounds);

        titleLeft = left + paddingHorizontal + offsetX;
        titleTop = top + titleBounds.height() + titleMarginTop;

        float maxWidth = titleBounds.width();
        float lastTop = titleTop;

        // Item titles
        for (PopupItem item : items) {
            paint.setTextSize(itemTitleTextSize);
            paint.getTextBounds(item.value, 0, item.value.length(), item.valueBounds);
            maxWidth = Math.max(maxWidth, item.valueBounds.width());

            lastTop += item.valueBounds.height() + itemTitleTopMargin;

            // Title
            item.titleLeft = left + paddingHorizontal + offsetX;
            item.titleTop = lastTop;
        }

        lastTop = titleTop;
        float maxValueWidth = 0;

        // Items values
        for (PopupItem item : items) {
            paint.setTextSize(itemValueTextSize);
            paint.getTextBounds(item.value, 0, item.value.length(), item.valueBounds);
            maxValueWidth = Math.max(maxValueWidth, item.valueBounds.width());

            lastTop += item.valueBounds.height() + itemTitleTopMargin;

            // Title
            item.valueLeft = left + paddingHorizontal + maxWidth + itemValueMarginLeft + offsetX;
            item.valueTop = lastTop;
        }

        // Items values pass 2
        for (PopupItem item : items) {
            item.valueLeft += maxValueWidth;
        }

        maxWidth += maxValueWidth + itemValueMarginLeft;

        iconLeft = (int) (left + maxWidth + offsetX);
        iconTop = top + iconMarginTop;

        backgroundRect.set(
                left + offsetX,
                top,
                left + offsetX + maxWidth + paddingHorizontal * 2,
                lastTop + paddingBottom
        );

        final float dp1 = MeasureUtils.convertDpToPixel(1);
        final float dp2 = MeasureUtils.convertDpToPixel(2);

        shadowRectLevel1.set(
                backgroundRect.left - dp1,
                backgroundRect.top - dp1,
                backgroundRect.right + dp1,
                backgroundRect.bottom + dp1
        );

        shadowRectLevel2.set(
                backgroundRect.left - dp2,
                backgroundRect.top - dp2,
                backgroundRect.right + dp2,
                backgroundRect.bottom + dp2
        );

        if (finalMeasure) {
            return;
        }

        measure(rect, -backgroundRect.width() - MeasureUtils.convertDpToPixel(20), true);
        final float newOffset = MeasureUtils.convertDpToPixel(8);

        if (backgroundRect.left - newOffset <= rect.left) {
            measure(rect, rect.left - left + newOffset, true);
        }
    }

    private void drawBackground(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLACK);
        paint.setAlpha((int) (10f * opacity));
        canvas.drawRoundRect(shadowRectLevel1, radius, radius, paint);

        paint.setColor(Color.BLACK);
        paint.setAlpha((int) (10f * opacity));
        canvas.drawRoundRect(shadowRectLevel2, radius, radius, paint);

        paint.setColor(chartColorPopupColor);
        paint.setAlpha((int) (255f * opacity));
        canvas.drawRoundRect(backgroundRect, radius, radius, paint);

        paint.setStyle(Paint.Style.FILL);
    }

    private void drawTitle(Canvas canvas) {
        paint.setColor(chartColorPopupTitleColor);
        paint.setAlpha((int) (255f * opacity));
        paint.setTextSize(titleTextSize);
        paint.setFakeBoldText(true);
        canvas.drawText(title, titleLeft, titleTop, paint);
        paint.setFakeBoldText(false);
    }

    private void drawItems(Canvas canvas) {
        for (PopupItem item : items) {
            drawItem(canvas, item);
        }
    }

    private void drawItem(Canvas canvas, PopupItem item) {
        paint.setColor(itemTitleColor);
        paint.setAlpha((int) (255f * opacity));
        paint.setTextSize(itemTitleTextSize);
        canvas.drawText(item.title, item.titleLeft, item.titleTop, paint);

        paint.setColor(item.color);
        paint.setAlpha((int) (255f * opacity));
        paint.setTextSize(itemValueTextSize);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(item.value, item.valueLeft, item.valueTop, paint);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    private void drawArrow(Canvas canvas) {
        if (arrowIcon == null)
            return;

        arrowIcon.setBounds(iconLeft, iconTop, iconLeft + iconWidth, iconTop + iconHeight);
        arrowIcon.setAlpha((int) (opacity * 255f));
        arrowIcon.draw(canvas);
    }

    boolean onTouchEvent(MotionEvent event) {
        if (!isVisible) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final float x = event.getX();
            final float y = event.getY();

            if (x > backgroundRect.left && x < backgroundRect.right && y > backgroundRect.top && y < backgroundRect.bottom) {
                if (onClickListener != null) {
                    onClickListener.onPopupClick();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
