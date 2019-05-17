package wifi.agardi.fmsproject;


import java.time.LocalDate;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ReturnCarDialog extends Dialog<ButtonType>{
	int oldKm = 0;
	int newKm = 0;
	
	public ReturnCarDialog(ReservationFX res, LocalDate returnDate) {
		super();
		this.setTitle("Returning a car");
		
		oldKm = res.getModellObject().getCar().getCarKM();
		this.setHeaderText("Returning '" + res.getModellObject().getCar().getCarLicensePlate() + "'\n" +
							"Return date: " + returnDate +"\n" +
							"Old km : " + oldKm + " km");
		
		Label lbMessage = new Label();
		
		TextField kmTF = new TextField();
		kmTF.setPromptText("New km");
		
		kmTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,6}?")) {
                	kmTF.setText(oldValue);
                	
                }
            }
        });
        
		if(!kmTF.getText().isEmpty()) {
			newKm = Integer.parseInt(kmTF.getText());
 		}
		
		
		VBox vb = new VBox(kmTF,lbMessage);

		this.getDialogPane().setContent(vb);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
		this.setResizable(true);

		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
		
		this.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(kmTF.textProperty().isEmpty());
		
		
//		this.setResultConverter(new Callback<ButtonType, >(){
//			@Override
//			public ReservationFX call(ButtonType bt) {
//				if(bt == ok && newKm <= oldKm) {
//					 Alert alertWarn= new Alert(AlertType.WARNING);
//						alertWarn.setTitle("Deleting a car");
//						alertWarn.setHeaderText("New km should be bigger than the old km!");
//						alertWarn.showAndWait();
//				}
//				return null;
//			}
//		});	
	
		
		
	}
	

}
