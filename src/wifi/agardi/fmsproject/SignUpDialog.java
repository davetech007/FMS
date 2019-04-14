package wifi.agardi.fmsproject;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SignUpDialog extends Dialog<ButtonType> {
	
	public SignUpDialog(String username, String password) {
		super();
		this.setTitle("Sign up");
		this.setHeaderText("Would you really want to sign up as " + username + "?");
		VBox vbox = new VBox(15);
		vbox.setPadding(new Insets(10));
		
		this.getDialogPane().setContent(vbox);
		ButtonType ok = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
	}

}
