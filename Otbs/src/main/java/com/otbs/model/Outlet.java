package com.otbs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor

@Entity

public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outlet_id")
    private Integer outletId;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "availablesims", nullable = false)
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
