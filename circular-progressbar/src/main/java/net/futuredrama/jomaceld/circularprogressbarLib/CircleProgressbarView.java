/*
* Copyright 2016 Jose Antonio Maestre Celdran
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.futuredrama.jomaceld.circularprogressbarLib;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A subclass of {@link View} class for creating a circular progressBar.
 *
 */
public class CircleProgressbarView extends View implements ValueAnimator.AnimatorUpdateListener,BarAnimationListener {

    /** The maximum value of the range. The default is 1 */
    private float maximum = 1;
    /** The minimum value of the range. The default is 0. */
    private float minimum = 0;
    /** Angle at witch the progress is 0. -90 => 12 o'clock */
    private int startAngle = -90;
    /** Angle offset. Used mainly for animation purposes*/
    private int startAngleOffset;
    /** Progressbar's background color */
    private int pbBackgroundColor = Color.DKGRAY;
    /** Progressbar's background Thickness */
    private float pbBackgroundThickness = 10;
    /** Default Progressbar's bars Thickness */
    private float pbBarsThickness = 8;
    /** List containing all the bars of type {@link BarComponent} */
    private ArrayList<BarComponent> barComponentsArray;
    /** When set to true, bars will be stacked in the progressbar */
    public boolean bStackBars = true;
    /** When set to true, bars will be stacked in the progressbar */
    public boolean bAntiAlias = true;
    /** Bars stroke cap style */
    private Paint.Cap barCapStyle = Paint.Cap.ROUND;

    private RectF rectF;
    private Paint progressbarBackgroundPaint;
    ObjectAnimator spinsAnimator;

    public CircleProgressbarView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleProgressbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleProgressbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /***
     *  Performs initialization
     *
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    private void init(AttributeSet attrs, int defStyle) {
        barComponentsArray = new ArrayList<>();
        rectF = new RectF();

        final TypedArray tArray = getContext().obtainStyledAttributes(attrs,
                                                    R.styleable.CircleProgressbarView, defStyle, 0);
        // Read XML attributes
        pbBackgroundColor = tArray.getColor(R.styleable.CircleProgressbarView_backgroundColor, pbBackgroundColor);
        pbBackgroundThickness = tArray.getDimension(R.styleable.CircleProgressbarView_backgroundThickness,pbBackgroundThickness);
        pbBarsThickness = tArray.getDimension(R.styleable.CircleProgressbarView_barThickness, pbBarsThickness);
        setStartAngle(tArray.getInt(R.styleable.CircleProgressbarView_startAngle,startAngle));
        setMaximum(tArray.getFloat(R.styleable.CircleProgressbarView_maxValue, maximum));
        setMinimum(tArray.getFloat(R.styleable.CircleProgressbarView_minValue, minimum));
        if(tArray.hasValue(R.styleable.CircleProgressbarView_barCapStyle)) {
            int index = tArray.getInt(R.styleable.CircleProgressbarView_barCapStyle,0);
            setBarStrokeCapStyle(Paint.Cap.values()[index]);
        }

        tArray.recycle();

        // Initialize background paint
        progressbarBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressbarBackgroundPaint.setStyle(Paint.Style.STROKE);
        progressbarBackgroundPaint.setColor(pbBackgroundColor);
        progressbarBackgroundPaint.setStrokeWidth(pbBackgroundThickness);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw Background
        canvas.drawOval(rectF, progressbarBackgroundPaint);
        // Draw bars
        float previousValue = 0;
        for (BarComponent bar : barComponentsArray) {
            float arcStartAngle = startAngle + startAngleOffset + bar.getAngleOffset() + previousValue;
            float arcEndAngle = 360 * bar.getProgressNormalized();

            canvas.drawArc(rectF,arcStartAngle ,arcEndAngle, false, bar.getBarPaint());
            if(bStackBars)
                previousValue += arcEndAngle;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        float maxThickness = pbBackgroundThickness;

        for(BarComponent b : barComponentsArray)
        {
            if(b.getBarThickness() > maxThickness)
                maxThickness = b.getBarThickness();
        }

        rectF.set(0 + maxThickness / 2f, 0 + maxThickness / 2f, min - maxThickness / 2f, min - maxThickness / 2f);
    }

    public void setProgressWithAnimation(float... progress)
    {
        ArrayList<BarComponent> array = getBarComponentsArray();
        for (int i = 0 ; i <  array.size(); i++ ) {
            float barProgress = 0;

            if(i < progress.length) {
                barProgress = progress[i];
            }

            BarComponent bar = array.get(i);
            // Store value
            bar.setValue(barProgress);
            // Store and animate normalized value
            bar.animateProgress(normalize(barProgress,minimum,maximum));
        }
    }

    /**
     * Returns a list containing the bar components
     * @return list containing {@link BarComponent}
     */
    public ArrayList<BarComponent> getBarComponentsArray() {
        return barComponentsArray;
    }

    /**
     * Adds a {@link BarComponent} to the progress bar
     * @param barComponent {@link BarComponent}  to be added
     */
    protected void addBarComponent (BarComponent barComponent)
    {
        barComponent.setBarAnimationListener(this);
        barComponentsArray.add(barComponent);
    }

    public void setNumberOfBars(int barNum)
    {
        ArrayList<BarComponent> barArray = getBarComponentsArray();
        barArray.clear();

        for (int i = 0; i < barNum; i++) {
            BarComponent auxBar = new BarComponent();
            auxBar.setBarThicknessPx(pbBarsThickness);
            auxBar.setStrokeCapStyle(barCapStyle);
            auxBar.setPaintAntiAlias(bAntiAlias);
            addBarComponent(auxBar);
        }
        invalidate();
    }

    /**
     * Set bars cap style
     * @param capStyle
     */
    public void setBarStrokeCapStyle(Paint.Cap capStyle)
    {
        if(barCapStyle == capStyle)
            return;

        barCapStyle = capStyle;
        for(BarComponent b : barComponentsArray) {
            b.setStrokeCapStyle(barCapStyle);
        }
    }

    public void setBarsColors(int[] colors)
    {
        ArrayList<BarComponent> array = getBarComponentsArray();
        for(int i = 0; i< array.size(); i++)
        {
            int colorIndex = i % colors.length;
            // Apply color
            array.get(i).setBarColor(colors[colorIndex]);
        }
    }

    /**
     * Sets the Stroke thickness of all the bars.
     *
     * @param barThickness
     * @param unit The unit to convert from. {@link TypedValue#TYPE_DIMENSION}
     */
    public void setBarsThickness(float barThickness, int unit) {

        float thickness_px = TypedValue.applyDimension(unit, barThickness, getResources().getDisplayMetrics());

        if (thickness_px == pbBarsThickness)
            return;

        pbBarsThickness = thickness_px;

        for(BarComponent bar : barComponentsArray) {
            bar.setBarThicknessPx(pbBarsThickness);
        }

        requestLayout();
    }

    /**
     * Updates the bars normalized value (between 0-1) when
     * a new minimum or maximum value is set.
     */
    private void updateBarsNormalizedProgress()
    {
        for (BarComponent bar : getBarComponentsArray()) {

            float newProgress = normalize(bar.getValue(), minimum, maximum);
            bar.animateProgress(newProgress);
        }
    }

    /**
     * Normalizes a value to a 0-1 range, based on a max(1) and min(0).
     * Any {@value} greater than max will be normalized to 1, and if less than min to 0
     * @param value Value to be normalized
     * @return normalized value in the 0-1 range.
     */
    private float normalize(float value,float min, float max)
    {
        if(value >= max)
            return 1;
        else if(value <= min)
            return  0;

        return  (value - min) / (max -min);
    }

    /**
     * Creates an Animation rotating the progressbar.
     *
     * @param repetitions Number of repetitions of the animation.
     *        {@link ObjectAnimator#INFINITE} for infinite
     * @param repeatMode Sets how the animation repeat mode.
     *        {@link ObjectAnimator#RESTART} or {@link ObjectAnimator#REVERSE}
     * @param duration Duration of the animation in milliseconds
     * @param fromAngle
     * @param toAngle
     */
    public void animateSpins(int repetitions,int repeatMode,int duration,int fromAngle,int toAngle )
    {
        startAngleOffset = fromAngle;
        PropertyValuesHolder ofStartAngle = PropertyValuesHolder.ofInt("startAngleOffset", toAngle);

        if (spinsAnimator == null) {
            spinsAnimator = ObjectAnimator.ofPropertyValuesHolder(this, ofStartAngle);
            spinsAnimator.addUpdateListener(this);
        } else {
            spinsAnimator.cancel();
            spinsAnimator.setValues(ofStartAngle);
        }

        spinsAnimator.setDuration(duration);
        spinsAnimator.setRepeatCount(repetitions);
        spinsAnimator.setRepeatMode(repeatMode);
        spinsAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        spinsAnimator.start();
    }

    public void spinBars(int repetitions,int repeatMode,int duration) {
        if(startAngleOffset >= 360)
            startAngleOffset %= 360;
        animateSpins(repetitions, repeatMode, duration, startAngleOffset, startAngleOffset + 360);
    }

    public void cancelSpin() {
        if(isSpinning()) {
            if(startAngleOffset >= 360)
                startAngleOffset %= 360;

            animateSpins(0,0,500,startAngleOffset,360);
        }
    }

    public boolean isSpinning() {
        return spinsAnimator != null && spinsAnimator.isRunning();
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float max) {
        if(max <= minimum || max == maximum)
            return;

        this.maximum = max;
        updateBarsNormalizedProgress();
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float min) {
        if(min >= maximum || min == minimum)
            return;

        minimum = min;
        updateBarsNormalizedProgress();
    }

    /**
     * @return The Angle at witch progress is 0
     */
    public int getStartAngle() {
        return startAngle;
    }

    /**
     * Sets the Angle at witch progress is 0
     * @param startAngle 12 o'clock is a -90 angle
     */
    public void setStartAngle(int startAngle) {

        this.startAngle = (startAngle % -360);
    }

    public int getStartAngleOffset() {
        return startAngleOffset;
    }

    public void setStartAngleOffset(int startAngleOffset) {
        this.startAngleOffset = startAngleOffset;
    }

    public int getPbBackgroundColor() {
        return pbBackgroundColor;
    }

    public void setPbBackgroundColor(int backgroundColor) {
        this.pbBackgroundColor = backgroundColor;
        this.progressbarBackgroundPaint.setColor(backgroundColor);
    }

    public Paint getProgressbarBackgroundPaint() {
        return progressbarBackgroundPaint;
    }

    public void setProgressbarBackgroundPaint(Paint progressbarBackgroundPaint) {
        this.progressbarBackgroundPaint = progressbarBackgroundPaint;
    }

    public float getPbBackgroundThickness() {
        return pbBackgroundThickness;
    }

    private void setPbBackgroundThickness(float pbBackgroundThickness) {
        this.pbBackgroundThickness = pbBackgroundThickness;
        progressbarBackgroundPaint.setStrokeWidth(pbBackgroundThickness);
        // force re-calculating the layout dimensions
        requestLayout();
    }

    public void setProgressbarBackgroundThickness(int thickness_dp, int unit) {
        Resources r = getResources();
        float thickness_px = TypedValue.applyDimension(unit, thickness_dp, r.getDisplayMetrics());
        this.setPbBackgroundThickness(thickness_px);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    @Override
    public void onBarAnimationUpdate() {
        invalidate();
    }
}