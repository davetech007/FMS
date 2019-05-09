package wifi.agardi.fmsproject;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.paint.Color;

public class DateCellFactory extends DateCell {
	LocalDate before;
	LocalDate after;
	
	public DateCellFactory() {
		
	}
	
	public DateCellFactory(LocalDate before, LocalDate after) {
		this.before = before;
		this.after = after;
	}
		@Override
		public void updateItem(LocalDate item, boolean empty) {
		super.updateItem(item, empty); 
		
		if (item.isBefore(before)) {
			this.setDisable(true);
		}
		
		
		if (item.isAfter(after)) {
			this.setDisable(true);
		}
		

		DayOfWeek day = DayOfWeek.from(item);
		
		if (day == DayOfWeek.SATURDAY) {
			this.setTextFill(Color.BLUE);
		}
		if (day == DayOfWeek.SUNDAY) {
			this.setTextFill(Color.RED);
		}
	  }
		
  } 



