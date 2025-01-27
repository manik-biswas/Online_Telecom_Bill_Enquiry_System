package com.otbs.model;

import java.time.LocalDate;

public class Connection {
    
    private int connectionId;
    private Customer customerObj;
    private String connectionType;
    private String networkType;
    private double processingFee;
    private LocalDate activationdate;
    private Outlet outletObj;
    private String status;
    private Plan plan;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public Customer getCustomerObj() {
        return customerObj;
    }

    public void setCustomerObj(Customer customerObj) {
        this.customerObj = customerObj;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(double d) {
        this.processingFee = d;
    }

    public Outlet getOutletObj() {
        return outletObj;
    }

    public void setOutletObj(Outlet outletObj) {
        this.outletObj = outletObj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getActivationdate() {
        return activationdate;
    }

    public void setActivationdate(LocalDate activationdate) {
        this.activationdate = activationdate;
    }
}
