package wifi.agardi.fmsproject;

import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Application;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
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
	Image imageSearch = new Image(Main.class.getResource("/Search.png").toExternalForm(), 18, 18, true, true);
	
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
			reserveBP.setPadding(new Insets(0, 35, 0, 35));
			
			GridPane reserveGP1 = new GridPane();
			reserveGP1.setAlignment(Pos.CENTER);
			reserveGP1.setPadding(new Insets(50, 20, 10, 20));
			reserveGP1.setHgap(15);
			reserveGP1.setVgap(10);
			reserveBP.setCenter(reserveGP1);
			
			ColumnConstraints columnSpace = new ColumnConstraints();
			columnSpace.setHgrow(Priority.ALWAYS);				
			ColumnConstraints col1 = new ColumnConstraints();
			reserveGP1.getColumnConstraints().addAll(col1, col1, columnSpace);
			reserveGP1.setMinSize(0, 0);
		
//GRIDPANE			
//Searching for a driver	
//TODO
			Button searchCostButton = new Button("Search for a driver");
			searchCostButton.setPrefWidth(195);
			searchCostButton.setGraphic(new ImageView(imageSearch));
			reserveGP1.add(searchCostButton, 0, 0);
	
			
		
//Grid 0. column			
			Label driverLabel = new Label("Driver details");
			reserveGP1.add(driverLabel, 0, 1);
			
			TextField firstNameTF = new TextField();
			firstNameTF.setPromptText("First name");
			reserveGP1.add(firstNameTF, 0, 2);
			
			TextField lastNameTF = new TextField();
			lastNameTF.setPromptText("Last name");
			reserveGP1.add(lastNameTF, 0, 3);
			
			DatePicker dateBornPicker = new DatePicker();
			dateBornPicker.setPromptText("Date of Born");
			reserveGP1.add(dateBornPicker, 0, 4);
			
			
			ComboBox<String> landComboBox = new ComboBox<>();
			landComboBox.getItems().addAll("American", "Hungarian", "Deutsch", "Chinese");
			landComboBox.setPromptText("Choose nationality");
			reserveGP1.add(landComboBox, 0, 5);
			
			
			TextField passportTF = new TextField();
			passportTF.setPromptText("Passport nr.");
			reserveGP1.add(passportTF, 0, 6);
			
			TextField dLicenseTF = new TextField();
			dLicenseTF.setPromptText("Driver's license nr.");
			reserveGP1.add(dLicenseTF, 0, 7);
			
			Label contactLabel = new Label("Contact");		
			reserveGP1.add(contactLabel, 0, 8);
			
			TextField telefonTF = new TextField();
			telefonTF.setPromptText("Telefon nr.");
			reserveGP1.add(telefonTF, 0, 9);
			
			TextField emailTF = new TextField();
			emailTF.setPromptText("E-mail");
			reserveGP1.add(emailTF, 0, 10);
//Notes
//TODO
			Label notesLabel = new Label("Notes");
			reserveGP1.add(notesLabel, 0, 11);
			
			TextArea notesTA = new TextArea();
			notesTA.setPromptText("comments");
			notesTA.setPrefSize(195, 70);
			reserveGP1.add(notesTA, 0, 12);

//			Label resErrorLabel = new Label();
//			reserveGP1.add(resErrorLabel, 0, 13);

			
//Grid 1. column		
			Label addressLabel = new Label("Address");
			reserveGP1.add(addressLabel, 1, 1);
			
			TextField landTF = new TextField();
			landTF.setPrefWidth(195);
			landTF.setPromptText("Land");
			reserveGP1.add(landTF, 1, 2);
			
			TextField cityTF = new TextField();
			cityTF.setPromptText("City");
			reserveGP1.add(cityTF, 1, 3);
			
			TextField streetTF = new TextField();
			streetTF.setPromptText("Street");
			reserveGP1.add(streetTF, 1, 4);
			
			TextField housenrTF = new TextField();
			housenrTF.setPromptText("House nr./door");
			reserveGP1.add(housenrTF, 1, 5);
			
					
			TextField postCodeTF = new TextField();
			postCodeTF.setPromptText("Postal code");
			reserveGP1.add(postCodeTF, 1, 6);
			
		

			
			
//Grid 3. column
//Searching for a car	
			ComboBox<String> carComboBox = new ComboBox<>();
			carComboBox.setPrefWidth(195);
			carComboBox.getItems().addAll("CatA", "CatB");
			carComboBox.setValue("Choose category");
			reserveGP1.add(carComboBox, 3, 0);
			
			Button searchCarButton = new Button("Search for a car");
			searchCarButton.setPrefWidth(195);
			searchCarButton.setGraphic(new ImageView(imageSearch));
//TODO			
			searchCarButton.setOnAction(e ->{
				CarListeDialog carLD = new CarListeDialog(showCarsTableView());
				carLD.showAndWait();
			});
			reserveGP1.add(searchCarButton, 4, 0);
	
			
			Label carLabel = new Label("Car details");
			reserveGP1.add(carLabel, 3, 1);
			
			TextField carLicensePlateTF = new TextField();
			carLicensePlateTF.setPromptText("Reserved car's license pl.");
			reserveGP1.add(carLicensePlateTF, 3, 2);
			
			TextField carWishTF = new TextField();
			carWishTF.setPromptText("Type wish");
			reserveGP1.add(carWishTF, 3, 3);
			
			
			Label pickupLabel = new Label("Pick up");
			reserveGP1.add(pickupLabel, 3, 4);
			
			TextField pickupLocTF = new TextField();
			pickupLocTF.setPromptText("Pickup location");
			reserveGP1.add(pickupLocTF, 3, 5);
			
			DatePicker datePickupPicker = new DatePicker();
			datePickupPicker.setPromptText("Pickup date");
			reserveGP1.add(datePickupPicker, 3, 6);
			

			HBox pickupHBox = new HBox();
			pickupHBox.setSpacing(5);
			TextField pickupHourTF = new TextField();
			Label pickupHourLB = new Label("hours");
			pickupHourTF.setMaxWidth(40);
			TextField pickupMinTF = new TextField();
			pickupMinTF.setMaxWidth(40);
			Label pickupMinuteLB = new Label("minutes");
			pickupHBox.getChildren().addAll(pickupHourTF,pickupHourLB, pickupMinTF, pickupMinuteLB);
			reserveGP1.add(pickupHBox, 3, 7);

			
//Grid 4. column			
			Label dateLabel = new Label("Insurance");
			reserveGP1.add(dateLabel, 4, 1);
			
			ToggleGroup tg = new ToggleGroup();
			RadioButton cascoRB = new RadioButton("Casco");
			cascoRB.setToggleGroup(tg);
			RadioButton fullCascoRB = new RadioButton("Full Casco");
			fullCascoRB.setToggleGroup(tg);
			reserveGP1.add(cascoRB, 4, 2);
			reserveGP1.add(fullCascoRB, 4, 3);
			
			Label returnLabel = new Label("Return");
			reserveGP1.add(returnLabel, 4, 4);
			
			TextField returnLocTF = new TextField();
			returnLocTF.setPromptText("Return location");
			reserveGP1.add(returnLocTF, 4, 5);
			
			DatePicker dateReturnPicker = new DatePicker();
			dateReturnPicker.setPromptText("Return date");
			reserveGP1.add(dateReturnPicker, 4, 6);
			
			
			HBox returnHBox = new HBox();
			returnHBox.setSpacing(5);
			TextField returnHourTF = new TextField();
			returnHourTF.setMaxWidth(40);
			Label returnHourLB = new Label("hours");
			TextField returnMinTF = new TextField();
			returnMinTF.setMaxWidth(40);
			Label returnMinuteLB = new Label("minutes");
			returnHBox.getChildren().addAll(returnHourTF, returnHourLB, returnMinTF, returnMinuteLB);
			reserveGP1.add(returnHBox, 4, 7);
			
//EXTRAS		
			Label extrasLB = new Label("Extras");
			reserveGP1.add(extrasLB, 3, 8);
			
			CheckBox cb1 = new CheckBox("GPS");
			CheckBox cb2 = new CheckBox("Additional driver");
			CheckBox cb3 = new CheckBox("Child seat");
			CheckBox cb4 = new CheckBox("Border crossing");
			reserveGP1.add(cb1, 3, 9);
			reserveGP1.add(cb2, 3, 10);
			reserveGP1.add(cb3, 4, 9);
			reserveGP1.add(cb4, 4, 10);
			//reserveGP1.setGridLinesVisible(true);
	
//PRICE TODO
			VBox priceVBox = new VBox();
			Label priceLabel1 = new Label("Base price * days =");
			Label priceLabel2 = new Label("Extras =");
			Label priceLabel3 = new Label("Expected total price =");
			priceVBox.getChildren().addAll(priceLabel1, priceLabel2, priceLabel3);
			reserveGP1.add(priceVBox, 4, 12);
			
			
//Reserve BorderPane BOTTOM BUTTONS
			
			Button saveCustomerButton = new Button("Save");
			saveCustomerButton.setPrefSize(110, 40);
			Image imageSave = new Image(Main.class.getResource("/SaveCustomer.png").toExternalForm(), 45, 45, true, true);
			saveCustomerButton.setGraphic(new ImageView(imageSave));
			saveCustomerButton.setAlignment(Pos.BOTTOM_RIGHT);
			
			Button clearPageButton = new Button("Clear");
			clearPageButton.setPrefSize(110, 40);
			Image imageClear = new Image(Main.class.getResource("/ClearRes.png").toExternalForm(), 30, 30, true, true);
			clearPageButton.setGraphic(new ImageView(imageClear));
			
			
			Button reserveButton = new Button("Reserve");
			reserveButton.setPrefSize(110, 40);
			Image imageReserve = new Image(Main.class.getResource("/SaveRes.png").toExternalForm(), 30, 30, true, true);
			reserveButton.setGraphic(new ImageView(imageReserve));
			
			
			HBox bottomHBox = new HBox();
			bottomHBox.setPadding(new Insets(60, 20, 10, 20));
			bottomHBox.setSpacing(10);
			bottomHBox.getChildren().addAll(saveCustomerButton, clearPageButton, reserveButton);
			bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
			
			reserveBP.setBottom(bottomHBox);

		return reserveBP;
		
	}
	
	
	
	
	
	
	public BorderPane carsMenu() {
		    BorderPane carsBP = new BorderPane();
		    carsBP.setPadding(new Insets(10, 20, 0, 20));
		    carsBP.setLeft(showCarsTableView());
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		  
			
			
		
		return carsBP;
	}
	
	
	
	public VBox showCarsTableView() {
		VBox carsLeftVBox = new VBox();
//Searching		
				ComboBox<String> carSearchBox = new ComboBox<>();
				carSearchBox.setPrefWidth(195);
				carSearchBox.getItems().addAll("All", "Category", "Fuel");
				carSearchBox.setValue("Searching criteria");
					
				Button carSearchButton = new Button("Search for a car");
				carSearchButton.setPrefWidth(195);
				carSearchButton.setGraphic(new ImageView(imageSearch));
				HBox carSearchHB = new HBox();
				carSearchHB.setAlignment(Pos.CENTER);
				carSearchHB.setSpacing(10);
					
				carSearchHB.getChildren().addAll(carSearchBox, carSearchButton);
					
				carsLeftVBox.getChildren().add(carSearchHB);
				carsLeftVBox.getChildren().add(carsTableView());
					
			return carsLeftVBox;	
	}
	
	
	
	
	public TableView<String> carsTableView() {
//TableView			
			TableColumn<String, String> categorieCol = new TableColumn<>("Category");
			categorieCol.setMinWidth(60);
		    categorieCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		
			TableColumn<String, String> markeCol = new TableColumn<>("Marke");
		    markeCol.setMinWidth(100);
		    markeCol.setCellValueFactory(new PropertyValueFactory<>("?"));
			
		    TableColumn<String, String> modellCol = new TableColumn<>("Modell");
		    modellCol.setMinWidth(100);
		    modellCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> licPlateCol = new TableColumn<>("License Plate");
		    licPlateCol.setMinWidth(100);
		    licPlateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> fuelTypeCol = new TableColumn<>("License Plate");
		    licPlateCol.setMinWidth(100);
		    licPlateCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		    TableColumn<String, String> onRentCol = new TableColumn<>("On Rent");
		    onRentCol.setMinWidth(100);
		    onRentCol.setCellValueFactory(new PropertyValueFactory<>("?"));
		    
		   
			TableView<String> carsTableView = new TableView<>();
			carsTableView.setPrefHeight(550);
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
