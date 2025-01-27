package com.otbs.controller;

import com.otbs.exception.InvalidEntityException;
import com.otbs.model.Plan;
import com.otbs.model.Plan.PlanStatus;
import com.otbs.service.PlanService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Plan>> getAllPlans() throws InvalidEntityException {
        List<Plan> plans = planService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/getId/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable int id) throws InvalidEntityException {
        return ResponseEntity.ok(planService.getPlanById(id).orElse(null));
    }

    @GetMapping("/getName/{planName}")
    public ResponseEntity<Plan> getPlanByName(@PathVariable String planName) throws InvalidEntityException {
        return ResponseEntity.ok(planService.getPlanByName(planName).orElse(null));
    }

    @GetMapping("/fixed-rate/{fixedRate}")
    public ResponseEntity<List<Plan>> getPlansByFixedRate(@PathVariable double fixedRate) throws InvalidEntityException {
        List<Plan> plans = planService.getPlansByFixedRate(fixedRate);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/plan-group/{planGroup}")
    public ResponseEntity<List<Plan>> getPlansByPlanGroup(@PathVariable String planGroup) throws InvalidEntityException {
        List<Plan> plans = planService.getPlansByPlanGroup(planGroup);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/data-limit/{dataLimit}")
    public ResponseEntity<List<Plan>> getPlansByDataLimit(@PathVariable String dataLimit) throws InvalidEntityException {
        List<Plan> plans = planService.getPlansByDataLimit(dataLimit);
        return ResponseEntity.ok(plans);
    }

    
    @GetMapping("/days/{numberOfDay}")
    public ResponseEntity<List<Plan>> getPlansByNumberOfDay(@PathVariable int numberOfDay) throws InvalidEntityException {
        List<Plan> plans = planService.getPlansByNumberOfDay(numberOfDay);
        if (plans.isEmpty()) {
            throw new InvalidEntityException("No plans found with " + numberOfDay + " days.");
        }
        return ResponseEntity.ok(plans);
    }


 
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Plan>> getPlansByStatus(@PathVariable PlanStatus status) throws InvalidEntityException {
        List<Plan> plans = planService.getPlansByStatus(status);
        return ResponseEntity.ok(plans);
    }
    
    
//    for the admin...

    @PostMapping("/add")
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) throws InvalidEntityException {
        Plan createdPlan = planService.createPlan(plan);
        return ResponseEntity.status(201).body(createdPlan);
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable int id, @Valid @RequestBody Plan updatedPlan) throws InvalidEntityException {
        Plan plan = planService.updatePlan(id, updatedPlan);
        return ResponseEntity.ok(plan);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deletePlan(@PathVariable int id) throws InvalidEntityException {
        planService.deletePlanById(id);
        return ResponseEntity.ok("Plan with ID " + id + " deleted successfully.");
    }
}
