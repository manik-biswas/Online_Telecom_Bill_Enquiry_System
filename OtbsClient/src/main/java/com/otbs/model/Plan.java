package com.otbs.model;
import java.util.List;

public class Plan {

    private int planId;
    private String planName;
    private double fixedRate;
    private String dataLimit;
    private String callLimit;
    private String smsLimit;
    private String planGroup;
    private int numberOfDay;
    private PlanStatus status;
    private float extraChargePerMB;
    private float extraChargePerCall;
    private float extraChargePerSMS;
  
//    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Connection> connections;
    
    
    
    public int getPlanId() {
		return planId;
	}



	public void setPlanId(int planId) {
		this.planId = planId;
	}



	public String getPlanName() {
		return planName;
	}



	public void setPlanName(String planName) {
		this.planName = planName;
	}



	public double getFixedRate() {
		return fixedRate;
	}



	public void setFixedRate(double fixedRate) {
		this.fixedRate = fixedRate;
	}



	public String getDataLimit() {
		return dataLimit;
	}



	public void setDataLimit(String dataLimit) {
		this.dataLimit = dataLimit;
	}



	public String getCallLimit() {
		return callLimit;
	}



	public void setCallLimit(String callLimit) {
		this.callLimit = callLimit;
	}



	public String getSmsLimit() {
		return smsLimit;
	}



	public void setSmsLimit(String smsLimit) {
		this.smsLimit = smsLimit;
	}



	public String getPlanGroup() {
		return planGroup;
	}



	public void setPlanGroup(String planGroup) {
		this.planGroup = planGroup;
	}



	public int getNumberOfDay() {
		return numberOfDay;
	}



	public void setNumberOfDay(int numberOfDay) {
		this.numberOfDay = numberOfDay;
	}



	public PlanStatus getStatus() {
		return status;
	}



	public void setStatus(PlanStatus status) {
		this.status = status;
	}



	public float getExtraChargePerMB() {
		return extraChargePerMB;
	}



	public void setExtraChargePerMB(float extraChargePerMB) {
		this.extraChargePerMB = extraChargePerMB;
	}



	public float getExtraChargePerCall() {
		return extraChargePerCall;
	}



	public void setExtraChargePerCall(float extraChargePerCall) {
		this.extraChargePerCall = extraChargePerCall;
	}



	public float getExtraChargePerSMS() {
		return extraChargePerSMS;
	}



	public void setExtraChargePerSMS(float extraChargePerSMS) {
		this.extraChargePerSMS = extraChargePerSMS;
	}



	public List<Connection> getConnections() {
		return connections;
	}



	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}



	public enum PlanStatus {
        ACTIVE,
        INACTIVE
    }
    
}

