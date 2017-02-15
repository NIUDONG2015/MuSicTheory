package musictheory.xinweitech.cn.musictheory.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by 35876 于萌萌
 * 创建日期: 22:54 . 2017年01月26日
 * 描述:
 * 模板代码
 * edit = (EditText) findViewById(R.id.edit);
 * <p>
 * edit.addTextChangedListener(new MyTextWatcher(new MyTextWatcher.CallBack() {
 *
 * @Override public void textInfo(String s, int count) {
 * Log.i("MainActivity", "textInfo: "+"内容是"+s+"\t长度是\t"+count);
 * }
 * }));
 * <p>
 * <p>
 * 备注: textView 的输入监听
 */

public class MyTextWatcher implements TextWatcher {
    public CallBack mCallBack;

    public interface CallBack {
        void textInfo(String s, int count);

    }

    public MyTextWatcher(CallBack callBack) {
        mCallBack = callBack;
    }

    public MyTextWatcher() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null != mCallBack)
            mCallBack.textInfo(s.toString(), s.length());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
