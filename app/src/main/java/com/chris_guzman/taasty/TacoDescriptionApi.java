package com.chris_guzman.taasty;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by chrisguzman on 5/28/17.
 */

public interface TacoDescriptionApi {
    @GET
    Call<String> getTacoDescription(@Url String url);
}
