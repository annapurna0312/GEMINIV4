package com.example.geminiv4.utils;


import android.content.Context;
import android.location.Location;

import com.example.geminiv4.multilingual.MultiLingualUtils;

public class LatLonUtils {



    public static boolean isLatValid(String lat){
        try{
            Double.parseDouble(lat);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public static boolean isLonValid(String lat){
        try{
            Double.parseDouble(lat);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public double getDirection(String lat1, String lon1, String lat2, String lon2){
        return getDirection(Double.parseDouble(lat1), Double.parseDouble(lon1), Double.parseDouble(lat2), Double.parseDouble(lon2));
    }
    public double getDirection(double lat1, double lon1, double lat2, double lon2){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);
        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);
        return startPoint.bearingTo(endPoint);
    }


    public double getDistance(String lat1, String lon1, String lat2, String lon2){
        return getDistance(Double.parseDouble(lat1), Double.parseDouble(lon1), Double.parseDouble(lat2), Double.parseDouble(lon2));
    }
    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);
        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);
        return startPoint.distanceTo(endPoint);
    }



    public String parseCardinalDirection(String angle, Context context) {
        String parsed = "";
        if(angle.toLowerCase().contains("to")){
            String fang = getDirectionAcronyms(angle.toLowerCase().split("to")[0].trim());
            String tang = getDirectionAcronyms(angle.toLowerCase().split("to")[0].trim());
            parsed = getCardinalDirectionName(fang,context)+"-"+getCardinalDirectionName(tang,context);
        }else{
            parsed = getCardinalDirectionName(angle,context);
        }
        return parsed;
    }

    private String getCardinalDirectionName(String angle, Context context){
        String parsed = "";
        MultiLingualUtils multiLingualUtils = new MultiLingualUtils();
        switch (angle){
            case "n":
                parsed = multiLingualUtils.getStringResourceByName("n",context);
                break;
            case "ne":
                parsed = multiLingualUtils.getStringResourceByName("ne",context);
                break;
            case "e":
                parsed = multiLingualUtils.getStringResourceByName("e",context);
                break;
            case "se":
                parsed = multiLingualUtils.getStringResourceByName("se",context);
                break;
            case "s":
                parsed = multiLingualUtils.getStringResourceByName("s",context);
                break;
            case "sw":
                parsed = multiLingualUtils.getStringResourceByName("sw",context);
                break;
            case "w":
                parsed = multiLingualUtils.getStringResourceByName("w",context);
                break;
            case "nw":
                parsed = multiLingualUtils.getStringResourceByName("nw",context);
                break;
        }
        return parsed;
    }

    private String getDirectionAcronyms(String to) {
        to = to.replaceAll("erly","");
        to = to.replaceAll("south","s");
        to = to.replaceAll("north","n");
        to = to.replaceAll("west","w");
        to = to.replaceAll("east","e");
        to = to.substring(0,1);
        return to;
    }


}


