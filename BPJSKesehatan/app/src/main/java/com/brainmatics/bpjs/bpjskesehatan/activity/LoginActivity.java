package com.brainmatics.bpjs.bpjskesehatan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;
import com.brainmatics.bpjs.bpjskesehatan.service.BackendService;

import java.util.List;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // menampilkan tulisan di log
        Log.d(TAG, "Menjalankan onCreate dalam LoginActivity");
    }

    public void btnLoginClicked(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void btnRegistrasiClicked(View v){

        new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {

                            BackendService service = new BackendService();
                            Page<Tagihan> hasil = service.semuaTagihan("p001");
                            Log.d(TAG, "Jumlah data : "+hasil.getTotalElements());
                            List<Tagihan> data = hasil.getContent();

                            BpjsDbHelper db = new BpjsDbHelper(LoginActivity.this);
                            if(!data.isEmpty()){
                                db.kosongkanTabelTagihan();
                            }

                            for(Tagihan t : data){
                                Log.d(TAG, "Tanggal Tagihan : "+t.getTanggalTagihan());
                                Log.d(TAG, "Nilai Tagihan : "+t.getNilai());
                                db.insertTagihan(t);
                            }
                        } catch (Exception err){
                            Log.e(TAG, err.getMessage());
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(LoginActivity.this, "Request Tagihan Selesai",
                                Toast.LENGTH_LONG).show();
                    }
        }.execute();

    }
}
