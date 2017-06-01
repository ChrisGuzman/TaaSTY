package com.chris_guzman.taasty;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by chrisguzman on 5/28/17.
 */

public interface TacoImageApi {
    @Headers({"Ocp-Apim-Subscription-Key: YOUR_KEY", "Content-Type: multipart/form-data"})
    @GET("search")
    Call<AzureResponse> getTacoImage(@Query("q") String term);
}
