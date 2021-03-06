package com.example.toilet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    int frag;

    public DistDialog(@NonNull Context context, Activity activity, int frag) {
        super(context);
        this.activity = activity;
        this.frag = frag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScoreAll();
        setContentView(R.layout.dist_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        list = new ArrayList<>(); //dist 정보를 담을 배열
        distAdapter = new DistAdapter(list,frag);
        mRecyclerView.setAdapter(distAdapter);
        distAdapter.notifyDataSetChanged();
    }
    public void changeAddress(String x, String y,double scores,String id) {
        if (frag == 0) {
            GpsTracker gpsTracker = new GpsTracker(getContext());
            double curLat = gpsTracker.latitude;
            double curLng = gpsTracker.longitude;
            MapView mMapView = ToiletFragment.mapView;
            MapPolyline polyline = new MapPolyline();
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(x),Double.parseDouble(y)));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(curLat,curLng));
            polyline.setTag(1000);
            polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
            mMapView.addPolyline(polyline);
        } else {
            GpsTracker gpsTracker = new GpsTracker(getContext());
            double curLat = gpsTracker.latitude;
            double curLng = gpsTracker.longitude;
            MapView mMapView = TrashFragment.mapView;
            MapPolyline polyline = new MapPolyline();
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(x),Double.parseDouble(y)));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(curLat,curLng));
            polyline.setTag(1000);
            polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
            mMapView.addPolyline(polyline);
        }
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
                    list.add(new Dist(id,scores,addr[0]));
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
        Call<List<ScoreResult>> res;
        if (frag == 0) { // frag == 0 --> toilet
            res = retrofitService.getAllScores();
        } else { // frag == 1 --> trash
            res = retrofitService.getAllTrashScores();
        }
        res.enqueue(new Callback<List<ScoreResult>>() {
            @Override
            public void onResponse(Call<List<ScoreResult>> call, Response<List<ScoreResult>> response) {
                List<ScoreResult> result = response.body();
                if (response.isSuccessful()) {
                    Log.e("scoreList", String.valueOf(result.get(0).getId()));
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
                changeAddress(String.valueOf(scoreList.get(i).getLat()),String.valueOf(scoreList.get(i).getLng()),scoreList.get(i).getAvg(),scoreList.get(i).getId());
            }
        }
    }
}
