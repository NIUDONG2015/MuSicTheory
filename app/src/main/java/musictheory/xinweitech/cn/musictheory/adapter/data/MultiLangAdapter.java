package musictheory.xinweitech.cn.musictheory.adapter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.List;

import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.entity.LangCheckBean;
import musictheory.xinweitech.cn.musictheory.entity.MultiLanguageEntity;

import static android.content.Context.MODE_PRIVATE;
import static musictheory.xinweitech.cn.musictheory.R.id.view_line;


public class MultiLangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static HashMap<Integer, Boolean> isselected;

    private Context mContext;
    //默认第几项
    //点击的位置     1 he 2;
    private OnItemClickListener mOnItemClickListener;

    private MultiLanguageEntity.DataBean mdataList;
    private List<LangCheckBean> dataBean;
    private static final String LANGUAGE = "language";
    private SharedPreferences userInfo;//用户信息缓存
    private int mPosition;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

/*    public MultiLangAdapter(MultiLanguageEntity.DataBean dataBean, Context context) {
        userInfo = context.getSharedPreferences("multilang", MODE_PRIVATE);
        mdataList = dataBean;
        this.mContext = context;
        mPosition = userInfo.getInt("position", 0);

    } */

    public MultiLangAdapter(List<LangCheckBean> dataBean, Context context) {
        userInfo = context.getSharedPreferences("multilang", MODE_PRIVATE);
        this.dataBean = dataBean;
        this.mContext = context;
        mPosition = userInfo.getInt("position", 0);
        Logger.d(dataBean+"00000000000000000000000"+mPosition);

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        View langView;
        private ImageView langImage;
        private TextView langName;
        private RelativeLayout rlLang;
        private View viewLine;


        public ItemViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);
            langView = view;
            langImage = (ImageView) view.findViewById(R.id.iv_item_lang);
            langName = (TextView) view.findViewById(R.id.tv_lang);
            rlLang = (RelativeLayout) view.findViewById(R.id.rl_lang_item);
            viewLine = (View) view.findViewById(R.id.view_line);
        }
    }


    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlBottom;


        public FootViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);
            rlBottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        }
    }


    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public int getItemCount() {
        Logger.d(dataBean.size()+ "------------" );
//        return mdataList.getList() == null ? 0 : mdataList.getList().size();//Item+Foot
        return dataBean == null ? 0 : dataBean.size();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang_checkbox, parent, false);
            final ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang_foot, parent, false);
            final FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String key = dataBean.get(position).getKey();
        String name = dataBean.get(position).getName();

        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).langName.setText(name);
            int size = dataBean.size();
            if (position == size - 1) {
                ((ItemViewHolder) holder).viewLine.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).viewLine.setVisibility(View.VISIBLE);
            }

            //外边回显和里边点击
            ((ItemViewHolder) holder).rlLang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position, key);
                }
            });

            if (mPosition == position) {
                ((ItemViewHolder) holder).langImage.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).langImage.setVisibility(View.GONE);
            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
        }


  /*      List<MultiLanguageEntity.DataBean.ListBean> list = mdataList.getList();

        String itemKey = list.get(position).getItemKey();
        String label = list.get(position).getLabel();//显示值
        int weight = list.get(position).getWeight();
        Logger.d(itemKey + label + weight);

        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).langName.setText(label);
            int size = mdataList.getList().size();
            if (position == size - 2) {
                ((ItemViewHolder) holder).viewLine.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).viewLine.setVisibility(View.VISIBLE);
            }

            //外边回显和里边点击
            ((ItemViewHolder) holder).rlLang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position, itemKey);
                }
            });

            if (mPosition == position) {
                ((ItemViewHolder) holder).langImage.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).langImage.setVisibility(View.GONE);
            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
        }*/

    }




    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String s);

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;


        }

    }
}