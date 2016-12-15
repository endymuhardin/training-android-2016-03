package com.brainmatics.bpjs.bpjskesehatan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.brainmatics.bpjs.bpjskesehatan.dto.Peserta;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BpjsDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "BpjsDbHelper";

    private static final int VERSION = 2;
    private static final String NAMA_DB = "bpjs.db";

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    private static final String CREATE_TBL_PESERTA = "create table "
            + PesertaDb.NAMA_TABEL + "( "
            + PesertaDb._ID +" INT PRIMARY KEY, "
            + PesertaDb.KOLOM_ID +" TEXT, "
            + PesertaDb.KOLOM_NOMOR +" TEXT, "
            + PesertaDb.KOLOM_NAMA +" TEXT, "
            + PesertaDb.KOLOM_EMAIL +" TEXT, "
            + PesertaDb.KOLOM_TANGGAL_LAHIR +" TEXT, "
            + PesertaDb.KOLOM_FOTO +" TEXT"
            +")";

    private static final String CREATE_TBL_TAGIHAN = "create table "
            + TagihanDb.NAMA_TABEL + "( "
            + TagihanDb._ID +" INT PRIMARY KEY, "
            + TagihanDb.KOLOM_ID +" TEXT, "
            + TagihanDb.KOLOM_ID_PESERTA +" TEXT, "
            + TagihanDb.KOLOM_TANGGAL_TAGIHAN +" TEXT, "
            + TagihanDb.KOLOM_TANGGAL_JATUH_TEMPO +" TEXT, "
            + TagihanDb.KOLOM_NILAI +" REAL "
            +")";

    private static final String DROP_TBL_TAGIHAN =
            "drop table if exists "+TagihanDb.NAMA_TABEL;

    private static final String DROP_TBL_PESERTA =
            "drop table if exists "+PesertaDb.NAMA_TABEL;

    public BpjsDbHelper(Context context) {
        super(context, NAMA_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_PESERTA);
        db.execSQL(CREATE_TBL_TAGIHAN);
        Log.d(TAG, CREATE_TBL_TAGIHAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versiLama, int versiBaru) {
        db.execSQL(DROP_TBL_TAGIHAN);
        db.execSQL(DROP_TBL_PESERTA);
        onCreate(db);
    }

    public static class PesertaDb implements BaseColumns {
        public static final String NAMA_TABEL = "peserta";
        public static final String KOLOM_ID = "id";
        public static final String KOLOM_NOMOR = "nomor";
        public static final String KOLOM_NAMA = "nama";
        public static final String KOLOM_EMAIL = "email";
        public static final String KOLOM_TANGGAL_LAHIR = "tanggal_lahir";
        public static final String KOLOM_FOTO = "foto";
    }

    public static class TagihanDb implements BaseColumns {
        public static final String NAMA_TABEL = "tagihan";
        public static final String KOLOM_ID = "id";
        public static final String KOLOM_ID_PESERTA = "id_peserta";
        public static final String KOLOM_TANGGAL_TAGIHAN = "tanggal_tagihan";
        public static final String KOLOM_TANGGAL_JATUH_TEMPO = "tanggal_jatuh_tempo";
        public static final String KOLOM_NILAI = "nilai";
    }

    public List<Tagihan> semuaTagihan(){
        List<Tagihan> hasil = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select tagihan.id as id_tagihan, tagihan.tanggal_tagihan, " +
                "tagihan.tanggal_jatuh_tempo, tagihan.nilai, " +
                "peserta.id as id_peserta, peserta.nomor, peserta.nama " +
                "from tagihan inner join peserta on tagihan.id_peserta = peserta.id " +
                "order by tagihan.tanggal_tagihan ";

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do {
                try {
                    Peserta p = new Peserta();
                    p.setId(c.getString(4));
                    p.setNomor(c.getString(5));
                    p.setNama(c.getString(6));

                    Tagihan t = new Tagihan();
                    t.setId(c.getString(0));
                    t.setPeserta(p);
                    t.setTanggalTagihan(formatter.parse(c.getString(1)));
                    t.setTanggalJatuhTempo(formatter.parse(c.getString(2)));
                    t.setNilai(new BigDecimal(c.getFloat(3)));
                    hasil.add(t);
                }catch (ParseException err){
                    Log.w(TAG, "Error parsing tanggal : "+err.getMessage());
                }
            } while (c.moveToNext());
        }

        db.close();
        return hasil;
    }

    public Peserta cariPesertaById(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from peserta where id = ?", new String[]{id});
        if(c.moveToFirst()){
            try {
                Peserta p = new Peserta();
                p.setId(c.getString(0));
                p.setNomor(c.getString(1));
                p.setNama(c.getString(2));
                p.setEmail(c.getString(3));
                p.setTanggalLahir(formatter.parse(c.getString(4)));
                p.setFoto(c.getString(5));
                return p;
            } catch (ParseException e) {
                Log.w(TAG, "Error parsing tanggal lahir : "+c.getString(4));
            }
        }
        db.close();
        return null;
    }
}
