package wifi.agardi.fmsproject;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class CustomerListDialog extends Dialog<ButtonType> {
	Image iconSearch = new Image(Main.class.getResource("/Search.png").toExternalForm(), 18, 18, true, true);
	
	public CustomerListDialog() {
		super();
		this.setTitle("Customer list");
		this.setHeaderText("Search for a customer");
		
		Label custSearchLB = new Label("Write a customer ID");
		
		TextField custIDTF = new TextField();
		custIDTF.setPromptText("Customer ID");
		custIDTF.setPrefWidth(150);
		
		Button custSearchBT = new Button("Search");
		custSearchBT.setPrefWidth(115);
		custSearchBT.setGraphic(new ImageView(iconSearch));
		
		HBox custHB= new HBox(custSearchLB, custIDTF, custSearchBT);
		custHB.setAlignment(Pos.CENTER);
		custHB.setSpacing(5);

		VBox custVB = new VBox(custHB, customersTableView());
		custVB.setSpacing(5);
		
		this.getDialogPane().setContent(custVB);
		this.setResizable(true);
		ButtonType ok = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
	}
	

	
	public TableView<String> customersTableView() {
	
	TableColumn<String, String> custIDCol = new TableColumn<>("Customer ID");
	custIDCol.setPrefWidth(100);
	custIDCol.setMinWidth(30);
    custIDCol.setCellValueFactory(new PropertyValueFactory<>("?"));

	TableColumn<String, String> firstNameCol = new TableColumn<>("First name");
	firstNameCol.setPrefWidth(110);
    firstNameCol.setMinWidth(30);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("?"));
	
    TableColumn<String, String> lastNameCol = new TableColumn<>("Last name");
    lastNameCol.setPrefWidth(110);
    lastNameCol.setMinWidth(30);
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("?"));
    
    TableColumn<String, String> nationalCol = new TableColumn<>("Nationality");
    nationalCol.setPrefWidth(100);
    nationalCol.setMinWidth(30);
    nationalCol.setCellValueFactory(new PropertyValueFactory<>("?"));
    
    TableColumn<String, String> dateOfBornCol = new TableColumn<>("Date of born");
    dateOfBornCol.setPrefWidth(100);
    nationalCol.setMinWidth(30);
    nationalCol.setCellValueFactory(new PropertyValueFactory<>("?"));
    
    TableColumn<String, String> passportCol = new TableColumn<>("Passport nr.");
    passportCol.setPrefWidth(100);
    passportCol.setMinWidth(30);
    passportCol.setCellValueFactory(new PropertyValueFactory<>("?"));

    TableColumn<String, String> driversLCol = new TableColumn<>("Driver's lic. nr.");
    driversLCol.setPrefWidth(100);
    driversLCol.setMinWidth(30);
    driversLCol.setCellValueFactory(new PropertyValueFactory<>("?"));
    
   
    TableView<String> customersTableView = new TableView<>();
    customersTableView.setPrefHeight(570);
    customersTableView.getColumns().addAll(custIDCol, firstNameCol, lastNameCol, nationalCol,dateOfBornCol, passportCol, driversLCol);

	
	return customersTableView;
	
	}

}
