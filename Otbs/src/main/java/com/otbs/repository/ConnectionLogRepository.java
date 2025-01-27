package com.otbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otbs.model.*;
@Repository
public interface ConnectionLogRepository extends JpaRepository<ConnectionLog, Integer> {

}
