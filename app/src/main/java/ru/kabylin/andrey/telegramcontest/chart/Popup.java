package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.*;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.List;

final class Popup {
    float stateOpacity = 0f;
    float opacity = 0f;
    int left = 0;
    private int top = 0;
    private final List<PopupItem> items = new ArrayList<>();
    private String title = "Sat, Feb 24";
    boolean isVisible = false;

    private OnPopupEventsListener eventsListener = null;
    private PopupOnClickListener onClickListener = null;

    void setOnPopupEventsListener(OnPopupEventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }

    void setPopupOnClickListener(PopupOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    Popup() {
        items.add(new PopupItem(Color.GREEN, "122", "#0"));
        items.add(new PopupItem(Color.RED, "67", "#1"));
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
    private final RectF popupRect = new RectF();
    private final RectF shadowRectLevel1 = new RectF();
    private final RectF shadowRectLevel2 = new RectF();
    private final float radius = MeasureUtils.convertDpToPixel(3);
    private float titleLeft = 0f;
    private float titleTop = 0f;
    private final Rect titleBounds = new Rect();

    private final float titleTextSize = MeasureUtils.convertDpToPixel(16);
    private final float itemTitleTextSize = MeasureUtils.convertDpToPixel(14);
    private final float itemValueTextSize = MeasureUtils.convertDpToPixel(18);

    private final float itemValueTopMargin = MeasureUtils.convertDpToPixel(16);
    private final float itemTitleTopMargin = MeasureUtils.convertDpToPixel(4);
    private final float itemRightMargin = MeasureUtils.convertDpToPixel(16);

    int chartColorPopupColor = Color.WHITE;
    int chartColorPopupTitleColor = Color.BLACK;

    void draw(Canvas canvas, Rect rect) {
        measure(rect, MeasureUtils.convertDpToPixel(20), false);

        drawBackground(canvas);
        drawTitle(canvas);
        drawItems(canvas);
    }

    private void measure(Rect rect, float offsetX, boolean finalMeasure) {
        // Avoid deep recursion
        if (finalMeasure) {
            return;
        }

        final float dp1 = MeasureUtils.convertDpToPixel(1);
        final float dp2 = MeasureUtils.convertDpToPixel(2);
        final float dp4 = MeasureUtils.convertDpToPixel(4);
        final float dp8 = MeasureUtils.convertDpToPixel(8);
        final float dp16 = MeasureUtils.convertDpToPixel(16);

        paint.setTextSize(titleTextSize);
        paint.getTextBounds(title, 0, title.length(), titleBounds);

//        float offsetX = MeasureUtils.convertDpToPixel(20);
        final float popupWidth = titleBounds.width();

        popupRect.set(
                left - offsetX,
                top + dp1,
                left - offsetX + popupWidth + dp16,
                top
        );

        shadowRectLevel1.set(
                left - dp1 - offsetX,
                top,
                left - offsetX + popupWidth + dp16 + dp1,
                top + dp1
        );

        shadowRectLevel2.set(
                left - dp1 - offsetX,
                top + dp1,
                left - offsetX + popupWidth + dp16 + dp1,
                top + dp2
        );

        titleLeft = popupRect.left + dp8;
        titleTop = top + titleBounds.height() + dp4;

        float top = 0;
        float bottom = 0;

        float lastItemLeft = popupRect.left + dp8;

        for (PopupItem item : items) {
            paint.setTextSize(itemValueTextSize);
            paint.getTextBounds(item.value, 0, item.value.length(), item.valueBounds);

            paint.setTextSize(itemTitleTextSize);
            paint.getTextBounds(item.title, 0, item.title.length(), item.titleBounds);
        }

        //
        for (PopupItem item : items) {
            // Value
            item.valueLeft = lastItemLeft;
            item.valueTop = titleTop + item.valueBounds.height() + itemValueTopMargin;

            // Title
            item.titleLeft = lastItemLeft;
            item.titleTop = item.valueTop + item.titleBounds.height() + itemTitleTopMargin;

            //
            final float width = Math.max(item.valueBounds.width(), item.titleBounds.width());
            lastItemLeft += width + itemRightMargin;

            bottom = item.titleTop + item.titleBounds.height();
        }

        popupRect.bottom = bottom;
        popupRect.right = Math.max(popupRect.right, lastItemLeft - itemRightMargin + dp8);

        shadowRectLevel1.bottom = bottom + dp1;
        shadowRectLevel1.right = Math.max(shadowRectLevel1.right, lastItemLeft - itemRightMargin + dp8 + dp1);

        shadowRectLevel2.bottom = bottom + dp2;
        shadowRectLevel2.right = Math.max(shadowRectLevel1.right, lastItemLeft - itemRightMargin + dp8 + dp1);

        if (popupRect.left <= rect.left) {
            final float newOffset = -MeasureUtils.convertDpToPixel(10);

            if (left - newOffset > rect.left) {
                measure(rect, newOffset, true);
            }
        }

        if (popupRect.right >= rect.right) {
            final float newOffset = popupRect.right - rect.right + MeasureUtils.convertDpToPixel(30);

            if (left - newOffset + popupRect.width() < rect.right) {
                measure(rect, newOffset, true);
            }
            else {
                measure(rect, popupRect.width() + MeasureUtils.convertDpToPixel(10), true);
            }
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
        canvas.drawRoundRect(popupRect, radius, radius, paint);

        paint.setStyle(Paint.Style.FILL);
    }

    private void drawTitle(Canvas canvas) {
        paint.setColor(chartColorPopupTitleColor);
        paint.setAlpha((int) (255f * opacity));
        paint.setTextSize(titleTextSize);

        canvas.drawText(title, titleLeft, titleTop, paint);
    }

    private void drawItems(Canvas canvas) {
        for (PopupItem item : items) {
            drawItem(canvas, item);
        }
    }

    private void drawItem(Canvas canvas, PopupItem item) {
        paint.setColor(item.color);
        paint.setAlpha((int) (255f * opacity));
        paint.setTextSize(itemValueTextSize);
        canvas.drawText(item.value, item.valueLeft, item.valueTop, paint);

        paint.setTextSize(itemTitleTextSize);
        canvas.drawText(item.title, item.titleLeft, item.titleTop, paint);
    }

    boolean onTouchEvent(MotionEvent event) {
        if (!isVisible) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final float x = event.getX();
            final float y = event.getY();

            if (x > popupRect.left && x < popupRect.right && y > popupRect.top && y < popupRect.bottom) {
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
