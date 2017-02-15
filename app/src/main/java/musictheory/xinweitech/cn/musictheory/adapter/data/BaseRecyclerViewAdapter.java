package musictheory.xinweitech.cn.musictheory.adapter.data;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Random;

/**
 * 抽取Base基类
 * Created by cdm on 2016/7/1.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private static final int HEADER_ITEM = 1;
    private static final int NORMAL_ITEM = 2;

    protected List<T> datas;
    protected View mHeader;
    protected Context mContext;

    public BaseRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setHeader(View mHeader) {
        this.mHeader = mHeader;
        notifyItemInserted(0);
    }


    public void setData(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder{

        public View holderView;

        public BaseItemViewHolder(View itemView) {
            super(itemView);
            holderView = itemView;
            initChildView(holderView);
        }

        public abstract void initChildView(View view);

        public abstract void bindData(T str);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeader != null && position == 0){
            return HEADER_ITEM;
        }
        return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        if (datas != null){
            if (mHeader != null){
                return datas.size() + 1;
            }
            return datas.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);

        if (viewType == HEADER_ITEM){
            mHeader.setLayoutParams(params);
            return new HeaderViewHolder(mHeader);
        }

        View view = createView();
        params.height = new Random().nextInt(30) + 35;
        view.setLayoutParams(params);
        return getViewHolder(view);
    }

    public abstract BaseItemViewHolder getViewHolder(View view);

    public abstract View createView();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mHeader != null && position == 0){
            return;
        }
        if (mHeader != null){
            position = position - 1;
        }

        T str = datas.get(position);
        BaseItemViewHolder itemViewHolder = (BaseItemViewHolder) holder;
        itemViewHolder.bindData(str);

        final int finalPosition = position;
        ((BaseItemViewHolder) holder).holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, finalPosition);
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mHeader != null && position == 0){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            if (mHeader != null && holder.getLayoutPosition() == 0){
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    public void setOnItemClickedListener(onItemClickedListener mListener) {
        this.mListener = mListener;
    }

    private onItemClickedListener mListener;

    public interface onItemClickedListener{
        void onItemClick(View view, int position);
    }

}
