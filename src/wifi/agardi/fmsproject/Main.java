package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	
	private ObservableList<CarFX> observCar;
	private FilteredList<CarFX> filteredListCars;  //Filtered and sorted list are connected with the observable list, needed to make because the license plate search
	private SortedList<CarFX> sortedListCars;
	
	TableView<CarFX> carsTableView;
	CarFX selectedCar;

	TabPane mainTabPane;
	Tab carsTab;
	Tab reserveTab;
	
	TextField carLicensePlateTF;
	TextField carWishTF;
	ComboBox<String> carComboBox;

	int minEngineSize = 500;
	int minEnginePower = 40;

//LOGIN	
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
	


//After login, MAIN Window	
	public void mainMenu() {
			Stage mainStage = new Stage();
			HBox mainHB = new HBox();
			mainHB.setPadding(new Insets(15, 15 , 15, 15));
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
			
			fillCarsObservableList();
			carsTab = new Tab("Cars");
			carsTab.setContent(openCarsMenu());
			carsTab.setClosable(false);
			carsTab.setId("carsTab");
		
			Tab reservationsTab = new Tab("Reservaitons");
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
	

	
//Initialize lists, reading data out from cars database	
	public void fillCarsObservableList() {
		  observCar = FXCollections.observableArrayList();
		  ArrayList<Car> cars = new ArrayList<>();
			try {
				cars = Database.readCarsTable();
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with cars database");
				e.printStackTrace();
			}
		    for(Car c : cars) {
		    	observCar.add(new CarFX(c));
		    }
		    filteredListCars = new FilteredList<>(observCar, p -> true);
		    sortedListCars = new SortedList<>(filteredListCars);
		}

	
	
	
	public void fillCarsObservableListDeactivedCars() {
		  observCar = FXCollections.observableArrayList();
		  ArrayList<Car> cars = new ArrayList<>();
			try {
				cars = Database.readDeactiveCarsTable();
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with deactive cars database");
				e.printStackTrace();
			}
		    for(Car c : cars) {
		    	observCar.add(new CarFX(c));
		    }
		    filteredListCars = new FilteredList<>(observCar, p -> true);
		    sortedListCars = new SortedList<>(filteredListCars);
		}
	
	
	
//RESERVE MENU	
	public BorderPane openReserveMenu() {
			BorderPane reserveBP = new BorderPane();
			
			GridPane reserveGP = new GridPane();
			reserveGP.setAlignment(Pos.CENTER);
			reserveGP.setPadding(new Insets(0, 25, 0, 25));
			reserveGP.setHgap(15);
			reserveGP.setVgap(10);
			reserveGP.setMinSize(0, 0);
			reserveBP.setCenter(reserveGP);
			
			ColumnConstraints columnSpace = new ColumnConstraints();
			columnSpace.setHgrow(Priority.ALWAYS);				
			ColumnConstraints col1 = new ColumnConstraints();
			reserveGP.getColumnConstraints().addAll(col1, col1, columnSpace);
		
//GRIDPANE			
//Searching for a driver	
//TODO
			Button searchDriverButton = new Button("Choose a driver");
			searchDriverButton.setId("searchDriverButton");
			reserveGP.add(searchDriverButton, 0, 0);
			searchDriverButton.setOnAction(e ->{
				CustomerListDialog custList = new CustomerListDialog();
				custList.showAndWait();
			});
			
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
			reserveGP.add(dateBornPicker, 0, 4);
			
//NATIONALITY TODO			
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
			
			Label contactLabel = new Label("Contact");		
			reserveGP.add(contactLabel, 0, 8);
			
			TextField telefonTF = new TextField();
			telefonTF.setPromptText("Telefon nr.");
			reserveGP.add(telefonTF, 0, 9);
			
			TextField emailTF = new TextField();
			emailTF.setPromptText("E-mail");
			reserveGP.add(emailTF, 0, 10);

			
//Grid 1. column	
			Label custIdLabel = new Label("ID = ");
			reserveGP.add(custIdLabel, 1, 0);
			
			Label addressLabel = new Label("Address");
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
					
			carComboBox = new ComboBox<>(FXCollections.observableArrayList(categoriesList()));
			carComboBox.setId("carSearchBox");
			carComboBox.setPromptText("Reserve a category");
			reserveGP.add(carComboBox, 4, 0);
	
			Label carLabel = new Label("Car details");
			reserveGP.add(carLabel, 3, 1);
			
			carLicensePlateTF = new TextField();
			carLicensePlateTF.setPromptText("Reserved car's license pl.");
			reserveGP.add(carLicensePlateTF, 3, 2);
			
			carWishTF = new TextField();
			carWishTF.setPromptText("Type wish");
			reserveGP.add(carWishTF, 3, 3);
			
//PICKUP DETAILS
			Label pickupLabel = new Label("Pick up");
			reserveGP.add(pickupLabel, 3, 4);
			
			TextField pickupLocTF = new TextField();
			pickupLocTF.setPromptText("Pickup location");
			reserveGP.add(pickupLocTF, 3, 5);
			
			DatePicker datePickupPicker = new DatePicker();
			datePickupPicker.setPromptText("Pickup date");
			reserveGP.add(datePickupPicker, 3, 6);
			

			HBox pickupHBox = new HBox();
			pickupHBox.setSpacing(5);
			pickupHBox.setAlignment(Pos.CENTER_LEFT);
			TextField pickupHourTF = new TextField();
			Label pickupHourLB = new Label("hours");
			pickupHourLB.setAlignment(Pos.CENTER);
			pickupHourTF.setMaxWidth(40);
			TextField pickupMinTF = new TextField();
			pickupMinTF.setMaxWidth(40);
			Label pickupMinuteLB = new Label("minutes");
			pickupHBox.getChildren().addAll(pickupHourTF,pickupHourLB, pickupMinTF, pickupMinuteLB);
			reserveGP.add(pickupHBox, 3, 7);

			
//Grid 4. column			
			Label dateLabel = new Label("Insurance");
			reserveGP.add(dateLabel, 4, 1);
//INSURANCE		
			ToggleGroup tg = new ToggleGroup();
			RadioButton cascoRB = new RadioButton("Casco");
			cascoRB.setToggleGroup(tg);
			RadioButton fullCascoRB = new RadioButton("Full Casco");
			fullCascoRB.setToggleGroup(tg);
			reserveGP.add(cascoRB, 4, 2);
			reserveGP.add(fullCascoRB, 4, 3);
//RETURN DETAILS			
			Label returnLabel = new Label("Return");
			reserveGP.add(returnLabel, 4, 4);
			
			TextField returnLocTF = new TextField();
			returnLocTF.setPromptText("Return location");
			reserveGP.add(returnLocTF, 4, 5);
			
			DatePicker dateReturnPicker = new DatePicker();
			dateReturnPicker.setPromptText("Return date");
			reserveGP.add(dateReturnPicker, 4, 6);
			
			
			HBox returnHBox = new HBox();
			returnHBox.setSpacing(5);
			returnHBox.setAlignment(Pos.CENTER_LEFT);
			TextField returnHourTF = new TextField();
			returnHourTF.setMaxWidth(40);
			Label returnHourLB = new Label("hours");
			TextField returnMinTF = new TextField();
			returnMinTF.setMaxWidth(40);
			Label returnMinuteLB = new Label("minutes");
			returnHBox.getChildren().addAll(returnHourTF, returnHourLB, returnMinTF, returnMinuteLB);
			reserveGP.add(returnHBox, 4, 7);
			
//EXTRAS		
			Label extrasLB = new Label("Extras");
			reserveGP.add(extrasLB, 3, 8);
			
			CheckBox cb1 = new CheckBox("GPS");
			CheckBox cb2 = new CheckBox("Additional driver");
			CheckBox cb3 = new CheckBox("Child seat");
			CheckBox cb4 = new CheckBox("Border crossing");
			reserveGP.add(cb1, 3, 9);
			reserveGP.add(cb2, 3, 10);
			reserveGP.add(cb3, 4, 9);
			reserveGP.add(cb4, 4, 10);	
			
//Notes, Comments			
			Label notesLabel2 = new Label("Notes");
			reserveGP.add(notesLabel2, 3, 11);
			
			TextArea notesTA2 = new TextArea();
			notesTA2.setPromptText("comments");
			notesTA2.setPrefSize(195, 70);
			reserveGP.add(notesTA2, 3, 12);
	
//PRICE TODO
			VBox priceVBox = new VBox();
			Label priceDaysLabel = new Label("Total days   = ");
			Label priceLabel1 =    new Label("Price * days = ");
			Label priceLabel2 =    new Label("Extras	   = ");
			Label priceLabel3 =    new Label("Total price  = ");
			priceVBox.getChildren().addAll(priceDaysLabel, priceLabel1, priceLabel2, priceLabel3);
			reserveGP.add(priceVBox, 4, 12);
			
			
//Reserve BorderPane BOTTOM BUTTONS
//DESIGN			
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
		
		ObservableValue<Boolean> resObs = carComboBox.valueProperty().isNull().or((ObservableBooleanValue) custObs);
			
					
		Button saveCustomerButton = new Button("Add");
			saveCustomerButton.setId("saveCustomerButton");	
			saveCustomerButton.disableProperty().bind(custObs);
			
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
			
			
		Button updateCustomerButton = new Button("Update");
			updateCustomerButton.setId("updateCustomerButton");	
			updateCustomerButton.disableProperty().bind(custObs);
			
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
			
			
		Button updateResButton = new Button("?");
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

			
		Button reserveButton = new Button("Save");
			reserveButton.setId("saveResButton");
			reserveButton.disableProperty().bind(resObs);
			
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
			
//SAVE CUSTOMER ON ACTION			
			
			saveCustomerButton.setOnAction(e -> {
			   try {
				String customerID = "FMSCID" + String.valueOf(System.currentTimeMillis() / 1000);
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
				
				
		        Alert confirmAdd = new Alert(AlertType.CONFIRMATION);
				
				
			  } catch (SQLException e1) {
				  System.out.println("Something is wrong with the database - add custormer");
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
    
    	TableColumn<CarFX, String> onRentCol = new TableColumn<>("OnRent");
    	onRentCol.setPrefWidth(60);
    	onRentCol.setMinWidth(30);
    	onRentCol.setCellValueFactory(new PropertyValueFactory<>("carIsOnRent"));
    
  
    	carsTableView = new TableView<>(observCar);
		carsTableView.setPrefHeight(570);
		carsTableView.getColumns().addAll(categorieCol, markeCol, modellCol, licPlateCol,fuelTypeCol, onRentCol);
		carsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
				}
				else {
					filteredListCars.setPredicate(s -> s.getModellObject().getCarCategory().contains(newV));
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
		    modelTF.setPromptText("Modell");
		    carsGP.add(modelTF, 0, 2);
		    
		    DatePicker yearPicker = new DatePicker();
		    yearPicker.setPromptText("Manuf. date");
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
		    
		   //ONLY numbers for km 
		    kmTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
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
					int price = 0;
					try {
						for(String key: Database.readCarCategoriesTable().keySet()) {
							if(newValue.equals(key))
							price = Database.readCarCategoriesTable().get(key);
						}
						basePriceLB.setText("Base price = " + price + " EUR / Day");
					} catch (SQLException e) {
						System.out.println("Something is wrong with the reading of car categories table price from database");
						e.printStackTrace();
					}
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
		    
		    //Only numbers for engine    
		    engineSizeTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
	                	engineSizeTF.setText(oldValue);
	                }
	            }
	        });   
		    enginePowerTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
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
		    featuresLV.setPrefSize(195, 130);
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
					
					for(String key : featuresMap.keySet()) {
						ObservableValue<Boolean> featuresCheckedValue = featuresMap.get(key);
						for(String s: selectedCar.getModellObject().getCarFeatures()) {
						if(key.equals(s)) {
							System.out.println("Extras= " + s);
						}
						} 
					}
				}
			});
				
	
//RESERVE CAR ACTION			
			makeResButton.setOnAction(e -> {
				mainTabPane.getSelectionModel().select(reserveTab);
				selectedCar = carsTableView.getSelectionModel().getSelectedItem();
				carLicensePlateTF.setText(selectedCar.getModellObject().getCarLicensePlate());
				carWishTF.setText(selectedCar.getModellObject().getCarBrand() + " " + 
								  selectedCar.getModellObject().getCarModel());
				carComboBox.getSelectionModel().select(selectedCar.getModellObject().getCarCategory());
			});			
			
			
//DELETE ACTION			
			deleteCarButton.setOnAction(e -> {
				Alert alertDelete = new Alert(AlertType.CONFIRMATION);
				alertDelete.setTitle("Deleting a car");
				alertDelete.setHeaderText("Please confirm!");
				alertDelete.setContentText("Would you really want to DELETE the selected '" + selectedCar.getModellObject().getCarBrand() + 
									" " + selectedCar.getModellObject().getCarModel() + "' with the license plate '" + selectedCar.getModellObject().getCarLicensePlate() + "'?");
				Optional<ButtonType> result = alertDelete.showAndWait();
				if(result.get() == ButtonType.OK) {
					try {
						Database.deleteCar(selectedCar.getModellObject().getCarVinNumber());
						observCar.remove(selectedCar);
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
										carKM, engSize, engPower, false, features));
				
				if(!Database.checkExistingCarVIN(vinNumber)) {
					Alert alertWarn= new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it seems it's a new car!");
					alertWarn.setContentText("Car with the VIN number '" + vinNumber + 
							"' doesn't exists! Please add as a new car! \n\nNote, that it is NOT possible to update the VIN number. "
							+ "In this case, please delete this car, and add as a new one!");
					alertWarn.showAndWait();
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
							observCar.remove(selectedCar);
							observCar.add(car);
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
					if(Database.checkExistingCarVIN(vinNumber)) {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a new car");
						alertWarn.setHeaderText("Please check again, it's an existing car!");
						alertWarn.setContentText("Car with the VIN number '"+ vinNumber + "' already exists!");
						alertWarn.showAndWait();
						return;
					}
					if(Database.checkExistingCarLicP(licPlate)) {
						Alert alertWarn= new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a new car");
						alertWarn.setHeaderText("Please check again, it's an existing car!");
						alertWarn.setContentText("Car with the license plate '"+ licPlate + "' already exists!");
						alertWarn.showAndWait();
						return;
					}
					if(vinNumber.length() < 11) {
						Alert alertWarn2= new Alert(AlertType.WARNING);
						alertWarn2.setTitle("Adding a new car");
						alertWarn2.setHeaderText("Please check again the VIN number!");
						alertWarn2.setContentText("Car's VIN number should be at least 11 characters "
												+ "(in cars made after 1981 every VIN number should be 17 characters)!");
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
								observCar.add(newCar);
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
		Label resSearchLB = new Label("Search for a reservation"); 
		resSearchLB.setId("searchLB");
		
		TextField resNumTF = new TextField();
		resNumTF.setId("searchTF");
		resNumTF.setPromptText("Reservation nr.");
		
		HBox resSearchHB = new HBox(resSearchLB, resNumTF);
		resSearchHB.setPadding(new Insets(15, 0, 5, 0));
		resSearchHB.setAlignment(Pos.CENTER);
		resSearchHB.setSpacing(5);	
		reservationsBP.setTop(resSearchHB);
		
//TableView
		TableColumn<String, String> resNumCol = new TableColumn<>("Reservation nr.");
		resNumCol.setPrefWidth(120);
		resNumCol.setMinWidth(30);
		resNumCol.setCellValueFactory(new PropertyValueFactory<>("?"));
	    
		TableColumn<String, String> custNameCol = new TableColumn<>("Customer name");
		custNameCol.setPrefWidth(140);
		custNameCol.setMinWidth(30);
		custNameCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> carCatCol = new TableColumn<>("Category");
		carCatCol.setPrefWidth(80);
		carCatCol.setMinWidth(30);
		carCatCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> carLicensePlateCol = new TableColumn<>("Lic. plate nr.");
		carLicensePlateCol.setPrefWidth(100);
		carLicensePlateCol.setMinWidth(30);
		carLicensePlateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> pickupDateCol = new TableColumn<>("Pickup date");
		pickupDateCol.setPrefWidth(100);
		pickupDateCol.setMinWidth(30);
		pickupDateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> pickupLocCol = new TableColumn<>("Pickup location");
		pickupLocCol.setPrefWidth(140);
		pickupLocCol.setMinWidth(30);
		pickupLocCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		
		TableColumn<String, String> returnDateCol = new TableColumn<>("Return date");
		returnDateCol.setPrefWidth(100);
		returnDateCol.setMinWidth(30);
		returnDateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> returnLocCol = new TableColumn<>("Return location");
		returnLocCol.setPrefWidth(140);
		returnLocCol.setMinWidth(30);
		returnLocCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
		TableColumn<String, String> insuranceCol = new TableColumn<>("Insurance");
		insuranceCol.setPrefWidth(70);
		insuranceCol.setMinWidth(30);
		insuranceCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
	    
	    TableView<String> reservTableView = new TableView<>();
	    reservTableView.getColumns().addAll(resNumCol, custNameCol, carCatCol, carLicensePlateCol, pickupDateCol,	
	    									pickupLocCol, returnDateCol, returnLocCol, insuranceCol);
	    
	    reservTableView.setPrefSize(1000, 570);
		reservationsBP.setCenter(reservTableView);
		
			
//Bottom Buttons		
//PRINT PDF			
		Button printPdfButton = new Button("Print PDF");
		printPdfButton.setId("printPdfButton");
		
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
		Button deleteResButton = new Button("Delete");
		deleteResButton.setId("deleteResButton");
		
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
//UPDATE		
		Button updateResButton = new Button("Update");
		updateResButton.setId("updateResButton");
		
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
		
//RETURN CAR		
		Button returnCarButton = new Button("Return car");
		returnCarButton.setId("returnCarButton");
		
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
		
	    
	    HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 5, 0, 0));
		bottomHBox.setSpacing(10);
		bottomHBox.getChildren().addAll(printPdfButton, deleteResButton, updateResButton, returnCarButton);
		bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		reservationsBP.setBottom(bottomHBox);
	    
	    
		return reservationsBP;
		
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
			
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	
		launch(args);
	}
}
