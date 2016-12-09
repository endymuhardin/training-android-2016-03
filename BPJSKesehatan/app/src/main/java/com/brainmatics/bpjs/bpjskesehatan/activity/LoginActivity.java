package com.brainmatics.bpjs.bpjskesehatan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.brainmatics.bpjs.bpjskesehatan.R;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // menampilkan tulisan di log
        Log.d(TAG, "Menjalankan onCreate dalam LoginActivity");
    }
}
