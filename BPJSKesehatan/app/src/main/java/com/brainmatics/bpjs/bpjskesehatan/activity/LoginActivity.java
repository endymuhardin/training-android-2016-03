package com.brainmatics.bpjs.bpjskesehatan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.brainmatics.bpjs.bpjskesehatan.service.BackendService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        // menampilkan tulisan di log
        Log.d(TAG, "Menjalankan onCreate dalam LoginActivity");

        // kirim token ke server
        BpjsDbHelper db = new BpjsDbHelper(this);
        String token = db.getFcmToken();
        if(token != null){

            Log.d(TAG, "Token dari Shared Pref : "+token);

            new AsyncTask<String, Void, Void>(){

                @Override
                protected Void doInBackground(String... token) {
                    BackendService backendService = new BackendService(LoginActivity.this);
                    try {
                        backendService.registrasiToken(token[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(token);

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
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... input) {
                String username = input[0];
                String password = input[1];

                BackendService service = new BackendService(LoginActivity.this);
                return service.login(username, password);
            }

            @Override
            protected void onPostExecute(String token) {
                if(token == null) {
                    Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "OAuth Token : "+token);
                new BpjsDbHelper(LoginActivity.this).simpanOauthToken(token);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        }.execute(username, password);
    }

    public void btnRegistrasiClicked(View v){

    }
}
