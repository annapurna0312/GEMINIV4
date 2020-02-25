package com.example.geminiv4.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.geminiv4.multilingual.FishLandingCenter;
import com.example.geminiv4.multilingual.LanguageNavigationHelper;
import com.example.geminiv4.multilingual.MultiLingualUtils;

import java.util.ArrayList;



public class FishMarketDataBase {

    SQLiteDatabase mDatabase;
    Context context;
    String FISHPRICES = "fishprices";
    String FISHTYPES = "fishtypes";
    String MARKETS = "marketlocations";
    MultiLingualUtils multiLingualUtils;
    FishLandingCenter fishLandingCenter;
    LanguageNavigationHelper languageNavigationHelper;

    String fishimages[] = {"anchovyheadless","baaracuda","basa","blackpromfret","indiansolomonbig","indiansolomonsmall","kingfish","kingfishsteakonly","mackerel","pinkpreach","ponyfish","redsnapper","seerfishbig","seerfishmedium","seerfishsmall","tuna","whitepromfretbig","whitepromfret"};


    public FishMarketDataBase(Context context) {
        this.context = context;
        mDatabase = context.openOrCreateDatabase("incois_messages_db", Context.MODE_PRIVATE, null);
        createTableIfNotExist();
        multiLingualUtils = new MultiLingualUtils();
        fishLandingCenter = new FishLandingCenter();
        languageNavigationHelper = new LanguageNavigationHelper(context);
    }

    public void updateDetailsThroughGAGAN(String message){
        String[] triplets = message.split("\\;");
        StringBuilder sb = new StringBuilder();
        for(String triplet : triplets){
            String[] ele = triplet.split("\\,");
            setFishPrice(ele[0],ele[1],ele[2]);
            sb.append(getFishPrice(ele[0],ele[1])+"  ");
        }
        System.out.println("===============> Fish Market Data Update : "+ sb+"<--");
    }







    private void createTableIfNotExist() {
        /*mDatabase.execSQL("DROP TABLE IF EXISTS " + MARKETS);
        mDatabase.execSQL("DROP TABLE IF EXISTS " + FISHTYPES);
        mDatabase.execSQL("DROP TABLE IF EXISTS " + FISHPRICES);*/

        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+FISHPRICES+" (mid INTEGER, fid INTEGER NOT NULL ,price 		VARCHAR(40));");
        //updateFishPrices();
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+FISHTYPES+" (fid INTEGER, name VARCHAR(100));");
        //updateFishTypeData();
        // id | name| state | district | taluk | block | market_type | latlon
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+MARKETS+" (mid INTEGER, name VARCHAR(40), state VARCHAR(40), district VARCHAR(40), taluk VARCHAR(40), block VARCHAR(40),market_type VARCHAR(40),lat VARCHAR(40),lon VARCHAR(40));");
        updateFishMarketData();
    }









    public int getFishPrice(String flcid, String fishid){
        Cursor imsgs = mDatabase.rawQuery("SELECT price FROM "+FISHPRICES+" where flcid=? and fid=?", new String[]{flcid,fishid});
        int price = 0;
        if (imsgs.moveToFirst()) {
            do {
                price = imsgs.getInt(0);
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return price;
    }

    public int getFishPriceTable(){
        Cursor imsgs = mDatabase.rawQuery("SELECT count(1) FROM "+FISHPRICES+" ", new String[]{});
        int count = 0;
        if (imsgs.moveToFirst()) {
            do {
                count = imsgs.getInt(0);
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return count;
    }

    public void setFishPrice(String flcid, String fishid, String price){
        ContentValues cv = new ContentValues();
        cv.put("price",price);
        mDatabase.update(FISHPRICES, cv, "flcid=? and fid=?", new String[]{flcid,fishid});
    }

    public int getFLCID(String state, String district, String flcname){
        Cursor imsgs = mDatabase.rawQuery("SELECT flcid FROM "+MARKETS+" where state=? and district=? and flcname=?", new String[]{state,district,flcname});
        int price = 0;
        if (imsgs.moveToFirst()) {
            do {
                price = imsgs.getInt(0);
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return price;
    }

    public ArrayList<Integer> getFishIDS(String state, String district, String flcname){
        ArrayList<Integer> ret = new ArrayList<>();
        int flcid = getFLCID(state,district,flcname);
        if(flcid>0){
            Cursor imsgs = mDatabase.rawQuery("SELECT distinct fid FROM "+FISHPRICES+" where flcid=? ", new String[]{flcid+""});
            int price = 0;
            if (imsgs.moveToFirst()) {
                do {
                    ret.add(imsgs.getInt(0));
                } while (imsgs.moveToNext());
            }
            imsgs.close();
        }
        return ret;
    }





    public ArrayList<String> getStates(){
        ArrayList<String> ret = new ArrayList<>();
        Cursor imsgs = mDatabase.rawQuery("SELECT distinct state FROM "+MARKETS+"", new String[]{});
        int price = 0;
        if (imsgs.moveToFirst()) {
            do {
                ret.add(imsgs.getString(0));
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return ret;
    }
    public ArrayList<String> getMultiLingualStates(){
        ArrayList<String> ret = new ArrayList<>();
        for(String state : getStates()){
            ret.add(multiLingualUtils.getZoneFromStringsFile(state,context));
        }
        return ret;
    }







    public ArrayList<String> getDistricts(String state){
        ArrayList<String> ret = new ArrayList<>();
        Cursor imsgs = mDatabase.rawQuery("SELECT distinct district FROM "+MARKETS+" where state=?", new String[]{state});
        int price = 0;
        if (imsgs.moveToFirst()) {
            do {
                ret.add(imsgs.getString(0));
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return ret;
    }
    public ArrayList<String> getMultilingualDistricts(String state){
        ArrayList<String> ret = new ArrayList<>();
        for(String district : getDistricts(state)){
            ret.add(fishLandingCenter.getMultiLingualDistrict(district.toLowerCase().replaceAll(" ",""),languageNavigationHelper.getSelectedLanguageIndex()));
        }
        return ret;
    }



    public ArrayList<String> getMarketNames(String state){
        //flcid,state,district,flcname
        Log.d("Markets" ,"==>"+state);
        ArrayList<String> ret = new ArrayList<>();
        Cursor imsgs = mDatabase.rawQuery("SELECT name FROM "+MARKETS+" where state=?", new String[]{state});
        if (imsgs.moveToFirst()) {
            do {
                Log.d("Markets" ,"==>"+state+"==>"+imsgs.getString(0));
                ret.add(imsgs.getString(0));
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return ret;
    }



    public ArrayList<String> getFlcNames(String state, String district){
        //flcid,state,district,flcname
        ArrayList<String> ret = new ArrayList<>();
        Cursor imsgs = mDatabase.rawQuery("SELECT distinct flcname FROM "+MARKETS+" where state=? and district=?", new String[]{state,district});
        if (imsgs.moveToFirst()) {
            do {
                ret.add(imsgs.getString(0));
            } while (imsgs.moveToNext());
        }
        imsgs.close();
        return ret;
    }
    public ArrayList<String> getMultilingualFlcNames(String state, String district){
        ArrayList<String> ret = new ArrayList<>();
        for(String flc : getFlcNames(state,district)){
            ret.add(fishLandingCenter.getMultiLingualFLC(flc,languageNavigationHelper.getSelectedLanguageIndex()));
        }
        return ret;
    }







    private void updateFishMarketData() {
        mDatabase.delete(MARKETS, "1=1", new String[]{});
        String sqladd1 = "insert into "+MARKETS+"\n" + "(mid,name,state,district,taluk,block,market_type,lat,lon)\n" + "values\n" +"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String data = "|  1 | Bapatla Fish Market                          | SouthAndhraPradesh | Guntur        | Bapatla           | Bapatla           | Wholesale & Retail | 15.903944,80.467140 |\n" +
                      "|  2 | Eluru Wholesale Fish Market (New Market)     | SouthAndhraPradesh | West Godavari | Eluru             | Eluru             | Wholesale          | 16.710661,81.095245 |\n" +
                      "|  3 | Guntur Venlok Municipal Market               | SouthAndhraPradesh | Guntur        | Guntur            | Guntur            | Wholesale & Retail | 16.306652,80.436539 |\n" +
                      "|  4 | Gantasthambam Wholesale fish market          | NorthAndhraPradesh | Vizianagaram  | Vizianagaram      | Vizianagaram      | Wholesale & Retail | 18.106659,83.395554 |\n" +
                      "|  5 | Chilakaluripeta Fish Market                  | SouthAndhraPradesh | Guntur        | Guntur            | Guntur            | Wholesale & Retail | 16.306652,80.436539 |\n" +
                      "|  6 | Ongole Wholesale Fish Market                 | SouthAndhraPradesh | Prakasam      | Ongole            | Ongole            | Wholesale          | 15.505723,80.049919 |\n" +
                      "|  7 | Hanumantha Raya Fish Market                  | SouthAndhraPradesh | Krishna       | Vijayawada        | Vijayawada        | Retail             | 16.506174,80.648018 |\n" +
                      "|  8 | Ponnur Fish market                           | SouthAndhraPradesh | Guntur        | Guntur            | Guntur            | Wholesale          | 16.306652,80.436539 |\n" +
                      "|  9 | Chinna Market                                | NorthAndhraPradesh | East Godavari | Kakinada          | Kakinada          | Retail             | 16.989065,82.247467 |\n" +
                      "| 10 | Mahanthi Fish Market                         | SouthAndhraPradesh | Krishna       | Vijayawada        | Vijayawada        | Wholesale & Retail | 16.506174,80.648018 |\n" +
                      "| 11 | Mypeda gate market                           | SouthAndhraPradesh | SPSR Nellore  | Nellore           | Nellore           | Retail             | 14.442599,79.986458 |\n" +
                      "| 12 | Jampeta Fish Market                          | NorthAndhraPradesh | East Godavari | Rajamahendravaram | Rajamahendravaram | Wholesale & Retail | 17.000538,81.804031 |\n" +
                      "| 13 | Machilipatnam wholesale & Retail Fish Market | SouthAndhraPradesh | Krishna       | Machilipatnam     | Machilipatnam     | Wholesale & Retail | 16.190546,81.136154 |\n" +
                      "| 14 | Padamatilanka Fish Market                    | SouthAndhraPradesh | Krishna       | Vijaywada         | Vijaywada         | Wholesale & Retail | 16.506174,80.648018 |\n" +
                      "| 15 | NFDB Market, Prakash Nagar                   | SouthAndhraPradesh | Krishna       | Prakash nagar     | Prakash nagar     | Retail             | 16.506174,80.648018 |\n" +
                      "| 16 | Markapuram Fish Market                       | SouthAndhraPradesh | Prakasam      | Ongole            | Ongole            | Retail             | 15.505723,80.049919 |\n" +
                      "| 17 | Nellore Muncipal Market                      | SouthAndhraPradesh | SPSR Nellore  | Nellore           | Nellore           | Wholesale          | 14.442599,79.986458 |\n" +
                      "| 18 | Dycus Road Market                            | SouthAndhraPradesh | SPSR Nellore  | Nellore           | Nellore           | Retail             | 14.442599,79.986458 |\n";
        for(String line : data.split("\\n")){
            String[] elements = line.split("\\|");
            String id =  elements[1].trim();
            String name =  elements[2].trim();
            String zone =  elements[3].trim();
            String district =  elements[4].trim();
            String taluk =  elements[5].trim();
            String block =  elements[6].trim();
            String market_type =  elements[7].trim();
            String latlon =  elements[8].trim();
            String pos[] = latlon.split("\\,");
            Log.d("Markets","mDatabase.execSQL(sqladd1, new String[]{\t" +id+",\t" +name+",\t"+zone+",\t"+district+",\t" +taluk+",\t" +block+",\t" +market_type+",\t" +pos[0]+",\t" +pos[1]+"");
            mDatabase.execSQL(sqladd1, new String[]{
                    "\""+id+"\"",
                    "\""+name+"\"",
                    "\""+zone+"\"",
                    "\""+district+"\"",
                    "\""+taluk+"\"",
                    "\""+block+"\"",
                    "\""+market_type+"\"",
                    "\""+pos[0]+"\"",
                    "\""+pos[1]+"\"",
            });
        }
    }


    private void updateFishPrices(){
        mDatabase.delete(FISHPRICES, "1=1", new String[]{});
        String sqladd1 = "insert into "+FISHPRICES+"\n" + "(mid,fid,price)\n" + "values\n" +"(?, ?, ?)";
        String data = "|   1 |   1 | 1.00000 |\n" +
                "|   1 |   2 | 0.00000 |\n" +
                "|   1 |   3 | 0.00000 |\n" +
                "|   1 |   4 | 0.00000 |\n" +
                "|   1 |   5 | 0.00000 |\n" +
                "|   1 |   6 | 0.00000 |\n" +
                "|   1 |   7 | 0.00000 |\n" +
                "|   1 |   8 | 0.00000 |\n" +
                "|   1 |   9 | 0.00000 |\n" +
                "|   1 |  10 | 0.00000 |\n" +
                "|   1 |  11 | 0.00000 |\n" +
                "|   1 |  12 | 0.00000 |\n" +
                "|   1 |  13 | 0.00000 |\n" +
                "|   1 |  14 | 0.00000 |\n" +
                "|   1 |  15 | 0.00000 |\n" +
                "|   1 |  16 | 0.00000 |\n" +
                "|   1 |  17 | 0.00000 |\n" +
                "|   1 |  18 | 0.00000 |\n" +
                "|   1 |  19 | 0.00000 |\n" +
                "|   1 |  20 | 0.00000 |\n" +
                "|   1 |  21 | 0.00000 |\n" +
                "|   1 |  22 | 0.00000 |\n" +
                "|   1 |  23 | 0.00000 |\n" +
                "|   1 |  24 | 0.00000 |\n" +
                "|   1 |  25 | 0.00000 |\n" +
                "|   1 |  26 | 0.00000 |\n" +
                "|   1 |  27 | 0.00000 |\n" +
                "|   1 |  28 | 0.00000 |\n" +
                "|   1 |  29 | 0.00000 |\n" +
                "|   1 |  30 | 0.00000 |\n" +
                "|   1 |  31 | 0.00000 |\n" +
                "|   1 |  32 | 0.00000 |\n" +
                "|   1 |  33 | 0.00000 |\n" +
                "|   1 |  34 | 0.00000 |\n" +
                "|   1 |  35 | 0.00000 |\n" +
                "|   1 |  36 | 0.00000 |\n" +
                "|   1 |  37 | 0.00000 |\n" +
                "|   1 |  38 | 0.00000 |\n" +
                "|   1 |  39 | 0.00000 |\n" +
                "|   1 |  40 | 0.00000 |\n" +
                "|   1 |  41 | 0.00000 |\n" +
                "|   1 |  42 | 0.00000 |\n" +
                "|   1 |  43 | 0.00000 |\n" +
                "|   1 |  44 | 0.00000 |\n" +
                "|   1 |  45 | 0.00000 |\n" +
                "|   1 |  46 | 0.00000 |\n" +
                "|   1 |  47 | 0.00000 |\n" +
                "|   1 |  48 | 0.00000 |\n" +
                "|   1 |  49 | 0.00000 |\n" +
                "|   1 |  50 | 0.00000 |\n" +
                "|   1 |  51 | 0.00000 |\n" +
                "|   1 |  52 | 0.00000 |\n" +
                "|   1 |  53 | 0.00000 |\n" +
                "|   1 |  54 | 0.00000 |\n" +
                "|   1 |  55 | 0.00000 |\n" +
                "|   1 |  56 | 0.00000 |\n" +
                "|   1 |  57 | 0.00000 |\n" +
                "|   1 |  58 | 0.00000 |\n" +
                "|   1 |  59 | 0.00000 |\n" +
                "|   1 |  60 | 0.00000 |\n" +
                "|   1 |  61 | 0.00000 |\n" +
                "|   1 |  62 | 0.00000 |\n" +
                "|   1 |  63 | 0.00000 |\n" +
                "|   1 |  64 | 0.00000 |\n" +
                "|   1 |  65 | 0.00000 |\n" +
                "|   1 |  66 | 0.00000 |\n" +
                "|   1 |  67 | 0.00000 |\n" +
                "|   1 |  68 | 0.00000 |\n" +
                "|   1 |  69 | 0.00000 |\n" +
                "|   1 |  70 | 0.00000 |\n" +
                "|   1 |  71 | 0.00000 |\n" +
                "|   1 |  72 | 0.00000 |\n" +
                "|   1 |  73 | 0.00000 |\n" +
                "|   1 |  74 | 0.00000 |\n" +
                "|   1 |  75 | 0.00000 |\n" +
                "|   1 |  76 | 0.00000 |\n" +
                "|   1 |  77 | 0.00000 |\n" +
                "|   1 |  78 | 0.00000 |\n" +
                "|   1 |  79 | 0.00000 |\n" +
                "|   1 |  80 | 0.00000 |\n" +
                "|   1 |  81 | 0.00000 |\n" +
                "|   1 |  82 | 0.00000 |\n" +
                "|   1 |  83 | 0.00000 |\n" +
                "|   1 |  84 | 0.00000 |\n" +
                "|   1 |  85 | 0.00000 |\n" +
                "|   1 |  86 | 0.00000 |\n" +
                "|   1 |  87 | 0.00000 |\n" +
                "|   1 |  88 | 0.00000 |\n" +
                "|   1 |  89 | 0.00000 |\n" +
                "|   1 |  90 | 0.00000 |\n" +
                "|   1 |  91 | 0.00000 |\n" +
                "|   1 |  92 | 0.00000 |\n" +
                "|   1 |  93 | 0.00000 |\n" +
                "|   1 |  94 | 0.00000 |\n" +
                "|   1 |  95 | 0.00000 |\n" +
                "|   1 |  96 | 0.00000 |\n" +
                "|   1 |  97 | 0.00000 |\n" +
                "|   1 |  98 | 0.00000 |\n" +
                "|   1 |  99 | 0.00000 |\n" +
                "|   1 | 100 | 0.00000 |\n" +
                "|   1 | 101 | 0.00000 |\n" +
                "|   1 | 102 | 0.00000 |\n" +
                "|   1 | 103 | 0.00000 |\n" +
                "|   1 | 104 | 0.00000 |\n" +
                "|   1 | 105 | 0.00000 |\n" +
                "|   1 | 106 | 0.00000 |\n" +
                "|   1 | 107 | 0.00000 |\n" +
                "|   1 | 108 | 0.00000 |\n" +
                "|   1 | 109 | 0.00000 |\n" +
                "|   1 | 110 | 0.00000 |\n" +
                "|   1 | 111 | 0.00000 |\n" +
                "|   1 | 112 | 0.00000 |\n" +
                "|   1 | 113 | 0.00000 |\n" +
                "|   1 | 114 | 0.00000 |\n" +
                "|   1 | 115 | 0.00000 |\n" +
                "|   1 | 116 | 0.00000 |\n" +
                "|   1 | 117 | 0.00000 |\n" +
                "|   1 | 118 | 0.00000 |\n" +
                "|   1 | 119 | 0.00000 |\n" +
                "|   1 | 120 | 0.00000 |\n" +
                "|   1 | 121 | 0.00000 |\n" +
                "|   1 | 122 | 0.00000 |\n" +
                "|   1 | 123 | 0.00000 |\n" +
                "|   1 | 124 | 0.00000 |\n" +
                "|   1 | 125 | 0.00000 |\n" +
                "|   1 | 126 | 0.00000 |\n" +
                "|   1 | 127 | 0.00000 |\n" +
                "|   1 | 128 | 0.00000 |\n" +
                "|   1 | 129 | 0.00000 |\n" +
                "|   1 | 130 | 0.00000 |\n" +
                "|   1 | 131 | 0.00000 |\n" +
                "|   1 | 132 | 0.00000 |\n" +
                "|   1 | 133 | 0.00000 |\n" +
                "|   1 | 134 | 0.00000 |\n" +
                "|   1 | 135 | 0.00000 |\n" +
                "|   1 | 136 | 0.00000 |\n" +
                "|   1 | 137 | 0.00000 |\n" +
                "|   1 | 138 | 0.00000 |\n" +
                "|   1 | 139 | 0.00000 |\n" +
                "|   1 | 140 | 0.00000 |\n" +
                "|   1 | 141 | 0.00000 |\n" +
                "|   1 | 142 | 0.00000 |\n" +
                "|   1 | 143 | 0.00000 |\n" +
                "|   1 | 144 | 0.00000 |\n" +
                "|   1 | 145 | 0.00000 |\n" +
                "|   1 | 146 | 0.00000 |\n" +
                "|   1 | 147 | 0.00000 |\n" +
                "|   2 |   1 | 1.00000 |\n" +
                "|   2 |   2 | 0.00000 |\n" +
                "|   2 |   3 | 0.00000 |\n" +
                "|   2 |   4 | 0.00000 |\n" +
                "|   2 |   5 | 0.00000 |\n" +
                "|   2 |   6 | 0.00000 |\n" +
                "|   2 |   7 | 0.00000 |\n" +
                "|   2 |   8 | 0.00000 |\n" +
                "|   2 |   9 | 0.00000 |\n" +
                "|   2 |  10 | 0.00000 |\n" +
                "|   2 |  11 | 0.00000 |\n" +
                "|   2 |  12 | 0.00000 |\n" +
                "|   2 |  13 | 0.00000 |\n" +
                "|   2 |  14 | 0.00000 |\n" +
                "|   2 |  15 | 0.00000 |\n" +
                "|   2 |  16 | 0.00000 |\n" +
                "|   2 |  17 | 0.00000 |\n" +
                "|   2 |  18 | 0.00000 |\n" +
                "|   2 |  19 | 0.00000 |\n" +
                "|   2 |  20 | 0.00000 |\n" +
                "|   2 |  21 | 0.00000 |\n" +
                "|   2 |  22 | 0.00000 |\n" +
                "|   2 |  23 | 0.00000 |\n" +
                "|   2 |  24 | 0.00000 |\n" +
                "|   2 |  25 | 0.00000 |\n" +
                "|   2 |  26 | 0.00000 |\n" +
                "|   2 |  27 | 0.00000 |\n" +
                "|   2 |  28 | 0.00000 |\n" +
                "|   2 |  29 | 0.00000 |\n" +
                "|   2 |  30 | 0.00000 |\n" +
                "|   2 |  31 | 0.00000 |\n" +
                "|   2 |  32 | 0.00000 |\n" +
                "|   2 |  33 | 0.00000 |\n" +
                "|   2 |  34 | 0.00000 |\n" +
                "|   2 |  35 | 0.00000 |\n" +
                "|   2 |  36 | 0.00000 |\n" +
                "|   2 |  37 | 0.00000 |\n" +
                "|   2 |  38 | 0.00000 |\n" +
                "|   2 |  39 | 0.00000 |\n" +
                "|   2 |  40 | 0.00000 |\n" +
                "|   2 |  41 | 0.00000 |\n" +
                "|   2 |  42 | 0.00000 |\n" +
                "|   2 |  43 | 0.00000 |\n" +
                "|   2 |  44 | 0.00000 |\n" +
                "|   2 |  45 | 0.00000 |\n" +
                "|   2 |  46 | 0.00000 |\n" +
                "|   2 |  47 | 0.00000 |\n" +
                "|   2 |  48 | 0.00000 |\n" +
                "|   2 |  49 | 0.00000 |\n" +
                "|   2 |  50 | 0.00000 |\n" +
                "|   2 |  51 | 0.00000 |\n" +
                "|   2 |  52 | 0.00000 |\n" +
                "|   2 |  53 | 0.00000 |\n" +
                "|   2 |  54 | 0.00000 |\n" +
                "|   2 |  55 | 0.00000 |\n" +
                "|   2 |  56 | 0.00000 |\n" +
                "|   2 |  57 | 0.00000 |\n" +
                "|   2 |  58 | 0.00000 |\n" +
                "|   2 |  59 | 0.00000 |\n" +
                "|   2 |  60 | 0.00000 |\n" +
                "|   2 |  61 | 0.00000 |\n" +
                "|   2 |  62 | 0.00000 |\n" +
                "|   2 |  63 | 0.00000 |\n" +
                "|   2 |  64 | 0.00000 |\n" +
                "|   2 |  65 | 0.00000 |\n" +
                "|   2 |  66 | 0.00000 |\n" +
                "|   2 |  67 | 0.00000 |\n" +
                "|   2 |  68 | 0.00000 |\n" +
                "|   2 |  69 | 0.00000 |\n" +
                "|   2 |  70 | 0.00000 |\n" +
                "|   2 |  71 | 0.00000 |\n" +
                "|   2 |  72 | 0.00000 |\n" +
                "|   2 |  73 | 0.00000 |\n" +
                "|   2 |  74 | 0.00000 |\n" +
                "|   2 |  75 | 0.00000 |\n" +
                "|   2 |  76 | 0.00000 |\n" +
                "|   2 |  77 | 0.00000 |\n" +
                "|   2 |  78 | 0.00000 |\n" +
                "|   2 |  79 | 0.00000 |\n" +
                "|   2 |  80 | 0.00000 |\n" +
                "|   2 |  81 | 0.00000 |\n" +
                "|   2 |  82 | 0.00000 |\n" +
                "|   2 |  83 | 0.00000 |\n" +
                "|   2 |  84 | 0.00000 |\n" +
                "|   2 |  85 | 0.00000 |\n" +
                "|   2 |  86 | 0.00000 |\n" +
                "|   2 |  87 | 0.00000 |\n" +
                "|   2 |  88 | 0.00000 |\n" +
                "|   2 |  89 | 0.00000 |\n" +
                "|   2 |  90 | 0.00000 |\n" +
                "|   2 |  91 | 0.00000 |\n" +
                "|   2 |  92 | 0.00000 |\n" +
                "|   2 |  93 | 0.00000 |\n" +
                "|   2 |  94 | 0.00000 |\n" +
                "|   2 |  95 | 0.00000 |\n" +
                "|   2 |  96 | 0.00000 |\n" +
                "|   2 |  97 | 0.00000 |\n" +
                "|   2 |  98 | 0.00000 |\n" +
                "|   2 |  99 | 0.00000 |\n" +
                "|   2 | 100 | 0.00000 |\n" +
                "|   2 | 101 | 0.00000 |\n" +
                "|   2 | 102 | 0.00000 |\n" +
                "|   2 | 103 | 0.00000 |\n" +
                "|   2 | 104 | 0.00000 |\n" +
                "|   2 | 105 | 0.00000 |\n" +
                "|   2 | 106 | 0.00000 |\n" +
                "|   2 | 107 | 0.00000 |\n" +
                "|   2 | 108 | 0.00000 |\n" +
                "|   2 | 109 | 0.00000 |\n" +
                "|   2 | 110 | 0.00000 |\n" +
                "|   2 | 111 | 0.00000 |\n" +
                "|   2 | 112 | 0.00000 |\n" +
                "|   2 | 113 | 0.00000 |\n" +
                "|   2 | 114 | 0.00000 |\n" +
                "|   2 | 115 | 0.00000 |\n" +
                "|   2 | 116 | 0.00000 |\n" +
                "|   2 | 117 | 0.00000 |\n" +
                "|   2 | 118 | 0.00000 |\n" +
                "|   2 | 119 | 0.00000 |\n" +
                "|   2 | 120 | 0.00000 |\n" +
                "|   2 | 121 | 0.00000 |\n" +
                "|   2 | 122 | 0.00000 |\n" +
                "|   2 | 123 | 0.00000 |\n" +
                "|   2 | 124 | 0.00000 |\n" +
                "|   2 | 125 | 0.00000 |\n" +
                "|   2 | 126 | 0.00000 |\n" +
                "|   2 | 127 | 0.00000 |\n" +
                "|   2 | 128 | 0.00000 |\n" +
                "|   2 | 129 | 0.00000 |\n" +
                "|   2 | 130 | 0.00000 |\n" +
                "|   2 | 131 | 0.00000 |\n" +
                "|   2 | 132 | 0.00000 |\n" +
                "|   2 | 133 | 0.00000 |\n" +
                "|   2 | 134 | 0.00000 |\n" +
                "|   2 | 135 | 0.00000 |\n" +
                "|   2 | 136 | 0.00000 |\n" +
                "|   2 | 137 | 0.00000 |\n" +
                "|   2 | 138 | 0.00000 |\n" +
                "|   2 | 139 | 0.00000 |\n" +
                "|   2 | 140 | 0.00000 |\n" +
                "|   2 | 141 | 0.00000 |\n" +
                "|   2 | 142 | 0.00000 |\n" +
                "|   2 | 143 | 0.00000 |\n" +
                "|   2 | 144 | 0.00000 |\n" +
                "|   2 | 145 | 0.00000 |\n" +
                "|   2 | 146 | 0.00000 |\n" +
                "|   2 | 147 | 0.00000 |\n" +
                "|   3 |   1 | 1.00000 |\n" +
                "|   3 |   2 | 0.00000 |\n" +
                "|   3 |   3 | 0.00000 |\n" +
                "|   3 |   4 | 0.00000 |\n" +
                "|   3 |   5 | 0.00000 |\n" +
                "|   3 |   6 | 0.00000 |\n" +
                "|   3 |   7 | 0.00000 |\n" +
                "|   3 |   8 | 0.00000 |\n" +
                "|   3 |   9 | 0.00000 |\n" +
                "|   3 |  10 | 0.00000 |\n" +
                "|   3 |  11 | 0.00000 |\n" +
                "|   3 |  12 | 0.00000 |\n" +
                "|   3 |  13 | 0.00000 |\n" +
                "|   3 |  14 | 0.00000 |\n" +
                "|   3 |  15 | 0.00000 |\n" +
                "|   3 |  16 | 0.00000 |\n" +
                "|   3 |  17 | 0.00000 |\n" +
                "|   3 |  18 | 0.00000 |\n" +
                "|   3 |  19 | 0.00000 |\n" +
                "|   3 |  20 | 0.00000 |\n" +
                "|   3 |  21 | 0.00000 |\n" +
                "|   3 |  22 | 0.00000 |\n" +
                "|   3 |  23 | 0.00000 |\n" +
                "|   3 |  24 | 0.00000 |\n" +
                "|   3 |  25 | 0.00000 |\n" +
                "|   3 |  26 | 0.00000 |\n" +
                "|   3 |  27 | 0.00000 |\n" +
                "|   3 |  28 | 0.00000 |\n" +
                "|   3 |  29 | 0.00000 |\n" +
                "|   3 |  30 | 0.00000 |\n" +
                "|   3 |  31 | 0.00000 |\n" +
                "|   3 |  32 | 0.00000 |\n" +
                "|   3 |  33 | 0.00000 |\n" +
                "|   3 |  34 | 0.00000 |\n" +
                "|   3 |  35 | 0.00000 |\n" +
                "|   3 |  36 | 0.00000 |\n" +
                "|   3 |  37 | 0.00000 |\n" +
                "|   3 |  38 | 0.00000 |\n" +
                "|   3 |  39 | 0.00000 |\n" +
                "|   3 |  40 | 0.00000 |\n" +
                "|   3 |  41 | 0.00000 |\n" +
                "|   3 |  42 | 0.00000 |\n" +
                "|   3 |  43 | 0.00000 |\n" +
                "|   3 |  44 | 0.00000 |\n" +
                "|   3 |  45 | 0.00000 |\n" +
                "|   3 |  46 | 0.00000 |\n" +
                "|   3 |  47 | 0.00000 |\n" +
                "|   3 |  48 | 0.00000 |\n" +
                "|   3 |  49 | 0.00000 |\n" +
                "|   3 |  50 | 0.00000 |\n" +
                "|   3 |  51 | 0.00000 |\n" +
                "|   3 |  52 | 0.00000 |\n" +
                "|   3 |  53 | 0.00000 |\n" +
                "|   3 |  54 | 0.00000 |\n" +
                "|   3 |  55 | 0.00000 |\n" +
                "|   3 |  56 | 0.00000 |\n" +
                "|   3 |  57 | 0.00000 |\n" +
                "|   3 |  58 | 0.00000 |\n" +
                "|   3 |  59 | 0.00000 |\n" +
                "|   3 |  60 | 0.00000 |\n" +
                "|   3 |  61 | 0.00000 |\n" +
                "|   3 |  62 | 0.00000 |\n" +
                "|   3 |  63 | 0.00000 |\n" +
                "|   3 |  64 | 0.00000 |\n" +
                "|   3 |  65 | 0.00000 |\n" +
                "|   3 |  66 | 0.00000 |\n" +
                "|   3 |  67 | 0.00000 |\n" +
                "|   3 |  68 | 0.00000 |\n" +
                "|   3 |  69 | 0.00000 |\n" +
                "|   3 |  70 | 0.00000 |\n" +
                "|   3 |  71 | 0.00000 |\n" +
                "|   3 |  72 | 0.00000 |\n" +
                "|   3 |  73 | 0.00000 |\n" +
                "|   3 |  74 | 0.00000 |\n" +
                "|   3 |  75 | 0.00000 |\n" +
                "|   3 |  76 | 0.00000 |\n" +
                "|   3 |  77 | 0.00000 |\n" +
                "|   3 |  78 | 0.00000 |\n" +
                "|   3 |  79 | 0.00000 |\n" +
                "|   3 |  80 | 0.00000 |\n" +
                "|   3 |  81 | 0.00000 |\n" +
                "|   3 |  82 | 0.00000 |\n" +
                "|   3 |  83 | 0.00000 |\n" +
                "|   3 |  84 | 0.00000 |\n" +
                "|   3 |  85 | 0.00000 |\n" +
                "|   3 |  86 | 0.00000 |\n" +
                "|   3 |  87 | 0.00000 |\n" +
                "|   3 |  88 | 0.00000 |\n" +
                "|   3 |  89 | 0.00000 |\n" +
                "|   3 |  90 | 0.00000 |\n" +
                "|   3 |  91 | 0.00000 |\n" +
                "|   3 |  92 | 0.00000 |\n" +
                "|   3 |  93 | 0.00000 |\n" +
                "|   3 |  94 | 0.00000 |\n" +
                "|   3 |  95 | 0.00000 |\n" +
                "|   3 |  96 | 0.00000 |\n" +
                "|   3 |  97 | 0.00000 |\n" +
                "|   3 |  98 | 0.00000 |\n" +
                "|   3 |  99 | 0.00000 |\n" +
                "|   3 | 100 | 0.00000 |\n" +
                "|   3 | 101 | 0.00000 |\n" +
                "|   3 | 102 | 0.00000 |\n" +
                "|   3 | 103 | 0.00000 |\n" +
                "|   3 | 104 | 0.00000 |\n" +
                "|   3 | 105 | 0.00000 |\n" +
                "|   3 | 106 | 0.00000 |\n" +
                "|   3 | 107 | 0.00000 |\n" +
                "|   3 | 108 | 0.00000 |\n" +
                "|   3 | 109 | 0.00000 |\n" +
                "|   3 | 110 | 0.00000 |\n" +
                "|   3 | 111 | 0.00000 |\n" +
                "|   3 | 112 | 0.00000 |\n" +
                "|   3 | 113 | 0.00000 |\n" +
                "|   3 | 114 | 0.00000 |\n" +
                "|   3 | 115 | 0.00000 |\n" +
                "|   3 | 116 | 0.00000 |\n" +
                "|   3 | 117 | 0.00000 |\n" +
                "|   3 | 118 | 0.00000 |\n" +
                "|   3 | 119 | 0.00000 |\n" +
                "|   3 | 120 | 0.00000 |\n" +
                "|   3 | 121 | 0.00000 |\n" +
                "|   3 | 122 | 0.00000 |\n" +
                "|   3 | 123 | 0.00000 |\n" +
                "|   3 | 124 | 0.00000 |\n" +
                "|   3 | 125 | 0.00000 |\n" +
                "|   3 | 126 | 0.00000 |\n" +
                "|   3 | 127 | 0.00000 |\n" +
                "|   3 | 128 | 0.00000 |\n" +
                "|   3 | 129 | 0.00000 |\n" +
                "|   3 | 130 | 0.00000 |\n" +
                "|   3 | 131 | 0.00000 |\n" +
                "|   3 | 132 | 0.00000 |\n" +
                "|   3 | 133 | 0.00000 |\n" +
                "|   3 | 134 | 0.00000 |\n" +
                "|   3 | 135 | 0.00000 |\n" +
                "|   3 | 136 | 0.00000 |\n" +
                "|   3 | 137 | 0.00000 |\n" +
                "|   3 | 138 | 0.00000 |\n" +
                "|   3 | 139 | 0.00000 |\n" +
                "|   3 | 140 | 0.00000 |\n" +
                "|   3 | 141 | 0.00000 |\n" +
                "|   3 | 142 | 0.00000 |\n" +
                "|   3 | 143 | 0.00000 |\n" +
                "|   3 | 144 | 0.00000 |\n" +
                "|   3 | 145 | 0.00000 |\n" +
                "|   3 | 146 | 0.00000 |\n" +
                "|   3 | 147 | 0.00000 |\n" +
                "|   4 |   1 | 0.00000 |\n" +
                "|   4 |   2 | 0.00000 |\n" +
                "|   4 |   3 | 0.00000 |\n" +
                "|   4 |   4 | 0.00000 |\n" +
                "|   4 |   5 | 0.00000 |\n" +
                "|   4 |   6 | 0.00000 |\n" +
                "|   4 |   7 | 0.00000 |\n" +
                "|   4 |   8 | 0.00000 |\n" +
                "|   4 |   9 | 0.00000 |\n" +
                "|   4 |  10 | 0.00000 |\n" +
                "|   4 |  11 | 0.00000 |\n" +
                "|   4 |  12 | 0.00000 |\n" +
                "|   4 |  13 | 0.00000 |\n" +
                "|   4 |  14 | 0.00000 |\n" +
                "|   4 |  15 | 0.00000 |\n" +
                "|   4 |  16 | 0.00000 |\n" +
                "|   4 |  17 | 0.00000 |\n" +
                "|   4 |  18 | 0.00000 |\n" +
                "|   4 |  19 | 0.00000 |\n" +
                "|   4 |  20 | 0.00000 |\n" +
                "|   4 |  21 | 0.00000 |\n" +
                "|   4 |  22 | 0.00000 |\n" +
                "|   4 |  23 | 0.00000 |\n" +
                "|   4 |  24 | 0.00000 |\n" +
                "|   4 |  25 | 0.00000 |\n" +
                "|   4 |  26 | 0.00000 |\n" +
                "|   4 |  27 | 0.00000 |\n" +
                "|   4 |  28 | 0.00000 |\n" +
                "|   4 |  29 | 0.00000 |\n" +
                "|   4 |  30 | 0.00000 |\n" +
                "|   4 |  31 | 0.00000 |\n" +
                "|   4 |  32 | 0.00000 |\n" +
                "|   4 |  33 | 0.00000 |\n" +
                "|   4 |  34 | 0.00000 |\n" +
                "|   4 |  35 | 0.00000 |\n" +
                "|   4 |  36 | 0.00000 |\n" +
                "|   4 |  37 | 0.00000 |\n" +
                "|   4 |  38 | 0.00000 |\n" +
                "|   4 |  39 | 0.00000 |\n" +
                "|   4 |  40 | 0.00000 |\n" +
                "|   4 |  41 | 0.00000 |\n" +
                "|   4 |  42 | 0.00000 |\n" +
                "|   4 |  43 | 0.00000 |\n" +
                "|   4 |  44 | 0.00000 |\n" +
                "|   4 |  45 | 0.00000 |\n" +
                "|   4 |  46 | 0.00000 |\n" +
                "|   4 |  47 | 0.00000 |\n" +
                "|   4 |  48 | 0.00000 |\n" +
                "|   4 |  49 | 0.00000 |\n" +
                "|   4 |  50 | 0.00000 |\n" +
                "|   4 |  51 | 0.00000 |\n" +
                "|   4 |  52 | 0.00000 |\n" +
                "|   4 |  53 | 0.00000 |\n" +
                "|   4 |  54 | 0.00000 |\n" +
                "|   4 |  55 | 0.00000 |\n" +
                "|   4 |  56 | 0.00000 |\n" +
                "|   4 |  57 | 0.00000 |\n" +
                "|   4 |  58 | 0.00000 |\n" +
                "|   4 |  59 | 0.00000 |\n" +
                "|   4 |  60 | 0.00000 |\n" +
                "|   4 |  61 | 0.00000 |\n" +
                "|   4 |  62 | 0.00000 |\n" +
                "|   4 |  63 | 0.00000 |\n" +
                "|   4 |  64 | 0.00000 |\n" +
                "|   4 |  65 | 0.00000 |\n" +
                "|   4 |  66 | 0.00000 |\n" +
                "|   4 |  67 | 0.00000 |\n" +
                "|   4 |  68 | 0.00000 |\n" +
                "|   4 |  69 | 0.00000 |\n" +
                "|   4 |  70 | 0.00000 |\n" +
                "|   4 |  71 | 0.00000 |\n" +
                "|   4 |  72 | 0.00000 |\n" +
                "|   4 |  73 | 0.00000 |\n" +
                "|   4 |  74 | 0.00000 |\n" +
                "|   4 |  75 | 0.00000 |\n" +
                "|   4 |  76 | 0.00000 |\n" +
                "|   4 |  77 | 0.00000 |\n" +
                "|   4 |  78 | 0.00000 |\n" +
                "|   4 |  79 | 0.00000 |\n" +
                "|   4 |  80 | 0.00000 |\n" +
                "|   4 |  81 | 0.00000 |\n" +
                "|   4 |  82 | 0.00000 |\n" +
                "|   4 |  83 | 0.00000 |\n" +
                "|   4 |  84 | 0.00000 |\n" +
                "|   4 |  85 | 0.00000 |\n" +
                "|   4 |  86 | 0.00000 |\n" +
                "|   4 |  87 | 0.00000 |\n" +
                "|   4 |  88 | 0.00000 |\n" +
                "|   4 |  89 | 0.00000 |\n" +
                "|   4 |  90 | 0.00000 |\n" +
                "|   4 |  91 | 0.00000 |\n" +
                "|   4 |  92 | 0.00000 |\n" +
                "|   4 |  93 | 0.00000 |\n" +
                "|   4 |  94 | 0.00000 |\n" +
                "|   4 |  95 | 0.00000 |\n" +
                "|   4 |  96 | 0.00000 |\n" +
                "|   4 |  97 | 0.00000 |\n" +
                "|   4 |  98 | 0.00000 |\n" +
                "|   4 |  99 | 0.00000 |\n" +
                "|   4 | 100 | 0.00000 |\n" +
                "|   4 | 101 | 0.00000 |\n" +
                "|   4 | 102 | 0.00000 |\n" +
                "|   4 | 103 | 0.00000 |\n" +
                "|   4 | 104 | 0.00000 |\n" +
                "|   4 | 105 | 0.00000 |\n" +
                "|   4 | 106 | 0.00000 |\n" +
                "|   4 | 107 | 0.00000 |\n" +
                "|   4 | 108 | 0.00000 |\n" +
                "|   4 | 109 | 0.00000 |\n" +
                "|   4 | 110 | 0.00000 |\n" +
                "|   4 | 111 | 0.00000 |\n" +
                "|   4 | 112 | 0.00000 |\n" +
                "|   4 | 113 | 0.00000 |\n" +
                "|   4 | 114 | 0.00000 |\n" +
                "|   4 | 115 | 0.00000 |\n" +
                "|   4 | 116 | 0.00000 |\n" +
                "|   4 | 117 | 0.00000 |\n" +
                "|   4 | 118 | 0.00000 |\n" +
                "|   4 | 119 | 0.00000 |\n" +
                "|   4 | 120 | 0.00000 |\n" +
                "|   4 | 121 | 0.00000 |\n" +
                "|   4 | 122 | 0.00000 |\n" +
                "|   4 | 123 | 0.00000 |\n" +
                "|   4 | 124 | 0.00000 |\n" +
                "|   4 | 125 | 0.00000 |\n" +
                "|   4 | 126 | 0.00000 |\n" +
                "|   4 | 127 | 0.00000 |\n" +
                "|   4 | 128 | 0.00000 |\n" +
                "|   4 | 129 | 0.00000 |\n" +
                "|   4 | 130 | 0.00000 |\n" +
                "|   4 | 131 | 0.00000 |\n" +
                "|   4 | 132 | 0.00000 |\n" +
                "|   4 | 133 | 0.00000 |\n" +
                "|   4 | 134 | 0.00000 |\n" +
                "|   4 | 135 | 0.00000 |\n" +
                "|   4 | 136 | 0.00000 |\n" +
                "|   4 | 137 | 0.00000 |\n" +
                "|   4 | 138 | 0.00000 |\n" +
                "|   4 | 139 | 0.00000 |\n" +
                "|   4 | 140 | 0.00000 |\n" +
                "|   4 | 141 | 0.00000 |\n" +
                "|   4 | 142 | 0.00000 |\n" +
                "|   4 | 143 | 0.00000 |\n" +
                "|   4 | 144 | 0.00000 |\n" +
                "|   4 | 145 | 0.00000 |\n" +
                "|   4 | 146 | 0.00000 |\n" +
                "|   4 | 147 | 0.00000 |\n" +
                "|   5 |   1 | 0.00000 |\n" +
                "|   5 |   2 | 0.00000 |\n" +
                "|   5 |   3 | 0.00000 |\n" +
                "|   5 |   4 | 0.00000 |\n" +
                "|   5 |   5 | 0.00000 |\n" +
                "|   5 |   6 | 0.00000 |\n" +
                "|   5 |   7 | 0.00000 |\n" +
                "|   5 |   8 | 0.00000 |\n" +
                "|   5 |   9 | 0.00000 |\n" +
                "|   5 |  10 | 0.00000 |\n" +
                "|   5 |  11 | 0.00000 |\n" +
                "|   5 |  12 | 0.00000 |\n" +
                "|   5 |  13 | 0.00000 |\n" +
                "|   5 |  14 | 0.00000 |\n" +
                "|   5 |  15 | 0.00000 |\n" +
                "|   5 |  16 | 0.00000 |\n" +
                "|   5 |  17 | 0.00000 |\n" +
                "|   5 |  18 | 0.00000 |\n" +
                "|   5 |  19 | 0.00000 |\n" +
                "|   5 |  20 | 0.00000 |\n" +
                "|   5 |  21 | 0.00000 |\n" +
                "|   5 |  22 | 0.00000 |\n" +
                "|   5 |  23 | 0.00000 |\n" +
                "|   5 |  24 | 0.00000 |\n" +
                "|   5 |  25 | 0.00000 |\n" +
                "|   5 |  26 | 0.00000 |\n" +
                "|   5 |  27 | 0.00000 |\n" +
                "|   5 |  28 | 0.00000 |\n" +
                "|   5 |  29 | 0.00000 |\n" +
                "|   5 |  30 | 0.00000 |\n" +
                "|   5 |  31 | 0.00000 |\n" +
                "|   5 |  32 | 0.00000 |\n" +
                "|   5 |  33 | 0.00000 |\n" +
                "|   5 |  34 | 0.00000 |\n" +
                "|   5 |  35 | 0.00000 |\n" +
                "|   5 |  36 | 0.00000 |\n" +
                "|   5 |  37 | 0.00000 |\n" +
                "|   5 |  38 | 0.00000 |\n" +
                "|   5 |  39 | 0.00000 |\n" +
                "|   5 |  40 | 0.00000 |\n" +
                "|   5 |  41 | 0.00000 |\n" +
                "|   5 |  42 | 0.00000 |\n" +
                "|   5 |  43 | 0.00000 |\n" +
                "|   5 |  44 | 0.00000 |\n" +
                "|   5 |  45 | 0.00000 |\n" +
                "|   5 |  46 | 0.00000 |\n" +
                "|   5 |  47 | 0.00000 |\n" +
                "|   5 |  48 | 0.00000 |\n" +
                "|   5 |  49 | 0.00000 |\n" +
                "|   5 |  50 | 0.00000 |\n" +
                "|   5 |  51 | 0.00000 |\n" +
                "|   5 |  52 | 0.00000 |\n" +
                "|   5 |  53 | 0.00000 |\n" +
                "|   5 |  54 | 0.00000 |\n" +
                "|   5 |  55 | 0.00000 |\n" +
                "|   5 |  56 | 0.00000 |\n" +
                "|   5 |  57 | 0.00000 |\n" +
                "|   5 |  58 | 0.00000 |\n" +
                "|   5 |  59 | 0.00000 |\n" +
                "|   5 |  60 | 0.00000 |\n" +
                "|   5 |  61 | 0.00000 |\n" +
                "|   5 |  62 | 0.00000 |\n" +
                "|   5 |  63 | 0.00000 |\n" +
                "|   5 |  64 | 0.00000 |\n" +
                "|   5 |  65 | 0.00000 |\n" +
                "|   5 |  66 | 0.00000 |\n" +
                "|   5 |  67 | 0.00000 |\n" +
                "|   5 |  68 | 0.00000 |\n" +
                "|   5 |  69 | 0.00000 |\n" +
                "|   5 |  70 | 0.00000 |\n" +
                "|   5 |  71 | 0.00000 |\n" +
                "|   5 |  72 | 0.00000 |\n" +
                "|   5 |  73 | 0.00000 |\n" +
                "|   5 |  74 | 0.00000 |\n" +
                "|   5 |  75 | 0.00000 |\n" +
                "|   5 |  76 | 0.00000 |\n" +
                "|   5 |  77 | 0.00000 |\n" +
                "|   5 |  78 | 0.00000 |\n" +
                "|   5 |  79 | 0.00000 |\n" +
                "|   5 |  80 | 0.00000 |\n" +
                "|   5 |  81 | 0.00000 |\n" +
                "|   5 |  82 | 0.00000 |\n" +
                "|   5 |  83 | 0.00000 |\n" +
                "|   5 |  84 | 0.00000 |\n" +
                "|   5 |  85 | 0.00000 |\n" +
                "|   5 |  86 | 0.00000 |\n" +
                "|   5 |  87 | 0.00000 |\n" +
                "|   5 |  88 | 0.00000 |\n" +
                "|   5 |  89 | 0.00000 |\n" +
                "|   5 |  90 | 0.00000 |\n" +
                "|   5 |  91 | 0.00000 |\n" +
                "|   5 |  92 | 0.00000 |\n" +
                "|   5 |  93 | 0.00000 |\n" +
                "|   5 |  94 | 0.00000 |\n" +
                "|   5 |  95 | 0.00000 |\n" +
                "|   5 |  96 | 0.00000 |\n" +
                "|   5 |  97 | 0.00000 |\n" +
                "|   5 |  98 | 0.00000 |\n" +
                "|   5 |  99 | 0.00000 |\n" +
                "|   5 | 100 | 0.00000 |\n" +
                "|   5 | 101 | 0.00000 |\n" +
                "|   5 | 102 | 0.00000 |\n" +
                "|   5 | 103 | 0.00000 |\n" +
                "|   5 | 104 | 0.00000 |\n" +
                "|   5 | 105 | 0.00000 |\n" +
                "|   5 | 106 | 0.00000 |\n" +
                "|   5 | 107 | 0.00000 |\n" +
                "|   5 | 108 | 0.00000 |\n" +
                "|   5 | 109 | 0.00000 |\n" +
                "|   5 | 110 | 0.00000 |\n" +
                "|   5 | 111 | 0.00000 |\n" +
                "|   5 | 112 | 0.00000 |\n" +
                "|   5 | 113 | 0.00000 |\n" +
                "|   5 | 114 | 0.00000 |\n" +
                "|   5 | 115 | 0.00000 |\n" +
                "|   5 | 116 | 0.00000 |\n" +
                "|   5 | 117 | 0.00000 |\n" +
                "|   5 | 118 | 0.00000 |\n" +
                "|   5 | 119 | 0.00000 |\n" +
                "|   5 | 120 | 0.00000 |\n" +
                "|   5 | 121 | 0.00000 |\n" +
                "|   5 | 122 | 0.00000 |\n" +
                "|   5 | 123 | 0.00000 |\n" +
                "|   5 | 124 | 0.00000 |\n" +
                "|   5 | 125 | 0.00000 |\n" +
                "|   5 | 126 | 0.00000 |\n" +
                "|   5 | 127 | 0.00000 |\n" +
                "|   5 | 128 | 0.00000 |\n" +
                "|   5 | 129 | 0.00000 |\n" +
                "|   5 | 130 | 0.00000 |\n" +
                "|   5 | 131 | 0.00000 |\n" +
                "|   5 | 132 | 0.00000 |\n" +
                "|   5 | 133 | 0.00000 |\n" +
                "|   5 | 134 | 0.00000 |\n" +
                "|   5 | 135 | 0.00000 |\n" +
                "|   5 | 136 | 0.00000 |\n" +
                "|   5 | 137 | 0.00000 |\n" +
                "|   5 | 138 | 0.00000 |\n" +
                "|   5 | 139 | 0.00000 |\n" +
                "|   5 | 140 | 0.00000 |\n" +
                "|   5 | 141 | 0.00000 |\n" +
                "|   5 | 142 | 0.00000 |\n" +
                "|   5 | 143 | 0.00000 |\n" +
                "|   5 | 144 | 0.00000 |\n" +
                "|   5 | 145 | 0.00000 |\n" +
                "|   5 | 146 | 0.00000 |\n" +
                "|   5 | 147 | 0.00000 |\n" +
                "|   6 |   1 | 0.00000 |\n" +
                "|   6 |   2 | 0.00000 |\n" +
                "|   6 |   3 | 0.00000 |\n" +
                "|   6 |   4 | 0.00000 |\n" +
                "|   6 |   5 | 0.00000 |\n" +
                "|   6 |   6 | 0.00000 |\n" +
                "|   6 |   7 | 0.00000 |\n" +
                "|   6 |   8 | 0.00000 |\n" +
                "|   6 |   9 | 0.00000 |\n" +
                "|   6 |  10 | 0.00000 |\n" +
                "|   6 |  11 | 0.00000 |\n" +
                "|   6 |  12 | 0.00000 |\n" +
                "|   6 |  13 | 0.00000 |\n" +
                "|   6 |  14 | 0.00000 |\n" +
                "|   6 |  15 | 0.00000 |\n" +
                "|   6 |  16 | 0.00000 |\n" +
                "|   6 |  17 | 0.00000 |\n" +
                "|   6 |  18 | 0.00000 |\n" +
                "|   6 |  19 | 0.00000 |\n" +
                "|   6 |  20 | 0.00000 |\n" +
                "|   6 |  21 | 0.00000 |\n" +
                "|   6 |  22 | 0.00000 |\n" +
                "|   6 |  23 | 0.00000 |\n" +
                "|   6 |  24 | 0.00000 |\n" +
                "|   6 |  25 | 0.00000 |\n" +
                "|   6 |  26 | 0.00000 |\n" +
                "|   6 |  27 | 0.00000 |\n" +
                "|   6 |  28 | 0.00000 |\n" +
                "|   6 |  29 | 0.00000 |\n" +
                "|   6 |  30 | 0.00000 |\n" +
                "|   6 |  31 | 0.00000 |\n" +
                "|   6 |  32 | 0.00000 |\n" +
                "|   6 |  33 | 0.00000 |\n" +
                "|   6 |  34 | 0.00000 |\n" +
                "|   6 |  35 | 0.00000 |\n" +
                "|   6 |  36 | 0.00000 |\n" +
                "|   6 |  37 | 0.00000 |\n" +
                "|   6 |  38 | 0.00000 |\n" +
                "|   6 |  39 | 0.00000 |\n" +
                "|   6 |  40 | 0.00000 |\n" +
                "|   6 |  41 | 0.00000 |\n" +
                "|   6 |  42 | 0.00000 |\n" +
                "|   6 |  43 | 0.00000 |\n" +
                "|   6 |  44 | 0.00000 |\n" +
                "|   6 |  45 | 0.00000 |\n" +
                "|   6 |  46 | 0.00000 |\n" +
                "|   6 |  47 | 0.00000 |\n" +
                "|   6 |  48 | 0.00000 |\n" +
                "|   6 |  49 | 0.00000 |\n" +
                "|   6 |  50 | 0.00000 |\n" +
                "|   6 |  51 | 0.00000 |\n" +
                "|   6 |  52 | 0.00000 |\n" +
                "|   6 |  53 | 0.00000 |\n" +
                "|   6 |  54 | 0.00000 |\n" +
                "|   6 |  55 | 0.00000 |\n" +
                "|   6 |  56 | 0.00000 |\n" +
                "|   6 |  57 | 0.00000 |\n" +
                "|   6 |  58 | 0.00000 |\n" +
                "|   6 |  59 | 0.00000 |\n" +
                "|   6 |  60 | 0.00000 |\n" +
                "|   6 |  61 | 0.00000 |\n" +
                "|   6 |  62 | 0.00000 |\n" +
                "|   6 |  63 | 0.00000 |\n" +
                "|   6 |  64 | 0.00000 |\n" +
                "|   6 |  65 | 0.00000 |\n" +
                "|   6 |  66 | 0.00000 |\n" +
                "|   6 |  67 | 0.00000 |\n" +
                "|   6 |  68 | 0.00000 |\n" +
                "|   6 |  69 | 0.00000 |\n" +
                "|   6 |  70 | 0.00000 |\n" +
                "|   6 |  71 | 0.00000 |\n" +
                "|   6 |  72 | 0.00000 |\n" +
                "|   6 |  73 | 0.00000 |\n" +
                "|   6 |  74 | 0.00000 |\n" +
                "|   6 |  75 | 0.00000 |\n" +
                "|   6 |  76 | 0.00000 |\n" +
                "|   6 |  77 | 0.00000 |\n" +
                "|   6 |  78 | 0.00000 |\n" +
                "|   6 |  79 | 0.00000 |\n" +
                "|   6 |  80 | 0.00000 |\n" +
                "|   6 |  81 | 0.00000 |\n" +
                "|   6 |  82 | 0.00000 |\n" +
                "|   6 |  83 | 0.00000 |\n" +
                "|   6 |  84 | 0.00000 |\n" +
                "|   6 |  85 | 0.00000 |\n" +
                "|   6 |  86 | 0.00000 |\n" +
                "|   6 |  87 | 0.00000 |\n" +
                "|   6 |  88 | 0.00000 |\n" +
                "|   6 |  89 | 0.00000 |\n" +
                "|   6 |  90 | 0.00000 |\n" +
                "|   6 |  91 | 0.00000 |\n" +
                "|   6 |  92 | 0.00000 |\n" +
                "|   6 |  93 | 0.00000 |\n" +
                "|   6 |  94 | 0.00000 |\n" +
                "|   6 |  95 | 0.00000 |\n" +
                "|   6 |  96 | 0.00000 |\n" +
                "|   6 |  97 | 0.00000 |\n" +
                "|   6 |  98 | 0.00000 |\n" +
                "|   6 |  99 | 0.00000 |\n" +
                "|   6 | 100 | 0.00000 |\n" +
                "|   6 | 101 | 0.00000 |\n" +
                "|   6 | 102 | 0.00000 |\n" +
                "|   6 | 103 | 0.00000 |\n" +
                "|   6 | 104 | 0.00000 |\n" +
                "|   6 | 105 | 0.00000 |\n" +
                "|   6 | 106 | 0.00000 |\n" +
                "|   6 | 107 | 0.00000 |\n" +
                "|   6 | 108 | 0.00000 |\n" +
                "|   6 | 109 | 0.00000 |\n" +
                "|   6 | 110 | 0.00000 |\n" +
                "|   6 | 111 | 0.00000 |\n" +
                "|   6 | 112 | 0.00000 |\n" +
                "|   6 | 113 | 0.00000 |\n" +
                "|   6 | 114 | 0.00000 |\n" +
                "|   6 | 115 | 0.00000 |\n" +
                "|   6 | 116 | 0.00000 |\n" +
                "|   6 | 117 | 0.00000 |\n" +
                "|   6 | 118 | 0.00000 |\n" +
                "|   6 | 119 | 0.00000 |\n" +
                "|   6 | 120 | 0.00000 |\n" +
                "|   6 | 121 | 0.00000 |\n" +
                "|   6 | 122 | 0.00000 |\n" +
                "|   6 | 123 | 0.00000 |\n" +
                "|   6 | 124 | 0.00000 |\n" +
                "|   6 | 125 | 0.00000 |\n" +
                "|   6 | 126 | 0.00000 |\n" +
                "|   6 | 127 | 0.00000 |\n" +
                "|   6 | 128 | 0.00000 |\n" +
                "|   6 | 129 | 0.00000 |\n" +
                "|   6 | 130 | 0.00000 |\n" +
                "|   6 | 131 | 0.00000 |\n" +
                "|   6 | 132 | 0.00000 |\n" +
                "|   6 | 133 | 0.00000 |\n" +
                "|   6 | 134 | 0.00000 |\n" +
                "|   6 | 135 | 0.00000 |\n" +
                "|   6 | 136 | 0.00000 |\n" +
                "|   6 | 137 | 0.00000 |\n" +
                "|   6 | 138 | 0.00000 |\n" +
                "|   6 | 139 | 0.00000 |\n" +
                "|   6 | 140 | 0.00000 |\n" +
                "|   6 | 141 | 0.00000 |\n" +
                "|   6 | 142 | 0.00000 |\n" +
                "|   6 | 143 | 0.00000 |\n" +
                "|   6 | 144 | 0.00000 |\n" +
                "|   6 | 145 | 0.00000 |\n" +
                "|   6 | 146 | 0.00000 |\n" +
                "|   6 | 147 | 0.00000 |\n" +
                "|   7 |   1 | 0.00000 |\n" +
                "|   7 |   2 | 0.00000 |\n" +
                "|   7 |   3 | 0.00000 |\n" +
                "|   7 |   4 | 0.00000 |\n" +
                "|   7 |   5 | 0.00000 |\n" +
                "|   7 |   6 | 0.00000 |\n" +
                "|   7 |   7 | 0.00000 |\n" +
                "|   7 |   8 | 0.00000 |\n" +
                "|   7 |   9 | 0.00000 |\n" +
                "|   7 |  10 | 0.00000 |\n" +
                "|   7 |  11 | 0.00000 |\n" +
                "|   7 |  12 | 0.00000 |\n" +
                "|   7 |  13 | 0.00000 |\n" +
                "|   7 |  14 | 0.00000 |\n" +
                "|   7 |  15 | 0.00000 |\n" +
                "|   7 |  16 | 0.00000 |\n" +
                "|   7 |  17 | 0.00000 |\n" +
                "|   7 |  18 | 0.00000 |\n" +
                "|   7 |  19 | 0.00000 |\n" +
                "|   7 |  20 | 0.00000 |\n" +
                "|   7 |  21 | 0.00000 |\n" +
                "|   7 |  22 | 0.00000 |\n" +
                "|   7 |  23 | 0.00000 |\n" +
                "|   7 |  24 | 0.00000 |\n" +
                "|   7 |  25 | 0.00000 |\n" +
                "|   7 |  26 | 0.00000 |\n" +
                "|   7 |  27 | 0.00000 |\n" +
                "|   7 |  28 | 0.00000 |\n" +
                "|   7 |  29 | 0.00000 |\n" +
                "|   7 |  30 | 0.00000 |\n" +
                "|   7 |  31 | 0.00000 |\n" +
                "|   7 |  32 | 0.00000 |\n" +
                "|   7 |  33 | 0.00000 |\n" +
                "|   7 |  34 | 0.00000 |\n" +
                "|   7 |  35 | 0.00000 |\n" +
                "|   7 |  36 | 0.00000 |\n" +
                "|   7 |  37 | 0.00000 |\n" +
                "|   7 |  38 | 0.00000 |\n" +
                "|   7 |  39 | 0.00000 |\n" +
                "|   7 |  40 | 0.00000 |\n" +
                "|   7 |  41 | 0.00000 |\n" +
                "|   7 |  42 | 0.00000 |\n" +
                "|   7 |  43 | 0.00000 |\n" +
                "|   7 |  44 | 0.00000 |\n" +
                "|   7 |  45 | 0.00000 |\n" +
                "|   7 |  46 | 0.00000 |\n" +
                "|   7 |  47 | 0.00000 |\n" +
                "|   7 |  48 | 0.00000 |\n" +
                "|   7 |  49 | 0.00000 |\n" +
                "|   7 |  50 | 0.00000 |\n" +
                "|   7 |  51 | 0.00000 |\n" +
                "|   7 |  52 | 0.00000 |\n" +
                "|   7 |  53 | 0.00000 |\n" +
                "|   7 |  54 | 0.00000 |\n" +
                "|   7 |  55 | 0.00000 |\n" +
                "|   7 |  56 | 0.00000 |\n" +
                "|   7 |  57 | 0.00000 |\n" +
                "|   7 |  58 | 0.00000 |\n" +
                "|   7 |  59 | 0.00000 |\n" +
                "|   7 |  60 | 0.00000 |\n" +
                "|   7 |  61 | 0.00000 |\n" +
                "|   7 |  62 | 0.00000 |\n" +
                "|   7 |  63 | 0.00000 |\n" +
                "|   7 |  64 | 0.00000 |\n" +
                "|   7 |  65 | 0.00000 |\n" +
                "|   7 |  66 | 0.00000 |\n" +
                "|   7 |  67 | 0.00000 |\n" +
                "|   7 |  68 | 0.00000 |\n" +
                "|   7 |  69 | 0.00000 |\n" +
                "|   7 |  70 | 0.00000 |\n" +
                "|   7 |  71 | 0.00000 |\n" +
                "|   7 |  72 | 0.00000 |\n" +
                "|   7 |  73 | 0.00000 |\n" +
                "|   7 |  74 | 0.00000 |\n" +
                "|   7 |  75 | 0.00000 |\n" +
                "|   7 |  76 | 0.00000 |\n" +
                "|   7 |  77 | 0.00000 |\n" +
                "|   7 |  78 | 0.00000 |\n" +
                "|   7 |  79 | 0.00000 |\n" +
                "|   7 |  80 | 0.00000 |\n" +
                "|   7 |  81 | 0.00000 |\n" +
                "|   7 |  82 | 0.00000 |\n" +
                "|   7 |  83 | 0.00000 |\n" +
                "|   7 |  84 | 0.00000 |\n" +
                "|   7 |  85 | 0.00000 |\n" +
                "|   7 |  86 | 0.00000 |\n" +
                "|   7 |  87 | 0.00000 |\n" +
                "|   7 |  88 | 0.00000 |\n" +
                "|   7 |  89 | 0.00000 |\n" +
                "|   7 |  90 | 0.00000 |\n" +
                "|   7 |  91 | 0.00000 |\n" +
                "|   7 |  92 | 0.00000 |\n" +
                "|   7 |  93 | 0.00000 |\n" +
                "|   7 |  94 | 0.00000 |\n" +
                "|   7 |  95 | 0.00000 |\n" +
                "|   7 |  96 | 0.00000 |\n" +
                "|   7 |  97 | 0.00000 |\n" +
                "|   7 |  98 | 0.00000 |\n" +
                "|   7 |  99 | 0.00000 |\n" +
                "|   7 | 100 | 0.00000 |\n" +
                "|   7 | 101 | 0.00000 |\n" +
                "|   7 | 102 | 0.00000 |\n" +
                "|   7 | 103 | 0.00000 |\n" +
                "|   7 | 104 | 0.00000 |\n" +
                "|   7 | 105 | 0.00000 |\n" +
                "|   7 | 106 | 0.00000 |\n" +
                "|   7 | 107 | 0.00000 |\n" +
                "|   7 | 108 | 0.00000 |\n" +
                "|   7 | 109 | 0.00000 |\n" +
                "|   7 | 110 | 0.00000 |\n" +
                "|   7 | 111 | 0.00000 |\n" +
                "|   7 | 112 | 0.00000 |\n" +
                "|   7 | 113 | 0.00000 |\n" +
                "|   7 | 114 | 0.00000 |\n" +
                "|   7 | 115 | 0.00000 |\n" +
                "|   7 | 116 | 0.00000 |\n" +
                "|   7 | 117 | 0.00000 |\n" +
                "|   7 | 118 | 0.00000 |\n" +
                "|   7 | 119 | 0.00000 |\n" +
                "|   7 | 120 | 0.00000 |\n" +
                "|   7 | 121 | 0.00000 |\n" +
                "|   7 | 122 | 0.00000 |\n" +
                "|   7 | 123 | 0.00000 |\n" +
                "|   7 | 124 | 0.00000 |\n" +
                "|   7 | 125 | 0.00000 |\n" +
                "|   7 | 126 | 0.00000 |\n" +
                "|   7 | 127 | 0.00000 |\n" +
                "|   7 | 128 | 0.00000 |\n" +
                "|   7 | 129 | 0.00000 |\n" +
                "|   7 | 130 | 0.00000 |\n" +
                "|   7 | 131 | 0.00000 |\n" +
                "|   7 | 132 | 0.00000 |\n" +
                "|   7 | 133 | 0.00000 |\n" +
                "|   7 | 134 | 0.00000 |\n" +
                "|   7 | 135 | 0.00000 |\n" +
                "|   7 | 136 | 0.00000 |\n" +
                "|   7 | 137 | 0.00000 |\n" +
                "|   7 | 138 | 0.00000 |\n" +
                "|   7 | 139 | 0.00000 |\n" +
                "|   7 | 140 | 0.00000 |\n" +
                "|   7 | 141 | 0.00000 |\n" +
                "|   7 | 142 | 0.00000 |\n" +
                "|   7 | 143 | 0.00000 |\n" +
                "|   7 | 144 | 0.00000 |\n" +
                "|   7 | 145 | 0.00000 |\n" +
                "|   7 | 146 | 0.00000 |\n" +
                "|   7 | 147 | 0.00000 |\n" +
                "|   8 |   1 | 0.00000 |\n" +
                "|   8 |   2 | 0.00000 |\n" +
                "|   8 |   3 | 0.00000 |\n" +
                "|   8 |   4 | 0.00000 |\n" +
                "|   8 |   5 | 0.00000 |\n" +
                "|   8 |   6 | 0.00000 |\n" +
                "|   8 |   7 | 0.00000 |\n" +
                "|   8 |   8 | 0.00000 |\n" +
                "|   8 |   9 | 0.00000 |\n" +
                "|   8 |  10 | 0.00000 |\n" +
                "|   8 |  11 | 0.00000 |\n" +
                "|   8 |  12 | 0.00000 |\n" +
                "|   8 |  13 | 0.00000 |\n" +
                "|   8 |  14 | 0.00000 |\n" +
                "|   8 |  15 | 0.00000 |\n" +
                "|   8 |  16 | 0.00000 |\n" +
                "|   8 |  17 | 0.00000 |\n" +
                "|   8 |  18 | 0.00000 |\n" +
                "|   8 |  19 | 0.00000 |\n" +
                "|   8 |  20 | 0.00000 |\n" +
                "|   8 |  21 | 0.00000 |\n" +
                "|   8 |  22 | 0.00000 |\n" +
                "|   8 |  23 | 0.00000 |\n" +
                "|   8 |  24 | 0.00000 |\n" +
                "|   8 |  25 | 0.00000 |\n" +
                "|   8 |  26 | 0.00000 |\n" +
                "|   8 |  27 | 0.00000 |\n" +
                "|   8 |  28 | 0.00000 |\n" +
                "|   8 |  29 | 0.00000 |\n" +
                "|   8 |  30 | 0.00000 |\n" +
                "|   8 |  31 | 0.00000 |\n" +
                "|   8 |  32 | 0.00000 |\n" +
                "|   8 |  33 | 0.00000 |\n" +
                "|   8 |  34 | 0.00000 |\n" +
                "|   8 |  35 | 0.00000 |\n" +
                "|   8 |  36 | 0.00000 |\n" +
                "|   8 |  37 | 0.00000 |\n" +
                "|   8 |  38 | 0.00000 |\n" +
                "|   8 |  39 | 0.00000 |\n" +
                "|   8 |  40 | 0.00000 |\n" +
                "|   8 |  41 | 0.00000 |\n" +
                "|   8 |  42 | 0.00000 |\n" +
                "|   8 |  43 | 0.00000 |\n" +
                "|   8 |  44 | 0.00000 |\n" +
                "|   8 |  45 | 0.00000 |\n" +
                "|   8 |  46 | 0.00000 |\n" +
                "|   8 |  47 | 0.00000 |\n" +
                "|   8 |  48 | 0.00000 |\n" +
                "|   8 |  49 | 0.00000 |\n" +
                "|   8 |  50 | 0.00000 |\n" +
                "|   8 |  51 | 0.00000 |\n" +
                "|   8 |  52 | 0.00000 |\n" +
                "|   8 |  53 | 0.00000 |\n" +
                "|   8 |  54 | 0.00000 |\n" +
                "|   8 |  55 | 0.00000 |\n" +
                "|   8 |  56 | 0.00000 |\n" +
                "|   8 |  57 | 0.00000 |\n" +
                "|   8 |  58 | 0.00000 |\n" +
                "|   8 |  59 | 0.00000 |\n" +
                "|   8 |  60 | 0.00000 |\n" +
                "|   8 |  61 | 0.00000 |\n" +
                "|   8 |  62 | 0.00000 |\n" +
                "|   8 |  63 | 0.00000 |\n" +
                "|   8 |  64 | 0.00000 |\n" +
                "|   8 |  65 | 0.00000 |\n" +
                "|   8 |  66 | 0.00000 |\n" +
                "|   8 |  67 | 0.00000 |\n" +
                "|   8 |  68 | 0.00000 |\n" +
                "|   8 |  69 | 0.00000 |\n" +
                "|   8 |  70 | 0.00000 |\n" +
                "|   8 |  71 | 0.00000 |\n" +
                "|   8 |  72 | 0.00000 |\n" +
                "|   8 |  73 | 0.00000 |\n" +
                "|   8 |  74 | 0.00000 |\n" +
                "|   8 |  75 | 0.00000 |\n" +
                "|   8 |  76 | 0.00000 |\n" +
                "|   8 |  77 | 0.00000 |\n" +
                "|   8 |  78 | 0.00000 |\n" +
                "|   8 |  79 | 0.00000 |\n" +
                "|   8 |  80 | 0.00000 |\n" +
                "|   8 |  81 | 0.00000 |\n" +
                "|   8 |  82 | 0.00000 |\n" +
                "|   8 |  83 | 0.00000 |\n" +
                "|   8 |  84 | 0.00000 |\n" +
                "|   8 |  85 | 0.00000 |\n" +
                "|   8 |  86 | 0.00000 |\n" +
                "|   8 |  87 | 0.00000 |\n" +
                "|   8 |  88 | 0.00000 |\n" +
                "|   8 |  89 | 0.00000 |\n" +
                "|   8 |  90 | 0.00000 |\n" +
                "|   8 |  91 | 0.00000 |\n" +
                "|   8 |  92 | 0.00000 |\n" +
                "|   8 |  93 | 0.00000 |\n" +
                "|   8 |  94 | 0.00000 |\n" +
                "|   8 |  95 | 0.00000 |\n" +
                "|   8 |  96 | 0.00000 |\n" +
                "|   8 |  97 | 0.00000 |\n" +
                "|   8 |  98 | 0.00000 |\n" +
                "|   8 |  99 | 0.00000 |\n" +
                "|   8 | 100 | 0.00000 |\n" +
                "|   8 | 101 | 0.00000 |\n" +
                "|   8 | 102 | 0.00000 |\n" +
                "|   8 | 103 | 0.00000 |\n" +
                "|   8 | 104 | 0.00000 |\n" +
                "|   8 | 105 | 0.00000 |\n" +
                "|   8 | 106 | 0.00000 |\n" +
                "|   8 | 107 | 0.00000 |\n" +
                "|   8 | 108 | 0.00000 |\n" +
                "|   8 | 109 | 0.00000 |\n" +
                "|   8 | 110 | 0.00000 |\n" +
                "|   8 | 111 | 0.00000 |\n" +
                "|   8 | 112 | 0.00000 |\n" +
                "|   8 | 113 | 0.00000 |\n" +
                "|   8 | 114 | 0.00000 |\n" +
                "|   8 | 115 | 0.00000 |\n" +
                "|   8 | 116 | 0.00000 |\n" +
                "|   8 | 117 | 0.00000 |\n" +
                "|   8 | 118 | 0.00000 |\n" +
                "|   8 | 119 | 0.00000 |\n" +
                "|   8 | 120 | 0.00000 |\n" +
                "|   8 | 121 | 0.00000 |\n" +
                "|   8 | 122 | 0.00000 |\n" +
                "|   8 | 123 | 0.00000 |\n" +
                "|   8 | 124 | 0.00000 |\n" +
                "|   8 | 125 | 0.00000 |\n" +
                "|   8 | 126 | 0.00000 |\n" +
                "|   8 | 127 | 0.00000 |\n" +
                "|   8 | 128 | 0.00000 |\n" +
                "|   8 | 129 | 0.00000 |\n" +
                "|   8 | 130 | 0.00000 |\n" +
                "|   8 | 131 | 0.00000 |\n" +
                "|   8 | 132 | 0.00000 |\n" +
                "|   8 | 133 | 0.00000 |\n" +
                "|   8 | 134 | 0.00000 |\n" +
                "|   8 | 135 | 0.00000 |\n" +
                "|   8 | 136 | 0.00000 |\n" +
                "|   8 | 137 | 0.00000 |\n" +
                "|   8 | 138 | 0.00000 |\n" +
                "|   8 | 139 | 0.00000 |\n" +
                "|   8 | 140 | 0.00000 |\n" +
                "|   8 | 141 | 0.00000 |\n" +
                "|   8 | 142 | 0.00000 |\n" +
                "|   8 | 143 | 0.00000 |\n" +
                "|   8 | 144 | 0.00000 |\n" +
                "|   8 | 145 | 0.00000 |\n" +
                "|   8 | 146 | 0.00000 |\n" +
                "|   8 | 147 | 0.00000 |\n" +
                "|   9 |   1 | 0.00000 |\n" +
                "|   9 |   2 | 0.00000 |\n" +
                "|   9 |   3 | 0.00000 |\n" +
                "|   9 |   4 | 0.00000 |\n" +
                "|   9 |   5 | 0.00000 |\n" +
                "|   9 |   6 | 0.00000 |\n" +
                "|   9 |   7 | 0.00000 |\n" +
                "|   9 |   8 | 0.00000 |\n" +
                "|   9 |   9 | 0.00000 |\n" +
                "|   9 |  10 | 0.00000 |\n" +
                "|   9 |  11 | 0.00000 |\n" +
                "|   9 |  12 | 0.00000 |\n" +
                "|   9 |  13 | 0.00000 |\n" +
                "|   9 |  14 | 0.00000 |\n" +
                "|   9 |  15 | 0.00000 |\n" +
                "|   9 |  16 | 0.00000 |\n" +
                "|   9 |  17 | 0.00000 |\n" +
                "|   9 |  18 | 0.00000 |\n" +
                "|   9 |  19 | 0.00000 |\n" +
                "|   9 |  20 | 0.00000 |\n" +
                "|   9 |  21 | 0.00000 |\n" +
                "|   9 |  22 | 0.00000 |\n" +
                "|   9 |  23 | 0.00000 |\n" +
                "|   9 |  24 | 0.00000 |\n" +
                "|   9 |  25 | 0.00000 |\n" +
                "|   9 |  26 | 0.00000 |\n" +
                "|   9 |  27 | 0.00000 |\n" +
                "|   9 |  28 | 0.00000 |\n" +
                "|   9 |  29 | 0.00000 |\n" +
                "|   9 |  30 | 0.00000 |\n" +
                "|   9 |  31 | 0.00000 |\n" +
                "|   9 |  32 | 0.00000 |\n" +
                "|   9 |  33 | 0.00000 |\n" +
                "|   9 |  34 | 0.00000 |\n" +
                "|   9 |  35 | 0.00000 |\n" +
                "|   9 |  36 | 0.00000 |\n" +
                "|   9 |  37 | 0.00000 |\n" +
                "|   9 |  38 | 0.00000 |\n" +
                "|   9 |  39 | 0.00000 |\n" +
                "|   9 |  40 | 0.00000 |\n" +
                "|   9 |  41 | 0.00000 |\n" +
                "|   9 |  42 | 0.00000 |\n" +
                "|   9 |  43 | 0.00000 |\n" +
                "|   9 |  44 | 0.00000 |\n" +
                "|   9 |  45 | 0.00000 |\n" +
                "|   9 |  46 | 0.00000 |\n" +
                "|   9 |  47 | 0.00000 |\n" +
                "|   9 |  48 | 0.00000 |\n" +
                "|   9 |  49 | 0.00000 |\n" +
                "|   9 |  50 | 0.00000 |\n" +
                "|   9 |  51 | 0.00000 |\n" +
                "|   9 |  52 | 0.00000 |\n" +
                "|   9 |  53 | 0.00000 |\n" +
                "|   9 |  54 | 0.00000 |\n" +
                "|   9 |  55 | 0.00000 |\n" +
                "|   9 |  56 | 0.00000 |\n" +
                "|   9 |  57 | 0.00000 |\n" +
                "|   9 |  58 | 0.00000 |\n" +
                "|   9 |  59 | 0.00000 |\n" +
                "|   9 |  60 | 0.00000 |\n" +
                "|   9 |  61 | 0.00000 |\n" +
                "|   9 |  62 | 0.00000 |\n" +
                "|   9 |  63 | 0.00000 |\n" +
                "|   9 |  64 | 0.00000 |\n" +
                "|   9 |  65 | 0.00000 |\n" +
                "|   9 |  66 | 0.00000 |\n" +
                "|   9 |  67 | 0.00000 |\n" +
                "|   9 |  68 | 0.00000 |\n" +
                "|   9 |  69 | 0.00000 |\n" +
                "|   9 |  70 | 0.00000 |\n" +
                "|   9 |  71 | 0.00000 |\n" +
                "|   9 |  72 | 0.00000 |\n" +
                "|   9 |  73 | 0.00000 |\n" +
                "|   9 |  74 | 0.00000 |\n" +
                "|   9 |  75 | 0.00000 |\n" +
                "|   9 |  76 | 0.00000 |\n" +
                "|   9 |  77 | 0.00000 |\n" +
                "|   9 |  78 | 0.00000 |\n" +
                "|   9 |  79 | 0.00000 |\n" +
                "|   9 |  80 | 0.00000 |\n" +
                "|   9 |  81 | 0.00000 |\n" +
                "|   9 |  82 | 0.00000 |\n" +
                "|   9 |  83 | 0.00000 |\n" +
                "|   9 |  84 | 0.00000 |\n" +
                "|   9 |  85 | 0.00000 |\n" +
                "|   9 |  86 | 0.00000 |\n" +
                "|   9 |  87 | 0.00000 |\n" +
                "|   9 |  88 | 0.00000 |\n" +
                "|   9 |  89 | 0.00000 |\n" +
                "|   9 |  90 | 0.00000 |\n" +
                "|   9 |  91 | 0.00000 |\n" +
                "|   9 |  92 | 0.00000 |\n" +
                "|   9 |  93 | 0.00000 |\n" +
                "|   9 |  94 | 0.00000 |\n" +
                "|   9 |  95 | 0.00000 |\n" +
                "|   9 |  96 | 0.00000 |\n" +
                "|   9 |  97 | 0.00000 |\n" +
                "|   9 |  98 | 0.00000 |\n" +
                "|   9 |  99 | 0.00000 |\n" +
                "|   9 | 100 | 0.00000 |\n" +
                "|   9 | 101 | 0.00000 |\n" +
                "|   9 | 102 | 0.00000 |\n" +
                "|   9 | 103 | 0.00000 |\n" +
                "|   9 | 104 | 0.00000 |\n" +
                "|   9 | 105 | 0.00000 |\n" +
                "|   9 | 106 | 0.00000 |\n" +
                "|   9 | 107 | 0.00000 |\n" +
                "|   9 | 108 | 0.00000 |\n" +
                "|   9 | 109 | 0.00000 |\n" +
                "|   9 | 110 | 0.00000 |\n" +
                "|   9 | 111 | 0.00000 |\n" +
                "|   9 | 112 | 0.00000 |\n" +
                "|   9 | 113 | 0.00000 |\n" +
                "|   9 | 114 | 0.00000 |\n" +
                "|   9 | 115 | 0.00000 |\n" +
                "|   9 | 116 | 0.00000 |\n" +
                "|   9 | 117 | 0.00000 |\n" +
                "|   9 | 118 | 0.00000 |\n" +
                "|   9 | 119 | 0.00000 |\n" +
                "|   9 | 120 | 0.00000 |\n" +
                "|   9 | 121 | 0.00000 |\n" +
                "|   9 | 122 | 0.00000 |\n" +
                "|   9 | 123 | 0.00000 |\n" +
                "|   9 | 124 | 0.00000 |\n" +
                "|   9 | 125 | 0.00000 |\n" +
                "|   9 | 126 | 0.00000 |\n" +
                "|   9 | 127 | 0.00000 |\n" +
                "|   9 | 128 | 0.00000 |\n" +
                "|   9 | 129 | 0.00000 |\n" +
                "|   9 | 130 | 0.00000 |\n" +
                "|   9 | 131 | 0.00000 |\n" +
                "|   9 | 132 | 0.00000 |\n" +
                "|   9 | 133 | 0.00000 |\n" +
                "|   9 | 134 | 0.00000 |\n" +
                "|   9 | 135 | 0.00000 |\n" +
                "|   9 | 136 | 0.00000 |\n" +
                "|   9 | 137 | 0.00000 |\n" +
                "|   9 | 138 | 0.00000 |\n" +
                "|   9 | 139 | 0.00000 |\n" +
                "|   9 | 140 | 0.00000 |\n" +
                "|   9 | 141 | 0.00000 |\n" +
                "|   9 | 142 | 0.00000 |\n" +
                "|   9 | 143 | 0.00000 |\n" +
                "|   9 | 144 | 0.00000 |\n" +
                "|   9 | 145 | 0.00000 |\n" +
                "|   9 | 146 | 0.00000 |\n" +
                "|   9 | 147 | 0.00000 |\n" +
                "|  10 |   1 | 0.00000 |\n" +
                "|  10 |   2 | 0.00000 |\n" +
                "|  10 |   3 | 0.00000 |\n" +
                "|  10 |   4 | 0.00000 |\n" +
                "|  10 |   5 | 0.00000 |\n" +
                "|  10 |   6 | 0.00000 |\n" +
                "|  10 |   7 | 0.00000 |\n" +
                "|  10 |   8 | 0.00000 |\n" +
                "|  10 |   9 | 0.00000 |\n" +
                "|  10 |  10 | 0.00000 |\n" +
                "|  10 |  11 | 0.00000 |\n" +
                "|  10 |  12 | 0.00000 |\n" +
                "|  10 |  13 | 0.00000 |\n" +
                "|  10 |  14 | 0.00000 |\n" +
                "|  10 |  15 | 0.00000 |\n" +
                "|  10 |  16 | 0.00000 |\n" +
                "|  10 |  17 | 0.00000 |\n" +
                "|  10 |  18 | 0.00000 |\n" +
                "|  10 |  19 | 0.00000 |\n" +
                "|  10 |  20 | 0.00000 |\n" +
                "|  10 |  21 | 0.00000 |\n" +
                "|  10 |  22 | 0.00000 |\n" +
                "|  10 |  23 | 0.00000 |\n" +
                "|  10 |  24 | 0.00000 |\n" +
                "|  10 |  25 | 0.00000 |\n" +
                "|  10 |  26 | 0.00000 |\n" +
                "|  10 |  27 | 0.00000 |\n" +
                "|  10 |  28 | 0.00000 |\n" +
                "|  10 |  29 | 0.00000 |\n" +
                "|  10 |  30 | 0.00000 |\n" +
                "|  10 |  31 | 0.00000 |\n" +
                "|  10 |  32 | 0.00000 |\n" +
                "|  10 |  33 | 0.00000 |\n" +
                "|  10 |  34 | 0.00000 |\n" +
                "|  10 |  35 | 0.00000 |\n" +
                "|  10 |  36 | 0.00000 |\n" +
                "|  10 |  37 | 0.00000 |\n" +
                "|  10 |  38 | 0.00000 |\n" +
                "|  10 |  39 | 0.00000 |\n" +
                "|  10 |  40 | 0.00000 |\n" +
                "|  10 |  41 | 0.00000 |\n" +
                "|  10 |  42 | 0.00000 |\n" +
                "|  10 |  43 | 0.00000 |\n" +
                "|  10 |  44 | 0.00000 |\n" +
                "|  10 |  45 | 0.00000 |\n" +
                "|  10 |  46 | 0.00000 |\n" +
                "|  10 |  47 | 0.00000 |\n" +
                "|  10 |  48 | 0.00000 |\n" +
                "|  10 |  49 | 0.00000 |\n" +
                "|  10 |  50 | 0.00000 |\n" +
                "|  10 |  51 | 0.00000 |\n" +
                "|  10 |  52 | 0.00000 |\n" +
                "|  10 |  53 | 0.00000 |\n" +
                "|  10 |  54 | 0.00000 |\n" +
                "|  10 |  55 | 0.00000 |\n" +
                "|  10 |  56 | 0.00000 |\n" +
                "|  10 |  57 | 0.00000 |\n" +
                "|  10 |  58 | 0.00000 |\n" +
                "|  10 |  59 | 0.00000 |\n" +
                "|  10 |  60 | 0.00000 |\n" +
                "|  10 |  61 | 0.00000 |\n" +
                "|  10 |  62 | 0.00000 |\n" +
                "|  10 |  63 | 0.00000 |\n" +
                "|  10 |  64 | 0.00000 |\n" +
                "|  10 |  65 | 0.00000 |\n" +
                "|  10 |  66 | 0.00000 |\n" +
                "|  10 |  67 | 0.00000 |\n" +
                "|  10 |  68 | 0.00000 |\n" +
                "|  10 |  69 | 0.00000 |\n" +
                "|  10 |  70 | 0.00000 |\n" +
                "|  10 |  71 | 0.00000 |\n" +
                "|  10 |  72 | 0.00000 |\n" +
                "|  10 |  73 | 0.00000 |\n" +
                "|  10 |  74 | 0.00000 |\n" +
                "|  10 |  75 | 0.00000 |\n" +
                "|  10 |  76 | 0.00000 |\n" +
                "|  10 |  77 | 0.00000 |\n" +
                "|  10 |  78 | 0.00000 |\n" +
                "|  10 |  79 | 0.00000 |\n" +
                "|  10 |  80 | 0.00000 |\n" +
                "|  10 |  81 | 0.00000 |\n" +
                "|  10 |  82 | 0.00000 |\n" +
                "|  10 |  83 | 0.00000 |\n" +
                "|  10 |  84 | 0.00000 |\n" +
                "|  10 |  85 | 0.00000 |\n" +
                "|  10 |  86 | 0.00000 |\n" +
                "|  10 |  87 | 0.00000 |\n" +
                "|  10 |  88 | 0.00000 |\n" +
                "|  10 |  89 | 0.00000 |\n" +
                "|  10 |  90 | 0.00000 |\n" +
                "|  10 |  91 | 0.00000 |\n" +
                "|  10 |  92 | 0.00000 |\n" +
                "|  10 |  93 | 0.00000 |\n" +
                "|  10 |  94 | 0.00000 |\n" +
                "|  10 |  95 | 0.00000 |\n" +
                "|  10 |  96 | 0.00000 |\n" +
                "|  10 |  97 | 0.00000 |\n" +
                "|  10 |  98 | 0.00000 |\n" +
                "|  10 |  99 | 0.00000 |\n" +
                "|  10 | 100 | 0.00000 |\n" +
                "|  10 | 101 | 0.00000 |\n" +
                "|  10 | 102 | 0.00000 |\n" +
                "|  10 | 103 | 0.00000 |\n" +
                "|  10 | 104 | 0.00000 |\n" +
                "|  10 | 105 | 0.00000 |\n" +
                "|  10 | 106 | 0.00000 |\n" +
                "|  10 | 107 | 0.00000 |\n" +
                "|  10 | 108 | 0.00000 |\n" +
                "|  10 | 109 | 0.00000 |\n" +
                "|  10 | 110 | 0.00000 |\n" +
                "|  10 | 111 | 0.00000 |\n" +
                "|  10 | 112 | 0.00000 |\n" +
                "|  10 | 113 | 0.00000 |\n" +
                "|  10 | 114 | 0.00000 |\n" +
                "|  10 | 115 | 0.00000 |\n" +
                "|  10 | 116 | 0.00000 |\n" +
                "|  10 | 117 | 0.00000 |\n" +
                "|  10 | 118 | 0.00000 |\n" +
                "|  10 | 119 | 0.00000 |\n" +
                "|  10 | 120 | 0.00000 |\n" +
                "|  10 | 121 | 0.00000 |\n" +
                "|  10 | 122 | 0.00000 |\n" +
                "|  10 | 123 | 0.00000 |\n" +
                "|  10 | 124 | 0.00000 |\n" +
                "|  10 | 125 | 0.00000 |\n" +
                "|  10 | 126 | 0.00000 |\n" +
                "|  10 | 127 | 0.00000 |\n" +
                "|  10 | 128 | 0.00000 |\n" +
                "|  10 | 129 | 0.00000 |\n" +
                "|  10 | 130 | 0.00000 |\n" +
                "|  10 | 131 | 0.00000 |\n" +
                "|  10 | 132 | 0.00000 |\n" +
                "|  10 | 133 | 0.00000 |\n" +
                "|  10 | 134 | 0.00000 |\n" +
                "|  10 | 135 | 0.00000 |\n" +
                "|  10 | 136 | 0.00000 |\n" +
                "|  10 | 137 | 0.00000 |\n" +
                "|  10 | 138 | 0.00000 |\n" +
                "|  10 | 139 | 0.00000 |\n" +
                "|  10 | 140 | 0.00000 |\n" +
                "|  10 | 141 | 0.00000 |\n" +
                "|  10 | 142 | 0.00000 |\n" +
                "|  10 | 143 | 0.00000 |\n" +
                "|  10 | 144 | 0.00000 |\n" +
                "|  10 | 145 | 0.00000 |\n" +
                "|  10 | 146 | 0.00000 |\n" +
                "|  10 | 147 | 0.00000 |\n" +
                "|  11 |   1 | 0.00000 |\n" +
                "|  11 |   2 | 0.00000 |\n" +
                "|  11 |   3 | 0.00000 |\n" +
                "|  11 |   4 | 0.00000 |\n" +
                "|  11 |   5 | 0.00000 |\n" +
                "|  11 |   6 | 0.00000 |\n" +
                "|  11 |   7 | 0.00000 |\n" +
                "|  11 |   8 | 0.00000 |\n" +
                "|  11 |   9 | 0.00000 |\n" +
                "|  11 |  10 | 0.00000 |\n" +
                "|  11 |  11 | 0.00000 |\n" +
                "|  11 |  12 | 0.00000 |\n" +
                "|  11 |  13 | 0.00000 |\n" +
                "|  11 |  14 | 0.00000 |\n" +
                "|  11 |  15 | 0.00000 |\n" +
                "|  11 |  16 | 0.00000 |\n" +
                "|  11 |  17 | 0.00000 |\n" +
                "|  11 |  18 | 0.00000 |\n" +
                "|  11 |  19 | 0.00000 |\n" +
                "|  11 |  20 | 0.00000 |\n" +
                "|  11 |  21 | 0.00000 |\n" +
                "|  11 |  22 | 0.00000 |\n" +
                "|  11 |  23 | 0.00000 |\n" +
                "|  11 |  24 | 0.00000 |\n" +
                "|  11 |  25 | 0.00000 |\n" +
                "|  11 |  26 | 0.00000 |\n" +
                "|  11 |  27 | 0.00000 |\n" +
                "|  11 |  28 | 0.00000 |\n" +
                "|  11 |  29 | 0.00000 |\n" +
                "|  11 |  30 | 0.00000 |\n" +
                "|  11 |  31 | 0.00000 |\n" +
                "|  11 |  32 | 0.00000 |\n" +
                "|  11 |  33 | 0.00000 |\n" +
                "|  11 |  34 | 0.00000 |\n" +
                "|  11 |  35 | 0.00000 |\n" +
                "|  11 |  36 | 0.00000 |\n" +
                "|  11 |  37 | 0.00000 |\n" +
                "|  11 |  38 | 0.00000 |\n" +
                "|  11 |  39 | 0.00000 |\n" +
                "|  11 |  40 | 0.00000 |\n" +
                "|  11 |  41 | 0.00000 |\n" +
                "|  11 |  42 | 0.00000 |\n" +
                "|  11 |  43 | 0.00000 |\n" +
                "|  11 |  44 | 0.00000 |\n" +
                "|  11 |  45 | 0.00000 |\n" +
                "|  11 |  46 | 0.00000 |\n" +
                "|  11 |  47 | 0.00000 |\n" +
                "|  11 |  48 | 0.00000 |\n" +
                "|  11 |  49 | 0.00000 |\n" +
                "|  11 |  50 | 0.00000 |\n" +
                "|  11 |  51 | 0.00000 |\n" +
                "|  11 |  52 | 0.00000 |\n" +
                "|  11 |  53 | 0.00000 |\n" +
                "|  11 |  54 | 0.00000 |\n" +
                "|  11 |  55 | 0.00000 |\n" +
                "|  11 |  56 | 0.00000 |\n" +
                "|  11 |  57 | 0.00000 |\n" +
                "|  11 |  58 | 0.00000 |\n" +
                "|  11 |  59 | 0.00000 |\n" +
                "|  11 |  60 | 0.00000 |\n" +
                "|  11 |  61 | 0.00000 |\n" +
                "|  11 |  62 | 0.00000 |\n" +
                "|  11 |  63 | 0.00000 |\n" +
                "|  11 |  64 | 0.00000 |\n" +
                "|  11 |  65 | 0.00000 |\n" +
                "|  11 |  66 | 0.00000 |\n" +
                "|  11 |  67 | 0.00000 |\n" +
                "|  11 |  68 | 0.00000 |\n" +
                "|  11 |  69 | 0.00000 |\n" +
                "|  11 |  70 | 0.00000 |\n" +
                "|  11 |  71 | 0.00000 |\n" +
                "|  11 |  72 | 0.00000 |\n" +
                "|  11 |  73 | 0.00000 |\n" +
                "|  11 |  74 | 0.00000 |\n" +
                "|  11 |  75 | 0.00000 |\n" +
                "|  11 |  76 | 0.00000 |\n" +
                "|  11 |  77 | 0.00000 |\n" +
                "|  11 |  78 | 0.00000 |\n" +
                "|  11 |  79 | 0.00000 |\n" +
                "|  11 |  80 | 0.00000 |\n" +
                "|  11 |  81 | 0.00000 |\n" +
                "|  11 |  82 | 0.00000 |\n" +
                "|  11 |  83 | 0.00000 |\n" +
                "|  11 |  84 | 0.00000 |\n" +
                "|  11 |  85 | 0.00000 |\n" +
                "|  11 |  86 | 0.00000 |\n" +
                "|  11 |  87 | 0.00000 |\n" +
                "|  11 |  88 | 0.00000 |\n" +
                "|  11 |  89 | 0.00000 |\n" +
                "|  11 |  90 | 0.00000 |\n" +
                "|  11 |  91 | 0.00000 |\n" +
                "|  11 |  92 | 0.00000 |\n" +
                "|  11 |  93 | 0.00000 |\n" +
                "|  11 |  94 | 0.00000 |\n" +
                "|  11 |  95 | 0.00000 |\n" +
                "|  11 |  96 | 0.00000 |\n" +
                "|  11 |  97 | 0.00000 |\n" +
                "|  11 |  98 | 0.00000 |\n" +
                "|  11 |  99 | 0.00000 |\n" +
                "|  11 | 100 | 0.00000 |\n" +
                "|  11 | 101 | 0.00000 |\n" +
                "|  11 | 102 | 0.00000 |\n" +
                "|  11 | 103 | 0.00000 |\n" +
                "|  11 | 104 | 0.00000 |\n" +
                "|  11 | 105 | 0.00000 |\n" +
                "|  11 | 106 | 0.00000 |\n" +
                "|  11 | 107 | 0.00000 |\n" +
                "|  11 | 108 | 0.00000 |\n" +
                "|  11 | 109 | 0.00000 |\n" +
                "|  11 | 110 | 0.00000 |\n" +
                "|  11 | 111 | 0.00000 |\n" +
                "|  11 | 112 | 0.00000 |\n" +
                "|  11 | 113 | 0.00000 |\n" +
                "|  11 | 114 | 0.00000 |\n" +
                "|  11 | 115 | 0.00000 |\n" +
                "|  11 | 116 | 0.00000 |\n" +
                "|  11 | 117 | 0.00000 |\n" +
                "|  11 | 118 | 0.00000 |\n" +
                "|  11 | 119 | 0.00000 |\n" +
                "|  11 | 120 | 0.00000 |\n" +
                "|  11 | 121 | 0.00000 |\n" +
                "|  11 | 122 | 0.00000 |\n" +
                "|  11 | 123 | 0.00000 |\n" +
                "|  11 | 124 | 0.00000 |\n" +
                "|  11 | 125 | 0.00000 |\n" +
                "|  11 | 126 | 0.00000 |\n" +
                "|  11 | 127 | 0.00000 |\n" +
                "|  11 | 128 | 0.00000 |\n" +
                "|  11 | 129 | 0.00000 |\n" +
                "|  11 | 130 | 0.00000 |\n" +
                "|  11 | 131 | 0.00000 |\n" +
                "|  11 | 132 | 0.00000 |\n" +
                "|  11 | 133 | 0.00000 |\n" +
                "|  11 | 134 | 0.00000 |\n" +
                "|  11 | 135 | 0.00000 |\n" +
                "|  11 | 136 | 0.00000 |\n" +
                "|  11 | 137 | 0.00000 |\n" +
                "|  11 | 138 | 0.00000 |\n" +
                "|  11 | 139 | 0.00000 |\n" +
                "|  11 | 140 | 0.00000 |\n" +
                "|  11 | 141 | 0.00000 |\n" +
                "|  11 | 142 | 0.00000 |\n" +
                "|  11 | 143 | 0.00000 |\n" +
                "|  11 | 144 | 0.00000 |\n" +
                "|  11 | 145 | 0.00000 |\n" +
                "|  11 | 146 | 0.00000 |\n" +
                "|  11 | 147 | 0.00000 |\n" +
                "|  12 |   1 | 0.00000 |\n" +
                "|  12 |   2 | 0.00000 |\n" +
                "|  12 |   3 | 0.00000 |\n" +
                "|  12 |   4 | 0.00000 |\n" +
                "|  12 |   5 | 0.00000 |\n" +
                "|  12 |   6 | 0.00000 |\n" +
                "|  12 |   7 | 0.00000 |\n" +
                "|  12 |   8 | 0.00000 |\n" +
                "|  12 |   9 | 0.00000 |\n" +
                "|  12 |  10 | 0.00000 |\n" +
                "|  12 |  11 | 0.00000 |\n" +
                "|  12 |  12 | 0.00000 |\n" +
                "|  12 |  13 | 0.00000 |\n" +
                "|  12 |  14 | 0.00000 |\n" +
                "|  12 |  15 | 0.00000 |\n" +
                "|  12 |  16 | 0.00000 |\n" +
                "|  12 |  17 | 0.00000 |\n" +
                "|  12 |  18 | 0.00000 |\n" +
                "|  12 |  19 | 0.00000 |\n" +
                "|  12 |  20 | 0.00000 |\n" +
                "|  12 |  21 | 0.00000 |\n" +
                "|  12 |  22 | 0.00000 |\n" +
                "|  12 |  23 | 0.00000 |\n" +
                "|  12 |  24 | 0.00000 |\n" +
                "|  12 |  25 | 0.00000 |\n" +
                "|  12 |  26 | 0.00000 |\n" +
                "|  12 |  27 | 0.00000 |\n" +
                "|  12 |  28 | 0.00000 |\n" +
                "|  12 |  29 | 0.00000 |\n" +
                "|  12 |  30 | 0.00000 |\n" +
                "|  12 |  31 | 0.00000 |\n" +
                "|  12 |  32 | 0.00000 |\n" +
                "|  12 |  33 | 0.00000 |\n" +
                "|  12 |  34 | 0.00000 |\n" +
                "|  12 |  35 | 0.00000 |\n" +
                "|  12 |  36 | 0.00000 |\n" +
                "|  12 |  37 | 0.00000 |\n" +
                "|  12 |  38 | 0.00000 |\n" +
                "|  12 |  39 | 0.00000 |\n" +
                "|  12 |  40 | 0.00000 |\n" +
                "|  12 |  41 | 0.00000 |\n" +
                "|  12 |  42 | 0.00000 |\n" +
                "|  12 |  43 | 0.00000 |\n" +
                "|  12 |  44 | 0.00000 |\n" +
                "|  12 |  45 | 0.00000 |\n" +
                "|  12 |  46 | 0.00000 |\n" +
                "|  12 |  47 | 0.00000 |\n" +
                "|  12 |  48 | 0.00000 |\n" +
                "|  12 |  49 | 0.00000 |\n" +
                "|  12 |  50 | 0.00000 |\n" +
                "|  12 |  51 | 0.00000 |\n" +
                "|  12 |  52 | 0.00000 |\n" +
                "|  12 |  53 | 0.00000 |\n" +
                "|  12 |  54 | 0.00000 |\n" +
                "|  12 |  55 | 0.00000 |\n" +
                "|  12 |  56 | 0.00000 |\n" +
                "|  12 |  57 | 0.00000 |\n" +
                "|  12 |  58 | 0.00000 |\n" +
                "|  12 |  59 | 0.00000 |\n" +
                "|  12 |  60 | 0.00000 |\n" +
                "|  12 |  61 | 0.00000 |\n" +
                "|  12 |  62 | 0.00000 |\n" +
                "|  12 |  63 | 0.00000 |\n" +
                "|  12 |  64 | 0.00000 |\n" +
                "|  12 |  65 | 0.00000 |\n" +
                "|  12 |  66 | 0.00000 |\n" +
                "|  12 |  67 | 0.00000 |\n" +
                "|  12 |  68 | 0.00000 |\n" +
                "|  12 |  69 | 0.00000 |\n" +
                "|  12 |  70 | 0.00000 |\n" +
                "|  12 |  71 | 0.00000 |\n" +
                "|  12 |  72 | 0.00000 |\n" +
                "|  12 |  73 | 0.00000 |\n" +
                "|  12 |  74 | 0.00000 |\n" +
                "|  12 |  75 | 0.00000 |\n" +
                "|  12 |  76 | 0.00000 |\n" +
                "|  12 |  77 | 0.00000 |\n" +
                "|  12 |  78 | 0.00000 |\n" +
                "|  12 |  79 | 0.00000 |\n" +
                "|  12 |  80 | 0.00000 |\n" +
                "|  12 |  81 | 0.00000 |\n" +
                "|  12 |  82 | 0.00000 |\n" +
                "|  12 |  83 | 0.00000 |\n" +
                "|  12 |  84 | 0.00000 |\n" +
                "|  12 |  85 | 0.00000 |\n" +
                "|  12 |  86 | 0.00000 |\n" +
                "|  12 |  87 | 0.00000 |\n" +
                "|  12 |  88 | 0.00000 |\n" +
                "|  12 |  89 | 0.00000 |\n" +
                "|  12 |  90 | 0.00000 |\n" +
                "|  12 |  91 | 0.00000 |\n" +
                "|  12 |  92 | 0.00000 |\n" +
                "|  12 |  93 | 0.00000 |\n" +
                "|  12 |  94 | 0.00000 |\n" +
                "|  12 |  95 | 0.00000 |\n" +
                "|  12 |  96 | 0.00000 |\n" +
                "|  12 |  97 | 0.00000 |\n" +
                "|  12 |  98 | 0.00000 |\n" +
                "|  12 |  99 | 0.00000 |\n" +
                "|  12 | 100 | 0.00000 |\n" +
                "|  12 | 101 | 0.00000 |\n" +
                "|  12 | 102 | 0.00000 |\n" +
                "|  12 | 103 | 0.00000 |\n" +
                "|  12 | 104 | 0.00000 |\n" +
                "|  12 | 105 | 0.00000 |\n" +
                "|  12 | 106 | 0.00000 |\n" +
                "|  12 | 107 | 0.00000 |\n" +
                "|  12 | 108 | 0.00000 |\n" +
                "|  12 | 109 | 0.00000 |\n" +
                "|  12 | 110 | 0.00000 |\n" +
                "|  12 | 111 | 0.00000 |\n" +
                "|  12 | 112 | 0.00000 |\n" +
                "|  12 | 113 | 0.00000 |\n" +
                "|  12 | 114 | 0.00000 |\n" +
                "|  12 | 115 | 0.00000 |\n" +
                "|  12 | 116 | 0.00000 |\n" +
                "|  12 | 117 | 0.00000 |\n" +
                "|  12 | 118 | 0.00000 |\n" +
                "|  12 | 119 | 0.00000 |\n" +
                "|  12 | 120 | 0.00000 |\n" +
                "|  12 | 121 | 0.00000 |\n" +
                "|  12 | 122 | 0.00000 |\n" +
                "|  12 | 123 | 0.00000 |\n" +
                "|  12 | 124 | 0.00000 |\n" +
                "|  12 | 125 | 0.00000 |\n" +
                "|  12 | 126 | 0.00000 |\n" +
                "|  12 | 127 | 0.00000 |\n" +
                "|  12 | 128 | 0.00000 |\n" +
                "|  12 | 129 | 0.00000 |\n" +
                "|  12 | 130 | 0.00000 |\n" +
                "|  12 | 131 | 0.00000 |\n" +
                "|  12 | 132 | 0.00000 |\n" +
                "|  12 | 133 | 0.00000 |\n" +
                "|  12 | 134 | 0.00000 |\n" +
                "|  12 | 135 | 0.00000 |\n" +
                "|  12 | 136 | 0.00000 |\n" +
                "|  12 | 137 | 0.00000 |\n" +
                "|  12 | 138 | 0.00000 |\n" +
                "|  12 | 139 | 0.00000 |\n" +
                "|  12 | 140 | 0.00000 |\n" +
                "|  12 | 141 | 0.00000 |\n" +
                "|  12 | 142 | 0.00000 |\n" +
                "|  12 | 143 | 0.00000 |\n" +
                "|  12 | 144 | 0.00000 |\n" +
                "|  12 | 145 | 0.00000 |\n" +
                "|  12 | 146 | 0.00000 |\n" +
                "|  12 | 147 | 0.00000 |\n" +
                "|  13 |   1 | 0.00000 |\n" +
                "|  13 |   2 | 0.00000 |\n" +
                "|  13 |   3 | 0.00000 |\n" +
                "|  13 |   4 | 0.00000 |\n" +
                "|  13 |   5 | 0.00000 |\n" +
                "|  13 |   6 | 0.00000 |\n" +
                "|  13 |   7 | 0.00000 |\n" +
                "|  13 |   8 | 0.00000 |\n" +
                "|  13 |   9 | 0.00000 |\n" +
                "|  13 |  10 | 0.00000 |\n" +
                "|  13 |  11 | 0.00000 |\n" +
                "|  13 |  12 | 0.00000 |\n" +
                "|  13 |  13 | 0.00000 |\n" +
                "|  13 |  14 | 0.00000 |\n" +
                "|  13 |  15 | 0.00000 |\n" +
                "|  13 |  16 | 0.00000 |\n" +
                "|  13 |  17 | 0.00000 |\n" +
                "|  13 |  18 | 0.00000 |\n" +
                "|  13 |  19 | 0.00000 |\n" +
                "|  13 |  20 | 0.00000 |\n" +
                "|  13 |  21 | 0.00000 |\n" +
                "|  13 |  22 | 0.00000 |\n" +
                "|  13 |  23 | 0.00000 |\n" +
                "|  13 |  24 | 0.00000 |\n" +
                "|  13 |  25 | 0.00000 |\n" +
                "|  13 |  26 | 0.00000 |\n" +
                "|  13 |  27 | 0.00000 |\n" +
                "|  13 |  28 | 0.00000 |\n" +
                "|  13 |  29 | 0.00000 |\n" +
                "|  13 |  30 | 0.00000 |\n" +
                "|  13 |  31 | 0.00000 |\n" +
                "|  13 |  32 | 0.00000 |\n" +
                "|  13 |  33 | 0.00000 |\n" +
                "|  13 |  34 | 0.00000 |\n" +
                "|  13 |  35 | 0.00000 |\n" +
                "|  13 |  36 | 0.00000 |\n" +
                "|  13 |  37 | 0.00000 |\n" +
                "|  13 |  38 | 0.00000 |\n" +
                "|  13 |  39 | 0.00000 |\n" +
                "|  13 |  40 | 0.00000 |\n" +
                "|  13 |  41 | 0.00000 |\n" +
                "|  13 |  42 | 0.00000 |\n" +
                "|  13 |  43 | 0.00000 |\n" +
                "|  13 |  44 | 0.00000 |\n" +
                "|  13 |  45 | 0.00000 |\n" +
                "|  13 |  46 | 0.00000 |\n" +
                "|  13 |  47 | 0.00000 |\n" +
                "|  13 |  48 | 0.00000 |\n" +
                "|  13 |  49 | 0.00000 |\n" +
                "|  13 |  50 | 0.00000 |\n" +
                "|  13 |  51 | 0.00000 |\n" +
                "|  13 |  52 | 0.00000 |\n" +
                "|  13 |  53 | 0.00000 |\n" +
                "|  13 |  54 | 0.00000 |\n" +
                "|  13 |  55 | 0.00000 |\n" +
                "|  13 |  56 | 0.00000 |\n" +
                "|  13 |  57 | 0.00000 |\n" +
                "|  13 |  58 | 0.00000 |\n" +
                "|  13 |  59 | 0.00000 |\n" +
                "|  13 |  60 | 0.00000 |\n" +
                "|  13 |  61 | 0.00000 |\n" +
                "|  13 |  62 | 0.00000 |\n" +
                "|  13 |  63 | 0.00000 |\n" +
                "|  13 |  64 | 0.00000 |\n" +
                "|  13 |  65 | 0.00000 |\n" +
                "|  13 |  66 | 0.00000 |\n" +
                "|  13 |  67 | 0.00000 |\n" +
                "|  13 |  68 | 0.00000 |\n" +
                "|  13 |  69 | 0.00000 |\n" +
                "|  13 |  70 | 0.00000 |\n" +
                "|  13 |  71 | 0.00000 |\n" +
                "|  13 |  72 | 0.00000 |\n" +
                "|  13 |  73 | 0.00000 |\n" +
                "|  13 |  74 | 0.00000 |\n" +
                "|  13 |  75 | 0.00000 |\n" +
                "|  13 |  76 | 0.00000 |\n" +
                "|  13 |  77 | 0.00000 |\n" +
                "|  13 |  78 | 0.00000 |\n" +
                "|  13 |  79 | 0.00000 |\n" +
                "|  13 |  80 | 0.00000 |\n" +
                "|  13 |  81 | 0.00000 |\n" +
                "|  13 |  82 | 0.00000 |\n" +
                "|  13 |  83 | 0.00000 |\n" +
                "|  13 |  84 | 0.00000 |\n" +
                "|  13 |  85 | 0.00000 |\n" +
                "|  13 |  86 | 0.00000 |\n" +
                "|  13 |  87 | 0.00000 |\n" +
                "|  13 |  88 | 0.00000 |\n" +
                "|  13 |  89 | 0.00000 |\n" +
                "|  13 |  90 | 0.00000 |\n" +
                "|  13 |  91 | 0.00000 |\n" +
                "|  13 |  92 | 0.00000 |\n" +
                "|  13 |  93 | 0.00000 |\n" +
                "|  13 |  94 | 0.00000 |\n" +
                "|  13 |  95 | 0.00000 |\n" +
                "|  13 |  96 | 0.00000 |\n" +
                "|  13 |  97 | 0.00000 |\n" +
                "|  13 |  98 | 0.00000 |\n" +
                "|  13 |  99 | 0.00000 |\n" +
                "|  13 | 100 | 0.00000 |\n" +
                "|  13 | 101 | 0.00000 |\n" +
                "|  13 | 102 | 0.00000 |\n" +
                "|  13 | 103 | 0.00000 |\n" +
                "|  13 | 104 | 0.00000 |\n" +
                "|  13 | 105 | 0.00000 |\n" +
                "|  13 | 106 | 0.00000 |\n" +
                "|  13 | 107 | 0.00000 |\n" +
                "|  13 | 108 | 0.00000 |\n" +
                "|  13 | 109 | 0.00000 |\n" +
                "|  13 | 110 | 0.00000 |\n" +
                "|  13 | 111 | 0.00000 |\n" +
                "|  13 | 112 | 0.00000 |\n" +
                "|  13 | 113 | 0.00000 |\n" +
                "|  13 | 114 | 0.00000 |\n" +
                "|  13 | 115 | 0.00000 |\n" +
                "|  13 | 116 | 0.00000 |\n" +
                "|  13 | 117 | 0.00000 |\n" +
                "|  13 | 118 | 0.00000 |\n" +
                "|  13 | 119 | 0.00000 |\n" +
                "|  13 | 120 | 0.00000 |\n" +
                "|  13 | 121 | 0.00000 |\n" +
                "|  13 | 122 | 0.00000 |\n" +
                "|  13 | 123 | 0.00000 |\n" +
                "|  13 | 124 | 0.00000 |\n" +
                "|  13 | 125 | 0.00000 |\n" +
                "|  13 | 126 | 0.00000 |\n" +
                "|  13 | 127 | 0.00000 |\n" +
                "|  13 | 128 | 0.00000 |\n" +
                "|  13 | 129 | 0.00000 |\n" +
                "|  13 | 130 | 0.00000 |\n" +
                "|  13 | 131 | 0.00000 |\n" +
                "|  13 | 132 | 0.00000 |\n" +
                "|  13 | 133 | 0.00000 |\n" +
                "|  13 | 134 | 0.00000 |\n" +
                "|  13 | 135 | 0.00000 |\n" +
                "|  13 | 136 | 0.00000 |\n" +
                "|  13 | 137 | 0.00000 |\n" +
                "|  13 | 138 | 0.00000 |\n" +
                "|  13 | 139 | 0.00000 |\n" +
                "|  13 | 140 | 0.00000 |\n" +
                "|  13 | 141 | 0.00000 |\n" +
                "|  13 | 142 | 0.00000 |\n" +
                "|  13 | 143 | 0.00000 |\n" +
                "|  13 | 144 | 0.00000 |\n" +
                "|  13 | 145 | 0.00000 |\n" +
                "|  13 | 146 | 0.00000 |\n" +
                "|  13 | 147 | 0.00000 |\n" +
                "|  14 |   1 | 0.00000 |\n" +
                "|  14 |   2 | 0.00000 |\n" +
                "|  14 |   3 | 0.00000 |\n" +
                "|  14 |   4 | 0.00000 |\n" +
                "|  14 |   5 | 0.00000 |\n" +
                "|  14 |   6 | 0.00000 |\n" +
                "|  14 |   7 | 0.00000 |\n" +
                "|  14 |   8 | 0.00000 |\n" +
                "|  14 |   9 | 0.00000 |\n" +
                "|  14 |  10 | 0.00000 |\n" +
                "|  14 |  11 | 0.00000 |\n" +
                "|  14 |  12 | 0.00000 |\n" +
                "|  14 |  13 | 0.00000 |\n" +
                "|  14 |  14 | 0.00000 |\n" +
                "|  14 |  15 | 0.00000 |\n" +
                "|  14 |  16 | 0.00000 |\n" +
                "|  14 |  17 | 0.00000 |\n" +
                "|  14 |  18 | 0.00000 |\n" +
                "|  14 |  19 | 0.00000 |\n" +
                "|  14 |  20 | 0.00000 |\n" +
                "|  14 |  21 | 0.00000 |\n" +
                "|  14 |  22 | 0.00000 |\n" +
                "|  14 |  23 | 0.00000 |\n" +
                "|  14 |  24 | 0.00000 |\n" +
                "|  14 |  25 | 0.00000 |\n" +
                "|  14 |  26 | 0.00000 |\n" +
                "|  14 |  27 | 0.00000 |\n" +
                "|  14 |  28 | 0.00000 |\n" +
                "|  14 |  29 | 0.00000 |\n" +
                "|  14 |  30 | 0.00000 |\n" +
                "|  14 |  31 | 0.00000 |\n" +
                "|  14 |  32 | 0.00000 |\n" +
                "|  14 |  33 | 0.00000 |\n" +
                "|  14 |  34 | 0.00000 |\n" +
                "|  14 |  35 | 0.00000 |\n" +
                "|  14 |  36 | 0.00000 |\n" +
                "|  14 |  37 | 0.00000 |\n" +
                "|  14 |  38 | 0.00000 |\n" +
                "|  14 |  39 | 0.00000 |\n" +
                "|  14 |  40 | 0.00000 |\n" +
                "|  14 |  41 | 0.00000 |\n" +
                "|  14 |  42 | 0.00000 |\n" +
                "|  14 |  43 | 0.00000 |\n" +
                "|  14 |  44 | 0.00000 |\n" +
                "|  14 |  45 | 0.00000 |\n" +
                "|  14 |  46 | 0.00000 |\n" +
                "|  14 |  47 | 0.00000 |\n" +
                "|  14 |  48 | 0.00000 |\n" +
                "|  14 |  49 | 0.00000 |\n" +
                "|  14 |  50 | 0.00000 |\n" +
                "|  14 |  51 | 0.00000 |\n" +
                "|  14 |  52 | 0.00000 |\n" +
                "|  14 |  53 | 0.00000 |\n" +
                "|  14 |  54 | 0.00000 |\n" +
                "|  14 |  55 | 0.00000 |\n" +
                "|  14 |  56 | 0.00000 |\n" +
                "|  14 |  57 | 0.00000 |\n" +
                "|  14 |  58 | 0.00000 |\n" +
                "|  14 |  59 | 0.00000 |\n" +
                "|  14 |  60 | 0.00000 |\n" +
                "|  14 |  61 | 0.00000 |\n" +
                "|  14 |  62 | 0.00000 |\n" +
                "|  14 |  63 | 0.00000 |\n" +
                "|  14 |  64 | 0.00000 |\n" +
                "|  14 |  65 | 0.00000 |\n" +
                "|  14 |  66 | 0.00000 |\n" +
                "|  14 |  67 | 0.00000 |\n" +
                "|  14 |  68 | 0.00000 |\n" +
                "|  14 |  69 | 0.00000 |\n" +
                "|  14 |  70 | 0.00000 |\n" +
                "|  14 |  71 | 0.00000 |\n" +
                "|  14 |  72 | 0.00000 |\n" +
                "|  14 |  73 | 0.00000 |\n" +
                "|  14 |  74 | 0.00000 |\n" +
                "|  14 |  75 | 0.00000 |\n" +
                "|  14 |  76 | 0.00000 |\n" +
                "|  14 |  77 | 0.00000 |\n" +
                "|  14 |  78 | 0.00000 |\n" +
                "|  14 |  79 | 0.00000 |\n" +
                "|  14 |  80 | 0.00000 |\n" +
                "|  14 |  81 | 0.00000 |\n" +
                "|  14 |  82 | 0.00000 |\n" +
                "|  14 |  83 | 0.00000 |\n" +
                "|  14 |  84 | 0.00000 |\n" +
                "|  14 |  85 | 0.00000 |\n" +
                "|  14 |  86 | 0.00000 |\n" +
                "|  14 |  87 | 0.00000 |\n" +
                "|  14 |  88 | 0.00000 |\n" +
                "|  14 |  89 | 0.00000 |\n" +
                "|  14 |  90 | 0.00000 |\n" +
                "|  14 |  91 | 0.00000 |\n" +
                "|  14 |  92 | 0.00000 |\n" +
                "|  14 |  93 | 0.00000 |\n" +
                "|  14 |  94 | 0.00000 |\n" +
                "|  14 |  95 | 0.00000 |\n" +
                "|  14 |  96 | 0.00000 |\n" +
                "|  14 |  97 | 0.00000 |\n" +
                "|  14 |  98 | 0.00000 |\n" +
                "|  14 |  99 | 0.00000 |\n" +
                "|  14 | 100 | 0.00000 |\n" +
                "|  14 | 101 | 0.00000 |\n" +
                "|  14 | 102 | 0.00000 |\n" +
                "|  14 | 103 | 0.00000 |\n" +
                "|  14 | 104 | 0.00000 |\n" +
                "|  14 | 105 | 0.00000 |\n" +
                "|  14 | 106 | 0.00000 |\n" +
                "|  14 | 107 | 0.00000 |\n" +
                "|  14 | 108 | 0.00000 |\n" +
                "|  14 | 109 | 0.00000 |\n" +
                "|  14 | 110 | 0.00000 |\n" +
                "|  14 | 111 | 0.00000 |\n" +
                "|  14 | 112 | 0.00000 |\n" +
                "|  14 | 113 | 0.00000 |\n" +
                "|  14 | 114 | 0.00000 |\n" +
                "|  14 | 115 | 0.00000 |\n" +
                "|  14 | 116 | 0.00000 |\n" +
                "|  14 | 117 | 0.00000 |\n" +
                "|  14 | 118 | 0.00000 |\n" +
                "|  14 | 119 | 0.00000 |\n" +
                "|  14 | 120 | 0.00000 |\n" +
                "|  14 | 121 | 0.00000 |\n" +
                "|  14 | 122 | 0.00000 |\n" +
                "|  14 | 123 | 0.00000 |\n" +
                "|  14 | 124 | 0.00000 |\n" +
                "|  14 | 125 | 0.00000 |\n" +
                "|  14 | 126 | 0.00000 |\n" +
                "|  14 | 127 | 0.00000 |\n" +
                "|  14 | 128 | 0.00000 |\n" +
                "|  14 | 129 | 0.00000 |\n" +
                "|  14 | 130 | 0.00000 |\n" +
                "|  14 | 131 | 0.00000 |\n" +
                "|  14 | 132 | 0.00000 |\n" +
                "|  14 | 133 | 0.00000 |\n" +
                "|  14 | 134 | 0.00000 |\n" +
                "|  14 | 135 | 0.00000 |\n" +
                "|  14 | 136 | 0.00000 |\n" +
                "|  14 | 137 | 0.00000 |\n" +
                "|  14 | 138 | 0.00000 |\n" +
                "|  14 | 139 | 0.00000 |\n" +
                "|  14 | 140 | 0.00000 |\n" +
                "|  14 | 141 | 0.00000 |\n" +
                "|  14 | 142 | 0.00000 |\n" +
                "|  14 | 143 | 0.00000 |\n" +
                "|  14 | 144 | 0.00000 |\n" +
                "|  14 | 145 | 0.00000 |\n" +
                "|  14 | 146 | 0.00000 |\n" +
                "|  14 | 147 | 0.00000 |\n" +
                "|  15 |   1 | 0.00000 |\n" +
                "|  15 |   2 | 0.00000 |\n" +
                "|  15 |   3 | 0.00000 |\n" +
                "|  15 |   4 | 0.00000 |\n" +
                "|  15 |   5 | 0.00000 |\n" +
                "|  15 |   6 | 0.00000 |\n" +
                "|  15 |   7 | 0.00000 |\n" +
                "|  15 |   8 | 0.00000 |\n" +
                "|  15 |   9 | 0.00000 |\n" +
                "|  15 |  10 | 0.00000 |\n" +
                "|  15 |  11 | 0.00000 |\n" +
                "|  15 |  12 | 0.00000 |\n" +
                "|  15 |  13 | 0.00000 |\n" +
                "|  15 |  14 | 0.00000 |\n" +
                "|  15 |  15 | 0.00000 |\n" +
                "|  15 |  16 | 0.00000 |\n" +
                "|  15 |  17 | 0.00000 |\n" +
                "|  15 |  18 | 0.00000 |\n" +
                "|  15 |  19 | 0.00000 |\n" +
                "|  15 |  20 | 0.00000 |\n" +
                "|  15 |  21 | 0.00000 |\n" +
                "|  15 |  22 | 0.00000 |\n" +
                "|  15 |  23 | 0.00000 |\n" +
                "|  15 |  24 | 0.00000 |\n" +
                "|  15 |  25 | 0.00000 |\n" +
                "|  15 |  26 | 0.00000 |\n" +
                "|  15 |  27 | 0.00000 |\n" +
                "|  15 |  28 | 0.00000 |\n" +
                "|  15 |  29 | 0.00000 |\n" +
                "|  15 |  30 | 0.00000 |\n" +
                "|  15 |  31 | 0.00000 |\n" +
                "|  15 |  32 | 0.00000 |\n" +
                "|  15 |  33 | 0.00000 |\n" +
                "|  15 |  34 | 0.00000 |\n" +
                "|  15 |  35 | 0.00000 |\n" +
                "|  15 |  36 | 0.00000 |\n" +
                "|  15 |  37 | 0.00000 |\n" +
                "|  15 |  38 | 0.00000 |\n" +
                "|  15 |  39 | 0.00000 |\n" +
                "|  15 |  40 | 0.00000 |\n" +
                "|  15 |  41 | 0.00000 |\n" +
                "|  15 |  42 | 0.00000 |\n" +
                "|  15 |  43 | 0.00000 |\n" +
                "|  15 |  44 | 0.00000 |\n" +
                "|  15 |  45 | 0.00000 |\n" +
                "|  15 |  46 | 0.00000 |\n" +
                "|  15 |  47 | 0.00000 |\n" +
                "|  15 |  48 | 0.00000 |\n" +
                "|  15 |  49 | 0.00000 |\n" +
                "|  15 |  50 | 0.00000 |\n" +
                "|  15 |  51 | 0.00000 |\n" +
                "|  15 |  52 | 0.00000 |\n" +
                "|  15 |  53 | 0.00000 |\n" +
                "|  15 |  54 | 0.00000 |\n" +
                "|  15 |  55 | 0.00000 |\n" +
                "|  15 |  56 | 0.00000 |\n" +
                "|  15 |  57 | 0.00000 |\n" +
                "|  15 |  58 | 0.00000 |\n" +
                "|  15 |  59 | 0.00000 |\n" +
                "|  15 |  60 | 0.00000 |\n" +
                "|  15 |  61 | 0.00000 |\n" +
                "|  15 |  62 | 0.00000 |\n" +
                "|  15 |  63 | 0.00000 |\n" +
                "|  15 |  64 | 0.00000 |\n" +
                "|  15 |  65 | 0.00000 |\n" +
                "|  15 |  66 | 0.00000 |\n" +
                "|  15 |  67 | 0.00000 |\n" +
                "|  15 |  68 | 0.00000 |\n" +
                "|  15 |  69 | 0.00000 |\n" +
                "|  15 |  70 | 0.00000 |\n" +
                "|  15 |  71 | 0.00000 |\n" +
                "|  15 |  72 | 0.00000 |\n" +
                "|  15 |  73 | 0.00000 |\n" +
                "|  15 |  74 | 0.00000 |\n" +
                "|  15 |  75 | 0.00000 |\n" +
                "|  15 |  76 | 0.00000 |\n" +
                "|  15 |  77 | 0.00000 |\n" +
                "|  15 |  78 | 0.00000 |\n" +
                "|  15 |  79 | 0.00000 |\n" +
                "|  15 |  80 | 0.00000 |\n" +
                "|  15 |  81 | 0.00000 |\n" +
                "|  15 |  82 | 0.00000 |\n" +
                "|  15 |  83 | 0.00000 |\n" +
                "|  15 |  84 | 0.00000 |\n" +
                "|  15 |  85 | 0.00000 |\n" +
                "|  15 |  86 | 0.00000 |\n" +
                "|  15 |  87 | 0.00000 |\n" +
                "|  15 |  88 | 0.00000 |\n" +
                "|  15 |  89 | 0.00000 |\n" +
                "|  15 |  90 | 0.00000 |\n" +
                "|  15 |  91 | 0.00000 |\n" +
                "|  15 |  92 | 0.00000 |\n" +
                "|  15 |  93 | 0.00000 |\n" +
                "|  15 |  94 | 0.00000 |\n" +
                "|  15 |  95 | 0.00000 |\n" +
                "|  15 |  96 | 0.00000 |\n" +
                "|  15 |  97 | 0.00000 |\n" +
                "|  15 |  98 | 0.00000 |\n" +
                "|  15 |  99 | 0.00000 |\n" +
                "|  15 | 100 | 0.00000 |\n" +
                "|  15 | 101 | 0.00000 |\n" +
                "|  15 | 102 | 0.00000 |\n" +
                "|  15 | 103 | 0.00000 |\n" +
                "|  15 | 104 | 0.00000 |\n" +
                "|  15 | 105 | 0.00000 |\n" +
                "|  15 | 106 | 0.00000 |\n" +
                "|  15 | 107 | 0.00000 |\n" +
                "|  15 | 108 | 0.00000 |\n" +
                "|  15 | 109 | 0.00000 |\n" +
                "|  15 | 110 | 0.00000 |\n" +
                "|  15 | 111 | 0.00000 |\n" +
                "|  15 | 112 | 0.00000 |\n" +
                "|  15 | 113 | 0.00000 |\n" +
                "|  15 | 114 | 0.00000 |\n" +
                "|  15 | 115 | 0.00000 |\n" +
                "|  15 | 116 | 0.00000 |\n" +
                "|  15 | 117 | 0.00000 |\n" +
                "|  15 | 118 | 0.00000 |\n" +
                "|  15 | 119 | 0.00000 |\n" +
                "|  15 | 120 | 0.00000 |\n" +
                "|  15 | 121 | 0.00000 |\n" +
                "|  15 | 122 | 0.00000 |\n" +
                "|  15 | 123 | 0.00000 |\n" +
                "|  15 | 124 | 0.00000 |\n" +
                "|  15 | 125 | 0.00000 |\n" +
                "|  15 | 126 | 0.00000 |\n" +
                "|  15 | 127 | 0.00000 |\n" +
                "|  15 | 128 | 0.00000 |\n" +
                "|  15 | 129 | 0.00000 |\n" +
                "|  15 | 130 | 0.00000 |\n" +
                "|  15 | 131 | 0.00000 |\n" +
                "|  15 | 132 | 0.00000 |\n" +
                "|  15 | 133 | 0.00000 |\n" +
                "|  15 | 134 | 0.00000 |\n" +
                "|  15 | 135 | 0.00000 |\n" +
                "|  15 | 136 | 0.00000 |\n" +
                "|  15 | 137 | 0.00000 |\n" +
                "|  15 | 138 | 0.00000 |\n" +
                "|  15 | 139 | 0.00000 |\n" +
                "|  15 | 140 | 0.00000 |\n" +
                "|  15 | 141 | 0.00000 |\n" +
                "|  15 | 142 | 0.00000 |\n" +
                "|  15 | 143 | 0.00000 |\n" +
                "|  15 | 144 | 0.00000 |\n" +
                "|  15 | 145 | 0.00000 |\n" +
                "|  15 | 146 | 0.00000 |\n" +
                "|  15 | 147 | 0.00000 |\n" +
                "|  16 |   1 | 0.00000 |\n" +
                "|  16 |   2 | 0.00000 |\n" +
                "|  16 |   3 | 0.00000 |\n" +
                "|  16 |   4 | 0.00000 |\n" +
                "|  16 |   5 | 0.00000 |\n" +
                "|  16 |   6 | 0.00000 |\n" +
                "|  16 |   7 | 0.00000 |\n" +
                "|  16 |   8 | 0.00000 |\n" +
                "|  16 |   9 | 0.00000 |\n" +
                "|  16 |  10 | 0.00000 |\n" +
                "|  16 |  11 | 0.00000 |\n" +
                "|  16 |  12 | 0.00000 |\n" +
                "|  16 |  13 | 0.00000 |\n" +
                "|  16 |  14 | 0.00000 |\n" +
                "|  16 |  15 | 0.00000 |\n" +
                "|  16 |  16 | 0.00000 |\n" +
                "|  16 |  17 | 0.00000 |\n" +
                "|  16 |  18 | 0.00000 |\n" +
                "|  16 |  19 | 0.00000 |\n" +
                "|  16 |  20 | 0.00000 |\n" +
                "|  16 |  21 | 0.00000 |\n" +
                "|  16 |  22 | 0.00000 |\n" +
                "|  16 |  23 | 0.00000 |\n" +
                "|  16 |  24 | 0.00000 |\n" +
                "|  16 |  25 | 0.00000 |\n" +
                "|  16 |  26 | 0.00000 |\n" +
                "|  16 |  27 | 0.00000 |\n" +
                "|  16 |  28 | 0.00000 |\n" +
                "|  16 |  29 | 0.00000 |\n" +
                "|  16 |  30 | 0.00000 |\n" +
                "|  16 |  31 | 0.00000 |\n" +
                "|  16 |  32 | 0.00000 |\n" +
                "|  16 |  33 | 0.00000 |\n" +
                "|  16 |  34 | 0.00000 |\n" +
                "|  16 |  35 | 0.00000 |\n" +
                "|  16 |  36 | 0.00000 |\n" +
                "|  16 |  37 | 0.00000 |\n" +
                "|  16 |  38 | 0.00000 |\n" +
                "|  16 |  39 | 0.00000 |\n" +
                "|  16 |  40 | 0.00000 |\n" +
                "|  16 |  41 | 0.00000 |\n" +
                "|  16 |  42 | 0.00000 |\n" +
                "|  16 |  43 | 0.00000 |\n" +
                "|  16 |  44 | 0.00000 |\n" +
                "|  16 |  45 | 0.00000 |\n" +
                "|  16 |  46 | 0.00000 |\n" +
                "|  16 |  47 | 0.00000 |\n" +
                "|  16 |  48 | 0.00000 |\n" +
                "|  16 |  49 | 0.00000 |\n" +
                "|  16 |  50 | 0.00000 |\n" +
                "|  16 |  51 | 0.00000 |\n" +
                "|  16 |  52 | 0.00000 |\n" +
                "|  16 |  53 | 0.00000 |\n" +
                "|  16 |  54 | 0.00000 |\n" +
                "|  16 |  55 | 0.00000 |\n" +
                "|  16 |  56 | 0.00000 |\n" +
                "|  16 |  57 | 0.00000 |\n" +
                "|  16 |  58 | 0.00000 |\n" +
                "|  16 |  59 | 0.00000 |\n" +
                "|  16 |  60 | 0.00000 |\n" +
                "|  16 |  61 | 0.00000 |\n" +
                "|  16 |  62 | 0.00000 |\n" +
                "|  16 |  63 | 0.00000 |\n" +
                "|  16 |  64 | 0.00000 |\n" +
                "|  16 |  65 | 0.00000 |\n" +
                "|  16 |  66 | 0.00000 |\n" +
                "|  16 |  67 | 0.00000 |\n" +
                "|  16 |  68 | 0.00000 |\n" +
                "|  16 |  69 | 0.00000 |\n" +
                "|  16 |  70 | 0.00000 |\n" +
                "|  16 |  71 | 0.00000 |\n" +
                "|  16 |  72 | 0.00000 |\n" +
                "|  16 |  73 | 0.00000 |\n" +
                "|  16 |  74 | 0.00000 |\n" +
                "|  16 |  75 | 0.00000 |\n" +
                "|  16 |  76 | 0.00000 |\n" +
                "|  16 |  77 | 0.00000 |\n" +
                "|  16 |  78 | 0.00000 |\n" +
                "|  16 |  79 | 0.00000 |\n" +
                "|  16 |  80 | 0.00000 |\n" +
                "|  16 |  81 | 0.00000 |\n" +
                "|  16 |  82 | 0.00000 |\n" +
                "|  16 |  83 | 0.00000 |\n" +
                "|  16 |  84 | 0.00000 |\n" +
                "|  16 |  85 | 0.00000 |\n" +
                "|  16 |  86 | 0.00000 |\n" +
                "|  16 |  87 | 0.00000 |\n" +
                "|  16 |  88 | 0.00000 |\n" +
                "|  16 |  89 | 0.00000 |\n" +
                "|  16 |  90 | 0.00000 |\n" +
                "|  16 |  91 | 0.00000 |\n" +
                "|  16 |  92 | 0.00000 |\n" +
                "|  16 |  93 | 0.00000 |\n" +
                "|  16 |  94 | 0.00000 |\n" +
                "|  16 |  95 | 0.00000 |\n" +
                "|  16 |  96 | 0.00000 |\n" +
                "|  16 |  97 | 0.00000 |\n" +
                "|  16 |  98 | 0.00000 |\n" +
                "|  16 |  99 | 0.00000 |\n" +
                "|  16 | 100 | 0.00000 |\n" +
                "|  16 | 101 | 0.00000 |\n" +
                "|  16 | 102 | 0.00000 |\n" +
                "|  16 | 103 | 0.00000 |\n" +
                "|  16 | 104 | 0.00000 |\n" +
                "|  16 | 105 | 0.00000 |\n" +
                "|  16 | 106 | 0.00000 |\n" +
                "|  16 | 107 | 0.00000 |\n" +
                "|  16 | 108 | 0.00000 |\n" +
                "|  16 | 109 | 0.00000 |\n" +
                "|  16 | 110 | 0.00000 |\n" +
                "|  16 | 111 | 0.00000 |\n" +
                "|  16 | 112 | 0.00000 |\n" +
                "|  16 | 113 | 0.00000 |\n" +
                "|  16 | 114 | 0.00000 |\n" +
                "|  16 | 115 | 0.00000 |\n" +
                "|  16 | 116 | 0.00000 |\n" +
                "|  16 | 117 | 0.00000 |\n" +
                "|  16 | 118 | 0.00000 |\n" +
                "|  16 | 119 | 0.00000 |\n" +
                "|  16 | 120 | 0.00000 |\n" +
                "|  16 | 121 | 0.00000 |\n" +
                "|  16 | 122 | 0.00000 |\n" +
                "|  16 | 123 | 0.00000 |\n" +
                "|  16 | 124 | 0.00000 |\n" +
                "|  16 | 125 | 0.00000 |\n" +
                "|  16 | 126 | 0.00000 |\n" +
                "|  16 | 127 | 0.00000 |\n" +
                "|  16 | 128 | 0.00000 |\n" +
                "|  16 | 129 | 0.00000 |\n" +
                "|  16 | 130 | 0.00000 |\n" +
                "|  16 | 131 | 0.00000 |\n" +
                "|  16 | 132 | 0.00000 |\n" +
                "|  16 | 133 | 0.00000 |\n" +
                "|  16 | 134 | 0.00000 |\n" +
                "|  16 | 135 | 0.00000 |\n" +
                "|  16 | 136 | 0.00000 |\n" +
                "|  16 | 137 | 0.00000 |\n" +
                "|  16 | 138 | 0.00000 |\n" +
                "|  16 | 139 | 0.00000 |\n" +
                "|  16 | 140 | 0.00000 |\n" +
                "|  16 | 141 | 0.00000 |\n" +
                "|  16 | 142 | 0.00000 |\n" +
                "|  16 | 143 | 0.00000 |\n" +
                "|  16 | 144 | 0.00000 |\n" +
                "|  16 | 145 | 0.00000 |\n" +
                "|  16 | 146 | 0.00000 |\n" +
                "|  16 | 147 | 0.00000 |\n" +
                "|  17 |   1 | 0.00000 |\n" +
                "|  17 |   2 | 0.00000 |\n" +
                "|  17 |   3 | 0.00000 |\n" +
                "|  17 |   4 | 0.00000 |\n" +
                "|  17 |   5 | 0.00000 |\n" +
                "|  17 |   6 | 0.00000 |\n" +
                "|  17 |   7 | 0.00000 |\n" +
                "|  17 |   8 | 0.00000 |\n" +
                "|  17 |   9 | 0.00000 |\n" +
                "|  17 |  10 | 0.00000 |\n" +
                "|  17 |  11 | 0.00000 |\n" +
                "|  17 |  12 | 0.00000 |\n" +
                "|  17 |  13 | 0.00000 |\n" +
                "|  17 |  14 | 0.00000 |\n" +
                "|  17 |  15 | 0.00000 |\n" +
                "|  17 |  16 | 0.00000 |\n" +
                "|  17 |  17 | 0.00000 |\n" +
                "|  17 |  18 | 0.00000 |\n" +
                "|  17 |  19 | 0.00000 |\n" +
                "|  17 |  20 | 0.00000 |\n" +
                "|  17 |  21 | 0.00000 |\n" +
                "|  17 |  22 | 0.00000 |\n" +
                "|  17 |  23 | 0.00000 |\n" +
                "|  17 |  24 | 0.00000 |\n" +
                "|  17 |  25 | 0.00000 |\n" +
                "|  17 |  26 | 0.00000 |\n" +
                "|  17 |  27 | 0.00000 |\n" +
                "|  17 |  28 | 0.00000 |\n" +
                "|  17 |  29 | 0.00000 |\n" +
                "|  17 |  30 | 0.00000 |\n" +
                "|  17 |  31 | 0.00000 |\n" +
                "|  17 |  32 | 0.00000 |\n" +
                "|  17 |  33 | 0.00000 |\n" +
                "|  17 |  34 | 0.00000 |\n" +
                "|  17 |  35 | 0.00000 |\n" +
                "|  17 |  36 | 0.00000 |\n" +
                "|  17 |  37 | 0.00000 |\n" +
                "|  17 |  38 | 0.00000 |\n" +
                "|  17 |  39 | 0.00000 |\n" +
                "|  17 |  40 | 0.00000 |\n" +
                "|  17 |  41 | 0.00000 |\n" +
                "|  17 |  42 | 0.00000 |\n" +
                "|  17 |  43 | 0.00000 |\n" +
                "|  17 |  44 | 0.00000 |\n" +
                "|  17 |  45 | 0.00000 |\n" +
                "|  17 |  46 | 0.00000 |\n" +
                "|  17 |  47 | 0.00000 |\n" +
                "|  17 |  48 | 0.00000 |\n" +
                "|  17 |  49 | 0.00000 |\n" +
                "|  17 |  50 | 0.00000 |\n" +
                "|  17 |  51 | 0.00000 |\n" +
                "|  17 |  52 | 0.00000 |\n" +
                "|  17 |  53 | 0.00000 |\n" +
                "|  17 |  54 | 0.00000 |\n" +
                "|  17 |  55 | 0.00000 |\n" +
                "|  17 |  56 | 0.00000 |\n" +
                "|  17 |  57 | 0.00000 |\n" +
                "|  17 |  58 | 0.00000 |\n" +
                "|  17 |  59 | 0.00000 |\n" +
                "|  17 |  60 | 0.00000 |\n" +
                "|  17 |  61 | 0.00000 |\n" +
                "|  17 |  62 | 0.00000 |\n" +
                "|  17 |  63 | 0.00000 |\n" +
                "|  17 |  64 | 0.00000 |\n" +
                "|  17 |  65 | 0.00000 |\n" +
                "|  17 |  66 | 0.00000 |\n" +
                "|  17 |  67 | 0.00000 |\n" +
                "|  17 |  68 | 0.00000 |\n" +
                "|  17 |  69 | 0.00000 |\n" +
                "|  17 |  70 | 0.00000 |\n" +
                "|  17 |  71 | 0.00000 |\n" +
                "|  17 |  72 | 0.00000 |\n" +
                "|  17 |  73 | 0.00000 |\n" +
                "|  17 |  74 | 0.00000 |\n" +
                "|  17 |  75 | 0.00000 |\n" +
                "|  17 |  76 | 0.00000 |\n" +
                "|  17 |  77 | 0.00000 |\n" +
                "|  17 |  78 | 0.00000 |\n" +
                "|  17 |  79 | 0.00000 |\n" +
                "|  17 |  80 | 0.00000 |\n" +
                "|  17 |  81 | 0.00000 |\n" +
                "|  17 |  82 | 0.00000 |\n" +
                "|  17 |  83 | 0.00000 |\n" +
                "|  17 |  84 | 0.00000 |\n" +
                "|  17 |  85 | 0.00000 |\n" +
                "|  17 |  86 | 0.00000 |\n" +
                "|  17 |  87 | 0.00000 |\n" +
                "|  17 |  88 | 0.00000 |\n" +
                "|  17 |  89 | 0.00000 |\n" +
                "|  17 |  90 | 0.00000 |\n" +
                "|  17 |  91 | 0.00000 |\n" +
                "|  17 |  92 | 0.00000 |\n" +
                "|  17 |  93 | 0.00000 |\n" +
                "|  17 |  94 | 0.00000 |\n" +
                "|  17 |  95 | 0.00000 |\n" +
                "|  17 |  96 | 0.00000 |\n" +
                "|  17 |  97 | 0.00000 |\n" +
                "|  17 |  98 | 0.00000 |\n" +
                "|  17 |  99 | 0.00000 |\n" +
                "|  17 | 100 | 0.00000 |\n" +
                "|  17 | 101 | 0.00000 |\n" +
                "|  17 | 102 | 0.00000 |\n" +
                "|  17 | 103 | 0.00000 |\n" +
                "|  17 | 104 | 0.00000 |\n" +
                "|  17 | 105 | 0.00000 |\n" +
                "|  17 | 106 | 0.00000 |\n" +
                "|  17 | 107 | 0.00000 |\n" +
                "|  17 | 108 | 0.00000 |\n" +
                "|  17 | 109 | 0.00000 |\n" +
                "|  17 | 110 | 0.00000 |\n" +
                "|  17 | 111 | 0.00000 |\n" +
                "|  17 | 112 | 0.00000 |\n" +
                "|  17 | 113 | 0.00000 |\n" +
                "|  17 | 114 | 0.00000 |\n" +
                "|  17 | 115 | 0.00000 |\n" +
                "|  17 | 116 | 0.00000 |\n" +
                "|  17 | 117 | 0.00000 |\n" +
                "|  17 | 118 | 0.00000 |\n" +
                "|  17 | 119 | 0.00000 |\n" +
                "|  17 | 120 | 0.00000 |\n" +
                "|  17 | 121 | 0.00000 |\n" +
                "|  17 | 122 | 0.00000 |\n" +
                "|  17 | 123 | 0.00000 |\n" +
                "|  17 | 124 | 0.00000 |\n" +
                "|  17 | 125 | 0.00000 |\n" +
                "|  17 | 126 | 0.00000 |\n" +
                "|  17 | 127 | 0.00000 |\n" +
                "|  17 | 128 | 0.00000 |\n" +
                "|  17 | 129 | 0.00000 |\n" +
                "|  17 | 130 | 0.00000 |\n" +
                "|  17 | 131 | 0.00000 |\n" +
                "|  17 | 132 | 0.00000 |\n" +
                "|  17 | 133 | 0.00000 |\n" +
                "|  17 | 134 | 0.00000 |\n" +
                "|  17 | 135 | 0.00000 |\n" +
                "|  17 | 136 | 0.00000 |\n" +
                "|  17 | 137 | 0.00000 |\n" +
                "|  17 | 138 | 0.00000 |\n" +
                "|  17 | 139 | 0.00000 |\n" +
                "|  17 | 140 | 0.00000 |\n" +
                "|  17 | 141 | 0.00000 |\n" +
                "|  17 | 142 | 0.00000 |\n" +
                "|  17 | 143 | 0.00000 |\n" +
                "|  17 | 144 | 0.00000 |\n" +
                "|  17 | 145 | 0.00000 |\n" +
                "|  17 | 146 | 0.00000 |\n" +
                "|  17 | 147 | 0.00000 |\n" +
                "|  18 |   1 | 0.00000 |\n" +
                "|  18 |   2 | 0.00000 |\n" +
                "|  18 |   3 | 0.00000 |\n" +
                "|  18 |   4 | 0.00000 |\n" +
                "|  18 |   5 | 0.00000 |\n" +
                "|  18 |   6 | 0.00000 |\n" +
                "|  18 |   7 | 0.00000 |\n" +
                "|  18 |   8 | 0.00000 |\n" +
                "|  18 |   9 | 0.00000 |\n" +
                "|  18 |  10 | 0.00000 |\n" +
                "|  18 |  11 | 0.00000 |\n" +
                "|  18 |  12 | 0.00000 |\n" +
                "|  18 |  13 | 0.00000 |\n" +
                "|  18 |  14 | 0.00000 |\n" +
                "|  18 |  15 | 0.00000 |\n" +
                "|  18 |  16 | 0.00000 |\n" +
                "|  18 |  17 | 0.00000 |\n" +
                "|  18 |  18 | 0.00000 |\n" +
                "|  18 |  19 | 0.00000 |\n" +
                "|  18 |  20 | 0.00000 |\n" +
                "|  18 |  21 | 0.00000 |\n" +
                "|  18 |  22 | 0.00000 |\n" +
                "|  18 |  23 | 0.00000 |\n" +
                "|  18 |  24 | 0.00000 |\n" +
                "|  18 |  25 | 0.00000 |\n" +
                "|  18 |  26 | 0.00000 |\n" +
                "|  18 |  27 | 0.00000 |\n" +
                "|  18 |  28 | 0.00000 |\n" +
                "|  18 |  29 | 0.00000 |\n" +
                "|  18 |  30 | 0.00000 |\n" +
                "|  18 |  31 | 0.00000 |\n" +
                "|  18 |  32 | 0.00000 |\n" +
                "|  18 |  33 | 0.00000 |\n" +
                "|  18 |  34 | 0.00000 |\n" +
                "|  18 |  35 | 0.00000 |\n" +
                "|  18 |  36 | 0.00000 |\n" +
                "|  18 |  37 | 0.00000 |\n" +
                "|  18 |  38 | 0.00000 |\n" +
                "|  18 |  39 | 0.00000 |\n" +
                "|  18 |  40 | 0.00000 |\n" +
                "|  18 |  41 | 0.00000 |\n" +
                "|  18 |  42 | 0.00000 |\n" +
                "|  18 |  43 | 0.00000 |\n" +
                "|  18 |  44 | 0.00000 |\n" +
                "|  18 |  45 | 0.00000 |\n" +
                "|  18 |  46 | 0.00000 |\n" +
                "|  18 |  47 | 0.00000 |\n" +
                "|  18 |  48 | 0.00000 |\n" +
                "|  18 |  49 | 0.00000 |\n" +
                "|  18 |  50 | 0.00000 |\n" +
                "|  18 |  51 | 0.00000 |\n" +
                "|  18 |  52 | 0.00000 |\n" +
                "|  18 |  53 | 0.00000 |\n" +
                "|  18 |  54 | 0.00000 |\n" +
                "|  18 |  55 | 0.00000 |\n" +
                "|  18 |  56 | 0.00000 |\n" +
                "|  18 |  57 | 0.00000 |\n" +
                "|  18 |  58 | 0.00000 |\n" +
                "|  18 |  59 | 0.00000 |\n" +
                "|  18 |  60 | 0.00000 |\n" +
                "|  18 |  61 | 0.00000 |\n" +
                "|  18 |  62 | 0.00000 |\n" +
                "|  18 |  63 | 0.00000 |\n" +
                "|  18 |  64 | 0.00000 |\n" +
                "|  18 |  65 | 0.00000 |\n" +
                "|  18 |  66 | 0.00000 |\n" +
                "|  18 |  67 | 0.00000 |\n" +
                "|  18 |  68 | 0.00000 |\n" +
                "|  18 |  69 | 0.00000 |\n" +
                "|  18 |  70 | 0.00000 |\n" +
                "|  18 |  71 | 0.00000 |\n" +
                "|  18 |  72 | 0.00000 |\n" +
                "|  18 |  73 | 0.00000 |\n" +
                "|  18 |  74 | 0.00000 |\n" +
                "|  18 |  75 | 0.00000 |\n" +
                "|  18 |  76 | 0.00000 |\n" +
                "|  18 |  77 | 0.00000 |\n" +
                "|  18 |  78 | 0.00000 |\n" +
                "|  18 |  79 | 0.00000 |\n" +
                "|  18 |  80 | 0.00000 |\n" +
                "|  18 |  81 | 0.00000 |\n" +
                "|  18 |  82 | 0.00000 |\n" +
                "|  18 |  83 | 0.00000 |\n" +
                "|  18 |  84 | 0.00000 |\n" +
                "|  18 |  85 | 0.00000 |\n" +
                "|  18 |  86 | 0.00000 |\n" +
                "|  18 |  87 | 0.00000 |\n" +
                "|  18 |  88 | 0.00000 |\n" +
                "|  18 |  89 | 0.00000 |\n" +
                "|  18 |  90 | 0.00000 |\n" +
                "|  18 |  91 | 0.00000 |\n" +
                "|  18 |  92 | 0.00000 |\n" +
                "|  18 |  93 | 0.00000 |\n" +
                "|  18 |  94 | 0.00000 |\n" +
                "|  18 |  95 | 0.00000 |\n" +
                "|  18 |  96 | 0.00000 |\n" +
                "|  18 |  97 | 0.00000 |\n" +
                "|  18 |  98 | 0.00000 |\n" +
                "|  18 |  99 | 0.00000 |\n" +
                "|  18 | 100 | 0.00000 |\n" +
                "|  18 | 101 | 0.00000 |\n" +
                "|  18 | 102 | 0.00000 |\n" +
                "|  18 | 103 | 0.00000 |\n" +
                "|  18 | 104 | 0.00000 |\n" +
                "|  18 | 105 | 0.00000 |\n" +
                "|  18 | 106 | 0.00000 |\n" +
                "|  18 | 107 | 0.00000 |\n" +
                "|  18 | 108 | 0.00000 |\n" +
                "|  18 | 109 | 0.00000 |\n" +
                "|  18 | 110 | 0.00000 |\n" +
                "|  18 | 111 | 0.00000 |\n" +
                "|  18 | 112 | 0.00000 |\n" +
                "|  18 | 113 | 0.00000 |\n" +
                "|  18 | 114 | 0.00000 |\n" +
                "|  18 | 115 | 0.00000 |\n" +
                "|  18 | 116 | 0.00000 |\n" +
                "|  18 | 117 | 0.00000 |\n" +
                "|  18 | 118 | 0.00000 |\n" +
                "|  18 | 119 | 0.00000 |\n" +
                "|  18 | 120 | 0.00000 |\n" +
                "|  18 | 121 | 0.00000 |\n" +
                "|  18 | 122 | 0.00000 |\n" +
                "|  18 | 123 | 0.00000 |\n" +
                "|  18 | 124 | 0.00000 |\n" +
                "|  18 | 125 | 0.00000 |\n" +
                "|  18 | 126 | 0.00000 |\n" +
                "|  18 | 127 | 0.00000 |\n" +
                "|  18 | 128 | 0.00000 |\n" +
                "|  18 | 129 | 0.00000 |\n" +
                "|  18 | 130 | 0.00000 |\n" +
                "|  18 | 131 | 0.00000 |\n" +
                "|  18 | 132 | 0.00000 |\n" +
                "|  18 | 133 | 0.00000 |\n" +
                "|  18 | 134 | 0.00000 |\n" +
                "|  18 | 135 | 0.00000 |\n" +
                "|  18 | 136 | 0.00000 |\n" +
                "|  18 | 137 | 0.00000 |\n" +
                "|  18 | 138 | 0.00000 |\n" +
                "|  18 | 139 | 0.00000 |\n" +
                "|  18 | 140 | 0.00000 |\n" +
                "|  18 | 141 | 0.00000 |\n" +
                "|  18 | 142 | 0.00000 |\n" +
                "|  18 | 143 | 0.00000 |\n" +
                "|  18 | 144 | 0.00000 |\n" +
                "|  18 | 145 | 0.00000 |\n" +
                "|  18 | 146 | 0.00000 |\n" +
                "|  18 | 147 | 0.00000 |";
        for(String line : data.split("\\n")){
            String[] elements = line.split("\\|");
            String mid =  elements[1].trim();
            String fid =  elements[2].trim();
            String price =  elements[3].trim();
            mDatabase.execSQL(sqladd1, new String[]{
                    "\""+mid+"\"",
                    "\""+fid+"\"",
                    "\""+price+"\"",
            });
        }

    }

    private void updateFishTypeData() {
        mDatabase.delete(FISHPRICES, "1=1", new String[]{});
        String sqladd1 = "insert into "+FISHTYPES+"\n" + "(fid,name)\n" + "values\n" +"(?, ?)";
        String data = "|   1 | Anchovies                       |\n" +
                "|   2 | Asian Seabass                   |\n" +
                "|   3 | Barred queen fish               |\n" +
                "|   4 | Bearded croaker                 |\n" +
                "|   5 | Big eye scad                    |\n" +
                "|   6 | Big eye thresher                |\n" +
                "|   7 | Big eye trevally                |\n" +
                "|   8 | Big eye tuna                    |\n" +
                "|   9 | Black barred half beak          |\n" +
                "|  10 | Black clam                      |\n" +
                "|  11 | Black marlin                    |\n" +
                "|  12 | Black pomfret                   |\n" +
                "|  13 | Black tip reef shark            |\n" +
                "|  14 | Blue Swimmer crab               |\n" +
                "|  15 | Bombay duck                     |\n" +
                "|  16 | Brown mussel                    |\n" +
                "|  17 | Bullet tuna                     |\n" +
                "|  18 | Catla                           |\n" +
                "|  19 | Chinese pomfret                 |\n" +
                "|  20 | Climbing Perch                  |\n" +
                "|  21 | Cobia                           |\n" +
                "|  22 | Commerson's anchovy             |\n" +
                "|  23 | Commerson's glassy perchlet     |\n" +
                "|  24 | Common Carp                     |\n" +
                "|  25 | Common dolphin fish             |\n" +
                "|  26 | Cross crab                      |\n" +
                "|  27 | Dog tooth tuna                  |\n" +
                "|  28 | Dorab wolf herring              |\n" +
                "|  29 | Engraved cat fish               |\n" +
                "|  30 | False trevally                  |\n" +
                "|  31 | Flat head lobster               |\n" +
                "|  32 | Flat head mullet                |\n" +
                "|  33 | Flower tail prawn               |\n" +
                "|  34 | Four finger thread fin          |\n" +
                "|  35 | Frigate tuna                    |\n" +
                "|  36 | Fringed - lipped peninsula carp |\n" +
                "|  37 | Gaint cat fish                  |\n" +
                "|  38 | Giant guitar fish               |\n" +
                "|  39 | Giant tiger Prawn               |\n" +
                "|  40 | Gold spot mullet                |\n" +
                "|  41 | Gold spotted granadier anchovy  |\n" +
                "|  42 | Granulated guitar fish          |\n" +
                "|  43 | Grass carp                      |\n" +
                "|  44 | Greasy grouper                  |\n" +
                "|  45 | Great barracuda                 |\n" +
                "|  46 | Greater lizard fish             |\n" +
                "|  47 | Green mussel                    |\n" +
                "|  48 | Green tiger prawn               |\n" +
                "|  49 | Grey Mullet                     |\n" +
                "|  50 | Hardenberg's anchovy            |\n" +
                "|  51 | Hilsa shad                      |\n" +
                "|  52 | Hound needle fish               |\n" +
                "|  53 | Hump back nylon shrimp          |\n" +
                "|  54 | Indian butter catfish           |\n" +
                "|  55 | Indian flat head                |\n" +
                "|  56 | Indian halibut                  |\n" +
                "|  57 | Indian Mackeral                 |\n" +
                "|  58 | Indian nylon shrimp             |\n" +
                "|  59 | Indian pike conger              |\n" +
                "|  60 | Indian scad                     |\n" +
                "|  61 | Indian squid                    |\n" +
                "|  62 | Indian thread fin               |\n" +
                "|  63 | Indian white shrimp             |\n" +
                "|  64 | Indo-pacific sail fish          |\n" +
                "|  65 | Indo-pacific seer fish          |\n" +
                "|  66 | Japanese threadfin bream        |\n" +
                "|  67 | Jinga prawn / Brown shrimp      |\n" +
                "|  68 | Johns snapper                   |\n" +
                "|  69 | Kiddi shrimp                    |\n" +
                "|  70 | King Seer                       |\n" +
                "|  71 | Kuruma prawn                    |\n" +
                "|  72 | Large head hairtail             |\n" +
                "|  73 | Large scale tongue sole         |\n" +
                "|  74 | Large tooth flounder            |\n" +
                "|  75 | Lesser tiger tooth croaker      |\n" +
                "|  76 | Little tuna                     |\n" +
                "|  77 | Long barrel squid               |\n" +
                "|  78 | Long tail tuna                  |\n" +
                "|  79 | Magur                           |\n" +
                "|  80 | Malabar anchovy                 |\n" +
                "|  81 | Malabar blood snapper           |\n" +
                "|  82 | Malabar grouper                 |\n" +
                "|  83 | Malabar tongue sole             |\n" +
                "|  84 | Mangrove snapper                |\n" +
                "|  85 | Marbled octopus                 |\n" +
                "|  86 | Margined Flying Fish            |\n" +
                "|  87 | Milk Shark                      |\n" +
                "|  88 | Milkfish                        |\n" +
                "|  89 | Mrigal                          |\n" +
                "|  90 | Mud crab                        |\n" +
                "|  91 | Murrel fish                     |\n" +
                "|  92 | Mustached anchovy               |\n" +
                "|  93 | Needle cuttle fish              |\n" +
                "|  94 | Octopus                         |\n" +
                "|  95 | Oil Sardine                     |\n" +
                "|  96 | Oyster                          |\n" +
                "|  97 | Pacu                            |\n" +
                "|  98 | Pale edged sting ray            |\n" +
                "|  99 | Pangasius                       |\n" +
                "| 100 | Pearl Spot                      |\n" +
                "| 101 | Pelagic thresher                |\n" +
                "| 102 | Pharaoh cuttlefish              |\n" +
                "| 103 | Pick handle barracuda           |\n" +
                "| 104 | Pompano                         |\n" +
                "| 105 | pompano dolphin fish            |\n" +
                "| 106 | Rainbow sardine                 |\n" +
                "| 107 | Randall's threadfin beam        |\n" +
                "| 108 | Red ring                        |\n" +
                "| 109 | Rock lobster                    |\n" +
                "| 110 | Rohu                            |\n" +
                "| 111 | Sharp tooth hammer croaker      |\n" +
                "| 112 | Short fin eel                   |\n" +
                "| 113 | Short neck clam                 |\n" +
                "| 114 | Shrimp scad                     |\n" +
                "| 115 | Silver carp                     |\n" +
                "| 116 | Silver Promfret                 |\n" +
                "| 117 | Singhi                          |\n" +
                "| 118 | Skipjack tuna                   |\n" +
                "| 119 | Slender bamboo shark            |\n" +
                "| 120 | Small head hairtail             |\n" +
                "| 121 | Small tooth saw fish            |\n" +
                "| 122 | Snake head                      |\n" +
                "| 123 | Spade nose shark                |\n" +
                "| 124 | Speckled prawn                  |\n" +
                "| 125 | Spineless cuttle fish           |\n" +
                "| 126 | Spiny cheek grouper             |\n" +
                "| 127 | Splendid pony fish              |\n" +
                "| 128 | Spotted crab                    |\n" +
                "| 129 | Spotted croaker                 |\n" +
                "| 130 | Spotted eagle ray               |\n" +
                "| 131 | Striped bonito                  |\n" +
                "| 132 | Sword fish                      |\n" +
                "| 133 | Sword tip squid                 |\n" +
                "| 134 | Tiger shark                     |\n" +
                "| 135 | Tiger shrimp                    |\n" +
                "| 136 | Tilapia                         |\n" +
                "| 137 | Torpedo scad                    |\n" +
                "| 138 | Two wing flying fish            |\n" +
                "| 139 | Wahoo                           |\n" +
                "| 140 | Whip tail sting ray             |\n" +
                "| 141 | White carp                      |\n" +
                "| 142 | White fin wolf herring          |\n" +
                "| 143 | White sardine                   |\n" +
                "| 144 | Yellow clam                     |\n" +
                "| 145 | yellow goat fish                |\n" +
                "| 146 | Yellowfin tuna                  |\n" +
                "| 147 | Zebra shark                     |\n";
        for(String line : data.split("\\n")){
            String[] elements = line.split("\\|");
            String id =  elements[1].trim();
            String name =  elements[2].trim();
            mDatabase.execSQL(sqladd1, new String[]{
                    "\""+id+"\"",
                    "\""+name+"\""
            });
        }
    }

}
