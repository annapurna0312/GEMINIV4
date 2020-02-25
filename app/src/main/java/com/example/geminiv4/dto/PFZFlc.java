package com.example.geminiv4.dto;

public class PFZFlc {
    private int id;
    private double lat;
    private double lon;
    private int dir;
    private double dep;
    private double maxwh;
    private double maxws;
    private double maxcs;

    public PFZFlc(int id, double lat, double lon, int dir, double dep, double maxwh, double maxws, double maxcs) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.dir = dir;
        this.dep = dep;
        this.maxwh = maxwh;
        this.maxws = maxws;
        this.maxcs = maxcs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public double getDep() {
        return dep;
    }

    public void setDep(double dep) {
        this.dep = dep;
    }

    public double getMaxwh() {
        return maxwh;
    }

    public void setMaxwh(double maxwh) {
        this.maxwh = maxwh;
    }

    public double getMaxws() {
        return maxws;
    }

    public void setMaxws(double maxws) {
        this.maxws = maxws;
    }

    public double getMaxcs() {
        return maxcs;
    }

    public void setMaxcs(double maxcs) {
        this.maxcs = maxcs;
    }
}
