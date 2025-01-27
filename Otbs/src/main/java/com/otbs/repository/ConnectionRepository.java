//package com.otbs.repository;
//
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.otbs.model.Connection;
//import com.otbs.model.*;
//
//@Repository
//public interface ConnectionRepository extends JpaRepository<Connection, Integer>{
//
//	
//	List<Connection> findByCustomerObjCustomerId(int customerId);
//
//    // Or use a custom JPQL query if needed
//    @Query("SELECT c FROM Connection c WHERE c.customerObj.customerId = :customerId")
//    List<Connection> findConnectionsByCustomerId(@Param("customerId") int customerId);
//	
//}
//


package com.otbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.otbs.model.Connection;
import com.otbs.model.*;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer>{
	List<Connection> findByCustomerObjCustomerId(int customerId);

    // Or use a custom JPQL query if needed
    @Query("SELECT c FROM Connection c WHERE c.customerObj.customerId = :customerId")
    List<Connection> findConnectionsByCustomerId(@Param("customerId") int customerId);
}

