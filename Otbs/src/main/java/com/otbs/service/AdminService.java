package com.otbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otbs.model.Admin;
import com.otbs.model.Customer;
import com.otbs.repository.AdminRepository;
import com.otbs.repository.CustomerRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Admin registerAdmin(String username, String password) {
        // Save new admin
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password); 
        return adminRepository.save(admin);
    }

    public Admin authenticate(String username, String password) {
    	Admin admin = adminRepository.findByUsernameAndPassword(username, password);
        if (admin != null) {
            return admin;
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}