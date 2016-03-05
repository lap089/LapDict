package com.lapdict.lap089.lapdict;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by user on 6/6/2015.
 */public class ListView3d extends ListView {

    /** Ambient light intensity */
    private static final int AMBIENT_LIGHT = 55;
    /** Diffuse light intensity */
    private static final int DIFFUSE_LIGHT = 200;
    /** Specular light intensity */
    private static final float SPECULAR_LIGHT = 70;
    /** Shininess constant */
    private static final float SHININESS = 200;
    /** The max intensity of the light */
    private static final int MAX_INTENSITY = 0xFF;
    /** Amount of down scaling */
    private static final float SCALE_DOWN_FACTOR = 0.15f;

    private Camera mCamera;
    private Matrix mMatrix;
    /** Paint object to draw with */
    private Paint mPaint;

    public ListView3d(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        // get top left coordinates
        int left = child.getLeft();
        int top = child.getTop();

        Bitmap bitmap = child.getDrawingCache();
        if (bitmap == null) {
            child.setDrawingCacheEnabled(true);
            child.buildDrawingCache();
            bitmap = child.getDrawingCache();
        }

        // get offset to center
        int centerX = child.getWidth() / 2;
        int centerY = child.getHeight() / 2;

        // get absolute center of child
        float pivotX = left + centerX;
        float pivotY = top + centerY;

        // calculate distance from center
        float centerScreen = getHeight() / 2;
        float distFromCenter = (pivotY - centerScreen) / centerScreen;

        // calculate scale and rotation
        float scale = (float)(1 - SCALE_DOWN_FACTOR *(1 - Math.cos(distFromCenter)));
        float rotation = 30 * distFromCenter;

        if (mCamera == null) {
            mCamera = new Camera();
        }
        mCamera.save();
        mCamera.rotateY(rotation);

        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
        }
        mPaint.setColorFilter(calculateLight((float) rotation));

        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postScale(scale, scale);
        mMatrix.postTranslate(pivotX, pivotY);

        canvas.drawBitmap(bitmap, mMatrix, mPaint);
        return false;
    }

    private LightingColorFilter calculateLight(final float rotation) {
        final double cosRotation = Math.cos(Math.PI * rotation / 180);
        int intensity = AMBIENT_LIGHT + (int) (DIFFUSE_LIGHT * cosRotation);
        int highlightIntensity = (int) (SPECULAR_LIGHT * Math.pow(cosRotation, SHININESS));
        if (intensity > MAX_INTENSITY) {
            intensity = MAX_INTENSITY;
        }
        if (highlightIntensity > MAX_INTENSITY) {
            highlightIntensity = MAX_INTENSITY;
        }
        final int light = Color.rgb(intensity, intensity, intensity);
        final int highlight = Color.rgb(highlightIntensity, highlightIntensity, highlightIntensity);
        return new LightingColorFilter(light, highlight);
    }
}