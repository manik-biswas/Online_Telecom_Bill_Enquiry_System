package com.otbs.controller;

import com.otbs.model.Admin;
import com.otbs.model.Customer;
import com.otbs.service.AdminService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        try {
            Admin registeredAdmin = adminService.registerAdmin(admin.getUsername(), admin.getPassword());
            return ResponseEntity.ok("Admin registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        String username = admin.getUsername();
        String password = admin.getPassword();

        Admin authenticatedAdmin = adminService.authenticate(username, password);
        if (authenticatedAdmin != null) {
            return ResponseEntity.ok(authenticatedAdmin);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/viewallcustomers")
    public List<Customer> getAllCustomers() {
        return adminService.getAllCustomers();
    }

   
}
