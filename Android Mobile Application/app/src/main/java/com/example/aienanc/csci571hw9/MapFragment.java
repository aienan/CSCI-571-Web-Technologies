package com.example.aienanc.csci571hw9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.maps.interfaces.OnAerisMarkerInfoWindowClickListener;
import com.hamweather.aeris.maps.markers.AerisMarker;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.response.EarthquakesResponse;
import com.hamweather.aeris.response.FiresResponse;
import com.hamweather.aeris.response.StormCellResponse;
import com.hamweather.aeris.response.StormReportsResponse;
import com.hamweather.aeris.tiles.AerisTile;

public class MapFragment extends MapViewFragment implements
        OnAerisMapLongClickListener,OnAerisMarkerInfoWindowClickListener, AerisCallback {

    private double lat;
    private double lon;
    private Marker marker;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        mapView.moveToLocation(new LatLng(MapActivity.lat, MapActivity.lon), 9);
        mapView.setOnAerisMapLongClickListener(this);
        mapView.setOnAerisWindowClickListener(this);
        mapView.addLayer(AerisTile.RADSAT);

        return view;
    }

    @Override
    public void onResult(EndpointType type, AerisResponse response){
        if (type == EndpointType.OBSERVATIONS) {
            if (response.isSuccessfulWithResponses()) {

            }
        }
    }

    @Override
    public void wildfireWindowPressed(FiresResponse firesResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void stormCellsWindowPressed(StormCellResponse stormCellResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void stormReportsWindowPressed(StormReportsResponse stormReportsResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void earthquakeWindowPressed(EarthquakesResponse earthquakesResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

}
