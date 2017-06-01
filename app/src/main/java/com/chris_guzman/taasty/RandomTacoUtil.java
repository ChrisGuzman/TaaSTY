package com.chris_guzman.taasty;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chrisguzman on 5/28/17.
 */

public class RandomTacoUtil {

    private final TacoApi tacoApi;
    private Retrofit retrofit;

    public RandomTacoUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://taco-randomizer.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        tacoApi = retrofit.create(TacoApi.class);
    }


    public Call<Taco> getRandomTaco() {
        return tacoApi.getRandomTaco(true);
    }

}
