package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.adapter.groupadapter.HomeEntityAdapter;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.HotelEntity;
import musictheory.xinweitech.cn.musictheory.entity.LessonListEntity;
import musictheory.xinweitech.cn.musictheory.entity.LessonListReq;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.utils.NetManager;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import musictheory.xinweitech.cn.musictheory.utils.StatusBarCompat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private Context mContext;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView_list;
    private HomeEntityAdapter mAdapter;
    private LinearLayout home_view;
    private HotelEntity entity;
    private int mCount = 0;
    private int lastVisibleItem;
    private LessonListEntity.DataBean list = new LessonListEntity().getData();
    private FrameLayout noNetstatus;
    private FrameLayout noDatastatus;
    private int lastPosition = 0;
    private int lastOffset = 0;
    private String able;
    private String json;
    private String lang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        MusicApplication.mContext = this;
        MusicApplication.setService();
        home_view = (LinearLayout) findViewById(R.id.home_view);
        noNetstatus = (FrameLayout) findViewById(R.id.frame_no_netstatus);
        noDatastatus = (FrameLayout) findViewById(R.id.frame_no_datastatus);
        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        //a.首先获得当前的语言或者国家：
        able = getResources().getConfiguration().locale.getCountry();
        String syslanguage = getResources().getConfiguration().locale.getLanguage();

        Logger.d("wo zhi xing la");
        String show_lang = PreferenceUtil.getString("language", syslanguage);

        Logger.d(show_lang + "--------------------------" + syslanguage);  //║ zh--------------------------zh
        if (show_lang.equals("zh") || able.equals("CN")) {
            lang = NetConstants.LANG;//中文
        } else if (show_lang.equals("en") || able.equals("US")) {
            lang = NetConstants.EN;
        } else if (show_lang.equals("zh_CN")) {
            lang = NetConstants.zh_CN;
        }
        //b.进行判断：如果是中文则返回的able.equals("CN")
        Gson gson = new Gson();
        LessonListReq lessonListReq = new LessonListReq();
        String userNo = (String) SharedPreferencesUtil.getData(this, "userNo", "");
        lessonListReq.setUserNo(userNo);
        lessonListReq.setStart(mCount);
        lessonListReq.setLimit(40);
        json = gson.toJson(lessonListReq);
        if (NetManager.getInstance().isOpenNetwork()) {
            requestData(json, lang);
            noNetstatus.setVisibility(View.GONE);
        } else {
            noNetstatus.setVisibility(View.VISIBLE);
        }

        //设置color
        StatusBarCompat.compat(HomeActivity.this, getResources().getColor(R.color.statusbar_blue));
        //获取数据


    }

    private void requestData(String json, String Lang) {
        MusicApplication.apiService.lessonList(NetConstants.EVENTTYPE_LESSON_LIST, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<LessonListEntity>() {
                    @Override
                    public void onNext(LessonListEntity lessonListEntities) {

                        if (lessonListEntities.getData() != null) {
                            noDatastatus.setVisibility(View.GONE);
                            data(lessonListEntities);
                        } else {
                            noDatastatus.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = e.getMessage();
                        //加载失败的图片
                        noDatastatus.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void data(LessonListEntity lessonListEntities) {
        mAdapter = new HomeEntityAdapter(HomeActivity.this);
        mAdapter.setData(lessonListEntities.getData());
        mAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_list.setAdapter(mAdapter);
        recyclerView_list.setLayoutManager(layoutManager);
        if (recyclerView_list != null)
            recyclerView_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    View topView = layoutManager.getChildAt(0);          //获取可视的第一个view
                    if (topView != null)
                        lastOffset = topView.getTop();                                   //获取与该view的顶部的偏移量
                    lastPosition = layoutManager.getPosition(topView);  //得到该View的数组位置


                }
            });
        ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(lastPosition, lastOffset);
    }

    @Override
    protected void initData() {
        Logger.i(TAG, "onCreate");

    }


    @Override
    protected void onResume() {
        super.onResume();
        //上来就刷新开始显示 勾选中状态
        if (NetManager.getInstance().isOpenNetwork()) {
            requestData(json, lang);
            noNetstatus.setVisibility(View.GONE);
            recyclerView_list.setVisibility(View.VISIBLE);
        } else {
            noNetstatus.setVisibility(View.VISIBLE);
            recyclerView_list.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.ll_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_more:
                startActivity(UserInfoActivity.class, null);
                break;
            default:
                break;
        }
    }

    // 返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键

        }
        return super.onKeyDown(keyCode, event);
    }
}