package musictheory.xinweitech.cn.musictheory.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by niudong on 2016/12/30.
 */

public interface NetConstants {
    String OUTSIDE = "outside";
    String EVENTTYPE_REGISTER = "mobile.user.register";
    String EVENTTYPE_LOGIN = "mobile.user.login";
    String EVENTTYPE_FORGET = "mobile.user.password.forget";
    String EVENTTYPE_UPDATE_PWD = "mobile.user.password.update";
    String EVENTTYPE_CHECK_EMAIL = "mobile.email.check";
    String EVENTTYPE_LESSON_LIST = "mobile.user.music.lesson.list";
    String EVENTTYPE_LESSON_DETAIL = "mobile.user.music.lesson.detail";
    String EVENTTYPE_LESSON_STATUS = "mobile.user.music.lesson.status.update";
    String EVENTTYPE_ADVERT = "mobile.advert.list";
    String URER_INFO = "mobile.user.info.detail";
    String LANG_ITEM_LIST = "mobile.item.list";


    //字典数据
    String modelName = "sys";
    //H5


    String ABOUT_US_EN = "http://test.teacherfamily.net:8024/mbc/aboutUs.html?lang=en";
    String ABOUT_ZH = "http://test.teacherfamily.net:8024/mbc/aboutUs.html?lang=zh";
    String REG_PROTOCOL_ZH = "http://test.teacherfamily.net:8024/mbc/protocol.html?lang=zh";
    String REG_PROTOCOL_EN = "http://test.teacherfamily.net:8024/mbc/protocol.html?lang=en";


    // v=V0&lang=zh&mediaSource=1
    int MEDIA_SOURCE = 1;
    String MULTIL_LANG = "lang";
    String LANG = "zh";
    String EN = "en";
    String V = "V0";
    String zh_CN = "zh_CN";

    String APP_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "ifitscale" + File.separator;

    String CRASH_LOG_PATH = APP_PATH + "crash" + File.separator;
    String LOG_PATH = APP_PATH + "log" + File.separator;

    String DOWNLOAD_PATH = APP_PATH + "download" + File.separator;

}
