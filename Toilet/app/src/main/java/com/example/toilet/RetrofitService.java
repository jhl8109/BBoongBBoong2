package com.example.toilet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("toilet")
    Call<ArrayList<Result>> getToilet();

    @POST("toilet/add")
    Call<Result> addToilet(@Body Result result);

    @FormUrlEncoded
    @PUT("toilet/{id}")
    Call<String> putToilet(
            @Path("id") String id,
            @Field("score") double score,
            @Field("comment") String comment
    );

    @GET("toilet/avg/{id}")
    Call<String> getScore(
            @Path("id") String id
    );

    @GET("toilet/id/{id}")
    Call<Result> getReviewData(
            @Path("id") String id
    );

    @GET("v2/local/geo/coord2regioncode.JSON")
    Call<AddrResult> getAddress(
            @Header("Authorization") String key,
            @Query("x") String x,
            @Query("y") String y
    );

    @GET("toilet/score")
    Call<List<ScoreResult>> getAllScores();

    @GET("trash/score")
    Call<List<ScoreResult>> getAllTrashScores();



    @GET("trash")
    Call<ArrayList<Result>> getTrash();

    @POST("trash/add")
    Call<Result> addTrash(@Body Result result);

    @FormUrlEncoded
    @PUT("trash/{id}")
    Call<String> putTrash(
            @Path("id") String id,
            @Field("score") double score,
            @Field("comment") String comment
    );

    @GET("trash/id/{id}")
    Call<Result> getTrashReviewData(
            @Path("id") String id
    );
}
