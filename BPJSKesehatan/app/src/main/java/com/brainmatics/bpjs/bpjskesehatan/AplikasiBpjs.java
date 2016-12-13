package com.brainmatics.bpjs.bpjskesehatan;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.facebook.stetho.Stetho;

public class AplikasiBpjs extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        BpjsDbHelper db = new BpjsDbHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("delete from peserta");
        database.execSQL("insert into peserta (_id, nomor, nama, email, tanggal_lahir, foto) values ('abc', '001', 'Peserta 001', 'peserta@gmail.com', '2010-10-10', 'foto.jpg')");
        db.close();
    }
}
