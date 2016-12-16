package com.brainmatics.bpjs.bpjskesehatan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.brainmatics.bpjs.bpjskesehatan.service.BackendService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // menampilkan tulisan di log
        Log.d(TAG, "Menjalankan onCreate dalam LoginActivity");

        // kirim token ke server
        BpjsDbHelper db = new BpjsDbHelper(this);
        String token = db.getFcmToken();
        if(token != null){
            Log.d(TAG, "Token dari Shared Pref : "+token);
            BackendService backendService = new BackendService();
            try {
                backendService.registrasiToken(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Log.d(TAG, "Delete token");
                FirebaseInstanceId firebase = FirebaseInstanceId.getInstance();
                if(firebase != null) {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                }
                Log.d(TAG, "Request token baru");
                FirebaseInstanceId.getInstance().getToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnLoginClicked(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void btnRegistrasiClicked(View v){

    }
}
