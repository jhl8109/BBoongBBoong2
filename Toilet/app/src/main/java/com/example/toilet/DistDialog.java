package com.example.toilet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DistDialog extends Dialog {

    private ArrayList<Dist> list;
    private DistAdapter distAdapter;
    RecyclerView mRecyclerView;
    Activity activity;
    private ArrayList<Double> scoreList;

    public DistDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScoreAll();
        //changeAddress();
        setContentView(R.layout.dist_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<>(); //dist 정보를 담을 배열
        distAdapter = new DistAdapter(list);

        mRecyclerView.setAdapter(distAdapter);
        distAdapter.notifyDataSetChanged();
    }
    public void changeAddress(String x, String y,double scores) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final String[] addr = new String[1];
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<AddrResult> res = retrofitService.getAddress("KakaoAK 16b9d5d1f68577d49a3ddcdae9f7c5ca",y,x);
        res.enqueue(new Callback<AddrResult>() {
            @Override
            public void onResponse(Call<AddrResult> call, Response<AddrResult> response) {
                AddrResult result = response.body();
                if (response.isSuccessful()) {
                    addr[0] = result.getDocuments().get(0).getAddress_name();
                    list.add(new Dist(scores,addr[0]));
                    Collections.sort(list);
                    mRecyclerView.setAdapter(distAdapter);
                    distAdapter.notifyDataSetChanged();
                }
                else {
                    Log.e("else called","test");
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<AddrResult> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }
    public void getScoreAll() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.249.18.109:443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<List<ScoreResult>> res = retrofitService.getAllScores();
        res.enqueue(new Callback<List<ScoreResult>>() {
            @Override
            public void onResponse(Call<List<ScoreResult>> call, Response<List<ScoreResult>> response) {
                List<ScoreResult> result = response.body();
                if (response.isSuccessful()) {
                    Log.e("scoreList", String.valueOf(result.size()));
                    Log.e("scoretest", String.valueOf(result.get(0).getAvg()));
                    rangeCheck(result);
                }
                else {
                    Log.e("else called","test");
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ScoreResult>> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }
    public void rangeCheck(List<ScoreResult> scoreList) {
        GpsTracker gpsTracker = new GpsTracker(getContext());
        double curLat = gpsTracker.latitude;
        double curLng = gpsTracker.longitude;
        for (int i = 0; i < scoreList.size(); i++) {
            double lat = scoreList.get(i).getLat();
            double lng = scoreList.get(i).getLng();
            double tmpLat = Math.abs(lat-curLat);
            double tmpLng = Math.abs(lng-curLng);
            double tmp = Math.pow(tmpLat,2)+Math.pow(tmpLng,2);
            if (Math.sqrt(tmp)<0.005) {
                changeAddress(String.valueOf(scoreList.get(i).getLat()),String.valueOf(scoreList.get(i).getLng()),scoreList.get(i).getAvg());
            }
        }
    }
}
