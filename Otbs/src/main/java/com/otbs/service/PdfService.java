package com.otbs.service;


import com.otbs.model.Bill;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateBillPdf(Bill bill) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Bill Details"));
        document.add(new Paragraph("Bill ID: " + bill.getBillId()));
        document.add(new Paragraph("Date: " + bill.getDate()));
        document.add(new Paragraph("Due Date: " + bill.getDueDate()));
        document.add(new Paragraph("Additional Charges: " + bill.getAdditionalCharges()));
        document.add(new Paragraph("Discount: " + bill.getDiscount()));
        document.add(new Paragraph("Tax: " + bill.getTax()));
        document.add(new Paragraph("Total Amount: " + bill.getTotalAmount()));
        document.add(new Paragraph("Status: " + bill.getStatus()));

        document.close();
        return outputStream.toByteArray();
    }
}
