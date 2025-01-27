package com.otbs.model;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;

    @ManyToOne
    @JoinColumn(name = "connectionId")
    @JsonIgnoreProperties("complaintsRaised")
    private Connection connection;

    @ManyToOne
    @JoinColumn(name = "executiveId")
    @JsonIgnoreProperties("complaintsAssigned")
    private HelpDeskExecutive assignedTo;

    private String complaintType;
    private String description;
    private String status;
    private String priority;
    private Date createdDate;
    private Date resolvedDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedbackId")
    private Feedback feedback;

	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public HelpDeskExecutive getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(HelpDeskExecutive assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getComplaintType() {
		return complaintType;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

    // Getters and Setters for all fields
}