package com.otbs.model;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UsageInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int usageId;
	
	@ManyToOne
	@JoinColumn(name = "connectionId",nullable=false)
	private Connection connection;
	private int calls;
	private int dataUsed;
	private int sms;
	private int roaming;
	private int internationalCalls;
public int getUsageId() {
		return usageId;
	}
	public void setUsageId(int usageId) {
		this.usageId = usageId;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public int getCalls() {
		return calls;
	}
	public void setCalls(int calls) {
		this.calls = calls;
	}
	
	public int getDataUsed() {
		return dataUsed;
	}
	public void setDataUsed(int dataUsed) {
		this.dataUsed = dataUsed;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}


public int getRoaming() {
	return roaming;
}
public void setRoaming(int roaming) {
	this.roaming = roaming;
}
public int getInternationalCalls() {
	return internationalCalls;
}
public void setInternationalCalls(int internationalCalls) {
	this.internationalCalls = internationalCalls;
}
private LocalDate date;
public LocalDate getDate() {
	return date;
}
public void setDate(LocalDate date) {
	this.date = date;
}

}
