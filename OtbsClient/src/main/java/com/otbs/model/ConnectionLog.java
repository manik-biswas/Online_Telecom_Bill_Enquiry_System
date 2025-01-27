package com.otbs.model;

import java.time.LocalDate;

public class ConnectionLog {
    
    private int logId;
    private Connection connectionObj;
    private String connectionType;
    private String networkType;
    private String status;
    private LocalDate changedate;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Connection getConnectionObj() {
        return connectionObj;
    }

    public void setConnectionObj(Connection connectionObj) {
        this.connectionObj = connectionObj;
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

    public LocalDate getChangedate() {
        return changedate;
    }

    public void setChangedate(LocalDate changedate) {
        this.changedate = changedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
