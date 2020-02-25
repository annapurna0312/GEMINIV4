package com.example.geminiv4.utils;

public class Converter {


    //## For Latitude
    public String lat_DD_to_DMS(String latitude){
        if(latitude!=null && !"Fetching...".equalsIgnoreCase(latitude) && latitude.length()>0){
            return lat_DD_to_DMS(Double.parseDouble(latitude));
        }else{
            return "Fetching...";
        }
    }
    public String lat_DD_to_DMS(double latitude){
        String result = "";
        String direction = "N";
        if(latitude < 0){
            direction = "S";
        }
        result = convert(latitude) + direction;
        return result;
    }

    //## For Longitude
    public String lon_DD_to_DMS(String lon){
        if(lon!=null && !"Fetching...".equalsIgnoreCase(lon) && lon.length()>0){
            return lon_DD_to_DMS(Double.parseDouble(lon));
        }else{
            return "Fetching...";
        }
    }
    public String lon_DD_to_DMS(double longitude){
        String result = "";
        String direction = "E";
        if(longitude < 0){
            direction = "W";
        }
        result = convert(longitude) + direction;
        return result;
    }















    private String convert(double decdegree){
        double dd = Math.abs(decdegree);
        int degree = (int) dd;
        double afterdecimal = ((dd - degree) * 60);
        int mincheck = (int) afterdecimal;
        String minute;
        if(mincheck<10) {
            minute =  "0"+ roundto2DecimalPlaces(afterdecimal);
        }else {
            minute =  roundto2DecimalPlaces(afterdecimal);
        }

        String s = String.valueOf(degree)+"° "+ String.valueOf(minute)+"\'";
        return s;
    }

    public String roundto2DecimalPlaces(double value){
        return String.format("%.2f", value);
    }

    public String roundto2DecimalPlaces(String value){
        return String.format("%.2f", Double.parseDouble(value));
    }
}
