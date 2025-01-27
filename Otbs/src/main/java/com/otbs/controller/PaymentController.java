package com.otbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import com.otbs.exception.InvalidEntityException;
import com.otbs.service.*;

@RestController
@RequestMapping("/api/bills")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/revenue")
    public Double calculateRevenue (
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws  InvalidEntityException{
        double revenue;
    	try {
        	revenue = paymentService.getRevenue(startDate, endDate);
        	if (revenue == 0.00) {
            	throw new InvalidEntityException("No revenue Generated");
            }
        }
        catch(Exception e){
        	revenue=0.00;
        }
        return revenue;
    }
}
