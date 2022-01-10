package com.example.toilet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomDialog extends Dialog {

    ImageView iv_toilet;
    ImageView iv_trash;
    Button btn_register;
    RatingBar ratingBar;
    EditText edits;
    GpsTracker gpsTracker = new GpsTracker(getContext());
    int selected;
    Activity activity;
    int idRecoded;

    public CustomDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.bottom_dialog);
        iv_toilet = findViewById(R.id.iv_toilet);
        iv_trash = findViewById(R.id.iv_trash);
        btn_register = findViewById(R.id.btn_register);
        ratingBar = findViewById(R.id.ratingbar);
        iv_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selected) {
                    case 0 :
                        iv_toilet.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clicked_border));
                        selected = R.id.iv_toilet;
                        break;
                    case R.id.iv_toilet :
                        iv_toilet.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.unclicked_border));
                        selected = 0;
                        break;
                    case R.id.iv_trash :
                        iv_trash.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.unclicked_border));
                        iv_toilet.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clicked_border));
                        selected = R.id.iv_toilet;
                        break;
                }
            }
        });
        iv_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selected) {
                    case 0 :
                        iv_trash.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clicked_border));
                        selected = R.id.iv_trash;
                        break;
                    case R.id.iv_toilet :
                        iv_trash.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clicked_border));
                        iv_toilet.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.unclicked_border));
                        selected = R.id.iv_trash;
                        break;
                    case R.id.iv_trash :
                        iv_trash.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.unclicked_border));
                        selected = 0;
                        break;
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar = findViewById(R.id.ratingbar);
                float score = ratingBar.getRating();
                edits = findViewById(R.id.edits);
                String comments = edits.getText().toString();
                double currentLatitude= gpsTracker.getLatitude();
                double currentLongitude = gpsTracker.getLongitude();
                double curLat =  Math.round(currentLatitude*1000)/1000.0;
                double curLng =  Math.round(currentLongitude*1000)/1000.0;
                if (selected== R.id.iv_toilet) {
                    ArrayList<Result> resultList = ((AppTest) activity.getApplication()).getToiletList();
                    for (int i = 0; i < resultList.size(); i++) {
                        double lat = Math.round(resultList.get(i).getLat()*1000)/1000.0;
                        double lng = Math.round(resultList.get(i).getLng()*1000)/1000.0;
                        Log.e("속상해", String.valueOf(lat));
                        if ((lat == curLat) && (lng == curLng)) {
                            idRecoded = i;
                            Log.e("idzzzzz",resultList.get(idRecoded).getId());
                            putServer(resultList.get(idRecoded).getId(),score,comments);
                            dismiss();
                            return;
                        }
                    }
                } else {
                    ArrayList<Result> resultList = ((AppTest) activity.getApplication()).getTrashList();
                    for (int i = 0; i < resultList.size(); i++) {
                        double lat = Math.round(resultList.get(i).getLat()*1000)/1000.0;
                        double lng = Math.round(resultList.get(i).getLng()*1000)/1000.0;
                        Log.e("속상해", String.valueOf(lat));
                        if ((lat == curLat) && (lng == curLng)) {
                            idRecoded = i;
                            Log.e("idzzzzz",resultList.get(idRecoded).getId());
                            putServer(resultList.get(idRecoded).getId(),score,comments);
                            dismiss();
                            return;
                        }
                    }
                }




                if (selected== R.id.iv_toilet) {

                    ArrayList<Review> list = new ArrayList();
                    Result result = new Result();
                    result.setLat(currentLatitude);
                    result.setLng(currentLongitude);
                    list.add(new Review(score,comments));
                    result.setReview(list);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.249.18.109:443/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                    Call<Result> res = retrofitService.addToilet(result);
                    res.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Result result = response.body();
                            if (response.isSuccessful()) {
                                Log.e("test",result.toString());
                            }
                        }
                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.e("failed","failed");
                            t.printStackTrace();
                        }
                    });
                    dismiss();
                } else {
                    ArrayList<Review> list = new ArrayList();
                    Result result = new Result();
                    result.setLat(currentLatitude);
                    result.setLng(currentLongitude);
                    list.add(new Review(score,comments));
                    result.setReview(list);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.249.18.109:443/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                    Call<Result> res = retrofitService.addTrash(result);
                    res.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Result result = response.body();
                            if (response.isSuccessful()) {
                                Log.e("test",result.toString());
                            }
                        }
                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.e("failed","failed");
                            t.printStackTrace();
                        }
                    });
                    dismiss();
                }
            }
        });
    }
    public void putServer(String _id, double score,String comment) {
        if (selected== R.id.iv_toilet) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.249.18.109:443/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> res = retrofitService.putToilet(_id,score,comment);
            res.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    if (response.isSuccessful()) {
                        Log.e("test",result);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("failed","failed");
                    t.printStackTrace();
                }
            });
        }
        else {
            Log.e("thisid?", _id);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.249.18.109:443/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> res = retrofitService.putTrash(_id,score,comment);
            res.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    if (response.isSuccessful()) {
                        Log.e("testxzxxzxx",result);
                    }
                    else {
                        try {
                            Log.e("why?",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("failed","failed");
                    t.printStackTrace();
                }
            });
        }
    }
}
