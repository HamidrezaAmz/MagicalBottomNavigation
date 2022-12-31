package com.hamidrezaamz.magicalbottomnavigationview.helper;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MirroredDrawable extends Drawable {

    final Drawable mDrawable;
    final Matrix matrix = new Matrix();

    public MirroredDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        matrix.reset();
        matrix.preScale(-1.0f, 1.0f, getBounds().width() / 2, getBounds().height() / 2);
        canvas.setMatrix(matrix);

        Rect drawingRect = new Rect();
        drawingRect.left = (getBounds().width() - mDrawable.getIntrinsicWidth()) / 2;
        drawingRect.top = (getBounds().height() - mDrawable.getIntrinsicHeight()) / 2;
        drawingRect.right = drawingRect.left + mDrawable.getIntrinsicWidth();
        drawingRect.bottom = drawingRect.top + mDrawable.getIntrinsicHeight();
        mDrawable.setBounds(drawingRect);
        mDrawable.draw(canvas);
    }

    // Other methods required to extend Drawable but aren't used here.

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}