package com.brainmatics.bpjs.bpjskesehatan;

import android.app.Application;
import android.content.ContentValues;
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

        ContentValues data = new ContentValues();
        data.put(BpjsDbHelper.PesertaDb.KOLOM_ID, "abc123");
        data.put(BpjsDbHelper.PesertaDb.KOLOM_NOMOR, "100100100");
        data.put(BpjsDbHelper.PesertaDb.KOLOM_NAMA, "Peserta 100");
        data.put(BpjsDbHelper.PesertaDb.KOLOM_EMAIL, "p100@example.com");
        data.put(BpjsDbHelper.PesertaDb.KOLOM_TANGGAL_LAHIR, "2010-10-10");
        data.put(BpjsDbHelper.PesertaDb.KOLOM_FOTO, "f100.jpg");
        database.insert(BpjsDbHelper.PesertaDb.NAMA_TABEL, null, data);

        ContentValues tagihan = new ContentValues();
        tagihan.put(BpjsDbHelper.TagihanDb.KOLOM_ID, "t123");
        tagihan.put(BpjsDbHelper.TagihanDb.KOLOM_ID_PESERTA, "abc123");
        tagihan.put(BpjsDbHelper.TagihanDb.KOLOM_TANGGAL_TAGIHAN, "2016-01-01");
        tagihan.put(BpjsDbHelper.TagihanDb.KOLOM_TANGGAL_JATUH_TEMPO, "2016-01-20");
        tagihan.put(BpjsDbHelper.TagihanDb.KOLOM_NILAI, "123000.00");
        database.insert(BpjsDbHelper.TagihanDb.NAMA_TABEL, null, tagihan);

        db.close();
    }
}
