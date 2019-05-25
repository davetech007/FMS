package wifi.agardi.fmsproject;

import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGeneration {
	private Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
	private Font secondTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
	private Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
	private Chunk signature = new Chunk("FMSADV - Wien, " + LocalDateTime.now());

	public void pdfGenerateReservation(Reservation res, int catPrice, int insPrice) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("Reservations/" + res.getResNumberID() + ".pdf"));
			document.open();

			Image image1 = Image.getInstance(getClass().getResource("/Backgroundfms.jpg"));
			image1.scaleToFit(700, 320);
			image1.setAbsolutePosition(0f, 600f);
			document.add(image1);

			Font expiredTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
			Font cancelledTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.RED);
			Font subMenuFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
			
			// calculating rental days, with max. 30 late
			int days = getDays(res);
			int rentPrice = (int) (days * catPrice);

			int price = 0;
			int extraPrices = 0;
			String extras = "";
			for (String s : res.getResExtras()) {
				for (String key : Database.readExtrasTable().keySet()) {
					if (s.equals(key))
						price = Database.readExtrasTable().get(key);
				}
				extraPrices += price;
				extras += "   - " + s + ": " + price + " € / rental\n";
			}

			document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n"));
			if (res.isStatus() == false && res.getReturnTime().isBefore(LocalDateTime.now())) {
				document.add(new Paragraph("Expired reservation!", expiredTitleFont));
			}
			if (res.isStatus()) {
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
			document.add(new Paragraph("Car type: " + res.getCar().getCarBrand() + " " + res.getCar().getCarModel()
									+ " " + res.getCar().getCarTransmission() + ", " + res.getCar().getCarFuelType() + " ("
									+ res.getCar().getCarCategory() + ")"));

			document.add(new Paragraph("\nRental details", subMenuFont));
			document.add(new Paragraph("Total days:  " + days));
			document.add(new Paragraph("Pickup:       " + res.getPickupTime() + ", " + res.getPickupLocation()));
			document.add(new Paragraph("Return:       " + res.getReturnTime() + ", " + res.getReturnLocation()));
			document.add(new Paragraph("Reservation notes: " + res.getResNotes()));

			document.add(new Paragraph("\nPrice", subMenuFont));
			document.add(new Paragraph("Base price for this category: " + catPrice + " € * " + days + " day(s): " + rentPrice + " €"));
			document.add(new Paragraph("Insurance: " + res.getInsuranceType() + ": " + insPrice +
										" € / day, total: "+ (insPrice * days) + " €"));
			document.add(new Paragraph("Selected extras total: " + extraPrices + " €\n" + extras));
			document.add(new Paragraph("Total car rental estimate: " + (rentPrice + (insPrice * days) + 
										extraPrices) + " € incl. TAX", subMenuFont));

			Paragraph base = new Paragraph(signature);
			base.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(base);

			document.close();

		} catch (Exception e) {
			System.out.println("Something is wrong with the PDF generation for reservation");
			e.printStackTrace();
		}

	}
//From the chosen (ldt) date 00:00- 24:00
	public void pdfGenerateDailyPlan(ArrayList<Reservation> todaysResCO, ArrayList<Reservation> todaysResCI, LocalDate ldt) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("DailyPlans/" + ldt + "_dailyPlanFMS.pdf"));
			document.open();

			Image image1 = Image.getInstance(getClass().getResource("/Backgroundfms.jpg"));
			image1.scaleToFit(700, 320);
			image1.setAbsolutePosition(0f, 600f);
			document.add(image1);

			document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n"));
			Paragraph titleP = new Paragraph("Your daily plan, in " + ldt + "\n\n", titleFont);
			titleP.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(titleP);

			float[] columnWidths = { 1, 3, 3, 2, 2, 3, 1 };
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(100);
			PdfPCell cell = new PdfPCell(new Phrase("CHECK-OUT", secondTitleFont));
			cell.setBackgroundColor(GrayColor.GRAYWHITE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(7);
			table.addCell(cell);

			table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Nr.");
			table.addCell("Pickup date");
			table.addCell("Pickup location");
			table.addCell("Reserved category");
			table.addCell("Car");
			table.addCell("Customer");
			table.addCell("Note");
			table.setHeaderRows(1);

			table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			todaysResCO.sort((o1, o2) -> o1.getPickupTime().compareTo(o2.getPickupTime()));
			for (int i = 1; i <= todaysResCO.size(); i++) {
				Reservation actualRes = todaysResCO.get(i - 1);

				table.addCell(new PdfPCell(new Phrase("" + i, smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getPickupTime().toString(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getPickupLocation(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getReservedCategory(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getCar().getCarLicensePlate(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getCustomer().getFirstName() + " " + 
													  actualRes.getCustomer().getLastName(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getResNotes(), smallFont)));
			}
			document.add(table);
			document.add(new Paragraph("\n\n\n"));

			PdfPTable tableCI = new PdfPTable(columnWidths);
			tableCI.setWidthPercentage(100);
			PdfPCell cell2 = new PdfPCell(new Phrase("CHECK-IN", secondTitleFont));
			cell2.setBackgroundColor(GrayColor.GRAYWHITE);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setColspan(7);
			tableCI.addCell(cell2);

			tableCI.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			tableCI.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tableCI.addCell("Nr.");
			tableCI.addCell("Return date");
			tableCI.addCell("Return location");
			tableCI.addCell("Reserved category");
			tableCI.addCell("Car");
			tableCI.addCell("Customer");
			tableCI.addCell("Note");
			tableCI.setHeaderRows(1);

			tableCI.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			tableCI.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			todaysResCI.sort((o1, o2) -> o1.getReturnTime().compareTo(o2.getReturnTime()));
			for (int i = 1; i <= todaysResCI.size(); i++) {
				Reservation actualRes = todaysResCI.get(i - 1);

				tableCI.addCell(new PdfPCell(new Phrase("" + i, smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReturnTime().toString(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReturnLocation(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReservedCategory(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getCar().getCarLicensePlate(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getCustomer().getFirstName() + " " +
														actualRes.getCustomer().getLastName(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getResNotes(), smallFont)));
			}
			document.add(tableCI);

			Paragraph base = new Paragraph(signature);
			base.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(base);

			document.close();

		} catch (Exception e) {
			System.out.println("Something is wrong with the PDF generation for daily plan");
			e.printStackTrace();
		}
	}
//From the chosen (ldt) exact date - next 7 days
	public void pdfGenerateWeeklyPlan(ArrayList<Reservation> weeklyResCO, ArrayList<Reservation> weeklyResCI, LocalDate ldt) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("WeeklyPlans/" + ldt + "-" + ldt.plusDays(6).getDayOfMonth() + "_weeklyPlanFMS.pdf"));
			document.open();

			Image image1 = Image.getInstance(getClass().getResource("/Backgroundfms.jpg"));
			image1.scaleToFit(700, 320);
			image1.setAbsolutePosition(0f, 600f);
			document.add(image1);

			document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n"));
			Paragraph titleP = new Paragraph("Your weekly plan", titleFont);
			Paragraph titlePDate = new Paragraph(ldt + " (" + ldt.getDayOfWeek() + ") - " + ldt.plusDays(6) + " ("
												+ ldt.plusDays(6).getDayOfWeek() + ")\n\n", secondTitleFont);
			titleP.setAlignment(Paragraph.ALIGN_CENTER);
			titlePDate.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(titleP);
			document.add(titlePDate);

			float[] columnWidths = { 1, 2, 3, 2, 2, 2, 3, 1 };
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(100);
			PdfPCell cell = new PdfPCell(new Phrase("CHECK-OUT", secondTitleFont));
			cell.setBackgroundColor(GrayColor.GRAYWHITE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(8);
			table.addCell(cell);

			table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Nr.");
			table.addCell("Pickup date");
			table.addCell("Pickup day");
			table.addCell("Pickup location");
			table.addCell("Reserved category");
			table.addCell("Car");
			table.addCell("Customer");
			table.addCell("days");
			table.setHeaderRows(1);

			table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			weeklyResCO.sort((o1, o2) -> o1.getPickupTime().compareTo(o2.getPickupTime()));
			for (int i = 1; i <= weeklyResCO.size(); i++) {
				Reservation actualRes = weeklyResCO.get(i - 1);

				table.addCell(new PdfPCell(new Phrase("" + i, smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getPickupTime().toString(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getPickupTime().getDayOfWeek().toString(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getPickupLocation(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getReservedCategory(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getCar().getCarLicensePlate(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(actualRes.getCustomer().getFirstName() + " " + 
													  actualRes.getCustomer().getLastName(), smallFont)));
				table.addCell(new PdfPCell(new Phrase(Integer.toString(getDays(actualRes)), smallFont)));

			}
			document.add(table);
			document.add(new Paragraph("\n\n\n"));

			PdfPTable tableCI = new PdfPTable(columnWidths);
			tableCI.setWidthPercentage(100);
			PdfPCell cell2 = new PdfPCell(new Phrase("CHECK-IN", secondTitleFont));
			cell2.setBackgroundColor(GrayColor.GRAYWHITE);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setColspan(8);
			tableCI.addCell(cell2);

			tableCI.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			tableCI.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tableCI.addCell("Nr.");
			tableCI.addCell("Return date");
			tableCI.addCell("Return day");
			tableCI.addCell("Return location");
			tableCI.addCell("Reserved category");
			tableCI.addCell("Car");
			tableCI.addCell("Customer");
			tableCI.addCell("days");
			tableCI.setHeaderRows(1);

			tableCI.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			tableCI.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			weeklyResCI.sort((o1, o2) -> o1.getReturnTime().compareTo(o2.getReturnTime()));
			for (int i = 1; i <= weeklyResCI.size(); i++) {
				Reservation actualRes = weeklyResCI.get(i - 1);

				tableCI.addCell(new PdfPCell(new Phrase("" + i, smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReturnTime().toString(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReturnTime().getDayOfWeek().toString(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReturnLocation(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getReservedCategory(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getCar().getCarLicensePlate(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(actualRes.getCustomer().getFirstName() + " " +
														actualRes.getCustomer().getLastName(), smallFont)));
				tableCI.addCell(new PdfPCell(new Phrase(Integer.toString(getDays(actualRes)), smallFont)));
			}
			document.add(tableCI);

			Paragraph base = new Paragraph(signature);
			base.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(base);

			document.close();

		} catch (Exception e) {
			System.out.println("Something is wrong with the PDF generation for daily plan");
			e.printStackTrace();
		}
	}

	private int getDays(Reservation rs) {
		LocalDateTime ldtReturn = rs.getReturnTime().minusMinutes(45);
		int days = 0;
		long hrs = Duration.between(rs.getPickupTime(), ldtReturn).toHours();
		if (hrs < 24) {
			days = 1;
		}
		if (hrs >= 24) {
			days = (int) (hrs / 24) + 1;
		}
		return days;
	}

}