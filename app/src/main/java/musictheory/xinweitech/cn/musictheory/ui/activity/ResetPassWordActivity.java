package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.ForgetPwd;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.ui.view.CustomDialog;
import musictheory.xinweitech.cn.musictheory.utils.NetManager;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by niudong on 2017/1/18.
 */


public class ResetPassWordActivity extends BaseActivity {
    private static final String TAG = "ResetPassWordActivity";

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.et_reg_email)
    EditText etRegEmail;
    @BindView(R.id.bt_next_go)
    Button btNextGo;
    @BindView(R.id.reset_view)
    LinearLayout regSetView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String restpwd;
    private String userEmail;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_reset_pwd);
        MusicApplication.setService();

        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_white));


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

    //初始化做的一些事情
    @Override
    protected void initData() {

        TextChange textChange = new TextChange();
        etRegEmail.addTextChangedListener(textChange);
        nomalColor();
    }

    public void nomalColor() {
        if (btNextGo != null) {
            btNextGo.setEnabled(false);//不可获取焦点
        }
        btNextGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
    }

    public void failColor() {
        btNextGo.setEnabled(false);//不可获取焦点
        btNextGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
        btNextGo.setTextColor(getResources().getColor(R.color.click_login));
    }

    public void successColor() {
        btNextGo.setEnabled(true);
        btNextGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
        btNextGo.setTextColor(getResources().getColor(R.color.colorwhite));


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

            if (etRegEmail.length() > 0) {
                successColor();
            } else {
                failColor();
            }
        }
    }

    @OnClick({R.id.ll_back, R.id.bt_next_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_next_go:
                hideKeyBoard();
                if (NetManager.getInstance().isOpenNetwork()) {
                    findpwd(lang);
                } else {
                    showSnackbar(regSetView, getResources().getString(R.string.Network_request_failed_please_try_again_after_checking_the_network));
                }
                //pang duan EditText shu ru nei rong

                break;
        }
    }


    //跳转到登录界面
    public void findpwd(String Lang) {
        //将邮箱和密码传给服务器

// 显示进度条

        userEmail = etRegEmail.getText().toString().trim();
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("loginName", userEmail);
        String json = gson.toJson(map);
        MusicApplication.apiService.forget(NetConstants.EVENTTYPE_FORGET, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<ForgetPwd>() {
                    @Override
                    public void onNext(ForgetPwd forgetPwd) {
                        processResult(forgetPwd);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(TAG, e.getMessage());

                    }
                });


    }

    private void processResult(ForgetPwd forgetPwd) {

        int err = forgetPwd.getErr();
        String errMsg = forgetPwd.getErrMsg();
        if (err == 0) {

            // Success跳转页面
            CustomDialog customDialog = new CustomDialog(ResetPassWordActivity.this);
            customDialog.setTitle(getResources().getString(R.string.send_success));
            customDialog.setMessage(getResources().getString(R.string.reset_password_send_your_email));
            customDialog.setYesOnclickListener(getResources().getString(R.string.confirm2), new CustomDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {


                            LoginActivity.actionStart(ResetPassWordActivity.this, userEmail);

                            customDialog.dismiss();
                        }
                    }
            );
            customDialog.show();
        } else {
            showSnackbar(regSetView, errMsg);
        }
    }

    // 隐藏键盘
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}