package com.example.toilet;

import org.json.JSONArray;
import org.json.JSONObject;

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
    @POST("toilet/add")
    Call<Result> addToilet(@Body Result result);
    @POST("toilet/add")
    Call<Result> addTrash(@Body Result result);
}
