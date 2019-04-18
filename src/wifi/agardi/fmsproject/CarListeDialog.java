package wifi.agardi.fmsproject;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public class CarListeDialog extends Dialog<ButtonType> {
	
	public CarListeDialog(VBox carList) {
		super();
		this.setTitle("Car list");
		this.setHeaderText("Car list");
	
	
		
		this.getDialogPane().setContent(carList);
		ButtonType close = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(close, cancel);
	}

}
