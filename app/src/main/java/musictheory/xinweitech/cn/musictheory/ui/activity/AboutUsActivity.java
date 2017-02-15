package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;

/**
 * Created by niudong on 2017/1/29.
 */


public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    private String urlAbout;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));

        String able = getResources().getConfiguration().locale.getCountry();
        String syslanguage = getResources().getConfiguration().locale.getLanguage();

        String show_lang = PreferenceUtil.getString("language", syslanguage);
        if (show_lang.equals("zh") || able.equals("CN")) {
            lang = NetConstants.ABOUT_ZH;//中文
        } else if (show_lang.equals("en") || able.equals("US")) {
            lang = NetConstants.ABOUT_US_EN;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(lang);
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
