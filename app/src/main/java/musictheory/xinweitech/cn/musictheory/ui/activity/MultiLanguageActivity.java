package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.adapter.data.MultiLangAdapter;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.LangCheckBean;
import musictheory.xinweitech.cn.musictheory.entity.MultiLanguageEntity;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.utils.JsonUtil;
import musictheory.xinweitech.cn.musictheory.utils.NetManager;
import musictheory.xinweitech.cn.musictheory.utils.RestartAPPTool;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by niudong on 2017/2/9.
 */


public class MultiLanguageActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RecyclerView recyclerView;
    private List<LangCheckBean> langList = new ArrayList<>();
    private String userNo;
    private String lang;
    private MultiLangAdapter adapter;
    private SharedPreferences userInfo;//用户信息缓存
    private SharedPreferences.Editor edit;
    private FrameLayout noNetStatus;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_multi_lang);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        recyclerView = (RecyclerView) findViewById(R.id.recyle_langview);
        noNetStatus = (FrameLayout) findViewById(R.id.frame_no_netstatus);
        userNo = (String) SharedPreferencesUtil.getData(this, "userNo", "");//1个
        userInfo = getSharedPreferences("multilang", MODE_PRIVATE);
        edit = userInfo.edit();
        StatusBarCompat.compat(this, getResources().getColor(R.color.statusbar_blue));
        addData();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userNo", userNo);
//        map.put("modelName", lang);//gai bian
        map.put("attrName", NetConstants.MULTIL_LANG);  //lang 语言列表
        String langJson = JsonUtil.encode(map);


        if (NetManager.getInstance().isOpenNetwork()) {
//            multiLanguageReq(langJson);
            adapter = new MultiLangAdapter(langList, this);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new MultiLangAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, String s) {
                    adapter.setPosition(position); //传递当前的点击位置zh1  zhe ge int 给 那个

                    adapter.notifyDataSetChanged();
                    edit.putInt("position", position);
                    edit.commit();
                    Logger.d(s + position);
                    switchLanguage(s);
                    finish();
                    Intent intent = new Intent(MultiLanguageActivity.this, SplashActivity.class);
                    startActivity(intent);
                    RestartAPPTool.restartAPP(MultiLanguageActivity.this, 1000);

                }
            });
        } else {
            noNetStatus.setVisibility(View.VISIBLE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void addData() {
        LangCheckBean sys = new LangCheckBean(0, "跟随系统", "sys");
        langList.add(sys);
        LangCheckBean zh = new LangCheckBean(1, "简体中文", "zh");
        langList.add(zh);
        LangCheckBean en = new LangCheckBean(2, "English", "en");
        langList.add(en);

    }


    public void multiLanguageReq(String json) {

        MusicApplication.apiService.multiLanguage(NetConstants.LANG_ITEM_LIST, json, NetConstants.V, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<MultiLanguageEntity>() {

                    @Override
                    public void onNext(MultiLanguageEntity multiLanguageEntity) {
                        int err = multiLanguageEntity.getErr();

                        Logger.d(err);
                        if (err == 0 && multiLanguageEntity.getData() != null) {
                            processResult(multiLanguageEntity);


                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        Logger.d(e.getMessage());
                    }


                });
    }

    private void processResult(MultiLanguageEntity multiLanguageEntity) {
/*
        MultiLanguageEntity.DataBean data = multiLanguageEntity.getData();
        adapter = new MultiLangAdapter(data, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiLangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String s) {
                adapter.setPosition(position); //传递当前的点击位置zh1  zhe ge int 给 那个

                adapter.notifyDataSetChanged();
                edit.putInt("position", position);
                edit.commit();
                Logger.d(s + position);
                switchLanguage(s);
                finish();
                Intent intent = new Intent(MultiLanguageActivity.this, SplashActivity.class);
                startActivity(intent);
                RestartAPPTool.restartAPP(MultiLanguageActivity.this, 1000);

            }
        });
*/

    }

    @Override
    protected void initData() {
        ll_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            default:
                break;
        }
    }
}
