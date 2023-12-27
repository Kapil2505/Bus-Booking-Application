package com.RedBus.util;

import com.RedBus.User.payload.BookingDetailsDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class PdfService {

    public byte[] generatePdf(BookingDetailsDto bookingDetails) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add content to the PDF
            addContentToPdf(document, bookingDetails);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private void addContentToPdf(Document document, BookingDetailsDto bookingDetails) throws DocumentException {
        document.add(new Paragraph("Booking ID: " + bookingDetails.getBookingId()));
        document.add(new Paragraph("Bus Company Name: " + bookingDetails.getBusCompanyName()));
        document.add(new Paragraph("Passenger Name: " + bookingDetails.getPassengerName()));
        document.add(new Paragraph("Seat Number: " + bookingDetails.getSeatNumber()));
        document.add(new Paragraph("Booking Time: " + bookingDetails.getBookingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        document.add(new Paragraph("Contact Number: " + bookingDetails.getContactNumber()));
        document.add(new Paragraph("Email: " + bookingDetails.getEmail()));
        document.add(new Paragraph("Payment Status: " + bookingDetails.isPaymentStatus()));
        document.add(new Paragraph("From City: " + bookingDetails.getFromCity()));
        document.add(new Paragraph("To City: " + bookingDetails.getToCity()));
        document.add(new Paragraph("Journey Date: " + bookingDetails.getJourneyDate().toString()));
        document.add(new Paragraph("Total Fare: " + bookingDetails.getTotalFare()));
    }
}

