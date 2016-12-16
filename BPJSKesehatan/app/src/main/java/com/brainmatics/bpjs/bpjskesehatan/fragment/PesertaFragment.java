package com.brainmatics.bpjs.bpjskesehatan.fragment;


import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesertaFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PesertaFragment";


    private GoogleApiClient googleApiClient;
    private Location lokasiSaatIni;
    private TextView txtLatitude;
    private TextView txtLongitude;

    public PesertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupGooglePlayServices();

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_peserta, container, false);

        txtLatitude = (TextView) fragmentView.findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) fragmentView.findViewById(R.id.txtLongitude);

        return fragmentView;
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void setupGooglePlayServices() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLokasi();
    }

    public void updateLokasi(){
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_GPS_PERMISSION);
            Log.d(TAG, "Belum punya permission untuk mengakses GPS");
            return;
        } else {
            lokasiSaatIni = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.d(TAG, "Mendapatkan posisi GPS : " + lokasiSaatIni);

            txtLatitude.setText(String.valueOf(lokasiSaatIni.getLatitude()));
            txtLongitude.setText(String.valueOf(lokasiSaatIni.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
