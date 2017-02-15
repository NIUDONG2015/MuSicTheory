package musictheory.xinweitech.cn.musictheory.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by niudong on 2017/2/9.
 */


public class ShapeDrawble extends Drawable {
    private static final int DEFAULT_WIDHT = 0;
    private static final int DEFAULT_HEIGHT = 0;
    private static final int DEFAULT_EDHWIDTH = 5;
    private Paint p;
    private Paint innerP;
    private int width = DEFAULT_WIDHT;//背景宽度   
    private int height = DEFAULT_HEIGHT;//背景高度  
    private int r ;//圆角半径  
    private boolean isEdging = false;//是否带边框  
    private int edgWidth = DEFAULT_EDHWIDTH;//边框厚度   
    public ShapeDrawble(View view, int radius, boolean isEdg){
        initPaint();
        getViewWH(view);
        r=radius;
        isEdging = isEdg;
    }
    private void initPaint(){
        p = new Paint();
        p.setColor(Color.parseColor("#84c3f1"));
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(3);
        innerP = new Paint();
        innerP.setColor(Color.parseColor("#84c3f1"));
        innerP.setStrokeJoin(Paint.Join.ROUND);
        innerP.setStrokeCap(Paint.Cap.ROUND);
        innerP.setStrokeWidth(3);
    }

    boolean hasMeasured = false;
    private void getViewWH(final View v){
        if(null == v){
            return;
        }
        v.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if(!hasMeasured){
                    width = v.getMeasuredWidth();
                    height = v.getMeasuredHeight();
                    hasMeasured = true;
                }
                return true;
            }
        });
    }
    public ShapeDrawble setRadius(int radius){
        this.r = radius;
        invalidateSelf();
        return this;
    }
    public ShapeDrawble setEdgWidth(int width){
        this.edgWidth = width;
        if(isEdging){
            invalidateSelf();
        }
        return this;
    }
    public ShapeDrawble setEdgColor(int color){
        p.setColor(color);
        invalidateSelf();
        return this;
    }
    public ShapeDrawble setInnerColor(int color){
        innerP.setColor(color);
        invalidateSelf();
        return this;
    }
    private void drawRect(Canvas canvas){
        RectF rf = new RectF(0, 0, width, height);

        canvas.drawRoundRect(rf, r, r, p);
    }
    private void drawEdg(Canvas canvas){
        RectF rf = new RectF(edgWidth, edgWidth, width-edgWidth, height-edgWidth);
        canvas.drawRoundRect(rf, r, r, innerP);
    }
    @Override
    public void draw(Canvas canvas) {
        drawRect(canvas);
        if(isEdging){
            drawEdg(canvas);
        }
    }
    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub  
    }
    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub   
    }
    @Override
    public int getOpacity() {
        // TODO Auto-generated method stub  
        return 1;
    }

}
