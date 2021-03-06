package musictheory.xinweitech.cn.musictheory.net;


import java.util.concurrent.TimeUnit;

import musictheory.xinweitech.cn.musictheory.constants.ConfigInfo;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by niudong on 2017/1/11.
 */
public class RetrofitManager {

    private static RetrofitManager manager;
    private static ApiService service;
    // 网络请求超时时间
    private static int DEFAULT_TIMEOUT = 5;

    private RetrofitManager() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ConfigInfo.getHOST())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }

    public static RetrofitManager getInstance() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null) {
                    manager = new RetrofitManager();
                }
            }
        }
        return manager;
    }

    public ApiService getService() {
        return service;
    }

    public static void reInit() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ConfigInfo.getHOST())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }
}
