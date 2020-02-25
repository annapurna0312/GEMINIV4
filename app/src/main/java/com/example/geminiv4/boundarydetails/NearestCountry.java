package com.example.geminiv4.boundarydetails;

public class NearestCountry {
	
	private String name;
	private String distnce;
	private double clat;
	private double clon;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistnce() {
		return distnce;
	}
	public void setDistnce(String distnce) {
		this.distnce = distnce;
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "NearestCountry [name=" + name + ", distnce=" + distnce + ", clat=" + clat + ", clon=" + clon + "]";
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

}
