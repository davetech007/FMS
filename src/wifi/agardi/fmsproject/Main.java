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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	TextField userNameTField;
	PasswordField passwordField;
	Label actionTarget;
	
	
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
//Login Page BORDER PANE TOP			
			Label nameLabel = new Label("Fleet Management System");
			nameLabel.setId("nameLabel");
			HBox nameHBox = new HBox(10);
			nameHBox.setAlignment(Pos.CENTER);
			nameHBox.getChildren().add(nameLabel);
			loginBP.setTop(nameHBox);			
//User name			
			Label userNameLabel = new Label("User Name");
			loginGP.add(userNameLabel, 0, 2, 1, 1);
			
			userNameTField = new TextField();
			userNameTField.setPrefSize(200, 30);
			loginGP.add(userNameTField, 0, 3, 1, 1);
//Password			
			Label passwordLabel = new Label("Password");
			loginGP.add(passwordLabel, 0, 4, 1, 1);
			
			passwordField = new PasswordField();
			loginGP.add(passwordField, 0, 5, 1, 1);
//Button Log in			
			Button logInButton = new Button("Login");
			HBox logInHBox = new HBox(10);
			logInHBox.setAlignment(Pos.BOTTOM_CENTER);
			logInHBox.getChildren().add(logInButton);
			loginGP.add(logInHBox, 0, 6);
//ActionTarget			
			actionTarget = new Label();
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
	
	
	
	

	
	public void openMainWindow() {
			Stage mainStage = new Stage();
			BorderPane mainBP = new BorderPane();
			mainBP.setPadding(new Insets(10,0,0,0));
//Main Title		
			Label mainTitle = new Label("Fleet Management System");
			mainTitle.setId("mainTitle");
			
			VBox mainTopVBox = new VBox();
			HBox mainTopHBox = new HBox();
			mainTopVBox.getChildren().add(mainTopHBox);
			mainTopHBox.getChildren().add(mainTitle);
			mainTopVBox.setAlignment(Pos.CENTER);
			mainTopHBox.setAlignment(Pos.CENTER);

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
			mainTabPaneHBox.setAlignment(Pos.CENTER);
			
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
					mainTitle.setText("Manage Cars");
				}
			});
			reservationsTab.setOnSelectionChanged(e -> {
				if(reservationsTab.isSelected()) {
					mainTitle.setText("Manage Reservations");
				}
			});
			dashboardTab.setOnSelectionChanged(e -> {
				if(dashboardTab.isSelected()) {
					mainTitle.setText("Dashboard");
				}
			});
		
		
		
		
		
		
		
			mainBP.getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
			Scene sceneMain = new Scene(mainBP, 1200, 700);
			mainStage.setScene(sceneMain);
			mainStage.show();
		
		
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
