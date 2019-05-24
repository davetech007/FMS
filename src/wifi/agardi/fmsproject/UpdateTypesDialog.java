package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.util.ArrayList;

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

public class UpdateTypesDialog extends Dialog<String> {

	public UpdateTypesDialog(ArrayList<String> categories, ArrayList<String> insurances, ArrayList<String> extras) {
		super();
		this.setTitle("Update types");

		ComboBox<String> typesComboBox = new ComboBox<>();
		typesComboBox.setItems(FXCollections.observableArrayList("Choose type", "Category", "Insurance", "Res.Extra"));
		typesComboBox.getSelectionModel().selectFirst();
		typesComboBox.setPrefWidth(220);
		this.headerTextProperty().bind(typesComboBox.valueProperty());

		ComboBox<String> newComboBox = new ComboBox<>();
		newComboBox.setDisable(true);
		newComboBox.setPrefWidth(160);

		TextField nameTF = new TextField();
		nameTF.setPrefWidth(200);
		nameTF.promptTextProperty().bind(typesComboBox.valueProperty());

		TextField priceTF = new TextField();
		priceTF.setPromptText("Price in €");
		nameTF.setPrefWidth(200);

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
				if (newValue.equals("Category")) {
					newComboBox.setItems(FXCollections.observableArrayList(categories));
					newComboBox.setDisable(false);
					nameTF.setText("");
					priceTF.setText("");
				}
				if (newValue.equals("Insurance")) {
					newComboBox.setItems(FXCollections.observableArrayList(insurances));
					newComboBox.setDisable(false);
					nameTF.setText("");
					priceTF.setText("");
				}
				if (newValue.equals("Res.Extra")) {
					newComboBox.setItems(FXCollections.observableArrayList(extras));
					newComboBox.setDisable(false);
					nameTF.setText("");
					priceTF.setText("");
				}
				if (newValue.equals("Choose type")) {
					newComboBox.setDisable(true);
					nameTF.setText("");
					priceTF.setText("");
				}
			}

		});

		newComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue != null && typesComboBox.getValue().equals("Category")) {
						nameTF.setText(newValue);
						for (String key : Database.readCarCategoriesTable().keySet()) {
							if (newValue.equals(key))
								priceTF.setText(Integer.toString(Database.readCarCategoriesTable().get(key)));
						}
					}
					if (newValue != null && typesComboBox.getValue().equals("Insurance")) {
						nameTF.setText(newValue);
						for (String key : Database.readInsurancesTable().keySet()) {
							if (newValue.equals(key))
								priceTF.setText(Integer.toString(Database.readInsurancesTable().get(key)));
						}
					}
					if (newValue != null && typesComboBox.getValue().equals("Res.Extra")) {
						nameTF.setText(newValue);
						for (String key : Database.readExtrasTable().keySet()) {
							if (newValue.equals(key))
								priceTF.setText(Integer.toString(Database.readExtrasTable().get(key)));
						}
					}
				} catch (SQLException e) {
					System.out.println("Something is wrong with the getCategoryPrice database connection");
					e.printStackTrace();
				}
			}

		});

		VBox vbox = new VBox(typesComboBox, newComboBox, nameTF, priceTF);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);

		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL;
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);

		this.getDialogPane().setContent(vbox);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());

		this.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
				.bind(nameTF.textProperty().isEmpty().or(priceTF.textProperty().isEmpty()));

		this.setResultConverter(new Callback<ButtonType, String>() {
			@Override
			public String call(ButtonType bt) {
				if (bt == ok) {
					try {
						String name = newComboBox.getValue();
						String newName = nameTF.getText();
						if (typesComboBox.getValue().equals("Category")) {
							Database.updateCarCategory(name, newName, Integer.parseInt(priceTF.getText()));
							alert("Category '" + name + "' to '" + newName);
						}
						if (typesComboBox.getValue().equals("Insurance")) {
							Database.updateInsuranceType(name, newName, Integer.parseInt(priceTF.getText()));
							alert("Insurance '" + name + "' to '" + newName);
						}
						if (typesComboBox.getValue().equals("Res.Extra")) {
							Database.updateExtraType(name, newName, Integer.parseInt(priceTF.getText()));
							alert("Reservation extra '" + name + "' to '" + newName);
						}
					} catch (SQLException e) {
						System.out.println("Something is wrong with updating types database connection");
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
		alertWarn.setContentText(s + "' has been successfully updated!");
		alertWarn.showAndWait();
		return alertWarn;
	}

}


