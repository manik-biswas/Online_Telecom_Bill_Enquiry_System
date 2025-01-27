package com.otbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otbs.model.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

	List<Complaint> findByConnection_ConnectionId(int connectionId);
}
