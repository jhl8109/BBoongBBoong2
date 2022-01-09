package com.example.toilet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class TitleActivity extends AppCompatActivity {

    Handler hd = new Handler();

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        permissionCheck();
        }

    private class SplashHandler implements Runnable {
        public void run() {
            startActivity (new Intent(getApplication(), MainActivity.class));
            TitleActivity.this.finish();
        }
    }

    private void permissionCheck(){
        try {
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
            ImageView bbongbbong = findViewById(R.id.bbongbbong);
            ImageView cloud = findViewById(R.id.cloud);
            Animation animc = AnimationUtils.loadAnimation(TitleActivity.this, R.anim.cloud);
            Animation anim = AnimationUtils.loadAnimation(TitleActivity.this, R.anim.bbong);
            bbongbbong.startAnimation(anim);
            cloud.startAnimation(animc);
            hd.postDelayed(new SplashHandler(), 5000);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
            permissionCheck();
        }
    };

}
