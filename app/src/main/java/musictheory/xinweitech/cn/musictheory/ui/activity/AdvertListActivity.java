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
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import musictheory.xinweitech.cn.musictheory.utils.SystemUtils;

/**
 * Created by niudong on 2017/2/3.
 */


public class AdvertListActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.web_view_download)
    WebView webViewDownLoad;
    private String urlDownLoad;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_advert_load);
        ButterKnife.bind(this);

        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));
        MusicApplication.setService();
        String linkUrl = (String) SharedPreferencesUtil.getData(AdvertListActivity.this, "linkUrl", "");
        String systemLanguage = SystemUtils.getSystemLanguage();//en
        if (systemLanguage.equals("en")) {
            urlDownLoad = NetConstants.REG_PROTOCOL_EN;
        } else if (systemLanguage.equals("zh")){
            urlDownLoad = NetConstants.REG_PROTOCOL_ZH;
        }
        webViewDownLoad.getSettings().setJavaScriptEnabled(true);
        webViewDownLoad.setWebViewClient(new WebViewClient());

        webViewDownLoad.loadUrl(linkUrl);

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
