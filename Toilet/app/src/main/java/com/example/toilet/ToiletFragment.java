package com.example.toilet;

import android.Manifest;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.Rating;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToiletFragment extends Fragment {


    static MapView mapView = null;
    static final int PERMISSIONS_REQUEST_READ_LOCATION = 0x00000001;

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
        ArrayList<DataMarker> dataMarkers = new ArrayList<DataMarker>();
    MapPoint centerPoint;
    MapPOIItem currentLocationMarker = new MapPOIItem();
    ViewGroup ct;
    String tempAddr;
    String score;
    public ToiletFragment() {
        // Required empty public constructor
    }
    public static ToiletFragment newInstance(String param1, String param2) {
        ToiletFragment fragment = new ToiletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        //new MarkerEventListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ct = container;
        View v = inflater.inflate(R.layout.fragment_toilet, container, false);
        mapView = new MapView(requireContext());
        ViewGroup mapViewContainer = v.findViewById(R.id.map_view);
        mapView.zoomIn(true);
        mapView.setPOIItemEventListener(poiItemEventListener);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setCurrentLocationEventListener(new MapView.CurrentLocationEventListener() {
            @Override
            public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
                if (mapPoint != null) {
                    // 현재 위치 업데이트
                    Toast.makeText(requireContext(), "이동 감지", Toast.LENGTH_LONG).show();
                    centerPoint = mapPoint;
                    currentLocationMarker.moveWithAnimation(mapPoint, true);
                    currentLocationMarker.setAlpha(1F);
                    if (mapView != null) {
                        mapView.setMapCenterPoint(
                                MapPoint.mapPointWithGeoCoord(
                                        mapPoint.getMapPointGeoCoord().latitude,
                                        mapPoint.getMapPointGeoCoord().longitude
                                ), true
                        );
                    }
                }
            }

            @Override
            public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

            }

            @Override
            public void onCurrentLocationUpdateFailed(MapView mapView) {
                Toast.makeText(requireContext(), "위치 정보를 불러오는데 실패했습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCurrentLocationUpdateCancelled(MapView mapView) {
                Toast.makeText(requireContext(), "위치 요청이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_READ_LOCATION);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        mapViewContainer.addView(mapView);
        connectingServer();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_tracking) {
            Toast.makeText(requireContext(), "Tracking", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeMarker(String id,Double latitude,Double longitude) {
        MapPoint mapPoint =  MapPoint.mapPointWithGeoCoord(latitude, longitude);
        dataMarkers.add(new DataMarker(id,mapPoint,MapPOIItem.MarkerType.BluePin)); //임시방편 코드
        MapPOIItem marker = new MapPOIItem();
        for(int i = 0; i<dataMarkers.size(); i++) {
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.toiletmarker);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f);
            marker.setItemName(dataMarkers.get(i).getIterName());
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            mapView.addPOIItem(marker);
        }
    }
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter{
        private final View mCalloutBalloon;

    public CustomCalloutBalloonAdapter(){
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem){
        String x = String.valueOf(poiItem.getMapPoint().getMapPointGeoCoord().latitude);
        String y = String.valueOf(poiItem.getMapPoint().getMapPointGeoCoord().longitude);
        changeAddress(x,y);
        String addr = tempAddr;
        String id = poiItem.getItemName();
        getAverageScore(id);
        ((TextView) mCalloutBalloon.findViewById(R.id.rating_title)).setText(tempAddr);
        if (score != null) {
            float temp = Float.parseFloat(score);
            Log.e("float", String.valueOf(temp));
            RatingBar ratingBar = mCalloutBalloon.findViewById(R.id.rating_desc);
            ratingBar.setRating(temp);
            ((RatingBar) mCalloutBalloon.findViewById(R.id.rating_desc)).setRating(temp);
        }
        else {
            RatingBar ratingBar = mCalloutBalloon.findViewById(R.id.rating_desc);
            ratingBar.setRating(0.0F);
            ((RatingBar) mCalloutBalloon.findViewById(R.id.rating_desc)).setRating(0.0F);
        }
        //refreshFragment();
        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem poiItem){
        return null;
    }
}
    private MapView.POIItemEventListener poiItemEventListener = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            //sample code 없음
            Log.e("111111111111111","진입");
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
            //balloon touch event
            Log.e("what?","what?");
            showDialog(mapPOIItem.getItemName());
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            //sample code 없음
            Log.e("333333333333","진입");
        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
            //sample code 없음
            Log.i("444444444444444","진입");
        }
    };
    private void showDialog(String id) {
        ReviewDialog dialog =  new ReviewDialog(requireContext(),id);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (!dialog.isShowing()) {
            dialog.show();
        }
        Window window = dialog.getWindow();
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void connectingServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.249.18.109:443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<Result>> res = retrofitService.getToilet();
        res.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                ArrayList<Result> result = response.body();
                if (response.isSuccessful()) {
                    Log.e("test", String.valueOf(result.get(0).getId()));
                    ((AppTest) getActivity().getApplication()).setToiletList(result);
                }
                else {
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for(int i = 0; i<result.size(); i++) {
                    makeMarker(result.get(i).getId(),result.get(i).getLat(),result.get(i).getLng());
                    Log.e("id", String.valueOf(result.get(i).getLat())+ " "+String.valueOf(result.get(i).getLng()));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }
    public void changeAddress(String x, String y) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<AddrResult> res = retrofitService.getAddress("KakaoAK 16b9d5d1f68577d49a3ddcdae9f7c5ca",y,x);
        res.enqueue(new Callback<AddrResult>() {

            @Override
            public void onResponse(Call<AddrResult> call, Response<AddrResult> response) {
                AddrResult result = response.body();
                if (response.isSuccessful()) {
                    tempAddr = result.getDocuments().get(0).getAddress_name();
                    Log.e("kakao test", tempAddr);
                }
                else {
                    Log.e("else called","test");
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<AddrResult> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }
    public void getAverageScore(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.249.18.109:443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<String> res = retrofitService.getScore(id);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (response.isSuccessful()) {
                    Log.e("test", result.toString());
                    score = result;
                }
                else {
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }
    public void refreshFragment(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireFragmentManager().beginTransaction().detach(this).commitNow();
            requireFragmentManager().beginTransaction().attach(this).commitNow();
        } else {
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}


