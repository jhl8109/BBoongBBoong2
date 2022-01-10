package com.example.toilet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class DistDialog extends Dialog {

    private ArrayList<Dist> list;
    private DistAdapter distAdapter;
    RecyclerView mRecyclerView;

    public DistDialog(@NonNull Context context, String id) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dist_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<>();
        distAdapter = new DistAdapter(list);

        //대충 id로 리스트에 근처 애들 넣는 코드

        mRecyclerView.setAdapter(distAdapter);
        distAdapter.notifyDataSetChanged();



    }

}
