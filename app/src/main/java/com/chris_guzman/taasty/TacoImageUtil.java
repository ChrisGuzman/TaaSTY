package com.chris_guzman.taasty;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chrisguzman on 5/28/17.
 */

public class TacoImageUtil {

    private final TacoImageApi tacoImageApi;
    private Retrofit retrofit;

    public TacoImageUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cognitive.microsoft.com/bing/v5.0/images/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        tacoImageApi = retrofit.create(TacoImageApi.class);
    }


    public Call<AzureResponse> getTacoImage(String term) {
        return tacoImageApi.getTacoImage(term);
    }

}
