package com.otbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otbs.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
//	@Query("SELECT c FROM Customer c WHERE c.username = :username")
	Customer findByUsername(String username);
    Customer findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT c.customerId FROM Customer c WHERE c.username = :username")
    int findcustomerid(String username);
    
}
