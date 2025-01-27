package com.otbs.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class HelpDeskExecutive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int executiveId;

    private String username;
    private String password;

//    private String name;
//    private String email;
//    private String contactNumber;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("assignedTo") 
    private List<Complaint> complaintsAssigned;

//    // Getters and Setters
//   
//    public String getName() {
//        return name;
//    }
//
    public int getExecutiveId() {
		return executiveId;
	}

	public void setExecutiveId(int executiveId) {
		this.executiveId = executiveId;
	}
//
//	public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getContactNumber() {
//        return contactNumber;
//    }
//
//    public void setContactNumber(String contactNumber) {
//        this.contactNumber = contactNumber;
//    }

    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    public List<Complaint> getComplaintsAssigned() {
        return complaintsAssigned;
    }

    public void setComplaintsAssigned(List<Complaint> complaintsAssigned) {
        this.complaintsAssigned = complaintsAssigned;
    }
}
