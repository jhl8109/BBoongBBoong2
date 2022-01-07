package com.example.toilet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment_toilet;
    Fragment fragment_trash;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragment_toilet = new ToiletFragment();
        fragment_trash = new TrashFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
        //권한 여부 확인
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        //권한 설정
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.toilet:
                    transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();
                    Log.e("111","111");
                    break;
                case R.id.trash:
                    transaction.replace(R.id.main_layout, fragment_trash).commitAllowingStateLoss();
                    Log.e("222","222");
                    break;
                default:
                    break;
            }
            return true;
        });


    }


    private void showBottomDialog() {
           CustomDialog dialog =  new CustomDialog(this);
           dialog.getWindow().setGravity(Gravity.BOTTOM);

           dialog.setCancelable(true);
           WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
           lp.copyFrom(dialog.getWindow().getAttributes());
           lp.width = WindowManager.LayoutParams.MATCH_PARENT;
           lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
           dialog.show();
           Window window = dialog.getWindow();
           window.setAttributes(lp);
           window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

}