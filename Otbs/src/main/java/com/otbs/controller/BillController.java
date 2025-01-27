package com.otbs.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otbs.exception.InvalidEntityException;
import com.otbs.model.Bill;
import com.otbs.service.BillServiceImpl;
import com.otbs.service.PdfService;
import com.otbs.repository.*;

@RestController
@RequestMapping("/api/bills")
public class BillController {
	
	@Autowired
	private PdfService pdfService;

	@Autowired
	private BillRepository billrepositort;
	
    @Autowired
    BillServiceImpl billService=new BillServiceImpl();


    @GetMapping("/customer/{customerId}")
    public List<Bill> getAllBillsByCustomerId(@PathVariable int customerId) throws InvalidEntityException {
        
        List<Bill> bills= billService.getAllBillsByCustomerId(customerId);
        if (bills.isEmpty()) {
            throw new InvalidEntityException("customer has no bill");
        }
        else return bills;
    }

    @GetMapping("/customer/{customerId}/unpaid")
    public List<Bill> getUnpaidBillsByCustomerId(@PathVariable int customerId) throws InvalidEntityException {
        List<Bill> bills = billService.getUnpaidBillsByCustomerId(customerId);
        if (bills.isEmpty()) {
            throw new InvalidEntityException("customer has No Bending bill");
        }
        else return bills;
    }

    @GetMapping("/customer/{customerId}/lastSixMonth")
    public List<Bill> getSixMonthBillsByCustomerId(@PathVariable int customerId) throws InvalidEntityException {
        List<Bill> bills = billService.getBillsForLastSixMonth(customerId);
        if (bills.isEmpty()) {
            throw new InvalidEntityException("customer has No Bending bill");
        }
        else return bills; 
    }
    
    @GetMapping("/customer/{billId}/bill")
    public Bill getbill(@PathVariable int billId) throws  InvalidEntityException{
    	return billService.getBill(billId);
    }

    @GetMapping("/pay/{billId}/{paymentMethod}")
    public String payBill(@PathVariable int billId,@PathVariable String paymentMethod) throws  InvalidEntityException{
        Boolean paid = billService.payBill(billId,paymentMethod);
        if(!paid) throw new InvalidEntityException("Bill Not Paid");
        else return "Bill paid Successflly";
    }
    
    //Genetation Pdf 
    @CrossOrigin(origins = "http://localhost:8091")
    @GetMapping("/download/{billId}")
    public ResponseEntity<byte[]> downloadBillPdf(@PathVariable int billId) {
        Bill bill = billrepositort.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

        byte[] pdfData = pdfService.generateBillPdf(bill);

        System.out.println("i am receiving the download request");
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill_" + billId + ".pdf")
                .body(pdfData);
    }

    //Admine purpose 
    
    @GetMapping("/customer/unpaidbills")
    public List<Bill> getAllUnpaidBills() throws InvalidEntityException{
    	List<Bill> bills= billService.allunpaidbill();
    	if(bills.isEmpty()) throw new InvalidEntityException("No Unpaid Bills Found");
    	else return bills;
    }
    
    @GetMapping("/customer/billcrossduedate")
    public List<Bill> getAllUnpaidBillCrossDue() throws InvalidEntityException{
    	List<Bill> bills= billService.billCrossDueDate();
    	if(bills.isEmpty()) throw new InvalidEntityException("No Unpaid Bills Crosses The DueDate");
    	else return bills;
    }

}



