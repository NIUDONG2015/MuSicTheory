package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

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
import musictheory.xinweitech.cn.musictheory.entity.RegisterEntity;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
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
public class RegisterActivity extends BaseActivity {

    private static final String mPhoneNumPattern = "^[1][0-9]{10}$";
    private static final String mEmailPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static final String mPasswordPattern = "[a-zA-Z0-9]{6,18}";
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.reg_view)
    LinearLayout regView;
    @BindView(R.id.reg_protocol)
    LinearLayout regProtocol;
    private String email;
    private String pwd;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.regist)
    Button regist;
    private SharedPreferences userInfo;//用户信息缓存
    private SharedPreferences.Editor edit;
    private String encryptPwd;
    private NetManager netManager;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        MusicApplication.setService();
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_white));

        nomalColor();

        TextChange textChange = new TextChange();

        etEmail.addTextChangedListener(textChange);
        etPwd.addTextChangedListener(textChange);


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
    }


    @OnClick({R.id.ll_back, R.id.regist, R.id.reg_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.regist:

                hideKeyBoard();
                //核实信息
                if (NetManager.getInstance().isOpenNetwork()) {
                    verfiyInfo();
                } else {
                    showSnackbar(regView, getResources().getString(R.string.Network_request_failed_please_try_again_after_checking_the_network));
                }
                break;
            case R.id.reg_protocol:
                startActivity(RegProtocolActivity.class, null);
                break;
            default:
                break;
        }
    }

    //完美解决
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (etEmail.length() > 0 && etPwd.length() > 0) {
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

    @Override
    protected void initData() {
    }


    public void nomalColor() {
        if (regist != null)
            regist.setEnabled(false);//不可获取焦点
        regist.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
    }

    public void failColor() {
        regist.setEnabled(false);//不可获取焦点
        regist.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
        regist.setTextColor(getResources().getColor(R.color.click_login));
    }

    public void successColor() {
        regist.setEnabled(true);
        regist.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
        regist.setTextColor(getResources().getColor(R.color.colorwhite));


    }


    public void verfiyInfo() {

        email = etEmail.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();

        //校验邮箱名称
        if (checkUserName(email)) {
            verfiyPwd();
        } else {
            showSnackbar(regView, getResources().getString(R.string.email_verfiy_erro));
            return;
        }
    }

    private void verfiyPwd() {

        if (checkPassword(pwd)) {
            gotoLogin(lang);
        } else {
            showSnackbar(regView, getResources().getString(R.string.password_hint));
            return;
        }
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


    //  验证完成后去 注册  registerOK再登录
    //  checkVcodel 验证码
    //跳转登陆
    private void gotoLogin(String Lang) {

        //将邮箱名和密码传给服务器
        encryptPwd = Md5Utils.encrypt(pwd);
        //通过Bean将数据转化为Json传走
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("loginName", email);
        map.put("password", encryptPwd);
        String json = gson.toJson(map);
        MusicApplication.apiService.register(NetConstants.EVENTTYPE_REGISTER, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<RegisterEntity>() {
                    @Override
                    public void onNext(RegisterEntity registerEntity) {
                        int err = registerEntity.getErr();
                        RegisterEntity.DataBean data = registerEntity.getData();
                        String errMsg = registerEntity.errMsg;
                        if (data != null && err == 0) {
                            String loginName = data.getLoginName();
                            int status = data.getStatus();
                            String userNo = data.getUserNo();
                            Logger.d(TAG, registerEntity.data);
                            //保存登录信息
                            SharedPreferencesUtil.saveData(RegisterActivity.this, "LoginUser", loginName);
                            SharedPreferencesUtil.saveData(RegisterActivity.this, "LoginPwd", pwd);
                            SharedPreferencesUtil.saveData(RegisterActivity.this, "loginstatus", status);
                            SharedPreferencesUtil.saveData(RegisterActivity.this, "userNo", userNo);
                            // Success跳转页面
                            startActivity(HomeActivity.class, null);
                            LoginActivity.instance.finish();
                            finish();

                        } else {
                            showSnackbar(regView, errMsg);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(TAG, e.getMessage());

                    }
                });


    }


}
