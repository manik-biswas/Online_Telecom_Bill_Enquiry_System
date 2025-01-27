package com.otbs.controller;

import com.otbs.model.Customer;
import com.otbs.service.CustomerService;
import com.otbs.service.EmailService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        try {
            Customer registeredCustomer = customerService.registerCustomer(customer);
            emailService.sendThankYouEmail(registeredCustomer.getEmail(), registeredCustomer.getName()); //REgisteration email sent
            return ResponseEntity.ok("Registration successful.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {
        String username = customer.getUsername();
        String password = customer.getPassword();

        Customer authenticatedCustomer = customerService.authenticate(username, password);
        if (authenticatedCustomer != null) {
            return ResponseEntity.ok(authenticatedCustomer);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/{username}")
    public Customer getCustomerByUsername(@PathVariable String username) {
        return customerService.getCustomerByUsername(username);
    }
    
    // by sivaraj 
    @GetMapping("/customerId/{username}")
    public int getcustomerId(@PathVariable String username) {
    	return customerService.getcustomerId(username);
    }

    // Update Customer Details (PUT Request)
    @PutMapping("/{username}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String username, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(username, customer);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}