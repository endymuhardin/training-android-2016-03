package com.brainmatics.bpjs.bpjskesehatan.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.activity.MainActivity;
import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Peserta;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;
import com.brainmatics.bpjs.bpjskesehatan.service.BackendService;
import com.brainmatics.bpjs.bpjskesehatan.service.TerimaFirebaseMessageService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesertaFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PesertaFragment";
    private static final int REQUEST_FOTO = 101;

    private GoogleApiClient googleApiClient;
    private Location lokasiSaatIni;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtAlamat;
    private ImageView imgFoto;
    private File fotoAsli;

    private String lokasiFotoFullSize;

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
        txtAlamat = (TextView) fragmentView.findViewById(R.id.txtAlamat);

        imgFoto = (ImageView) fragmentView.findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ambilFoto(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnSimpan = (Button) fragmentView.findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSimpanClicked(view);
            }
        });

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
            updateAlamat();
        }
    }

    private void updateAlamat(){

        try {
            Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> hasil = geo.getFromLocation(lokasiSaatIni.getLatitude(), lokasiSaatIni.getLongitude(), 1);
            if(!hasil.isEmpty()){
                Address alamat = hasil.get(0);
                txtAlamat.setText(alamat.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // handle image diklik
    public void ambilFoto(View v) throws IOException {
        Log.d(TAG, "Ambil foto");

        // 1. Siapkan file penampungan
        File folderPenyimpanan = getActivity()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d(TAG, "Files dir : "+Environment.DIRECTORY_PICTURES);
        String namaFile = UUID.randomUUID().toString();
        fotoAsli = File.createTempFile(namaFile, ".jpg", folderPenyimpanan);
        lokasiFotoFullSize = fotoAsli.getAbsolutePath();

        // 2. Jalankan kamera untuk mengambil foto
        Intent ambilFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(ambilFoto.resolveActivity(getActivity().getPackageManager()) != null){
            Uri uriFoto = FileProvider.getUriForFile(getActivity(),
                    "com.brainmatics.bpjs.bpjskesehatan.fileprovider",
                    fotoAsli);
            ambilFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
            startActivityForResult(ambilFoto, REQUEST_FOTO);
        }

        // 3. Tampilkan di imageview
        // lakukan di method onActivityResult
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_FOTO == requestCode) {
            if(resultCode != Activity.RESULT_OK) {
                Log.d(TAG, "Gagal ambil foto");
                return;
            }
            tampilkanFoto();
            uploadFoto();
        }
    }

    private void tampilkanFoto() {
        // foto selesai diambil
        Log.d(TAG, "Lokasi foto yang ukuran asli ada di "+lokasiFotoFullSize);
        BitmapFactory.Options opsi = new BitmapFactory.Options();
        opsi.inJustDecodeBounds = true; // ambil metadata aja, jangan load filenya
        BitmapFactory.decodeFile(lokasiFotoFullSize, opsi);
        Log.d(TAG, "Ukuran asli : "+opsi.outWidth +" x "+opsi.outHeight);

        // resize foto
        int ukuranFoto = Math.min(opsi.outHeight, opsi.outWidth);
        int skala = 1;
        int ukuranAkhir = ukuranFoto;

        while(ukuranAkhir > 80){
            skala *= 2;
            ukuranAkhir = ukuranAkhir / 2;
        }

        Log.d(TAG, "Skala : " + skala);

        // sekarang baru load fotonya
        opsi.inJustDecodeBounds = false;
        opsi.inSampleSize = skala;

        Bitmap fotoKecil = BitmapFactory.decodeFile(lokasiFotoFullSize, opsi);
        imgFoto.setImageBitmap(fotoKecil);
    }

    private void uploadFoto() {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Peserta peserta = new Peserta();
                peserta.setNomor(UUID.randomUUID().toString());
                peserta.setNama("Peserta Dummy");
                peserta.setEmail("p@p.com");
                peserta.setFoto("foto.jpg");
                peserta.setTanggalLahir(new Date());
                try {
                    new BackendService(getActivity())
                            .simpanPeserta(peserta, fotoAsli);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Koneksi Google API Client gagal");
    }

    public void btnSimpanClicked(View v){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... id) {
                String nama = updateTagihan(id[0]);
                return nama;
            }

            @Override
            protected void onPostExecute(String nama) {
                tampilkanNotifikasi(nama);

            }

            private String updateTagihan(String peserta) {
                try {
                    BackendService service = new BackendService(getActivity());
                    Page<Tagihan> hasil = service.semuaTagihan(peserta);
                    Log.d(TAG, "Jumlah data : "+hasil.getTotalElements());
                    List<Tagihan> data = hasil.getContent();

                    BpjsDbHelper db = new BpjsDbHelper(getActivity());
                    if(!data.isEmpty()){
                        db.kosongkanTabelTagihan();
                    }
                    for(Tagihan t : data){
                        Log.d(TAG, "Tanggal Tagihan : "+t.getTanggalTagihan());
                        Log.d(TAG, "Nilai Tagihan : "+t.getNilai());
                        db.insertTagihan(t);
                        return t.getPeserta().getNama();
                    }
                } catch (Exception err){
                    Log.e(TAG, err.getMessage());
                }
                return null;
            }

            private void tampilkanNotifikasi(String nama) {
                // activity yang mau ditampilkan pada saat notifikasi diklik
                Intent screenTagihan = new Intent(getActivity(), MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(screenTagihan);
                PendingIntent tagihanPendingIntent =
                        stackBuilder.getPendingIntent(0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                // tampilkan notifikasi
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.logo_only)
                                .setContentTitle("Tagihan Baru")
                                .setContentText("Ada tagihan baru untuk peserta "
                                        +nama);
                mBuilder.setContentIntent(tagihanPendingIntent);
                mBuilder.setAutoCancel(true);
                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                int notificationId = 100;
                mNotificationManager.notify(notificationId, mBuilder.build());
            }
        }.execute("p001");
    }

}
