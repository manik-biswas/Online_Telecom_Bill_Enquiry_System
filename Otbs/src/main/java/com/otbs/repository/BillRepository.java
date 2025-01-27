package com.otbs.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.otbs.model.Bill;
import com.otbs.model.Connection;
import com.otbs.model.UsageInfo;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findByConnectionAndUsage(Connection connection, UsageInfo usage);

    @Query("SELECT b FROM Bill b WHERE b.connection.customerObj.customerId = :customerId")
    List<Bill> findAllBillsByCustomerId(@Param("customerId") int customerId);

    // Fetch unpaid bills for a specific customer
    @Query("SELECT b FROM Bill b WHERE b.connection.customerObj.customerId = :customerId AND b.status = 'unpaid'")
    List<Bill> findUnpaidBillsByCustomerId(@Param("customerId") int customerId);

    @Query("SELECT b FROM Bill b JOIN b.connection c WHERE c.customerObj.customerId = :customerId AND b.date >= :startDate")
    List<Bill> findBillsByCustomerIdAndDateAfter(@Param("customerId") int customerId, @Param("startDate") LocalDate startDate);

    Bill findByBillId(int billId);
    
    @Query("SELECT b FROM Bill b WHERE b.status = :status")
    List<Bill> findAllBillsByStatus(@Param("status") String status);
    
    @Query("SELECT b FROM Bill b WHERE b.status = :status And b.dueDate < :startDate ")
    List<Bill> findAllBillsCrossDueDate(@Param("status") String status, @Param("startDate") LocalDate startDate);


}
