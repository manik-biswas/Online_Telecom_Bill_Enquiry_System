package com.otbs.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.otbs.model.Bill;

import jakarta.servlet.http.HttpSession;


@Controller
public class BillController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.bill.base.url}")
    private String billBaseUrl;

    @GetMapping("/unpaidBills")
    public String unpaidBills(Model model,HttpSession session) {
        int customerId= (int) session.getAttribute("customerId");
        System.out.println("Customerid ----------this fromn the billview section -------------------------------"+customerId);
        // session.setAttribute("customerId",customerId);
        try{ 
            ResponseEntity<List> response =restTemplate.getForEntity(billBaseUrl+"/customer"+"/"+customerId+"/unpaid", List.class);
            List<Bill> billList = response.getBody();
            model.addAttribute("unpaidBills",billList);
            // session.setAttribute("unpaidBills", billList);
            // session.setAttribute("message", null );

        }
        catch(Exception e){
            model.addAttribute("message"," customer has no Bending bill ");
            // session.setAttribute("message", " customer has no Bending bill " );  
        }

        return "unpaidbillView";    
    }
    
    
    
    @GetMapping("/allBills")
    public String allBills(Model model,HttpSession session) {
        int customerId= (int) session.getAttribute("customerId");
        try{
            ResponseEntity<List> response =restTemplate.getForEntity(billBaseUrl+"/customer"+"/"+customerId, List.class);
            List allbillList = response.getBody();
            model.addAttribute("allBills",allbillList);    
        }
        catch(Exception e){
            model.addAttribute("allmessage"," You Are New Customer ");
        }
       
        try{ 
            ResponseEntity<List> unpaidbillresponse =restTemplate.getForEntity(billBaseUrl+"/customer"+"/"+customerId+"/unpaid", List.class);

            List<Bill> billList = unpaidbillresponse.getBody();
            model.addAttribute("unpaidBills",billList);

        }
        catch(Exception e){
            model.addAttribute("message"," customer has no Bending bill ");
        }
        model.addAttribute("allbill"," true ");
        
        return "unpaidbillView";
    }
    
    

//    @GetMapping("/allBills")
//    public String allBills(Model model,HttpSession session) {
//        int customerId= (int) session.getAttribute("customerId");
//        ResponseEntity<List> response =restTemplate.getForEntity(billBaseUrl+"/customer"+"/"+customerId, List.class);
//        List allbillList = response.getBody();
//        model.addAttribute("allBills",allbillList);
//
//        try{ 
//            ResponseEntity<List> unpaidbillresponse =restTemplate.getForEntity(billBaseUrl+"/customer"+"/"+customerId+"/unpaid", List.class);
//
//            List<Bill> billList = unpaidbillresponse.getBody();
//            model.addAttribute("unpaidBills",billList);
//
//        }
//        catch(Exception e){
//            model.addAttribute("message"," customer has no Bending bill ");
//        }
//        
//        return "unpaidbillView";
//    }

    @GetMapping("/viewbill")
    public String getbill(@RequestParam String billId, Model model, HttpSession session) {
        ResponseEntity<Bill> response =restTemplate.getForEntity(billBaseUrl+"/customer/"+billId+"/bill", Bill.class);
        Bill bill = (Bill) response.getBody();
        model.addAttribute("bill",bill);
        session.setAttribute("bill", bill.getBillId());
        return "billview";
    }
    

    @GetMapping("/paybill")
    public String payBill(@RequestParam String billId,@RequestParam String paymentMethod, Model model, HttpSession session) {
        // Call the backend API to process the payment
        ResponseEntity<String> response = restTemplate.getForEntity(billBaseUrl + "/pay/" + billId +"/"+paymentMethod, String.class);
        
        // Optional: Check the response and add attributes to the model
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("paymessage", "Payment successful for Bill ID: " + billId);
        } else {
            model.addAttribute("paymessage", "Payment failed for Bill ID: " + billId);
        }

        int billdetails= (int) session.getAttribute("bill");
        ResponseEntity<Bill> billresponse =restTemplate.getForEntity(billBaseUrl+"/customer/"+billdetails+"/bill", Bill.class);
        Bill bill = (Bill) billresponse.getBody();
        model.addAttribute("bill",bill);
        // Return the same view or redirect as needed
        return "billView";
    }

    @GetMapping("/api/config/base-url")
    @ResponseBody
    public Map<String, String> getBaseUrl() {
        return Map.of("baseUrl", billBaseUrl);
    }

    //pdf generation 

    @GetMapping("/pdfdownload")
    public byte[] getMethodName(@RequestParam int billId) {
        ResponseEntity<byte[]> pdf = restTemplate.getForEntity(billBaseUrl + "/download/" + billId, byte[].class);
        
        byte[] billpdf = pdf.getBody();
        return billpdf;
    }
    
    
    

    // --------------------------Admine controller ----------------------------------------------------
    

    //view all unpaid bills 

    @GetMapping("/allunpaidbills")
    public String getAllUnpaidBills(Model model){
        
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(billBaseUrl + "/customer/unpaidbills", List.class);
            List allUnpaidBills =response.getBody();
            model.addAttribute("allUnpaidBills",allUnpaidBills);

        } catch (Exception e) {
            model.addAttribute("message","NO Unpaid Bills");
        }
        model.addAttribute("allunpaid","All Unpaid Bills");
        return "adminebillenquiry";
    }

    @GetMapping("/billcrossesduedate")
    public String getAllUnpaidBillCrossDueDate(Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(billBaseUrl + "/customer/billcrossduedate", List.class);
            List allUnpaidBills =response.getBody();
            System.out.println(allUnpaidBills);
            model.addAttribute("allUnpaidBills",allUnpaidBills);
        } catch (Exception e) {
            model.addAttribute("message","NO Unpaid Bills Crosses Due Date");
        }
        model.addAttribute("filterpaid","Unpaid Bills Crosses DueDate");
        return "adminebillenquiry";
    }
    

    //retrive the total revenue genetated

    
    @GetMapping("/revenuemanage")
    public String getMethodName(Model model) {
        return "billAdmine";
    }

    @GetMapping("/revenue")
    public String getrevenueGenerated(@RequestParam("fromDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate
                ,@RequestParam("toDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,Model model) {
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(billBaseUrl + "/revenue?startDate="+fromDate+"&endDate="+toDate, Double.class);
            double revenue = response.getBody();
            model.addAttribute("revenue",revenue);

        } catch (Exception e) {
            model.addAttribute("revmessage","No Bills Paid Yet ( No Revenue Generated )");
        }
        System.out.println(fromDate);
        System.out.println("revenue executed");
        return "billAdmine";
    }
}