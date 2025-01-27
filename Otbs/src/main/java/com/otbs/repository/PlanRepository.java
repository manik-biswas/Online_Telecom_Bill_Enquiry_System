package com.otbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otbs.model.Plan;
import com.otbs.model.Plan.PlanStatus;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    // Custom query method to find by plan name
    Optional<Plan> findByPlanName(String planName);
    
    // Custom method for searching by fixedRate
    List<Plan> findByFixedRate(double fixedRate);
    
    // Custom method for searching by group of plan
    List<Plan> findByPlanGroup(String planGroup);
    
   // Custom method for searching by dataLimit
    List<Plan> findByDataLimit(String dataLimit);
    
   // Custom method for searching based on the numberOfDay
    List<Plan> findByNumberOfDay(int numberOfDay);
    
   // Custom method for searching by plan status
    List<Plan> findByStatus(PlanStatus status);
    
    boolean existsByPlanName(String planName);
}
