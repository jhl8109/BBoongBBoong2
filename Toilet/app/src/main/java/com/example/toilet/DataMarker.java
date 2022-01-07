package com.example.toilet;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

public class DataMarker {
    private String iterName;
    private MapPoint mapPoint;
    private MapPOIItem.MarkerType markerType;

    public DataMarker(String iterName, MapPoint mapPoint, MapPOIItem.MarkerType markerType) {
        this.iterName = iterName;
        this.mapPoint = mapPoint;
        this.markerType = markerType;
    }

    public String getIterName() {
        return iterName;
    }

    public void setIterName(String iterName) {
        this.iterName = iterName;
    }

    public MapPoint getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

    public MapPOIItem.MarkerType getMarkerType() {
        return markerType;
    }

    public void setMarkerType(MapPOIItem.MarkerType markerType) {
        this.markerType = markerType;
    }
}
