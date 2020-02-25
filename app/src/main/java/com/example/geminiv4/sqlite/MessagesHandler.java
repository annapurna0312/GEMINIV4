package com.example.geminiv4.sqlite;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.example.geminiv4.devicedata.Frame0;
import com.example.geminiv4.devicedata.FrameN;
import com.example.geminiv4.devicedata.IncoisMessage;
import com.example.geminiv4.multilingual.MultiLingualUtils;
import com.example.geminiv4.notification.Notifier;
import com.example.geminiv4.utils.AppConstants;
import com.example.geminiv4.utils.GPSDate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MessagesHandler  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "incois_messages_db";

    String MESSAGES_TABLE = "incois_messages";
    String FRAMES_TABLE = "frames_received";
    String MESSAGES_BLACKLIST = "messages_blacklisted";
    String GPS_TABLE = "gpstable";

    String T_FRAMES_MID = "mid";
    String T_FRAMES_FID = "fid";
    String T_FRAMES_MSG = "message";
    String T_FRAMES_ARRIVAL = "arrival_date";


    String T_MESSAGES_mid="mid";
    String T_MESSAGES_message="message";
    String T_MESSAGES_framescount="framescount";
    String T_MESSAGES_alert="alert";
    String T_MESSAGES_priority="priority";
    String T_MESSAGES_test="test";
    String T_MESSAGES_expiration="expiration";
    String T_MESSAGES_arrival_date="arrival_date";
    String T_MESSAGES_country="country";
    String T_MESSAGES_messageformat="messageformat";
    String T_MESSAGES_version="version";
    String T_MESSAGES_imagetext="imagetext";
    String T_MESSAGES_template="template";
    String T_MESSAGES_compression="compression";
    String T_MESSAGES_typeofservice="typeofservice";
    String T_MESSAGES_zone="zone";
    String T_MESSAGES_sparebits="spare";
    String T_MESSAGES_s127="s127";
    String T_MESSAGES_s128="s128";
    String T_MESSAGES_s132="s132";
    String T_MESSAGES_complete="complete";
    String T_MESSAGES_READ="read";


    String T_GPS_LAT = "lat";
    String T_GPS_LON = "lon";
    String T_GPS_TIME = "datetime";


    Context context;
    public ServiceWiseDecoder serviceWiseDecoder;
    //TextToSpeech tts;
    Notifier notifier;
    MultiLingualUtils multiLingualUtils;


    public MessagesHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        serviceWiseDecoder = new ServiceWiseDecoder();
        if(notifier==null){notifier=new Notifier(context);}
        multiLingualUtils = new MultiLingualUtils();
        /*tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });*/
        //clearAllMessages();
        //createSampleData();
    }



    private void createSampleData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MESSAGES_TABLE, "mid=22", new String[]{});
        db.delete(FRAMES_TABLE, "mid=22", new String[]{});
        ContentValues values = new ContentValues();
        values.put(T_MESSAGES_mid,   22);

        /*values.put(T_MESSAGES_message,  "11.98732320490,93.14526770070,100,1,100,200;12.00667635340,93.14999663520,100,2,101,201;12.02184698540,93.16275696110,100,3,102,202;12.03269751110,93.17953301980,100,4,103,203;12.59704596900,93.14526770070,200,5,104,204;12.61627744630,93.15072361680,200,6,105,205;12.63477218360,93.15830517290,200,7,106,206;12.65220946760,93.16807348220,200,8,107,207;12.04254454040,93.19693977650,200,9,108,208;12.05436349910,93.21302297610,200,10,109,209;12.07018503640,93.22518114230,200,11,110,210;12.10593073640,93.24287845120,200,12,111,211;12.16525057250,93.24966537930,200,13,112,212;12.18521013180,93.24843619040,200,14,113,213;12.20509655260,93.24631693330,200,15,114,214;12.22491450190,93.24363287150,200,16,115,215;12.24456083610,93.23990408240,200,17,116,216;11.22912539660,93.32142670520,500,18,117,217;12.66830483540,93.17992537630,500,19,118,218;12.68291383080,93.19356596870,500,20,119,219;12.69536224780,93.20918591180,500,21,120,220;12.70418522510,93.22709370800,500,22,121,221;12.71007553750,93.24619573970,500,23,122,222;12.71460505900,93.26567511940,500,24,123,223;12.71906565570,93.28517107240,500,25,124,224;12.72289126090,93.30479989920,500,26,125,225;12.08749578090,93.23517847910,500,27,126,226;12.12537195710,93.24749263950,500,28,127,227;12.14525649670,93.24953826540,500,29,128,228;11.11356561950,93.34836145380,500,30,129,229;11.13354756250,93.34812665620,500,31,130,230;11.15339963670,93.34574286930,500,32,131,231;11.17308202210,93.34220016050,500,33,132,232;11.19243771580,93.33720218590,500,34,133,233;11.21116900310,93.33021697610,500,35,134,234;11.05538195640,93.30094418290,1000,36,135,235;11.06475437080,93.31859497000,1000,37,136,236;11.07709251080,93.33427534460,1000,38,137,237;11.09396661730,93.34468654970,1000,39,138,238;");
        values.put(T_MESSAGES_typeofservice, "PFZ");*/
        values.put(T_MESSAGES_message,  "11.98732320490,93.14526770070,100,1,100,200;12.00667635340,93.14999663520,100,2,101,201;12.02184698540,93.16275696110,100,3,102,202;12.03269751110,93.17953301980,100,4,103,203;12.59704596900,93.14526770070,200,5,104,204;12.61627744630,93.15072361680,200,6,105,205;12.63477218360,93.15830517290,200,7,106,206;12.65220946760,93.16807348220,200,8,107,207;12.04254454040,93.19693977650,200,9,108,208;12.05436349910,93.21302297610,200,10,109,209;12.07018503640,93.22518114230,200,11,110,210;12.10593073640,93.24287845120,200,12,111,211;12.16525057250,93.24966537930,200,13,112,212;12.18521013180,93.24843619040,200,14,113,213;12.20509655260,93.24631693330,200,15,114,214;12.22491450190,93.24363287150,200,16,115,215;12.24456083610,93.23990408240,200,17,116,216;11.22912539660,93.32142670520,500,18,117,217;12.66830483540,93.17992537630,500,19,118,218;12.68291383080,93.19356596870,500,20,119,219;12.69536224780,93.20918591180,500,21,120,220;12.70418522510,93.22709370800,500,22,121,221;12.71007553750,93.24619573970,500,23,122,222;12.71460505900,93.26567511940,500,24,123,223;12.71906565570,93.28517107240,500,25,124,224;12.72289126090,93.30479989920,500,26,125,225;12.08749578090,93.23517847910,500,27,126,226;12.12537195710,93.24749263950,500,28,127,227;12.14525649670,93.24953826540,500,29,128,228;11.11356561950,93.34836145380,500,30,129,229;11.13354756250,93.34812665620,500,31,130,230;11.15339963670,93.34574286930,500,32,131,231;11.17308202210,93.34220016050,500,33,132,232;11.19243771580,93.33720218590,500,34,133,233;11.21116900310,93.33021697610,500,35,134,234;11.05538195640,93.30094418290,1000,36,135,235;11.06475437080,93.31859497000,1000,37,136,236;11.07709251080,93.33427534460,1000,38,137,237;11.09396661730,93.34468654970,1000,39,138,238;");
        values.put(T_MESSAGES_typeofservice, "PFZ");
        //values.put(T_MESSAGES_message,  "2.7,3;2.0,3;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.7,2;1.2,2;1.9,2;2.0,3;1.2,2;1.0,2;1.7,2;1.6,2;1.2,2;1.7,2;1.8,2;0.8,2;1.5,2;1.4,2;1.4,2;1.8,2;1.2,2;1.3,2;1.1,2;1.4,2;1.3,2;1.4,2;1.2,2;1.3,2;1.3,2;1.3,2;1.1,2;1.5,2;1.4,2;1.2,2;1.4,2;1.7,2;1.7,2;1.5,2;1.5,2;1.2,2;1.3,2;0.7,2;0.6,2;0.5,1;0.3,1;0.3,1;0.3,1;0.4,1;0.3,1;0.2,1;0.4,1;0.3,1;0.3,1;0.3,1;0.3,1;0.3,1;0.3,1;0.2,1;0.2,1;0.5,1;0.3,1;0.2,1;0.2,1;0.2,1;0.3,1;0.4,1;0.2,1;0.4,1;0.4,1;0.4,1;0.2,1;0.2,1;0.2,1|24.8,62.2;10;4");
        //values.put(T_MESSAGES_typeofservice, "TSUNAMI");
        values.put(T_MESSAGES_framescount, "10");
        values.put(T_MESSAGES_alert, "1");
        values.put(T_MESSAGES_priority, "7");
        values.put(T_MESSAGES_test, "1");
        values.put(T_MESSAGES_expiration, "2019-09-05 23:30:00");
        values.put(T_MESSAGES_arrival_date, "2019-09-04 16:09:00");
        values.put(T_MESSAGES_country, "india");
        values.put(T_MESSAGES_messageformat, "india");
        values.put(T_MESSAGES_version, "1");
        values.put(T_MESSAGES_imagetext, "1");
        values.put(T_MESSAGES_template, "1");
        values.put(T_MESSAGES_compression, "0");
        values.put(T_MESSAGES_s127, "1");
        values.put(T_MESSAGES_s128, "1");
        values.put(T_MESSAGES_s132, "1");
        values.put(T_MESSAGES_zone, "Andaman");
        values.put(T_MESSAGES_complete, "1");
        values.put(T_MESSAGES_sparebits, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        // Inserting Row
        db.insert(MESSAGES_TABLE, null, values);
        db.close();
    }


    /* Retrieving Messages */

    public List<IncoisMessage> getMessages() {
        List<IncoisMessage> messages = new LinkedList<>();
        messages.addAll(getMessages("OSF"));
        messages.addAll(getMessages("STRONGWINDALERT"));
        messages.addAll(getMessages("CYCLONE"));
        messages.addAll(getMessages("TSUNAMI"));
        messages.addAll(getMessages("PFZ"));
        return messages;
    }
    public IncoisMessage getMessage(int msgid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE+" where mid=?", new String[]{String.valueOf(msgid)});
        List<IncoisMessage> output = getMessage(imsgs);
        if(output.size()>0){ return output.get(0); }else{ return null; }

    }
    public List<FrameN> getFrames(int msgid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+FRAMES_TABLE+" where mid=? order by fid", new String[]{String.valueOf(msgid)});
        List<FrameN> messages = new LinkedList<>();
        if (imsgs.moveToFirst()) {
            do {
                FrameN fn =  new FrameN();
                fn.setMsgid(imsgs.getInt(imsgs.getColumnIndex(T_FRAMES_MID)));
                fn.setFrmid(imsgs.getInt(imsgs.getColumnIndex(T_FRAMES_FID)));
                fn.setMessage(imsgs.getString(imsgs.getColumnIndex(T_FRAMES_MSG)));
                fn.setArrival_date(imsgs.getString(imsgs.getColumnIndex(T_FRAMES_ARRIVAL)));
                messages.add(fn);
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return messages;
    }
    public List<IncoisMessage> getMessages(String typeofservice) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE+" where typeofservice=? and complete=?", new String[]{typeofservice,"1"});
        return getMessage(imsgs);
    }
    public List<IncoisMessage> getAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE, null);
        return getMessage(imsgs);
    }
    public List<IncoisMessage> getMessages(String typeofservice, String currentregion) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE+" where typeofservice=? and zone=? and complete=?", new String[]{typeofservice,currentregion,"1"});
        return getMessage(imsgs);
    }








    /* Storing  Messages */
    public void storeFrame(String pid,String frame) {
        try{
            String payload = frame.substring(0, 212);
            if(payload.contains("1")){
                int msgid = binaryToUnsignedInteger(payload.substring(0, 10));
                int frmid = binaryToUnsignedInteger(payload.substring(10, 20));
                int prnid = Integer.parseInt(pid);
                if(msgid !=0 && shouldStoreFrame(msgid,frmid,prnid,payload)) {
                    if(frmid == 0){
                        Frame0 frame0 = new Frame0(payload.substring(20, payload.length()), msgid);
                        Log.i("BT_Check","["+prnid+"]"+""+frame0);
                        storeFrame0(frame0,prnid);
                    }else{
                        storeFrameN(msgid,frmid,payload.substring(20, payload.length()));
                    }
                    updateMessageIfComplete(msgid,prnid);
                }
            }else{
                //Log.e("BT_storeframe","["+pid+"] No data... only zeros : "+payload);
            }
            removeExpiredMessages();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateMessageIfComplete(int msgid, int prnid) {
        List<FrameN> framesstored = getFrames(msgid);
        IncoisMessage frame0data = getMessage(msgid);
        //if frame0 is already arrived
        if(frame0data!=null){
            int framescount = frame0data.getFramescount();
            int framesreceived = framesstored.size();

            if(framescount == framesreceived){
                String message = null;
                if(frame0data.getTemplate()){
                    message = serviceWiseDecoder.decode(framesstored,frame0data);
                }else{
                    message = serviceWiseDecoder.decode(framesstored,null);
                }
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("s"+prnid,"1");
                if(!frame0data.getTypeofservice().equalsIgnoreCase("ERASER")){
                    values.put("message", message);
                }else{
                    storeBlackListedMessages(message.split("\\,"));
                }

                values.put("arrival_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                values.put("complete","1");
                // updating row
                db.update(MESSAGES_TABLE, values,  "mid=?",new String[] { String.valueOf(msgid) });
                Log.i("BT_Message",getMessage(msgid).toString());
                db.close();

                notifier.showNotification(
                        multiLingualUtils.getServiceHeading(frame0data.getTypeofservice(),context)+" - "+multiLingualUtils.getZoneFromStringsFile(frame0data.getZone(),context),
                        ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                           frame0data);
            }
        }else{
        }

    }


    int c = 0;
    public void storeSampledata(){
        if(c==7
        ){
            return;
        }else{
            createSampleData(c);
            c++;
        }
    }



    String[] tempmsgs = {
            "8.87,76.18,-58,6,1.7,0.1;8.89,76.17,-55,5,1.7,0.1;8.91,76.16,-54,5,1.7,0.1;8.92,76.15,-59,5,1.7,0.1;8.94,76.14,-59,5,1.7,0.1;8.96,76.13,-61,5,1.7,0.1;8.97,76.12,-63,5,1.7,0.1;8.99,76.10,-58,5,1.7,0.1;9.00,76.09,-65,5,1.7,0.1;9.01,76.07,-68,5,1.7,0.1;9.02,76.05,-72,5,1.7,0.1;9.03,76.04,-77,5,1.7,0.1;9.05,76.02,-81,5,1.7,0.1;9.06,76.01,-84,5,1.7,0.1;9.07,75.99,-86,5,1.7,0.1;9.08,75.97,-93,5,1.7,0.1;9.10,75.96,-99,5,1.7,0.2;9.12,75.95,-108,5,1.7,0.2;9.13,75.94,-112,5,1.7,0.2;9.15,75.94,-122,5,1.7,0.2;9.17,75.93,-120,5,1.7,0.2;9.19,75.93,-116,5,1.7,0.2;9.21,75.93,-104,5,1.7,0.2;9.23,75.93,-97,5,1.7,0.2;9.25,75.93,-88,5,1.7,0.2;10.30,75.65,-117,5,1.5,0.1;10.32,75.65,-101,5,1.5,0.1;10.34,75.65,-149,5,1.5,0.1;10.36,75.64,-127,5,1.5,0.1;10.37,75.64,-97,5,1.5,0.1;10.39,75.63,-67,5,1.5,0.1;10.41,75.63,-71,5,1.5,0.1;10.43,75.62,-64,5,1.5,0.1;10.45,75.61,-61,5,1.5,0.1;10.47,75.60,-64,5,1.5,0.1;10.48,75.59,-71,5,1.5,0.1;10.50,75.57,-78,5,1.5,0.1;10.51,75.56,-74,5,1.5,0.1;10.52,75.55,-89,5,1.5,0.1;10.53,75.53,-94,5,1.5,0.1;10.54,75.51,-102,5,1.5,0.1;10.54,75.49,-99,5,1.5,0.1;10.54,75.47,-104,5,1.5,0.1;10.55,75.45,-125,5,1.6,0.1;10.55,75.43,-149,5,1.6,0.1;10.55,75.41,-188,5,1.6,0.1;10.56,75.39,-223,5,1.6,0.0;10.57,75.37,-314,5,1.6,0.0;10.58,75.36,-364,5,1.6,0.0;10.59,75.34,-433,5,1.6,0.0;10.60,75.32,-470,5,1.6,0.1;10.61,75.31,-485,5,1.6,0.1;10.63,75.30,-529,5,1.6,0.1;10.65,75.28,-552,5,1.6,0.1;10.66,75.27,-602,5,1.6,0.1;10.68,75.26,-604,5,1.6,0.1;10.70,75.26,-615,5,1.6,0.1;10.72,75.25,-560,5,1.6,0.1;10.74,75.25,-504,5,1.6,0.1;10.76,75.25,-405,5,1.6,0.1;11.40,75.08,-51,4,1.5,0.1;11.42,75.07,-49,4,1.5,0.1;11.44,75.06,-47,4,1.5,0.1;11.46,75.05,-50,4,1.5,0.1;11.47,75.04,-62,4,1.5,0.1;11.49,75.03,-55,4,1.5,0.2;11.51,75.02,-52,4,1.5,0.2;11.52,75.00,-52,4,1.5,0.2;11.54,74.99,-58,4,1.5,0.2;11.55,74.98,-58,4,1.5,0.2;11.56,74.96,-54,4,1.5,0.2;11.57,74.94,-60,4,1.5,0.2;11.58,74.92,-69,4,1.5,0.2;11.58,74.90,-73,4,1.5,0.2;11.59,74.89,-73,4,1.5,0.2;11.60,74.87,-79,5,1.5,0.2;11.61,74.85,-93,5,1.6,0.2;11.62,74.83,-98,5,1.6,0.2;11.63,74.82,-95,5,1.6,0.2;11.65,74.81,-75,5,1.6,0.2;11.66,74.79,-66,5,1.5,0.2;11.68,74.78,-60,5,1.5,0.2;11.70,74.78,-57,5,1.5,0.2;11.72,74.77,-78,5,1.5,0.2;11.74,74.76,-60,5,1.5,0.2;11.76,74.76,-53,5,1.5,0.2;11.78,74.76,-41,5,1.5,0.2;11.80,74.75,-42,5,1.5,0.2;11.82,74.75,-47,5,1.5,0.2;11.84,74.75,-55,5,1.5,0.2;11.86,74.75,-59,5,1.5,0.2;11.88,74.75,-61,4,1.5,0.2;11.90,74.75,-61,4,1.5,0.2;11.92,74.75,-61,4,1.5,0.2",
            "13.03,74.52,41,4,1.3,0.1;13.04,74.51,41,4,1.3,0.1;13.06,74.50,42,4,1.3,0.1;13.08,74.50,41,4,1.3,0.1;13.10,74.50,41,4,1.3,0.1;13.12,74.50,41,4,1.3,0.1;13.14,74.49,40,4,1.3,0.1;13.15,74.48,41,4,1.3,0.1;13.17,74.47,40,4,1.2,0.1;13.18,74.46,41,4,1.2,0.1;13.20,74.44,41,4,1.3,0.1;13.21,74.43,42,4,1.3,0.1;13.23,74.42,42,4,1.3,0.1;13.25,74.41,42,4,1.3,0.1;13.27,74.41,40,4,1.3,0.1;13.29,74.40,40,4,1.3,0.1;13.44,73.66,103,5,1.6,0.1;13.46,73.67,103,5,1.6,0.1;13.46,74.62,13,4,0.9,0.0;13.47,73.68,98,5,1.6,0.1;13.48,74.61,16,4,0.9,0.0;13.48,73.70,90,5,1.6,0.1;13.49,73.72,79,5,1.6,0.1;13.50,73.74,68,5,1.6,0.1;13.50,74.61,15,4,0.9,0.0;13.50,73.76,56,5,1.5,0.1;13.51,74.62,14,4,0.9,0.0;13.51,73.77,44,5,1.5,0.1;13.52,74.64,12,3,0.9,0.0;13.53,73.79,40,5,1.5,0.1;13.53,74.65,12,3,0.9,0.0;13.55,73.79,47,5,1.5,0.1;13.57,73.79,63,5,1.5,0.1;13.59,73.80,68,5,1.5,0.1;13.60,73.81,71,5,1.5,0.1;13.62,73.83,69,5,1.5,0.1;13.63,73.84,65,5,1.5,0.1;13.86,73.30,498,5,1.6,0.0;13.88,73.30,443,5,1.6,0.0;13.90,73.30,356,5,1.6,0.0;13.91,73.32,276,5,1.6,0.0;13.92,73.33,273,5,1.6,0.1;13.94,73.35,271,5,1.6,0.1;13.95,73.37,270,5,1.6,0.1;13.96,73.38,267,5,1.6,0.1;13.98,73.38,254,5,1.6,0.1;14.00,73.38,239,5,1.6,0.1;14.02,73.38,227,5,1.6,0.1;14.24,74.30,28,4,1.1,0.0;14.25,74.28,33,4,1.1,0.0;14.27,74.27,31,4,1.0,0.0;14.28,74.27,30,4,1.0,0.0;14.30,74.25,30,4,1.1,0.0;14.31,74.23,32,4,1.1,0.0;14.37,74.35,11,4,1.0,0.0;14.39,74.36,10,5,0.0,0.0;14.41,74.36,10,5,0.0,0.0;14.43,74.35,7,5,1.0,0.0;14.45,74.35,5,5,1.0,0.0;14.47,74.34,8,5,0.9,0.0;14.48,74.32,6,5,0.9,0.0;14.49,74.31,11,5,0.9,0.0;14.52,72.84,880,5,1.6,0.1;14.54,72.83,989,5,1.6,0.1;14.55,72.82,1093,5,1.6,0.1;14.57,74.27,10,5,0.0,0.0;14.57,72.81,1206,5,1.6,0.1;14.58,72.79,1287,5,1.6,0.1;14.58,72.77,1346,5,1.6,0.1;14.58,74.26,8,5,0.0,0.0;14.59,72.75,1402,5,1.7,0.1;14.60,72.73,1451,5,1.7,0.1;14.60,74.25,11,5,0.9,0.0;14.61,72.72,1421,5,1.7,0.1;14.62,74.25,31,5,0.9,0.0;14.63,72.71,1361,5,1.7,0.1;14.64,74.23,9,3,0.9,0.0;14.65,72.70,1237,5,1.7,0.1;14.65,74.22,5,3,0.9,0.0;14.66,72.69,1072,5,1.6,0.1;14.67,74.22,8,3,0.8,0.0;14.68,72.69,790,5,1.6,0.1;14.70,72.69,616,5,1.6,0.1;14.72,72.69,478,5,1.6,0.1;14.74,72.69,300,5,1.6,0.1;14.76,72.68,214,5,1.6,0.0;14.78,72.67,184,5,1.6,0.0;14.78,72.65,139,5,1.7,0.0",
            "2019-09-27,2019-09-29,17:30,23:30,3.3,3.6,2.0,3.0,Gujarat,Diu Island,Jakhau,",
            "2019-09-27,2019-09-29,17:30,23:30,3.0,3.3,2.2,3.2,Maharashtra,Sindhudurg (Malvan),Vasai,",
            "Gujarat,50-60,se,48",
            "Maharashtra,50-60,se,48",
            "1|---|20.2,70.5|10,s,Diu Island,Gujarat|||Fishermen are requested to come back to land immediately|60|3.6|60"
    };

    String[] temptos  = {
            "PFZ",
            "PFZ",
            "OSF",
            "OSF",
            "STRONGWINDALERT",
            "STRONGWINDALERT",
            "CYCLONE"
    };

    String[] tempzone = {
            "Kerala",
            "Karnataka",
            "Gujarat",
            "Maharashtra",
            "Gujarat",
            "Maharashtra",
            "Gujarat",
    };

    private void createSampleData(int c){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MESSAGES_TABLE, "mid="+(c+1), new String[]{});
        db.delete(FRAMES_TABLE, "mid="+(c+1), new String[]{});
        ContentValues values = new ContentValues();
        values.put(T_MESSAGES_mid,  (c+1));
        values.put(T_MESSAGES_message,  tempmsgs[c]);
        values.put(T_MESSAGES_typeofservice, temptos[c]);
        values.put(T_MESSAGES_framescount, "10");
        values.put(T_MESSAGES_alert, "1");
        values.put(T_MESSAGES_priority, "7");
        values.put(T_MESSAGES_test, "1");
        values.put(T_MESSAGES_expiration, "2019-09-29 23:30:00");
        values.put(T_MESSAGES_arrival_date, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(T_MESSAGES_country, "india");
        values.put(T_MESSAGES_messageformat, "india");
        values.put(T_MESSAGES_version, "1");
        values.put(T_MESSAGES_imagetext, "1");
        values.put(T_MESSAGES_template, "1");
        values.put(T_MESSAGES_compression, "0");
        values.put(T_MESSAGES_s127, "1");
        values.put(T_MESSAGES_s128, "1");
        values.put(T_MESSAGES_s132, "1");
        values.put(T_MESSAGES_zone, tempzone[c]);
        values.put(T_MESSAGES_complete, "1");
        values.put(T_MESSAGES_sparebits, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        // Inserting Row
        db.insert(MESSAGES_TABLE, null, values);
        db.close();
        notifier.showTempNotification(
                multiLingualUtils.getServiceHeading(temptos[c],context)+" - "+multiLingualUtils.getZoneFromStringsFile(tempzone[c],context),
                ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), temptos[c]);

    }








    public void storeGPSInfo(String lat, String lon, String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_GPS_LAT,  String.valueOf(lat));
        values.put(T_GPS_LON,  String.valueOf(lon));
        values.put(T_GPS_TIME, String.valueOf(datetime));
        // Inserting Row
        db.insert(GPS_TABLE, null, values);
        Log.i("GPS_TABLE",lat+" "+lon+" "+datetime);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getGPSInfo(){
        ArrayList<HashMap<String, String>> ret = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+GPS_TABLE+" ", null);
        if(imsgs.getCount()>0){
            if (imsgs.moveToFirst()) {
                do {
                    HashMap<String, String> gpsdata = new HashMap<>();
                    gpsdata.put("lat",imsgs.getString(imsgs.getColumnIndex(T_GPS_LAT)));
                    gpsdata.put("lon",imsgs.getString(imsgs.getColumnIndex(T_GPS_LON)));
                    gpsdata.put("datetime",imsgs.getString(imsgs.getColumnIndex(T_GPS_TIME)));
                    ret.add(gpsdata);
                } while (imsgs.moveToNext());
            }
        }
        imsgs.close();
        return ret;
    }

    public void clearGPSInfo(){
        notifier.showNotification("GPS Beforeclear",""+getGPSInfo().size(),""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GPS_TABLE, "1=1", new String[]{});
        db.close();
        notifier.showNotification("GPS After clear",""+getGPSInfo().size(),""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }



    private void storeFrame0(Frame0 f0, int prn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_MESSAGES_mid,      String.valueOf(f0.getMid()));
        values.put(T_MESSAGES_message,  "");
        values.put(T_MESSAGES_framescount, String.valueOf(f0.getFramescount()));
        values.put(T_MESSAGES_alert, String.valueOf(f0.isIsalert()));
        values.put(T_MESSAGES_priority, String.valueOf(f0.getPriority()));
        values.put(T_MESSAGES_test, (f0.isTest() ? "1" : "0"));
        values.put(T_MESSAGES_expiration, f0.getExpiration());
        values.put(T_MESSAGES_arrival_date, "");
        values.put(T_MESSAGES_country, f0.getCountry());
        values.put(T_MESSAGES_messageformat, f0.getMessageformat());
        values.put(T_MESSAGES_version, String.valueOf(f0.getVersion()));
        values.put(T_MESSAGES_imagetext, (f0.isText() ? "1" : "0"));
        values.put(T_MESSAGES_template, (f0.isTemplate() ? "1" : "0"));
        values.put(T_MESSAGES_compression, (f0.isCompressed() ? "1" : "0"));
        values.put(T_MESSAGES_typeofservice, f0.getTypeofservice());
        values.put(T_MESSAGES_s127, ((prn==127) ? "1" : "0"));
        values.put(T_MESSAGES_s128, ((prn==128) ? "1" : "0"));
        values.put(T_MESSAGES_s132, ((prn==132) ? "1" : "0"));
        values.put(T_MESSAGES_zone, f0.getZone());
        values.put(T_MESSAGES_complete, "0");
        values.put(T_MESSAGES_sparebits, f0.getSparebits());
        // Inserting Row
        db.insert(MESSAGES_TABLE, null, values);
        Log.i("BT_Frame0",f0.toString());
        db.close();
    }


    private void storeFrameN(int msgid, int frmid, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_FRAMES_MID, String.valueOf(msgid));
        values.put(T_FRAMES_FID, String.valueOf(frmid));
        values.put(T_FRAMES_MSG, message);
        values.put(T_FRAMES_ARRIVAL, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        db.insert(FRAMES_TABLE, null, values);
        db.close();
        Log.i("BT_FrameN",msgid+","+frmid+"==>"+message);
    }

    private void storeBlackListedMessages(String[] msgids){
        SQLiteDatabase db = this.getWritableDatabase();
        for(String mid : msgids){
            ContentValues values = new ContentValues();
            values.put(T_FRAMES_MID, mid);
            db.insert(MESSAGES_BLACKLIST, null, values);
            removeMessage(mid);
            Log.i("BT_BlacklistedFrame",mid+"");
        }
        db.close();
    }



    public boolean shouldStoreFrame(int msgid,int frmid,int prnid, String payload){
        boolean ret = true;
        if(!isBlackListed(msgid)){
            if(frmid == 0){
                Frame0 frame0 = new Frame0(payload.substring(20, payload.length()), msgid);
                //if the message already exists
                if(messageExists(msgid,prnid)){ret = false;
                    Log.e("Bt_check","["+msgid+"][0]message already exists");}
                //if the expiry date is over
                try {if(GPSDate.df.parse(frame0.getExpiration()).before(new Date())){ret = false;
                    Log.e("Bt_check","["+frame0.getExpiration()+"]message expired");}} catch (ParseException e) {e.printStackTrace();}
                //if the originator is not incois
                if(frame0.getOriginator().length()==0){ret = false;
                    Log.e("Bt_check","["+msgid+"]message not from incois");}
                //if the message is test message
                if(frame0.isTest()){ret = false;
                    Log.e("Bt_check","["+msgid+"]message is test message");}
            }else{
                //if the frame is already there is the database
                if(frameExists(msgid,frmid,prnid)){ret = false;
                    Log.e("Bt_check","["+msgid+"]["+frmid+"]frame already exists");}
                //if the message is complete and a frame came in
                if(isMessageComplete(msgid,prnid)){ret = false;
                    Log.e("Bt_check","message already complete --> "+getMessage(msgid));}
            }
        }else{
            ret = false;
            Log.e("Bt_check","message is blacklisted");
        }
        return ret;
    }

    /**if the message exists with the given id, then return true
     * @param mid
     * @return
     */
    public boolean isBlackListed(int mid) {
        boolean exist = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_BLACKLIST+" where mid=?", new String[]{mid+""});
        List<IncoisMessage> messages = getMessage(imsgs);
        if(messages.size()>0){
            exist = true;
        }
        return exist;
    }


    private boolean isMessageComplete(int mid,int prnid) {
        boolean ret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE+" where mid=?", new String[]{String.valueOf(mid)});
        List<IncoisMessage> messages = getMessage(imsgs);
        if(messages.size()>0){
            if(messages.get(0).isComplete()){
                ret = true; updatePRNStatus(mid,prnid);
            }
        }
        db.close();
        return ret;
    }


    /**if the message exists with the given id, then return true
     * @param mid
     * @param prnid
     * @return
     */
    public boolean messageExists(int mid, int prnid) {
        boolean exist = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+MESSAGES_TABLE+" where mid=?", new String[]{mid+""});
        IncoisMessage msg = null;
        List<IncoisMessage> messages = getMessage(imsgs);
        if(messages.size()>0){
            exist = true; updatePRNStatus(mid,prnid);
        }
        return exist;
    }


    /**if the message exists with the given id, then return true
     * @param mid
     * @param prnid
     * @return
     */
    public boolean frameExists(int mid, int frmid, int prnid) {
        boolean exist = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor imsgs = db.rawQuery("SELECT * FROM "+FRAMES_TABLE+" where mid=? and fid=?", new String[]{mid+"",frmid+""});
        IncoisMessage msg = null;
        List<FrameN> messages = getFrame(imsgs);
        if(messages.size()>0){
            exist = true; updatePRNStatus(mid,prnid);
        }
        return exist;
    }


    private void updatePRNStatus(int msgid,int prnid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("s"+prnid, 1);
        db.update(MESSAGES_TABLE, values, "mid=?",new String[] { String.valueOf(msgid) });
        db.close();
    }













    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+MESSAGES_TABLE+" (" +
                            T_MESSAGES_mid+" INTEGER NOT NULL CONSTRAINT mid_pk PRIMARY KEY ," +
                            T_MESSAGES_message+" 		VARCHAR(500) NOT NULL," +
                            T_MESSAGES_framescount+"    INTEGER NOT NULL," +
                            T_MESSAGES_alert+" 		INTEGER NOT NULL," +
                            T_MESSAGES_priority+" 		INTEGER NOT NULL," +
                            T_MESSAGES_test+" 			INTEGER NOT NULL," +
                            T_MESSAGES_expiration+" 	VARCHAR(40) NOT NULL," +
                            T_MESSAGES_arrival_date+" 	VARCHAR(40) NOT NULL," +
                            T_MESSAGES_country+" 		VARCHAR(30) NOT NULL," +
                            T_MESSAGES_messageformat+" 	VARCHAR(50) NOT NULL," +
                            T_MESSAGES_version+" 		VARCHAR(20) NOT NULL," +
                            T_MESSAGES_imagetext+" 		INTEGER," +
                            T_MESSAGES_template+" 		INTEGER," +
                            T_MESSAGES_compression+" 		INTEGER," +
                            T_MESSAGES_typeofservice+" 	VARHCHAR(20)," +
                            T_MESSAGES_sparebits+" 	TEXT," +
                            T_MESSAGES_s127+" 	INTEGER," +
                            T_MESSAGES_s128+" 	INTEGER," +
                            T_MESSAGES_s132+" 	INTEGER," +
                            T_MESSAGES_zone+" 			VARCHAR(20)," +
                            T_MESSAGES_complete+" 	INTEGER," +
                            T_MESSAGES_READ + " INTEGER NOT NULL default 0" +
                            ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS "+FRAMES_TABLE+" (" +T_FRAMES_MID+" INTEGER," +T_FRAMES_FID+" INTEGER," +
                            T_FRAMES_MSG+"    VARCHAR(500) NOT NULL," +T_FRAMES_ARRIVAL+" 	VARCHAR(40) NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS "+MESSAGES_BLACKLIST+"("+T_MESSAGES_mid+" INTEGER NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS "+GPS_TABLE+"("+
                        T_GPS_LAT+" VARCHAR(40), "+
                        T_GPS_LON+" VARCHAR(40), "+
                        T_GPS_TIME+" VARCHAR(40));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MessagesHandler","Upgrading the database");
        Toast.makeText(context, "Upgrading the database", Toast.LENGTH_SHORT).show();
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRAMES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_BLACKLIST);
        db.execSQL("DROP TABLE IF EXISTS " + GPS_TABLE);
        onCreate(db);
    }


    public int binaryToUnsignedInteger(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i = numbers.length - 1; i >= 0; --i) {
            if (numbers[i] == '1') {
                result = (int)((double)result + Math.pow(2.0D, (double)(numbers.length - i - 1)));
            }
        }
        return result;
    }




    /** get the Messages given the cursor (query).
     * @param imsgs
     * @return
     */
    public List<IncoisMessage> getMessage(Cursor imsgs){
        List<IncoisMessage> messages = new LinkedList<>();
        if(imsgs.getCount()>0){
            if (imsgs.moveToFirst()) {
                do {
                    IncoisMessage msg = new IncoisMessage();
                    msg.setMid(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_mid)));
                    msg.setMessage(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_message)));
                    msg.setFramescount(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_framescount)));
                    msg.setAlert(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_alert))==1);
                    msg.setPriority(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_priority)));
                    msg.setTest(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_test)));
                    msg.setExpiration(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_expiration)));
                    msg.setArrival_date(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_arrival_date)));
                    msg.setCountry(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_country)));
                    msg.setMessageformat(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_messageformat)));
                    msg.setVersion(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_version)));
                    msg.setImagetext(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_imagetext)));
                    msg.setTemplate(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_template))==1);
                    msg.setCompression(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_compression))==1);
                    msg.setTypeofservice(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_typeofservice)));
                    msg.setPrn127(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_s127))==1);
                    msg.setPrn128(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_s128))==1);
                    msg.setPrn132(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_s132))==1);
                    msg.setZone(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_zone)));
                    msg.setComplete(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_complete))==1);
                    msg.setSparebits(imsgs.getString(imsgs.getColumnIndex(T_MESSAGES_sparebits)));
                    msg.setRead(imsgs.getInt(imsgs.getColumnIndex(T_MESSAGES_READ))==1);
                    messages.add(msg);
                } while (imsgs.moveToNext());
            }
        }
        imsgs.close();
        return messages;
    }

    /** get the Messages given the cursor (query).
     * @param imsgs
     * @return
     */
    public List<FrameN> getFrame(Cursor imsgs){
        List<FrameN> messages = new LinkedList<>();
        if(imsgs.getCount()>0){
            if (imsgs.moveToFirst()) {
                do {
                    FrameN msg = new FrameN();
                    msg.setMsgid(imsgs.getInt(imsgs.getColumnIndex(T_FRAMES_MID)));
                    msg.setFrmid(imsgs.getInt(imsgs.getColumnIndex(T_FRAMES_FID)));
                    msg.setMessage(imsgs.getString(imsgs.getColumnIndex(T_FRAMES_MSG)));
                    msg.setArrival_date(imsgs.getString(imsgs.getColumnIndex(T_FRAMES_ARRIVAL)));
                    messages.add(msg);
                } while (imsgs.moveToNext());
            }
        }
        imsgs.close();
        return messages;
    }





    public void clearAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MESSAGES_TABLE, "1=1", new String[]{});
        db.delete(FRAMES_TABLE, "1=1", new String[]{});
        db.close();
    }
    public void removeExpiredMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<IncoisMessage> messages = getAllMessages();
        for (IncoisMessage im : messages) {
            try {
                Date exp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(im.getExpiration());
                if (exp.before(new Date())) {
                    db.delete(MESSAGES_TABLE, "mid=" + im.getMid(), new String[]{});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
    }
    public void removeMessage(String mid) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
             db.delete(MESSAGES_TABLE, "mid=" + mid, new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }







    public void markItemRead(Context context, int msgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_MESSAGES_READ, "1");
        int updated = db.update(MESSAGES_TABLE, values,  "mid=?",new String[] { String.valueOf(msgid) });
        db.close();
    }


    public int getNavicStateID(String state){
        int binarypattern = 0;
        if(state.equalsIgnoreCase("Gujarat")){binarypattern = 1;}else
        if(state.equalsIgnoreCase("Maharashtra")){binarypattern = 2;}else
        if(state.equalsIgnoreCase("Goa")){binarypattern = 3;}else
        if(state.equalsIgnoreCase("Karnataka")){binarypattern = 4;}else
        if(state.equalsIgnoreCase("Kerala")){binarypattern = 5;}else
        if(state.equalsIgnoreCase("SouthTamilNadu")){binarypattern = 6;}else
        if(state.equalsIgnoreCase("NorthTamilNadu")){binarypattern = 7;}else
        if(state.equalsIgnoreCase("SouthAndhraPradesh")){binarypattern = 8;}else
        if(state.equalsIgnoreCase("NorthAndhraPradesh")){binarypattern = 9;}else
        if(state.equalsIgnoreCase("Odisha")){binarypattern = 10;}else
        if(state.equalsIgnoreCase("WestBengal")){binarypattern = 11;}else
        if(state.equalsIgnoreCase("Andaman")){binarypattern = 12;}else
        if(state.equalsIgnoreCase("Nicobar")){binarypattern = 13;}else
        if(state.equalsIgnoreCase("Lakshadweep")){binarypattern = 14;}
        return binarypattern;
    }

}



class ServiceWiseDecoder {


    public String decode(List<FrameN> framesstored, IncoisMessage frame0data){
        String message = null;
        if(frame0data!=null){
            switch (frame0data.getTypeofservice()){
                case "OSF" :
                    message =  decodeOSF(framesstored,frame0data);
                    break;
                case "PFZ" :
                    message =  decodePFZ(framesstored,frame0data);
                    break;
                case "TSUNAMI" :
                    message =  decodeTsunami(framesstored,frame0data);
                    break;
                case "CYCLONE" :
                    message =  decodeCyclone(framesstored,frame0data);
                    break;
                case "FishMarketData" :
                    message =  decodeFishMarketData(framesstored);
                    break;
                case "STRONGWINDALERT" :
                    message =  decodeStrongWind(framesstored,frame0data);
                    break;
                case "ERASER" :
                    message =  decodeERASERData(framesstored,frame0data);
                    break;
            }
        }else{
            message =  decode(framesstored);
        }
        return message;
    }


    private String decode(List<FrameN> framesstored){
        StringBuffer sb = new StringBuffer();
        for(FrameN framen : framesstored){
            char[] chars = framen.getMessage().toCharArray();
            for(int j = 0; j < chars.length; j += 8) {
                int idx = 0;
                int sum = 0;
                for(int i = 7; i >= 0; --i) {
                    if (chars[i + j] == '1') {
                        sum += 1 << idx;
                    }
                    ++idx;
                }
                if(sum>0)
                    sb.append(Character.toChars(sum));
            }
        }
        return sb.toString();
    }


    /** decode osf message if it is templated message
     * @param framesstored
     * @return
     */
    private String decodeFishMarketData(List<FrameN> framesstored){
        StringBuffer sb = new StringBuffer();
        for(FrameN framen : framesstored){
            char[] chars = framen.getMessage().toCharArray();
            for(int j = 0; j < chars.length; j += 8) {
                int idx = 0;
                int sum = 0;
                for(int i = 7; i >= 0; --i) {
                    if (chars[i + j] == '1') {
                        sum += 1 << idx;
                    }
                    ++idx;
                }
                if(sum>0)
                    sb.append(Character.toChars(sum));
            }
        }
        return sb.toString();
    }

    /** decode osf message if it is templated message
     * @param framesstored
     * @param frame0data
     * @return
     */
    private String decodeERASERData(List<FrameN> framesstored, IncoisMessage frame0data){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 110 ; i=i+10) {
            int mid = binaryToUnsignedInteger(frame0data.getSparebits().substring(i,i+10));
            if(mid!=0){sb.append(mid);if(i<100){sb.append(",");}}
        }
        return sb.toString();
    }



    /** decode osf message if it is templated message
     * @param framesstored
     * @param frame0data
     * @return
     */
    private String decodeCyclone(List<FrameN> framesstored, IncoisMessage frame0data){
        String message = null;

        NavicFishLandingCenters nv = new NavicFishLandingCenters();
        int category = binaryToUnsignedInteger(frame0data.getSparebits().substring(0,3));
        String name = getNavicSystemName(frame0data.getSparebits().substring(3,9));
        String lat = ((double)binaryToUnsignedInteger(frame0data.getSparebits().substring(9,18))/10)+"";
        String lon =  ((double)binaryToUnsignedInteger(frame0data.getSparebits().substring(18,28))/10)+"";

        /*int date =  binaryToUnsignedInteger(frame0data.getSparebits().substring(28,44));
        int time =  binaryToUnsignedInteger(frame0data.getSparebits().substring(44,55));*/
        String dd = String.format("%02d", binaryToUnsignedInteger(frame0data.getSparebits().substring(28,33)));
        String mm = String.format("%02d", binaryToUnsignedInteger(frame0data.getSparebits().substring(33,37)));
        String yyyy = "20"+String.format("%02d", binaryToUnsignedInteger(frame0data.getSparebits().substring(37,44)));
        String hh = String.format("%02d", binaryToUnsignedInteger(frame0data.getSparebits().substring(44,49)));
        String MM = String.format("%02d", binaryToUnsignedInteger(frame0data.getSparebits().substring(49,55)));
        //"yyyy-MM-dd HH:mm:ss"
        String datetime = yyyy+"-"+mm+"-"+dd+" "+hh+":"+MM+":00";



        String direc =  getNavicDirection(frame0data.getSparebits().substring(55,58));
        int kms =  binaryToUnsignedInteger(frame0data.getSparebits().substring(58,69));
        String loc =  nv.alllandingcenters.get(binaryToUnsignedInteger(frame0data.getSparebits().substring(69,80)));

        int maxws =  binaryToUnsignedInteger(frame0data.getSparebits().substring(80,87));
        double maxwh =  (double)( (double)binaryToUnsignedInteger(frame0data.getSparebits().substring(87,95))/10);
        double maxcs =  (double)((double)binaryToUnsignedInteger(frame0data.getSparebits().substring(95,101))/10);
        String advice =  getNavicAdvice(frame0data.getSparebits().substring(101,103));


        try{
            //"1|Vayu|15.5,84.2|510,ssw,Puri,Odisha||15.5,84.2;15.8,84.3;16.6,84.6;17.4,84.9;18.2,85.1;19.2,85.5;20.9,86.5;22.8,88.0;24.8,89.4;26.3,90.8" +
            //"|Fishermen are requested to come back to land immediately|120|8|120
            message = category+"|"+name+"|"+lat+","+lon+"|"+kms+","+direc+","+loc+"|||"+advice+"|"+maxws+"|"+maxwh+"|"+maxcs+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }


    /** decode osf message if it is templated message
     * @param framesstored
     * @param frame0data
     * @return
     */
    private String decodeOSF(List<FrameN> framesstored, IncoisMessage frame0data){
        return getNaviTemplateData(frame0data);
    }



    /** decode osf message if it is templated message
     * @param framesstored
     * @param frame0data
     * @return
     */
    private String decodeStrongWind(List<FrameN> framesstored, IncoisMessage frame0data){
        StringBuffer sb = new StringBuffer();
        sb.append(frame0data.getSparebits());
        for(FrameN frameN : framesstored){ sb.append(frameN.getMessage()); }
        String data = sb.toString();
        sb = new StringBuffer();
        int hours = (binaryToUnsignedInteger(data.substring(17,20))+1)*24;
        sb.append(
                frame0data.getZone()
                        +","+binaryToUnsignedInteger(data.substring(0,7))
                        +" to "+binaryToUnsignedInteger(data.substring(7,14))
                        +","+getNavicDirection(data.substring(14,17))
                        +","+hours
        );
        return sb.toString();
    }





    /** decode osf message if it is templated message
     * @param framesstored
     * @param incoisMessage
     * @return
     */
    //2.7,3;2.0,3;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.8,2;1.7,2;2.7,3;2.0,3;1.8,2;1.7,2;1.2,2;1.9,2;2.0,3;1.2,2;1.0,2;1.7,2;1.6,2;1.2,2;1.7,2;1.8,2;0.8,2;1.5,2;1.4,2;1.4,2;1.8,2;1.2,2;1.3,2;1.1,2;1.4,2;1.3,2;1.4,2;1.2,2;1.3,2;1.3,2;1.3,2;1.1,2;1.5,2;1.4,2;1.2,2;1.4,2;1.7,2;1.7,2;1.5,2;1.5,2;1.2,2;1.3,2;0.7,2;0.6,2;0.5,1;0.3,1;0.3,1;0.3,1;0.4,1;0.3,1;0.2,1;0.4,1;0.3,1;0.3,1;0.3,1;0.3,1;0.3,1;0.3,1;0.2,1;0.2,1;0.5,1;0.3,1;0.2,1;0.2,1;0.2,1;0.3,1;0.4,1;0.2,1;0.4,1;0.4,1;0.4,1;0.2,1;0.2,1;0.2,1|24.8,62.2;10;4
    private String decodeTsunami(List<FrameN> framesstored, IncoisMessage incoisMessage){
        StringBuffer sb = new StringBuffer();
        sb.append(incoisMessage.getSparebits());
        for(FrameN frameN : framesstored){ sb.append(frameN.getMessage()); }
        String data = sb.toString();
        sb = new StringBuffer();
        String threats = data.substring(0, 850);
        //appending all threat categories
        List<String> pairs = splitEqually(threats, 10);
        for(int i=0; i<pairs.size() ; i++){
            String tw = pairs.get(i);
            sb.append(((double) (binaryToUnsignedInteger(tw.substring(0, 8)))/10)+",");
            sb.append(binaryToUnsignedInteger(tw.substring(8, tw.length())));
            if(i < (pairs.size()-1)) {
                sb.append(";");
            }
        }

        sb.append("|");
        String eq = data.substring(850, 890);
        double lat = ((double) (binaryToUnsignedInteger(eq.substring(0, 11)))/10)-90;
        lat = Math.round(lat * 10) / 10.0;
        double lon = ((double) (binaryToUnsignedInteger(eq.substring(11, 24)))/10)-180;
        lon = Math.round(lon * 10) / 10.0;

        sb.append(lat+",");
        sb.append(lon+";");
        sb.append(binaryToUnsignedInteger(eq.substring(24, 36))+";");
        sb.append(binaryToUnsignedInteger(eq.substring(36, 39)));
        return sb.toString();
    }

    public List<String> splitEqually(String text, int size) {
        List<String> ret = new ArrayList<>((text.length() + size - 1) / size);
        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }


    /** decode osf message if it is templated message
     * @param framesstored
     * @param frame0data
     * @return
     */
    private String decodePFZ(List<FrameN> framesstored, IncoisMessage frame0data){
        StringBuffer sb = new StringBuffer();
        sb.append(frame0data.getSparebits());
        for(int i = 0; i<framesstored.size(); i++){
            sb.append(framesstored.get(i).getMessage());
        }
        String data = sb.toString();
        sb = new StringBuffer();
        List<String> pairs = splitEqually(data,57);
        for(int k=0; k<pairs.size() ; k++){
            String pair = pairs.get(k);
            if(pair.length()==57 && pair.contains("1")) {
                /*12*/String lat = String.format("%.2f", (((double)binaryToUnsignedInteger(pair.substring(0,12))/100)+4));
                /*12*/String lon = String.format("%.2f", (((double)binaryToUnsignedInteger(pair.substring(12,24))/100)+61));
                /*12*/String dep = (binaryToUnsignedInteger(pair.substring(24,36)))+"";
                /*7*/ String maxws = String.format("%.2f", ((double)binaryToUnsignedInteger(pair.substring(36,43)))*3.6);
                /*8*/ String maxwh = String.format("%.2f", ((double)binaryToUnsignedInteger(pair.substring(43,51))/10));
                /*6*/ String maxcs = String.format("%.2f", ((double)binaryToUnsignedInteger(pair.substring(51,57))/10));
                sb.append(lat+","+lon+","+dep+","+maxws+","+maxwh+","+maxcs+";");
            }
        }
        return sb.toString();
    }





    public String getNaviTemplateData(IncoisMessage frame0){
        String sparebits = frame0.getSparebits();
        String message = null;
        if(frame0.getTypeofservice().equalsIgnoreCase("OSF")){
            NavicFishLandingCenters nv = new NavicFishLandingCenters();
            String region = getNavicStateBinaryPattern(sparebits.substring(0,4));
            int loc1 = binaryToUnsignedInteger(sparebits.substring(4,12));
            int loc2 = binaryToUnsignedInteger(sparebits.substring(12,20));
            int wh1 =  binaryToUnsignedInteger(sparebits.substring(20,28));
            int wh2 =  binaryToUnsignedInteger(sparebits.substring(28,36));
            int cs1 =  binaryToUnsignedInteger(sparebits.substring(36,42));
            int cs2 =  binaryToUnsignedInteger(sparebits.substring(42,48));
            String dd = String.format("%02d", binaryToUnsignedInteger(sparebits.substring(48,53)));
            String mm = String.format("%02d", binaryToUnsignedInteger(sparebits.substring(53,57)));
            String yyyy = "20"+String.format("%02d", binaryToUnsignedInteger(sparebits.substring(57,64)));
            String hh = String.format("%02d", binaryToUnsignedInteger(sparebits.substring(64,69)));
            String MM = String.format("%02d", binaryToUnsignedInteger(sparebits.substring(69,75)));
            //"yyyy-MM-dd HH:mm:ss"
            String datetime = yyyy+"-"+mm+"-"+dd+" "+hh+":"+MM+":00";
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(GPSDate.df.parse(datetime));
                int validity = binaryToUnsignedInteger(sparebits.substring(75,76));
                if(validity==0){c.add(Calendar.DAY_OF_MONTH,1);}else
                if(validity==1){c.add(Calendar.DAY_OF_MONTH,2);}
                c.set(Calendar.HOUR_OF_DAY,23);
                c.set(Calendar.MINUTE,30);
                c.set(Calendar.SECOND,00);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String ADVICE = String.format("%02d", binaryToUnsignedInteger(sparebits.substring(76,78)));
            try{
                String fromdate = AppConstants.display_dateformat.format(new SimpleDateFormat("yyyy-MM-dd").parse(yyyy+"-"+mm+"-"+dd));
                String todate = AppConstants.display_dateformat.format(c.getTime());
                String fromhr = hh+":"+MM;
                String tohr = AppConstants.display_timeformat.format(c.getTime());
                String range1 = ((double)wh1/10)+"";
                String range2 = ((double)wh2/10)+"";
                String scs1 = ((double)cs1/10)+"";
                String scs2 = ((double)cs2/10)+"";
                String coast = region;
                String fromloc = (nv.getFishLandingcenters(coast).get(loc1));
                String toloc = (nv.getFishLandingcenters(coast).get(loc2));
                String advice = getNavicAdvice(ADVICE);
                message = fromdate+","+todate+","+fromhr+","+tohr+","+range1+","+range2+","+scs1+","+scs2+","+coast+","+fromloc+","+toloc+","+advice;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return message;
    }
    public int binaryToUnsignedInteger(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i = numbers.length - 1; i >= 0; --i) {
            if (numbers[i] == '1') {
                result = (int)((double)result + Math.pow(2.0D, (double)(numbers.length - i - 1)));
            }
        }
        return result;
    }

    public String getNavicAdvice(String code){
        String advice = null;
        if(code.equalsIgnoreCase("00")){
            advice  = "Fishermen not to venture into open seas";
        }
        if(code.equalsIgnoreCase("01")){
            advice  = "Fishermen at Sea are requested to come to nearest ports";
        }
        if(code.equalsIgnoreCase("10")){
            advice  = "Fishermen to be cautious while going out in the sea";
        }
        if(code.equalsIgnoreCase("11")){
            advice  = "Fishermen are advised to return to coast";
        }
        return advice;
    }





    public String getNavicStateBinaryPattern(String state){
        String binarypattern = "";
        if(state.equalsIgnoreCase("0001")){binarypattern = "Gujarat";}else
        if(state.equalsIgnoreCase("0010")){binarypattern = "Maharashtra";}else
        if(state.equalsIgnoreCase("0011")){binarypattern = "Goa";}else
        if(state.equalsIgnoreCase("0100")){binarypattern = "Karnataka";}else
        if(state.equalsIgnoreCase("0101")){binarypattern = "Kerala";}else
        if(state.equalsIgnoreCase("0110")){binarypattern = "SouthTamilNadu";}else
        if(state.equalsIgnoreCase("0111")){binarypattern = "NorthTamilNadu";}else
        if(state.equalsIgnoreCase("1000")){binarypattern = "SouthAndhraPradesh";}else
        if(state.equalsIgnoreCase("1001")){binarypattern = "NorthAndhraPradesh";}else
        if(state.equalsIgnoreCase("1010")){binarypattern = "Odisha";}else
        if(state.equalsIgnoreCase("1011")){binarypattern = "WestBengal";}else
        if(state.equalsIgnoreCase("1100")){binarypattern = "Andaman";}else
        if(state.equalsIgnoreCase("1101")){binarypattern = "Nicobar";}else
        if(state.equalsIgnoreCase("1110")){binarypattern = "Lakshadweep";}
        return binarypattern;
    }



    public String getNaviSystemcategory(String state){
        String binarypattern = "";
        if(state.equalsIgnoreCase("000")){binarypattern = "Low Pressure";}else
        if(state.equalsIgnoreCase("001")){binarypattern = "Depression";}else
        if(state.equalsIgnoreCase("010")){binarypattern = "Deep Depression";}else
        if(state.equalsIgnoreCase("011")){binarypattern = "Cyclone Storm";}else
        if(state.equalsIgnoreCase("100")){binarypattern = "Severe Cyclone Storm";}else
        if(state.equalsIgnoreCase("101")){binarypattern = "Very Severe Cyclone Storm";}else
        if(state.equalsIgnoreCase("110")){binarypattern = "Extremely Severe Cyclone Storm";}else
        if(state.equalsIgnoreCase("111")){binarypattern = "Super Cyclonic Storm";}
        return binarypattern;
    }

    public String getNavicSystemName(String state){
        String binarypattern = "";
        if(state.equalsIgnoreCase("001010")){binarypattern = "Hikaa";}else
        if(state.equalsIgnoreCase("001011")){binarypattern = "Kyarr";}else
        if(state.equalsIgnoreCase("001100")){binarypattern = "Maha";}else
        if(state.equalsIgnoreCase("001101")){binarypattern = "Bulbul";}else
        if(state.equalsIgnoreCase("001110")){binarypattern = "Pawn";}else
        if(state.equalsIgnoreCase("001111")){binarypattern = "Amphan";}
        return binarypattern;
    }

    public String getNavicDirection(String state){
        String binarypattern = null;
        if(state.equalsIgnoreCase("000")){binarypattern="S";}else
        if(state.equalsIgnoreCase("001")){binarypattern="E";}else
        if(state.equalsIgnoreCase("010")){binarypattern="N";}else
        if(state.equalsIgnoreCase("011")){binarypattern="W";}else
        if(state.equalsIgnoreCase("100")){binarypattern="NW";}else
        if(state.equalsIgnoreCase("101")){binarypattern="NE";}else
        if(state.equalsIgnoreCase("110")){binarypattern="SW";}else
        if(state.equalsIgnoreCase("111")){binarypattern="SE";}
        return binarypattern.toLowerCase();
    }

}
