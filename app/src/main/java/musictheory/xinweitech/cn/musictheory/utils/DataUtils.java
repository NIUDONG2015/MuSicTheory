package musictheory.xinweitech.cn.musictheory.utils;

import android.content.Context;

import com.google.gson.Gson;

import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.LessonListEntity;
import musictheory.xinweitech.cn.musictheory.entity.LessonListReq;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by niudong on 2017/2/8.
 */


public class DataUtils {


    private static LessonListEntity.DataBean data;

    public static LessonListEntity.DataBean startHome(Context mContext) {

        Gson gson = new Gson();
        //用一个bean来传递数据
        //从缓存中取数据
        String userNo = (String) SharedPreferencesUtil.getData(mContext, "userNo", "");
        LessonListReq lessonListReq = new LessonListReq();
        lessonListReq.setUserNo(userNo);
        lessonListReq.setStart(0);
        lessonListReq.setLimit(40);
        String json = gson.toJson(lessonListReq);
        MusicApplication.apiService.lessonList(NetConstants.EVENTTYPE_LESSON_LIST, json, NetConstants.V, NetConstants.LANG, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<LessonListEntity>() {
                    @Override
                    public void onNext(LessonListEntity lessonListEntity) {
                        data = lessonListEntity.getData();

                    }
                });
        return data;
    }
}


/*       if (lessonListEntities.getData() != null) {
                            LessonListEntity.DataBean data = lessonListEntities.getData();
                            HomeActivity.actionStart(mContext, data);
                        }*/