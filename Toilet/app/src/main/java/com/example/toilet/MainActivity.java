package com.example.toilet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.graphics.Color;
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
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

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
    Boolean toilet_range = false;
    Boolean trash_range = false;
    int fragCheck = R.id.toilet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("뿡뿡이");
        toolbar.setTitleTextAppearance(this,R.style.TextAppearance_Toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFEFC"));
        setSupportActionBar(toolbar);
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
        fragCheck = R.id.toilet;
        transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.toilet:
                    fragCheck = R.id.toilet;
                    transaction.detach(fragment_trash).commitNow();
                    transaction.attach(fragment_toilet).commitNow();
                    transaction.replace(R.id.main_layout, fragment_toilet).commitAllowingStateLoss();
                    break;
                case R.id.trash:
                    fragCheck = R.id.trash;
                    transaction.detach(fragment_toilet).commitNow();
                    transaction.attach(fragment_trash).commitNow();
                    transaction.replace(R.id.main_layout, fragment_trash).commitAllowingStateLoss();
                    break;
                default:
                    break;
            }
            return true;
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_tracking && fragCheck == R.id.toilet &&!toilet_range) {
            MapView mMapView = ToiletFragment.mapView;
            GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
            MapCircle circle1 = new MapCircle(
            MapPoint.mapPointWithGeoCoord(gpsTracker.getLatitude(), gpsTracker.longitude), // center
                    460, // radius
                    Color.argb(128, 255, 255, 255), // strokeColor
                    Color.argb(128, 205, 220, 57) // fillColor
            );
            circle1.setTag(1234);
            mMapView.addCircle(circle1);
            showDistDialog();
            toilet_range = !toilet_range;
        }
        else if(id == R.id.action_tracking && fragCheck == R.id.toilet && toilet_range) {
            MapView mMapView = ToiletFragment.mapView;

            if (mMapView.getPolylines() != null) {
                mMapView.removeAllPolylines();
            }
            mMapView.removeAllCircles();
            toilet_range = !toilet_range;
        }
        else if(id == R.id.action_tracking && fragCheck == R.id.trash && !trash_range) {
            MapView mMapView = TrashFragment.mapView;
            GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
            MapCircle circle1 = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(gpsTracker.getLatitude(), gpsTracker.longitude), // center
                    460, // radius
                    Color.argb(128, 255, 255, 255), // strokeColor
                    Color.argb(128, 205, 220, 57) // fillColor
            );
            circle1.setTag(1234);
            mMapView.addCircle(circle1);
            showDistDialog();
            trash_range = !trash_range;
        }
        else if(id == R.id.action_tracking && trash_range && fragCheck == R.id.trash) { // action_tracking && trash_range && fragCheck == R.id.trash
            MapView mMapView = TrashFragment.mapView;
            if (mMapView.getPolylines() != null) {
                mMapView.removeAllPolylines();
            }
            mMapView.removeAllCircles();
            trash_range = !trash_range;
        }
        else if(id == R.id.action_tracking && trash_range){
            MapView mMapView = TrashFragment.mapView;
            mMapView.removeAllCircles();
            trash_range = !trash_range;
        } else {
            MapView mMapView = ToiletFragment.mapView;
            mMapView.removeAllCircles();
            toilet_range = !toilet_range;
        }
        switch (id) {
            case R.id.action_waterfall:
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
                if (playCheck == R.id.action_waterfall && !play) {
                    Toast.makeText(this, "재생", Toast.LENGTH_SHORT).show();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_pause));
                    play = true;
                    mediaPlayer = MediaPlayer.create(this,R.raw.waterfall);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                } else if(playCheck == R.id.action_bird && !play){
                    play = true;
                    Toast.makeText(this, "재생", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(this,R.raw.bird);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    item.setIcon(getResources().getDrawable(R.drawable.ic_pause));
                }
                else if(playCheck == R.id.action_waterfall && play){
                    Toast.makeText(this, "일시정지", Toast.LENGTH_SHORT).show();
                    play = false;
                    mediaPlayer.pause();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_play));
                }else if(playCheck == R.id.action_bird && play){
                    Toast.makeText(this, "일시정지", Toast.LENGTH_SHORT).show();
                    play = false;
                    mediaPlayer.pause();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_play));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDistDialog() {
        DistDialog dialog;
        if (fragCheck == R.id.toilet) {
            dialog =  new DistDialog(this, fragment_toilet.requireActivity(),0);

        } else { // trash frag
            dialog =  new DistDialog(this, fragment_trash.requireActivity(),1);

        }
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

    private void showBottomDialog() {
           CustomDialog dialog =  new CustomDialog(this, fragment_toilet.requireActivity());
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