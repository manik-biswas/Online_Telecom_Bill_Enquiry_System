package com.otbs.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.otbs.model.Complaint;
import com.otbs.model.Feedback;
import com.otbs.model.Plan;

@Controller
public class ComplaintClientController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.complaint.base.url}")
    private String complaintBaseUrl;
    
    @GetMapping("/complaintmanagement")
	public String complaintmanagementPage() {
		return "complaintmanagement";
	}

    // Render the complaint submission page
    @GetMapping("/addComplaint")
    public String showComplaintPage(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "addComplaint";  // Thymeleaf template for submitting a complaint
    }

    // Handle the complaint submission
    @PostMapping("/addComplaint")
    public String registerComplaint(@ModelAttribute Complaint complaint, Model model) {
        try {
            // Send the complaint to the backend API
            Complaint response = restTemplate.postForObject(complaintBaseUrl , complaint, Complaint.class);

            if (response != null) {
                model.addAttribute("message", "Complaint submitted successfully!");
                model.addAttribute("complaint", response);
                return "complaint-success";  // Redirect to success page
            } else {
                model.addAttribute("error", "Failed to submit complaint");
                return "addComplaint";  // Return to the complaint form with an error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while submitting the complaint: " + e.getMessage());
            return "addComplaint";  // Return to the form with an error message
        }
    }

    // Render the View All Complaints Page
    @GetMapping("/viewAllComplaints")
    public String viewAllComplaints(Model model) {
        try {
            // Fetch all complaints from the backend API
            Complaint[] complaintsArray = restTemplate.getForObject(complaintBaseUrl , Complaint[].class);
            List<Complaint> complaints = Arrays.asList(complaintsArray);

            model.addAttribute("complaints", complaints);
            return "viewallcomplaints";  // Thymeleaf template for viewing all complaints
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching complaints: " + e.getMessage());
            return "viewallcomplaints";
        }
    }

   
    
    @PostMapping("/updateComplaint")
    public String updateComplaint(@ModelAttribute Complaint complaint, Model model) {
        try {
            // Send the updated complaint details to the backend API
            String url = complaintBaseUrl + "/update";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Complaint> request = new HttpEntity<>(complaint, headers);
            ResponseEntity<Complaint> response = restTemplate.exchange(url, HttpMethod.PUT, request, Complaint.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Complaint updated successfully!");
                return "update-complaint-success"; // Redirect to a success page
            } else {
                model.addAttribute("error", "Failed to update complaint");
                return "updateComplaint"; // Return to the update form with an error
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the complaint: " + e.getMessage());
            return "updateComplaint"; // Return to the form with an error
        }
    }





   
    
 // Render the Complaint Delete Page
    @GetMapping("/deleteComplaint")
    public String getDeleteComplaintPage() {
        return "deleteComplaint"; // Thymeleaf template for deleting a complaint
    }

    // Handle Complaint Deletion
    @PostMapping("/deleteComplaint")
    public String deleteComplaint(@RequestParam Integer complaintId, Model model) {
        try {
            // Construct the full backend URL for the delete request
            String deleteUrl = complaintBaseUrl + "/" + complaintId;

            // Make the DELETE request to the backend API
            restTemplate.delete(deleteUrl);

            // Add success message to the model
            model.addAttribute("message", "Complaint with ID " + complaintId + " deleted successfully!");
            return "delete-complaint-success"; // Redirect to the success page
        } catch (Exception e) {
            // Handle exceptions and add error message to the model
            model.addAttribute("error", "An error occurred while deleting the complaint: " + e.getMessage());
            return "deleteComplaint"; // Return to the delete page with the error message
        }
    }

    @GetMapping("/resolveComplaint")
    public String showResolveComplaintPage() {
        return "resolveComplaint";  // Thymeleaf template for resolving complaints
    }

    // Handle the Resolve Complaint Submission
    @PostMapping("/resolveComplaint")
    public String resolveComplaint(@RequestParam Integer complaintId, 
                                   @RequestParam Integer executiveId, 
                                   @RequestParam String resolutionDetails, 
                                   Model model) {
        try {
            // Send the resolution details to the backend API for resolution
            String url = complaintBaseUrl ;
            ResponseEntity<String> response = restTemplate.postForEntity(url, 
                new ResolutionRequest(executiveId, resolutionDetails), String.class, complaintId);

            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Complaint resolved successfully!");
                return "resolve-complaint-success";  // Redirect to success page
            } else {
                model.addAttribute("error", "Failed to resolve complaint");
                return "resolveComplaint";  // Return to resolve complaint page with an error
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while resolving the complaint: " + e.getMessage());
            return "resolveComplaint";  // Return to the form with an error message
        }
    }

    // Additional methods...

    // Helper class to structure the resolution request
    public static class ResolutionRequest {
        private Integer executiveId;
        private String resolutionDetails;

        public ResolutionRequest(Integer executiveId, String resolutionDetails) {
            this.executiveId = executiveId;
            this.resolutionDetails = resolutionDetails;
        }

        public Integer getExecutiveId() {
            return executiveId;
        }

        public void setExecutiveId(Integer executiveId) {
            this.executiveId = executiveId;
        }

        public String getResolutionDetails() {
            return resolutionDetails;
        }

        public void setResolutionDetails(String resolutionDetails) {
            this.resolutionDetails = resolutionDetails;
        }
    }
    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedbackForm";  // Thymeleaf template for feedback form
    }

    // Handle form submission for feedback
    @PostMapping("/submitFeedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        try {
            // Send the feedback to the backend API
            ResponseEntity<Feedback> response = restTemplate.postForEntity(complaintBaseUrl, feedback, Feedback.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Thank you for your feedback!");
                return "feedbackConfirmation";  // Redirect to a confirmation page after submission
            } else {
                model.addAttribute("error", "Failed to submit feedback. Please try again.");
                return "feedbackForm";  // Return to the form with an error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while submitting your feedback: " + e.getMessage());
            return "feedbackForm";  // Return to the form with an error message
        }
    }
    
}
