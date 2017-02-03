package ru.sample.tabolin.itunepreview.web;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sample.tabolin.itunepreview.BuildConfig;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class WebApi {

    private static volatile WebApi instance;

    public static WebApi get(){
        if (instance == null){
            synchronized (WebApi.class){
                if (instance == null){
                    instance = new WebApi();
                }
            }
        }
        return instance;
    }

    private final WebApiContract webApi;

    private WebApi() {
        webApi = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://itunes.apple.com/")
                .client(createProdApiClient())
                .build()
                .create(WebApiContract.class);
    }

    private OkHttpClient createProdApiClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(createHttpLoggingInterceptor())
                .build();
    }

    private Interceptor createHttpLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    public Single<List<PreviewModel>> loadPreviewListByKeyWord(String keyWord){
        return webApi
                .loadPreviewListByKeyWord(keyWord)
                .flatMap(response -> Single.just(response.results));

    }
}
