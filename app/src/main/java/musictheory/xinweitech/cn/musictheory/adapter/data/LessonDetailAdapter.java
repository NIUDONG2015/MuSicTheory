package musictheory.xinweitech.cn.musictheory.adapter.data;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.entity.LessonDetailEntity;

/**
 * Created by gag on 2017-01-24.  采用ListView 加载
 */
public class LessonDetailAdapter extends BaseAdapter {
    private LessonDetailEntity detailDataFromNet;
    //数据已经传过来啦
    public LessonDetailAdapter(LessonDetailEntity detailDataFromNet) {
        this.detailDataFromNet = detailDataFromNet;

    }


    @Override
    public int getCount() {
        return detailDataFromNet.getData().getList().size();
    }

    @Override
    public Object getItem(int position) {
        return detailDataFromNet.getData().getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        if (convertView == null) {
            convertView = View.inflate(MusicApplication.getInstance().getApplicationContext(), R.layout.detail_info_item, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.detail_iv);
            TextView tv = (TextView) convertView.findViewById(R.id.detail_tv);

            holder1 = new ViewHolder1();
            holder1.tv = tv;
            holder1.iv = iv;
            convertView.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) convertView.getTag();
        }


 /*       Glide.with(MusicApplication.mContext).load(detailDataFromNet.getData().getList().get(position).getIcoUrl()).crossFade(500).into(holder1.iv);
        holder1.tv.setText(detailDataFromNet.getData().getList().get(position).getTitle());
        if (detailDataFromNet.getData().getList().get(position).getStatus() == 1){
            holder1.ck.setChecked(true);
        }else
        {
            holder1.ck.setChecked(false);
        }*/

        return convertView;
    }

    static class ViewHolder1 {
        public ImageView iv;
        public TextView tv;
        public CheckBox ck;
    }
}
