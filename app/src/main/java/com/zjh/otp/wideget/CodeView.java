package com.zjh.otp.wideget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjh.otp.app.R;


public class CodeView extends View {

    private Paint mPaint;
    private Paint mCirclePaint;
    private Rect rect;
    private String text;
    private boolean showText;

    public CodeView(Context context) {
        this(context, null);
    }

    public CodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(getContext().getColor(R.color.code_text));
        mPaint.setTextSize(sp2px(getContext(), 32));
        mPaint.setFakeBoldText(true);
        mPaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#1296db"));
        mCirclePaint.setAntiAlias(true);

        rect = new Rect();

        showText = true;
    }

    public void setTextSie(float value){
        mPaint.setTextSize(sp2px(getContext(), value));
    }

    public void setTextColor(int color){
        mPaint.setColor(color);
    }

    public void setText(String text){
        this.text = text;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == text || "".equals(text)){
            return;
        }
        char[] textArray = text.toCharArray();
        int length = textArray.length;
        float startX = 0;
        float defaultWidth = dip2px(getContext(), 18);
        for (int i = 0; i < length; i++){
            String str = String.valueOf(textArray[i]);
            mPaint.getTextBounds(str, 0, str.length(), rect);
            int width = rect.width();
            int height = rect.height();
            if (showText){
                float x = startX + defaultWidth / 2 - (float) width / 2;
                float y = (float) getHeight() / 2 + (float) height / 2;
                canvas.drawText(str, x, y, mPaint);
            }else {
                float cx = startX + defaultWidth / 2;
                float cy = (float) getHeight() / 2;
                canvas.drawCircle(cx, cy, 12, mCirclePaint);
            }
            if ((length == 6 && i == 2)
                    || (length == 8 && i == 3)){
                startX = startX + defaultWidth + dip2px(getContext(), 16);
            } else {
                startX = startX + defaultWidth + dip2px(getContext(), 6);
            }
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context, float value) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value * scaledDensity + 0.5f);
    }
}
