package com.example.geminiv4.devicedata;

import java.io.Serializable;

public class IncoisMessage implements Serializable {

    private int mid;
    private String message;
    private int framescount;
    private boolean alert;
    private int priority;
    private int test;
    private String expiration;
    private String arrival_date;
    private String country;
    private String messageformat;
    private String version;
    private boolean template;
    private boolean compression;
    private int imagetext;
    private String typeofservice;
    private boolean prn127;
    private boolean prn128;
    private boolean prn132;
    private String zone;
    private boolean complete;
    private String sparebits;
    private boolean read;


    public int getMid()
    {
        return mid;
    }

    public void setMid(int mid)
    {
        this.mid = mid;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public int getTest()
    {
        return test;
    }

    public void setTest(int test)
    {
        this.test = test;
    }

    public String getExpiration()
    {
        return expiration;
    }

    public void setExpiration(String expiration)
    {
        this.expiration = expiration;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getMessageformat()
    {
        return messageformat;
    }

    public void setMessageformat(String messageformat)
    {
        this.messageformat = messageformat;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public boolean getTemplate(){return template;}

    public void setTemplate(boolean template)
    {
        this.template = template;
    }

    public int getImagetext()
    {
        return imagetext;
    }

    public void setImagetext(int imagetext)
    {
        this.imagetext = imagetext;
    }

    public String getTypeofservice()
    {
        return typeofservice;
    }

    public void setTypeofservice(String typeofservice)
    {
        this.typeofservice = typeofservice;
    }

    public String getZone()
    {
        return zone;
    }

    public void setZone(String zone)
    {
        this.zone = zone;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public int getFramescount() {return framescount;}

    public void setFramescount(int framescount) {this.framescount = framescount;}

    public boolean isPrn127() {return prn127;}

    public void setPrn127(boolean prn127) {this.prn127 = prn127;}

    public boolean isPrn128() {return prn128;}

    public void setPrn128(boolean prn128) {this.prn128 = prn128;}

    public boolean isPrn132() {return prn132;}

    public void setPrn132(boolean prn132) {this.prn132 = prn132;}

    public boolean isComplete() {return complete;}

    public void setComplete(boolean complete) {this.complete = complete;}


    @Override
    public String toString() {
        return "IncoisMessage{" +
                "mid=" + mid +
                ", message='" + message + '\'' +"\n"+
                ", framescount=" + framescount +"\n"+
                ", priority=" + priority +"\n"+
                ", test=" + test +"\n"+
                ", expiration='" + expiration + '\'' +"\n"+
                ", arrival_date='" + arrival_date + '\'' +"\n"+
                ", country='" + country + '\'' +"\n"+
                ", messageformat='" + messageformat + '\'' +"\n"+
                ", version='" + version + '\'' +"\n"+
                ", template=" + template +"\n"+
                ", imagetext=" + imagetext +"\n"+
                ", typeofservice='" + typeofservice + '\'' +"\n"+
                ", prn127=" + prn127 +"\n"+
                ", prn128=" + prn128 +"\n"+
                ", prn132=" + prn132 +"\n"+
                ", zone='" + zone + '\'' +"\n"+
                ", complete=" + complete +"\n"+
                '}';
    }

    public String getSparebits() {
        return sparebits;
    }

    public void setSparebits(String sparebits) {
        this.sparebits = sparebits;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isTemplate() {
        return template;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
