package musictheory.xinweitech.cn.musictheory.adapter.groupadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.entity.LessonListEntity;
import musictheory.xinweitech.cn.musictheory.ui.activity.HomeDetailActivity;
import musictheory.xinweitech.cn.musictheory.utils.GlideUtils;

import static android.content.ContentValues.TAG;


/**
 * Created by niudong on 2016/1/25.
 * <p>
 * 分组列表加载数据
 */

public class HomeEntityAdapter extends SectionedRecyclerViewAdapter<HeaderHolder, DescHolder, RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public LessonListEntity.DataBean allLessonList;
    private Context mContext;
    private LayoutInflater mInflater;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private SparseBooleanArray mBooleanMap;//记录下哪个section是被打开的
    private DescHolder descHolder;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;

    public HomeEntityAdapter(Context context) {
        mContext = context;
        Log.d(TAG, "constructor");
        mInflater = LayoutInflater.from(context);
//        mBooleanMap = new SparseBooleanArray();
    }

    //shuju guolai
    public void setData(LessonListEntity.DataBean allTagList) {
        this.allLessonList = allTagList;
//        notifyDataSetChanged();
    }

/*
    public void clearData(ArrayList<HotelEntity.TagsEntity> allLessonList) {
        this.allLessonList = allLessonList;
        allLessonList.clear();
    }*/

    //部门数量
    @Override
    protected int getSectionCount() {

        int count = allLessonList.getList().size();
        return count;
    }

    //列表数量
    @Override
    protected int getItemCountForSection(int section) {
//      int count = allLessonList.get(section).getTagInfoList().size();
        int count = allLessonList.getList().get(section).getDList().size();
        return allLessonList.getList().get(section).getDList().isEmpty() ? 0 : count;
    }

  /*  @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected int getSectionFooterViewType(int section) {
        return super.getSectionFooterViewType(section);

    }*/

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }


    //部门名称
    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.home_head_title, parent, false));
    }

    @Override
    protected FootHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    //添加创建脚布局



    //列表详情
    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        descHolder = new DescHolder(mInflater.inflate(R.layout.home_desc_item, parent, false));


        return descHolder;
    }


    //绑定数据
    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
//        final String name = allLessonList.get(section).tagsName;
        final String categoryName = allLessonList.getList().get(section).getCategory();


        holder.titleView.setText(categoryName);

    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

    }
/*    @Override
    protected void onBindFooterViewHolder(FootHolder holder, int section, int position) {
        FootHolder footViewHolder = (FootHolder) holder;
        int size = allLessonList.getList().get(section).getDList().size();
        Logger.d(size + "-7------------------------------" + section + position);//6

        if (position == size - 1)
            footViewHolder.foot_view_no_more.setVisibility(View.VISIBLE);
    }*/

    @Override
    protected void onBindItemViewHolder(DescHolder holder, int section, final int position) {
        int lessonId = allLessonList.getList().get(section).getDList().get(position).getLessonId();
        final String title = allLessonList.getList().get(section).getDList().get(position).getTitle();
//        boolean checkBoolean = userInfo.getBoolean("" + title, false);
        String icoUrl = allLessonList.getList()
                .get(section).getDList().get(position).getIcoUrl();
//通过显示隐藏分割线
        int size = allLessonList.getList().get(section).getDList().size();
        if (position == size - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }

        Logger.d(size);
//Glide加载突破
        GlideUtils.glidFormUrltoTx(mContext, icoUrl, holder.iv_item);
        holder.descView.setText(title);
        //判断选中状态
        int status = allLessonList.getList().get(section).getDList().get(position).getStatus();
        if (status == 0) {
            holder.iv_status.setVisibility(View.VISIBLE);
        } else {
            holder.iv_status.setVisibility(View.GONE);

        }

/*
        //以前没有接口数据之前的解决办法
        boolean checkStatus = userInfo.getBoolean(title, false);
        //状态和id进行判断   相等的话就选中
        if (checkStatus) {
//                holder.iv_status.setTag(new Integer(position));//设置tag 否则划回来时选中消失
            holder.iv_status.setVisibility(View.VISIBLE);
        } else {

            holder.iv_status.setVisibility(View.INVISIBLE);
        }*/
        //点击跳转详情页    没有关掉当前页面

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDetailActivity.actionStart(mContext, lessonId, title);

                //可能和他有关系
                //用户行为 统计点击列表进入详情页
                MobclickAgent.onEvent(v.getContext(), "m8sic_detail");
                HashMap<String, String> map = new HashMap<>();
                map.put("music_detail", title);
                MobclickAgent.onEvent(v.getContext(), "music_detail", map);


            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
