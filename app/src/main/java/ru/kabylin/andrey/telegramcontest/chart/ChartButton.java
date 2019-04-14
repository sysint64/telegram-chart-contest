package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

final class ChartButton {
    int left = 0;
    int top = 0;
    String title = "Test";
    int color = Color.BLACK;
    int textColor = Color.WHITE;
    Drawable checkIcon;
    float textSize = MeasureUtils.convertDpToPixel(16);
    int width = 0;
    int height = (int) MeasureUtils.convertDpToPixel(32);
    private final Rect textBounds = new Rect();
    private final RectF backgroundRect = new RectF();
    private final RectF borderRect = new RectF();
    boolean isChecked = true;
    ChartButtonOnClickListener onClickListener = null;

    private int iconLeft = 0;
    private int iconTop = 0;
    private int textLeft = 0;
    private int textTop = 0;
    private int iconWidth = (int) MeasureUtils.convertDpToPixel(18);
    private int iconHeight = (int) MeasureUtils.convertDpToPixel(20);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float backgroundCornerRadius = MeasureUtils.convertDpToPixel(24);
    private final float paddingLeft = MeasureUtils.convertDpToPixel(8);
    private final float paddingRight = MeasureUtils.convertDpToPixel(16);
    private final float textMarginLeft = MeasureUtils.convertDpToPixel(4);
    private final float textMarginTop = MeasureUtils.convertDpToPixel(6);
    private final float strokeSize = MeasureUtils.convertDpToPixel(2);

    private float checkedTextOpacity = 0f;
    private float textOpacity = 1f;
    private float checkedTextOpacityState = 0f;
    private float textOpacityState = 1f;
    private float backgroundOpacity = 1f;
    private float backgroundOpacityState = 1f;
    private float iconOpacity = 1f;
    private float iconOpacityState = 1f;
    private float iconOffset = 0f;
    private float iconOffsetState = 0f;
    private float textLeftOffset = 0;
    private float textLeftOffsetState = 0;

    private static final float textOpacityChangeSpeed = 100f;
    private static final float backgroundOpacityChangeSpeed = 150f;
    private static final float iconOpacityChangeSpeed = 150f;
    private static final float iconOffsetChangeSpeed = 150f;
    private static final float textLeftOffsetChangeSpeed = 150f;

    void onDraw(Canvas canvas) {
        measure();

        drawBackground(canvas);
        drawBorder(canvas);
        drawIcon(canvas);
        drawText(canvas);
        drawUncheckedText(canvas);
    }

    void onProgress(float deltaTime) {
        textOpacity = MathUtils.interpTo(
                textOpacity,
                textOpacityState,
                deltaTime,
                textOpacityChangeSpeed
        );

        checkedTextOpacity = MathUtils.interpTo(
                checkedTextOpacity,
                checkedTextOpacityState,
                deltaTime,
                textOpacityChangeSpeed
        );

        backgroundOpacity = MathUtils.interpTo(
                backgroundOpacity,
                backgroundOpacityState,
                deltaTime,
                backgroundOpacityChangeSpeed
        );

        iconOpacity = MathUtils.interpTo(
                iconOpacity,
                iconOpacityState,
                deltaTime,
                iconOpacityChangeSpeed
        );

        iconOffset = MathUtils.interpTo(
                iconOffset,
                iconOffsetState,
                deltaTime,
                iconOffsetChangeSpeed
        );

        textLeftOffset = MathUtils.interpTo(
                textLeftOffset,
                textLeftOffsetState,
                deltaTime,
                textLeftOffsetChangeSpeed
        );
    }

    private void drawBackground(Canvas canvas) {
        paint.setColor(color);
        paint.setAlpha((int) (backgroundOpacity * 255f));
        canvas.drawRoundRect(backgroundRect, backgroundCornerRadius, backgroundCornerRadius, paint);
    }

    private void drawBorder(Canvas canvas) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeSize);
        canvas.drawRoundRect(borderRect, backgroundCornerRadius, backgroundCornerRadius, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawIcon(Canvas canvas) {
        checkIcon.setBounds(iconLeft, iconTop, iconLeft + iconWidth, iconTop + iconHeight);
        checkIcon.setAlpha((int) (iconOpacity * 255f));
        checkIcon.draw(canvas);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(textColor);
        paint.setAlpha((int) (textOpacity * 255f));
        paint.setFakeBoldText(false);
        canvas.drawText(title, textLeft + textLeftOffset, textTop, paint);
    }

    private void drawUncheckedText(Canvas canvas) {
        paint.setColor(color);
        paint.setAlpha((int) (checkedTextOpacity * 255f));
        paint.setFakeBoldText(true);
        canvas.drawText(title, textLeft + textLeftOffset, textTop, paint);
    }

    private void measure() {
        paint.setTextSize(textSize);
        paint.getTextBounds(title, 0, title.length(), textBounds);

        textLeft = (int) (left + paddingLeft + iconWidth + textMarginLeft);
        textTop = top + height / 2 + (int) textMarginTop;

        if (!isChecked) {
            checkedTextOpacityState = 1f;
            textOpacityState = 0f;
            backgroundOpacityState = 0f;
            iconOpacityState = 0f;
            iconOffsetState = iconWidth;
            textLeftOffsetState = -iconWidth - paddingLeft - textMarginLeft + (width - textBounds.width()) / 2f;
        } else {
            checkedTextOpacityState = 0f;
            textOpacityState = 1f;
            backgroundOpacityState = 1f;
            iconOpacityState = 1f;
            iconOffsetState = 0f;
            textLeftOffsetState = 0f;
        }

        backgroundRect.set(
                (float) left,
                (float) top,
                textLeft + textBounds.width() + paddingRight,
                top + height
        );

        borderRect.set(backgroundRect);
        borderRect.left += strokeSize / 2;
        borderRect.right -= strokeSize / 2;
        borderRect.top += strokeSize / 2;
        borderRect.bottom -= strokeSize / 2;

        width = (int) backgroundRect.width();

        iconLeft = left + (int) paddingLeft + (int) iconOffset;
        iconTop = top + (height - iconHeight) / 2;
    }

    boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final float x = event.getX();
            final float y = event.getY();

            if (x > left && x < left + width && y > top && y < top + height) {
                isChecked = !isChecked;
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
