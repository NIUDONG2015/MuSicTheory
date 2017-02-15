package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.LoginEntity;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.presenter.LoginPresenter;
import musictheory.xinweitech.cn.musictheory.utils.Md5Utils;
import musictheory.xinweitech.cn.musictheory.utils.NetManager;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by niudong on 2017/1/11.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private SharedPreferences userInfo;//用户信息缓存
    private SharedPreferences.Editor edit;
    private static final String mPhoneNumPattern = "^[1][0-9]{10}$";
    private static final String mEmailPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static final String mPasswordPattern = "[a-zA-Z0-9]{6,18}";
    @BindView(R.id.login_view)
    LinearLayout loginView;
    private LoginPresenter presenter;
    private IntentFilter intentFilter;
    private String username;
    private String pwd;
    private ProgressDialog dialog;
    @BindView(R.id.ll_reg_to)
    LinearLayout lv_reg_to;
    @BindView(R.id.login_email)
    EditText login_email;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.ll_forget_pwd)
    LinearLayout ll_forget_pwd;
    public static LoginActivity instance = null;
    @BindView(R.id.progress_bar)
    ProgressBar progress_Bar;
    //deng lu zhang hao
    private ArrayList<String> numbers = new ArrayList<>();
    //mi ma
    private ArrayList<String> passWordList = new ArrayList<>();
    private String usernameLast;
    private String passWordLast;
    private String encrypt;
    private String meailNew;
    private String pwdNew;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        MusicApplication.setService();
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_white));
        nomalColor();
        // 之前写法将--注册账号回显      现在改为上一次登录信息回显
        instance = this;
        String able = getResources().getConfiguration().locale.getCountry();
        String syslanguage = getResources().getConfiguration().locale.getLanguage();
        String show_lang = PreferenceUtil.getString("language", syslanguage);

        if (show_lang != null)
            if (show_lang.equals("zh") || able.equals("CN")) {
                //中文
                lang = NetConstants.LANG;
            } else if (show_lang.equals("en") || able.equals("US")) {
                lang = NetConstants.EN;
            } else if (show_lang.equals("zh_CN")) {
                lang = NetConstants.zh_CN;
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String loginUser = (String) SharedPreferencesUtil.getData(this, "LoginUser", "");
        String loginPwd = (String) SharedPreferencesUtil.getData(this, "LoginPwd", "");
        if (!TextUtils.isEmpty(loginUser) && !TextUtils.isEmpty(loginPwd)) {
            startActivity(HomeActivity.class, null);
            finish();
        }
    }

    public static void actionStart(Context context, String etRegEmail) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("resetemail", etRegEmail);
        context.startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(intent + "---------------------------------");
        String resetemail = intent.getStringExtra("resetemail");
        Log.i(TAG, "initView: " + resetemail);
        if (resetemail == null) {
            login_email.setText("");
        } else {
            login_email.setText(resetemail);
        }
    }

    @OnClick(R.id.login)
    public void onClick() {

        hideKeyBoard();


        if (NetManager.getInstance().isOpenNetwork()) {
            checkInfo();
        } else {
            showSnackbar(loginView, getResources().getString(R.string.Network_request_failed_please_try_again_after_checking_the_network));
        }

    }

    @Override
    protected void initData() {
        //默认下颜色

        TextChange textChange = new TextChange();

        login_email.addTextChangedListener(textChange);
        etPwd.addTextChangedListener(textChange);
        lv_reg_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RegisterActivity.class, null);

            }
        });

        ll_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ResetPassWordActivity.class, null);
            }
        });
/**
 * 此方法在输入密码 点击键盘离开的时候触发
 */

        etPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {

                    hideKeyBoard();
                    if (NetManager.getInstance().isOpenNetwork()) {
                        checkInfo();
                    } else {
                        showSnackbar(loginView, getResources().getString(R.string.Network_request_failed_please_try_again_after_checking_the_network));
                    }
                    return true;
                }
                return false;
            }
        });


    }


    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (login_email.length() > 0 && etPwd.length() > 0) {
                successColor();
            } else {
                failColor();
            }
        }
    }

    // 隐藏键盘
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void nomalColor() {
        if (login != null)
            login.setEnabled(false);//不可获取焦点
        login.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
    }

    public void failColor() {
        login.setEnabled(false);//不可获取焦点
        login.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
        login.setTextColor(getResources().getColor(R.color.click_login));
    }

    public void successColor() {
        login.setEnabled(true);
        login.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
        login.setTextColor(getResources().getColor(R.color.colorwhite));


    }

    /**
     * 检查信息
     *
     * @return
     */

    private void checkInfo() {
        username = login_email.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        LoginSuccess(lang);
        progress_Bar.setVisibility(View.VISIBLE);
        Logger.d(lang);
    }


    /**
     * 请求服务器登录成功保存登录状态
     *
     * @param Lang
     */
    private void LoginSuccess(String Lang) {

        //将邮箱和密码传给服务器

        String encryptPwd = Md5Utils.encrypt(pwd);
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("loginName", username);
        map.put("password", encryptPwd);
        String json = gson.toJson(map);
        MusicApplication.apiService.login(NetConstants.EVENTTYPE_LOGIN, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<LoginEntity>() {
                    @Override
                    public void onNext(LoginEntity loginEntity) {
                        String errMsg = loginEntity.getErrMsg();

                        processResult(loginEntity);
                        //
                        progress_Bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                        progress_Bar.setVisibility(View.GONE);
                        Logger.d(TAG, e.getMessage());
                    }
                });


    }


    private boolean checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return false;
        }
        if (!Pattern.matches(mEmailPattern, userName) && !Pattern.matches(mPhoneNumPattern, userName)) {
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        if (!Pattern.matches(mPasswordPattern, password)) {
            return false;
        }
        return true;
    }


    public void processResult(LoginEntity loginEntity) {

        int err = loginEntity.getErr();
        LoginEntity.DataBean data = loginEntity.getData();

        String errMsg = loginEntity.getErrMsg();
        if (data != null && err == 0) {
            String loginName = data.getLoginName();
            int status = data.getStatus();
            String userNo = data.getUserNo();
            Logger.d(TAG, loginName + status + userNo);
            // Success跳转页面   保存UserNo 放到sp
            // 将账号密码以及userid保存起来
            SharedPreferencesUtil.saveData(this, "LoginUser", username);
            SharedPreferencesUtil.saveData(this, "LoginPwd", pwd);
            SharedPreferencesUtil.saveData(this, "loginstatus", status);
            SharedPreferencesUtil.saveData(this, "userNo", userNo);
            startActivity(HomeActivity.class, null);
            finish();

        } else {
            showSnackbar(loginView, errMsg);
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
