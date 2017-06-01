package com.chris_guzman.taasty;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by chrisguzman on 5/28/17.
 */

public class TacoDescriptionUtil {

    private final TacoDescriptionApi tacoDescriptionApi;
    private Retrofit retrofit;

    public TacoDescriptionUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/sinker/tacofancy/master/full_tacos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        tacoDescriptionApi = retrofit.create(TacoDescriptionApi.class);
    }


    public Call<String> getRandomTaco(String url) {
        return tacoDescriptionApi.getTacoDescription(url);
    }

}
