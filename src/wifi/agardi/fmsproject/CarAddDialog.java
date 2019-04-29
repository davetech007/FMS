package wifi.agardi.fmsproject;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class CarAddDialog extends Dialog<ButtonType> {
	
	public CarAddDialog(String brand, String model, String licPlate) {
		super();
		this.setTitle("Adding a new car");
		this.setHeaderText("Would you like to add a new -> " + brand + " " + model + ", with license plate " + licPlate + "?");

		//this.getDialogPane().setContent(carList);
		ButtonType close = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(close, cancel);
	}

}
