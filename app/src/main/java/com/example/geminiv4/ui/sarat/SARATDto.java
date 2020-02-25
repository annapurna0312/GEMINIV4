package com.example.geminiv4.ui.sarat;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class SARATDto {

    String object;
    String lastknowntime;
    String validupto;
    ArrayList<Region> regions;

    String probableregions;


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getLastknowntime() {
        return lastknowntime;
    }

    public void setLastknowntime(String lastknowntime) {
        this.lastknowntime = lastknowntime;
    }

    public String getValidupto() {
        return validupto;
    }

    public void setValidupto(String validupto) {
        this.validupto = validupto;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public String getProbableregions() {
        return probableregions;
    }

    public void setProbableregions(String probableregions) {
        this.regions = new ArrayList<>();
        String[] regions = probableregions.split("\\;");
        for(String region : regions){
            this.regions.add(new Region(region));
        }
        this.probableregions = probableregions;
    }

    @Override
    public String toString() {
        return "SARATDto{" +
                "object='" + object + '\'' +
                ", lastknowntime='" + lastknowntime + '\'' +
                ", validupto='" + validupto + '\'' +
                ", regions=" + regions +
                ", probableregions='" + probableregions + '\'' +
                '}';
    }
}


class Region{

    String probability;
    ArrayList<GeoPoint> region;

    public Region(String data){
        region = new ArrayList<>();
        probability = data.split("\\%")[0];
        String[] latlons = data.split("\\%")[1].split("\\,");
        for(int i = 0 ; i < latlons.length ; i = i+2 ){
            region.add(new GeoPoint(Double.parseDouble(latlons[i+1]),Double.parseDouble(latlons[i])));
        }
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public ArrayList<GeoPoint> getRegion() {
        return region;
    }

    public void setRegion(ArrayList<GeoPoint> region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Region{" +
                "probability='" + probability + '\'' +
                ", region=" + region +
                '}';
    }
}
