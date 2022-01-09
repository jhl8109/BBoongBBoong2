package com.example.toilet;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    private ArrayList<Review> reviews;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_review;
        protected ImageView iv_review;
        protected RatingBar rb_review;

        public CustomViewHolder(View view) {
            super(view);
            this.tv_review = (TextView) view.findViewById(R.id.tv_review);
            this.iv_review = (ImageView) view.findViewById(R.id.iv_review);
            this.rb_review = (RatingBar) view.findViewById(R.id.rb_review);
        }
    }
    public RecyclerAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public RecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.CustomViewHolder holder, int position) {
        holder.tv_review.setText(reviews.get(position).getComment());
        holder.rb_review.setRating((float) reviews.get(position).getScore());
        int ratingScore = (int) reviews.get(position).getScore();
        Log.e("ratingScore", String.valueOf(ratingScore));
        switch (ratingScore) {
            case 1:
                holder.iv_review.setImageResource(R.drawable.face1);
                break;
            case 2:
                holder.iv_review.setImageResource(R.drawable.face2);
                break;
            case 3:
                holder.iv_review.setImageResource(R.drawable.face3);
                break;
            case 4:
                holder.iv_review.setImageResource(R.drawable.face4);
                break;
            case 5:
                holder.iv_review.setImageResource(R.drawable.face5);
                break;
            default:
                holder.iv_review.setImageResource(R.drawable.face1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (null!=reviews ? reviews.size() : 0);
    }
}
