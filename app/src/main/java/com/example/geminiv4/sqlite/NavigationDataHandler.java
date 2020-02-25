package com.example.geminiv4.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NavigationDataHandler  extends SQLiteOpenHelper {

    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "navigationaldata";

    String MARKLOCATION_TABLE = "locationmarker";
    String T_MARKLOCATION_LAT = "lat";
    String T_MARKLOCATION_LON = "lon";
    String T_MARKLOCATION_TAG = "tag";
    String T_MARKLOCATION_MARKEDDATE = "markeddate";

    public static String tag_sunkenship = "sunkenship";
    public static String tag_rockybottom = "rockybottom";
    public static String tag_dangerzone = "danzerzone";
    public static String tag_pfzlocaition = "pfzpoint";



    public NavigationDataHandler( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public void markLocation(String lat, String lon, String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        values.put(T_MARKLOCATION_LAT, String.valueOf(lat));
        values.put(T_MARKLOCATION_LON, String.valueOf(lon));
        values.put(T_MARKLOCATION_TAG, String.valueOf(tag));
        values.put(T_MARKLOCATION_MARKEDDATE, String.valueOf(datetime));
        db.insert(MARKLOCATION_TABLE, null, values);
        db.close();
        Log.i("Location_Marked",lat+","+lon+","+datetime+"==>"+tag);
    }

    public List<GeoPoint> getSunkenShipLocations() {
        return getMarkedLocations(tag_sunkenship);
    }

    public List<GeoPoint> getRockyBottomLocations() {
        return getMarkedLocations(tag_rockybottom);
    }

    public List<GeoPoint> getDangerZoneLocations() {
        return getMarkedLocations(tag_dangerzone);
    }

    public List<GeoPoint> getPFZLocations() {
        return getMarkedLocations(tag_pfzlocaition);
    }









    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+MARKLOCATION_TABLE+"("+
                T_MARKLOCATION_LAT+" VARCHAR(40), "+
                T_MARKLOCATION_LON+" VARCHAR(40), "+
                T_MARKLOCATION_TAG+" VARCHAR(40), "+
                T_MARKLOCATION_MARKEDDATE+" VARCHAR(40));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("NavigationDataHandler","Upgrading the database");
        db.execSQL("DROP TABLE IF EXISTS " + MARKLOCATION_TABLE);
        onCreate(db);
    }


    private List<GeoPoint> getMarkedLocations(String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MARKLOCATION_TABLE+" where tag=?", new String[]{String.valueOf(tag)});
        List<GeoPoint> messages = new LinkedList<>();
        if(imsgs.getCount()>0){
            if (imsgs.moveToFirst()) {
                do {
                    GeoPoint geoPoint = new GeoPoint(
                            Double.parseDouble(imsgs.getString(imsgs.getColumnIndex(T_MARKLOCATION_LAT))),
                            Double.parseDouble(imsgs.getString(imsgs.getColumnIndex(T_MARKLOCATION_LON)))
                    );
                    messages.add(geoPoint);
                } while (imsgs.moveToNext());
            }
        }
        imsgs.close();
        return messages;
    }
}

