package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;
    private static final float DEFAULT_NEEDLE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        size = Math.min(widthWithoutPadding, heightWithoutPadding);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = Math.min(getWidth(), getHeight());

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

        postInvalidateDelayed(1000);
    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        //textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.09f);
        textPaint.setColor(hoursValuesColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        String hourValue;
        float top, bottom;
        float centerX, centerY;
        float baseline;

        top = fontMetrics.top;
        bottom = fontMetrics.bottom;

        hourValue = "12";
        centerX = (float) mCenterX;
        centerY = (float) (mCenterY - (int) (mWidth * 0.38f));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "06";
        centerX = (float) mCenterX;
        centerY = (float) (mCenterY + (int) (mWidth * 0.38f));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "03";
        centerX = (float) (mCenterY + (int) (mWidth * 0.38f));
        centerY = (float) mCenterY;
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "09";
        centerX = (float) (mCenterY - (int) (mWidth * 0.38f));
        centerY = (float) mCenterY;
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        int radius = (int) (mWidth * 0.38f);

        hourValue = "01";
        centerX = (float) (mCenterX + radius * Math.sin(Math.toRadians(30)));
        centerY = (float) (mCenterY - radius * Math.cos(Math.toRadians(30)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "05";
        centerX = (float) (mCenterX + radius * Math.sin(Math.toRadians(30)));
        centerY = (float) (mCenterY + radius * Math.cos(Math.toRadians(30)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "07";
        centerX = (float) (mCenterX - radius * Math.sin(Math.toRadians(30)));
        centerY = (float) (mCenterY + radius * Math.cos(Math.toRadians(30)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "11";
        centerX = (float) (mCenterX - radius * Math.sin(Math.toRadians(30)));
        centerY = (float) (mCenterY - radius * Math.cos(Math.toRadians(30)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "02";
        centerX = (float) (mCenterX + radius * Math.sin(Math.toRadians(60)));
        centerY = (float) (mCenterY - radius * Math.cos(Math.toRadians(60)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "04";
        centerX = (float) (mCenterX + radius * Math.sin(Math.toRadians(60)));
        centerY = (float) (mCenterY + radius * Math.cos(Math.toRadians(60)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "08";
        centerX = (float) (mCenterX - radius * Math.sin(Math.toRadians(60)));
        centerY = (float) (mCenterY + radius * Math.cos(Math.toRadians(60)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

        hourValue = "10";
        centerX = (float) (mCenterX - radius * Math.sin(Math.toRadians(60)));
        centerY = (float) (mCenterY - radius * Math.cos(Math.toRadians(60)));
        baseline = (centerY + ((bottom - top) / 2 - bottom));
        canvas.drawText(hourValue, centerX, baseline, textPaint);

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
        int rPadded, rEnd;
        int startX, startY;
        int stopX, stopY;

        Paint paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        //rPadded = mCenterX;
        rEnd = mCenterX - (int) (mWidth * 0.20f);
        startX =  mCenterX;
        startY =  mCenterX;
        second = (second - 15 + 60) % 60;
        stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(second * 6)));
        stopY = (int) (mCenterY + rEnd * Math.sin(Math.toRadians(second * 6)));
        paint.setStrokeWidth(mWidth * DEFAULT_NEEDLE_STROKE_WIDTH * 0.8f);
        paint.setColor(secondsNeedleColor);
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        rEnd = mCenterX - (int) (mWidth * 0.24f);
        startX =  mCenterX;
        startY =  mCenterY;
        minute = (minute - 15 + 60) % 60;
        stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(minute * 6)));
        stopY = (int) (mCenterY + rEnd * Math.sin(Math.toRadians(minute * 6)));
        paint.setStrokeWidth(mWidth * DEFAULT_NEEDLE_STROKE_WIDTH * 1.5f);
        paint.setColor(minutesNeedleColor);
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        rEnd = mCenterX - (int) (mWidth * 0.30f);
        startX =  mCenterX;
        startY =  mCenterY;
        if (amPm == AM) {
            hour %= 12;
        }
        hour = (hour - 3 + 12) % 12;
        stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(hour * 30)));
        stopY = (int) (mCenterY + rEnd * Math.sin(Math.toRadians(hour * 30)));
        paint.setStrokeWidth(mWidth * DEFAULT_NEEDLE_STROKE_WIDTH * 1.8f);
        paint.setColor(hoursNeedleColor);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
        Paint paint = new Paint();
        paint.setStrokeWidth(mWidth * 0.01f);

        paint.setColor(centerOuterColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterX, mRadius *  0.01f, paint);

        paint.setColor(centerInnerColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX, mCenterY, mRadius * 0.01f, paint);

    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

}