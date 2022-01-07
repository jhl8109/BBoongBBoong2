package com.example.toilet;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToiletFragment extends Fragment {
    static final int PERMISSIONS_REQUEST_READ_LOCATION = 0x00000001;

    ArrayList<DataMarker> dataMarkers = new ArrayList<DataMarker>();
    MapPoint centerPoint;
    MapPOIItem currentLocationMarker = new MapPOIItem();
    MapView mapView;
    int tag;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toilet, container, false);
        mapView = new MapView(requireContext());
        ViewGroup mapViewContainer = v.findViewById(R.id.map_view);
        mapView.zoomIn(true);
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

        makeMarker();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("localhost:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<List<Result>> res = retrofitService.getToilet();
        res.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                if (response.isSuccessful()) {
                    Log.e("test",response.body().toString());
                }

            }
            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {

            }
        });
        return v;
    }
    public void makeMarker() {
        dataMarkers.add(new DataMarker("test",centerPoint,MapPOIItem.MarkerType.BluePin));
        //Log.e("centerPoint",centerPoint.toString());
        MapPoint mapPoint =  MapPoint.mapPointWithGeoCoord(37.5514579595, 126.951949155);
        MapPOIItem marker = new MapPOIItem();
        for(int i = 0; i<dataMarkers.size(); i++) {
            marker.setItemName(dataMarkers.get(i).getIterName());
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(dataMarkers.get(i).getMarkerType());
            mapView.addPOIItem(marker);
        }

    }
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        String place_name = mapPOIItem.getItemName();
        tag = mapPOIItem.getTag();
        Toast.makeText(requireContext(), "클릭 감지", Toast.LENGTH_LONG).show();

    }
}

