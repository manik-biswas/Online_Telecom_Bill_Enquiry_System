package com.otbs.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.otbs.exception.InvalidEntityException;
import com.otbs.model.Plan;
import com.otbs.model.Plan.PlanStatus;


public interface PlanService {

    List<Plan> getAllPlans() throws InvalidEntityException;

    Optional<Plan> getPlanById(int id) throws InvalidEntityException;
    
    Optional<Plan> getPlanByName(String planName) throws InvalidEntityException;
    
    List<Plan> getPlansByFixedRate(double fixedRate) throws InvalidEntityException;
    
    List<Plan> getPlansByPlanGroup(String planGroup) throws InvalidEntityException;
    
    List<Plan> getPlansByDataLimit(String dataLimit) throws InvalidEntityException;
    
    List<Plan> getPlansByNumberOfDay(int numberOfDay) throws InvalidEntityException;
    
    List<Plan> getPlansByStatus(PlanStatus status) throws InvalidEntityException;
    
//    for the admin part
    
    Plan createPlan(Plan plan) throws InvalidEntityException;
    
    Plan updatePlan(int id, Plan updatedPlan) throws InvalidEntityException;
    
    void deletePlanById(int id) throws InvalidEntityException;

}