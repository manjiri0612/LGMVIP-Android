package com.manjiri.facedetection.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


public class RectOverlay extends GraphicOverlay.Graphic{
    private int nRectColor= Color.GREEN;
    private float StrokeWidth =4.0f;
    private Paint mRectPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;
    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);
        mRectPaint=new Paint();
        mRectPaint.setColor(nRectColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(StrokeWidth);
        this.graphicOverlay=graphicOverlay;
        this.rect=rect;
        postInvalidate();


    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF=new RectF(rect);
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.top=translateX(rectF.top);
        rectF.bottom=translateX(rectF.bottom);
        canvas.drawRect(rectF,mRectPaint);




    }
}
