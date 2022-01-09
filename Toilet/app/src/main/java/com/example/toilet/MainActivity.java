package com.example.toilet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment_toilet;
    Fragment fragment_trash;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    MediaPlayer mediaPlayer;
    int playCheck = R.id.action_waterfall;
    Boolean play = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한 설정
        getSupportActionBar().setTitle("뿡뿡이");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));


        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
        //권한 여부 확인


        fragment_toilet = new ToiletFragment();
        fragment_trash = new TrashFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.toilet:
                    transaction.detach(fragment_trash).commitNow();
                    transaction.attach(fragment_toilet).commitNow();
                    transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();
                    break;
                case R.id.trash:
                    transaction.detach(fragment_toilet).commitNow();
                    transaction.attach(fragment_trash).commitNow();
                    transaction.replace(R.id.main_layout, fragment_trash).commitAllowingStateLoss();
                    break;
                default:
                    break;
            }
            return true;
        });
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.action_waterfall:
                Toast.makeText(this, "물소리", Toast.LENGTH_SHORT).show();
                if (playCheck == R.id.action_bird && play) {
                    mediaPlayer.pause();
                    playCheck = R.id.action_waterfall;
                    mediaPlayer = MediaPlayer.create(this,R.raw.waterfall);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_pause));
                } else if (playCheck == R.id.action_bird && !play) {
                    playCheck = R.id.action_waterfall;
                } else {} //(playCheck == R.id.action_waterfall)
                break;

            case R.id.action_bird:
                Toast.makeText(this, "새소리", Toast.LENGTH_SHORT).show();
                if (playCheck == R.id.action_waterfall && play) {
                    playCheck = R.id.action_bird;
                    mediaPlayer.pause();
                    mediaPlayer = MediaPlayer.create(this,R.raw.bird);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_pause));
                } else if (playCheck == R.id.action_waterfall && !play) {
                    playCheck = R.id.action_bird;
                } else {} //(playCheck == R.id.action_bird)
                break;

            case R.id.action_play:
                Toast.makeText(this, "재생", Toast.LENGTH_SHORT).show();
                if (playCheck == R.id.action_waterfall && !play) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_pause));
                    play = true;
                    mediaPlayer = MediaPlayer.create(this,R.raw.waterfall);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                } else if(playCheck == R.id.action_bird && !play){
                    play = true;
                    mediaPlayer = MediaPlayer.create(this,R.raw.bird);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    item.setIcon(getResources().getDrawable(R.drawable.ic_pause));
                }
                else if(playCheck == R.id.action_waterfall && play){
                    play = false;
                    mediaPlayer.pause();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_play));
                }else if(playCheck == R.id.action_bird && play){
                    play = false;
                    mediaPlayer.pause();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_play));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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