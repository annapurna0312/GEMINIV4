package com.example.geminiv4.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.geminiv4.devicedata.IncoisMessage;
import com.example.geminiv4.dto.PFZFlc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FLCWisePFZHandler extends SQLiteOpenHelper {

    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "incois_flcwisepfz";

    String FLC_TABLE = "FLCtable";
    String PFZFLC_TABLE = "flcwise_pfz";

    String T_FLC_ID = "flcid";
    String T_FLC_STATE = "state";
    String T_FLC_DIST = "dist";
    String T_FLC_NAME = "flcname";


    String T_PFZ_FLCID = "flcid";
    String T_PFZ_LAT = "lat";
    String T_PFZ_LON = "lon";
    String T_PFZ_DIST = "distance";
    String T_PFZ_DIR = "direction";
    String T_PFZ_DEP = "depth";
    String T_PFZ_MAXWH = "maxwh";
    String T_PFZ_MAXWS = "maxws";
    String T_PFZ_MAXCS = "maxcs";
    String T_PFZ_ARRIVAL = "arrival_date";




    public FLCWisePFZHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //clearAllMessages();
        //createSampleData();
    }







    /** get the pfz data for the given flc id
     * @param flcid
     * @return
     */
    public PFZFlc getPFZData(int flcid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+PFZFLC_TABLE+" where "+T_PFZ_FLCID+"=?", new String[]{String.valueOf(flcid)});
        PFZFlc output = getMessage(imsgs);
        return output;
    }

    /** get the Messages given the cursor (query).
     * @param imsgs
     * @return
     */
    public PFZFlc getMessage(Cursor imsgs){
        PFZFlc pfzFlc = null;
        if(imsgs.getCount()>0){
            if (imsgs.moveToFirst()) {
                do {
                    pfzFlc = new PFZFlc(
                            imsgs.getInt(imsgs.getColumnIndex(T_FLC_ID)),
                            imsgs.getDouble(imsgs.getColumnIndex(T_PFZ_LAT)),imsgs.getDouble(imsgs.getColumnIndex(T_PFZ_LON)),
                            imsgs.getInt(imsgs.getColumnIndex(T_PFZ_DEP)),imsgs.getInt(imsgs.getColumnIndex(T_PFZ_DIR)),
                            imsgs.getDouble(imsgs.getColumnIndex(T_PFZ_MAXWH)),imsgs.getDouble(imsgs.getColumnIndex(T_PFZ_MAXWS)),
                            imsgs.getDouble(imsgs.getColumnIndex(T_PFZ_MAXCS)));
                } while (imsgs.moveToNext());
            }
        }
        imsgs.close();
        return pfzFlc;
    }





















    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+FLC_TABLE+" (" +
                T_FLC_ID+" INTEGER NOT NULL CONSTRAINT mid_pk PRIMARY KEY ," +
                T_FLC_STATE+" 		VARCHAR(500) NOT NULL," +
                T_FLC_DIST +" 	    VARHCHAR(20)," +
                T_FLC_NAME +" 		VARCHAR(20));");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+PFZFLC_TABLE+" (" +T_PFZ_FLCID+" INTEGER," +
                T_PFZ_LAT+" VARCHAR(40) NOT NULL," +
                T_PFZ_LON+" VARCHAR(40) NOT NULL," +
                T_PFZ_DIST+" INTEGER NOT NULL," +
                T_PFZ_DIR+" VARCHAR(40) NOT NULL," +
                T_PFZ_DEP+" VARCHAR(40) NOT NULL," +
                T_PFZ_MAXWH+" VARCHAR(40) NOT NULL," +
                T_PFZ_MAXWS+" VARCHAR(40) NOT NULL," +
                T_PFZ_MAXCS+" VARCHAR(40) NOT NULL," +
                T_PFZ_ARRIVAL+" VARCHAR(40) NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MessagesHandler","Upgrading the database");
        Toast.makeText(context, "Upgrading the database", Toast.LENGTH_SHORT).show();
        db.execSQL("DROP TABLE IF EXISTS " + FLC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PFZFLC_TABLE);
        onCreate(db);
    }

    public void removeExpiredMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calObj = Calendar.getInstance();
            String currentDate = dateFormat.format(calObj.getTime());
            db.delete(PFZFLC_TABLE, T_PFZ_ARRIVAL+">="+currentDate, new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
}
