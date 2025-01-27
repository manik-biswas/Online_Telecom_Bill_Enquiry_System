package com.otbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otbs.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsernameAndPassword(String username, String password);
}
