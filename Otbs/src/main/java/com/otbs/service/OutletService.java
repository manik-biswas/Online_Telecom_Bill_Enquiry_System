package com.otbs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otbs.model.Outlet;
import com.otbs.repository.OutletRepository;

@Service
public class OutletService {
	
	@Autowired
	private OutletRepository outletRepository;
	
	public OutletService(OutletRepository outletRepository) {
		this.outletRepository=outletRepository;
	}
	
	//add a new outlet
	public Outlet addOutlet(Outlet outlet) {
		return outletRepository.save(outlet);
	}
	
	//get all outlets
	public List<Outlet> getAllOutlets(){
		return outletRepository.findAll()
				               .stream()
				               .collect(Collectors.toList());
	}
	
	
	//find the nearest outlet by location
	public Outlet findNearestOutlet(String location) {
		List<Outlet> outlets = outletRepository.findByLocation(location);
		return outlets.stream().findFirst().get();
	}

}
