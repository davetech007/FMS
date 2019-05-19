package wifi.agardi.fmsproject;

import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PdfReservation {
	
	
	public void pdfGenerateReservation(Reservation res) throws FileNotFoundException {
		String dest = res.getResNumberID() + ".pdf";
		
			FileOutputStream fos = new FileOutputStream(dest);
			
			PdfWriter writer = new PdfWriter(fos);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			
			
			document.add(new Paragraph("Helloka"));
			
			
	
		
			
			document.close();
			
			
		
		
		
		
	}

}
