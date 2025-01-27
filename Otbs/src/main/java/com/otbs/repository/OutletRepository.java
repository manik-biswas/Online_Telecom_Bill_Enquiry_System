package com.otbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otbs.model.*;
@Repository
public interface OutletRepository extends JpaRepository<Outlet, Integer> {
	
	List<Outlet> findByLocation(String location);

}
