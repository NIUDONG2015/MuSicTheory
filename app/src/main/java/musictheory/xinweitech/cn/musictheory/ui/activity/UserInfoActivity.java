package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.CheckEmailEntity;
import musictheory.xinweitech.cn.musictheory.entity.UserInfoIsCheck;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.ui.view.CustomDialog;
import musictheory.xinweitech.cn.musictheory.ui.view.CustomDialogLogout;
import musictheory.xinweitech.cn.musictheory.utils.ActivityCollector;
import musictheory.xinweitech.cn.musictheory.utils.JsonUtil;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import okhttp3.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by niudong on 2017/1/12.
 */


public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.rlmulti_lang)
    RelativeLayout rlmultiLang;
    private SharedPreferences userInfo;//用户信息缓存
    private SharedPreferences.Editor edit;
    private static final String TAG = "UserInfoActivity";
    @BindView(R.id.user_view)
    LinearLayout userView;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_email_name)
    TextView tvEmailName;
    @BindView(R.id.rl_verify)
    RelativeLayout rlVerify;
    @BindView(R.id.rl_users)
    RelativeLayout cHangePwd;
    @BindView(R.id.changge_pwd)
    RelativeLayout rlUsers;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;

    @BindView(R.id.bt_logout)
    Button btLogout;
    private LinearLayout verify_no;
    private LinearLayout verify_ok;
    private String emailLoginUser;
    private int emailCheckstatus;
    private int emailStatus;
    private static final String downloadUrl = "http://182.92.6.139:80/apk/school_2.17_117.apk";

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private String lang;
    private String jsonInfo;
    private String userNo;
    private int status;

    @Override
    protected void initView() {

        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        MusicApplication.setService();
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userNo = (String) SharedPreferencesUtil.getData(this, "userNo", "");
        edit = userInfo.edit();
        verify_no = (LinearLayout) findViewById(R.id.ll_verify_no);//  xian shi he ying cang
        verify_ok = (LinearLayout) findViewById(R.id.ll_verify_ok);//  xian shi he ying cang

        //从缓存中取数据判断是否ok

        emailLoginUser = (String) SharedPreferencesUtil.getData(this, "LoginUser", "");
        tvEmailName.setText(emailLoginUser);
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, String> map2 = new HashMap<>();
        map2.put("userNo", userNo);
        jsonInfo = JsonUtil.encode(map2);
        String able = getResources().getConfiguration().locale.getCountry();
        String syslanguage = getResources().getConfiguration().locale.getLanguage();
        String show_lang = PreferenceUtil.getString("language", syslanguage);
        if (show_lang.equals("zh") || able.equals("CN")) {
            //中文
            lang = NetConstants.LANG;
        } else if (show_lang.equals("en") || able.equals("US")) {
            lang = NetConstants.EN;
        } else if (show_lang.equals("zh_CN")) {
            lang = NetConstants.zh_CN;
        }

        findUserIoforequest(jsonInfo, lang);
    }

    private void findUserIoforequest(String Json, String Lang) {

        MusicApplication.apiService.findUserIofo(NetConstants.URER_INFO, Json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<UserInfoIsCheck>() {
                    @Override
                    public void onNext(UserInfoIsCheck userInfoIsCheck) {
                        if (userInfoIsCheck.getErr() == 0) {
                            status = userInfoIsCheck.getData().getStatus();

                            if (status == 0) {
                                verify_ok.setVisibility(View.VISIBLE);
                                verify_no.setVisibility(View.GONE);
                            } else {
                                verify_ok.setVisibility(View.GONE);
                                verify_no.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                });
    }

    @Override
    protected void initData() {


    }


    @OnClick({R.id.ll_back, R.id.changge_pwd, R.id.rl_verify, R.id.rl_users, R.id.rl_update, R.id.bt_logout, R.id.rlmulti_lang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
               finish();
                break;

            case R.id.changge_pwd:
                startActivity(ChangePassWordActivity.class, null);

                break;
            case R.id.rl_verify:
                if (status == 0) {
                    return;
                }
                //yan zheng ba
                checkEmailRequest(jsonInfo);
                break;
            case R.id.rl_users:
                startActivity(AboutUsActivity.class, null);
                break;
            case R.id.rl_update:
                showSnackbar(userView, getResources().getString(R.string.check_update));
//                downloadApk();
                break;

            case R.id.bt_logout:
                showLogOutDialog();
                break;

            case R.id.rlmulti_lang:
                startActivity(MultiLanguageActivity.class, null);
                break;
        }
    }

    public void showLogOutDialog() {
        CustomDialogLogout customDialog = new CustomDialogLogout(UserInfoActivity.this);
        customDialog.setTitle(getResources().getString(R.string.confirm_exit));
        customDialog.setNoOnclickListener(getResources().getString(R.string.cancel), new CustomDialogLogout.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                customDialog.dismiss();
            }
        });

        customDialog.setYesOnclickListener(getResources().getString(R.string.confirm2), new CustomDialogLogout.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ActivityCollector.finishAll(); // 销毁所有活动
                edit.clear();
                edit.commit();


                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    public void checkEmailRequest(String Json) {
        //将邮箱和密码传给服务器
        MusicApplication.apiService.checkEmail(NetConstants.EVENTTYPE_CHECK_EMAIL, Json, NetConstants.V, NetConstants.LANG, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<CheckEmailEntity>() {
                    @Override
                    public void onNext(CheckEmailEntity checkEmailEntity) {
                        processResult(checkEmailEntity);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(TAG, e.getMessage());
                    }
                });
    }


    private void processResult(CheckEmailEntity checkEmailEntity) {

        int err = checkEmailEntity.getErr();
        String errMsg = checkEmailEntity.getErrMsg();
        if (err == 0) {
            // Success跳转页面
            CustomDialog customDialog = new CustomDialog(UserInfoActivity.this);
            customDialog.setTitle(getResources().getString(R.string.send_success));
            customDialog.setMessage(getResources().getString(R.string.verification_link_has_been_sent_to_your_mailbox));
            customDialog.setYesOnclickListener(getResources().getString(R.string.confirm2), new CustomDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            customDialog.dismiss();
                        }
                    }
            );
            customDialog.show();
        } else {
            showSnackbar(userView, errMsg);
        }
    }

    private void downloadApk() {
        showNotification();
        OkHttpUtils.get()
                .url(downloadUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), getApkName(downloadUrl)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String message = e.getMessage();

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        int nowProgress = (int) (progress * 100);
                        if (nowProgress >= 100) {
                            mBuilder.setProgress(0, 0, false);    //移除进度条
                            mBuilder.setContentTitle("下载完成,点击安装");

                        } else {
                            if (nowProgress > 0) {
                                mBuilder.setDefaults(Notification.COLOR_DEFAULT);
                            }
                            //注意:此方法在4.0以后版本才有用.如果需要支持早期版本,需使用RemoteViews来自定义视图
                            mBuilder.setProgress(100, nowProgress, false);
                            mBuilder.setContentTitle("正在下载...");
//                            mBuilder.setOngoing(true);
                        }
                        mNotificationManager.notify(1, mBuilder.build());
                        Logger.d("total = " + total + ",progress = " + (progress * 100));
                    }
                });

    }

    public void showNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.header_more)     //设置通知小Icon
//                .setContentTitle("开始下载...")     //设置通知栏标题
                .setTicker("开始下载...")
                .setWhen(System.currentTimeMillis())    //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(NotificationCompat.PRIORITY_MAX)  //设置通知的优先级PRIORITY_HIGH或PRIORITY_MAX  以浮动形式展示
                //.setAutoCancel(true)    //设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(true)       ////true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                // 设置通知的提示音
                // Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                // builder.setSound(alarmSound);
                .setDefaults(NotificationCompat.DEFAULT_ALL);//或者设置系统默认DEFAULT_ALL,包含DEFAULT_SOUND(提示音), DEFAULT_VIBRATE(振动), DEFAULT_LIGHTS(灯光闪烁)

//        mNotificationManager.notify(1, mBuilder.build());
    }

    /**
     * 通过下载url截取apk文件名
     *
     * @param url
     * @return
     */
    private String getApkName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

}

