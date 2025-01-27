package com.otbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Complaint {
	 private int id;
    private int complaintId;
    private String complaintType;
    private String description;
    private String priority;
    private String status;
    private String resolutionDetails;
    
//    public Complaint(Integer id, String complaintType, String description, String priority) {
//        this.id = id;
//        this.complaintType = complaintType;
//        this.description = description;
//        this.priority = priority;
//    }

    // Getters and Setters
    
    

    

    public String getComplaintType() {
		return complaintType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolutionDetails() {
        return resolutionDetails;
    }

    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
    
    
}
