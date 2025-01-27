package com.otbs.service;

import java.time.LocalDate;


import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;

import jakarta.mail.Message;

import jakarta.mail.PasswordAuthentication;

import jakarta.mail.Session;

import jakarta.mail.Transport;

import jakarta.transaction.Transactional;

import jakarta.mail.MessagingException;

import jakarta.mail.internet.InternetAddress;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.otbs.model.Bill;
import com.otbs.model.Connection;
import com.otbs.model.Payment;
import com.otbs.model.Plan;
import com.otbs.model.UsageInfo;
import com.otbs.repository.BillRepository;
import com.otbs.repository.ConnectionRepository;
import com.otbs.repository.PaymentRepository;
import com.otbs.repository.PlanRepository;
import com.otbs.repository.UsageInfoRepository;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;


@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UsageInfoRepository usageInfoRepository;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    @Transactional
    public void generatePrepaidBill(int connectionId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        Plan plan = connection.getPlan();
        if (LocalDate.now().isAfter(planend(connection.getActivationdate(),plan.getNumberOfDay()))){ //compare validity date with the current date
            // Block service if validity expired
            connection.setStatus("Blocked");
            connectionRepository.save(connection);  //updating that the plan blocked

            // Generate summary bill
            UsageInfo usage = usageInfoRepository.findByConnection(connection)
                    .orElseThrow(() -> new RuntimeException("Usage info not found"));
            
            Bill existingBill = billRepository.findByConnectionAndUsage(connection, usage); //checking bill alredy there
            if (existingBill != null) {
            	System.out.println("problem solved");// bill already there
            	// mailsending();
            }
            else {

	            double additionalCharges = calculateAdditionalCharges(usage, plan);
	            double totalCharges = plan.getFixedRate() + additionalCharges;
	
	            Bill bill = new Bill();
	            bill.setConnection(connection);
	            bill.setUsage(usage);
	            bill.setDate(LocalDate.now());
	            bill.setTotalAmount(totalCharges);
	            bill.setStatus("Paid");
	            billRepository.save(bill);
	            
	            
            }
        }
    }

    @Override
    @Transactional
    public void generatePostpaidBill(int connectionId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        Plan plan = connection.getPlan();

        if (LocalDate.now().isAfter(planend(connection.getActivationdate(),plan.getNumberOfDay()))){ 
            UsageInfo usage = usageInfoRepository.findByConnection(connection)
                    .orElseThrow(() -> new RuntimeException("Usage info not found"));
            
            Bill existingBill = billRepository.findByConnectionAndUsage(connection, usage); //checking bill alredy there
            if (existingBill != null) {

                System.out.println("problem solved");  // bill alredy there
                
            }
            else {
                
                // Calculate additional charges if usage exceeds limits
                float additionalCharges = calculateAdditionalCharges(usage, plan);        // generate bill

                float totalCharges = (float)(plan.getFixedRate() + additionalCharges);
                float tax = (float) (totalCharges * 0.18); // Assuming 18% tax
                int finalAmount = Math.round(totalCharges + tax);
                
                //new changes
                LocalDate planstart=connection.getActivationdate();
                LocalDate enddate=planend(connection.getActivationdate(),plan.getNumberOfDay());
                LocalDate due=LocalDate.now().plusDays(15);
                String planname=plan.getPlanName();
                
                
                // Create a new bill
                Bill bill = new Bill();
                bill.setConnection(connection);
                bill.setUsage(usage);
                bill.setDate(LocalDate.now());
                bill.setTotalAmount(finalAmount);
                bill.setTax(tax);
                bill.setStatus("Unpaid");
                bill.setDueDate(due); // Set due date
                Bill generated_bill=billRepository.save(bill);
                int  bill_id=generated_bill.getBillId();         // Get bill id
                System.out.println("bill_id: " + bill_id);

                // Suspend service if the previous bill is unpaid
                if (bill.getStatus().equalsIgnoreCase("Unpaid") &&  //ignoring the case-sensitive means "unpaid" and "UNPAID" both are same 
                        LocalDate.now().isAfter(bill.getDueDate())) {
                    connection.setStatus("Blocked");
                    connectionRepository.save(connection);
                }
                
                //new to update the connection ...
                connection.setActivationdate(LocalDate.now());  //updating the last billing time 
                connectionRepository.save(connection);
                String customerEmail = connection.getCustomerObj().getEmail();

                
                mailsendingPost(bill_id,planname,planstart,enddate,totalCharges,tax,finalAmount,due,customerEmail,usage); //billdate,charges,tax,total,duedate
                
                }
        } 
        
        
    }
    
    //due alert email 
    public void dueAlert(Bill bill) {
    	String custEmail=bill.getConnection().getCustomerObj().getEmail();
    	dueBending(bill,custEmail);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void generateBillsForAllConnections() {
        List<Connection> connections = connectionRepository.findAll(); //getting all the connection , i think we need to fetch the " active " connection
        System.out.println(connections.size());
        System.out.println(connections);
        for (Connection connection : connections) {
            if ("Prepaid".equalsIgnoreCase(connection.getConnectionType())) {
                generatePrepaidBill(connection.getConnectionId());
                System.out.println("prepaid");
            } else if ("postpaid".equalsIgnoreCase(connection.getConnectionType())) {
                generatePostpaidBill(connection.getConnectionId());  //edited by me to check date 
                System.out.println("postpaid");
            }
        }
        List<Bill> unpaidbills = billRepository.findAllBillsByStatus("unpaid");
        for(Bill bills : unpaidbills) {
        	LocalDate duedate= bills.getDueDate();
        	LocalDate currentDate = LocalDate.now();
        	int dueDateDifference =(int) Math.abs(ChronoUnit.DAYS.between(duedate, currentDate)) ;
        	if(dueDateDifference == 3) {
        		dueAlert(bills);
        	}
        }
        System.out.println("sivaraj- hear");
    }
    
    //Get All Bills
    public List<Bill> getAllBillsByCustomerId(int customerId) {
        return billRepository.findAllBillsByCustomerId(customerId);
    }

    // Get unpaid bills for a specific customer
    public List<Bill> getUnpaidBillsByCustomerId(int customerId) {
        return billRepository.findUnpaidBillsByCustomerId(customerId);
    }

    public List<Bill> getBillsForLastSixMonth(int customerId) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        return billRepository.findBillsByCustomerIdAndDateAfter(customerId, sixMonthsAgo);
    }

    @Transactional
    public Boolean payBill(int billId,String paymentMethod){
        Bill bill = billRepository.findByBillId(billId);
        if(bill == null) return false;
        
        bill.setStatus("Paid");
        billRepository.save(bill);
        
        Payment payDetail = new Payment();
        payDetail.setBillId(bill);
        payDetail.setAmount(bill.getTotalAmount());
        payDetail.setpaymentDate(LocalDate.now());
        payDetail.setPaymentMethod(paymentMethod);
        paymentRepository.save(payDetail);
        
        String custemail=bill.getConnection().getCustomerObj().getEmail();
        paymentMail(bill,custemail);
        return true;
    }

    private float calculateAdditionalCharges(UsageInfo usage, Plan plan) {
        float dataCharges = Math.max(0, usage.getDataUsed() - (convertStringToInt(plan.getDataLimit())))
                * plan.getExtraChargePerMB();
        float callCharges = Math.max(0, usage.getCalls() -(convertStringToInt(plan.getCallLimit())))
                * plan.getExtraChargePerCall();
        float smsCharges = Math.max(0, usage.getSms() - convertStringToInt(plan.getSmsLimit()))
                * plan.getExtraChargePerSMS();
        return dataCharges + callCharges + smsCharges;
    }
    
    
    //convert the string to integer for plan convertion 
    
    public int convertStringToInt(String input) {
        // Remove any non-numeric characters from the string
        String quantity=input.replaceAll("[^0-9]", "");
        return Integer.parseInt(quantity);
    }
    
    public static LocalDate planend(LocalDate date,int day) {
    	return date.plusDays(day);
    }
    
    public Bill getBill(int billId){
    	return billRepository.findByBillId(billId);
    }
    
    public List<Bill> allunpaidbill(){
    	return billRepository.findAllBillsByStatus("unpaid");
    }
    
    public List<Bill> billCrossDueDate(){
    	return billRepository.findAllBillsCrossDueDate("unpaid", LocalDate.now());
    }
    
    //Emails
    
    public static void  mailsendingPost(int bill_id,String planname,LocalDate planstart,LocalDate enddate,float totalCharges,float tax,int finalAmount,LocalDate due,String email,UsageInfo usage) {
        BillEmailService mailTemplate = new BillEmailService();
        final String senderEmail=mailTemplate.getSenderEmail();
        final String senderPassword=mailTemplate.getSenderPassword();

         // Receiver's email
         String recipientEmail = email;

         // SMTP server properties
         Properties properties = new Properties();
         properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
         properties.put("mail.smtp.port", "587"); // TLS port
         properties.put("mail.smtp.auth", "true"); // Enable authentication
         properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS

         // Create a session with authentication
         Session session = Session.getInstance(properties, new Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(senderEmail, senderPassword);
             }
         });

         try {
             // Create a MIME email message
             MimeMessage message = new MimeMessage(session);
             message.setFrom(new InternetAddress(senderEmail)); // Sender's email
             message.setRecipients(
                 Message.RecipientType.TO,
                 InternetAddress.parse(recipientEmail)
             );
             message.setSubject("New Bill "); // Email subject


             //int bill_id,String planname,LocalDate planstart,LocalDate enddate,float totalCharges,float tax,int finalAmount,LocalDate due
             
             String billSender=mailTemplate.getBillCreating();
             String usage_details=String.valueOf(usage.getCalls())+ "Calls , "+String.valueOf(usage.getDataUsed())+" GB of Data And " +String.valueOf(usage.getSms())+ " SMS";
             String emailBody = billSender.replace("{{billId}}", String.valueOf(bill_id))
                     .replace("{{issueDate}}", LocalDate.now().toString())
                     .replace("{{planName}}", planname)
                     .replace("{{billCycle}}", planstart.toString()+" to "+enddate.toString())
                     .replace("{{totalCharges}}", String.valueOf(totalCharges))
                     .replace("{{tax}}", String.valueOf(tax))
                     .replace("{{finalAmount}}", String.valueOf(finalAmount))
                     .replace("{{dueDate}}", due.toString())
                     .replace("{{Usage}}",usage_details);

             // Set the content of the email to HTML
             message.setContent(emailBody, "text/html; charset=utf-8");

             // Send the email
             Transport.send(message);

             System.out.println("HTML billgenerated email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    
    public static void paymentMail(Bill bill , String email) {
    	
    	 BillEmailService mailTemplate = new BillEmailService();
         final String senderEmail=mailTemplate.getSenderEmail();
         final String senderPassword=mailTemplate.getSenderPassword();

          // Receiver's email
          String recipientEmail = email;

          // SMTP server properties
          Properties properties = new Properties();
          properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
          properties.put("mail.smtp.port", "587"); // TLS port
          properties.put("mail.smtp.auth", "true"); // Enable authentication
          properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS

          // Create a session with authentication
          Session session = Session.getInstance(properties, new Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(senderEmail, senderPassword);
              }
          });
          
          try {
              // Create a MIME email message
              MimeMessage message = new MimeMessage(session);
              message.setFrom(new InternetAddress(senderEmail)); // Sender's email
              message.setRecipients(
                  Message.RecipientType.TO,
                  InternetAddress.parse(recipientEmail)
              );
              message.setSubject("Payment Successful"); // Email subject


              //int bill_id,String planname,LocalDate planstart,LocalDate enddate,float totalCharges,float tax,int finalAmount,LocalDate due
              
              String billSender=mailTemplate.getPaymentSuccess();
              
              String emailBody = billSender.replace("{{customerName}}",bill.getConnection().getCustomerObj().getName())
            		  .replace("{{billId}}",String.valueOf(bill.getBillId()))
            		  .replace("{{amountPaid}}",String.valueOf(bill.getTotalAmount()))
            		  .replace("{{paymentDate}}", LocalDate.now().toString());

              // Set the content of the email to HTML
              message.setContent(emailBody, "text/html; charset=utf-8");

              // Send the email
              Transport.send(message);

              System.out.println("HTML payment email sent successfully!");

         } catch (MessagingException e) {
             e.printStackTrace();
         }
     }
    
    public static void dueBending(Bill bill , String email) {
    	
   	 BillEmailService mailTemplate = new BillEmailService();
        final String senderEmail=mailTemplate.getSenderEmail();
        final String senderPassword=mailTemplate.getSenderPassword();

         // Receiver's email
         String recipientEmail = email;

         // SMTP server properties
         Properties properties = new Properties();
         properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
         properties.put("mail.smtp.port", "587"); // TLS port
         properties.put("mail.smtp.auth", "true"); // Enable authentication
         properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS

         // Create a session with authentication
         Session session = Session.getInstance(properties, new Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(senderEmail, senderPassword);
             }
         });
         
         try {
             // Create a MIME email message
             MimeMessage message = new MimeMessage(session);
             message.setFrom(new InternetAddress(senderEmail)); // Sender's email
             message.setRecipients(
                 Message.RecipientType.TO,
                 InternetAddress.parse(recipientEmail)
             );
             message.setSubject("Pending Bills Remainder"); // Email subject


             //int bill_id,String planname,LocalDate planstart,LocalDate enddate,float totalCharges,float tax,int finalAmount,LocalDate due
             
             String billSender=mailTemplate.getDueBending();
             
             String emailBody = billSender.replace("{{customerName}}",bill.getConnection().getCustomerObj().getName())
           		  .replace("{{billId}}",String.valueOf(bill.getBillId()))
           		  .replace("{{amount}}",String.valueOf(bill.getTotalAmount()))
           		  .replace("{{dueDate}}", bill.getDueDate().toString());

             // Set the content of the email to HTML
             message.setContent(emailBody, "text/html; charset=utf-8");

             // Send the email
             Transport.send(message);

             System.out.println("HTML duebending email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    
}

