package com.example.geminiv4.devicedata;

import android.app.Service;

import com.example.geminiv4.boundarydetails.IndianBoundaryDetails;
import com.example.geminiv4.boundarydetails.NearestCountry;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class GPSInfo implements Serializable {

    private String Accuracy = "Fetching...";
    private String Altitude = "Fetching...";
    private String Date = "DD-MM-YY";
    private int Fix = 0;
    private String FixAvailable = "Fetching...";
    private String Heading;
    private String Latitude = "Fetching...";
    private String Longitude = "Fetching...";
    private String Msg;
    private String Speed="0";

    private String[] Azumith = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"};
    private String[] Elevation = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"};
    private String[] Signal2Noise = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"};
    private String[] SvId = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"};

    private String Time = "HH:MM:SS";
    private String Validity = "Fetching...";
    private String hdop = "Fetching .... ";
    private int ns;
    private String pdop = "Fetching .... ";
    private String satsInUselog = "Fetching .... ";
    private int tempNumOfSatslog;
    private String vdop = "Fetching .... ";


    private String currentregion;
    private String nearestflc;
    private String nearestflc_dis;
    private String nearestflc_dir;
    private String nearestflc_eta;



    private NearestCountry nearestCountry;
    private IndianBoundaryDetails indianBoundaryDetails;





    public String[] getSvId() {
        return this.SvId;
    }

    public void setSvId(String[] svId) {
        this.SvId = svId;
    }

    public String[] getElevation() {
        return this.Elevation;
    }

    public void setElevation(String[] elevation) {
        this.Elevation = elevation;
    }

    public String[] getAzumith() {return this.Azumith; }

    public void setAzumith(String[] azumith) {
        this.Azumith = azumith;
    }

    public String[] getSignal2Noise() {
        return this.Signal2Noise;
    }

    public void setSignal2Noise(String[] signal2Noise) {
        this.Signal2Noise = signal2Noise;
    }

    public void setFix(Integer fix) {
        this.Fix = fix.intValue();
    }

    public Integer getFix() {
        return Integer.valueOf(this.Fix);
    }

    public String getFixAvailable() {
        return this.FixAvailable;
    }

    public void setFixAvailable(String fixAvailable) {
        this.FixAvailable = fixAvailable;
    }

    public String getTime() {
        return this.Time;
    }

    public void setTime(String time) {
        Calendar c = Calendar.getInstance();

        this.Time = time;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getAltitude() {
        return this.Altitude;
    }

    public void setAltitude(String altitude) {
        this.Altitude = altitude;
    }

    public String getAccuracy() {
        return this.Accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.Accuracy = accuracy;
    }

    public String getValidity() {
        return this.Validity;
    }

    public void setValidity(String validity) {
        this.Validity = validity;
    }

    public String getMsg() {
        return this.Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }

    public void setNumOfSats(int numOfSats) {
        this.ns = numOfSats;
    }

    public int getNumOfSats() {
        return this.ns;
    }

    /**speed in knots
     * @return
     */
    public String getSpeed() {
        return this.Speed;
    }

    public String getHeading() {
        return this.Heading;
    }

    public void setSpeed(String speed) {
        this.Speed = speed;
    }

    public void setHeading(String heading) {
        this.Heading = heading;
    }

    public void setPdop(String PDOP) {
        this.pdop = PDOP;
    }

    public String getPdop() {
        return this.pdop;
    }

    public void setVdop(String VDOP) {
        this.vdop = VDOP;
    }

    public String getVdop() {
        return this.vdop;
    }

    public void setHdop(String HDOP) {
        this.hdop = HDOP;
    }

    public String getHdop() {
        return this.hdop;
    }

    public void setSatsInUse(String satsInUse) {
        this.satsInUselog = satsInUse;
    }

    public String getSatsInUse() {
        return this.satsInUselog;
    }

    public void setTempNumOfSats(int tempNumOfSats) {
        this.tempNumOfSatslog = tempNumOfSats;
    }

    public int getTempNumOfSats() {
        return this.tempNumOfSatslog;
    }


    public void setFix(int fix) {
        Fix = fix;
    }

    public String getSatsInUselog() {
        return satsInUselog;
    }

    public void setSatsInUselog(String satsInUselog) {
        this.satsInUselog = satsInUselog;
    }

    public String getCurrentregion() {
        return currentregion;
    }

    public void setCurrentregion(String currentregion) {
        this.currentregion = currentregion;
    }

    public String getNearestflc() {
        return nearestflc;
    }

    public void setNearestflc(String nearestflc) {
        this.nearestflc = nearestflc;
    }

    public String getNearestflc_dis() {
        return nearestflc_dis;
    }

    public void setNearestflc_dis(String nearestflc_dis) {
        this.nearestflc_dis = nearestflc_dis;
    }

    public String getNearestflc_dir() {
        return nearestflc_dir;
    }

    public void setNearestflc_dir(String nearestflc_dir) {
        this.nearestflc_dir = nearestflc_dir;
    }

    public String getNearestflc_eta() {
        return nearestflc_eta;
    }

    public void setNearestflc_eta(String nearestflc_eta) {
        this.nearestflc_eta = nearestflc_eta;
    }

    public NearestCountry getNearestCountry() {
        return nearestCountry;
    }

    public void setNearestCountry(NearestCountry nearestCountry) {
        this.nearestCountry = nearestCountry;
    }

    public IndianBoundaryDetails getIndianBoundaryDetails() {
        return indianBoundaryDetails;
    }

    public void setIndianBoundaryDetails(IndianBoundaryDetails indianBoundaryDetails) {
        this.indianBoundaryDetails = indianBoundaryDetails;
    }

    @Override
    public String toString() {
        return "GPSInfo{" +
                "Accuracy='" + Accuracy + '\'' +
                ", Altitude='" + Altitude + '\'' +
                ", Date='" + Date + '\'' +
                ", Fix=" + Fix +
                ", FixAvailable='" + FixAvailable + '\'' +
                ", Heading='" + Heading + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Msg='" + Msg + '\'' +
                ", Speed='" + Speed + '\'' +
                ", Time='" + Time + '\'' +
                ", Validity='" + Validity + '\'' +
                ", hdop='" + hdop + '\'' +
                ", ns=" + ns +
                ", pdop='" + pdop + '\'' +
                ", satsInUselog='" + satsInUselog + '\'' +
                ", tempNumOfSatslog=" + tempNumOfSatslog +
                ", vdop='" + vdop + '\'' +
                ", currentregion='" + currentregion + '\'' +
                ", nearestflc='" + nearestflc + '\'' +
                ", nearestflc_dis='" + nearestflc_dis + '\'' +
                ", nearestflc_dir='" + nearestflc_dir + '\'' +
                ", nearestflc_eta='" + nearestflc_eta + '\'' +
                ", nearestCountry=" + nearestCountry +
                ", indianBoundaryDetails=" + indianBoundaryDetails +
                '}';
    }
}
