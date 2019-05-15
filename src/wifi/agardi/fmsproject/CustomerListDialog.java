package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.derby.iapi.types.DataValueDescriptor;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class CustomerListDialog extends Dialog<CustomerFX> {
	DropShadow shadow = new DropShadow();
	
	private ObservableList<CustomerFX> observCustomers;
	TableView<CustomerFX> customersTableView;
	private FilteredList<CustomerFX> filteredListCustomers;  //Filtered and sorted list are connected with the observable list, needed to make because the id search
	private SortedList<CustomerFX> sortedListCustomers;
	
	CustomerFX selectedCustomer;
	
	public CustomerListDialog() {
		super();
		this.setTitle("Customer list");
		this.setHeaderText("Search for a customer");
		fillCustomersObservableList();
		
		ComboBox<String> searchCB = new ComboBox<>();
		searchCB.setItems(FXCollections.observableArrayList("Searching criteria", "Customer ID", "First name", "Last name"));
		searchCB.getSelectionModel().selectFirst();
		searchCB.setId("carSearchBox");

		TextField custSearchTF = new TextField();
		custSearchTF.setDisable(true);
		custSearchTF.promptTextProperty().bind(searchCB.valueProperty());
		custSearchTF.setId("searchTF");
		
		Button deleteCustomerButton = new Button("Delete");
		deleteCustomerButton.setPadding(new Insets(0, 0, 0, 20));
		deleteCustomerButton.setId("deleteCustomerButton");
		deleteCustomerButton.setDisable(true);
//Animation shadow for delete button		
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
		
		HBox custHB = new HBox(searchCB, custSearchTF, deleteCustomerButton);
		custHB.setAlignment(Pos.CENTER);
		custHB.setSpacing(5);
		
		VBox custVB = new VBox(custHB, customersTableView());
		custVB.setSpacing(5);

//If a customer selected, delete button enabled		
		customersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if(newV != null) {
				selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
				deleteCustomerButton.setDisable(false);
			}
		});
		
//SEARHCING		
		searchCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.contains("Customer ID")) {
					custSearchTF.setDisable(false);
					custSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListCustomers.setPredicate(s -> true);
						}
						else {
							filteredListCustomers.setPredicate(s -> s.getModellObject().getCustomerID().toLowerCase().contains(newV));
						}
					});
				} 
				if(newValue.contains("First name")) {
					custSearchTF.setDisable(false);
					custSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListCustomers.setPredicate(s -> true);
						}
						else {
							filteredListCustomers.setPredicate(s -> s.getModellObject().getFirstName().toLowerCase().contains(newV));
							
						}
					});
				}
				if(newValue.contains("Last name")) {
					custSearchTF.setDisable(false);
					custSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListCustomers.setPredicate(s -> true);
						}
						else {
							filteredListCustomers.setPredicate(s -> s.getModellObject().getLastName().toLowerCase().contains(newV));
						}
					});	
				}
			}
		});
	
		
		
//DELETE Customer
		deleteCustomerButton.setOnAction(e -> {
			try {
				for(Reservation r : Database.readReservationsTable("active", "")) {
					if(r.getCustomer().getCustomerID().equals(selectedCustomer.getCustomerID())) {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Deleting a customer");
						alertWarn.setHeaderText("You can't delete this customer!");
						alertWarn.setContentText(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() +
												" still has a reservation (ID = '" + r.getResNumberID() + "').\n" +
												"You should first cancel the reservation.");
						alertWarn.showAndWait();
						return;
					}
				}
			} catch (SQLException e2) {
				System.out.println("Something is wrong with the customer delete");
				e2.printStackTrace();
			}

			Alert confirmDel = new Alert(AlertType.CONFIRMATION);
			confirmDel.setTitle("Delete customer");
			confirmDel.setHeaderText("Please confirm!");
			confirmDel.setContentText("Would you really want to DELETE this customer '" + 
									  selectedCustomer.getModellObject().getFirstName() + 
									  " " + selectedCustomer.getModellObject().getLastName() + "'?" +
									  "\n\nNote, that the customer stays in the database, and you can check his/her reservations from the past.");
			Optional<ButtonType> result = confirmDel.showAndWait();
			if(result.get() == ButtonType.OK) {
				try {
					Database.deleteCustomer(selectedCustomer.getModellObject().getCustomerID());
				} catch (SQLException e1) {
					System.out.println("Something is wrong with the customer delete database connection");
					e1.printStackTrace();
				}
				observCustomers.remove(selectedCustomer);
				selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
			}
		});
		    
		this.getDialogPane().setContent(custVB);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
		this.setResizable(true);
		ButtonType ok = ButtonType.OK;
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
		
//Return selected customer object
		this.setResultConverter(new Callback<ButtonType, CustomerFX>(){
			@Override
			public CustomerFX call(ButtonType bt) {
				if(bt == ok) {
					return selectedCustomer;
				}
				return null;
			}
		});	
	}
	
	
	public void fillCustomersObservableList() {
		observCustomers = FXCollections.observableArrayList();
		  ArrayList<Customer> customers = new ArrayList<>();
			try {
				customers = Database.readCustomersTable("active", "");
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with customers database");
				e.printStackTrace();
			}
		    for(Customer c : customers) {
		    	observCustomers.add(new CustomerFX(c));
		    }
		    filteredListCustomers = new FilteredList<>(observCustomers, p -> true);
		    sortedListCustomers = new SortedList<>(filteredListCustomers);
		}
	
	
	
	public TableView<CustomerFX> customersTableView() {
	
	TableColumn<CustomerFX, String> custIDCol = new TableColumn<>("Customer ID");
	custIDCol.setPrefWidth(140);
	custIDCol.setMinWidth(30);
    custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

	TableColumn<CustomerFX, String> firstNameCol = new TableColumn<>("First name");
	firstNameCol.setPrefWidth(130);
    firstNameCol.setMinWidth(30);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    firstNameCol.setSortType(TableColumn.SortType.ASCENDING);
	
    TableColumn<CustomerFX, String> lastNameCol = new TableColumn<>("Last name");
    lastNameCol.setPrefWidth(130);
    lastNameCol.setMinWidth(30);
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    firstNameCol.setSortType(TableColumn.SortType.ASCENDING);
    
    TableColumn<CustomerFX, String> nationalCol = new TableColumn<>("Nationality");
    nationalCol.setPrefWidth(100);
    nationalCol.setMinWidth(30);
    nationalCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
    
    TableColumn<CustomerFX, String> dateOfBornCol = new TableColumn<>("Date of born");
    dateOfBornCol.setPrefWidth(90);
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
    
   
    customersTableView = new TableView<>(observCustomers);
    customersTableView.setPrefHeight(570);
    customersTableView.getColumns().addAll(custIDCol, firstNameCol, lastNameCol, nationalCol,dateOfBornCol, passportCol, driversLCol);
    customersTableView.setPlaceholder(new Label("Customer not found!"));
    customersTableView.getSortOrder().addAll(firstNameCol, lastNameCol);
    
    sortedListCustomers.comparatorProperty().bind(customersTableView.comparatorProperty());
    customersTableView.setItems(sortedListCustomers);
	
	return customersTableView;
	
	}

}
