package com.chris_guzman.taasty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chrisguzman on 5/28/17.
 */

public interface TacoApi {
    @GET("random")
    Call<Taco> getRandomTaco(@Query("full-taco") boolean fullTaco);
}
