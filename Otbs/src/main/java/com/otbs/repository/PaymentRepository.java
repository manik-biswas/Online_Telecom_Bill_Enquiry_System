package com.otbs.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.otbs.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer>{

	@Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Double calculateRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
