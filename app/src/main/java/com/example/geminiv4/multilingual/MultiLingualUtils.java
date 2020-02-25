package com.example.geminiv4.multilingual;

import android.content.Context;

import com.example.geminiv4.R;

public class MultiLingualUtils {


    public String getServiceHeading(String typeofservice, Context context) {
        String ret = "";
        if (typeofservice.equalsIgnoreCase("OSF")) {
            ret = context.getString(R.string.High_Wave_warning);
        }
        if (typeofservice.equalsIgnoreCase("CYCLONE")) {
            ret = context.getString(R.string.cardview_cyclone_title);
        }
        if (typeofservice.equalsIgnoreCase("PFZ")) {
            ret = context.getString(R.string.Potential_Fishing_Zone);
        }
        if (typeofservice.equalsIgnoreCase("TSUNAMI")) {
            ret = context.getString(R.string.tsunami_alert);
        }
        if (typeofservice.equalsIgnoreCase("STRONGWINDALERT")) {
            ret = context.getString(R.string.strong_wind_alert);
        }
        return ret;
    }


    /** Given any string value get the multilingual text
     * @param aString
     * @param context
     * @return
     */
    public String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return aString;
        } else {
            return context.getString(resId);
        }
    }


    /** Given a zone in english, the method returns the zone in the selected language
     * @param zone_en
     * @param context
     * @return
     */
    public String getZoneFromStringsFile(String zone_en, Context context) {
        int index = -1;
        String[] zones = context.getResources().getStringArray(R.array.region_array_en);
        for (int l = 0; l < zones.length; l++) {
            if (zones[l].equalsIgnoreCase(zone_en)) {
                index = l;
                break;
            }
        }
        String[] retstr = context.getResources().getStringArray(R.array.region_array);
        if (index == -1) {
            return zone_en;
        } else {
            return retstr[index];
        }
    }




    public String getDistanceWithUnits(String distance_in_kms, String units, Context context){
        return getDistanceWithUnits(Double.parseDouble(distance_in_kms),units,context);
    }
    public String getDistanceWithUnits(double distance_in_kms, String units, Context context){
        StringBuffer ret = new StringBuffer();
        if(units.equalsIgnoreCase("kms")){
            ret.append(distance_in_kms+" "+new MultiLingualUtils().getStringResourceByName("kms",context));
        }
        return ret.toString();
    }

    public String getCurrentSpeedWithUnits(double cs_in_kms, String units, Context context){
        StringBuffer ret = new StringBuffer();
        if(units.equalsIgnoreCase("cmps")){
            ret.append(cs_in_kms+" "+new MultiLingualUtils().getStringResourceByName("cmps",context));
        }
        return ret.toString();
    }

}

