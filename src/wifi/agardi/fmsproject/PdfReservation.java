package wifi.agardi.fmsproject;

import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfReservation {
	
	public void pdfGenerateReservation(Reservation res, int catPrice, int insPrice){
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("Reservations/" + res.getResNumberID() + ".pdf"));
			document.open();
			
		    Image image1 = Image.getInstance(getClass().getResource("/Backgroundfms.jpg"));
	            image1.scaleToFit(700, 320);
	            image1.setAbsolutePosition(0f, 600f);
	            document.add(image1);
	            
	         Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
	         Font expiredTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
	         Font cancelledTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.RED);
	         Font secondTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
	         Font subMenuFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
	         //calculating rental days, with max. 30 late 
	         LocalDateTime ldtReturn = res.getReturnTime().minusMinutes(45);
			    int days = 0;
			    long hrs = Duration.between(res.getPickupTime(), ldtReturn).toHours();
			    if(hrs < 24) {
			    	days = 1;
			    }
			    if(hrs >= 24) {
			    	days = (int) (hrs/24) + 1;
			    }
			    int rentPrice = (int) (days * catPrice);
			    
			    int price = 0;
			    int extraPrices = 0;
			    String extras = "";
			    for(String s : res.getResExtras()) {
			    	for(String key: Database.readExtrasTable().keySet()) {
						if(s.equals(key))
						price = Database.readExtrasTable().get(key);
					}
			    	extraPrices += price;
			    	extras += "   - " + s + ": " + price + " € / rental\n";
			    }	

			    
			 document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n"));
			 if(res.isStatus() == false && res.getReturnTime().isBefore(LocalDateTime.now())) {
				 document.add(new Paragraph("Expired reservation!", expiredTitleFont)); 
				}
			 if(res.isStatus()) {
				 document.add(new Paragraph("Cancelled reservation!", cancelledTitleFont)); 
			 } else {
				 document.add(new Paragraph("Your car reservation confirmation", titleFont));
			 }
			
			 document.add(new Paragraph("Reservation ID: " + res.getResNumberID(), secondTitleFont));
			 document.add(new Paragraph("Thank your for your booking. This document confirms your car rental reservation.\n\n"));
		
			 document.add(new Paragraph("Customer", subMenuFont));
			 document.add(new Paragraph("Customer ID: " + res.getCustomer().getCustomerID()));
			 document.add(new Paragraph("Customer name: " + res.getCustomer().getFirstName() + " " + res.getCustomer().getLastName()));
			
			 document.add(new Paragraph("\nCar", subMenuFont));
			 document.add(new Paragraph("Reserved category:  " + res.getReservedCategory()));
			 document.add(new Paragraph("Car type: " + res.getCar().getCarBrand() + " " +
					 								   res.getCar().getCarModel() + " " + 
					 								   res.getCar().getCarTransmission() + ", " + 
					 								   res.getCar().getCarFuelType() + " (" +
					 								   res.getCar().getCarCategory() + ")"));
			 
			 document.add(new Paragraph("\nRental details", subMenuFont));
			 document.add(new Paragraph("Total days:  " + days));
			 document.add(new Paragraph("Pickup:       " + res.getPickupTime() + ", " + res.getPickupLocation()));
			 document.add(new Paragraph("Return:       " + res.getReturnTime() + ", " + res.getReturnLocation()));
			 document.add(new Paragraph("Reservation notes: " + res.getResNotes()));
			 			   
			    
			 document.add(new Paragraph("\nPrice", subMenuFont)); 
			 document.add(new Paragraph("Base price for this category: " + catPrice + " € * " + days + " day(s): " + rentPrice + " €")); 
			 document.add(new Paragraph("Insurance: " + res.getInsuranceType() + ": " + insPrice + " € / day, total: " + (insPrice*days) + " €"));
			 document.add(new Paragraph("Selected extras total: " + extraPrices + " €\n" + extras));
			 document.add(new Paragraph("Total car rental estimate: " + (rentPrice+ (insPrice*days) +extraPrices) + " € incl. TAX", subMenuFont)); 
			 
				
			 Paragraph footNote = new Paragraph("Wien, "  + LocalDateTime.now());
			 footNote.setAlignment(Paragraph.ALIGN_RIGHT);
			 document.add(footNote);
			 
			 document.close();
				
		
		} catch (Exception e) {
			System.out.println("Something is wrong with the PDF generation for reservation");
			e.printStackTrace();
		} 
		
		
	}

}
