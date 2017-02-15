package musictheory.xinweitech.cn.musictheory.adapter.groupadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhy.autolayout.utils.AutoUtils;

import musictheory.xinweitech.cn.musictheory.R;

/**
 * Created by niudong on 2017/1/31.
 */

public class FootHolder extends RecyclerView.ViewHolder {
    public LinearLayout foot_view_no_more;

    public FootHolder(View itemView) {
        super(itemView);

        AutoUtils.autoSize(itemView);
        initView();
    }

    private void initView() {
        foot_view_no_more = (LinearLayout) itemView.findViewById(R.id.ll_no_more);
    }


}
