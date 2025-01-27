package com.otbs.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paymentId;
	
	@OneToOne
	@JoinColumn(name="bill_id")
	private Bill billId;
	private double amount;
	private String paymentMethod;
	private LocalDate paymentDate;
	private String transactionId;

	
	//Getter and setter
	
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public Bill getBillId() {
		return billId;
	}
	public void setBillId(Bill billId) {
		this.billId = billId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public LocalDate getpaymentDate() {
		return paymentDate;
	}
	public void setpaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
