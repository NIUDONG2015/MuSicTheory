package musictheory.xinweitech.cn.musictheory.utils;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.Button;

/**
 * Created by niudong on 2017/2/9.
 */


public class MakeShapes {
    /**
     * @param button
     * @param color
     */
    public static void setTint(@NonNull Button button, @ColorInt int color) {
        ColorStateList s1 = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setBackgroundTintList(s1);
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (button.getBackground() != null)
                button.getBackground().setColorFilter(color, mode);
        }
    }
/*
    public void makeShape() {
        ShapeDrawable    activeDrawable = new ShapeDrawable();
        inactiveDrawable = new ShapeDrawable();
        activeDrawable.setBounds(0, 0, (int) mIndicatorSize,
                (int) mIndicatorSize);
        inactiveDrawable.setBounds(0, 0, (int) mIndicatorSize,
                (int) mIndicatorSize);

        int i[] = new int[2];
        i[0] = android.R.attr.textColorSecondary;
        i[1] = android.R.attr.textColorSecondaryInverse;
        TypedArray a = this.getTheme().obtainStyledAttributes(i);

        Shape s1 = new OvalShape();
        s1.resize(mIndicatorSize, mIndicatorSize);
        Shape s2 = new OvalShape();
        s2.resize(mIndicatorSize, mIndicatorSize);

        ((ShapeDrawable) activeDrawable).getPaint().setColor(
                a.getColor(0, Color.DKGRAY));
        ((ShapeDrawable) inactiveDrawable).getPaint().setColor(
                a.getColor(1, Color.LTGRAY));

        ((ShapeDrawable) activeDrawable).setShape(s1);
        ((ShapeDrawable) inactiveDrawable).setShape(s2);
    }*/
}