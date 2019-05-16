package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.apache.derby.iapi.store.raw.FetchDescriptor;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class Main extends Application {
	DropShadow shadow = new DropShadow();  //Effect for the buttons
	LocalDate minDate = LocalDate.of(1900, 01, 01);
	
	int minEngineSize = 500;
	int minEnginePower = 40;
	
	LocalDateTime ldtPickup;
	LocalDateTime ldtReturn;
	
	TabPane mainTabPane;
	Tab carsTab;
	Tab reserveTab;
	Tab reservationsTab;

	TableView<CarFX> carsTableView;
	CarFX selectedCar;
	private ObservableList<CarFX> observCars;
	private FilteredList<CarFX> filteredListCars;  //Filtered and sorted list are connected with the observable list, needed to make because the license plate search
	private SortedList<CarFX> sortedListCars;
	
	TextField carLicensePlateTF;
	ComboBox<String> carComboBox;

	Label custIdLabel;
	Label resIdLabel;
	Label rentStatusLabel;
	CustomerFX selectedCust;
	
	TableView<ReservationFX> reservTableView;
	ReservationFX selectedRes;
	private ObservableList<ReservationFX> observReservations;
	private FilteredList<ReservationFX> filteredListReservations;
	private SortedList<ReservationFX> sortedListReservations;
	
	

//LOGIN WINDOW	
	@Override
	public void start(Stage primaryStage) {
//Setting up LOGIN page		
			BorderPane loginBP = new BorderPane();
			loginBP.setPadding(new Insets(35, 35, 35, 35));
			GridPane loginGP = new GridPane();
			loginGP.setAlignment(Pos.CENTER);
			loginGP.setHgap(10);
			loginGP.setVgap(10);
			loginGP.setPadding(new Insets(15, 15, 15, 15));
			loginBP.setCenter(loginGP);
//Login Page BORDER PANE TOP TITLE		
			Label nameLabel = new Label("Fleet Management System");
			nameLabel.setId("nameLabel");
			HBox nameHBox = new HBox(10);
			nameHBox.setAlignment(Pos.CENTER);
			nameHBox.getChildren().add(nameLabel);
			loginBP.setTop(nameHBox);			
//User name			
			Label userNameLabel = new Label("User Name");
			loginGP.add(userNameLabel, 0, 2, 1, 1);
			
			TextField userNameTField = new TextField();
			userNameTField.setPrefSize(220, 30);
			loginGP.add(userNameTField, 0, 3, 1, 1);
//Password			
			Label passwordLabel = new Label("Password");
			loginGP.add(passwordLabel, 0, 4, 1, 1);
			
			PasswordField passwordField = new PasswordField();
			loginGP.add(passwordField, 0, 5, 1, 1);
//Button Log in			
			Button logInButton = new Button("Login");
			HBox logInHBox = new HBox(10);
			logInHBox.setAlignment(Pos.BOTTOM_CENTER);
			logInHBox.getChildren().add(logInButton);
			loginGP.add(logInHBox, 0, 6);

//ActionTarget			
			Label actionTarget = new Label();
			actionTarget.setId("actionTarget");
			HBox actionHBox = new HBox();
			actionHBox.setAlignment(Pos.BOTTOM_CENTER);
			actionHBox.getChildren().add(actionTarget);
			loginGP.add(actionHBox, 0, 8);
//LogIn on action			
			logInButton.setOnAction(new EventHandler<ActionEvent>() {	
				@Override
				public void handle(ActionEvent event) {
					try {
						if(Database.logIn(userNameTField.getText(), passwordField.getText())) {
						primaryStage.close();
						mainMenu();
						}
						else {
						actionTarget.setText("Login failed");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
			
//Sign up BORDER PANE BOTTOM
			Label signUpLabel = new Label("You don't have an account?");
			Button signUpButton = new Button("Sign up");
			
			VBox signUpVBox = new VBox(10);
			signUpVBox.setAlignment(Pos.BOTTOM_CENTER);	
			
			signUpVBox.getChildren().addAll(signUpLabel, signUpButton);
			loginBP.setBottom(signUpVBox);
			
			
//SignUp on action			
			signUpButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						if(Database.checkUser(userNameTField.getText())) {
							actionTarget.setId("actionTarget");
							actionTarget.setText("User already exists!");
							return;
						}
						if((userNameTField.getText().length() > 0) && (passwordField.getText().length() > 0)) {
							Alert alertSignUp = new Alert(AlertType.CONFIRMATION);
							alertSignUp.setTitle("Sign up");
							alertSignUp.setHeaderText("Please confirm!");
							alertSignUp.setContentText("Would you really want to sign up as '" + userNameTField.getText() + "'?");
							Optional<ButtonType> result = alertSignUp.showAndWait();
							if(result.get() == ButtonType.OK) {
								Database.signUp(userNameTField.getText(), passwordField.getText());
								actionTarget.setId("actionTargetSuccess");
								actionTarget.setText("Successfully signed up!");
							}
							else {
								actionTarget.setText("Signing up cancelled!");
							}
						} else {
						        actionTarget.setText("You should type min. 3 characters!");	
						}
					} catch (SQLException e) {
						System.out.println("Something is wrong with the users database");
						e.printStackTrace();
					}
				}
				
			});
					
			Scene scene = new Scene(loginBP, 700, 500);
			primaryStage.setScene(scene);
			loginBP.getStylesheets().add(Main.class.getResource("/Login.css").toExternalForm());
			primaryStage.setTitle("Welcome");
			primaryStage.show();	
	}
	


//MAIN WINDOW after login
	public void mainMenu() {
			Stage mainStage = new Stage();
			HBox mainHB = new HBox();
			mainHB.setPadding(new Insets(15, 15 , 12, 15));
			mainHB.setAlignment(Pos.CENTER);
//Main Title		
			Label mainTitle = new Label("Fleet Management System");
			mainTitle.setId("mainTitle");
			mainTitle.setPadding(new Insets(0, 0, 15, 0));
			HBox mainTopHBox = new HBox();
			mainTopHBox.setAlignment(Pos.CENTER);
			mainTopHBox.getChildren().add(mainTitle);
			
			VBox mainTopVBox = new VBox();
			mainTopVBox.getChildren().add(mainTopHBox);
			mainTopVBox.setAlignment(Pos.TOP_CENTER);
			mainHB.getChildren().add(mainTopVBox);
			
//Tab Pane
			reserveTab = new Tab("Reserve");
			reserveTab.setContent(openReserveMenu());
			reserveTab.setClosable(false);
			reserveTab.setId("reserveTab");
			
			fillCarsObservableList("active", "");
			carsTab = new Tab("Cars");
			carsTab.setContent(openCarsMenu());
			carsTab.setClosable(false);
			carsTab.setId("carsTab");
		
			fillReservationsObservableList();
			reservationsTab = new Tab("Reservaitons");
			reservationsTab.setContent(openReservationsMenu());
			reservationsTab.setClosable(false);
			reservationsTab.setId("reservationsTab");
		
			Tab dashboardTab = new Tab("Dashboard");
			dashboardTab.setClosable(false);
			dashboardTab.setId("dashboardTab");
	
			mainTabPane = new TabPane();
			mainTabPane.getTabs().addAll(reserveTab, carsTab, reservationsTab, dashboardTab);
			
	
//Tab on Action		
			reserveTab.setOnSelectionChanged(e -> {
				if(reserveTab.isSelected()) {
					mainTitle.setText("Make a reservation");
				}
			});  
			carsTab.setOnSelectionChanged(e -> {
				if(carsTab.isSelected()) {	
					mainTitle.setText("Manage cars");
				}
			});
			reservationsTab.setOnSelectionChanged(e -> {
				if(reservationsTab.isSelected()) {
					mainTitle.setText("Manage reservations");
				}
			});
			dashboardTab.setOnSelectionChanged(e -> {
				if(dashboardTab.isSelected()) {
					mainTitle.setText("Dashboard");
				}
			});
	
//Main VBox Top second Child			
			mainTopVBox.getChildren().add(mainTabPane);		
			Scene sceneMain = new Scene(mainHB, 1050, 750);
			mainHB.getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
			mainStage.setScene(sceneMain);
			mainStage.show();
	  }
	
	
	

	
//RESERVE MENU	
	public BorderPane openReserveMenu() {
		ArrayList<String> hours = new ArrayList<>();
		for(int i = 0; i<24; i++) {
			hours.add(Integer.toString(i));
		}
		ArrayList<String> minutes = new ArrayList<>();
			minutes.add("00");
			minutes.add("15");
			minutes.add("30");
			minutes.add("45");

		
			BorderPane reserveBP = new BorderPane();
			GridPane reserveGP = new GridPane();
			reserveGP.setAlignment(Pos.BOTTOM_CENTER);
			reserveGP.setPadding(new Insets(0, 30, 0, 25));
			reserveGP.setHgap(15);
			reserveGP.setVgap(10);
			reserveGP.setMinSize(0, 0);
			reserveBP.setCenter(reserveGP);
			//set the 3rd column's size to be automatic
			ColumnConstraints columnSpace = new ColumnConstraints();
			columnSpace.setHgrow(Priority.ALWAYS);				
			ColumnConstraints col1 = new ColumnConstraints();
			reserveGP.getColumnConstraints().addAll(col1, col1, columnSpace);
		
//GRIDPANE			
//Searching for a driver	
			Button searchDriverButton = new Button("Choose a driver");
			searchDriverButton.setId("searchDriverButton");
			reserveGP.add(searchDriverButton, 0, 0);
			
			searchDriverButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  searchDriverButton.setEffect(shadow);
			          }
			        });
			searchDriverButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  searchDriverButton.setEffect(null);
			          }
			        });
			
//Grid 0. column			
			Label driverLabel = new Label("Driver details");
			reserveGP.add(driverLabel, 0, 1);
			
			TextField firstNameTF = new TextField();
			firstNameTF.setPromptText("First name");
			reserveGP.add(firstNameTF, 0, 2);
			
			TextField lastNameTF = new TextField();
			lastNameTF.setPromptText("Last name");
			reserveGP.add(lastNameTF, 0, 3);
			
			DatePicker dateBornPicker = new DatePicker();
			dateBornPicker.setPromptText("Date of Born");
			dateBornPicker.setDayCellFactory(dp -> new DateCellFactory(minDate, LocalDate.now()));
			reserveGP.add(dateBornPicker, 0, 4);
			
//NATIONALITY		
			ComboBox<String> nationalityComboBox = new ComboBox<>();
			   try {
			    	ArrayList<String> nationalities = Database.readNationalitiesTable();
			    	Collections.sort(nationalities);
			    	nationalityComboBox.setItems(FXCollections.observableArrayList(nationalities));
				} catch (SQLException e1) {
					System.out.println("Nationalities database to combobox adding failed...");
					e1.printStackTrace();
				} 
			nationalityComboBox.setPrefWidth(195);
			nationalityComboBox.setPromptText("Choose nationality");
			reserveGP.add(nationalityComboBox, 0, 5);
			
			TextField passportTF = new TextField();
			passportTF.setPromptText("Passport nr.");
			reserveGP.add(passportTF, 0, 6);
			
			TextField dLicenseTF = new TextField();
			dLicenseTF.setPromptText("Driver's license nr.");
			reserveGP.add(dLicenseTF, 0, 7);
			
			
//Grid 1. column	
			custIdLabel = new Label("ID = ");
			reserveGP.add(custIdLabel, 1, 0);
			
			Label addressLabel = new Label("Address / Contact");
			reserveGP.add(addressLabel, 1, 1);
			
			TextField landTF = new TextField();
			landTF.setPrefWidth(195);
			landTF.setPromptText("Land");
			reserveGP.add(landTF, 1, 2);
			
			TextField cityTF = new TextField();
			cityTF.setPromptText("City");
			reserveGP.add(cityTF, 1, 3);
			
			TextField streetTF = new TextField();
			streetTF.setPromptText("Street, house nr./door");
			reserveGP.add(streetTF, 1, 4);
					
			TextField postCodeTF = new TextField();
			postCodeTF.setPromptText("Postal code");
			reserveGP.add(postCodeTF, 1, 5);
						
			TextField telefonTF = new TextField();
			telefonTF.setId("telefonTF");
			telefonTF.setPromptText("Telefon nr.");
			reserveGP.add(telefonTF, 1, 6);
			
			TextField emailTF = new TextField();
			emailTF.setId("emailTF");
			emailTF.setPromptText("E-mail");
			reserveGP.add(emailTF, 1, 7);
			
//Grid 3. column
//Searching for a car	
			//It opens CARS menu to choose a car, then fills the license plate / brand
			Button chooseCarButton = new Button("Choose a car  ");
			chooseCarButton.setId("chooseCarButton");
			reserveGP.add(chooseCarButton, 3, 0);
			chooseCarButton.setOnAction(e ->{
				mainTabPane.getSelectionModel().select(carsTab);
			});
			
			chooseCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  chooseCarButton.setEffect(shadow);
			          }
			        });
			chooseCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  chooseCarButton.setEffect(null);
			          }
			        });
	
			Label carLabel = new Label("Category");
			reserveGP.add(carLabel, 3, 1);
			
			carComboBox = new ComboBox<>(FXCollections.observableArrayList(categoriesList()));
			carComboBox.setId("carSearchBox");
			carComboBox.setPromptText("Category");
			reserveGP.add(carComboBox, 3, 2);
			
			
//PICKUP DETAILS
			Label pickupLabel = new Label("Pick up");
			reserveGP.add(pickupLabel, 3, 3);
			
			TextField pickupLocTF = new TextField();
			pickupLocTF.setPromptText("Pickup location");
			reserveGP.add(pickupLocTF, 3, 4);
			
			DatePicker datePickupPicker = new DatePicker();
			datePickupPicker.setPromptText("Pickup date");
			reserveGP.add(datePickupPicker, 3, 5);
			
			HBox pickupHBox = new HBox();
			pickupHBox.setSpacing(5);
			pickupHBox.setAlignment(Pos.CENTER_LEFT);
			ComboBox<String> pickupHourCB = new ComboBox<>(FXCollections.observableArrayList(hours));
			Label pickupHourLB = new Label("hrs.");
			pickupHourLB.setAlignment(Pos.CENTER);
			pickupHourCB.setMaxWidth(65);
			ComboBox<String> pickupMinCB = new ComboBox<>(FXCollections.observableArrayList(minutes));
			pickupMinCB.setMaxWidth(65);
			Label pickupMinuteLB = new Label("min.");
			pickupHBox.getChildren().addAll(pickupHourCB,pickupHourLB, pickupMinCB, pickupMinuteLB);
			reserveGP.add(pickupHBox, 3, 6);		
			
			//Notes, Comments			
			Label notesLabel2 = new Label("Notes");
			reserveGP.add(notesLabel2, 3, 7);
			
			TextArea notesTA2 = new TextArea();
			notesTA2.setPromptText("Comments, category wish,...");
			notesTA2.setPrefSize(195, 100);
			reserveGP.add(notesTA2, 3, 8);
			notesTA2.setOnKeyTyped(e -> {
			    	int maxChar = 299;
			    	if(notesTA2.getText().length() == maxChar) 
			    	e.consume();
			    });
			
//Calculate price button			
			Button calcPriceButton = new Button("Calculate price");
			calcPriceButton.setId("calcPriceButton");
			reserveGP.add(calcPriceButton, 3, 10);

			calcPriceButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  calcPriceButton.setEffect(shadow);
			          }
			        });
			calcPriceButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  calcPriceButton.setEffect(null);
			          }
			        });
			
//Grid 4. column
			carLicensePlateTF = new TextField();
			carLicensePlateTF.setPrefWidth(130);
			carLicensePlateTF.setEditable(false);
			carLicensePlateTF.setPromptText("License plate");
			
			Button clearLpBT = new Button("Clear");
			clearLpBT.setDisable(true);
			clearLpBT.setPrefWidth(65);
			clearLpBT.setOnAction(e -> {
				carLicensePlateTF.setText("");
			});
			
			HBox licPHB = new HBox(carLicensePlateTF, clearLpBT);
			reserveGP.add(licPHB, 4, 0);

			Label dateLabel = new Label("Insurance");
			reserveGP.add(dateLabel, 4, 1);
//INSURANCE		
			ComboBox<String> insuranceComboBox = new ComboBox<>(FXCollections.observableArrayList(insurancesList()));
			insuranceComboBox.setPromptText("Choose insurance");
			insuranceComboBox.setPrefWidth(195);
			reserveGP.add(insuranceComboBox, 4, 2);
	
			
//RETURN DETAILS			
			Label returnLabel = new Label("Return");
			reserveGP.add(returnLabel, 4, 3);
			
			TextField returnLocTF = new TextField();
			returnLocTF.setPromptText("Return location");
			reserveGP.add(returnLocTF, 4, 4);
			
			DatePicker dateReturnPicker = new DatePicker();
			dateReturnPicker.setDisable(true);
			dateReturnPicker.setPromptText("Return date");
			reserveGP.add(dateReturnPicker, 4, 5);
			
			HBox returnHBox = new HBox();
			returnHBox.setSpacing(5);
			returnHBox.setAlignment(Pos.CENTER_LEFT);
			ComboBox<String> returnHourCB = new ComboBox<>(FXCollections.observableArrayList(hours));
			returnHourCB.setMaxWidth(65);
			Label returnHourLB = new Label("hrs.");
			ComboBox<String> returnMinCB = new ComboBox<>(FXCollections.observableArrayList(minutes));
			returnMinCB.setMaxWidth(65);
			Label returnMinuteLB = new Label("min.");
			returnHBox.getChildren().addAll(returnHourCB, returnHourLB, returnMinCB, returnMinuteLB);
			reserveGP.add(returnHBox, 4, 6);	
			
			datePickupPicker.setDayCellFactory(dp -> new DateCellFactory(LocalDate.now(), LocalDate.MAX));
			dateReturnPicker.disableProperty().bind(datePickupPicker.valueProperty().isNull());
			
			datePickupPicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
				@Override
				public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
						LocalDate newValue) {
					dateReturnPicker.setDayCellFactory(dp -> new DateCellFactory(newValue, LocalDate.MAX));
				}
			});		
			
//EXTRAS LISTVIEW		
    		   LinkedHashMap<String, ObservableValue<Boolean>> extrasMap = new LinkedHashMap<>();
				      for(String e : extrasList()) {
				    	  extrasMap.put(e, new SimpleBooleanProperty(false));
					  }			 
				      
			    Label extrasLB = new Label("Extras");
   			    reserveGP.add(extrasLB, 4, 7);
			   
			    ListView<String> extrasLV = new ListView<>();
			    extrasLV.setEditable(true);
			    extrasLV.getItems().addAll(extrasMap.keySet());
			    
			    Callback<String, ObservableValue<Boolean>> itemToBoolean = (String item) -> extrasMap.get(item);
			    extrasLV.setCellFactory(CheckBoxListCell.forListView(itemToBoolean));
			    extrasLV.setPrefSize(195, 100);
			    reserveGP.add(extrasLV, 4, 8);
		
//PRICE		    
			VBox priceVBox = new VBox();
			resIdLabel =           new Label("ResID  = ");
			rentStatusLabel =      new Label("Status = ");
			Label priceDaysLabel = new Label("Rent   = ");
			Label priceLabelIns =  new Label("Ins.   = "); 
			Label priceLabel2 =    new Label("Extras = ");
			Label priceLabel3 =    new Label("Total  = ");
			priceVBox.getChildren().addAll(resIdLabel, rentStatusLabel, priceDaysLabel, priceLabelIns, priceLabel2, priceLabel3);
			reserveGP.add(priceVBox, 4, 10);
			
			
			
//Reserve BorderPane BOTTOM BUTTONS
//DESIGN, Disable property
			ObservableValue<Boolean> custObs = firstNameTF.textProperty().isEmpty()
					.or(lastNameTF.textProperty().isEmpty())
					.or(dateBornPicker.valueProperty().isNull())
					.or(nationalityComboBox.valueProperty().isNull())
					.or(passportTF.textProperty().isEmpty())
					.or(dLicenseTF.textProperty().isEmpty())
					.or(telefonTF.textProperty().isEmpty())
					.or(emailTF.textProperty().isEmpty())
					.or(landTF.textProperty().isEmpty())
					.or(cityTF.textProperty().isEmpty())
					.or(streetTF.textProperty().isEmpty())
					.or(postCodeTF.textProperty().isEmpty());
		
		Button saveCustomerButton = new Button("Add");
			saveCustomerButton.setId("saveCustomerButton");	
			saveCustomerButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  saveCustomerButton.setEffect(shadow);
			          }
			        });
			saveCustomerButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  saveCustomerButton.setEffect(null);
			          }
			        });
			saveCustomerButton.disableProperty().bind(custObs);
			
		Button updateCustomerButton = new Button("Update");
			updateCustomerButton.setId("updateCustomerButton");	
			updateCustomerButton.setDisable(true);
			updateCustomerButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateCustomerButton.setEffect(shadow);
			          }
			        });
			updateCustomerButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateCustomerButton.setEffect(null);
			          }
			        });
			
			
		Button updateResButton = new Button("Update");
			updateResButton.setId("updateResButton");
			updateResButton.setDisable(true);
			updateResButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateResButton.setEffect(shadow);
			          }
			        });
			updateResButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateResButton.setEffect(null);
			          }
			        });

			
		Button reserveButton = new Button("New");
			reserveButton.setId("saveResButton");
			reserveButton.setDisable(true);
			reserveButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  reserveButton.setEffect(shadow);
			          }
			        });
			reserveButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  reserveButton.setEffect(null);
			          }
			        });
		

//SearchDriver ACTION			
			searchDriverButton.setOnAction(e ->{
				CustomerListDialog custList = new CustomerListDialog();
				Optional<CustomerFX> result = custList.showAndWait();
				if(result.isPresent()) {
					selectedCust = result.get();
					custIdLabel.setText("ID = " + selectedCust.getModellObject().getCustomerID());
					firstNameTF.setText(selectedCust.getModellObject().getFirstName());
					lastNameTF.setText(selectedCust.getModellObject().getLastName());
					dateBornPicker.setValue(selectedCust.getModellObject().getDateOfBorn());
					nationalityComboBox.setValue(selectedCust.getModellObject().getNationality());
					passportTF.setText(selectedCust.getModellObject().getPassportNum());
					dLicenseTF.setText(selectedCust.getModellObject().getDriversLicenseNum());
					telefonTF.setText(selectedCust.getModellObject().getTelefon());
					emailTF.setText(selectedCust.getModellObject().geteMail());
					landTF.setText(selectedCust.getModellObject().getAddressLand());
					cityTF.setText(selectedCust.getModellObject().getAddressCity());
					streetTF.setText(selectedCust.getModellObject().getAddressStreet());
					postCodeTF.setText(selectedCust.getModellObject().getAddressPostalCode());	
					updateCustomerButton.setDisable(false);
				}
			});	
	
			
//SAVE CUSTOMER ON ACTION			
			saveCustomerButton.setOnAction(e -> {
			   try {
				String customerID = "CID" + String.valueOf(System.currentTimeMillis() / 1000);
				String firstName = firstNameTF.getText().substring(0, 1).toUpperCase() + firstNameTF.getText().substring(1);
				String lastName = lastNameTF.getText().substring(0, 1).toUpperCase() + lastNameTF.getText().substring(1);
				LocalDate dateOfBorn = dateBornPicker.getValue();
				String nationality = nationalityComboBox.getSelectionModel().getSelectedItem().toString();
				String passportNum = passportTF.getText().toUpperCase();
				String driversLicenseNum = dLicenseTF.getText().toUpperCase();
				String telefon = telefonTF.getText().toUpperCase();
				String eMail = emailTF.getText().toLowerCase();
				String addressLand = landTF.getText().substring(0, 1).toUpperCase() + landTF.getText().substring(1);
				String addressCity = cityTF.getText().substring(0, 1).toUpperCase() + cityTF.getText().substring(1);
				String addressStreet = streetTF.getText().substring(0, 1).toUpperCase() + streetTF.getText().substring(1);
				String addressPostalCode = postCodeTF.getText().toUpperCase();
				
				CustomerFX newCustomer = new CustomerFX(new Customer(customerID, firstName, lastName, dateOfBorn,
														nationality, passportNum, driversLicenseNum, telefon, eMail,
														addressLand, addressCity, addressStreet, addressPostalCode));
			
				if(Database.checkExistingCustomer(passportNum, "", "") != "") {
					Alert alertWarn= new Alert(AlertType.WARNING);
					alertWarn.setTitle("Adding a customer");
					alertWarn.setHeaderText("Please check again, PASSPORT number already exists!");
					alertWarn.setContentText("You wanted to add a new customer '"+ firstName + " " + lastName + 
											 "', but his/her passport number '" + passportNum +
											 "' already exists under this name '" + 
											 Database.checkExistingCustomer(passportNum, "", "") + "'");
					alertWarn.showAndWait();
					return;
				}
				if(Database.checkExistingCustomer("", driversLicenseNum, "") != "") {
					Alert alertWarn= new Alert(AlertType.WARNING);
					alertWarn.setTitle("Adding a customer");
					alertWarn.setHeaderText("Please check again, DRIVER'S LICENSE number already exists!");
					alertWarn.setContentText("You wanted to add a new customer '"+ firstName + " " + lastName + 
											 "', but his/her driver's license number '" + driversLicenseNum +
											 "' already exists under this name '" +
											 Database.checkExistingCustomer("", driversLicenseNum, "") + "'");
					alertWarn.showAndWait();
					return;
				}
				 if (!(eMail.length() > 3 && eMail.contains("@") && eMail.contains("."))){
					 Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a customer");
						alertWarn.setHeaderText("Please check again, e-mail address looks invalid!");
						alertWarn.setContentText("Please give a real e-mail address (contains '@' and '.')!");
						alertWarn.showAndWait();
						return;
				 }
				else {
					Alert confirmAdd = new Alert(AlertType.CONFIRMATION);
					confirmAdd.setTitle("Adding a new customer");
					confirmAdd.setHeaderText("Please confirm!");
					confirmAdd.setContentText("Would you really want to ADD a new customer '" + firstName + " " + lastName + "'?");
					Optional<ButtonType> result = confirmAdd.showAndWait();
					if(result.get() == ButtonType.OK) {
					Database.addNewCustomer(newCustomer.getModellObject());
					custIdLabel.setText("ID = " + newCustomer.getModellObject().getCustomerID());
					selectedCust = newCustomer;
					}
				}
			  } catch (SQLException e1) {
				  System.out.println("Something is wrong with the database - add custormer");
				  e1.printStackTrace();
			  }
				
			});
//UPDATE CUSTOMER ON ACTION			
			updateCustomerButton.setOnAction(e -> {
			 try {
				String customerID = selectedCust.getModellObject().getCustomerID();
				String firstName = firstNameTF.getText().substring(0, 1).toUpperCase() + firstNameTF.getText().substring(1);
				String lastName = lastNameTF.getText().substring(0, 1).toUpperCase() + lastNameTF.getText().substring(1);
				LocalDate dateOfBorn = dateBornPicker.getValue();
				String nationality = nationalityComboBox.getSelectionModel().getSelectedItem().toString();
				String passportNum = passportTF.getText().toUpperCase();
				String driversLicenseNum = dLicenseTF.getText().toUpperCase();
				String telefon = telefonTF.getText().toUpperCase();
				String eMail = emailTF.getText().toLowerCase();
				String addressLand = landTF.getText().substring(0, 1).toUpperCase() + landTF.getText().substring(1);
				String addressCity = cityTF.getText().substring(0, 1).toUpperCase() + cityTF.getText().substring(1);
				String addressStreet = streetTF.getText().substring(0, 1).toUpperCase() + streetTF.getText().substring(1);
				String addressPostalCode = postCodeTF.getText().toUpperCase();
				
				CustomerFX updateCustomer = new CustomerFX(new Customer(customerID, firstName, lastName, dateOfBorn,
														nationality, passportNum, driversLicenseNum, telefon, eMail,
														addressLand, addressCity, addressStreet, addressPostalCode));
				
				Alert confirmUpdate = new Alert(AlertType.CONFIRMATION);
				confirmUpdate.setTitle("Updating customer");
				confirmUpdate.setHeaderText("Please confirm!");
				confirmUpdate.setContentText("Would you really want to UPDATE the selected customer?\n\n" +
											"Customer with this ID '" + customerID + "' exists under this name '" +
											 Database.checkExistingCustomer("", "", customerID) + "'.\n\n" +
											 "Update to '" + firstName + " " + lastName + "'?");
				Optional<ButtonType> result = confirmUpdate.showAndWait();
				if(result.get() == ButtonType.OK) {
					Database.updateCustomer(updateCustomer.getModellObject());
					selectedCust = updateCustomer;
				}
				
			 } catch (SQLException e1) {
					  System.out.println("Something is wrong with the database - update custormer");
					  e1.printStackTrace();
				  }
			});
			
//RESERVE BUTTON ON ACTION	
			//Enable the calculate price button
			ObservableValue<Boolean> calcObs = carComboBox.valueProperty().isNull().
					    or(insuranceComboBox.valueProperty().isNull()).
						or(datePickupPicker.valueProperty().isNull()).
						or(pickupHourCB.valueProperty().isNull()).
						or(pickupMinCB.valueProperty().isNull()).
						or(dateReturnPicker.valueProperty().isNull()).
						or(returnHourCB.valueProperty().isNull()).
						or(returnMinCB.valueProperty().isNull());
			
			//Enable with calculate price + other details the reserve button 
			ObservableValue<Boolean> resCustObs = custIdLabel.textProperty().length().lessThan(6).
															or(carLicensePlateTF.textProperty().isEmpty()).
															or(pickupLocTF.textProperty().isEmpty()).
															or(returnLocTF.textProperty().isEmpty()).
															or((ObservableBooleanValue) calcObs);
			reserveButton.disableProperty().bind(resCustObs);
			
			
			
			
//CALCULATE PRICE ON ACTION	
			calcPriceButton.disableProperty().bind(calcObs);
			
			calcPriceButton.setOnAction(e -> {
				ldtPickup = LocalDateTime.of(datePickupPicker.getValue().getYear(),
						datePickupPicker.getValue().getMonth(),
						datePickupPicker.getValue().getDayOfMonth(),
						Integer.parseInt(pickupHourCB.getValue()),
						Integer.parseInt(pickupMinCB.getValue()));
				
			    ldtReturn = LocalDateTime.of(dateReturnPicker.getValue().getYear(),
						dateReturnPicker.getValue().getMonth(),
						dateReturnPicker.getValue().getDayOfMonth(),
						Integer.parseInt(returnHourCB.getValue()),
						Integer.parseInt(returnMinCB.getValue()));
			    
			    long days = Duration.between(ldtPickup, ldtReturn).toDays() + 1;
			    int dailyPrice = getCategoryPrice(carComboBox.getValue());
			    int insurancePrice = (int) (getInsurancePrice(insuranceComboBox.getValue()) * days);
			    int rentPrice = (int) (days * dailyPrice);
			    
			    int extraPrices = 0;
				for(String key : extrasMap.keySet()) {
					ObservableValue<Boolean> val = extrasMap.get(key);
					if(val.getValue()) {
						extraPrices += getExtraPrice(key);
					}
				}
				
				priceDaysLabel.setText("Rent = " + days + " days*" + dailyPrice + " = " + rentPrice + " €");
			    priceLabelIns.setText("Ins.   = " + insurancePrice + " €");
				priceLabel2.setText("Extras = " + extraPrices + " €");
				priceLabel3.setText("Total  = " + (rentPrice + extraPrices + insurancePrice) + " €");
			
			});
			
			
			
			
//RES ID LABEL WHEN A RESERVATION IS SELECTED, set selected CUSTOMER and CAR 			
			resIdLabel.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(newValue.length() > 8) {
						try {
							for(Customer s : Database.readCustomersTable("", "")) {
								if(s.getCustomerID().equals(selectedRes.getModellObject().getCustomer().getCustomerID())) {
									selectedCust = new CustomerFX(s);
								}
							}
							for(Car c : Database.readCarsTable("", "")) {
								if(c.getCarVinNumber().equals(selectedRes.getModellObject().getCar().getCarVinNumber())) {
									selectedCar = new CarFX(c);
								}
							}
							
						} catch (SQLException e1) {
							System.out.println("Something is wrong with the reading customers");
							e1.printStackTrace();
						}
						custIdLabel.setText("ID = " + selectedRes.getModellObject().getCustomer().getCustomerID());
						firstNameTF.setText(selectedRes.getModellObject().getCustomer().getFirstName());
					    lastNameTF.setText(selectedRes.getModellObject().getCustomer().getLastName());
					    dateBornPicker.setValue(selectedRes.getModellObject().getCustomer().getDateOfBorn());
					    nationalityComboBox.setValue(selectedRes.getModellObject().getCustomer().getNationality());
					    passportTF.setText(selectedRes.getModellObject().getCustomer().getPassportNum());
					    dLicenseTF.setText(selectedRes.getModellObject().getCustomer().getDriversLicenseNum());
					    landTF.setText(selectedRes.getModellObject().getCustomer().getAddressLand());
					    cityTF.setText(selectedRes.getModellObject().getCustomer().getAddressCity());
					    streetTF.setText(selectedRes.getModellObject().getCustomer().getAddressStreet());
					    postCodeTF.setText(selectedRes.getModellObject().getCustomer().getAddressPostalCode());
					    telefonTF.setText(selectedRes.getModellObject().getCustomer().getTelefon());
					    emailTF.setText(selectedRes.getModellObject().getCustomer().geteMail());
					    carComboBox.setValue(selectedRes.getModellObject().getReservedCategory());
					    carLicensePlateTF.setText(selectedRes.getModellObject().getCar().getCarLicensePlate());
					    insuranceComboBox.setValue(selectedRes.getModellObject().getInsuranceType());
					    pickupLocTF.setText(selectedRes.getModellObject().getPickupLocation());
					    returnLocTF.setText(selectedRes.getModellObject().getReturnLocation());
					    datePickupPicker.setValue(selectedRes.getModellObject().getPickupTime().toLocalDate());
					    dateReturnPicker.setValue(selectedRes.getModellObject().getReturnTime().toLocalDate());
					    pickupHourCB.setValue(String.valueOf(selectedRes.getModellObject().getPickupTime().getHour()));
					    pickupMinCB.setValue(String.valueOf(selectedRes.getModellObject().getPickupTime().getMinute()));
					    returnHourCB.setValue(String.valueOf(selectedRes.getModellObject().getReturnTime().getHour()));
					    returnMinCB.setValue(String.valueOf(selectedRes.getModellObject().getReturnTime().getMinute()));
					    notesTA2.setText(selectedRes.getModellObject().getResNotes());

						ArrayList<String> extras = extrasList();
						    Collections.sort(extras);
								for(String e : extras) {
									  extrasLV.getItems().clear();
									  extrasMap.put(e, new SimpleBooleanProperty(false));
									  extrasLV.getItems().addAll(extrasMap.keySet());
							     }			 
						for(String key : extrasMap.keySet()) {
							for(String s: selectedRes.getModellObject().getResExtras()) {
								if(key.equals(s)) {
									extrasLV.getItems().clear();
									extrasMap.put(s, new SimpleBooleanProperty(true));
									extrasLV.getItems().addAll(extrasMap.keySet());
								}
							} 
						}
						updateResButton.setDisable(false); 
						calcPriceButton.fire();
					}
				}
				
			});
			
			
			
//RESERVE BUTTON ON ACTION			
			reserveButton.setOnAction(e -> {
			  try {	
				String resNumberID = "RESID" + String.valueOf(System.currentTimeMillis() / 1000);
				String reservedCategory = carComboBox.getSelectionModel().getSelectedItem().toString();
				String insuranceType = insuranceComboBox.getSelectionModel().getSelectedItem().toString();
				String pickupLocation = pickupLocTF.getText();
				LocalDateTime pickupTime = LocalDateTime.of(datePickupPicker.getValue().getYear(),
															datePickupPicker.getValue().getMonth(),
															datePickupPicker.getValue().getDayOfMonth(),
															Integer.parseInt(pickupHourCB.getValue()), 
															Integer.parseInt(pickupMinCB.getValue()));
				String returnLocation = returnLocTF.getText();
				LocalDateTime returnTime = LocalDateTime.of(dateReturnPicker.getValue().getYear(),
															dateReturnPicker.getValue().getMonth(),
															dateReturnPicker.getValue().getDayOfMonth(),
															Integer.parseInt(returnHourCB.getValue()), 
															Integer.parseInt(returnMinCB.getValue()));
				String resNotes = notesTA2.getText();
				
				ArrayList<String> extras = new ArrayList<>();
				for(String key : extrasMap.keySet()) {
					ObservableValue<Boolean> val = extrasMap.get(key);
					if(val.getValue()) {
						extras.add(key);
					}
				}
				ReservationFX newRes = new ReservationFX(new Reservation(resNumberID, selectedCust.getModellObject(), 
											selectedCar.getModellObject(), reservedCategory, insuranceType, pickupLocation,
											pickupTime, returnLocation, returnTime, resNotes, extras, false));
				

				for(Reservation r : Database.readReservationsTable("active", "")) {
					if(selectedCar.getModellObject().getCarVinNumber().equals(r.getCar().getCarVinNumber())) {
						if(r.getReturnTime().isAfter(pickupTime) && r.getPickupTime().isBefore(returnTime)) {
						    Alert alertWarn= new Alert(AlertType.WARNING);
							alertWarn.setTitle("Make a reservation");
							alertWarn.setHeaderText("You can't reserve this car!");
							alertWarn.setContentText(selectedCar.getCarLicensePlate() + " has a reservation around this time. Please choose another date!\n\n" +
													 "Reservation ID = '" + r.getResNumberID() + "'\n" +
													 "From '" + r.getPickupTime() + "' until '" + r.getReturnTime() + "'");
							alertWarn.showAndWait();
						    return;
							}
						} 
					}

				Alert alertAdd = new Alert(AlertType.CONFIRMATION);
				alertAdd.setTitle("Adding a new reservation");
				alertAdd.setHeaderText("Please confirm!");
				alertAdd.setContentText("Would you really want to ADD this new reservation?\n\n" +
										"Reserved category: '" + reservedCategory + "'\n" + 
										"License plate: '" + selectedCar.getCarLicensePlate() + "'\n" +
										"For: '" + selectedCust.getModellObject().getFirstName() + " " +
												   selectedCust.getModellObject().getLastName() +"'");
				Optional<ButtonType> result = alertAdd.showAndWait();
				if(result.get() == ButtonType.OK) {
						Database.addNewReservation(newRes.getModellObject());
						observReservations.add(newRes);
						selectedRes = newRes;
						resIdLabel.setText("ResID = " + selectedRes.getModellObject().getResNumberID());
						rentStatusLabel.setText("Status = " + selectedRes.getStatusName());
					}
				
		   } catch (SQLException e1) {
			   System.out.println("Something is wrong with the database - adding a new reservation");
			   e1.printStackTrace();
		}		
	});
				
			
//UPDATE RES BUTTON ON ACTION
			updateResButton.setOnAction(e -> {
				  try {	
						String resNumberID = selectedRes.getModellObject().getResNumberID();
						String reservedCategory = carComboBox.getSelectionModel().getSelectedItem().toString();
						String insuranceType = insuranceComboBox.getSelectionModel().getSelectedItem().toString();
						String pickupLocation = pickupLocTF.getText();
						LocalDateTime pickupTime = LocalDateTime.of(datePickupPicker.getValue().getYear(),
																	datePickupPicker.getValue().getMonth(),
																	datePickupPicker.getValue().getDayOfMonth(),
																	Integer.parseInt(pickupHourCB.getValue()), 
																	Integer.parseInt(pickupMinCB.getValue()));
						String returnLocation = returnLocTF.getText();
						LocalDateTime returnTime = LocalDateTime.of(dateReturnPicker.getValue().getYear(),
																	dateReturnPicker.getValue().getMonth(),
																	dateReturnPicker.getValue().getDayOfMonth(),
																	Integer.parseInt(returnHourCB.getValue()), 
																	Integer.parseInt(returnMinCB.getValue()));
						String resNotes = notesTA2.getText();
						
						ArrayList<String> extras = new ArrayList<>();
						for(String key : extrasMap.keySet()) {
							ObservableValue<Boolean> val = extrasMap.get(key);
							if(val.getValue()) {
								extras.add(key);
							}
						}
						ReservationFX updateRes = new ReservationFX(new Reservation(resNumberID, selectedCust.getModellObject(), 
												selectedCar.getModellObject(), reservedCategory, insuranceType, pickupLocation, 
												pickupTime, returnLocation, returnTime, resNotes, extras, false));
				
						
						for(Reservation r : Database.readReservationsTable("active", "")) {
							if((selectedCar.getModellObject().getCarVinNumber().equals(r.getCar().getCarVinNumber()) && !resNumberID.equals(r.getResNumberID()))) {
								if(r.getReturnTime().isAfter(pickupTime) && r.getPickupTime().isBefore(returnTime)) {
								    Alert alertWarn= new Alert(AlertType.WARNING);
									alertWarn.setTitle("Update a reservation");
									alertWarn.setHeaderText("You can't update this reservation with this car!");
									alertWarn.setContentText(selectedCar.getCarLicensePlate() + " has a reservation around this time. Please choose another date!\n\n" +
															 "Reservation ID = '" + r.getResNumberID() + "'\n" +
															 "From '" + r.getPickupTime() + "' until '" + r.getReturnTime() + "'");
									alertWarn.showAndWait();
								    return;
									}
								} 
							}

						Alert alertUpdate = new Alert(AlertType.CONFIRMATION);
						alertUpdate.setTitle("Updating a reservation");
						alertUpdate.setHeaderText("Please confirm!");
						alertUpdate.setContentText("Would you really want to UPDATE this reservation?\n\n" +
												"Reserved category: '" + reservedCategory + "'\n" + 
												"License plate: '" + selectedCar.getCarLicensePlate() + "'\n" +
												"For: '" + selectedCust.getModellObject().getFirstName() + " " +
														   selectedCust.getModellObject().getLastName() +"'");
						Optional<ButtonType> result = alertUpdate.showAndWait();
						if(result.get() == ButtonType.OK) {
								Database.updateReservation(updateRes.getModellObject());
								observReservations.remove(selectedRes);
								observReservations.add(updateRes);
								selectedRes = updateRes;
								rentStatusLabel.setText("Status = " + selectedRes.getStatusName());
							}

				   } catch (SQLException e1) {
					   System.out.println("Something is wrong with the database - updating reservation");
					   e1.printStackTrace();
				}
			});

			HBox bottomHBoxRes = new HBox(updateResButton, reserveButton);
			bottomHBoxRes.setPadding(new Insets(0, 5, 0, 0));
			bottomHBoxRes.setSpacing(10);
			bottomHBoxRes.setAlignment(Pos.BOTTOM_RIGHT);
			
			HBox bottomHBoxCust = new HBox(saveCustomerButton, updateCustomerButton);
			bottomHBoxCust.setPadding(new Insets(0, 0, 0, 20));
			bottomHBoxCust.setSpacing(10);
			bottomHBoxCust.setAlignment(Pos.BOTTOM_LEFT);
			
			VBox bottomVBox = new VBox(bottomHBoxCust, bottomHBoxRes);
			reserveBP.setBottom(bottomVBox);

		return reserveBP;
		
	}
	

	
//CARS MENU	
//CARS MENU	
	public VBox showCarsTableView() {
		TableColumn<CarFX, String> categorieCol = new TableColumn<>("Category");
		categorieCol.setPrefWidth(80);
		categorieCol.setMinWidth(30);
		categorieCol.setCellValueFactory(new PropertyValueFactory<>("carCategory"));
		categorieCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<CarFX, String> markeCol = new TableColumn<>("Brand");
		markeCol.setPrefWidth(105);
		markeCol.setMinWidth(30);
		markeCol.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
	
		TableColumn<CarFX, String> modellCol = new TableColumn<>("Modell");
		modellCol.setPrefWidth(105);
		modellCol.setMinWidth(30);
		modellCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
    
		TableColumn<CarFX, String> licPlateCol = new TableColumn<>("License Plate");
		licPlateCol.setPrefWidth(90);
		licPlateCol.setMinWidth(30);
    	licPlateCol.setCellValueFactory(new PropertyValueFactory<>("carLicensePlate"));
    
    	TableColumn<CarFX, String> fuelTypeCol = new TableColumn<>("Fuel Type");
    	fuelTypeCol.setPrefWidth(80);
    	fuelTypeCol.setMinWidth(30);
    	fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("carFuelType"));
    
    	TableColumn<CarFX, Boolean> onRentCol = new TableColumn<>("OnRent");
    	onRentCol.setPrefWidth(60);
    	onRentCol.setMinWidth(30);
    	onRentCol.setCellValueFactory(new PropertyValueFactory<>("carIsOnRent"));
    
    	onRentCol.setCellFactory(col -> new TableCell<CarFX, Boolean>() {
	    @Override
	    	protected void updateItem(Boolean item, boolean empty) {
	    		super.updateItem(item, empty) ;
	    		setText(empty ? null : item ? "OnRent" : "Ready" );
	    	}
    	});
  
    	carsTableView = new TableView<>(observCars);
		carsTableView.setPrefHeight(570);
		carsTableView.getColumns().addAll(categorieCol, markeCol, modellCol, licPlateCol,fuelTypeCol, onRentCol);
		//carsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		carsTableView.getSortOrder().add(categorieCol);
		carsTableView.setPlaceholder(new Label("No cars available!"));
	
	 	sortedListCars.comparatorProperty().bind(carsTableView.comparatorProperty());
	 	carsTableView.setItems(sortedListCars);
	
	 		VBox carsLeftVBox = new VBox();
			carsLeftVBox.setSpacing(5);
//Searching		
			Label carSearchLB = new Label("Search for a car");
			carSearchLB.setId("searchLB");
			
			ArrayList<String> categoriesAll = new ArrayList<>();
			   categoriesAll.add("All cars");
			   for(String s: categoriesList()) {
			    	categoriesAll.add(s);
			    }
			ComboBox<String>  carSearchBox = new ComboBox<>(FXCollections.observableArrayList(categoriesAll));
			carSearchBox.setId("carSearchBox");
			carSearchBox.getSelectionModel().select(0);
			
			TextField searchTF = new TextField();
			searchTF.setId("searchTF");
			searchTF.setPromptText("License plate nr.");

//SEARCHING LISTENERS
					
				
			carSearchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {	
				if(newV.contains("All cars")) {
					filteredListCars.setPredicate(s -> true);
					searchTF.setDisable(false);
				}
				else {
					filteredListCars.setPredicate(s -> s.getModellObject().getCarCategory().contains(newV));
					searchTF.setDisable(true);
				}
			});
		    
			searchTF.textProperty().addListener((obs2, oldV2, newV2) -> {
				if(newV2 == null || newV2.length() == 0) {
					filteredListCars.setPredicate(s -> true);
				}
				else {
					filteredListCars.setPredicate(s -> s.getModellObject().getCarLicensePlate().toLowerCase().contains(newV2));
				}
			});
		
			HBox carSearchHB = new HBox();
			carSearchHB.setAlignment(Pos.CENTER_LEFT);
			carSearchHB.setSpacing(5);	
			carSearchHB.getChildren().addAll(carSearchLB, carSearchBox, searchTF);
				
			carsLeftVBox.getChildren().add(carSearchHB);
			carsLeftVBox.getChildren().add(carsTableView);	
		
		return carsLeftVBox;	
}

	
	
	
	
	public BorderPane openCarsMenu() {
		Label basePriceLB = new Label("Base price = ");
		    BorderPane carsBP = new BorderPane();
		    GridPane carsGP = new GridPane();
		    carsGP.setAlignment(Pos.CENTER_RIGHT);
		    carsGP.setHgap(15);
		    carsGP.setVgap(10);
		    carsGP.setMinSize(0, 0);
		      
		    HBox carsHB = new HBox(); 
		    carsHB.setAlignment(Pos.CENTER);
		    carsHB.setPadding(new Insets(15,0,0,0));
		    carsHB.setSpacing(20);
		    carsHB.getChildren().add(showCarsTableView());
		    carsHB.getChildren().add(carsGP);
		    carsBP.setCenter(carsHB);
		    
//GridPane LEFT SIDE
		    Label vehicleLB = new Label("Vehicle");
		    vehicleLB.setPrefWidth(195);
		    carsGP.add(vehicleLB, 0, 0);
		    
		    TextField brandTF = new TextField();
		    brandTF.setPromptText("Brand");
		    carsGP.add(brandTF, 0, 1);
		    
		    TextField modelTF = new TextField();
		    modelTF.setPromptText("Model");
		    carsGP.add(modelTF, 0, 2);
		    
		    DatePicker yearPicker = new DatePicker();
		    yearPicker.setPromptText("Manuf. date");
		    yearPicker.setDayCellFactory(dp -> new DateCellFactory(minDate, LocalDate.now()));
		    carsGP.add(yearPicker, 0, 3);
		    
		    HBox kmHB = new HBox();
		    kmHB.setAlignment(Pos.CENTER_LEFT);
		    kmHB.setSpacing(5);
		    TextField kmTF = new TextField();
		    kmTF.setPromptText("Actual km");
		    kmTF.setPrefWidth(170);
		    Label kmLbl = new Label("km");
		    kmHB.getChildren().addAll(kmTF, kmLbl);
		    carsGP.add(kmHB, 0, 4);
		    
		   //ONLY numbers for km, max 6 characters
		    kmTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,6}?")) {
	                    kmTF.setText(oldValue);
	                }
	            }
	        });
		    
//COMBOBOX		    
//FUELTYPES
		    ComboBox<String> carFuelBox = new ComboBox<>();
		    try {
		    	ArrayList<String> fuelTypes = Database.readCarFuelTypeTable();
		    	Collections.sort(fuelTypes);
				carFuelBox.setItems(FXCollections.observableArrayList(fuelTypes));
			} catch (SQLException e1) {
				System.out.println("Fuel types database to combobox adding failed...");
				e1.printStackTrace();
			}
		    carFuelBox.setPrefWidth(195);
		    carFuelBox.setPromptText("Fuel");
		    carsGP.add(carFuelBox, 0, 5);
//TRANSMISSION		   
		    ComboBox<String> carTransmBox = new ComboBox<>();
		    try {
		    	ArrayList<String> transmissions = Database.readCarTransmissionTypeTable();
		    	Collections.sort(transmissions);
		    	carTransmBox.setItems(FXCollections.observableArrayList(transmissions));
		    } catch (SQLException e1) {
				System.out.println("Transmission types database to combobox adding failed...");
				e1.printStackTrace();
			}
		    carTransmBox.setPrefWidth(195);
		    carTransmBox.setPromptText("Transmission");
		    carsGP.add(carTransmBox, 0, 6);
	    
//Reservations
		    Label reservationsLB = new Label("Reservations");
		    carsGP.add(reservationsLB, 0, 7);
			
			ListView<String> reservationsLV = new ListView<>();
		    carsGP.add(reservationsLV, 0, 8);
		    reservationsLV.setPrefSize(195, 130);
	
		        
		
//GridPane RIGHT SIDE	
//CATEGORIES
		    ComboBox<String> carCategorieBox = new ComboBox<>(FXCollections.observableArrayList(categoriesList()));
		    carCategorieBox.setPrefWidth(195);
		    carCategorieBox.setPromptText("Category");
		    carsGP.add(carCategorieBox, 1, 1);
		    
		    carCategorieBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					basePriceLB.setText("Base price = " + getCategoryPrice(newValue) + " € / day");
				}
			});
		   		    
		    TextField licPlateTF = new TextField();
		    licPlateTF.setPromptText("License plate nr.");
		    carsGP.add(licPlateTF, 1, 2);
		    
		    TextField vinNumTF = new TextField();
		    vinNumTF.setPromptText("VIN number");
		    carsGP.add(vinNumTF, 1, 3);
		    //All the VIN numbers are max 17 characters
		    vinNumTF.setOnKeyTyped(e -> {
		    	int maxChar = 17;
		    	if(vinNumTF.getText().length() == maxChar) 
		    	e.consume();
		    });

//COLOR TYPES		    
		    ComboBox<String> carColorBox = new ComboBox<>();
		    try {
		    	ArrayList<String> colors = Database.readCarColorsTable();
		    	Collections.sort(colors);
		    	carColorBox.setItems(FXCollections.observableArrayList(colors));
		    } catch (SQLException e1) {
				System.out.println("Color types database to combobox adding failed...");
				e1.printStackTrace();
			}
		    carColorBox.setPrefWidth(195);
		    carColorBox.setPromptText("Color");
		    carsGP.add(carColorBox, 1, 4);
		    
//Engine		    
		    Label detailsLB = new Label("Engine");
		    carsGP.add(detailsLB, 1, 5);
		   
		    HBox engineHB = new HBox();
		    engineHB.setAlignment(Pos.CENTER_LEFT);
		    engineHB.setSpacing(4);
		    TextField engineSizeTF = new TextField();
		    engineSizeTF.setPrefWidth(70);
		    engineSizeTF.setPromptText("Size");
		    Label ccmLB = new Label("CC");
		    TextField enginePowerTF = new TextField();
		    enginePowerTF.setPrefWidth(70);
		    enginePowerTF.setPromptText("Power");
		    Label powerLB = new Label("KW"); 
		    engineHB.getChildren().addAll(engineSizeTF,ccmLB,enginePowerTF,powerLB);
		    carsGP.add(engineHB, 1, 6);
		    
		    //Only numbers for engine, max 4, 5 characters  
		    engineSizeTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,5}?")) {
	                	engineSizeTF.setText(oldValue);
	                }
	            }
	        });   
		    enginePowerTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,4}?")) {
	                	enginePowerTF.setText(oldValue);
	                }
	            }
	        });
		      
	
//Features LISTVIEW CHECKBOX
		    Label featuresLB = new Label("Features");
		    carsGP.add(featuresLB, 1, 7);

		    LinkedHashMap<String, ObservableValue<Boolean>> featuresMap = new LinkedHashMap<>();
		    try {
		    	ArrayList<String> features = Database.readFeaturesTable();
		    	Collections.sort(features);
				 for(String e : features) {
					    featuresMap.put(e, new SimpleBooleanProperty(false));
					    }			 
			} catch (SQLException e2) {
				System.out.println("Something is wrong with creating list view from features database");
				e2.printStackTrace();
			}
		   
		    ListView<String> featuresLV = new ListView<>();
		    featuresLV.setEditable(true);
		    featuresLV.getItems().addAll(featuresMap.keySet());
		    
		    Callback<String, ObservableValue<Boolean>> itemToBoolean = (String item) -> featuresMap.get(item);
		    featuresLV.setCellFactory(CheckBoxListCell.forListView(itemToBoolean));
		    featuresLV.setPrefSize(195, 245);
		    carsGP.add(featuresLV, 1, 8);
		    
		   
//Price		    
		    carsGP.add(basePriceLB, 1, 9);
 
	    
//Bottom BUTTONS, first the design, then the function
		Button makeResButton = new Button("Reserve");
			makeResButton.setId("makeResButton");
			makeResButton.setDisable(true);
			makeResButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
				        new EventHandler<MouseEvent>() {
				          @Override
				          public void handle(MouseEvent e) {
				        	  makeResButton.setEffect(shadow);
				          }
				        });
			makeResButton.addEventHandler(MouseEvent.MOUSE_EXITED,
				        new EventHandler<MouseEvent>() {
				          @Override
				          public void handle(MouseEvent e) {
				        	  makeResButton.setEffect(null);
				          }
				        });
				
		Button deleteCarButton = new Button("Delete");
			deleteCarButton.setId("deleteCarButton");
			deleteCarButton.setDisable(true);
			
			deleteCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deleteCarButton.setEffect(shadow);
			          }
			        });
			deleteCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deleteCarButton.setEffect(null);
			          }
			        });
				
		Button updateCarButton = new Button("Update");
			updateCarButton.setId("updateCarButton");
			updateCarButton.setDisable(true);
			
			updateCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateCarButton.setEffect(shadow);
			          }
			        });
			updateCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateCarButton.setEffect(null);
			          }
			        });
			
		Button addCarButton = new Button("Add");
			addCarButton.setId("addCarButton");
			
			addCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  addCarButton.setEffect(shadow);
			          }
			        });
			addCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  addCarButton.setEffect(null);
			          }
			        });
			
			
//Filling text fields from table view selected items, enable buttons
			carsTableView.getSelectionModel().selectedItemProperty().addListener((o, oldS, newS) -> {
				if (newS != null) {
					selectedCar = carsTableView.getSelectionModel().getSelectedItem();
					deleteCarButton.setDisable(false);
					makeResButton.setDisable(false);
					updateCarButton.setDisable(false);
					
					brandTF.setText(selectedCar.getModellObject().getCarBrand());
					modelTF.setText(selectedCar.getModellObject().getCarModel());
					kmTF.setText(String.valueOf(selectedCar.getModellObject().getCarKM()));
					yearPicker.setValue(selectedCar.getModellObject().getCarManufDate());
					carFuelBox.setValue(selectedCar.getModellObject().getCarFuelType());
					carTransmBox.setValue(selectedCar.getModellObject().getCarTransmission());
					carCategorieBox.setValue(selectedCar.getModellObject().getCarCategory());
					licPlateTF.setText(selectedCar.getModellObject().getCarLicensePlate());
					vinNumTF.setText(selectedCar.getModellObject().getCarVinNumber());
					carColorBox.setValue(selectedCar.getModellObject().getCarColor());
					engineSizeTF.setText(String.valueOf(selectedCar.getModellObject().getCarEngineSize()));
					enginePowerTF.setText(String.valueOf(selectedCar.getModellObject().getCarEnginePower()));
		//Clear features map, then fill it with the cars features			
					try {
					    ArrayList<String> features = Database.readFeaturesTable();
					    Collections.sort(features);
							for(String e : features) {
								  featuresLV.getItems().clear();
								  featuresMap.put(e, new SimpleBooleanProperty(false));
							      featuresLV.getItems().addAll(featuresMap.keySet());
						     }
					for(String key : featuresMap.keySet()) {
						for(String s: selectedCar.getModellObject().getCarFeatures()) {
							if(key.equals(s)) {
								featuresLV.getItems().clear();
								featuresMap.put(s, new SimpleBooleanProperty(true));
								featuresLV.getItems().addAll(featuresMap.keySet());
							}
						} 
					}
			//Active reservations for car
					reservationsLV.getItems().clear();
					  ArrayList<String> ls = new ArrayList<>();
						for(Reservation r : Database.readReservationsTable("active", "")) {
							if(r.getCar().getCarVinNumber().equals(selectedCar.getModellObject().getCarVinNumber())) {
							   ls.add(r.getPickupTime() + " - " + r.getReturnTime());
							}
						}
						Collections.sort(ls);
						reservationsLV.getItems().addAll(ls);
					} catch (SQLException e1) {
						System.out.println("Something is wrong with the reading reservations, features for cars database");
						e1.printStackTrace();
					}
				}
			});
				
	
//RESERVE CAR ACTION			
			makeResButton.setOnAction(e -> {
				mainTabPane.getSelectionModel().select(reserveTab);
				selectedCar = carsTableView.getSelectionModel().getSelectedItem();
				carLicensePlateTF.setText(selectedCar.getModellObject().getCarLicensePlate());
				carComboBox.getSelectionModel().select(selectedCar.getModellObject().getCarCategory());
			});			
			
//DELETE ACTION			
			deleteCarButton.setOnAction(e -> {
			   try {
				    for(Reservation r : Database.readReservationsTable("active", "")) {
					  if(r.getCar().getCarVinNumber().equals(selectedCar.getModellObject().getCarVinNumber())) {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Deleting a car");
						alertWarn.setHeaderText("You can't delete this car!");
						alertWarn.setContentText(selectedCar.getCarLicensePlate() + " still has a reservation (ID = '"
												 + r.getResNumberID() + "').\n" + "You should first cancel the reservation.");
						alertWarn.showAndWait();
						return;
						}
				    }
				} catch (SQLException e2) {
				System.out.println("Something is wrong with the delete car");
				e2.printStackTrace();
				}

				Alert alertDelete = new Alert(AlertType.CONFIRMATION);
				alertDelete.setTitle("Deleting a car");
				alertDelete.setHeaderText("Please confirm!");
				alertDelete.setContentText("Would you really want to DELETE the selected '" 
										   + selectedCar.getModellObject().getCarBrand() + 
									       " " + selectedCar.getModellObject().getCarModel() + 
									       "' with the license plate '" + selectedCar.getModellObject().getCarLicensePlate() + "'?");
				Optional<ButtonType> result = alertDelete.showAndWait();
				if(result.get() == ButtonType.OK) {
					try {
						Database.deleteCar(selectedCar.getModellObject().getCarVinNumber());
						observCars.remove(selectedCar);
						selectedCar = carsTableView.getSelectionModel().getSelectedItem();
					} catch (SQLException e1) {
						System.out.println("Something is wrong with the delete car database connection");
						e1.printStackTrace();
					}
				}
					
			});
				
//UPDATE CAR ACTION	
			updateCarButton.setOnAction(e -> {
			  try {
				String vinNumber = vinNumTF.getText().toUpperCase();
				String licPlate = licPlateTF.getText().toUpperCase();
				String brand = brandTF.getText().substring(0, 1).toUpperCase() + brandTF.getText().substring(1);
				String model = modelTF.getText().substring(0, 1).toUpperCase() + modelTF.getText().substring(1);
				String category = carCategorieBox.getSelectionModel().getSelectedItem().toString();
				String color = carColorBox.getSelectionModel().getSelectedItem().toString();
				String fuel = carFuelBox.getSelectionModel().getSelectedItem().toString();
				String transm = carTransmBox.getSelectionModel().getSelectedItem().toString();
				LocalDate manufDate = yearPicker.getValue();
				int carKM = Integer.parseInt(kmTF.getText());
				int engSize = Integer.parseInt(engineSizeTF.getText());
				int engPower = Integer.parseInt(enginePowerTF.getText());
				
				ArrayList<String> features = new ArrayList<>();
				for(String key : featuresMap.keySet()) {
					
					ObservableValue<Boolean> val = featuresMap.get(key);
					if(val.getValue()) {
						features.add(key);
					}
				}

				CarFX car = new CarFX(new Car(vinNumber, licPlate, brand, model, 
										category, color, fuel, transm, manufDate, 
										carKM, engSize, engPower, selectedCar.isCarIsOnRent(), features));
				
				if(Database.checkExistingCar(vinNumber, "") == "") {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it seems it's a new car!");
					alertWarn.setContentText("Car with the VIN number '" + vinNumber + 
							                 "' doesn't exists! Please add as a new car! " +
							                 "\n\nNote, that it is NOT possible to update the VIN number. " +
							                 "In this case, please delete this car, and add as a new one!");
					alertWarn.showAndWait();
					return;
				}
				if(Database.checkExistingCar("", licPlate) == "") {
					Alert alertWarn2 = new Alert(AlertType.WARNING);
					alertWarn2.setTitle("Updating a car");
					alertWarn2.setHeaderText("Please check again, it seems it's a new car!");
					alertWarn2.setContentText("Car with the license plate '" + licPlate + 
							                 "' doesn't exists! Please add as a new car! " +
							                 "\n\nNote, that it is NOT possible to update the license plate. " +
							                 "In this case, please delete this car, and add as a new one!");
					alertWarn2.showAndWait();
					return;
				} else {
					Alert alertUpdate = new Alert(AlertType.CONFIRMATION);
					alertUpdate.setTitle("Updating a car");
					alertUpdate.setHeaderText("Please confirm!");
					alertUpdate.setContentText("Would you really want to UPDATE this '" + brand + 
										       " " + model + "' with the VIN number '" + vinNumber + "'?");
					Optional<ButtonType> result = alertUpdate.showAndWait();
					if(result.get() == ButtonType.OK) {
							Database.updateCar(car.getModellObject());
							observCars.remove(selectedCar);
							observCars.add(car);
							selectedCar = car;
						}
				}
			  } catch (SQLException e1) {
				  System.out.println("Something is wrong with the database - update car");
				  e1.printStackTrace();
			  }
			});
					
			
//ADD NEW CAR ACTION
			addCarButton.disableProperty().bind(brandTF.textProperty().isEmpty()
											.or(modelTF.textProperty().isEmpty())
											.or(kmTF.textProperty().isEmpty())
											.or(yearPicker.valueProperty().isNull())
											.or(carFuelBox.valueProperty().isNull())
											.or(carTransmBox.valueProperty().isNull())
											.or(carCategorieBox.valueProperty().isNull())
											.or(licPlateTF.textProperty().isEmpty())
											.or(vinNumTF.textProperty().isEmpty())
											.or(carColorBox.valueProperty().isNull())
											.or(engineSizeTF.textProperty().isEmpty())
											.or(enginePowerTF.textProperty().isEmpty()));
			
			addCarButton.setOnAction(e -> {
				try {
					String vinNumber = vinNumTF.getText().toUpperCase();
					String licPlate = licPlateTF.getText().toUpperCase();
					String brand = brandTF.getText().substring(0, 1).toUpperCase() + brandTF.getText().substring(1);
					String model = modelTF.getText().substring(0, 1).toUpperCase() + modelTF.getText().substring(1);
					String category = carCategorieBox.getSelectionModel().getSelectedItem().toString();
					String color = carColorBox.getSelectionModel().getSelectedItem().toString();
					String fuel = carFuelBox.getSelectionModel().getSelectedItem().toString();
					String transm = carTransmBox.getSelectionModel().getSelectedItem().toString();
					LocalDate manufDate = yearPicker.getValue();
					int carKM = Integer.parseInt(kmTF.getText());
					int engSize = Integer.parseInt(engineSizeTF.getText());
					int engPower = Integer.parseInt(enginePowerTF.getText());
					
					ArrayList<String> features = new ArrayList<>();
					for(String key : featuresMap.keySet()) {
						ObservableValue<Boolean> val = featuresMap.get(key);
						if(val.getValue()) {
							features.add(key);
						}
					}
					CarFX newCar = new CarFX(new Car(vinNumber, licPlate, brand, model, 
											category, color, fuel, transm, manufDate, 
											carKM, engSize, engPower, false, features));
					//ALERT					
					if(Database.checkExistingCar(vinNumber, "") != "") {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a new car");
						alertWarn.setHeaderText("Please check again, it's an existing car!");
						alertWarn.setContentText("Car with the VIN number '"+ vinNumber + "' already exists!\n\n" +
												 "This car is a " + Database.checkExistingCar(vinNumber, ""));
						alertWarn.showAndWait();
						return;
					}
					if(Database.checkExistingCar("", licPlate) != "") {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a new car");
						alertWarn.setHeaderText("Please check again, it's an existing car!");
						alertWarn.setContentText("Car with the license plate '"+ licPlate + "' already exists!\n\n" +
												 "This car is a " + Database.checkExistingCar("", licPlate));
						alertWarn.showAndWait();
						return;
					}
					if(vinNumber.length() < 11) {
						Alert alertWarn2= new Alert(AlertType.WARNING);
						alertWarn2.setTitle("Adding a new car");
						alertWarn2.setHeaderText("Please check again the VIN number!");
						alertWarn2.setContentText("Car's VIN number should be at least 11 characters " +
												  "(in cars made after 1981 every VIN number should be 17 characters)!");
						alertWarn2.showAndWait();
						return;
						
					} if(engSize < minEngineSize) {
						Alert alertWarn3= new Alert(AlertType.WARNING);
						alertWarn3.setTitle("Adding a new car");
						alertWarn3.setHeaderText("Please check again the engine size!");
						alertWarn3.setContentText("Is the engine size is really so small? Only " + engSize + " CCM?");
						alertWarn3.showAndWait();
						return;
					} if(engPower < minEnginePower) {
						Alert alertWarn4= new Alert(AlertType.WARNING);
						alertWarn4.setTitle("Adding a new car");
						alertWarn4.setHeaderText("Please check again the engine power!");
						alertWarn4.setContentText("Is the engine so weak? Engine power only " + engPower + " KW?");
						alertWarn4.showAndWait();
						return;
					}
					else {
						Alert alertAdd = new Alert(AlertType.CONFIRMATION);
						alertAdd.setTitle("Adding a new car");
						alertAdd.setHeaderText("Please confirm!");
						alertAdd.setContentText("Would you really want to ADD this '" + brand + 
											" " + model + "' with the license plate '" + licPlate + "'?");
						Optional<ButtonType> result = alertAdd.showAndWait();
						if(result.get() == ButtonType.OK) {
								Database.addNewCar(newCar.getModellObject());
								observCars.add(newCar);
								selectedCar = newCar;
							}
					}	
				} catch (SQLException e1) {
					System.out.println("Something is wrong with the database - adding a new car");
					e1.printStackTrace();
				}		
			});
  
		    HBox bottomHBox = new HBox();
			bottomHBox.setPadding(new Insets(10, 5, 0, 0));
			bottomHBox.setSpacing(10);
			bottomHBox.getChildren().addAll(makeResButton, deleteCarButton, updateCarButton, addCarButton);
			bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		    carsBP.setBottom(bottomHBox);	
			 
		
		return carsBP;
	}
	
	

	
	
//RESERVATIONS MENU	
	public BorderPane openReservationsMenu() {
		BorderPane reservationsBP = new BorderPane();			
//Search		
		ComboBox<String> searchCB = new ComboBox<>();
		searchCB.setItems(FXCollections.observableArrayList("Searching criteria", "Reservation ID", "First name", "Last name"));
		searchCB.getSelectionModel().selectFirst();
		searchCB.setId("carSearchBox");
		
		TextField resSearchTF = new TextField();
		resSearchTF.setDisable(true);
		resSearchTF.setId("searchTF");
		resSearchTF.promptTextProperty().bind(searchCB.valueProperty());
		
		HBox resSearchHB = new HBox(searchCB, resSearchTF);
		resSearchHB.setPadding(new Insets(15, 0, 5, 0));
		resSearchHB.setAlignment(Pos.CENTER);
		resSearchHB.setSpacing(5);	
		reservationsBP.setTop(resSearchHB);
		
//TableView
		TableColumn<ReservationFX, String> resNumCol = new TableColumn<>("Reservation nr.");
		resNumCol.setPrefWidth(125);
		resNumCol.setMinWidth(30);
		resNumCol.setCellValueFactory(new PropertyValueFactory<>("resNumberID"));
	    
		
		TableColumn<ReservationFX, String> custFirstNameCol = new TableColumn<>("First name");
		custFirstNameCol.setPrefWidth(80);
		custFirstNameCol.setMinWidth(30);
		custFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<ReservationFX, String> custLastNameCol = new TableColumn<>("Last name");
		custLastNameCol.setPrefWidth(80);
		custLastNameCol.setMinWidth(30);
		custLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		
		TableColumn<ReservationFX, String> carCatCol = new TableColumn<>("Category");
		carCatCol.setPrefWidth(70);
		carCatCol.setMinWidth(30);
		carCatCol.setCellValueFactory(new PropertyValueFactory<>("reservedCategory"));
		
		TableColumn<ReservationFX, String> carLicensePlateCol = new TableColumn<>("Lic. plate");
		carLicensePlateCol.setPrefWidth(80);
		carLicensePlateCol.setMinWidth(30);
		carLicensePlateCol.setCellValueFactory(new PropertyValueFactory<>("carLicensePlate"));
		
		TableColumn<ReservationFX, String> pickupTimeCol = new TableColumn<>("PICKUP time");
		pickupTimeCol.setPrefWidth(130);
		pickupTimeCol.setMinWidth(30);
		pickupTimeCol.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
		pickupTimeCol.setSortType(TableColumn.SortType.ASCENDING);
		
		TableColumn<ReservationFX, String> pickupLocCol = new TableColumn<>("Pickup location");
		pickupLocCol.setPrefWidth(115);
		pickupLocCol.setMinWidth(30);
		pickupLocCol.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
		
		TableColumn<ReservationFX, String> returnTimeCol = new TableColumn<>("RETURN time");
		returnTimeCol.setPrefWidth(130);
		returnTimeCol.setMinWidth(30);
		returnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
		
		TableColumn<ReservationFX, String> returnLocCol = new TableColumn<>("Return location");
		returnLocCol.setPrefWidth(115);
		returnLocCol.setMinWidth(30);
		returnLocCol.setCellValueFactory(new PropertyValueFactory<>("returnLocation"));
		
		TableColumn<ReservationFX, String> statusCol = new TableColumn<>("Status");
		statusCol.setPrefWidth(70);
		statusCol.setMinWidth(30);
		statusCol.setCellValueFactory(new PropertyValueFactory<>("statusName"));	
		
		
	    reservTableView = new TableView<>(observReservations);
	    reservTableView.getColumns().addAll(resNumCol, custFirstNameCol, custLastNameCol, carCatCol, carLicensePlateCol,
	    									pickupTimeCol, pickupLocCol, returnTimeCol, returnLocCol, statusCol);
	    reservTableView.getSortOrder().add(pickupTimeCol);
	    reservTableView.setPlaceholder(new Label("No reservations available!"));
	    reservTableView.setPrefSize(1000, 570);
		reservationsBP.setCenter(reservTableView);
		
		sortedListReservations.comparatorProperty().bind(reservTableView.comparatorProperty());
		reservTableView.setItems(sortedListReservations);
		
//Searching	
		searchCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.contains("Reservation ID")) {
					resSearchTF.setDisable(false);
					resSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListReservations.setPredicate(s -> true);
						}
						else {
							filteredListReservations.setPredicate(s -> s.getModellObject().getResNumberID().toLowerCase().contains(newV));
						}
					});
				} 
				if(newValue.contains("First name")) {
					resSearchTF.setDisable(false);
					resSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListReservations.setPredicate(s -> true);
						}
						else {
							filteredListReservations.setPredicate(s -> s.getModellObject().getCustomer().getFirstName().toLowerCase().contains(newV));
							
						}
					});
				}
				if(newValue.contains("Last name")){
					resSearchTF.setDisable(false);
					resSearchTF.textProperty().addListener((obs, oldV, newV) -> {
						if(newV == null || newV.length() == 0) {
							filteredListReservations.setPredicate(s -> true);
						}
						else {
							filteredListReservations.setPredicate(s -> s.getModellObject().getCustomer().getLastName().toLowerCase().contains(newV));
						}
					});	
				}
			}
		});
		
		
			
//Bottom Buttons		
//PRINT PDF			
		Button printPdfButton = new Button("Print PDF");
		printPdfButton.setId("printPdfButton");
		printPdfButton.setDisable(true);
		
		printPdfButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  printPdfButton.setEffect(shadow);
		          }
		        });
		printPdfButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  printPdfButton.setEffect(null);
		          }
		        });
//DELETE		
		Button deleteResButton = new Button("Cancel");
		deleteResButton.setId("deleteResButton");
		deleteResButton.setDisable(true);
		
		deleteResButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  deleteResButton.setEffect(shadow);
		          }
		        });
		deleteResButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  deleteResButton.setEffect(null);
		          }
		        });
//SHOW RES		
		Button showResButton = new Button("Details");
		showResButton.setId("showResButton");
		showResButton.setDisable(true);
		
		showResButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  showResButton.setEffect(shadow);
		          }
		        });
		showResButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  showResButton.setEffect(null);
		          }
		        });
		
//RETURN CAR		
		Button returnCarButton = new Button("Return car");
		returnCarButton.setId("returnCarButton");
		returnCarButton.setDisable(true);
		
		returnCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  returnCarButton.setEffect(shadow);
		          }
		        });
		returnCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		        	  returnCarButton.setEffect(null);
		          }
		        });
		
		
		reservTableView.getSelectionModel().selectedItemProperty().addListener((o, oldS, newS) -> {
			if (newS != null) {
				selectedRes = reservTableView.getSelectionModel().getSelectedItem();
				printPdfButton.setDisable(false);
				deleteResButton.setDisable(false);
				showResButton.setDisable(false);
				returnCarButton.setDisable(false);	
			}
		});
				
		
//PRINT PDF ON ACTION
		

		
		
//DELETE RES ON ACTION
		deleteResButton.setOnAction(e -> {
			Alert alertCancel = new Alert(AlertType.CONFIRMATION);
			alertCancel.setTitle("Cancelling a reservation");
			alertCancel.setHeaderText("Please confirm!");
			alertCancel.setContentText("Would you really want to CANCEL the selected '" 
									   + selectedRes.getModellObject().getResNumberID() + 
								       "' reservation?");
			Optional<ButtonType> result = alertCancel.showAndWait();
			if(result.get() == ButtonType.OK) {
				try {
					Database.cancelReservation(selectedRes.getModellObject().getResNumberID());
					for(Reservation r : Database.readReservationsTable("", "cancelled")) {
						if(r.getResNumberID().equals(selectedRes.getResNumberID())) {
							observReservations.remove(selectedRes);
							ReservationFX newRes = new ReservationFX(new Reservation(r.getResNumberID(),
																	r.getCustomer(),
																	r.getCar(),
																	r.getReservedCategory(),
																	r.getInsuranceType(),
																	r.getPickupLocation(),
																	r.getPickupTime(),
																	r.getReturnLocation(),
																	r.getReturnTime(),
																	r.getResNotes(),
																	r.getResExtras(),
																	r.isStatus()));
							observReservations.add(newRes);
							selectedRes = newRes;
						}
					}
				} catch (SQLException e1) {
					System.out.println("Something is wrong with the cancel res database connection");
					e1.printStackTrace();
				}
			}
			
			
		});
		
	
		
//UPDATE RES ON ACTION
		showResButton.setOnAction(e -> {
			mainTabPane.getSelectionModel().select(reserveTab);
			resIdLabel.setText("ResID = " + selectedRes.getModellObject().getResNumberID());
			rentStatusLabel.setText("Status = " + selectedRes.getStatusName());
		});

		
		
//RETURN CAR ON ACTION		
	
		
	    HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 5, 0, 0));
		bottomHBox.setSpacing(10);
		bottomHBox.getChildren().addAll(printPdfButton, deleteResButton, showResButton, returnCarButton);
		bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		reservationsBP.setBottom(bottomHBox);
	    
	    
		return reservationsBP;
		
	}
	
	
	
	
	
//METHODS for CARS menu
//Initialize lists, reading data out from cars database	
	public void fillCarsObservableList(String active, String deactive) {
		 observCars = FXCollections.observableArrayList();
			ArrayList<Car> cars = new ArrayList<>();
			try {
				cars = Database.readCarsTable(active, deactive);
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with cars database");
				e.printStackTrace();
			}
			 for(Car c : cars) {
			    observCars.add(new CarFX(c));
			 }
			  filteredListCars = new FilteredList<>(observCars, p -> true);
			  sortedListCars = new SortedList<>(filteredListCars);
		}
	
	
//Categories	
	public ArrayList<String> categoriesList(){
		ArrayList<String> categoryNames = new ArrayList<>();
		try {
			for(String key: Database.readCarCategoriesTable().keySet()) {
				categoryNames.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of car categories table from database");
			e.printStackTrace();
		}
		Collections.sort(categoryNames);
		return categoryNames;
	}
	
	
	
	public int getCategoryPrice(String category) {
		int price = 0;
		try {
			for(String key: Database.readCarCategoriesTable().keySet()) {
				if(category.equals(key))
				price = Database.readCarCategoriesTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getCategoryPrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}
	

//METHODS for RESERVATIONS menu	
//Insurances	
	public ArrayList<String> insurancesList(){
		ArrayList<String> insuranceTypes = new ArrayList<>();
		try {
			for(String key: Database.readInsurancesTable().keySet()) {
				insuranceTypes.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of insurances table from database");
			e.printStackTrace();
		}
		Collections.sort(insuranceTypes);
		return insuranceTypes;
	}
	
	
	
	public int getInsurancePrice(String insurance) {
		int price = 0;
		try {
			for(String key: Database.readInsurancesTable().keySet()) {
				if(insurance.equals(key))
				price = Database.readInsurancesTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getInsurancePrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}
	
	
//Extras
	public ArrayList<String> extrasList(){
		ArrayList<String> extras = new ArrayList<>();
		try {
			for(String key: Database.readExtrasTable().keySet()) {
				extras.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of extras table from database");
			e.printStackTrace();
		}
		Collections.sort(extras);
		return extras;
	}
	
	
	
	public int getExtraPrice(String extraName) {
		int price = 0;
		try {
			for(String key: Database.readExtrasTable().keySet()) {
				if(extraName.equals(key))
				price = Database.readExtrasTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getExtraPrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}

	
	
	public void fillReservationsObservableList() {
		 observReservations = FXCollections.observableArrayList();
			ArrayList<Reservation> reservations = new ArrayList<>();
			try {
				reservations = Database.readReservationsTable("","");
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with reservations database");
				e.printStackTrace();
			}
			 for(Reservation r : reservations) {
				 observReservations.add(new ReservationFX(r));
			 }
			  filteredListReservations = new FilteredList<>(observReservations, p -> true);
			  sortedListReservations = new SortedList<>(filteredListReservations);
		}
	
	
	
	
	
	public static void main(String[] args) {
		try {
			Database.createUsersTable();
				System.out.println("Created users table, or already exists");
			Database.createCarCategoriesTable();
				System.out.println("Created car categories table, or alresy exists");
			Database.createCarFuelTypeTable();
				System.out.println("Created car fuel types table, or already exists");
			Database.createCarTransmissionTypeTable();
				System.out.println("Created car transmission types table, or already exists");
			Database.createCarColorsTable();
				System.out.println("Created car color types table, or already exists");
			Database.createCarsTable();
				System.out.println("Created cars table, or already exists");
			Database.createFeaturesTable();
				System.out.println("Created features table, or already exists");
		    Database.createCarFeaturesTable();
				System.out.println("Created car features junction table, or already exists");
			Database.createNationalitiesTable();
				System.out.println("Created nationalities table, or already exists");
			Database.createCustomersTable();
				System.out.println("Created customers table, or already exists");
			Database.createInsurancesTable();
				System.out.println("Created insurances table, or already exists");
			Database.createExtrasTable();
				System.out.println("Created extras table, or already exists");
			Database.createReservationsTable();
				System.out.println("Created reservations table, or already exists");
			Database.createReservationExtrasTable();
				System.out.println("Created reservation extras junction table, or already exists");


				
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	
		launch(args);
	}
}
