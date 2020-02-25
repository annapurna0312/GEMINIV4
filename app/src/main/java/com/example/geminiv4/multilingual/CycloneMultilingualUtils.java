package com.example.geminiv4.multilingual;

import android.content.Context;

public class CycloneMultilingualUtils {

    Context context;
    MultiLingualUtils multiLingualUtils;
    FishLandingCenter fishLandingCenter;

    public CycloneMultilingualUtils(Context context) {
        this.context = context;
        multiLingualUtils = new MultiLingualUtils();
        fishLandingCenter = new FishLandingCenter();
    }

    public String getCyclonePosition(String distance, String direction, String city, String state){
        String msg = "";
        int selectedlanguage = new LanguageNavigationHelper(context).getSelectedLanguageIndex();
        switch (selectedlanguage) {
            case 0:
                //en
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
            case 1:
                //hindi
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
            case 2:
                //gu
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
            case 3:
                //mr
                msg = multiLingualUtils.getZoneFromStringsFile(state,context)+" मधील "+fishLandingCenter.getMultiLingualFLC(city,selectedlanguage)+
                        " येथे "+multiLingualUtils.getStringResourceByName(direction,context)+
                        " या दिशेला "+multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" वर आहे";
                break;
            case 4:
                //ka
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
            case 5:
                //ml
                msg = fishLandingCenter.getMultiLingualFLC(city,selectedlanguage)+" ("+multiLingualUtils.getZoneFromStringsFile(state,context)+") " +
                        "ക്ക്  "+multiLingualUtils.getStringResourceByName(direction,context)+", "+multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" അകലെ  ";
                break;
            case 6:
                //ta
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
            case 7:
                //te
                msg =   fishLandingCenter.getMultiLingualFLC(city,selectedlanguage)+" " +
                        "("+multiLingualUtils.getZoneFromStringsFile(state,context)+") తీరం నుండి "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" దిశగా "+
                        multiLingualUtils.getDistanceWithUnits(distance,"kms",context);
                break;
            case 8:
                //or
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+fishLandingCenter.getMultiLingualFLC(city,selectedlanguage)+", "+
                        multiLingualUtils.getZoneFromStringsFile(state,context);
                break;
            case 9:
                //bn
                msg = multiLingualUtils.getDistanceWithUnits(distance,"kms",context)+" "+
                        multiLingualUtils.getStringResourceByName(direction,context)+" off "+city+", "+state;
                break;
        }
        return msg;
    }
}
