package com.example.toilet;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.MapViewEventListener;

import java.util.ArrayList;

public class ToiletFragment extends Fragment {

    ArrayList<DataMarker> dataMarkers = new ArrayList<DataMarker>();
    MapPoint centerPoint;
    MapPOIItem currentLocationMarker = new MapPOIItem();
    MapView mapView;

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
        mapViewContainer.addView(mapView);
        mapView.zoomIn(true);
        mapView.setCurrentLocationEventListener(new MapView.CurrentLocationEventListener() {
            @Override
            public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
                if (mapPoint != null) {
                    // 현재 위치 업데이트
                    Toast.makeText(requireContext(), "이동 감지", Toast.LENGTH_LONG).show();
                    centerPoint = mapPoint;
                    currentLocationMarker.moveWithAnimation(mapPoint, false);
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
                //Toast.makeText(requireContext(), "단말의 각도 값 요청", Toast.LENGTH_SHORT).show();
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
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        makeMarker();
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
}