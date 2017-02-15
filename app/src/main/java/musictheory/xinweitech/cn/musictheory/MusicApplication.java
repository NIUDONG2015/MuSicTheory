package musictheory.xinweitech.cn.musictheory;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;

import musictheory.xinweitech.cn.musictheory.net.ApiService;
import musictheory.xinweitech.cn.musictheory.net.RetrofitManager;

/**
 * Created by Administrator on 2017/1/11.
 */

public class MusicApplication extends Application {
    public static ApiService apiService;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        String appId = "f347cf1d9e";   //上Bugly(bugly.qq.com)注册产品获取的App  new
        Bugly.init(getApplicationContext(), appId, false);
        //     如需增加自动检查时机可以使用
        Beta.checkUpgrade(false, false);
        //初始化BugLy
    // 获取当前包名
        String packageName = mContext.getPackageName();
    // 获取当前进程名

    /*    String processName = AppProcessName.getProcessName(android.os.Process.myPid());
    // 设置是否为上报进程
    CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContext);
    strategy.setUploadProcess(processName==null||processName.equals(packageName));

     boolean isDebug = true;  //true代表App处于调试阶段，false代表App发布阶段
     CrashReport.initCrashReport(mContext,appId,isDebug,strategy);  //初始化SDK*/
        //日志工具
        Logger.init("Music");

        //友盟统计
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);

        // 关闭Activity的默认统计方式，我们需要统计Activity+Fragment
        MobclickAgent.openActivityDurationTrack(false);
        //关闭友盟错误统计功能
        MobclickAgent.setCatchUncaughtExceptions(false);
    }


    public static Context getInstance() {
        return mContext;
    }


    public static void setService() {
        apiService = RetrofitManager.getInstance().getService();
    }


}
