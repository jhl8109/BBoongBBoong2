package com.example.toilet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {
    @GET("toilet")
    Call<Result> getToilet();
    @FormUrlEncoded
    @POST("toilet/add")
    Call<Result> addToilet(
            @Field("lat") Double lat,@Field("lnd") Double lnd,@Field("review") Review review
    );

}
