package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.derby.iapi.types.DataValueDescriptor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class CustomerListDialog extends Dialog<ButtonType> {
	DropShadow shadow = new DropShadow();
	
	private ObservableList<CustomerFX> customerObs;
	TableView<CustomerFX> customersTableView;
	private FilteredList<CustomerFX> filteredListCustomers;  //Filtered and sorted list are connected with the observable list, needed to make because the id search
	private SortedList<CustomerFX> sortedListCustomers;
	
	CustomerFX selectedCustomer;
	
	public CustomerListDialog() {
		super();
		this.setTitle("Customer list");
		this.setHeaderText("Search for a customer");
		fillCustomersObservableList();
		
		Label custSearchLB = new Label("Write a customer ID");
		custSearchLB.setId("searchLB");
		
		TextField custIDTF = new TextField();
		custIDTF.setPromptText("Customer ID");
		custIDTF.setId("searchTF");

		Button deleteCustomerButton = new Button("Delete");
		deleteCustomerButton.setPadding(new Insets(0, 0, 0, 20));
		deleteCustomerButton.setId("deleteCustomerButton");
		deleteCustomerButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  deleteCustomerButton.setEffect(shadow);
		          }
		        });
		deleteCustomerButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  deleteCustomerButton.setEffect(null);
		          }
		        });
		
		
		
		
		
		
		
		
		HBox custHB = new HBox(custSearchLB, custIDTF, deleteCustomerButton);
		custHB.setAlignment(Pos.CENTER);
		custHB.setSpacing(5);
		
		VBox custVB = new VBox(custHB, customersTableView());
		custVB.setSpacing(5);
		
		this.getDialogPane().setContent(custVB);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
		this.setResizable(true);
		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
	}
	

	
	public void fillCustomersObservableList() {
		customerObs = FXCollections.observableArrayList();
		  ArrayList<Customer> customers = new ArrayList<>();
			try {
				customers = Database.readCustomersTable();
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with customers database");
				e.printStackTrace();
			}
		    for(Customer c : customers) {
		    	customerObs.add(new CustomerFX(c));
		    }
		    filteredListCustomers = new FilteredList<>(customerObs, p -> true);
		    sortedListCustomers = new SortedList<>(filteredListCustomers);
		}
	
	
	
	public TableView<CustomerFX> customersTableView() {
	
	TableColumn<CustomerFX, String> custIDCol = new TableColumn<>("Customer ID");
	custIDCol.setPrefWidth(100);
	custIDCol.setMinWidth(30);
    custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

	TableColumn<CustomerFX, String> firstNameCol = new TableColumn<>("First name");
	firstNameCol.setPrefWidth(110);
    firstNameCol.setMinWidth(30);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	
    TableColumn<CustomerFX, String> lastNameCol = new TableColumn<>("Last name");
    lastNameCol.setPrefWidth(110);
    lastNameCol.setMinWidth(30);
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    
    TableColumn<CustomerFX, String> nationalCol = new TableColumn<>("Nationality");
    nationalCol.setPrefWidth(100);
    nationalCol.setMinWidth(30);
    nationalCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
    
    TableColumn<CustomerFX, String> dateOfBornCol = new TableColumn<>("Date of born");
    dateOfBornCol.setPrefWidth(100);
    dateOfBornCol.setMinWidth(30);
    dateOfBornCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBorn"));
    
    TableColumn<CustomerFX, String> passportCol = new TableColumn<>("Passport nr.");
    passportCol.setPrefWidth(100);
    passportCol.setMinWidth(30);
    passportCol.setCellValueFactory(new PropertyValueFactory<>("passportNum"));

    TableColumn<CustomerFX, String> driversLCol = new TableColumn<>("Driver's lic. nr.");
    driversLCol.setPrefWidth(100);
    driversLCol.setMinWidth(30);
    driversLCol.setCellValueFactory(new PropertyValueFactory<>("driversLicenseNum"));
    
   
    customersTableView = new TableView<>(customerObs);
    customersTableView.setPrefHeight(570);
    customersTableView.getColumns().addAll(custIDCol, firstNameCol, lastNameCol, nationalCol,dateOfBornCol, passportCol, driversLCol);
    customersTableView.setPlaceholder(new Label("Customer not found!"));
    
    sortedListCustomers.comparatorProperty().bind(customersTableView.comparatorProperty());
    customersTableView.setItems(sortedListCustomers);
	
	return customersTableView;
	
	}

}
