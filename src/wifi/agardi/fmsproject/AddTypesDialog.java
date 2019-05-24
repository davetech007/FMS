package wifi.agardi.fmsproject;

import java.sql.SQLException;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class AddTypesDialog extends Dialog<String> {

	public AddTypesDialog() {
		super();

		this.setTitle("Add types");
		this.setHeaderText("Add new types");

		ComboBox<String> typesComboBox = new ComboBox<>();
		typesComboBox.setItems(FXCollections.observableArrayList("Category", "FuelType", "Transmission", "CarColor",
															"CarFeature", "Nationality", "Insurance", "Res.Extra"));
		typesComboBox.getSelectionModel().selectFirst();
		typesComboBox.setPrefWidth(220);
		typesComboBox.setPromptText("Choose type");

		TextField nameTF = new TextField();
		nameTF.setPrefWidth(220);
		nameTF.promptTextProperty().bind(typesComboBox.valueProperty());

		TextField priceTF = new TextField();
		priceTF.setPromptText("Price in €");
		nameTF.setPrefWidth(220);

		priceTF.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d{0,5}?")) {
					priceTF.setText(oldValue);
				}
			}
		});

		typesComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals("FuelType") || newValue.equals("Transmission") || newValue.equals("CarColor")
						|| newValue.equals("CarFeature") || newValue.equals("Nationality")) {
					priceTF.setDisable(true);
				} else {
					priceTF.setDisable(false);
				}
			}
		});

		VBox vbox = new VBox(typesComboBox, nameTF, priceTF);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);

		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL;
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);

		this.getDialogPane().setContent(vbox);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());

		this.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(nameTF.textProperty().isEmpty());
			
		this.setResultConverter(new Callback<ButtonType, String>() {
			@Override
			public String call(ButtonType bt) {

				if (bt == ok) {
					try {
						String name = nameTF.getText();
						if (typesComboBox.getValue().equals("Category")) {
							if (priceTF.getText().isEmpty()) {
								Alert alertWarn = new Alert(AlertType.WARNING);
								alertWarn.setTitle("Adding new category");
								alertWarn.setHeaderText("Please check the category price again!");
								alertWarn.setContentText("Category must have a daily price in €!");
								alertWarn.showAndWait();
							}
							Database.addCarCategory(name, Integer.parseInt(priceTF.getText()));
							alert("category");
						}
						if (typesComboBox.getValue().equals("FuelType")) {
							Database.addCarFuelType(name);
							alert("fuel type");
						}
						if (typesComboBox.getValue().equals("Transmission")) {
							Database.addCarTransmissionType(name);
							alert("transmission");
						}
						if (typesComboBox.getValue().equals("CarColor")) {
							Database.addCarColor(name);
							alert("car color");
						}
						if (typesComboBox.getValue().equals("CarFeature")) {
							Database.addFeature(name);
							alert("car feature");
						}
						if (typesComboBox.getValue().equals("Nationality")) {
							Database.addNationality(name);
							alert("nationality");
						}
						if (typesComboBox.getValue().equals("Insurance")) {
							if (priceTF.getText().isEmpty()) {
								Alert alertWarn = new Alert(AlertType.WARNING);
								alertWarn.setTitle("Adding new insurance type");
								alertWarn.setHeaderText("Please check the insurane price again!");
								alertWarn.setContentText("Insurance must have a daily price in €!");
								alertWarn.showAndWait();
							}
							Database.addInsuranceType(name, Integer.parseInt(priceTF.getText()));
							alert("insurance");
						}
						if (typesComboBox.getValue().equals("Res.Extra")) {
							if (priceTF.getText().isEmpty()) {
								Alert alertWarn = new Alert(AlertType.WARNING);
								alertWarn.setTitle("Adding new reservation extra");
								alertWarn.setHeaderText("Please check the reservation extra price again!");
								alertWarn.setContentText("Reservation extra must have a (/rental) price in €!");
								alertWarn.showAndWait();
							}
							Database.addExtraType(name, Integer.parseInt(priceTF.getText()));
							alert("reservation extra");
						}
					} catch (SQLException e) {
						System.out.println("Something is wrong with the adding new types database connection");
						e.printStackTrace();
					}
				}
				return null;
			}
		});

	}

	private Alert alert(String s) {
		Alert alertWarn = new Alert(AlertType.WARNING);
		alertWarn.setTitle("Please restart!");
		alertWarn.setHeaderText("Please restart the program to see the changes!");
		alertWarn.setContentText("A new '" + s + "' has been successfully added!");
		alertWarn.showAndWait();
		return alertWarn;
	}
	
	
}
