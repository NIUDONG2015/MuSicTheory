package musictheory.xinweitech.cn.musictheory.utils;

import com.google.gson.Gson;

/**
 * Created by yangjinxiao on 2016/7/4.
 */
public class JsonUtil {
    private static final Gson mGson = new Gson();
    public static String encode(Object o) {
        if (o == null) {
            return "";
        }
        return mGson.toJson(o);
    }
    public static final Object decode(String jsonStr, Class clazz) {
        if (jsonStr instanceof String) {
            return mGson.fromJson(jsonStr, clazz);
        }
        return null;
    }
}
