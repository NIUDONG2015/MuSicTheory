package musictheory.xinweitech.cn.musictheory.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import musictheory.xinweitech.cn.musictheory.R;

/**
 * NiuDong
 * 当加载网络图片时，由于加载过程中图片未能及时显示，此时可能需要设置等待时的图片，通过placeHolder()方法
 * //centerCrop()：图片裁剪，ImageView 可能会完全填充，但图像可能不会完整显示。
 * glide加载图片工具类
 * .dontAnimate() //没有淡入淡出效果
 * .crossFade() //淡入淡出效果默认持续时间300,版本3.6.1默认激活的
 * .fitCenter() //缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。该图像将会完全显示，但可能不会填满整个 ImageView。
 * .CenterCrop() //缩放图像让它填充到 ImageView 界限内并且裁剪额外的部分。ImageView 可能会完全填充，但图像可能不会完整显示。
 */
public class GlideUtils {

    //从资源中加载图片
    public static void glidFormRes(Context context, int resId, ImageView iv) {
        Glide.with(context).load(resId).into(iv);

    }

    //从网络加载图片,项目中头像加载方法
    public static void glidFormUrltoTx(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
                .error(R.drawable.home_note_ico01)
                .override(50, 50)
                .fitCenter()
                .dontAnimate()

                .into(iv);
    }

    //从网络加载图片,绕过内存缓存和磁盘缓存
    public static void glidFormUrlnoCache(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.ic_delete).error(R.drawable.ic_drawer_home)
                .dontAnimate()
                .fitCenter()
                .skipMemoryCache(true)  //内存不缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) //磁盘不缓存
                .into(iv);
    }

    //从网络加载图片做缓存（社区模块大图片加载）
    public static void glidFormUrltoCache(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
//                .placeholder(R.drawable.test1).error(R.drawable.test1)
                .dontAnimate() //没有淡入淡出效果
                .override(640, 240)
                .centerCrop()
                .fitCenter()
//                .thumbnail(0.9f)
//                .crossFade() //淡入淡出效果默认持续时间300,版本3.6.1默认激活的

                .into(iv);
    }


    //从文件中加载图片
    public static void glidFormFile(Context context, File file, ImageView iv) {
        Glide.with(context).load(file).into(iv);
    }

    //从Uri中加载图片
    public static void glidFormUrii(Context context, Uri uri, ImageView iv) {
        Glide.with(context).load(uri).into(iv);
    }

    public static void setImage(Context context, String url, int errorPic,
                                int loadingPic, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().centerCrop()
                .placeholder(loadingPic).error(errorPic)
                .into(new MyBitmapImageViewTarget(imageView));
    }

}
