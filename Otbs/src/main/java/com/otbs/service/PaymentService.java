package com.otbs.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otbs.repository.PaymentRepository;

import java.time.LocalDate;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Double getRevenue(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.calculateRevenue(startDate, endDate);
    }
}
