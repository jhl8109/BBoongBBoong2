package com.example.toilet;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ReviewDialog extends Dialog {

    private ArrayList<Review> list;
    private RecyclerAdapter recyclerAdapter;
    private EditText et_review;
    private Button btn_review;
    private RatingBar rb_addReview;
    private String _id;
    RecyclerView mRecyclerView;
    int frag;

    public ReviewDialog(@NonNull Context context, String id, int frag) {
        super(context);
        _id = id;
        this.frag = frag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        list = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(list);
        connectingReview(_id);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        btn_review = findViewById(R.id.btn_review);


        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_review = findViewById(R.id.et_review);
                rb_addReview = findViewById(R.id.rb_addReview);
                String text = et_review.getText().toString();
                float addScore = rb_addReview.getRating();
                Review review  = new Review(addScore,text);
                Log.e("review check", text + " " + addScore);
                list.add(review);
                recyclerAdapter = new RecyclerAdapter(list);
                mRecyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.249.18.109:443/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                Call<String> res = retrofitService.putToilet(_id,review.getScore(),review.getComment());
                res.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (response.isSuccessful()) {
                            Log.e("test",result);
                        } else {
                            Log.e("err", call.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("failed","failed");
                        t.printStackTrace();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void connectingReview(String _id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.249.18.109:443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Log.e("_id",_id);
        Call<Result> res;

        if (frag == 0) {
            res = retrofitService.getReviewData(_id);
        } else {
            res = retrofitService.getTrashReviewData(_id);
        }
        res.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (response.isSuccessful()) {
                    Log.e("test", result.getReview().get(0).getComment());
                    list.addAll(result.getReview());
                    Log.e("listSize", String.valueOf(list.size()));
                    recyclerAdapter = new RecyclerAdapter(list);
                    mRecyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }

    public void setOnCancelListener(OnDismissListener onDismissListener) {
        if (frag == 0) {
            ToiletFragment.dialogCheck--;
            Log.e("delete","zzzz");
        } else {
            TrashFragment.dialogCheck--;
            Log.e("delete","zzzz");
        }
    }
}
