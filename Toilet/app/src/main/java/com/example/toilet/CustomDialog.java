package com.example.toilet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

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

    int selected;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_dialog);
        iv_toilet = findViewById(R.id.iv_toilet);
        iv_trash = findViewById(R.id.iv_trash);
        btn_register = findViewById(R.id.btn_register);
        ratingBar = findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
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
                JSONObject review = {}
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.249.18.109:443/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                Call<Result> res = retrofitService.addToilet(10.0,10.0,new List<Result>new Review(5.0,"good"));
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
                        Log.e("call",call.toString());
                        Log.e("failed","failed");
                        t.printStackTrace();
                    }
                });
                dismiss();
            }
        });

    }
}
