package musictheory.xinweitech.cn.musictheory.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import musictheory.xinweitech.cn.musictheory.MusicApplication;

public class NetManager {
    Context context;
    private static NetManager netManager;

    public NetManager(Context context) {
        this.context = context;
    }

    /**
     * 判断网络是否打开
     *
     * @return
     */
    public boolean isOpenNetwork() {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    /**
     * 判断WIFI是否打开
     *
     * @return
     */
    public boolean isOpenWifi() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    static {

        netManager = new NetManager(MusicApplication.mContext);
    }

    public static NetManager getInstance() {
        return netManager;
    }

}
