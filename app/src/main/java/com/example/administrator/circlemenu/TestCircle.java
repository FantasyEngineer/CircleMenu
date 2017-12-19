package com.example.administrator.circlemenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/12/18.
 */

public class TestCircle extends View {
    public TestCircle(Context context) {
        super(context);
    }

    public TestCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int deviationDegree = 0;
        int sweepAngle = 60;
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        canvas.drawArc(rect, deviationDegree, sweepAngle, true, paint);
    }
}
