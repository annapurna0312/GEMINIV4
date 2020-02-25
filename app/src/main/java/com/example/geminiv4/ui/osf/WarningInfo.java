package com.example.geminiv4.ui.osf;

import com.example.geminiv4.R;

import java.util.HashMap;

public class WarningInfo {

    private String zid,state,wh1,wh2,cs1,cs2,fromloc,toloc,advice;
    private int image;

    public WarningInfo(String zid, String state, String wh1, String wh2, String cs1, String cs2, String fromloc, String toloc, String advice) {
        setZid(zid);
        this.state = state;
        this.wh1 = wh1;
        this.wh2 = wh2;
        this.cs1 = cs1;
        this.cs2 = cs2;
        this.fromloc = fromloc;
        this.toloc = toloc;
        this.advice = advice;
    }

    public WarningInfo() {
    }


    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
        switch (Integer.parseInt(zid)){
            case 1 :
                setState("Gujarat");
                setImage(R.drawable.gujarat_image);
                break;
            case 2 :
                setState("Maharashtra");
                setImage(R.drawable.maharashtra_image);
                break;
            case 3 :
                setState("Goa");
                setImage(R.drawable.goa_image);
                break;
            case 4 :
                setState("Karnataka");
                setImage(R.drawable.karnataka_image);
                break;
            case 5 :
                setState("Kerala");
                setImage(R.drawable.kerala_image);
                break;
            case 6 :
                setState("SouthTamilNadu");
                setImage(R.drawable.southtamilnadu);
                break;
            case 7 :
                setState("NorthTamilNadu");
                setImage(R.drawable.northtamilnadu);
                break;
            case 8 :
                setState("SouthAndhraPradesh");
                setImage(R.drawable.southandhrapradesh);
                break;
            case 9 :
                setState("NorthAndhraPradesh");
                setImage(R.drawable.northandhrapradesh);
                break;
            case 10 :
                setState("Odisha");
                setImage(R.drawable.odisha);
                break;
            case 11 :
                setState("WestBengal");
                setImage(R.drawable.westbengal);
                break;
            case 12 :
                setState("Andaman");
                setImage(R.drawable.andaman_image);
                break;
            case 13 :
                setState("Nicobar");
                setImage(R.drawable.andaman_image);
                break;


        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWh1() {
        return wh1;
    }

    public void setWh1(String wh1) {
        this.wh1 = wh1;
    }

    public String getWh2() {
        return wh2;
    }

    public void setWh2(String wh2) {
        this.wh2 = wh2;
    }

    public String getCs1() {
        return cs1;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    public String getFromloc() {
        return fromloc;
    }

    public void setFromloc(String fromloc) {
        this.fromloc = fromloc;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



}
