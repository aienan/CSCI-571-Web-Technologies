package com.example.aienanc.csci571hw9;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.AerisProgressListener;
import com.hamweather.aeris.communication.fields.ObservationFields;
import com.hamweather.aeris.communication.loaders.ObservationsTask;
import com.hamweather.aeris.communication.loaders.ObservationsTaskCallback;
import com.hamweather.aeris.communication.parameter.ParameterBuilder;
import com.hamweather.aeris.communication.parameter.PlaceParameter;
import com.hamweather.aeris.model.AerisError;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    public static double lat;
    public static double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        this.lat = intent.getDoubleExtra(ResultActivity.EXTRA_LAT, 0);
        this.lon = intent.getDoubleExtra(ResultActivity.EXTRA_LON, 0);

        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);
        PlaceParameter place = new PlaceParameter(this.lat, this.lon);
        ObservationsTask task = new ObservationsTask(this,
                new ObservationsTaskCallback() {

                    @Override
                    public void onObservationsFailed(AerisError error) {
                        // handle fail here
                    }

                    @Override
                    public void onObservationsLoaded(List responses) {
                        // handle successful loading here.
                    }

                });
        ParameterBuilder builder = new ParameterBuilder()
                .withFields(ObservationFields.ICON)
                .withFilter("day")
                .withLimit(2)
                .withRadius(5)
                .withFrom("-24hours")
                .withTo("now");

        task.requestClosest(place, builder.build());
        task.withProgress(new AerisProgressListener() {

            @Override
            public void hideProgress() {
                //hide a the progress dialog
            }

            @Override
            public void showProgress() {
                //do something like show a progress dialog
            }

        });


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        MapFragment myFragment = new MapFragment();
        fragmentTransaction.add(R.id.MapFrame, myFragment);
        fragmentTransaction.commit();

    }
}
