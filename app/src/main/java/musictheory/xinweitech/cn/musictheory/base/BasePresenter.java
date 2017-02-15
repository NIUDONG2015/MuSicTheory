package musictheory.xinweitech.cn.musictheory.base;

/**
 * Created by Mr.ZCM on 2016/5/12.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:定义共有的方法
 */
public interface BasePresenter<T>{
    void attachView(T view);
    void detachView();
}
