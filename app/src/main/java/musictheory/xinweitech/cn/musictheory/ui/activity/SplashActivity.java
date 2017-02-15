package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;

/**
 * Created by niudong on 2017/1/12.
 */


public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_view)
    RelativeLayout splashView;

    @Override
    protected void initView() {
/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        String loginUser = (String) SharedPreferencesUtil.getData(this, "LoginUser", "");
        String loginPwd = (String) SharedPreferencesUtil.getData(this, "LoginPwd", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(loginUser) && TextUtils.isEmpty(loginPwd)) {
                    startActivity(LoginActivity.class, null);
                    finish();
                } else {
                    startActivity(HomeActivity.class, null);
                    finish();
                }
            }
        }, 1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
