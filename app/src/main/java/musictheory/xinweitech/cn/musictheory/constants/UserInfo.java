package musictheory.xinweitech.cn.musictheory.constants;

import android.content.Context;
import android.os.Environment;

/**
 * Created by niudong on 2016/6/28.
 */
public class UserInfo {
    private static String mId = null;
    private static String mUserName = null;
    private static String mPassword = null;



    private static String lastOperateTime = null;
    private static String sip = null;
    private static String sipPassword = null;
    private static String mRealName = null;

    public static String getSipPassword() {
        return sipPassword;
    }

    public static void setSipPassword(String sipPassword) {

        UserInfo.sipPassword = sipPassword;
    }

    public static void setRealName(String mRealName) {
        UserInfo.mRealName = mRealName;
    }

    public static String getRealName() {

        return mRealName;
    }

    public static String getSip() {
        return sip;
    }

    public static void setSip(String sip) {

        UserInfo.sip = sip;
    }


    public static void setLastOperateTime(String lastOperateTime) {
        UserInfo.lastOperateTime = lastOperateTime;
    }

    public static String getLastOperateTime() {
        return lastOperateTime;
    }

    public static void setId(String id) {
        mId = id;
    }

    public static String getId() {
        if (mId == null) {
            return "";
        }
        return mId;
    }

    public static void setUserName(String mUserName) {
        UserInfo.mUserName = mUserName;
    }

    public static String getUserName() {
        return mUserName;
    }

    public static void setPassword(String mPassword) {
        UserInfo.mPassword = mPassword;
    }

    public static String getPassword() {
        return mPassword;
    }

    public static String getUserIconPath(String id, Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + context.getPackageName() + "/userIcon/" + id;
    }
}
