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
import android.graphics.Paint;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class BarComponent implements ValueAnimator.AnimatorUpdateListener{

    private float progress;
    private float barThickness;
    private int barColor;
    private float angleOffset = 0;

    private Paint barPaint;

    BarAnimationListener mListener;

    /** Default constructor */
    public BarComponent()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setBarPaint(paint);

        setBarThicknessPx(4);
        setProgress(0);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
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
    public void animateProgress(float p)
    {
        PropertyValuesHolder valueHolder = PropertyValuesHolder.ofFloat("progress", p);

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

}
