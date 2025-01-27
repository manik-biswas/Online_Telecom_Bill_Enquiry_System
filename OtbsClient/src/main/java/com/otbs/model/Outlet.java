package com.otbs.model;

public class Outlet {

 
    private Integer outletId;

    private String location;

    private Integer availableSIMs = 50; 

	public Integer getOutletId() {
		return outletId;
	}

	public void setOutletId(Integer outletId) {
		this.outletId = outletId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getAvailableSIMs() {
		return availableSIMs;
	}

	public void setAvailableSIMs(Integer availableSIMs) {
		this.availableSIMs = availableSIMs;
	}
}

