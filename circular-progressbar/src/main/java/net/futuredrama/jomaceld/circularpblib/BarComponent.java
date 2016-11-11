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
package net.futuredrama.jomaceld.circularpblib;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class BarComponent implements ValueAnimator.AnimatorUpdateListener{

    BarAnimationListener mListener;
    /** Bar value */
    private float value;
    /** Stores the normalized (0-1 range) bar value based on progressbar maximum and minimum */
    private float progressNormalized;
    private float barThickness;
    private int barColor;
    private float angleOffset = 0;
    private Paint barPaint;

    /** Default constructor */
    public BarComponent(){
        // Initialize Paint with defaults
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        setBarPaint(paint);
        setValue(0);
    }

    public void animateProgress(float p)
    {
        PropertyValuesHolder valueHolder = PropertyValuesHolder.ofFloat("progressNormalized", p);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setStartDelay(0);
        animator.addUpdateListener(this);

        animator.start();
    }

    public void animateAngleOffset(float p,int duration)
    {
        PropertyValuesHolder progressVH = PropertyValuesHolder.ofFloat("angleOffset", p);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, progressVH);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setStartDelay(0);
        animator.addUpdateListener(this);

        animator.start();
    }

    public void setStrokeCapStyle(Paint.Cap capStyle)
    {
        barPaint.setStrokeCap(capStyle);
    }

    public void setPaintAntiAlias(boolean aliasing)
    {
        barPaint.setAntiAlias(aliasing);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getBarThickness() {
        return barThickness;
    }

    public void setBarThicknessPx(float barThickness) {
        this.barThickness = barThickness;
        barPaint.setStrokeWidth(barThickness);
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        barPaint.setColor(barColor);
    }

    public Paint getBarPaint() {
        return barPaint;
    }

    public void setBarPaint(Paint barPaint) {
        this.barPaint = barPaint;
    }

    public float getAngleOffset() {
        return angleOffset;
    }

    public void setAngleOffset(float startAngleOffset) {
        this.angleOffset = startAngleOffset;
    }

    public float getProgressNormalized() {
        return progressNormalized;
    }

    public void setProgressNormalized(float progressNormalized) {
        this.progressNormalized = progressNormalized;
    }

    public void setBarAnimationListener(BarAnimationListener l)
    {
        mListener = l;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(mListener != null)
            mListener.onBarAnimationUpdate();
        else
            Log.w("BarComponent", "Warning: Bar hasn't been added to a ProgressBar");
    }
}
