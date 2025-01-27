package com.otbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otbs.model.Outlet;
import com.otbs.service.OutletService;

@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/outlet")
public class OutletController {
    
    @Autowired
    private OutletService outletService;
    
    public OutletController(OutletService outletService) {
        this.outletService = outletService;
    }
    
    @PostMapping("/add")
    public Outlet addOutlet(@RequestBody Outlet outlet) {
        return outletService.addOutlet(outlet);
    }
    
    @GetMapping("/all")
    public List<Outlet> getAllOutlets(){
        return outletService.getAllOutlets();
    }
    
    @GetMapping("/nearest/{location}")
    public ResponseEntity<Outlet> findNearestOutlet(@PathVariable String location) {
        Outlet nearestOutlet = outletService.findNearestOutlet(location);
        if (nearestOutlet != null) {
            return new ResponseEntity<>(nearestOutlet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
