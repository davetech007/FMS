package wifi.agardi.fmsproject;
/*MAY 2019 - WIFI Project Fleet Management System (Car reservations management) ADV
 *open version, v. 1.0
 *created David Viktor Agardi*/

import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import static java.time.temporal.TemporalAdjusters.*;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private DropShadow shadow = new DropShadow();  //Effect for the buttons
	private int minEngineSize = 200;   //The minimum size/power that is allowed for the engine, just to not to have not realistic data
	private int minEnginePower = 20;
	private Stage mainStage;
	
	private TabPane mainTabPane;
	private Tab carsTab;
	private Tab reserveTab;
	private Tab reservationsTab;
	private Tab dashboardTab;
	
	private TableView<CarFX> carsTableView;
	private CarFX selectedCar;
	private ObservableList<CarFX> observCars;
	private ObservableList<CarFX> observDeactiveCars;
	private FilteredList<CarFX> filteredListCars;  //Filtered and sorted list are connected with the observable list, needed to make because the 'live' license plate search
	private SortedList<CarFX> sortedListCars;
	
	private Label carLicensePlateLB;
	private ComboBox<String> carComboBox;

	private Label custIdLabel;
	private Label resIdLabel;
	private Label rentStatusLabel;
	private Label customerStatusLabel;
	private Label carStatusLabel;
	private CustomerFX selectedCust;
	
	private TableView<ReservationFX> reservTableView;
	private ReservationFX selectedRes;
	private ObservableList<ReservationFX> observReservations;
	private FilteredList<ReservationFX> filteredListReservations;
	private SortedList<ReservationFX> sortedListReservations;
	
	private BorderPane reservationsBP;

	private Button printPdfButton;
	private Button deleteResButton;
	private Button showResButton;
	
	private ArrayList<String> featuresOriginal;
	private ArrayList<String> extrasOriginal;
	
//LOGIN WINDOW	
/*Here is going to be checked the user name/password. It is possible the sign up freely, and use all the features.
*Now everything is open, but it is ready to lock the functions/sign up easily.
*/
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
			userNameTField.setPrefSize(240, 30);
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
			loginGP.add(actionHBox, 0, 7);
			
//LogIn on action, when successfully it opens the main menu		
			logInButton.setOnAction(new EventHandler<ActionEvent>() {	
				@Override
				public void handle(ActionEvent event) {
					try {
						if(Database.logIn(userNameTField.getText(), passwordField.getText())) {
						actionTarget.setText("Loading...");
						primaryStage.close();
						mainMenu();
						}
						else {
						actionTarget.setId("actionTarget");
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
						if((userNameTField.getText().length() > 2) && (passwordField.getText().length() > 2)) {
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
								actionTarget.setId("actionTarget");
								actionTarget.setText("Signing up cancelled!");
							}
						} else {
								actionTarget.setId("actionTarget");
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
	private void mainMenu() {
		    mainStage = new Stage();	    
			HBox mainHB = new HBox();
			mainHB.setPadding(new Insets(15, 10 , 12, 10));
			mainHB.setAlignment(Pos.CENTER);
//Main Title		
			Label mainTitle = new Label("Fleet Management System");
			mainTitle.setId("mainTitle");
			mainTitle.setPadding(new Insets(0, 0, 5, 0));
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
//initialize database readings
			fillCarsObservableList("active", "");
			fillCarsObservableList("", "deactive");
			carsTab = new Tab("Cars");
			carsTab.setContent(openCarsMenu());
			carsTab.setClosable(false);
			carsTab.setId("carsTab");
		
			fillReservationsObservableList();
			reservationsTab = new Tab("Reservaitons");
			reservationsTab.setContent(openReservationsMenu());
			reservationsTab.setClosable(false);
			reservationsTab.setId("reservationsTab");
		
			dashboardTab = new Tab("Dashboard");
			dashboardTab.setContent(dashboardMenu(LocalDate.now()));
			dashboardTab.setClosable(false);
			dashboardTab.setId("dashboardTab");
	
			mainTabPane = new TabPane();
			mainTabPane.getTabs().addAll(reserveTab, carsTab, reservationsTab, dashboardTab);
			mainTabPane.prefHeightProperty().bind(mainStage.heightProperty());
			mainTabPane.prefWidthProperty().bind(mainStage.widthProperty());
			
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
					dashboardTab.setContent(dashboardMenu(LocalDate.now()));
				}
			});
	
//Main VBox Top second Child			
			mainTopVBox.getChildren().add(mainTabPane);	
			Scene sceneMain = new Scene(mainHB, 1070, 720);
			mainHB.getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
			mainStage.setScene(sceneMain);
			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Alert alertClose = new Alert(AlertType.CONFIRMATION);
					alertClose.setTitle("Quit");
					alertClose.setHeaderText("Please confirm!");
					alertClose.setContentText("There maybe unsaved work. Are you sure you want to quit?");
					Optional<ButtonType> result = alertClose.showAndWait();
					if (result.get() == ButtonType.OK) {
						Platform.exit();
					} else {
						event.consume();
					}
				}
			});
			mainStage.show();
	  }
	
	
	
	
//RESERVE MENU	
	private BorderPane openReserveMenu() {
		//Creating hours, and minutes for the pickup/return time
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
			reserveGP.setAlignment(Pos.CENTER);
			reserveGP.setPadding(new Insets(20, 35, 0, 30));
			reserveGP.setHgap(15);
			reserveGP.setVgap(10);
			reserveGP.setMinSize(0, 0);
			reserveBP.setCenter(reserveGP);
			//set the 3rd column's size to be automatic stretched
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
			dateBornPicker.setDayCellFactory(dp -> new DateCellFactory(LocalDate.MIN, LocalDate.now()));
			reserveGP.add(dateBornPicker, 0, 4);
			
//NATIONALITY		
			ComboBox<String> nationalityComboBox = new ComboBox<>(FXCollections.observableArrayList(nationalitiesList()));
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
			custIdLabel = new Label();
			custIdLabel.setId("IdLabel");
			HBox custHB = new HBox(custIdLabel);
			custHB.setAlignment(Pos.CENTER);
			reserveGP.add(custHB, 1, 0);
			
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
/*Searching for a car	
 * It opens CARS menu to choose a car, then fills the license plate / category. 
 * Note, that after you can choose any type of category, because when you have only a different category,
 * and the client needs another type, it is possible the make this upgrade.
 */
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
			//Max 300 characters, database set to 300 too
			notesTA2.setOnKeyTyped(e -> {
			    	int maxChar = 299;
			    	if(notesTA2.getText().length() == maxChar) 
			    	e.consume();
			    });
			
//Calculate price button
			rentStatusLabel = new Label("Res.status : ");
			customerStatusLabel = new Label("Cust.status: ");
			carStatusLabel = new Label("Car status : ");
			
			Button calcPriceButton = new Button("Calculate price");
			calcPriceButton.setId("calcPriceButton");

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
			
			VBox calcVB = new VBox(rentStatusLabel, customerStatusLabel, carStatusLabel, calcPriceButton);
			calcVB.setAlignment(Pos.BOTTOM_CENTER);
			reserveGP.add(calcVB, 3, 10);		
			
//Grid 4. column
			carLicensePlateLB = new Label();
			carLicensePlateLB.setId("IdLabel");
			HBox lbHB = new HBox(carLicensePlateLB);
			lbHB.setAlignment(Pos.CENTER);
			reserveGP.add(lbHB, 4, 0);

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
							
			
//EXTRAS LISTVIEW		
			LinkedHashMap<String, ObservableValue<Boolean>> extrasMap = new LinkedHashMap<>();
				for (String e : extrasList()) {
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
			resIdLabel =           new Label();
			resIdLabel.setId("IdLabel");
			HBox residHB = new HBox(resIdLabel);
			residHB.setAlignment(Pos.CENTER);
			Label priceDaysLabel = new Label("Rent  : ");
			Label priceLabelIns =  new Label("Insurance: "); 
			Label priceLabel2 =    new Label("Extras: ");
			Label priceLabel3 =    new Label("Total : ");
			priceVBox.getChildren().addAll(residHB, priceDaysLabel, priceLabelIns, priceLabel2, priceLabel3);
			reserveGP.add(priceVBox, 4, 10);		
			
//Reserve BorderPane BOTTOM BUTTONS
//DESIGN, Disable property
			//for save customers
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
	
/*The return date can't be set, only after the pickup date has been chosen.
 * When the pickup date	set after the return date, it automatically changes the return date 
 * to the pickup date.	
 */
			datePickupPicker.setDayCellFactory(dp -> new DateCellFactory(LocalDate.now(), LocalDate.MAX));
			dateReturnPicker.disableProperty().bind(datePickupPicker.valueProperty().isNull());
			
			datePickupPicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
				@Override
				public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
									LocalDate newValue) {
					dateReturnPicker.setDayCellFactory(dp -> new DateCellFactory(newValue, LocalDate.MAX));
					if (dateReturnPicker.getValue() != null && newValue.isAfter(dateReturnPicker.getValue())) {
						dateReturnPicker.setValue(newValue);
					}
				}
			});
		
//SearchDriver ACTION for active customers, changes accordingly the customer label too, when selected		
			searchDriverButton.setOnAction(e ->{
				CustomerListDialog custList = new CustomerListDialog("active", "", observReservations);
				Optional<CustomerFX> result = custList.showAndWait();
				if(result.isPresent()) {
					selectedCust = result.get();
					custIdLabel.setText(selectedCust.getModellObject().getCustomerID());
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
				}
				if(selectedCust != null && 
							custIdLabel.getText().equals(selectedCust.getModellObject().getCustomerID())) {
					try {
						for(Customer c : Database.readCustomersTable("active", "")) {
							if(c.getCustomerID().equals(custIdLabel.getText())) {
								customerStatusLabel.setText("Cust.status: active");
								return;
							}
						}
						for(Customer c : Database.readCustomersTable("", "deactive")) {
							if(c.getCustomerID().equals(custIdLabel.getText())) {
								customerStatusLabel.setText("Cust.status: DEACTIVE");
							}
						}
					} catch (SQLException e1) {
						System.out.println("Something is wrong with the cust.status label database connection");
						e1.printStackTrace();
					}
				}
			});	
//When a customer has been selected, it is possible to update too			
			custIdLabel.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(newValue.length() > 4) {
						updateCustomerButton.disableProperty().bind(custObs);
					}
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
			
				for (Customer c : Database.readCustomersTable("", "")) {
					if (c.getPassportNum().equals(passportNum)) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a customer");
						alertWarn.setHeaderText("Please check again, PASSPORT number already exists!");
						alertWarn.setContentText("You wanted to add a new customer '" + firstName + " " + lastName
												+ "', but his/her passport number '" + passportNum
												+ "' already exists under this name '" + c.getFirstName() + " " + c.getLastName()
												+ "'");
						alertWarn.showAndWait();
						return;
					}
					if (c.getDriversLicenseNum().equals(driversLicenseNum)) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Adding a customer");
						alertWarn.setHeaderText("Please check again, DRIVER'S LICENSE number already exists!");
						alertWarn.setContentText("You wanted to add a new customer '" + firstName + " " + lastName
												+ "', but his/her driver's license number '" + driversLicenseNum
												+ "' already exists under this name '" + c.getFirstName() + " " + c.getLastName()
												+ "'");
						alertWarn.showAndWait();
						return;
					}
				}
				if (!(eMail.length() > 3 && eMail.contains("@") && eMail.contains("."))) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Adding a customer");
					alertWarn.setHeaderText("Please check again, e-mail address looks invalid!");
					alertWarn.setContentText("Please give a real e-mail address (contains '@' and '.')!");
					alertWarn.showAndWait();
					return;
				}
				
				Alert confirmAdd = new Alert(AlertType.CONFIRMATION);
				confirmAdd.setTitle("Adding a new customer");
				confirmAdd.setHeaderText("Please confirm!");
				confirmAdd.setContentText("Would you really want to ADD a new customer '" + firstName + " " + lastName + "'?");
				Optional<ButtonType> result = confirmAdd.showAndWait();
				if (result.get() == ButtonType.OK) {
					Database.addNewCustomer(newCustomer.getModellObject());
					custIdLabel.setText(newCustomer.getModellObject().getCustomerID());
					selectedCust = newCustomer;
				}
			} catch (SQLException e1) {
				System.out.println("Something is wrong with the database - add custormer");
				e1.printStackTrace();
			}
		});
			
/*UPDATE CUSTOMER ON ACTION	
 * After updating the customer, the whole reservations menu will be also reloaded, to be up to date.
 * This can be a little slower.		
 */
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
				
				
				for (Customer r : Database.readCustomersTable("", "deactive")) {
					if (selectedCust.getCustomerID().equals(r.getCustomerID())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Updating customer");
						alertWarn.setHeaderText("Please check again!");
						alertWarn.setContentText("You can't update, because the customer has been DEACTIVATED!");
						alertWarn.showAndWait();
						return;
					}
				}
				Customer dbCustomer = null;
				for (Customer c : Database.readCustomersTable("", "")) {
					if (c.getCustomerID().equals(customerID)) {
						dbCustomer = c;
					}
					if (!selectedCust.getModellObject().getCustomerID().equals(c.getCustomerID())
							&& c.getPassportNum().equals(passportNum)) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Updating a customer");
						alertWarn.setHeaderText("Please check again, PASSPORT number already exists!");
						alertWarn.setContentText("You wanted to update a customer '" + firstName + " " + lastName
												+ "', but his/her passport number '" + passportNum
												+ "' already exists under this name '" + c.getFirstName() + " " + c.getLastName()
												+ "'");
						alertWarn.showAndWait();
						return;
					}
					if (!selectedCust.getModellObject().getCustomerID().equals(c.getCustomerID())
							&& c.getDriversLicenseNum().equals(driversLicenseNum)) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Updating a customer");
						alertWarn.setHeaderText("Please check again, DRIVER'S LICENSE number already exists!");
						alertWarn.setContentText("You wanted to update a customer '" + firstName + " " + lastName
												+ "', but his/her driver's license number '" + driversLicenseNum
												+ "' already exists under this name '" + c.getFirstName() + " " + c.getLastName()
												+ "'");
						alertWarn.showAndWait();
						return;
					}
					if (!(eMail.length() > 3 && eMail.contains("@") && eMail.contains("."))) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Updating a customer");
						alertWarn.setHeaderText("Please check again, e-mail address looks invalid!");
						alertWarn.setContentText("Please give a real e-mail address (contains '@' and '.')!");
						alertWarn.showAndWait();
						return;
					}
				}
				
				if (dbCustomer != null && dbCustomer.getCustomerID().equals(customerID)) {
						Alert confirmUpdate = new Alert(AlertType.CONFIRMATION);
						confirmUpdate.setTitle("Updating customer");
						confirmUpdate.setHeaderText("Please confirm!");
						confirmUpdate.setContentText("Would you really want to UPDATE the selected customer?\n\n"
													+ "Customer with this ID '" + customerID + "' exists under this name '"
													+ dbCustomer.getFirstName() + " " + dbCustomer.getLastName() + "'.\n\n" + "Update to '" + firstName + " "
													+ lastName + "'?");
						Optional<ButtonType> result = confirmUpdate.showAndWait();
						if (result.get() == ButtonType.OK) {
							Database.updateCustomer(updateCustomer.getModellObject());
							selectedCust = updateCustomer;
							reservationsBP.setCenter(showReservationsTableView());
						}
				}
			} catch (SQLException e1) {
				System.out.println("Something is wrong with the database - update custormer");
				e1.printStackTrace();
			}
		});

//RESERVE SIDE 			
		//Enable the calculate price button
			ObservableValue<Boolean> calcObs = carComboBox.valueProperty().isNull().
					    or(insuranceComboBox.valueProperty().isNull()).
						or(datePickupPicker.valueProperty().isNull()).
						or(pickupHourCB.valueProperty().isNull()).
						or(pickupMinCB.valueProperty().isNull()).
						or(dateReturnPicker.valueProperty().isNull()).
						or(returnHourCB.valueProperty().isNull()).
						or(returnMinCB.valueProperty().isNull());
			
		/*Enable with calculate price + other details the reserve button 
		 * (reserve possible only when a customer has been saved)
		 */
			ObservableValue<Boolean> resCustObs = custIdLabel.textProperty().length().lessThan(5).
															or(carLicensePlateLB.textProperty().isEmpty()).
															or(pickupLocTF.textProperty().isEmpty()).
															or(returnLocTF.textProperty().isEmpty()).
															or((ObservableBooleanValue) calcObs);
			reserveButton.disableProperty().bind(resCustObs);
					
//CALCULATE PRICE ON ACTION	
			calcPriceButton.disableProperty().bind(calcObs);
			
			calcPriceButton.setOnAction(e -> {
				LocalDateTime ldtPickup = LocalDateTime.of(datePickupPicker.getValue().getYear(),
						datePickupPicker.getValue().getMonth(),
						datePickupPicker.getValue().getDayOfMonth(),
						Integer.parseInt(pickupHourCB.getValue()),
						Integer.parseInt(pickupMinCB.getValue()));
				
				LocalDateTime ldtReturn = LocalDateTime.of(dateReturnPicker.getValue().getYear(),
						dateReturnPicker.getValue().getMonth(),
						dateReturnPicker.getValue().getDayOfMonth(),
						Integer.parseInt(returnHourCB.getValue()),
						Integer.parseInt(returnMinCB.getValue()));
			    ldtReturn = ldtReturn.minusMinutes(45);
			    //1 day = 24 hours, and it is possible a 30 minutes late
			    int days = 0;
			    long hrs = Duration.between(ldtPickup, ldtReturn).toHours();
			    if(hrs < 24) {
			    	days = 1;
			    }
			    if(hrs >= 24) {
			    	days = (int) (hrs/24) + 1;
			    }
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
				priceDaysLabel.setText("Rent: " + days + " days*" + dailyPrice + " = " + rentPrice + " EUR");
			    priceLabelIns.setText("Insurance: " + insurancePrice + " EUR");
				priceLabel2.setText("Extras: " + extraPrices + " EUR");
				priceLabel3.setText("Total : " + (rentPrice + extraPrices + insurancePrice) + " EUR");
			});
					
			
//RES ID LABEL WHEN A RESERVATION IS SELECTED, set selected CUSTOMER and CAR, and fills all the details			
			resIdLabel.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 5) {
					try {
						for (Customer s : Database.readCustomersTable("", "")) {
							if (s.getCustomerID().equals(selectedRes.getModellObject().getCustomer().getCustomerID())) {
								selectedCust = new CustomerFX(s);
							}
						}
						for(CarFX c : observCars) {
							if (c.getModellObject().getCarVinNumber().equals(selectedRes.getModellObject().getCar().getCarVinNumber())) {
								selectedCar = c;
							}
						}
						for(CarFX c : observDeactiveCars) {
							if (c.getModellObject().getCarVinNumber().equals(selectedRes.getModellObject().getCar().getCarVinNumber())) {
								selectedCar = c;
							}
						}
						} catch (SQLException e1) {
							System.out.println("Something is wrong with the reading customers");
							e1.printStackTrace();
						}
						custIdLabel.setText(selectedRes.getModellObject().getCustomer().getCustomerID());
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
					    carLicensePlateLB.setText(selectedRes.getModellObject().getCar().getCarLicensePlate());
					    insuranceComboBox.setValue(selectedRes.getModellObject().getInsuranceType());
					    pickupLocTF.setText(selectedRes.getModellObject().getPickupLocation());
					    returnLocTF.setText(selectedRes.getModellObject().getReturnLocation());
					    datePickupPicker.setValue(selectedRes.getModellObject().getPickupTime().toLocalDate());
					    dateReturnPicker.setValue(selectedRes.getModellObject().getReturnTime().toLocalDate());
					    pickupHourCB.setValue(String.valueOf(selectedRes.getModellObject().getPickupTime().getHour()));
					    String pMin = String.valueOf(selectedRes.getModellObject().getPickupTime().getMinute());
					    if(pMin.equals("0")) {
					    	 pickupMinCB.setValue("00");
					    } else {
					    	pickupMinCB.setValue(pMin);
					    }
					    returnHourCB.setValue(String.valueOf(selectedRes.getModellObject().getReturnTime().getHour()));
					    String rMin = String.valueOf(selectedRes.getModellObject().getReturnTime().getMinute());
					    if(rMin.equals("0")) {
					    	returnMinCB.setValue("00");
					    } else {
					    	returnMinCB.setValue(rMin);
					    }
					    notesTA2.setText(selectedRes.getModellObject().getResNotes());

						for(String e : extrasOriginal) {
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
						updateResButton.disableProperty().bind(resCustObs); 
						calcPriceButton.fire();
					}
				}
			});
				
/*RESERVE BUTTON ON ACTION
 * in reserve, and update reservation, firstly it checks, that the selected car/reservation
 * is the same as before (or not null) when it was chosen, comparing the license plate/reservation ID LABEL 
 * with the actual in the observable lists. When not, it changes accordingly to the correct. It needed, 
 * because when you check a car, or reservation during the res/update procedure, the program changes 
 * the selected object accordingly. It could have make problems.			
 */
			reserveButton.setOnAction(e -> {
			  try {	
				if (selectedCar == null || !selectedCar.getCarLicensePlate().equals(carLicensePlateLB.getText())) {
					for (CarFX car : observCars) {
						if (car.getModellObject().getCarLicensePlate().equals(carLicensePlateLB.getText())) {
							selectedCar = car;
						}
					}
				}
				if (selectedCar == null || !selectedCar.getCarLicensePlate().equals(carLicensePlateLB.getText())) {
					for (CarFX car : observDeactiveCars) {
						if (car.getModellObject().getCarLicensePlate().equals(carLicensePlateLB.getText())) {
							selectedCar = car;
						}
					}
				}
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
				
				
				if (pickupTime.isAfter(returnTime)) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Make a reservation");
					alertWarn.setHeaderText("Please check again!");
					alertWarn.setContentText("The return time should be after the pickup time!");
					alertWarn.showAndWait();
					return;
				}
				for (Customer r : Database.readCustomersTable("", "deactive")) {
					if (selectedCust.getCustomerID().equals(r.getCustomerID())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Make a reservation");
						alertWarn.setHeaderText("Please check again!");
						alertWarn.setContentText("You can't make a reservation, because the customer has been DEACTIVATED!");
						alertWarn.showAndWait();
						return;
					}
				}
				for (CarFX c : observDeactiveCars) {
					if (selectedCar.getCarVinNumber().equals(c.getModellObject().getCarVinNumber())
							 || carLicensePlateLB.getText().equals(c.getModellObject().getCarLicensePlate())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Make a reservation");
						alertWarn.setHeaderText("Please check again!");
						alertWarn.setContentText("You can't make a reservation, because this car has been DEACTIVATED!");
						alertWarn.showAndWait();
						return;
					}
				}
				for (ReservationFX r : observReservations) {
					if (r.getModellObject().isStatus() == false 
							&& selectedCar.getModellObject().getCarVinNumber().equals(r.getModellObject().getCar().getCarVinNumber()) 
										&& r.getModellObject().getReturnTime().isAfter(pickupTime) 
										&& r.getModellObject().getPickupTime().isBefore(returnTime)) {
						
							Alert alertWarn = new Alert(AlertType.WARNING);
							alertWarn.setTitle("Make a reservation");
							alertWarn.setHeaderText("You can't reserve this car!");
							alertWarn.setContentText(selectedCar.getCarLicensePlate()
													+ " has a reservation around this time. Please choose another date!\n\n"
													+ "Reservation ID: '" + r.getModellObject().getResNumberID() + "'\n"
													+ "From '" + r.getModellObject().getPickupTime()
													+ "' until '" + r.getModellObject().getReturnTime() + "'");
							alertWarn.showAndWait();
							return;
					}
				}
				
				Alert alertAdd = new Alert(AlertType.CONFIRMATION);
				alertAdd.setTitle("Adding a new reservation");
				alertAdd.setHeaderText("Please confirm!");
				alertAdd.setContentText("Would you really want to ADD this new reservation?\n\n" + "Reserved category: '"
									+ reservedCategory + "'\n" + "License plate: '" + selectedCar.getCarLicensePlate()
									+ "'\n" + "For: '" + selectedCust.getModellObject().getFirstName() + " "
									+ selectedCust.getModellObject().getLastName() + "'");
				Optional<ButtonType> result = alertAdd.showAndWait();
				if (result.get() == ButtonType.OK) {
						Database.addNewReservation(newRes.getModellObject());
						observReservations.add(newRes);
						selectedRes = newRes;
						resIdLabel.setText(selectedRes.getModellObject().getResNumberID());
						rentStatusLabel.setText("Res.status : " + selectedRes.getStatusName());
						calcPriceButton.fire();
				}

			} catch (SQLException e1) {
				System.out.println("Something is wrong with the database - adding a new reservation");
				e1.printStackTrace();
			}
		});	
			
/*UPDATE RES BUTTON ON ACTION
 * Same as with reserve, but it needs to check the reservation too.
 * After updating the car, the whole reservations menu will be also reloaded, to be up to date.
 * This can be a little slower.		
 */
			updateResButton.setOnAction(e -> {
			 try {	
				if (selectedCar == null || !selectedCar.getCarLicensePlate().equals(carLicensePlateLB.getText())) {
					for (CarFX car : observCars) {
						if (car.getModellObject().getCarLicensePlate().equals(carLicensePlateLB.getText())) {
							selectedCar = car;
						}
					}
				}
				if (selectedCar == null || !selectedCar.getCarLicensePlate().equals(carLicensePlateLB.getText())) {
					for (CarFX car : observDeactiveCars) {
						if (car.getModellObject().getCarLicensePlate().equals(carLicensePlateLB.getText())) {
							selectedCar = car;
						}
					}
				}
				if (!selectedRes.getModellObject().getResNumberID().equals(resIdLabel.getText())) {
					for (ReservationFX r : observReservations) {
						if (r.getModellObject().getResNumberID().equals(resIdLabel.getText())) {
							selectedRes = r;
						}
					}
				}
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
				for (String key : extrasMap.keySet()) {
					ObservableValue<Boolean> val = extrasMap.get(key);
					if (val.getValue()) {
						extras.add(key);
					}
				}
				ReservationFX updateRes = new ReservationFX(new Reservation(resNumberID, selectedCust.getModellObject(),
						selectedCar.getModellObject(), reservedCategory, insuranceType, pickupLocation, pickupTime,
						returnLocation, returnTime, resNotes, extras, false));

				if (pickupTime.isAfter(returnTime)) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Update a reservation");
					alertWarn.setHeaderText("Please check again!");
					alertWarn.setContentText("The return time should be after the pickup time!");
					alertWarn.showAndWait();
					return;
				}
				for (Customer r : Database.readCustomersTable("", "deactive")) {
					if (selectedCust.getCustomerID().equals(r.getCustomerID())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Update a reservation");
						alertWarn.setHeaderText("Please check again!");
						alertWarn.setContentText("You can't update this reservation, because the customer has been DEACTIVATED!");
						alertWarn.showAndWait();
						return;
					}
				}
				for (CarFX c : observDeactiveCars) {
					if (selectedCar.getCarVinNumber().equals(c.getModellObject().getCarVinNumber())
							 || carLicensePlateLB.getText().equals(c.getModellObject().getCarLicensePlate())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("UPDATE a reservation");
						alertWarn.setHeaderText("Please check again!");
						alertWarn.setContentText("You can't update this reservation, because this car has been DEACTIVATED!");
						alertWarn.showAndWait();
						return;
					}
				}
				for (ReservationFX r : observReservations) {
					if (r.getModellObject().isStatus() == false
							&& selectedCar.getModellObject().getCarVinNumber().equals(r.getModellObject().getCar().getCarVinNumber())
									&& !resNumberID.equals(r.getModellObject().getResNumberID())
									&& r.getModellObject().getReturnTime().isAfter(pickupTime) 
									&& r.getModellObject().getPickupTime().isBefore(returnTime)) {

							Alert alertWarn = new Alert(AlertType.WARNING);
							alertWarn.setTitle("Update a reservation");
							alertWarn.setHeaderText("You can't update this reservation with this car!");
							alertWarn.setContentText(selectedCar.getCarLicensePlate()
													+ " has a reservation around this time. Please choose another date!\n\n"
													+ "Reservation ID: '" + r.getModellObject().getResNumberID() + "'\n" 
													+ "From '" + r.getModellObject().getPickupTime()
													+ "' until '" + r.getModellObject().getReturnTime() + "'");
							alertWarn.showAndWait();
							return;
					}
				}

				Alert alertUpdate = new Alert(AlertType.CONFIRMATION);
				alertUpdate.setTitle("Updating a reservation");
				alertUpdate.setHeaderText("Please confirm!");
				alertUpdate.setContentText("Would you really want to UPDATE this reservation?\n"
										+ "Reservation number: " + resNumberID + "\n\nReserved category: '"
										+ reservedCategory + "'\n" + "License plate: '" + selectedCar.getCarLicensePlate()
										+ "'\n" + "For: '" + selectedCust.getModellObject().getFirstName() + " "
										+ selectedCust.getModellObject().getLastName() + "'");
				Optional<ButtonType> result = alertUpdate.showAndWait();
				if (result.get() == ButtonType.OK) {
					Database.updateReservation(updateRes.getModellObject());
					observReservations.remove(selectedRes);
					observReservations.add(updateRes);
					selectedRes = updateRes;
					rentStatusLabel.setText("Res.status : " + selectedRes.getStatusName());
					calcPriceButton.fire();
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
		bottomHBoxCust.setPadding(new Insets(0, 0, 0, 30));
		bottomHBoxCust.setSpacing(10);
		bottomHBoxCust.setAlignment(Pos.BOTTOM_LEFT);

		VBox bottomVBox = new VBox(bottomHBoxCust, bottomHBoxRes);
		reserveBP.setBottom(bottomVBox);

		return reserveBP;
	}
	



//CARS MENU	
	private TableView<CarFX> showCarsTableView() {
		TableColumn<CarFX, Number> numberCol = new TableColumn<>("nr.");
		numberCol.setSortable(false);
		numberCol.setPrefWidth(35);
		numberCol.setMinWidth(30);
		
		TableColumn<CarFX, String> categorieCol = new TableColumn<>("Category");
		categorieCol.setPrefWidth(100);
		categorieCol.setMinWidth(30);
		categorieCol.setCellValueFactory(new PropertyValueFactory<>("carCategory"));
		categorieCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<CarFX, String> markeCol = new TableColumn<>("Brand");
		markeCol.setPrefWidth(110);
		markeCol.setMinWidth(30);
		markeCol.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
		markeCol.setSortType(TableColumn.SortType.ASCENDING);
		
		TableColumn<CarFX, String> modellCol = new TableColumn<>("Modell");
		modellCol.setPrefWidth(110);
		modellCol.setMinWidth(30);
		modellCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
		modellCol.setSortType(TableColumn.SortType.ASCENDING);
		
		TableColumn<CarFX, String> licPlateCol = new TableColumn<>("License Plate");
		licPlateCol.setPrefWidth(105);
		licPlateCol.setMinWidth(30);
    	licPlateCol.setCellValueFactory(new PropertyValueFactory<>("carLicensePlate"));
    
    	TableColumn<CarFX, String> fuelTypeCol = new TableColumn<>("Fuel Type");
    	fuelTypeCol.setPrefWidth(75);
    	fuelTypeCol.setMinWidth(30);
    	fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("carFuelType"));
    
    	TableColumn<CarFX, Boolean> onRentCol = new TableColumn<>("OnRent");
    	onRentCol.setPrefWidth(60);
    	onRentCol.setMinWidth(30);
    	onRentCol.setCellValueFactory(new PropertyValueFactory<>("isOnRent"));
    	onRentCol.setSortType(TableColumn.SortType.DESCENDING);
    
    	onRentCol.setCellFactory(col -> new TableCell<CarFX, Boolean>() {
	    @Override
	    	protected void updateItem(Boolean item, boolean empty) {
	    		super.updateItem(item, empty) ;
	    		setText(empty ? null : item ? "OnRent" : "Ready" );
	    	}
    	});
    	
    	carsTableView = new TableView<>(observCars);
		carsTableView.setPrefSize(600, 550);
		carsTableView.getColumns().addAll(numberCol, categorieCol, markeCol, modellCol, licPlateCol,fuelTypeCol, onRentCol);
		carsTableView.getSortOrder().addAll(onRentCol, categorieCol, markeCol, modellCol);
		carsTableView.setPlaceholder(new Label("No cars available!"));
		sortedListCars.comparatorProperty().bind(carsTableView.comparatorProperty());
	 	carsTableView.setItems(sortedListCars);
	 	
		numberCol.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(carsTableView.getItems().indexOf(column.getValue())+1));

		return carsTableView;	
}

	
	
	
	private BorderPane openCarsMenu() {
		Label basePriceLB = new Label("Base price = ");
			BorderPane carsBP = new BorderPane();
			
		    GridPane carsGP = new GridPane();
		    carsGP.setAlignment(Pos.CENTER_RIGHT);
		    carsGP.setHgap(15);
		    carsGP.setVgap(10);
		    carsGP.setMinSize(0, 0);	      

			VBox carsLeftVBox = new VBox();
			carsLeftVBox.setSpacing(5);
			carsLeftVBox.setAlignment(Pos.CENTER_LEFT);

//Searching		
			Label carSearchLB = new Label("Search for a car");
			carSearchLB.setId("searchLB");
			//Categories list, with extra "all cars" menu
			ArrayList<String> categoriesAll = new ArrayList<>();
			   categoriesAll.add("All cars");
			   for(String s: categoriesList()) {
			    	categoriesAll.add(s);
			    }
			ComboBox<String> carSearchBox = new ComboBox<>(FXCollections.observableArrayList(categoriesAll));
			carSearchBox.setId("carSearchBox");
			carSearchBox.getSelectionModel().select(0);
			
			TextField searchTF = new TextField();
			searchTF.setId("searchTF");
			searchTF.setPromptText("License plate nr.");
				    
		    HBox carSearchHB = new HBox();
			carSearchHB.setAlignment(Pos.CENTER);
			carSearchHB.setSpacing(5);	
			carSearchHB.getChildren().addAll(carSearchLB, carSearchBox, searchTF);
			carsLeftVBox.getChildren().add(carSearchHB);
			carsLeftVBox.getChildren().add(showCarsTableView());
			
/*SEARCHING LISTENERS
 * You choose an exact category, then you can check all the cars in it, 
 * or you can search by typing a license plate in the search field.	
 */
			carSearchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {	
				if(newV.contains("All cars")) {
					filteredListCars.setPredicate(s -> true);
					searchTF.setDisable(false);
					searchTF.clear();
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
		    
		    HBox carsHB = new HBox(); 
		    carsHB.setAlignment(Pos.CENTER);
		    carsHB.setPadding(new Insets(15,0,0,0));
		    carsHB.setSpacing(15);
		    carsHB.getChildren().add(carsLeftVBox);
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
		    yearPicker.setDayCellFactory(dp -> new DateCellFactory(LocalDate.MIN, LocalDate.now()));
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
		    
		   //ONLY numbers for km, max 6 characters.
		    kmTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,6}?")) {
	                    kmTF.setText(oldValue);
	                }
	                if (newValue.length() > 6) {
	                    kmTF.setText(oldValue);
	                	Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Car km");
						alertWarn.setHeaderText("Really??? :)");
						alertWarn.setContentText("This car has more than a MILLION KM? :) :D \n\n" +
												"No way, or at least, show me one, i'm going to buy it!! :D");
						alertWarn.showAndWait();
						return;
	                }
	            }
	        });
		    
//COMBOBOX		    
//FUELTYPES
		    ComboBox<String> carFuelBox = new ComboBox<>(FXCollections.observableArrayList(fuelTypesList()));
		    carFuelBox.setPrefWidth(195);
		    carFuelBox.setPromptText("Fuel");
		    carsGP.add(carFuelBox, 0, 5);
//TRANSMISSION		   
		    ComboBox<String> carTransmBox = new ComboBox<>(FXCollections.observableArrayList(transmissionsList()));
		    carTransmBox.setPrefWidth(195);
		    carTransmBox.setPromptText("Transmission");
		    carsGP.add(carTransmBox, 0, 6);
	    
//Reservations
		    Label reservationsLB = new Label("Active reservations");
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
		    //Chosen category shows the base price of the car
		    carCategorieBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					basePriceLB.setText("Base price = " + getCategoryPrice(newValue) + " EUR/day");
				}
			});
		   		    
		    TextField licPlateTF = new TextField();
		    licPlateTF.setPromptText("License plate nr.");
		    carsGP.add(licPlateTF, 1, 2);
		    
		    TextField vinNumTF = new TextField();
		    vinNumTF.setPromptText("VIN number");
		    carsGP.add(vinNumTF, 1, 3);
		    //All the VIN numbers are max the real 17 characters
		    vinNumTF.setOnKeyTyped(e -> {
		    	int maxChar = 17;
		    	if(vinNumTF.getText().length() == maxChar) 
		    	e.consume();
		    });

//COLOR TYPES		    
		    ComboBox<String> carColorBox = new ComboBox<>(FXCollections.observableArrayList(carColorList()));
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
		    
		    //Only numbers for engine, max 3, 4 characters  
		    engineSizeTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,4}?")) {
	                	engineSizeTF.setText(oldValue);
	                }
	            }
	        });   
		    enginePowerTF.textProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                if (!newValue.matches("\\d{0,3}?")) {
	                	enginePowerTF.setText(oldValue);
	                }
	            }
	        });
		      
//Features LISTVIEW CHECKBOX
		    Label featuresLB = new Label("Features");
		    carsGP.add(featuresLB, 1, 7);

		    LinkedHashMap<String, ObservableValue<Boolean>> featuresMap = new LinkedHashMap<>();
				for(String e : featuresList()) {
				 featuresMap.put(e, new SimpleBooleanProperty(false));
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
//ON RENT 		
		Button onRentButton = new Button("On rent");
		  	onRentButton.setId("onRentButton");
		  	onRentButton.setDisable(true);
		  		
		  	onRentButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
		  				new EventHandler<MouseEvent>() {
		  				  @Override
		  				  public void handle(MouseEvent e) {
		  					  onRentButton.setEffect(shadow);
		  		          }
		  		        });
		  	onRentButton.addEventHandler(MouseEvent.MOUSE_EXITED,
		  		        new EventHandler<MouseEvent>() {
		  		          @Override
		  		          public void handle(MouseEvent e) {
		  		        	  onRentButton.setEffect(null);
		  		          }
		  				});	    
	  
		  
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
/*Disable property for add/update car. Update a car is possible after it is chosen,
 * but it should be disabled, when somehting is empty
 */
		ObservableValue<Boolean> carBT = brandTF.textProperty().isEmpty()
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
					.or(enginePowerTF.textProperty().isEmpty());
		
		addCarButton.disableProperty().bind(carBT);
		
//Filling text fields from table view selected items, enable buttons, or disable, when nothing is selected
			carsTableView.getSelectionModel().selectedItemProperty().addListener((o, oldS, newS) -> {
				if (newS != null) {
					selectedCar = carsTableView.getSelectionModel().getSelectedItem();
					deleteCarButton.setDisable(false);
					makeResButton.setDisable(false);
					onRentButton.setDisable(false);
					updateCarButton.disableProperty().bind(carBT);
					
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
		//Clear features map to empty, then fill it with the selected cars features			
					for(String e : featuresOriginal) {
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
			//Shows the active reservations for car
				reservationsLV.getItems().clear();
				ArrayList<String> ls = new ArrayList<>();
				for (ReservationFX r : observReservations) {
					if (r.getModellObject().getCar().getCarVinNumber()
									.equals(selectedCar.getModellObject().getCarVinNumber())
									&& r.getModellObject().getReturnTime().isAfter(LocalDateTime.now())
									&& r.getModellObject().isStatus() == false) {
						ls.add(r.getPickupTime() + " - " + r.getReturnTime());
					}
				}
				Collections.sort(ls);
				reservationsLV.getItems().addAll(ls);
			}
			if(newS == null) {
				makeResButton.setDisable(true);
				onRentButton.setDisable(true);
				deleteCarButton.setDisable(true);
				updateCarButton.disableProperty().unbind();
				updateCarButton.setDisable(true);
			}
		});
			
			
/*ON RENT ON ACTION. 
 * It is for the 'self-control', you can manage it freely, to see how many have you got on-rent,...
 * With returning, you need to write the actual km, when it is smaller, then before, nothing will be updated.
 * Because thus funktion is prepared too, later it is easyto connect with an extended part of the program, 
 * where the real rental agreements are made from the reservations.
 */
		onRentButton.setOnAction(e -> {
			try {
				String vinNum = selectedCar.getModellObject().getCarVinNumber();
				String licPlate = selectedCar.getModellObject().getCarLicensePlate();
				if (selectedCar.isOnRent()) {
					ReturnCarDialog retCar = new ReturnCarDialog(selectedCar.getModellObject());
					Optional<Integer> result = retCar.showAndWait();
					if (result.isPresent()) {
						int km = result.get();
						if (km < selectedCar.getModellObject().getCarKM()) {
							System.out.println("The returned km must be BIGGER than the old km!");
							return;
						} else {
							Database.checkInCar(vinNum, km);
							CarFX newCar = new CarFX(new Car(vinNum,
													licPlate,
													selectedCar.getModellObject().getCarBrand(),
													selectedCar.getModellObject().getCarModel(),
													selectedCar.getModellObject().getCarCategory(),
													selectedCar.getModellObject().getCarColor(),
													selectedCar.getModellObject().getCarFuelType(),
													selectedCar.getModellObject().getCarTransmission(),
													selectedCar.getModellObject().getCarManufDate(),
													km,
													selectedCar.getModellObject().getCarEngineSize(),
													selectedCar.getModellObject().getCarEnginePower(),
													selectedCar.getModellObject().getCarFeatures(),
													false));
							observCars.remove(selectedCar);
							observCars.add(newCar);
						}
					}		
					return;
				} else {
					Alert alertConf = new Alert(AlertType.CONFIRMATION);
					alertConf.setTitle("Check-out car");
					alertConf.setHeaderText("Please confirm!");
					alertConf.setContentText("Check-out '" + licPlate + "'?");
					Optional<ButtonType> result = alertConf.showAndWait();
					if (result.get() == ButtonType.OK) {
						Database.checkOutCar(vinNum);
						CarFX newCar = new CarFX(new Car(vinNum,
												licPlate,
												selectedCar.getModellObject().getCarBrand(),
												selectedCar.getModellObject().getCarModel(),
												selectedCar.getModellObject().getCarCategory(),
												selectedCar.getModellObject().getCarColor(),
												selectedCar.getModellObject().getCarFuelType(),
												selectedCar.getModellObject().getCarTransmission(),
												selectedCar.getModellObject().getCarManufDate(),
												selectedCar.getModellObject().getCarKM(),
												selectedCar.getModellObject().getCarEngineSize(),
												selectedCar.getModellObject().getCarEnginePower(),
												selectedCar.getModellObject().getCarFeatures(),
												true));
						observCars.remove(selectedCar);
						observCars.add(newCar);
					}
				}
			} catch (SQLException e1) {
				System.out.println("Something is wrong with the onRent Database connection");
				e1.printStackTrace();
			}
		});
				
	
//RESERVE CAR ACTION			
		makeResButton.setOnAction(e -> {
			mainTabPane.getSelectionModel().select(reserveTab);
			selectedCar = carsTableView.getSelectionModel().getSelectedItem();
			carLicensePlateLB.setText(selectedCar.getModellObject().getCarLicensePlate());
			carComboBox.getSelectionModel().select(selectedCar.getModellObject().getCarCategory());
		});

/*DELETE ACTION	
 * When the car has an active (not expired, from the past, and not cancelled) reservation,
 * it is not possible to delete.	
 */
		deleteCarButton.setOnAction(e -> {
				for (ReservationFX r : observReservations) {
					if (r.getModellObject().getCar().getCarVinNumber().equals(selectedCar.getModellObject().getCarVinNumber())
								&& r.getModellObject().getReturnTime().isAfter(LocalDateTime.now())) {
						Alert alertWarn = new Alert(AlertType.WARNING);
						alertWarn.setTitle("Deleting a car");
						alertWarn.setHeaderText("You can't delete this car!");
						alertWarn.setContentText(selectedCar.getCarLicensePlate() + " still has a reservation (ID = '"
												+ r.getResNumberID() + "').\n" + "You should first cancel the reservation.");
						alertWarn.showAndWait();
						return;
					}
				} 

			Alert alertDelete = new Alert(AlertType.CONFIRMATION);
			alertDelete.setTitle("Deleting a car");
			alertDelete.setHeaderText("Please confirm!");
			alertDelete.setContentText("Would you really want to DELETE the selected '"
										+ selectedCar.getModellObject().getCarBrand() + " " + selectedCar.getModellObject().getCarModel()
										+ "' with the license plate '" + selectedCar.getModellObject().getCarLicensePlate() + "'?");
			Optional<ButtonType> result = alertDelete.showAndWait();
			if (result.get() == ButtonType.OK) {
				try {
					Database.deleteCar(selectedCar.getModellObject().getCarVinNumber());
					observDeactiveCars.add(selectedCar);
					if(carLicensePlateLB.getText().equals(selectedCar.getModellObject().getCarLicensePlate())) {
					carStatusLabel.setText("Car status : DEACTIVE");
					}
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
				for (String key : featuresMap.keySet()) {

					ObservableValue<Boolean> val = featuresMap.get(key);
					if (val.getValue()) {
						features.add(key);
					}
				}
				CarFX car = new CarFX(new Car(vinNumber, licPlate, brand, model, category, color, fuel, transm,
											 manufDate, carKM, engSize, engPower, features, selectedCar.isOnRent()));

				String vinNDatabase = Database.checkExistingCar(vinNumber, "");
				String licPDatabase = Database.checkExistingCar("", licPlate);
				if (vinNDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it seems it's a new car!");
					alertWarn.setContentText("Car with the VIN number '" + vinNumber + "' doesn't exists! Please add as a new car! "
											+ "\n\nNote, that it is NOT possible to update the VIN number.");
					alertWarn.showAndWait();
					return;
				}
				if (licPDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it seems it's a new car!");
					alertWarn.setContentText("Car with the license plate '" + licPlate + "' doesn't exists! Please add as a new car! "
											+ "\n\nNote, that it is NOT possible to update the license plate number.");
					alertWarn.showAndWait();
					return;
				}
				if (!selectedCar.getCarVinNumber().equals(vinNumber)
						&& !vinNDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it's an existing car!");
					alertWarn.setContentText("Car with the VIN number '" + vinNumber + "' already exists!\n\n"
											+ "This car is a(n) " + vinNDatabase);
					alertWarn.showAndWait();
					return;
				}
				if (!selectedCar.getCarLicensePlate().equals(licPlate)
						&& !licPDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Updating a car");
					alertWarn.setHeaderText("Please check again, it's an existing car!");
					alertWarn.setContentText("Car with the license plate '" + licPlate + "' already exists!\n\n"
											+ "This car is a(n) " + licPDatabase);
					alertWarn.showAndWait();
					return;
				}
				if (vinNumber.length() < 11) {
					Alert alertWarn2 = new Alert(AlertType.WARNING);
					alertWarn2.setTitle("Updating a car");
					alertWarn2.setHeaderText("Please check again the VIN number!");
					alertWarn2.setContentText("Car's VIN number should be at least 11 characters "
											+ "(in cars made after 1981 every VIN number should be 17 characters)!");
					alertWarn2.showAndWait();
					return;

				}
				if (engSize < minEngineSize) {
					Alert alertWarn3 = new Alert(AlertType.WARNING);
					alertWarn3.setTitle("Updating a car");
					alertWarn3.setHeaderText("Please check again the engine size!");
					alertWarn3.setContentText("Is the engine size is really so small? Only " + engSize + " CCM?");
					alertWarn3.showAndWait();
					return;
				}
				if (engPower < minEnginePower) {
					Alert alertWarn4 = new Alert(AlertType.WARNING);
					alertWarn4.setTitle("Updating a car");
					alertWarn4.setHeaderText("Please check again the engine power!");
					alertWarn4.setContentText("Is the engine so weak? Engine power only " + engPower + " KW?");
					alertWarn4.showAndWait();
					return;
				}

				Alert alertUpdate = new Alert(AlertType.CONFIRMATION);
				alertUpdate.setTitle("Updating a car");
				alertUpdate.setHeaderText("Please confirm!");
				alertUpdate.setContentText("Would you really want to UPDATE this '" + brand + " " + model
											+ "' with the VIN number '" + vinNumber + "'?");
				Optional<ButtonType> result = alertUpdate.showAndWait();
				if (result.get() == ButtonType.OK) {
					Database.updateCar(car.getModellObject());
					observCars.remove(selectedCar);
					observCars.add(car);
					selectedCar = car;
					reservationsBP.setCenter(showReservationsTableView());
				}

			} catch (SQLException e1) {
				System.out.println("Something is wrong with the database - update car");
				e1.printStackTrace();
			}
		});
					
			
//ADD NEW CAR ACTION
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
				for (String key : featuresMap.keySet()) {
					ObservableValue<Boolean> val = featuresMap.get(key);
					if (val.getValue()) {
						features.add(key);
					}
				}
				CarFX newCar = new CarFX(new Car(vinNumber, licPlate, brand, model, category, color, fuel, transm,
												manufDate, carKM, engSize, engPower, features, false));
				// ALERT
				String vinNDatabase = Database.checkExistingCar(vinNumber, "");
				String licPDatabase = Database.checkExistingCar("", licPlate);
				if (!vinNDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Adding a new car");
					alertWarn.setHeaderText("Please check again, it's an existing car!");
					alertWarn.setContentText("Car with the VIN number '" + vinNumber + "' already exists!\n\n"
											+ "This car is a(n) " + vinNDatabase);
					alertWarn.showAndWait();
					return;
				}
				if (!licPDatabase.equals("")) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Adding a new car");
					alertWarn.setHeaderText("Please check again, it's an existing car!");
					alertWarn.setContentText("Car with the license plate '" + licPlate + "' already exists!\n\n"
											+ "This car is a(n) " + licPDatabase);
					alertWarn.showAndWait();
					return;
				}
				if (vinNumber.length() < 11) {
					Alert alertWarn2 = new Alert(AlertType.WARNING);
					alertWarn2.setTitle("Adding a new car");
					alertWarn2.setHeaderText("Please check again the VIN number!");
					alertWarn2.setContentText("Car's VIN number should be at least 11 characters "
											+ "(in cars made after 1981 every VIN number should be 17 characters)!");
					alertWarn2.showAndWait();
					return;

				}
				if (engSize < minEngineSize) {
					Alert alertWarn3 = new Alert(AlertType.WARNING);
					alertWarn3.setTitle("Adding a new car");
					alertWarn3.setHeaderText("Please check again the engine size!");
					alertWarn3.setContentText("Is the engine size is really so small? Only " + engSize + " CCM?");
					alertWarn3.showAndWait();
					return;
				}
				if (engPower < minEnginePower) {
					Alert alertWarn4 = new Alert(AlertType.WARNING);
					alertWarn4.setTitle("Adding a new car");
					alertWarn4.setHeaderText("Please check again the engine power!");
					alertWarn4.setContentText("Is the engine so weak? Engine power only " + engPower + " KW?");
					alertWarn4.showAndWait();
					return;
				}

				Alert alertAdd = new Alert(AlertType.CONFIRMATION);
				alertAdd.setTitle("Adding a new car");
				alertAdd.setHeaderText("Please confirm!");
				alertAdd.setContentText("Would you really want to ADD this '" + brand + " " + model
										+ "' with the license plate '" + licPlate + "'?");
				Optional<ButtonType> result = alertAdd.showAndWait();
				if (result.get() == ButtonType.OK) {
					Database.addNewCar(newCar.getModellObject());
					observCars.add(newCar);
					selectedCar = newCar;
				}
			} catch (SQLException e1) {
				System.out.println("Something is wrong with the database - adding a new car");
				e1.printStackTrace();
			}
		});
  
		HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 5, 0, 0));
		bottomHBox.setSpacing(10);
		bottomHBox.getChildren().addAll(makeResButton, onRentButton, deleteCarButton, updateCarButton, addCarButton);
		bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		carsBP.setBottom(bottomHBox);
			 
		
		return carsBP;
	}
	
	
	
	
//RESERVATIONS MENU	
	private TableView<ReservationFX> showReservationsTableView(){
		TableColumn<ReservationFX, Number> numberCol = new TableColumn<>("nr.");
		numberCol.setSortable(false);
		numberCol.setPrefWidth(30);
		numberCol.setMinWidth(30);
		
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
		carCatCol.setPrefWidth(75);
		carCatCol.setMinWidth(30);
		carCatCol.setCellValueFactory(new PropertyValueFactory<>("reservedCategory"));
		
		TableColumn<ReservationFX, String> carLicensePlateCol = new TableColumn<>("Lic. plate");
		carLicensePlateCol.setPrefWidth(80);
		carLicensePlateCol.setMinWidth(30);
		carLicensePlateCol.setCellValueFactory(new PropertyValueFactory<>("carLicensePlate"));
		
		TableColumn<ReservationFX, String> pickupTimeCol = new TableColumn<>("PICKUP time");
		pickupTimeCol.setPrefWidth(125);
		pickupTimeCol.setMinWidth(30);
		pickupTimeCol.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
		pickupTimeCol.setSortType(TableColumn.SortType.ASCENDING);
		
		TableColumn<ReservationFX, String> pickupLocCol = new TableColumn<>("Pickup location");
		pickupLocCol.setPrefWidth(105);
		pickupLocCol.setMinWidth(30);
		pickupLocCol.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
		
		TableColumn<ReservationFX, String> returnTimeCol = new TableColumn<>("RETURN time");
		returnTimeCol.setPrefWidth(125);
		returnTimeCol.setMinWidth(30);
		returnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
		
		TableColumn<ReservationFX, String> returnLocCol = new TableColumn<>("Return location");
		returnLocCol.setPrefWidth(105);
		returnLocCol.setMinWidth(30);
		returnLocCol.setCellValueFactory(new PropertyValueFactory<>("returnLocation"));
		
		TableColumn<ReservationFX, String> statusCol = new TableColumn<>("Status");
		statusCol.setPrefWidth(75);
		statusCol.setMinWidth(30);
		statusCol.setCellValueFactory(new PropertyValueFactory<>("statusName"));
		statusCol.setSortType(TableColumn.SortType.ASCENDING);
		
		observReservations.clear();
		fillReservationsObservableList();
		
	    reservTableView = new TableView<>(observReservations);
	    reservTableView.getColumns().addAll(numberCol, resNumCol, custFirstNameCol, custLastNameCol, carCatCol, carLicensePlateCol,
	    									pickupTimeCol, pickupLocCol, returnTimeCol, returnLocCol, statusCol);
	    reservTableView.getSortOrder().addAll(statusCol, pickupTimeCol);
	    reservTableView.setPlaceholder(new Label("No reservations available!"));
	    reservTableView.setPrefSize(1000, 570);
	    
	    numberCol.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(reservTableView.getItems().indexOf(column.getValue())+1));
		
		sortedListReservations.comparatorProperty().bind(reservTableView.comparatorProperty());
		reservTableView.setItems(sortedListReservations);
		//Enable/disable buttons according to if it is selected or not
		reservTableView.getSelectionModel().selectedItemProperty().addListener((o, oldS, newS) -> {
			if (newS != null) {
				selectedRes = reservTableView.getSelectionModel().getSelectedItem();
				printPdfButton.setDisable(false);
				showResButton.setDisable(false);
				deleteResButton.setDisable(false);
			}
			if(newS == null) {
				printPdfButton.setDisable(true);
				showResButton.setDisable(true);
				deleteResButton.setDisable(true);
			}
		});
		return reservTableView;
	}

	

	private BorderPane openReservationsMenu() {
		reservationsBP = new BorderPane();			
//Search with more options		
		ComboBox<String> searchCB = new ComboBox<>();
		searchCB.setItems(FXCollections.observableArrayList("All reservations", "Reservation ID", "First name", "Last name"));
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
		
		reservationsBP.setCenter(showReservationsTableView());
		
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
				if(newValue.contains("All reservations")){
					resSearchTF.clear();
					resSearchTF.setDisable(true);
					filteredListReservations.setPredicate(s -> true);
				}
			}
		});
				
			
//Bottom Buttons		
//PRINT PDF			
		printPdfButton = new Button("Res. PDF");
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
		deleteResButton = new Button("Cancel");
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
		showResButton = new Button("Details");
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

		
		
/*PRINT PDF ON ACTION
 * Firstly, it generates the pdf, then it is possible to open it in the file chooser
 */
		printPdfButton.setOnAction(e -> {
			 if (selectedRes != null) {
				 	PdfGeneration pdfCreator = new PdfGeneration();
					pdfCreator.pdfGenerateReservation(selectedRes.getModellObject(),
													  getCategoryPrice(selectedRes.getReservedCategory()), 
													  getInsurancePrice(selectedRes.getInsuranceType()));
			 }
			 Alert alertReady = new Alert(AlertType.CONFIRMATION);
			 alertReady.setTitle("PDF from this reservation has been successfully created!");
			 alertReady.setHeaderText("Open it?");
			 alertReady.setContentText("Would you want to open the created .pdf file? File name: '" 
					 					+ selectedRes.getModellObject().getResNumberID() + ".pdf'");
				Optional<ButtonType> result = alertReady.showAndWait();
				if(result.get() == ButtonType.OK) {
					 FileChooser fileChooser = new FileChooser(); 
					 fileChooser.setInitialDirectory(new File("Reservations/"));
					 fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
					 fileChooser.setTitle("Open reservation PDF");
					 fileChooser.showOpenDialog(mainStage);
				}
		});

		
/*DELETE RES ON ACTION
 * Already cancelled reservation can't be cancelled
 */
		deleteResButton.setOnAction(e -> {
			ReservationFX newRes = null;
			for (ReservationFX r : observReservations) {
				if (r.getModellObject().getResNumberID().equals(selectedRes.getModellObject().getResNumberID())
						&& r.getModellObject().isStatus()) {
					Alert alertWarn = new Alert(AlertType.WARNING);
					alertWarn.setTitle("Cancelling a reservation");
					alertWarn.setHeaderText("Please check again, already cancelled!");
					alertWarn.setContentText("You wanted to cancel this reservation '"
									+ r.getModellObject().getResNumberID() + "', " + "but it's already cancelled.");
					alertWarn.showAndWait();
					return;
				}
				if (r.getModellObject().getResNumberID().equals(selectedRes.getModellObject().getResNumberID())
						&& r.getModellObject().isStatus() == false) {
						     newRes = new ReservationFX(new Reservation(
									r.getModellObject().getResNumberID(), r.getModellObject().getCustomer(),
									r.getModellObject().getCar(), r.getModellObject().getReservedCategory(),
									r.getModellObject().getInsuranceType(), r.getModellObject().getPickupLocation(),
									r.getModellObject().getPickupTime(), r.getModellObject().getReturnLocation(),
									r.getModellObject().getReturnTime(), r.getModellObject().getResNotes(),
									r.getModellObject().getResExtras(), true));
				}	
			}	
			if(newRes != null) {
				Alert alertCancel = new Alert(AlertType.CONFIRMATION);
				alertCancel.setTitle("Cancelling a reservation");
				alertCancel.setHeaderText("Please confirm!");
				alertCancel.setContentText("Would you really want to CANCEL the selected '"
										+ selectedRes.getModellObject().getResNumberID() + "' reservation?");
				Optional<ButtonType> result = alertCancel.showAndWait();
				if (result.get() == ButtonType.OK) {
					try {
						Database.cancelReservation(selectedRes.getModellObject().getResNumberID());
						observReservations.remove(selectedRes);
						observReservations.add(newRes);
						if(resIdLabel.getText().equals(newRes.getModellObject().getResNumberID())) {
						rentStatusLabel.setText("Status : " + newRes.getStatusName());
						}
					} catch (SQLException e1) {
							System.out.println("Something is wrong with the cancel res database connection");
							e1.printStackTrace();
					}
				}
			}
		});
			
/*SHOW RES ON ACTION
 * Res.number id will be changed, and then everything. If the selected res-customer is deactive, 
 * or selected res-car, the id will be changed accordingly.
 */
		showResButton.setOnAction(e -> {
			try {
				mainTabPane.getSelectionModel().select(reserveTab);
				resIdLabel.setText("");
				resIdLabel.setText(selectedRes.getModellObject().getResNumberID());
				rentStatusLabel.setText("Res.status : " + selectedRes.getStatusName());
				customerStatusLabel.setText("Cust.status: active");
				carStatusLabel.setText("Car status : active");
				
				for (Customer r : Database.readCustomersTable("", "deactive")) {
					if (r.getCustomerID().equals(selectedRes.getModellObject().getCustomer().getCustomerID())) {
						customerStatusLabel.setText("Cust.status: DEACTIVE");
					} 
				}
				for (CarFX r : observDeactiveCars) {
					if (r.getModellObject().getCarVinNumber().equals(selectedRes.getModellObject().getCar().getCarVinNumber())) {
						carStatusLabel.setText("Car status : DEACTIVE");
					}
				}
			} catch (SQLException e1) {
				System.out.println("Something is wrong with the status Database reading");
				e1.printStackTrace();
			}
		});
		
	    HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 5, 0, 0));
		bottomHBox.setSpacing(10);
		bottomHBox.getChildren().addAll(printPdfButton, deleteResButton, showResButton);
		bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		reservationsBP.setBottom(bottomHBox);
	     
		return reservationsBP;
		
	}
	
	
	
	
/*DASHBOARD MENU, LocalDate ldt is the actual chosen date in the menu, 
 * from this day will the whole statistic / PDF printing generated
 */
	private BorderPane dashboardMenu(LocalDate ldt) {
		BorderPane dashboardBP = new BorderPane();
		int nowCO = 0;
		int nowplus1CO = 0;
		int nowplus2CO = 0;
		int nowplus3CO = 0;
		int nowplus4CO = 0;
		int nowplus5CO = 0;
		int nowplus6CO = 0;

		int nowCI = 0;
		int nowplus1CI = 0;
		int nowplus2CI = 0;
		int nowplus3CI = 0;
		int nowplus4CI = 0;
		int nowplus5CI = 0;
		int nowplus6CI = 0;
		
		int actualMonth = 0;
		int actualMonthMins1 = 0;
		int actualMonthMins2 = 0;
		int actualMonthMins3 = 0;
		int actualMonthMins4 = 0;
		int actualMonthMins5 = 0;

		//Initialize time now
		LocalDate localDate = ldt;
		//Creating the actual next 7 days
		LocalDateTime startToday = localDate.atStartOfDay();
		LocalDate localDatePlus1 = localDate.plusDays(1);
		LocalDate localDatePlus2 = localDate.plusDays(2);
		LocalDate localDatePlus3 = localDate.plusDays(3);
		LocalDate localDatePlus4 = localDate.plusDays(4);
		LocalDate localDatePlus5 = localDate.plusDays(5);
		LocalDate localDatePlus6 = localDate.plusDays(6);
		//Creating the last 6 month
		LocalDate startThisMonth = localDate.with(firstDayOfMonth());
		//Creating ArrayLists for the todays, and the weekly reservation
		ArrayList<Reservation> todaysResCO = new ArrayList<>();
		ArrayList<Reservation> todaysResCI = new ArrayList<>();
		ArrayList<Reservation> weeklyResCO = new ArrayList<>();
		ArrayList<Reservation> weeklyResCI = new ArrayList<>();

			for(ReservationFX s : observReservations) {
				Reservation r = s.getModellObject();
				if(r.isStatus() == false) {
				//TODAY
				if(r.getPickupTime().isAfter(startToday) && 
							r.getPickupTime().isBefore(startToday.plusHours(24))) {	
					nowCO++;
					todaysResCO.add(r);
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday) && 
							r.getReturnTime().isBefore(startToday.plusHours(24))) {	
					nowCI++;  
					todaysResCI.add(r);
					weeklyResCI.add(r);
				}
				//TOMORROW
				if(r.getPickupTime().isAfter(startToday.plusHours(24)) && 
							r.getPickupTime().isBefore(startToday.plusHours(48))) {	
					nowplus1CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(24)) && 
							r.getReturnTime().isBefore(startToday.plusHours(48))) {	
					nowplus1CI++;
					weeklyResCI.add(r);
				}
				//+2. DAY
				if(r.getPickupTime().isAfter(startToday.plusHours(48)) && 
							r.getPickupTime().isBefore(startToday.plusHours(72))) {	
					nowplus2CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(48)) && 
							r.getReturnTime().isBefore(startToday.plusHours(72))) {	
					nowplus2CI++;
					weeklyResCI.add(r);
				}
				//+3. DAY
				if(r.getPickupTime().isAfter(startToday.plusHours(72)) && 
							r.getPickupTime().isBefore(startToday.plusHours(96))) {	
					nowplus3CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(72)) && 
							r.getReturnTime().isBefore(startToday.plusHours(96))) {	
					nowplus3CI++;
					weeklyResCI.add(r);
				}
				//+4. DAY
				if(r.getPickupTime().isAfter(startToday.plusHours(96)) && 
							r.getPickupTime().isBefore(startToday.plusHours(120))) {	
					nowplus4CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(96)) && 
							r.getReturnTime().isBefore(startToday.plusHours(120))) {	
					nowplus4CI++;
					weeklyResCI.add(r);
				}
				//+5. DAY
				if(r.getPickupTime().isAfter(startToday.plusHours(120)) && 
							r.getPickupTime().isBefore(startToday.plusHours(144))) {	
					nowplus5CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(120)) && 
							r.getReturnTime().isBefore(startToday.plusHours(144))) {	
					nowplus5CI++;
					weeklyResCI.add(r);
				}
				//+6. DAY
				if(r.getPickupTime().isAfter(startToday.plusHours(144)) && 
							r.getPickupTime().isBefore(startToday.plusHours(168))) {	
					nowplus6CO++;
					weeklyResCO.add(r);
				}
				if(r.getReturnTime().isAfter(startToday.plusHours(144)) && 
							r.getReturnTime().isBefore(startToday.plusHours(168))) {	
					nowplus6CI++;
					weeklyResCI.add(r);
				}	
				//MONTHLY, THIS MONTH
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth.plusMonths(1)) &&  
							r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusDays(1))) {
					actualMonth++;
				}
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth) && 
						r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusMonths(1).minusDays(1))) {
					actualMonthMins1++;
				}
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth.minusMonths(1)) && 
						r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusMonths(2).minusDays(1))) {
					actualMonthMins2++;
				}
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth.minusMonths(2)) && 
						r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusMonths(3).minusDays(1))) {
					actualMonthMins3++;
				}
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth.minusMonths(3)) && 
						r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusMonths(4).minusDays(1))) {
					actualMonthMins4++;
				}
				if(r.getPickupTime().toLocalDate().isBefore(startThisMonth.minusMonths(4)) && 
						r.getPickupTime().toLocalDate().isAfter(startThisMonth.minusMonths(5).minusDays(1))) {
					actualMonthMins5++;
				}
				}
			}
		
//CREATING WEEKLY STATISTIC
			String today = localDate.getDayOfWeek() + "\n" +  localDate.getDayOfMonth() + " " + localDate.getMonth();
			String todayPlus1 = localDatePlus1.getDayOfWeek() + "\n" +  localDatePlus1.getDayOfMonth() + " " + localDatePlus1.getMonth();
			String todayPlus2 = localDatePlus2.getDayOfWeek() + "\n" +  localDatePlus2.getDayOfMonth() + " " + localDatePlus2.getMonth();
			String todayPlus3 = localDatePlus3.getDayOfWeek() + "\n" +  localDatePlus3.getDayOfMonth() + " " + localDatePlus3.getMonth();
			String todayPlus4 = localDatePlus4.getDayOfWeek() + "\n" +  localDatePlus4.getDayOfMonth() + " " + localDatePlus4.getMonth();
			String todayPlus5 = localDatePlus5.getDayOfWeek() + "\n" +  localDatePlus5.getDayOfMonth() + " " + localDatePlus5.getMonth();
			String todayPlus6 = localDatePlus6.getDayOfWeek() + "\n" +  localDatePlus6.getDayOfMonth() + " " + localDatePlus6.getMonth();
        
			final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        final BarChart<String,Number> bc =  new BarChart<>(xAxis,yAxis);
	        bc.setTitle("Weekly Summary");
	        xAxis.setLabel("Days");
	        yAxis.setLabel("Amount");
		   
	        XYChart.Series<String, Number> seriesCO = new XYChart.Series<>();
	        seriesCO.setName("Check-out");       
	        seriesCO.getData().add(new XYChart.Data<>(today, nowCO));
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus1, nowplus1CO));
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus2, nowplus2CO));
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus3, nowplus3CO));
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus4, nowplus4CO));
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus5, nowplus5CO)); 
	        seriesCO.getData().add(new XYChart.Data<>(todayPlus6, nowplus6CO)); 
	        
	        XYChart.Series<String, Number> seriesCI = new XYChart.Series<>();
	        seriesCI.setName("Check-in");
	        seriesCI.getData().add(new XYChart.Data<>(today, nowCI));
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus1, nowplus1CI));
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus2, nowplus2CI));
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus3, nowplus3CI));
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus4, nowplus4CI));
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus5, nowplus5CI)); 
	        seriesCI.getData().add(new XYChart.Data<>(todayPlus6, nowplus6CI)); 
	        bc.getData().addAll(seriesCO, seriesCI);
		
	        int weeklyTotalCO = (nowCO + nowplus1CO + nowplus2CO + nowplus3CO + nowplus4CO + nowplus5CO + nowplus6CO);
	        int weeklyTotalCI = (nowCI + nowplus1CI + nowplus2CI + nowplus3CI + nowplus4CI + nowplus5CI + nowplus6CI);
	        
	        Label infoWeekly = new Label("The next 7 days (from "+ ldt.getDayOfMonth() +" "+ ldt.getMonth() +") you have total " + weeklyTotalCO + " CO, and " +  weeklyTotalCI + " CI");
	        Label infoDaily= new Label("Today (" + localDate.getDayOfWeek() + ") you have total " + nowCO + " CO, and "+ nowCI + " CI");
	        VBox vboxWeekly = new VBox(bc, infoWeekly, infoDaily);
	        vboxWeekly.setAlignment(Pos.TOP_CENTER);
		    
//CREATING LAST 6 MONTH STATISTIC		    
			String thisMonth = localDate.getMonth().toString();
			String thisMonthMin1 = localDate.getMonth().minus(1).toString();
			String thisMonthMin2 = localDate.getMonth().minus(2).toString();
			String thisMonthMin3 = localDate.getMonth().minus(3).toString();
			String thisMonthMin4 = localDate.getMonth().minus(4).toString();
			String thisMonthMin5 = localDate.getMonth().minus(5).toString();
		    
		    
		    final CategoryAxis xAxisM = new CategoryAxis();
	        final NumberAxis yAxisM = new NumberAxis();   
	        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxisM,yAxisM);      
	        lineChart.setTitle("Reservations summary, last 6 month");
	        xAxisM.setLabel("Months");  
	        yAxisM.setLabel("Amount");
	        
	        XYChart.Series<String, Number> seriesMonth = new XYChart.Series<>();
	        seriesMonth.setName("Reservations counter");
		    
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonthMin5, actualMonthMins5));
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonthMin4, actualMonthMins4));
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonthMin3, actualMonthMins3));
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonthMin2, actualMonthMins2));
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonthMin1, actualMonthMins1));
	        seriesMonth.getData().add(new XYChart.Data<>(thisMonth, actualMonth));   
	        lineChart.getData().add(seriesMonth);
	        
	        int monthlyTotal = (actualMonth + actualMonthMins1 + actualMonthMins2 + actualMonthMins3 + actualMonthMins4 + actualMonthMins5);
	        int avgTotal = (monthlyTotal / 6);
	        Label infoMonthlySum = new Label("The last 6 months (" + thisMonth + " and before) you have had total " + monthlyTotal + " reservations");
	        Label avgTotalRes = new Label("Average "  + avgTotal + " reservations/month");
	        Label infoMonthly = new Label("In this month (" + thisMonth + ") you have total " + actualMonth + " reservations");
	        VBox vboxMonthly = new VBox(lineChart, infoMonthlySum, avgTotalRes, infoMonthly);
	        vboxMonthly.setAlignment(Pos.TOP_CENTER);
 
	        
	        HBox centerHB = new HBox(vboxWeekly, vboxMonthly);
	        centerHB.setPadding(new Insets(20, 0, 0, 0));
	        centerHB.setAlignment(Pos.CENTER);
	        dashboardBP.setCenter(centerHB);	        
		    
//Buttons		
			Button dailyPlanButton = new Button("Daily plan");
			dailyPlanButton.setId("dailyPlanButton");
			
			dailyPlanButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  dailyPlanButton.setEffect(shadow);
			          }
			        });
			dailyPlanButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  dailyPlanButton.setEffect(null);
			          }
			        });
		 
			Button weeklPlanButton = new Button("Weekly plan");
			weeklPlanButton.setId("weeklyPlanButton");
			
			weeklPlanButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  weeklPlanButton.setEffect(shadow);
			          }
			        });
			weeklPlanButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  weeklPlanButton.setEffect(null);
			          }
			        });
			
			Button deactivCustButton = new Button("Activate");
			deactivCustButton.setId("deactivCustButton");
			
			deactivCustButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deactivCustButton.setEffect(shadow);
			          }
			        });
			deactivCustButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deactivCustButton.setEffect(null);
			          }
			        });
			
			Button deactivCarButton = new Button("Activate");
			deactivCarButton.setId("deactivCarButton");
			
			deactivCarButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deactivCarButton.setEffect(shadow);
			          }
			        });
			deactivCarButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  deactivCarButton.setEffect(null);
			          }
			        });
			
			Button addTypesButton = new Button("Add");
			addTypesButton.setId("addTypesButton");
			
			addTypesButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  addTypesButton.setEffect(shadow);
			          }
			        });
			addTypesButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  addTypesButton.setEffect(null);
			          }
			        });
			
			Button updateTypesButton = new Button("Update");
			updateTypesButton.setId("updateTypesButton");
			
			updateTypesButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateTypesButton.setEffect(shadow);
			          }
			        });
			updateTypesButton.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  updateTypesButton.setEffect(null);
			          }
			        });

//DailyPlan ON ACTION			
			dailyPlanButton.setOnAction(e -> {
				PdfGeneration pdfDaily = new PdfGeneration();
				pdfDaily.pdfGenerateDailyPlan(todaysResCO, todaysResCI, ldt);

				Alert alertReady = new Alert(AlertType.CONFIRMATION);
				alertReady.setHeaderText("Daily plan has been successfully created(in the 'DailyPlans' folder)!");
				alertReady.setTitle("Open this folder?");
				alertReady.setContentText("Would you want to open the created '" + ldt +  "_dailyPlanFMS.pdf' file?");
				Optional<ButtonType> result = alertReady.showAndWait();
				if (result.get() == ButtonType.OK) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File("DailyPlans/"));
					fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
					fileChooser.setTitle("Open daily plan PDF");
					fileChooser.showOpenDialog(mainStage);
				}
			});
			
//WeeklyPlan ON ACTION				
			weeklPlanButton.setOnAction(e -> {
				PdfGeneration pdfWeekly = new PdfGeneration();
				pdfWeekly.pdfGenerateWeeklyPlan(weeklyResCO, weeklyResCI, ldt);

				Alert alertReady = new Alert(AlertType.CONFIRMATION);
				alertReady.setHeaderText("Weekly plan has been successfully created(in the 'WeeklyPlans' folder)!");
				alertReady.setTitle("Open this folder?");
				alertReady.setContentText("Would you want to open the created '" + ldt + "-" 
											+ ldt.plusDays(6).getDayOfMonth() + "_weeklyPlanFMS.pdf' file?");
				Optional<ButtonType> result = alertReady.showAndWait();
				if (result.get() == ButtonType.OK) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File("WeeklyPlans/"));
					fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
					fileChooser.setTitle("Open weekly plan PDF");
					fileChooser.showOpenDialog(mainStage);
				}
			});

/*Activate CUSTOMER
 * It opens the customer list dialog, but with the deactivated customers. 
 */
			deactivCustButton.setOnAction(e -> {
				CustomerListDialog custList = new CustomerListDialog("", "deactive", observReservations);
				custList.showAndWait();
				if(selectedCust != null && 
						custIdLabel.getText().equals(selectedCust.getModellObject().getCustomerID())) {
					try {
						for(Customer c : Database.readCustomersTable("active", "")) {
							if(c.getCustomerID().equals(custIdLabel.getText())) {
								customerStatusLabel.setText("Cust.status: active");
								return;
							}
						}
						for(Customer c : Database.readCustomersTable("", "deactive")) {
							if(c.getCustomerID().equals(custIdLabel.getText())) {
								customerStatusLabel.setText("Cust.status: DEACTIVE");
							}
						}
					} catch (SQLException e1) {
						System.out.println("Something is wrong with the cust.status label database connection");
						e1.printStackTrace();
					}
				}
			});
			
//Activate CAR			
			deactivCarButton.setOnAction(e -> {
				CarListDialog carList = new CarListDialog(observDeactiveCars);
				Optional<CarFX> result = carList.showAndWait();
				if(result.isPresent()) {
					observDeactiveCars.remove(result.get());
					observCars.add(result.get());
					if(carLicensePlateLB.getText().equals(result.get().getModellObject().getCarLicensePlate())) {
						carStatusLabel.setText("Car status : active");
					}
				}
			});
			
/*Add Types ON ACTION
 * With this function it is possible to add new types, like category, insurance,...
 */
			addTypesButton.setOnAction(e -> {
				AddTypesDialog addTypes = new AddTypesDialog();
				addTypes.showAndWait();
			});
			
/*Update Types ON ACTION
 * It is possible to update the category, insurance, extras, with their prices too.
 */
			updateTypesButton.setOnAction(e -> {
				UpdateTypesDialog updateTypes = new UpdateTypesDialog(categoriesList(), insurancesList(), extrasList());
				updateTypes.showAndWait();
			});			
			
			
//CHOOSE DATE			
			Button chooseDateBT = new Button("Choose");
			chooseDateBT.setDisable(true);
			chooseDateBT.setId("chooseDateBT");
			
			
			chooseDateBT.addEventHandler(MouseEvent.MOUSE_ENTERED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  chooseDateBT.setEffect(shadow);
			          }
			        });
			chooseDateBT.addEventHandler(MouseEvent.MOUSE_EXITED,
			        new EventHandler<MouseEvent>() {
			          @Override
			          public void handle(MouseEvent e) {
			        	  chooseDateBT.setEffect(null);
			          }
			        });
			
			DatePicker dateP = new DatePicker();
			dateP.setPrefSize(160, 30);
			dateP.setValue(ldt);
			chooseDateBT.disableProperty().bind(dateP.valueProperty().isNull());
			dateP.setDayCellFactory(dp -> new DateCellFactory(LocalDate.MIN, LocalDate.MAX));
			
			HBox tobHB = new HBox(dateP, chooseDateBT);
			tobHB.setAlignment(Pos.CENTER);
			dashboardBP.setTop(tobHB);
			
/*Choose date ON ACTION
 * After the chosen date, the whole dashboard menu will be reloaded, starting the statistic with chosen date
 */
			chooseDateBT.setOnAction(e -> {
				dashboardTab.setContent(dashboardMenu(dateP.getValue()));
			});
			
		    HBox bottomHBox = new HBox();
			bottomHBox.setPadding(new Insets(10, 5, 0, 0));
			bottomHBox.setSpacing(10);
			bottomHBox.getChildren().addAll(updateTypesButton, addTypesButton, deactivCarButton, deactivCustButton, dailyPlanButton, weeklPlanButton);
			bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
			dashboardBP.setPadding(new Insets(15,0,0,0));
			dashboardBP.setBottom(bottomHBox);
		
		return dashboardBP;
	}
	
	
	
	
	
//METHODS for menu
//Initialize lists, reading data out from cars database	
	private void fillCarsObservableList(String active, String deactive) {
		if (active.equals("active") && deactive.equals("")) {
			observCars = FXCollections.observableArrayList();
			ArrayList<Car> cars = new ArrayList<>();
			try {
				cars = Database.readCarsTable(active, deactive);
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with cars database");
				e.printStackTrace();
			}
			for (Car c : cars) {
				observCars.add(new CarFX(c));
			}
			filteredListCars = new FilteredList<>(observCars, p -> true);
			sortedListCars = new SortedList<>(filteredListCars);
		}
		if (active.equals("") && deactive.equals("deactive")) {
			observDeactiveCars = FXCollections.observableArrayList();
			ArrayList<Car> cars = new ArrayList<>();
			try {
				cars = Database.readCarsTable(active, deactive);
			} catch (SQLException e) {
				System.out.println("Something is wrong with the filling observable list with deactive cars database");
				e.printStackTrace();
			}
			for (Car c : cars) {
				observDeactiveCars.add(new CarFX(c));
			}
		}
	}
	
	
//Initialize reservations list	
	private void fillReservationsObservableList() {
		observReservations = FXCollections.observableArrayList();
		ArrayList<Reservation> reservations = new ArrayList<>();
		try {
			reservations = Database.readReservationsTable("", "");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the filling observable list with reservations database");
			e.printStackTrace();
		}
		for (Reservation r : reservations) {
			observReservations.add(new ReservationFX(r));
		}
		filteredListReservations = new FilteredList<>(observReservations, p -> true);
		sortedListReservations = new SortedList<>(filteredListReservations);
	}
	
//FILL Types
//Categories	
	private ArrayList<String> categoriesList() {
		ArrayList<String> categoryNames = new ArrayList<>();
		try {
			for (String key : Database.readCarCategoriesTable().keySet()) {
				categoryNames.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of car categories table from database");
			e.printStackTrace();
		}
		Collections.sort(categoryNames);
		return categoryNames;
	}
	
	
//Get the price for a category	
	private int getCategoryPrice(String category) {
		int price = 0;
		try {
			for (String key : Database.readCarCategoriesTable().keySet()) {
				if (category.equals(key))
					price = Database.readCarCategoriesTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getCategoryPrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}
	

//FuelTypes	
	private ArrayList<String> fuelTypesList() {
		ArrayList<String> fuelTypes = new ArrayList<>();
		try {
			for (String s : Database.readCarFuelTypeTable()) {
				fuelTypes.add(s);
			}
		} catch (SQLException e1) {
			System.out.println("Fuel types database reading failed...");
			e1.printStackTrace();
		}
		Collections.sort(fuelTypes);
		return fuelTypes;
	}

	
//Transmission
	private ArrayList<String> transmissionsList() {
		ArrayList<String> transmissions = new ArrayList<>();
		try {
			for (String s : Database.readCarTransmissionTypeTable()) {
				transmissions.add(s);
			}
		} catch (SQLException e1) {
			System.out.println("Transmission database reading failed...");
			e1.printStackTrace();
		}
		Collections.sort(transmissions);
		return transmissions;
	}
	

//Car colors
	private ArrayList<String> carColorList() {
		ArrayList<String> colors = new ArrayList<>();
		try {
			for (String s : Database.readCarColorsTable()) {
				colors.add(s);
			}
		} catch (SQLException e1) {
			System.out.println("Car colors database reading failed...");
			e1.printStackTrace();
		}
		Collections.sort(colors);
		return colors;
	}
	
	
//Car features	
	private ArrayList<String> featuresList() {
		ArrayList<String> features = new ArrayList<>();
		featuresOriginal = new ArrayList<>();
		try {
			for (String s : Database.readFeaturesTable()) {
				features.add(s);
				featuresOriginal.add(s);
			}
		} catch (SQLException e1) {
			System.out.println("Car features database reading failed...");
			e1.printStackTrace();
		}
		Collections.sort(features);
		Collections.sort(featuresOriginal);
		return features;
	}


//Nationalities	
	private ArrayList<String> nationalitiesList() {
		ArrayList<String> nationalities = new ArrayList<>();
		try {
			for (String s : Database.readNationalitiesTable()) {
				nationalities.add(s);
			}
		} catch (SQLException e1) {
			System.out.println("Nationalities database reading failed...");
			e1.printStackTrace();
		}
		Collections.sort(nationalities);
		return nationalities;
	}
	
	
//Insurances	
	private ArrayList<String> insurancesList() {
		ArrayList<String> insuranceTypes = new ArrayList<>();
		try {
			for (String key : Database.readInsurancesTable().keySet()) {
				insuranceTypes.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of insurances table from database");
			e.printStackTrace();
		}
		Collections.sort(insuranceTypes);
		return insuranceTypes;
	}
	
	
//Get the price for an insurance	
	private int getInsurancePrice(String insurance) {
		int price = 0;
		try {
			for (String key : Database.readInsurancesTable().keySet()) {
				if (insurance.equals(key))
					price = Database.readInsurancesTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getInsurancePrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}
	
	
//Extras
	private ArrayList<String> extrasList() {
		ArrayList<String> extras = new ArrayList<>();
		extrasOriginal = new ArrayList<>();
		try {
			for (String key : Database.readExtrasTable().keySet()) {
				extras.add(key);
				extrasOriginal.add(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the reading of extras table from database");
			e.printStackTrace();
		}
		Collections.sort(extrasOriginal);
		Collections.sort(extras);
		return extras;
	}
	
	
//Get the price for an extra	
	private int getExtraPrice(String extraName) {
		int price = 0;
		try {
			for (String key : Database.readExtrasTable().keySet()) {
				if (extraName.equals(key))
					price = Database.readExtrasTable().get(key);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the getExtraPrice method's database connection");
			e.printStackTrace();
		}
		return price;
	}

	

	
/*Starting, initializing the whole database, when the table/database not existing, will be created	
 * with the basic data.
 */
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
