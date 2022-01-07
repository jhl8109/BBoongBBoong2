package com.example.toilet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("toilet")
    Call<List<Result>> getToilet();
}
