package com.otbs.service;

import com.otbs.exception.InvalidEntityException;
import com.otbs.model.Plan;
import com.otbs.model.Plan.PlanStatus;
import com.otbs.repository.PlanRepository;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {

    private static final Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> getAllPlans() throws InvalidEntityException {
        logger.info("Fetching all plans");
        return planRepository.findAll();
    }

    @Override
    public Optional<Plan> getPlanById(int id) throws InvalidEntityException {
        logger.info("Fetching plan by ID: {}", id);
        Optional<Plan> plan = planRepository.findById(id);
        if (plan.isEmpty()) {
            throw new InvalidEntityException("Plan with ID " + id + " does not exist.");
        }
        return plan;
        
//        return planRepository.findById(id)
//                .or(() -> {
//                    throw new InvalidEntityException("Plan with ID " + id + " does not exist.");
//                });
    }


    @Override
    public Optional<Plan> getPlanByName(String planName) throws InvalidEntityException {
    	logger.info("Fetching plans with plan name: {}", planName);
    	Optional<Plan> plan = planRepository.findByPlanName(planName);
        if (plan.isEmpty()) {
            throw new InvalidEntityException("Plan with name " + planName + " does not exist.");
        }
        return plan;
//        return planRepository.findByPlanName(planName)
//                .or(() -> {
//                    throw new InvalidEntityException("Plan with name " + planName + " does not exist.");
//                });
    }

    @Override
    public List<Plan> getPlansByFixedRate(double fixedRate) throws InvalidEntityException {
        logger.info("Fetching plans with fixed rate: {}", fixedRate);
        List<Plan> plans = planRepository.findByFixedRate(fixedRate);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found with fixed rate: " + fixedRate);
        }
        return plans;
    }

    @Override
    public List<Plan> getPlansByPlanGroup(String planGroup) throws InvalidEntityException {
        logger.info("Fetching plans in group: {}", planGroup);
        List<Plan> plans = planRepository.findByPlanGroup(planGroup);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found in group: " + planGroup);
        }
        return plans;
    }

    @Override
    public List<Plan> getPlansByDataLimit(String dataLimit) throws InvalidEntityException {
        logger.info("Fetching plans with data limit: {}", dataLimit);
        List<Plan> plans = planRepository.findByDataLimit(dataLimit);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found with data limit: " + dataLimit);
        }
        return plans;
    }
    
   
    
    @Override
    public List<Plan> getPlansByNumberOfDay(int numberOfDay) throws InvalidEntityException {
        logger.info("Fetching plans by number of days: {}", numberOfDay);
        List<Plan> plans = planRepository.findByNumberOfDay(numberOfDay);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found with " + numberOfDay + " days.");
        }
        return plans;
    }

    
    
    @Override
    public List<Plan> getPlansByStatus(PlanStatus status) throws InvalidEntityException {
        logger.info("Fetching plans with status: {}", status);
        List<Plan> plans = planRepository.findByStatus(status);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found with status: " + status);
        }
        return plans;
    }
    
//    only for the admin...
    
    @Override
    public Plan createPlan(@Valid Plan plan) throws InvalidEntityException {
        logger.info("Creating new plan: {}", plan.getPlanName());
        if (planRepository.findByPlanName(plan.getPlanName()).isPresent()) {
            throw new InvalidEntityException("Plan with name " + plan.getPlanName() + " already exists.");
        }
        return planRepository.save(plan);
    }
    
    
    
    @Override
    @Transactional
    public Plan updatePlan(int id, @Valid Plan updatedPlan) throws InvalidEntityException {
        logger.info("Updating plan with ID: {}", id);
        
        // Check if another plan with the same name exists
//        if (planRepository.existsByPlanName(updatedPlan.getPlanName())) {
//            throw new InvalidEntityException("Plan with name " + updatedPlan.getPlanName() + " already exists.");
//        }
        
        if (planRepository.existsByPlanName(updatedPlan.getPlanName()) && 
                planRepository.findById(id).map(Plan::getPlanName).equals(updatedPlan.getPlanName())) {
                throw new InvalidEntityException("Plan with name " + updatedPlan.getPlanName() + " already exists.");
            }
        
        return planRepository.findById(id).map(existingPlan -> {
            existingPlan.setPlanName(updatedPlan.getPlanName());
            existingPlan.setFixedRate(updatedPlan.getFixedRate());
            existingPlan.setDataLimit(updatedPlan.getDataLimit());
            existingPlan.setCallLimit(updatedPlan.getCallLimit());
            existingPlan.setSmsLimit(updatedPlan.getSmsLimit());
            existingPlan.setPlanGroup(updatedPlan.getPlanGroup());
            existingPlan.setNumberOfDay(updatedPlan.getNumberOfDay());
            existingPlan.setStatus(updatedPlan.getStatus());
            existingPlan.setExtraChargePerMB(updatedPlan.getExtraChargePerMB());
            existingPlan.setExtraChargePerCall(updatedPlan.getExtraChargePerCall());
            existingPlan.setExtraChargePerSMS(updatedPlan.getExtraChargePerSMS());
            return planRepository.save(existingPlan);
        }).orElseThrow(() -> new InvalidEntityException("Plan with ID " + id + " not found."));
    }

    
    @Override
    public void deletePlanById(int id) throws InvalidEntityException {
        logger.info("Deleting plan with ID: {}", id);
        
        // Check if the plan exists
        Plan plan = planRepository.findById(id)
            .orElseThrow(() -> new InvalidEntityException("Plan with ID " + id + " does not exist."));
        
        // Check if the status is active
        if (plan.getStatus() == Plan.PlanStatus.ACTIVE) {
            throw new InvalidEntityException("Plan with ID " + id + " is active and cannot be deleted.");
        }
   
        planRepository.deleteById(id);
    }


}
