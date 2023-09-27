package com.zjh.otp.wideget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zjh.otp.app.R;


public class CountDownPie extends View {

    private final Paint piePaint = new Paint();
    private CountDownListener listener;
    private int color;
    private final int innerColor;
    private final int normalColor;

    public CountDownPie(Context context) {
        this(context, null);
    }

    public CountDownPie(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownPie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        color = 0xff396aff;
        innerColor = getContext().getColor(R.color.code_cunt_down_inner);
        normalColor = getContext().getColor(R.color.code_cunt_down_normal);
        piePaint.setAntiAlias(true);

    }

    public void setListener(CountDownListener listener) {
        this.listener = listener;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        piePaint.setColor(color);
        float degree = listener == null ? 90 : listener.getDegree();
        canvas.drawArc(0, 0, getWidth(), getHeight(), 270-degree, degree, true, piePaint);

        piePaint.setColor(normalColor);
        canvas.drawArc(0, 0, getWidth(), getHeight(), -90, 360-degree, true, piePaint);

        float radius = (float) Math.min(getWidth(), getHeight()) / 2;
        piePaint.setColor(innerColor);
        float innerCircle = radius  - 6;
        canvas.drawCircle((float) getWidth()/2, (float) getHeight()/2, innerCircle, piePaint);
    }
}
