package musictheory.xinweitech.cn.musictheory.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by niudong on 2017/2/1.
 */


public class PrefercesService {
    private Context context;

    public PrefercesService(Context context) {
        super();
        this.context = context;
    }

    /**
     * 保存参数
     * prefercesService.save(nameString,Integer.parseInt(ageString));
     *
     * @param name 姓名
     * @param age  年龄
     */
    public void save(String name, int age) {
        //第一个参数 指定名称 不需要写后缀名 第二个参数文件的操作模式
        SharedPreferences preferences = context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
        //取到编辑器
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putInt("age", age);
        //把数据提交给文件中
        editor.commit();
    }

    /**
     * 获取各项配置参数
     * <p>
     * 回显
     * <p>
     * Map<String,String> params=prefercesService.getPreferences();
     * name.setText(params.get("name"));
     * age.setText(params.get("age"));
     *
     * @return
     */
    public Map<String, String> getPreferences() {
        SharedPreferences pre = context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
        //如果得到的name没有值则设置为空 pre.getString("name", "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", pre.getString("name", ""));
        params.put("age", String.valueOf(pre.getInt("age", 0)));

        return params;


    }
}

