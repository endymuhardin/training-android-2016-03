package com.brainmatics.bpjs.bpjskesehatan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BpjsDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAMA_DB = "bpjs.db";

    private static final String CREATE_TBL_PESERTA = "create table "
            + PesertaDb.NAMA_TABEL + "( "
            + PesertaDb._ID +" INT PRIMARY KEY, "
            + PesertaDb.KOLOM_NOMOR +" TEXT, "
            + PesertaDb.KOLOM_NAMA +" TEXT, "
            + PesertaDb.KOLOM_EMAIL +" TEXT, "
            + PesertaDb.KOLOM_TANGGAL_LAHIR +" TEXT, "
            + PesertaDb.KOLOM_FOTO +" TEXT"
            +")";

    private static final String DROP_TBL_PESERTA =
            "drop table if exists "+PesertaDb.NAMA_TABEL;

    public BpjsDbHelper(Context context) {
        super(context, NAMA_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_PESERTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versiLama, int versiBaru) {
        db.execSQL(DROP_TBL_PESERTA);
        onCreate(db);
    }

    public static class PesertaDb implements BaseColumns {
        public static final String NAMA_TABEL = "peserta";
        public static final String KOLOM_NOMOR = "nomor";
        public static final String KOLOM_NAMA = "nama";
        public static final String KOLOM_EMAIL = "email";
        public static final String KOLOM_TANGGAL_LAHIR = "tanggal_lahir";
        public static final String KOLOM_FOTO = "foto";

    }
}
