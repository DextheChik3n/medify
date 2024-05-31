package com.sp.medify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RemoteController;

public class MedicineHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "medicinelist.db";
    private static final int SCHEMA_VERSION = 1;

    public MedicineHelper (Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicines_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, medicineID INTEGER, " +
                "medicineName TEXT, medicineDosage TEXT, medicineType TEXT, medicinePurpose TEXT, " +
                "medicineExpiry DATE, medicineRemarks TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public Cursor getAll() {
        return (getReadableDatabase().rawQuery("SELECT _id, medicineID, medicineName, medicineDosage, medicineType, " +
                "medicinePurpose, medicineExpiry, medicineRemarks FROM medicines_table ORDER BY medicineName", null));
    }

    public Cursor getById (String id) {
        String[] args = {id};

        return (getReadableDatabase().rawQuery("SELECT _id, medicineID, medicineName, medicineDosage, medicineType," +
                " medicinePurpose, medicineExpiry, medicineRemarks FROM medicines_table WHERE _ID=?", args));
    }

    public void insert (String medicineID, String medicineName, String medicineDosage, String medicineType,
                        String medicinePurpose,String medicineExpiry, String medicineRemarks) {
        ContentValues cv = new ContentValues();

        cv.put("medicineID",medicineID);
        cv.put("medicineName",medicineName);
        cv.put("medicineDosage",medicineDosage);
        cv.put("medicineType",medicineType);
        cv.put("medicinePurpose", medicinePurpose);
        cv.put("medicineExpiry", medicineExpiry);
        cv.put("medicineRemarks",medicineRemarks);

        getWritableDatabase().insert("medicines_table", "medicineID", cv);
    }

    public void update (String id, String medicineID, String medicineName, String medicineDosage, String medicineType, String medicinePurpose
            , String medicineExpiry, String medicineRemarks) {
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("medicineID",medicineID);
        cv.put("medicineName",medicineName);
        cv.put("medicineDosage",medicineDosage);
        cv.put("medicineType",medicineType);
        cv.put("medicinePurpose", medicinePurpose);
        cv.put("medicineExpiry", medicineExpiry);
        cv.put("medicineRemarks",medicineRemarks);

        getWritableDatabase().update("medicines_table", cv, "_ID=?", args);
    }

    public void delete (String id) {
        String[] args = {id};
        getWritableDatabase().delete("medicines_table","_ID=?", args);
    }


    public String getID (Cursor c) {return (c.getString(0));}

    public String getMedicineID (Cursor c) {return (c.getString(1));}

    public String getMedicineName (Cursor c) {return (c.getString(2));}

    public String getMedicineDosage (Cursor c) {return (c.getString(3));}

    public String getMedicineType (Cursor c) {return (c.getString(4));}

    public String getMedicinePurpose (Cursor c) {return (c.getString(5));}

    public String getMedicineExpiry (Cursor c) {return (c.getString(6));}

    public String getMedicineRemarks (Cursor c) {return (c.getString(7));}
}
