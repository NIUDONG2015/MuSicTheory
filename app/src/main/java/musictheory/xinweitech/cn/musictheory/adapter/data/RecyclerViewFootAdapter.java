package musictheory.xinweitech.cn.musictheory.adapter.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.constants.NetConstants;
import musictheory.xinweitech.cn.musictheory.entity.CheckStatusEntity;
import musictheory.xinweitech.cn.musictheory.entity.CheckStatusRequest;
import musictheory.xinweitech.cn.musictheory.entity.HomeDetailEntity;
import musictheory.xinweitech.cn.musictheory.net.MySubscriber;
import musictheory.xinweitech.cn.musictheory.ui.activity.AdvertListActivity;
import musictheory.xinweitech.cn.musictheory.utils.GlideUtils;
import musictheory.xinweitech.cn.musictheory.utils.PreferenceUtil;
import musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;
import static musictheory.xinweitech.cn.musictheory.utils.SharedPreferencesUtil.getData;


/**
 * Created by niudong
 * on 2017/1/22.
 */
public class RecyclerViewFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private Context mContext;
    private HomeDetailEntity dataDetailList;
    private LayoutInflater inflater;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private SharedPreferences userInfo;//用户信息缓存
    //是否显示单选框,默认false
    private CheckStatusRequest check;
    private final String userNo;
    private int lessonId;
    private String able;
    private String checkBoxChangelang;


    public RecyclerViewFootAdapter(Context context, HomeDetailEntity dataList) {
        this.dataDetailList = dataList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        userNo = (String) getData(mContext, "userNo", "");

    }


    //创建ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == TYPE_ITEM) {
            //        View v = inflater.inflate(R.layout.detail_info_item, parent, false);
            View v = View.inflate(mContext, R.layout.detail_info_item, null);
            v.setLayoutParams(params);
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);

            return itemViewHolder;


        } else if (viewType == TYPE_FOOTER) {
            View footView = View.inflate(mContext, R.layout.recycle_footview_layout, null);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            footView.setLayoutParams(params);
            FootViewHolder footViewHolder = new FootViewHolder(footView);

            return footViewHolder;
        }

        return null;
    }


    @Override
    public int getItemCount() {
        Logger.d("执行啦。。。。。。。。。");
        List<HomeDetailEntity.DataBean.ListBean> list = dataDetailList.getData().getList();
        return list == null ? 0 : list.size() + 1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView detail_tv;
        public ImageView detail_iv;
        public ImageView iv_point;
        public AutoLinearLayout ll_text_point;
        public View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            detail_tv = (TextView) itemView.findViewById(R.id.detail_tv);
            detail_iv = (ImageView) itemView.findViewById(R.id.detail_iv);
            iv_point = (ImageView) itemView.findViewById(R.id.iv_point);
            ll_text_point = (AutoLinearLayout) itemView.findViewById(R.id.ll_text_point);
            view = (View) itemView.findViewById(R.id.view);

        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_check;
        private RelativeLayout rlDownload;
        private ImageView imageLoadView;
        private TextView textLoadView;

        public FootViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);
            rlDownload = (RelativeLayout) view.findViewById(R.id.rl_download);
            cb_check = (CheckBox) view.findViewById(R.id.cb_check);
            imageLoadView = (ImageView) view.findViewById(R.id.iv_loadImage);
            textLoadView = (TextView) view.findViewById(R.id.tv_load_text);
        }
    }


    @Override
    public int getItemViewType(int position) {//39-----
        // 最后一个item设置为footerView
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;


        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int status = dataDetailList.getData().getStatus();
        lessonId = dataDetailList.getData().getLessonId();


        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).view.setVisibility(View.GONE);
            Logger.d(position);
            int type = dataDetailList.getData().getList().get(position).getType();
            String content = dataDetailList.getData().getList().get(position).getContent();

            if (type == 0) {
                //wen ben
                ((ItemViewHolder) holder).ll_text_point.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).iv_point.setVisibility(VISIBLE);//显示
                ((ItemViewHolder) holder).detail_tv.setVisibility(VISIBLE);
                ((ItemViewHolder) holder).detail_tv.setText(content);
                ((ItemViewHolder) holder).view.setVisibility(View.GONE);
                ((ItemViewHolder) holder).detail_iv.setVisibility(View.GONE);
            } else if (type == 1) {
                //图片
                ((ItemViewHolder) holder).ll_text_point.setVisibility(View.GONE);
//                ((ItemViewHolder) holder).detail_tv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).detail_iv.setVisibility(VISIBLE);
//                ((ItemViewHolder) holder).iv_point.setVisibility(View.INVISIBLE);
                GlideUtils.glidFormUrltoCache(mContext, content, ((ItemViewHolder) holder).detail_iv);
            } else if (type == 2) {
                ((ItemViewHolder) holder).ll_text_point.setVisibility(View.GONE);
                ((ItemViewHolder) holder).detail_iv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).view.setVisibility(VISIBLE);
            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            int size = (int) SharedPreferencesUtil.getData(mContext, "size", -1);

            String contentLoad = (String) SharedPreferencesUtil.getData(mContext, "content", "");
            String icoUrl = (String) SharedPreferencesUtil.getData(mContext, "icoUrl", "");

            if (size == 0) {
                ((FootViewHolder) holder).rlDownload.setVisibility(View.GONE);
                return;
            } else {
                ((FootViewHolder) holder).rlDownload.setVisibility(View.VISIBLE);
                GlideUtils.glidFormUrltoTx(mContext, icoUrl, footViewHolder.imageLoadView);
                ((FootViewHolder) holder).textLoadView.setText(contentLoad);
                footViewHolder.rlDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, AdvertListActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }


            if (status == 0) {
                footViewHolder.cb_check.setChecked(true);
            } else {
                footViewHolder.cb_check.setChecked(false);
            }
            String able = mContext.  getResources().getConfiguration().locale.getCountry();

            String syslanguage = mContext.getResources().getConfiguration().locale.getLanguage();
            String show_lang = PreferenceUtil.getString("language", syslanguage);
            if (show_lang.equals("zh") || able.equals("CN")) {
                checkBoxChangelang = NetConstants.LANG;//中文
            } else if (show_lang.equals("en") || able.equals("US")) {
                checkBoxChangelang = NetConstants.EN;
            }

       /*     //设置显示字体
            able = mContext.getResources().getConfiguration().locale.getCountry();

            if (able.equals("CN")) {
                checkBoxChangelang = NetConstants.LANG;
            } else if (able.equals("US")) {
                checkBoxChangelang = NetConstants.EN;
            }*/
            check = new CheckStatusRequest();
            footViewHolder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        check.setStatus(0);
                    } else {
                        check.setStatus(1);
                    }

                    //这里做判断哦
                    checkState(checkBoxChangelang);
                }
            });


        }


    }


    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }

    public void checkState(String Lang) {

        check.setUserNo(userNo);
        check.setLessonId(lessonId);
        Gson gson = new Gson();
        //Bean duix request Js

        //toJson
        String json = gson.toJson(check);

        MusicApplication.apiService.checkState(NetConstants.EVENTTYPE_LESSON_STATUS, json, NetConstants.V, Lang, NetConstants.MEDIA_SOURCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<CheckStatusEntity>() {
                    @Override
                    public void onNext(CheckStatusEntity checkStatusEntity) {

                        int err = checkStatusEntity.getErr();
                        if (err == 0) {
                        }
                        String errMsg = checkStatusEntity.getErrMsg();
                    }

                    @Override
                    public void onError(Throwable e) {

                        String message = e.getMessage();
                    }
                });

    }


}