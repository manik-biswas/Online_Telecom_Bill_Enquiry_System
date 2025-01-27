package com.otbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.otbs.model.Customer;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerClientController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.customer.base.url}")
    private String customerBaseUrl;

    // Show registration page
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    // Register a customer
    @PostMapping("/register")
    public String registerCustomer(Customer customer, Model model) {
        try {
            // Call backend API to register the customer
            ResponseEntity<String> response = restTemplate.postForEntity(customerBaseUrl + "/register", customer, String.class);

            // Success response
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Registration successful!");
            } else {
                model.addAttribute("error", "An unexpected error occurred. Please try again.");
            }
        } catch (Exception e) {
            // Handle error responses
            String errorMessage = "An error occurred during registration.";
            if (e.getMessage().contains("Username already exists")) {
                errorMessage = "Username already exists.";
            } else if (e.getMessage().contains("Email already exists")) {
                errorMessage = "Email already exists.";
            }
            model.addAttribute("error", errorMessage);
        }
        return "register";
    }
    
    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Handle login request
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        String loginUrl = customerBaseUrl + "/login";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                loginUrl,
                requestEntity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Customer customer = restTemplate.getForObject(customerBaseUrl + "/" + username, Customer.class);                 

                ResponseEntity<Integer> responsecus = restTemplate.getForEntity(customerBaseUrl + "/customerId/" + username, Integer.class);
                int customerid =responsecus.getBody();
                session.setAttribute("customerId", customerid);
                
                session.setAttribute("loggedInCustomer", customer);
                session.setAttribute("role", "customer");
                System.out.println("customerId--------------------------------------" + customerid);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Unable to process login. Please try again.");
            e.printStackTrace(); // Log the exception
            return "login";
        }
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute Customer customer, Model model, HttpSession session) {
        try {
            restTemplate.put(customerBaseUrl + "/" + customer.getUsername(), customer);
            session.setAttribute("loggedInCustomer", customer); // Update session with new details
            model.addAttribute("message", "Profile updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile. Please try again.");
            e.printStackTrace();
        }
        return "redirect:/profiledetails";
    }

    // Show dashboard for logged-in customer
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Retrieve the logged-in customer from session
        Customer customer = (Customer) session.getAttribute("loggedInCustomer");

        if (customer != null) {
            model.addAttribute("customer", customer);
            return "customerdashboard";  // Display the dashboard
        } else {
            return "redirect:/login";  // Redirect to login if no customer is found in session
        }
    }
    
    // View and update customer profile
    @GetMapping("/profiledetails")
    public String viewProfile(
            @RequestParam(required = false) String username,
            Model model, 
            HttpSession session) {
        Customer customer;

        if (username != null) {
            // Fetch customer details using the username from the query parameter
            customer = restTemplate.getForObject(customerBaseUrl + "/" + username, Customer.class);
        } else {
            // Fallback: Retrieve the logged-in customer from session
            customer = (Customer) session.getAttribute("loggedInCustomer");
        }

        if (customer != null) {
            model.addAttribute("customer", customer);
            return "profiledetails";
        } else {
            return "redirect:/login";
        }
    }


    // Log out customer
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalidate the session to log the user out
        return "redirect:/login";  // Redirect to login page
    }
}
