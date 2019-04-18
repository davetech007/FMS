package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
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
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class Main extends Application {
	Image imageSearchIcon = new Image(Main.class.getResource("/Search.png").toExternalForm(), 18, 18, true, true);
	Image imageSaveIcon = new Image(Main.class.getResource("/SaveCustomer.png").toExternalForm(), 45, 45, true, true);
	Image imageReserveIcon = new Image(Main.class.getResource("/SaveRes.png").toExternalForm(), 30, 30, true, true);
	Image imageDeleteIcon = new Image(Main.class.getResource("/Delete.png").toExternalForm(), 30, 30, true, true);
	Image imageClearIcon = new Image(Main.class.getResource("/ClearPage.png").toExternalForm(), 30, 30, true, true);
	Image imageUpdateIcon = new Image(Main.class.getResource("/Update.png").toExternalForm(), 30, 30, true, true);
	Image imageAddCarIcon = new Image(Main.class.getResource("/AddCar.png").toExternalForm(), 30, 30, true, true);
	Image imageDamageIcon = new Image(Main.class.getResource("/Add.png").toExternalForm(), 20, 20, true, true);
	Image imageListDamagesIcon = new Image(Main.class.getResource("/ListDamages.png").toExternalForm(), 20, 20, true, true);
	
	
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
			userNameTField.setPrefSize(200, 30);
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
			signUpVBox.getChildren().add(signUpLabel);
			signUpVBox.getChildren().add(signUpButton);
			loginBP.setBottom(signUpVBox);
			
//SignUp on action			
			signUpButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						if((userNameTField.getText().length() > 2) && (passwordField.getText().length() > 2)) {
						SignUpDialog signUpDialog = new SignUpDialog(userNameTField.getText(), passwordField.getText());
						
						if(Database.checkUser(userNameTField.getText())) {
							actionTarget.setText("User already exists!");
							return;
						}
						
						Optional<ButtonType> result = signUpDialog.showAndWait();
						if(result.isPresent())
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
						e.printStackTrace();
					}
				}
				
			});
					
			Scene scene = new Scene(loginBP, 450, 450);
			primaryStage.setScene(scene);
			loginBP.getStylesheets().add(Main.class.getResource("/Login.css").toExternalForm());
			primaryStage.setTitle("Welcome");
			primaryStage.show();
	
	}
	
	

	
	
	

//After login, MAIN Window	
		
	public void mainMenu() {
			Stage mainStage = new Stage();
			HBox mainHB = new HBox();
			mainHB.setPadding(new Insets(20,0,10,0));
			mainHB.setAlignment(Pos.CENTER);
//Main Title		
			Label mainTitle = new Label("Fleet Management System");
			mainTitle.setId("mainTitle");
		
			VBox mainTopVBox = new VBox();
			HBox mainTopHBox = new HBox();
			mainTopVBox.getChildren().add(mainTopHBox);
			mainTopHBox.getChildren().add(mainTitle);
			mainTopVBox.setAlignment(Pos.TOP_CENTER);
			//mainTopVBox.setPrefHeight(120);
			mainTopHBox.setAlignment(Pos.TOP_CENTER);

			mainHB.getChildren().add(mainTopVBox);
//Tab Pane		
			Tab reserveTab = new Tab("Reserve");
			reserveTab.setContent(openReserveMenu());
		
			reserveTab.setClosable(false);
			Image imageReserve = new Image(Main.class.getResource("/Reserve.png").toExternalForm(), 30, 30, true, true);
			reserveTab.setGraphic(new ImageView(imageReserve));
			
			Tab carsTab = new Tab("Cars");
			carsTab.setContent(carsMenu());
		
		
			carsTab.setClosable(false);
			Image imageCars = new Image(Main.class.getResource("/Cars.png").toExternalForm(), 30, 30, true, true);
			carsTab.setGraphic(new ImageView(imageCars));
		
			Tab reservationsTab = new Tab("Reservaitons");
			reservationsTab.setClosable(false);
			Image imageRes = new Image(Main.class.getResource("/Reservations.png").toExternalForm(), 30, 30, true, true);
			reservationsTab.setGraphic(new ImageView(imageRes));
		
			Tab dashboardTab = new Tab("Dashboard");
			dashboardTab.setClosable(false);
			Image imageDashboard = new Image(Main.class.getResource("/Dashboard.png").toExternalForm(), 30, 30, true, true);
			dashboardTab.setGraphic(new ImageView(imageDashboard));
	
			TabPane mainTabPane = new TabPane();
		
			mainTabPane.getTabs().addAll(reserveTab, carsTab, reservationsTab, dashboardTab);
			HBox mainTabPaneHBox = new HBox();
			mainTabPaneHBox.getChildren().add(mainTabPane);
			//mainTabPaneHBox.setAlignment(Pos.TOP_CENTER);
		
//Main VBox Top second Child			
			mainTopVBox.getChildren().add(mainTabPaneHBox);
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
	
		
			mainHB.getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
			Scene sceneMain = new Scene(mainHB, 1100, 750);
			mainStage.setScene(sceneMain);
			mainStage.show();
    	
    }
	
	
	
	
	
	public BorderPane openReserveMenu() {
			BorderPane reserveBP = new BorderPane();
			reserveBP.setPadding(new Insets(0, 40, 0, 35));
			
			GridPane reserveGP = new GridPane();
			reserveGP.setAlignment(Pos.CENTER);
			reserveGP.setPadding(new Insets(50, 30, 0, 30));
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
			Button searchDriverButton = new Button("Search for a driver");
			searchDriverButton.setPrefWidth(195);
			searchDriverButton.setGraphic(new ImageView(imageSearchIcon));
			reserveGP.add(searchDriverButton, 0, 0);
	
			
		
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
			
			
			ComboBox<String> landComboBox = new ComboBox<>();
			landComboBox.getItems().addAll("American", "Hungarian", "Deutsch", "Chinese");
			landComboBox.setPromptText("Choose nationality");
			reserveGP.add(landComboBox, 0, 5);
			
			
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
//Notes
//TODO
			Label notesLabel = new Label("Notes");
			reserveGP.add(notesLabel, 0, 11);
			
			TextArea notesTA = new TextArea();
			notesTA.setPromptText("comments");
			notesTA.setPrefSize(195, 70);
			reserveGP.add(notesTA, 0, 12);
			
//Grid 1. column		
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
			streetTF.setPromptText("Street");
			reserveGP.add(streetTF, 1, 4);
			
			TextField housenrTF = new TextField();
			housenrTF.setPromptText("House nr./door");
			reserveGP.add(housenrTF, 1, 5);
			
					
			TextField postCodeTF = new TextField();
			postCodeTF.setPromptText("Postal code");
			reserveGP.add(postCodeTF, 1, 6);
			
			
			
//Grid 3. column
//Searching for a car	
					
			ComboBox<Categorie> carComboBox = new ComboBox<>(FXCollections.observableArrayList(Categorie.values()));
			carComboBox.setPrefWidth(195);
			carComboBox.setPromptText("Reserve only a category");
			reserveGP.add(carComboBox, 3, 0);
			
			
			
			Button searchCarButton = new Button("or search for a car");
			searchCarButton.setPrefWidth(195);
			searchCarButton.setGraphic(new ImageView(imageSearchIcon));
//TODO			
			searchCarButton.setOnAction(e ->{
				CarListeDialog carLD = new CarListeDialog(showCarsTableView());
				carLD.setResizable(true);
				carLD.showAndWait();
			});
			reserveGP.add(searchCarButton, 4, 0);
	
			
			Label carLabel = new Label("Car details");
			reserveGP.add(carLabel, 3, 1);
			
			TextField carLicensePlateTF = new TextField();
			carLicensePlateTF.setPromptText("Reserved car's license pl.");
			reserveGP.add(carLicensePlateTF, 3, 2);
			
			TextField carWishTF = new TextField();
			carWishTF.setPromptText("Type wish");
			reserveGP.add(carWishTF, 3, 3);
			
			
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
			TextField pickupHourTF = new TextField();
			Label pickupHourLB = new Label("hours");
			pickupHourTF.setMaxWidth(40);
			TextField pickupMinTF = new TextField();
			pickupMinTF.setMaxWidth(40);
			Label pickupMinuteLB = new Label("minutes");
			pickupHBox.getChildren().addAll(pickupHourTF,pickupHourLB, pickupMinTF, pickupMinuteLB);
			reserveGP.add(pickupHBox, 3, 7);

			
//Grid 4. column			
			Label dateLabel = new Label("Insurance");
			reserveGP.add(dateLabel, 4, 1);
//TODO			
			ToggleGroup tg = new ToggleGroup();
			RadioButton cascoRB = new RadioButton("Casco");
			cascoRB.setToggleGroup(tg);
			RadioButton fullCascoRB = new RadioButton("Full Casco");
			fullCascoRB.setToggleGroup(tg);
			reserveGP.add(cascoRB, 4, 2);
			reserveGP.add(fullCascoRB, 4, 3);
			
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
			//reserveGP.setGridLinesVisible(true);
	
//PRICE TODO
			VBox priceVBox = new VBox();
			Label priceDaysLabel = new Label("Total days   = ");
			Label priceLabel1 =    new Label("Price * days = ");
			Label priceLabel2 =    new Label("Extras	   = ");
			Label priceLabel3 =    new Label("Total price  = ");
			priceVBox.getChildren().addAll(priceDaysLabel, priceLabel1, priceLabel2, priceLabel3);
			reserveGP.add(priceVBox, 4, 12);
			
			
//Reserve BorderPane BOTTOM BUTTONS
			
			Button saveCustomerButton = new Button("Save");
			saveCustomerButton.setAlignment(Pos.BOTTOM_LEFT);
			saveCustomerButton.setPrefSize(110, 40);
			saveCustomerButton.setGraphic(new ImageView(imageSaveIcon));
			
			Button clearPageButton = new Button("Clear");
			clearPageButton.setPrefSize(110, 40);
			clearPageButton.setGraphic(new ImageView(imageClearIcon));
			
			Button updateResButton = new Button("Update");
			updateResButton.setPrefSize(110, 40);	
			updateResButton.setGraphic(new ImageView(imageUpdateIcon));
			
			Button reserveButton = new Button("Reserve");
			reserveButton.setPrefSize(110, 40);
			reserveButton.setGraphic(new ImageView(imageReserveIcon));
			
			
			HBox bottomHBoxCustomer = new HBox();
			HBox bottomHBoxRes = new HBox();
			HBox bottomHBox = new HBox();
			bottomHBoxCustomer.getChildren().add(saveCustomerButton);
			bottomHBoxRes.getChildren().addAll(clearPageButton, updateResButton, reserveButton);
			bottomHBox.setPadding(new Insets(60, 20, 10, 20));
			bottomHBoxRes.setSpacing(10);
			bottomHBox.setSpacing(40);
			bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
			bottomHBox.getChildren().addAll(bottomHBoxCustomer, bottomHBoxRes);
			
			reserveBP.setBottom(bottomHBox);

		return reserveBP;
		
	}
	
	
	
	
	
	
	public BorderPane carsMenu() {
		    BorderPane carsBP = new BorderPane();
		    carsBP.setPadding(new Insets(10, 40, 0, 30));
		    carsBP.setLeft(showCarsTableView());   

		    GridPane carsGP = new GridPane();
		    carsGP.setAlignment(Pos.TOP_CENTER);
		    carsGP.setPadding(new Insets(50, 20, 0, 10));
		    carsGP.setHgap(20);
		    carsGP.setVgap(10);
		    carsGP.setMinSize(0, 0);
		    carsBP.setRight(carsGP);
		    
//GridPane	
//Left side
		    Label vehicleLB = new Label("Vehicle");
		    vehicleLB.setPrefWidth(195);
		    carsGP.add(vehicleLB, 0, 0);
		    
		    TextField brandTF = new TextField();
		    brandTF.setPromptText("Brand");
		    carsGP.add(brandTF, 0, 1);
		    
		    TextField modellTF = new TextField();
		    modellTF.setPromptText("Modell");
		    carsGP.add(modellTF, 0, 2);
		    
		    DatePicker yearPicker = new DatePicker();
		    yearPicker.setPromptText("Manuf. date");
		    carsGP.add(yearPicker, 0, 3);
		    
		    TextField kmTF = new TextField();
		    kmTF.setPromptText("Actual KM");
		    carsGP.add(kmTF, 0, 4);
		    
		    Label detailsLB = new Label("Engine");
		    carsGP.add(detailsLB, 0, 5);
//Engine		    
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
		    carsGP.add(engineHB, 0, 6);
		    
		
//Right side		
		    ComboBox<Categorie> carCategorieBox = new ComboBox<>(FXCollections.observableArrayList(Categorie.values()));
		    carCategorieBox.setPrefWidth(195);
		    carCategorieBox.setPromptText("Category");
		    carsGP.add(carCategorieBox, 1, 1);
		    
		    TextField licPlateTF = new TextField();
		    licPlateTF.setPromptText("License plate");
		    carsGP.add(licPlateTF, 1, 2);
		    
		    TextField vinNumTF = new TextField();
		    vinNumTF.setPromptText("VIN number");
		    carsGP.add(vinNumTF, 1, 3);
		    
		    ComboBox<Color> carColorBox = new ComboBox<>(FXCollections.observableArrayList(Color.values()));
		    carColorBox.setPrefWidth(195);
		    carColorBox.setPromptText("Color");
		    carsGP.add(carColorBox, 1, 4);
		    
		    ComboBox<FuelType> carFuelBox = new ComboBox<>(FXCollections.observableArrayList(FuelType.values()));
		    carFuelBox.setPrefWidth(195);
		    carFuelBox.setPromptText("Fuel");
		    carsGP.add(carFuelBox, 1, 5);
		    
		    ComboBox<Transmission> carTransmBox = new ComboBox<>(FXCollections.observableArrayList(Transmission.values()));
		    carTransmBox.setPrefWidth(195);
		    carTransmBox.setPromptText("Transmission");
		    carsGP.add(carTransmBox, 1, 6);
		    
//Price		    
		    Label basePriceLB = new Label("Base price/day = ");
		    carsGP.add(basePriceLB, 1, 7);  
		    
		    
		    ListView<String> extrasLV = new ListView<>();
		    extrasLV.setPrefSize(195, 50);
		    carsGP.add(extrasLV, 1, 11);
		    
		    
//Damage buttons
		    Label damagesLB = new Label("Damages");
		    carsGP.add(damagesLB, 0, 7);
		    
		    TextField damagesTF = new TextField();
		    damagesTF.setPromptText("Type, location...");
		    carsGP.add(damagesTF, 0, 8);
		    
		    Button listDamageButton = new Button("List");
		    listDamageButton.setPrefSize(85, 30);
		    listDamageButton.setGraphic(new ImageView(imageListDamagesIcon));
			
		    Button addDamageButton = new Button("Add");
		    addDamageButton.setPrefSize(85, 30);
			addDamageButton.setGraphic(new ImageView(imageDamageIcon));
					
			HBox damageHBox = new HBox();
			damageHBox.setAlignment(Pos.BASELINE_LEFT);
			damageHBox.setSpacing(25);
		    damageHBox.getChildren().addAll(listDamageButton, addDamageButton);
		    carsGP.add(damageHBox, 0, 9);
			
			    
//Bottom Buttons
			Button deleteCarButton = new Button("Delete");
			deleteCarButton.setPrefSize(110, 40);
			deleteCarButton.setGraphic(new ImageView(imageDeleteIcon));
				
			Button updateCarButton = new Button("Update");
			updateCarButton.setPrefSize(110, 40);	
			updateCarButton.setGraphic(new ImageView(imageUpdateIcon));
	
			Button addCarButton = new Button("Add");
			addCarButton.setPrefSize(110, 40);
			addCarButton.setGraphic(new ImageView(imageAddCarIcon));
		    
		    HBox bottomHBox = new HBox();
			bottomHBox.setPadding(new Insets(30, 20, 10, 20));
			bottomHBox.setSpacing(10);
			bottomHBox.getChildren().addAll(deleteCarButton, updateCarButton, addCarButton);
			bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
			
		    carsBP.setBottom(bottomHBox);

		  
			
			
		
		return carsBP;
	}
	
	
	
	public VBox showCarsTableView() {
			VBox carsLeftVBox = new VBox();
//Searching		
				ComboBox<Categorie> carSearchBox = new ComboBox<>(FXCollections.observableArrayList(Categorie.values()));
				carSearchBox.setPrefWidth(195);
				carSearchBox.setPromptText("Choose category");
				
				Label carSearchLB = new Label("Search for a car -> "); 
				Button carSearchBT = new Button("Search");
				carSearchBT.setPrefWidth(130);
				carSearchBT.setGraphic(new ImageView(imageSearchIcon));
				HBox carSearchHB = new HBox();
				carSearchHB.setAlignment(Pos.CENTER);
				carSearchHB.setSpacing(15);
					
				carSearchHB.getChildren().addAll(carSearchLB, carSearchBox, carSearchBT);
					
				carsLeftVBox.getChildren().add(carSearchHB);
				carsLeftVBox.getChildren().add(carsTableView());
			
			return carsLeftVBox;	
	}
	
	
	
	public TableView<String> carsTableView() {
//TableView			
			TableColumn<String, String> categorieCol = new TableColumn<>("Category");
			categorieCol.setPrefWidth(80);
			categorieCol.setMinWidth(30);
		    categorieCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
			TableColumn<String, String> markeCol = new TableColumn<>("Brand");
			markeCol.setPrefWidth(110);
		    markeCol.setMinWidth(30);
		    markeCol.setCellValueFactory(new PropertyValueFactory<>("?"));
			
		    TableColumn<String, String> modellCol = new TableColumn<>("Modell");
		    modellCol.setPrefWidth(110);
		    modellCol.setMinWidth(30);
		    modellCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> licPlateCol = new TableColumn<>("License Plate");
		    licPlateCol.setPrefWidth(100);
		    licPlateCol.setMinWidth(30);
		    licPlateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> fuelTypeCol = new TableColumn<>("Fuel Type");
		    fuelTypeCol.setPrefWidth(90);
		    licPlateCol.setMinWidth(30);
		    licPlateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> onRentCol = new TableColumn<>("OnRent");
		    onRentCol.setPrefWidth(60);
		    onRentCol.setMinWidth(30);
		    onRentCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		   
		    TableView<String> carsTableView = new TableView<>();
			carsTableView.autosize();
			carsTableView.setPrefHeight(570);
			carsTableView.getColumns().addAll(categorieCol, markeCol, modellCol, licPlateCol,fuelTypeCol, onRentCol);
			carsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
			return carsTableView;
		
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		try {
			Database.createUsersTable();
			System.out.println("Created Users table, or already exists");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		launch(args);
	}
}
