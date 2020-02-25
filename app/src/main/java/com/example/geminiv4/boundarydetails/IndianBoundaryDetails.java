package com.example.geminiv4.boundarydetails;

public class IndianBoundaryDetails {

    private String region;
    private boolean isInside = false;
    private double clat;
    private double clon;
    private String distance;
    private String nearestregion;


    public String getNearestregion() {
        return nearestregion;
    }

    public void setNearestregion(String nearestregion) {
        this.nearestregion = nearestregion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public double getClat() {
        return clat;
    }

    public void setClat(double clat) {
        this.clat = clat;
    }

    public double getClon() {
        return clon;
    }

    public void setClon(double clon) {
        this.clon = clon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "IndianBoundaryDetails [region=" + region + ", isInside=" + isInside + ", clat=" + clat + ", clon="
                + clon + ", distance=" + distance + ", nearestregion=" + nearestregion + "]";
    }


}
