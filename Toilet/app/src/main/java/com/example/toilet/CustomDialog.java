package com.example.toilet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
                dismiss();
            }
        });

    }
}
