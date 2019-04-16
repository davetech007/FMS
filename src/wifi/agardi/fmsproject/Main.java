package wifi.agardi.fmsproject;
	
import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	BorderPane mainBP;
	
	
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
						openMainWindow();
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
	
	public void openMainWindow() {
			Stage mainStage = new Stage();
			mainBP = new BorderPane();
			mainBP.setPadding(new Insets(10,0,10,0));
		
//Main Title		
			Label mainTitle = new Label("Fleet Management System");
			mainTitle.setId("mainTitle");
			
			VBox mainTopVBox = new VBox();
			HBox mainTopHBox = new HBox();
			mainTopVBox.getChildren().add(mainTopHBox);
			mainTopHBox.getChildren().add(mainTitle);
			mainTopVBox.setAlignment(Pos.TOP_CENTER);
			mainTopVBox.setPrefHeight(120);
			mainTopHBox.setAlignment(Pos.TOP_CENTER);

			mainBP.setTop(mainTopVBox);
//Tab Pane		
			Tab reserveTab = new Tab("Reserve");
			reserveTab.setClosable(false);
			Image imageReserve = new Image(Main.class.getResource("/Reserve.png").toExternalForm(), 30, 30, true, true);
			reserveTab.setGraphic(new ImageView(imageReserve));
			
			Tab carsTab = new Tab("Cars");
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
			mainTabPane.setPrefSize(1200, 150);
			
			
			mainTabPane.getTabs().addAll(reserveTab, carsTab, reservationsTab, dashboardTab);
			HBox mainTabPaneHBox = new HBox();
			mainTabPaneHBox.getChildren().add(mainTabPane);
			mainTabPaneHBox.setAlignment(Pos.TOP_CENTER);
			
//Main VBox Top second Child			
			mainTopVBox.getChildren().add(mainTabPaneHBox);
//Tab on Action		
			reserveTab.setOnSelectionChanged(e -> {
				if(reserveTab.isSelected()) {
					openReserveMenu();
					mainTitle.setText("Make a reservation");
				}
			});  
			carsTab.setOnSelectionChanged(e -> {
				if(carsTab.isSelected()) {
					openCarsMenu();
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
		
		
			
		
			openReserveMenu();
			mainBP.getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
			Scene sceneMain = new Scene(mainBP, 1200, 800);
			mainStage.setScene(sceneMain);
			mainStage.show();
		
		
	}
	
	
	
	
	
	
	
	
	public void openReserveMenu() {
			BorderPane reserveBP = new BorderPane();
			reserveBP.setPadding(new Insets(5, 30, 30, 30));
			
			GridPane reserveGP1 = new GridPane();
			reserveGP1.setAlignment(Pos.TOP_CENTER);
			reserveGP1.setPadding(new Insets(10, 10, 10, 60));
			reserveGP1.setHgap(15);
			reserveGP1.setVgap(10);
			reserveBP.setLeft(reserveGP1);
			
			GridPane reserveGP2 = new GridPane();
			reserveGP2.setAlignment(Pos.TOP_CENTER);
			reserveGP2.setPadding(new Insets(10, 60, 10, 10));
			reserveGP2.setHgap(15);
			reserveGP2.setVgap(10);
			reserveBP.setRight(reserveGP2);
			
//GRIDPANE LEFT				
//Searching for a driver
			
			ComboBox<String> driverComboBox = new ComboBox<>();
			driverComboBox.setPrefWidth(195);
			driverComboBox.getItems().addAll("First Name", "Last Name", "Email");
			driverComboBox.setValue("Searching criteria");
			reserveGP1.add(driverComboBox, 0, 0);
			
			TextField searchDriverTF = new TextField();
			searchDriverTF.setPromptText("search for a driver");
			reserveGP1.add(searchDriverTF, 0, 1);
			
			Button searchCostButton = new Button();
			Image imageSearch = new Image(Main.class.getResource("/Search.png").toExternalForm(), 18, 18, true, true);
			searchCostButton.setGraphic(new ImageView(imageSearch));
			reserveGP1.add(searchCostButton, 1, 0);	
			
			TextField custIdTF = new TextField();
			custIdTF.setPromptText("Customer ID");
			reserveGP1.add(custIdTF, 1, 1);
			

		
//Grid1 left side			
			Label driverLabel = new Label("Driver details");
			reserveGP1.add(driverLabel, 0, 2);
			
			TextField firstNameTF = new TextField();
			firstNameTF.setPromptText("First name");
			reserveGP1.add(firstNameTF, 0, 3);
			
			TextField lastNameTF = new TextField();
			lastNameTF.setPromptText("Last name");
			reserveGP1.add(lastNameTF, 0, 4);
			
			DatePicker dateBornPicker = new DatePicker();
			dateBornPicker.setPromptText("Date of Born");
			reserveGP1.add(dateBornPicker, 0, 5);
			
			
			ComboBox<String> landComboBox = new ComboBox<>();
			landComboBox.setPrefWidth(195);
			landComboBox.getItems().addAll("American", "Hungarian", "Deutsch", "Chinese");
			landComboBox.setPromptText("Nationality");
			reserveGP1.add(landComboBox, 0, 6);
			
			
			TextField passportTF = new TextField();
			passportTF.setPromptText("Passport nr.");
			reserveGP1.add(passportTF, 0, 7);
			
			TextField dLicenseTF = new TextField();
			dLicenseTF.setPromptText("Driver's license nr.");
			reserveGP1.add(dLicenseTF, 0, 8);
			
			Label contactLabel = new Label("Contact");
			reserveGP1.add(contactLabel, 0, 9);
			
			TextField telefonTF = new TextField();
			telefonTF.setPromptText("Telefon nr.");
			reserveGP1.add(telefonTF, 0, 10);
			
			TextField emailTF = new TextField();
			emailTF.setPromptText("E-mail");
			reserveGP1.add(emailTF, 0,11);
			
			
//Grid1 right side			
			Label addressLabel = new Label("Address");
			reserveGP1.add(addressLabel, 1, 2);
			
			TextField landTF = new TextField();
			landTF.setPrefWidth(195);
			landTF.setPromptText("Land");
			reserveGP1.add(landTF, 1, 3);
			
			TextField cityTF = new TextField();
			cityTF.setPrefWidth(195);
			cityTF.setPromptText("City");
			reserveGP1.add(cityTF, 1, 4);
			
			TextField streetTF = new TextField();
			streetTF.setPrefWidth(195);
			streetTF.setPromptText("Street");
			reserveGP1.add(streetTF, 1, 5);
			
			TextField housenrTF = new TextField();
			housenrTF.setPrefWidth(195);
			housenrTF.setPromptText("House nr./door");
			reserveGP1.add(housenrTF, 1, 6);
			
					
			TextField postCodeTF = new TextField();
			postCodeTF.setPrefWidth(195);
			postCodeTF.setPromptText("Postal code");
			reserveGP1.add(postCodeTF, 1, 7);
			
			Label notesLabel = new Label("Notes");
			reserveGP1.add(notesLabel, 1, 9);
			
			TextField notesTF = new TextField();
			notesTF.setPromptText("comments");
			reserveGP1.add(notesTF, 1, 10);
			
			Label resErrorLabel = new Label();
			reserveGP1.add(resErrorLabel, 0, 13);
			
			
			
//GRIDPANE RIGHT
//Searching for a car			
			ComboBox<String> carComboBox = new ComboBox<>();
			carComboBox.setPrefWidth(195);
			carComboBox.getItems().addAll("CatA", "CatB");
			carComboBox.setValue("Categorie");
			reserveGP2.add(carComboBox, 0, 0);
			
			TextField searchCarTF = new TextField();
			searchCarTF.setPromptText("search for a marke");
			reserveGP2.add(searchCarTF, 0, 1);
			
			Button searchCarButton = new Button();
			searchCarButton.setGraphic(new ImageView(imageSearch));
			reserveGP2.add(searchCarButton, 1, 0);	
			
			TextField carLicensePlateTF = new TextField();
			carLicensePlateTF.setPromptText("Car license plate");
			reserveGP2.add(carLicensePlateTF, 1, 1);
			

//Grid2 left side			
			Label carLabel = new Label("Car details");
			reserveGP2.add(carLabel, 0, 2);
			
			TextField carMarkeTF = new TextField();
			carMarkeTF.setPromptText("Marke");
			reserveGP2.add(carMarkeTF, 0, 3);
			
			TextField carModellTF = new TextField();
			carModellTF.setPromptText("Modell");
			reserveGP2.add(carModellTF, 0, 4);
			
			Label pickupLabel = new Label("Pick up");
			reserveGP2.add(pickupLabel, 0, 5);
			
			TextField pickupLocTF = new TextField();
			pickupLocTF.setPromptText("Pickup location");
			reserveGP2.add(pickupLocTF, 0, 6);
			
			DatePicker datePickupPicker = new DatePicker();
			datePickupPicker.setPromptText("Pickup date");
			reserveGP2.add(datePickupPicker, 0, 7);
			

			HBox pickupHBox = new HBox();
			pickupHBox.setSpacing(5);
			TextField pickupHourTF = new TextField();
			Label pickupHourLB = new Label("hours");
			pickupHourTF.setMaxWidth(40);
			TextField pickupMinTF = new TextField();
			pickupMinTF.setMaxWidth(40);
			Label pickupMinuteLB = new Label("minutes");
			pickupHBox.getChildren().addAll(pickupHourTF,pickupHourLB, pickupMinTF, pickupMinuteLB);
			reserveGP2.add(pickupHBox, 0, 8);
			
			
			
			
//Grid2 right side			
			Label dateLabel = new Label("Insurance");
			reserveGP2.add(dateLabel, 1, 2);
			
			ToggleGroup tg = new ToggleGroup();
			RadioButton cascoRB = new RadioButton("Casco");
			cascoRB.setToggleGroup(tg);
			RadioButton fullCascoRB = new RadioButton("Full Casco");
			fullCascoRB.setToggleGroup(tg);
			reserveGP2.add(cascoRB, 1, 3);
			reserveGP2.add(fullCascoRB, 1, 4);
			
			Label returnLabel = new Label("Return");
			reserveGP2.add(returnLabel, 1, 5);
			
			TextField returnLocTF = new TextField();
			returnLocTF.setPromptText("Return location");
			reserveGP2.add(returnLocTF, 1, 6);
			
			DatePicker dateReturnPicker = new DatePicker();
			dateReturnPicker.setPromptText("Return date");
			reserveGP2.add(dateReturnPicker, 1, 7);
			
			
			HBox returnHBox = new HBox();
			returnHBox.setSpacing(5);
			TextField returnHourTF = new TextField();
			returnHourTF.setMaxWidth(40);
			Label returnHourLB = new Label("hours");
			TextField returnMinTF = new TextField();
			returnMinTF.setMaxWidth(40);
			Label returnMinuteLB = new Label("minutes");
			returnHBox.getChildren().addAll(returnHourTF, returnHourLB, returnMinTF, returnMinuteLB);
			reserveGP2.add(returnHBox, 1, 8);
		
			
//Reserve BorderPane BOTTOM BUTTONS
			
			Button saveCustomerButton = new Button("Save");
			saveCustomerButton.setPrefSize(110, 40);
			Image imageSave = new Image(Main.class.getResource("/SaveCustomer.png").toExternalForm(), 45, 45, true, true);
			saveCustomerButton.setGraphic(new ImageView(imageSave));
			
			
			Button clearPageButton = new Button("Clear");
			clearPageButton.setPrefSize(110, 40);
			Image imageClear = new Image(Main.class.getResource("/ClearRes.png").toExternalForm(), 30, 30, true, true);
			clearPageButton.setGraphic(new ImageView(imageClear));
			
			
			Button reserveButton = new Button("Reserve");
			reserveButton.setPrefSize(110, 40);
			Image imageReserve = new Image(Main.class.getResource("/SaveRes.png").toExternalForm(), 30, 30, true, true);
			reserveButton.setGraphic(new ImageView(imageReserve));
			
			
			HBox bottomHBox = new HBox();
			bottomHBox.setPadding(new Insets(20, 20, 10, 20));
			bottomHBox.setSpacing(10);
			bottomHBox.getChildren().addAll(saveCustomerButton, clearPageButton, reserveButton);
			bottomHBox.setAlignment(Pos.CENTER_RIGHT);
			
			reserveBP.setId("reserveBP");
			
			mainBP.setCenter(reserveBP);
			mainBP.setBottom(bottomHBox);
			

		
		
	}
	
	
	
	
	
	
	public void openCarsMenu() {
			Label test = new Label("test");
			mainBP.setCenter(test);
		
		
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
