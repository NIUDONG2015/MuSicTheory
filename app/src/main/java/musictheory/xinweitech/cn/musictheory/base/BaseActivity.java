package musictheory.xinweitech.cn.musictheory.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Locale;

import butterknife.ButterKnife;
import musictheory.xinweitech.cn.musictheory.entity.Language;
import musictheory.xinweitech.cn.musictheory.service.IntentService;
import musictheory.xinweitech.cn.musictheory.service.PushService;
import musictheory.xinweitech.cn.musictheory.utils.ActivityCollector;
import musictheory.xinweitech.cn.musictheory.utils.Constants;
import musictheory.xinweitech.cn.musictheory.utils.JsonUtil;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.ToastUtil;


/**
 * Created by niudong on 2017/1/11.
 */

public abstract class BaseActivity extends AutoLayoutActivity {
    private Context mContext;
    private Toast mToast;
    private static final String LANGUAGE = "language";
    private LayoutInflater mInflater;
    private IntentFilter intentFilter;
    //    private ForceOfflineReceiver receiver;
    public Language mLang;//当前的语言环境

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //设定屏幕朝向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityCollector.addActivity(this);
        mInflater = LayoutInflater.from(mContext);
//        initLang();
        initView();
        ButterKnife.bind(this);
        initData();

        //初始化个推   //  为第三方自定义推送服务

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        // 为第三方自定义的推送服务事件接收类
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);


        PreferenceUtil.init(this);
        //根据上次的语言设置，重新设置语言   没有事系统语言
        String syslanguage = getResources().getConfiguration().locale.getLanguage();
        switchLanguage(PreferenceUtil.getString("language", syslanguage));
    }

    public void initLang() {
        //读取缓存中存储的语言信息
        String cacheLang = SharedPreferenceUtil.getSharedPreferences(LANGUAGE, Constants.Language.SIMPLED_CHINESE, this);
        try {
            //从缓存中取数据
            mLang = (Language) JsonUtil.decode(cacheLang, Language.class);
        } catch (Exception e) {//取不到的话走中文！！
            mLang = new Language(Language.Type.SIMPLED_CHINESE, Language.Type4Show.SIMPLED_CHINESE);
        }
        switchLanguage(mLang);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        友盟
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

    }

    @Override
    protected void onPause() {
        super.onPause();
//        友盟
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());


    }

    protected abstract void initView();


    protected abstract void initData();

    /**
     * 以下Activity切换效果随需求变更
     *
     * @param intent
     * @param requestCode
     */

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 获取输入的文字
     *
     * @param textView
     * @return
     */
    public String getText(TextView textView) {
        return textView.getText().toString();
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public boolean equals(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    public void showToast(int resId) {
        ToastUtil.showToast(mContext, resId);
    }

    public boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public void showSnackbar(View view, CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }


    // 自定义Toast
    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);

        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.RemoveActivity(this);
    }


    protected void switchLanguage(String language) {
        //设置应用语言类型demo
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")){
            config.locale = Locale.ENGLISH;
        }else if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("sys")) {
            config.locale = Locale.getDefault();
        } else if (language.equals("zh_CN")) {
            config.locale = Locale.TRADITIONAL_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        //保存设置语言的类型
        PreferenceUtil.commitString("language", language);
    }

    /**
     * 切换语言
     *
     * @param language
     */
    public void switchLanguage(Language language) {
        if (language == null) {
            return;
        }
        // 设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Locale local = config.locale;
        if (Language.Type.ENGLISH.equals(language.getType())) {
            if (local != Locale.ENGLISH) {
                config.locale = Locale.ENGLISH;
            }
        } else if ((Language.Type.SIMPLED_CHINESE).equals(language.getType())) {//zhong
            if (local != Locale.SIMPLIFIED_CHINESE) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            }
        }
        resources.updateConfiguration(config, dm);
        // 保存设置语言的类型
        SharedPreferenceUtil.setSharedPreferences(LANGUAGE, JsonUtil.encode(language), this);
    }
}
