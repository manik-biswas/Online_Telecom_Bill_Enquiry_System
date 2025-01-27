package com.otbs.service;

import com.otbs.model.Complaint;
import com.otbs.model.Feedback;
import com.otbs.model.HelpDeskExecutive;
import com.otbs.repository.ComplaintRepository;
import com.otbs.repository.FeedbackRepository;
import com.otbs.repository.HelpDeskExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private HelpDeskExecutiveRepository helpDeskExecutiveRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint addComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint(int complaintId) {
        complaintRepository.deleteById(complaintId);
    }

    public Complaint updateComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public Complaint viewComplaint(int id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint with ID " + id + " not found"));
    }

    public List<Complaint> viewComplaintHistory(int connectionId) {
        return complaintRepository.findByConnection_ConnectionId(connectionId);
    }

    public Complaint solveComplaint(int complaintId, int executiveId, String resolutionDetails) {
        Complaint complaint = viewComplaint(complaintId);
        if (complaint.getAssignedTo().getExecutiveId() != executiveId) {
            throw new IllegalStateException("Complaint not assigned to this executive!");
        }
        complaint.setStatus("Resolved");
        complaint.setResolvedDate(new Date());
        complaint.setDescription(resolutionDetails);
        return complaintRepository.save(complaint);
    }

    public Complaint escalateComplaint(int id) {
        Complaint complaint = viewComplaint(id);
        if (!"Resolved".equals(complaint.getStatus())) {
            HelpDeskExecutive newExecutive = helpDeskExecutiveRepository
                    .findById(complaint.getAssignedTo().getExecutiveId() + 1)
                    .orElseThrow(() -> new RuntimeException("No higher-level executive available!"));
            complaint.setAssignedTo(newExecutive);
            complaint.setPriority("High");
            return complaintRepository.save(complaint);
        }
        throw new IllegalStateException("Complaint already resolved!");
    }

    public Feedback submitFeedback(int complaintId, Feedback feedback) {
        Complaint complaint = viewComplaint(complaintId);
        if (!"Resolved".equals(complaint.getStatus())) {
            throw new IllegalStateException("Cannot submit feedback for unresolved complaints!");
        }
        Feedback savedFeedback = feedbackRepository.save(feedback);
        complaint.setFeedback(savedFeedback);
        complaintRepository.save(complaint);
        return savedFeedback;
    }
}