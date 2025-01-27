package com.otbs.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int connectionId;

    // Many connections can belong to one customer
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"connections"}) // Prevent circular references for customer
    @NotNull(message = "Customer object cannot be null")
    private Customer customerObj;

    @NotBlank(message = "Connection type cannot be blank")
    @Column(nullable = false)
    private String connectionType;
   
    @NotBlank(message = "Network type cannot be blank")
    private String networkType;

    private double processingFee;

    @NotNull(message = "Activation date cannot be null")
    @JsonProperty("activationdate") // Ensure it maps correctly in JSON
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationdate;

    // Many connections can be associated with one outlet
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outlet_id", nullable = false)
    @NotNull(message = "Outlet object cannot be null")
    @JsonIgnoreProperties({"connections"}) // Prevent circular references for outlet
    private Outlet outletObj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planId")
    @JsonIgnoreProperties({"connections"}) // Prevent circular references for plan
    private Plan plan;

    @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("connection") // Prevent circular references for complaints
    private List<Complaint> complaintsRaised;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    // Getters and Setters

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

    public void setProcessingFee(double processingFee) {
        this.processingFee = processingFee;
    }

    public LocalDate getActivationdate() {
        return activationdate;
    }

    public void setActivationdate(LocalDate activationdate) {
        this.activationdate = activationdate;
    }

    public Outlet getOutletObj() {
        return outletObj;
    }

    public void setOutletObj(Outlet outletObj) {
        this.outletObj = outletObj;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public List<Complaint> getComplaintsRaised() {
        return complaintsRaised;
    }

    public void setComplaintsRaised(List<Complaint> complaintsRaised) {
        this.complaintsRaised = complaintsRaised;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
