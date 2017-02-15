package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.adapter.data.RecyclerViewFootAdapter;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.AdvertListEntity;
import musictheory.xinweitech.cn.musictheory.entity.HomeDetailEntity;
import musictheory.xinweitech.cn.musictheory.entity.LessonDetailReq;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.utils.NetManager;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by niudong on 2017/1/12.
 */


public class HomeDetailActivity extends BaseActivity {

    private static final String TAG = "HomeDetailActivity";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.home_detail_title)
    TextView homeDetailTitle;
    @BindView(R.id.recyle_view)
    RecyclerView recyclerView;
    @BindView(R.id.frame_no_netstatus)
    FrameLayout noNetstatus;
    FrameLayout noDatastatus;
    private int lessonId;

    private LinearLayout home_detail_view;
    private String userNo;
    private String newsTitleF;
    private RecyclerViewFootAdapter recyclerViewAdapter;
    private String advertDownLoadjson;
    private String detailjson;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);
        MusicApplication.setService();
        noDatastatus = (FrameLayout) findViewById(R.id.frame_no_datastatus);
        home_detail_view = (LinearLayout) findViewById(R.id.home_detail_view);

        lessonId = getIntent().getFlags();
        Logger.d(lessonId);
        newsTitleF = getIntent().getStringExtra("news_title_f");
        userNo = (String) SharedPreferencesUtil.getData(this, "userNo", "");
        String able = getResources().getConfiguration().locale.getCountry();
        String syslanguage = getResources().getConfiguration().locale.getLanguage();

        String show_lang = PreferenceUtil.getString("language", syslanguage);
        if (show_lang.equals("zh") || able.equals("CN")) {
            lang = NetConstants.LANG;//中文
        } else if (show_lang.equals("en") || able.equals("US")) {
            lang = NetConstants.EN;
        }
        Logger.d(lang);

        Gson gson = new Gson();
        //Bean duix request Js
        //toJson
        //将邮箱和密码传给服务器
        //用一个bean来传递数据
        LessonDetailReq lessonDetailReq = new LessonDetailReq();
        lessonDetailReq.setUserNo(userNo);
        lessonDetailReq.setLessonId(lessonId);
        detailjson = gson.toJson(lessonDetailReq);
        //掌握状态数据
        HashMap<String, String> map = new HashMap<>();
        map.put("userNo", userNo);
        advertDownLoadjson = gson.toJson(map);
        Logger.d(advertDownLoadjson);
        //广告      //false
        advertDownLoad(advertDownLoadjson, lang);

        StatusBarCompat.compat(HomeDetailActivity.this, getResources().getColor(R.color.statusbar_blue));

        homeDetailTitle.setText(newsTitleF);
        if (NetManager.getInstance().isOpenNetwork()) {
            lessondetailRequest(detailjson, lang);
        } else {
            noNetstatus.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
        }
//        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
    }

    //jieshou 传过来的数据
    public static void actionStart(Context context, int lessonId, String newsTitle) {
        Intent intent = new Intent(context, HomeDetailActivity.class);
        intent.addFlags(lessonId);
        intent.putExtra("news_title_f", newsTitle);
        context.startActivity(intent);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            default:

                break;
        }
    }

    public void lessondetailRequest(String json, String sLang) {


        MusicApplication.apiService.lessondetail(NetConstants.EVENTTYPE_LESSON_DETAIL, json, NetConstants.V, sLang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HomeDetailEntity>() {
                               @Override
                               public void onNext(HomeDetailEntity lesson) {
                                   processResult(lesson);

                               }

                               @Override
                               public void onError(Throwable e) {
                                   Logger.d(TAG, e.getMessage());


                                   noDatastatus.setVisibility(View.VISIBLE);
                               }
                           }

                );

    }

    public void processResult(HomeDetailEntity lesson) {
        int err = lesson.getErr();
        if (lesson != null && err == 0) {
            recyclerViewAdapter = new RecyclerViewFootAdapter(HomeDetailActivity.this, lesson);
            recyclerViewAdapter.notifyDataSetChanged();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            recyclerView.setAdapter(recyclerViewAdapter);

        } else {

            noDatastatus.setVisibility(View.VISIBLE);
        }
    }

    public void advertDownLoad(String json, String Lang) {

        MusicApplication.apiService.advertDownLoad(NetConstants.EVENTTYPE_ADVERT, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<AdvertListEntity>() {
                    @Override
                    public void onNext(AdvertListEntity advertLoad) {
                        List<AdvertListEntity.DataBean.ListBean> list = advertLoad.getData().getList();
                        Logger.d(list);
                        if (list.size() > 0 && advertLoad.getErr() == 0)
                            for (int i = 0; i < list.size(); i++) {
                                String content = list.get(i).getContent();
                                String icoUrl = list.get(i).getIcoUrl();
                                String linkUrl = list.get(i).getLinkUrl();

                                SharedPreferencesUtil.saveData(HomeDetailActivity.this, "size", list.size());
                                SharedPreferencesUtil.saveData(HomeDetailActivity.this, "content", content);
                                SharedPreferencesUtil.saveData(HomeDetailActivity.this, "icoUrl", icoUrl);
                                SharedPreferencesUtil.saveData(HomeDetailActivity.this, "linkUrl", linkUrl);

                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = e.getMessage();
                    }
                });
    }


}
