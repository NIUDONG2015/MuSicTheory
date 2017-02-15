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

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.ChangePwdEntity;
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
public class ChangePassWordActivity extends BaseActivity {
    private static final String TAG = "ChangePassWordActivity";
    private SharedPreferences userInfo;//用户信息缓存
    @BindView(R.id.iv_back)
    LinearLayout ivBack;
    @BindView(R.id.ll_change_view)
    LinearLayout changeView;
    @BindView(R.id.et_pre_pwd)
    EditText etPrePwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.commit)
    Button commit;
    private String oldPwd;
    private String newPwd;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        MusicApplication.setService();
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));

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
        nomalColor();

        TextChange textChange = new TextChange();

        etPrePwd.addTextChangedListener(textChange);
        etNewPwd.addTextChangedListener(textChange);


    }

    public void nomalColor() {
        if (commit != null)
            commit.setEnabled(false);//不可获取焦点
        commit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
    }

    public void failColor() {
        commit.setEnabled(false);//不可获取焦点
        commit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_normal));
        commit.setTextColor(getResources().getColor(R.color.click_login));
    }

    public void successColor() {
        commit.setEnabled(true);
        commit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
        commit.setTextColor(getResources().getColor(R.color.colorwhite));


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
            if (etPrePwd.length() > 0 && etNewPwd.length() > 0) {
                successColor();
            } else {
                failColor();
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.et_pre_pwd, R.id.et_new_pwd, R.id.commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.commit:
                hideKeyBoard();

                if (NetManager.getInstance().isOpenNetwork()) {
                    checkInfo();
                } else {
                    showSnackbar(changeView, getResources().getString(R.string.Network_request_failed_please_try_again_after_checking_the_network));
                }

                //请求修改密码

                break;
        }
    }

    private void checkInfo() {
        //获取输入的密码
        oldPwd = etPrePwd.getText().toString().trim();
        newPwd = etNewPwd.getText().toString().trim();


        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)) {
            showSnackbar(changeView, getString(R.string.configure_old_pwd_and_new_number));
            return;
        }

        if (newPwd.length() >= 6) {
            changePwdRequest(lang);
        } else {
            showSnackbar(changeView, getString(R.string.the_new_password_more_than_six));
            return;
        }
    }

    //记得做判空处理
    public void changePwdRequest(String Lang) {

        //跳转 个人中心或者重新登录
        //将邮箱名和密码传给服务器
        String encryptOldPwd = Md5Utils.encrypt(oldPwd);
        String encryptNewPwd = Md5Utils.encrypt(newPwd);
        //通过Bean将数据转化为Json传走
        Gson gson = new Gson();
        String userNo = (String) SharedPreferencesUtil.getData(this, "userNo", "");
        Map<String, String> map = new HashMap<>();
        map.put("userNo", userNo);
        map.put("oldPassword", encryptOldPwd);
        map.put("newPassword", encryptNewPwd);
        String json = gson.toJson(map);
        MusicApplication.apiService.changePwd(NetConstants.EVENTTYPE_UPDATE_PWD, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<ChangePwdEntity>() {
                    @Override
                    public void onNext(ChangePwdEntity changePwdEntity) {

                        processResult(changePwdEntity);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(TAG, e.getMessage());
                    }
                });


    }

    // 隐藏键盘
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void processResult(ChangePwdEntity changePwdEntity) {

        int err = changePwdEntity.getErr();
        String errMsg = changePwdEntity.getErrMsg();
        if (err == 0) {
            Logger.d(TAG, err);
            // Success跳转页面
            showSnackbar(changeView, getResources().getString(R.string.success_modify));
            finish();
            startActivity(UserInfoActivity.class, null);
        } else {
            showSnackbar(changeView, errMsg);
        }
    }
}
