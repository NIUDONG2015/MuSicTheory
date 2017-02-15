package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;

/**
 * Created by niudong on 2017/1/29.
 */


public class RegProtocolActivity extends BaseActivity {
    @BindView(R.id.web_view_protocol)
    WebView webViewProtocol;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    private String urlProtocol;
    private String lang;

    @Override
    protected void initView() {

        setContentView(R.layout.activity_h5_protocol);
        ButterKnife.bind(this);
        MusicApplication.setService();
        String able = getResources().getConfiguration().locale.getCountry();

        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));
        String syslanguage = getResources().getConfiguration().locale.getLanguage();
        String show_lang = PreferenceUtil.getString("language",syslanguage);

        if (show_lang.equals("zh") || able.equals("CN")) {
            lang = NetConstants.REG_PROTOCOL_ZH;//中文
        } else if (show_lang.equals("en") || able.equals("US")) {
            lang = NetConstants.REG_PROTOCOL_EN;
        }

        webViewProtocol.getSettings().setJavaScriptEnabled(true);
        webViewProtocol.setWebViewClient(new WebViewClient());
        webViewProtocol.loadUrl(lang);

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
