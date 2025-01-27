package com.otbs.controller;

import com.otbs.model.Complaint;
import com.otbs.model.Feedback;
import com.otbs.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @PostMapping
    public Complaint addComplaint(@RequestBody Complaint complaint) {
        return complaintService.addComplaint(complaint);
    }

    @DeleteMapping("/{id}")
    public String deleteComplaint(@PathVariable int id) {
        complaintService.deleteComplaint(id);
        return "Complaint with ID " + id + " deleted.";
    }

    @PutMapping("/update")
    public Complaint updateComplaint(@RequestBody Complaint complaint) {
        return complaintService.updateComplaint(complaint);
    }

    @GetMapping("/{id}")
    public Complaint viewComplaint(@PathVariable int id) {
        return complaintService.viewComplaint(id);
    }

    @PostMapping(value = "/resolve", consumes = "application/x-www-form-urlencoded")
    public Complaint resolveComplaint(
            @RequestParam int complaintId,
            @RequestParam int executiveId,
            @RequestParam String resolutionDetails) {
        return complaintService.solveComplaint(complaintId, executiveId, resolutionDetails);
    }

    @GetMapping("/history/{connectionId}")
    public List<Complaint> viewComplaintHistory(@PathVariable int connectionId) {
        return complaintService.viewComplaintHistory(connectionId);
    }

    @PostMapping("/escalate/{id}")
    public Complaint escalateComplaint(@PathVariable int id) {
        return complaintService.escalateComplaint(id);
    }

    @PostMapping("/{id}/feedback")
    public Feedback submitFeedback(@PathVariable int id, @RequestBody Feedback feedback) {
        return complaintService.submitFeedback(id, feedback);
    }
}