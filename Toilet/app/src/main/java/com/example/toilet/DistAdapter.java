package com.example.toilet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DistAdapter extends RecyclerView.Adapter<DistAdapter.DistViewHolder>{
    private ArrayList<Dist> dists;

    public class DistViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_review2;
        protected ImageView iv_review2;
        protected RatingBar rb_review2;

        public DistViewHolder(View view) {
            super(view);
            this.tv_review2 = (TextView) view.findViewById(R.id.tv_review2);
            this.iv_review2 = (ImageView) view.findViewById(R.id.iv_review2);
            this.rb_review2 = (RatingBar) view.findViewById(R.id.rb_review2);
        }
    }

    public DistAdapter(ArrayList<Dist> dists) { this.dists = dists;}

    @NonNull
    @Override
    public DistAdapter.DistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dist_item,parent,false);
        DistViewHolder viewHolder = new DistViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DistAdapter.DistViewHolder holder, int position) {
        holder.tv_review2.setText(dists.get(position).getAddress());
        int score = (int) dists.get(position).getScore();
        holder.rb_review2.setRating((float) dists.get(position).getScore());
        switch (score) {
            case 1:
                holder.iv_review2.setImageResource(R.drawable.face1);
                break;
            case 2:
                holder.iv_review2.setImageResource(R.drawable.face2);
                break;
            case 3:
                holder.iv_review2.setImageResource(R.drawable.face3);
                break;
            case 4:
                holder.iv_review2.setImageResource(R.drawable.face4);
                break;
            case 5:
                holder.iv_review2.setImageResource(R.drawable.face5);
                break;
            default:
                holder.iv_review2.setImageResource(R.drawable.face1);
                break;
        }
    }



    @Override
    public int getItemCount() {
        return dists.size() ;
    }
}