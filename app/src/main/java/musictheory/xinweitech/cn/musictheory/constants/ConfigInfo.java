package musictheory.xinweitech.cn.musictheory.constants;

/**
 * Created by niudong on 2017/1/11.
 */

public class ConfigInfo {
        private static String HOST = "http://test.teacherfamily.net:8024/";
//    private static String HOST = "http://115.28.14.122/";

    public static void setHOST(String HOST) {
        ConfigInfo.HOST = HOST;
    }

    public static String getHOST() {
        return HOST;
    }
}
