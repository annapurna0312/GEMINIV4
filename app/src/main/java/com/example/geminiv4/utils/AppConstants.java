package com.example.geminiv4.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by srikanth on 03/11/18.
 */

public class AppConstants {

    public static final String DATA = "Data";
    public static final String SERVICE_TYPE = "Service_Type";
    public static final String SELECTED_LANGUAGE = "SelectedLanguage";
    public static final String INCIOS_MESSAGE = "InciosMessage";

    public static final String CURRENTLAT = "currentlat";
    public static final String CURRENTLON = "currentlon";


    public static final DateFormat display_dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    public static final DateFormat display_timeformat = new SimpleDateFormat("HH:mm");


    public static final String PFZ_LINES_DELIM = "#";
    public static final String PFZ_POINTS_DELIM = ";";
    public static final String PFZ_LATLONDEPTH_DELIM = ",";
}
