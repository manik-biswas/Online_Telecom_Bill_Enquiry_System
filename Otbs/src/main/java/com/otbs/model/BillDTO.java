package com.otbs.model;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.LocalDate;

public class BillDTO {

	private int billId;
    public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getTotalAmount() {
//		return totalAmount;
		BigDecimal bd = new BigDecimal(totalAmount);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private int connectionId;
    private LocalDate date;
    private double totalAmount;
    private String status;

    public BillDTO(int billId, int connectionId, LocalDate date, double totalAmount, String status) {
        this.billId = billId;
        this.connectionId = connectionId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    
}
