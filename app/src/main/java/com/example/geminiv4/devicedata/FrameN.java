package com.example.geminiv4.devicedata;

public class FrameN {
    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getFrmid() {
        return frmid;
    }

    public void setFrmid(int frmid) {
        this.frmid = frmid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    int msgid;
    int frmid;

    @Override
    public String toString() {
        return "FrameN{" +
                "msgid=" + msgid +
                ", frmid=" + frmid +
                ", message='" + message + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                '}';
    }

    String message;

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    String arrival_date;


}
