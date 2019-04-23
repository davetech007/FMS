package wifi.agardi.fmsproject;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public class CarListDialog extends Dialog<ButtonType> {
	
	public CarListDialog(VBox carList) {
		super();
		this.setTitle("Car list");
		this.setHeaderText("Search for a car");
	
	
		
		this.getDialogPane().setContent(carList);
		ButtonType close = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(close, cancel);
	}

}
