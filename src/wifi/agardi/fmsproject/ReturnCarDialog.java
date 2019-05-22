package wifi.agardi.fmsproject;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ReturnCarDialog extends Dialog<Integer>{
	int newKm = 0;
	
	public ReturnCarDialog(Car car) {
		super();
		this.setTitle("Check in car");

		this.setHeaderText("Check in '" + car.getCarLicensePlate() + "' \n" +
						   "Old km : " + car.getCarKM() + " km");
		
		Label lbMessage = new Label();
		
		TextField kmTF = new TextField();
		kmTF.setText(Integer.toString(car.getCarKM()));
		
		kmTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,6}?")) {
                	kmTF.setText(oldValue);
                	
                }
            }
        });
       
		VBox vb = new VBox(kmTF,lbMessage);

		this.getDialogPane().setContent(vb);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
		this.setResizable(true);

		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
		
		this.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(kmTF.textProperty().isEmpty());
		
		
		this.setResultConverter(new Callback<ButtonType, Integer>(){
			@Override
			public Integer call(ButtonType bt) {
				if(!kmTF.getText().isEmpty() && bt == ok) {
					newKm = Integer.parseInt(kmTF.getText());
						if(newKm > car.getCarKM()) {
							return newKm;
						}
				}
				return null;
			}
		});	
	}

}
