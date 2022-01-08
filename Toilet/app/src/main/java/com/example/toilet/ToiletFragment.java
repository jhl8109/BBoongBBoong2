package com.example.toilet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
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
    ViewGroup ct;


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
        makeMarker();
        return v;
    }
    public void makeMarker() {
        dataMarkers.add(new DataMarker("test",centerPoint,MapPOIItem.MarkerType.BluePin));
        //Log.e("centerPoint",centerPoint.toString());
        MapPoint mapPoint =  MapPoint.mapPointWithGeoCoord(37.5514579595, 126.951949155);
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
        ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText("경상남도 양산시 물금읍 신주로 40");
        ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("score");
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
            showDialog();
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
    private void showDialog() {
        ReviewDialog dialog =  new ReviewDialog(requireContext());
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


