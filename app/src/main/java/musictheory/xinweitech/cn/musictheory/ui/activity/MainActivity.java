package musictheory.xinweitech.cn.musictheory.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import musictheory.xinweitech.cn.musictheory.MusicApplication;
import musictheory.xinweitech.cn.musictheory.R;
import musictheory.xinweitech.cn.musictheory.base.BaseActivity;
import musictheory.xinweitech.cn.musictheory.ui.fragment.FirstFragment;
import musictheory.xinweitech.cn.musictheory.ui.fragment.SecondFragment;
import musictheory.xinweitech.cn.musictheory.ui.view.InterceptEventLinearLayout;
import musictheory.xinweitech.cn.musictheory.utils.Md5Utils;

/**
 * Created by niudong on 2017/1/11.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.iell_progress_bar)
    InterceptEventLinearLayout iellProgressBar;
    private Context mContext;
    @BindView(R.id.bt_1)
    Button bt1;
    @BindView(R.id.bt_2)
    Button bt2;
    @BindView(R.id.bt_3)
    Button bt3;
    @BindView(R.id.bt_4)
    Button bt4;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private String encrypt;
    private String s;
    private String loginName;

    //http://blog.csdn.net/taoerchun/article/details/48109599   Json
    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        MusicApplication.setService();
        loginName = "test@163.com";
        encrypt = Md5Utils.encrypt("1");
        s = encrypt.toString();
        Log.i(TAG, "initData: " + encrypt);
    }
    @Override
    protected void initData() {


    }


    //Fragment
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        firstFragment = new FirstFragment();
        Bundle data = new Bundle();
        data.putInt("id", 0);
        firstFragment.setArguments(data);
        adapter.addFrag(firstFragment, "Frist");

        secondFragment = new SecondFragment();
        data = new Bundle();
        data.putInt("id", 1);
        secondFragment.setArguments(data);
        adapter.addFrag(secondFragment, "Second");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iellProgressBar.setVisibility(View.GONE);
    }

}
