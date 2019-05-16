package wifi.agardi.fmsproject;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationFX {
	private Reservation modellObject;
	private StringProperty resNumberID;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty carLicensePlate;
	private StringProperty reservedCategory;
	private StringProperty insuranceType;
	private StringProperty pickupLocation;
	private ObjectProperty<LocalDateTime> pickupTime;
	private StringProperty returnLocation;
	private ObjectProperty<LocalDateTime> returnTime;
	private StringProperty resNotes;
	private ArrayList<String> resExtras;
	private BooleanProperty status;
	private StringProperty statusName;
	
	
	public ReservationFX(Reservation res) {
		modellObject = res;
		resNumberID = new SimpleStringProperty(res.getResNumberID());
		firstName = new SimpleStringProperty(res.getCustomer().getFirstName());
		lastName = new SimpleStringProperty(res.getCustomer().getLastName());
		carLicensePlate = new SimpleStringProperty(res.getCar().getCarLicensePlate());
		reservedCategory = new SimpleStringProperty(res.getReservedCategory());
		insuranceType = new SimpleStringProperty(res.getInsuranceType());
		pickupLocation = new SimpleStringProperty(res.getPickupLocation());
		pickupTime = new SimpleObjectProperty<>(res.getPickupTime());
		returnLocation = new SimpleStringProperty(res.getReturnLocation());
		returnTime = new SimpleObjectProperty<>(res.getReturnTime());
		resNotes = new SimpleStringProperty(res.getResNotes());
		status = new SimpleBooleanProperty(res.isStatus());
		statusName = new SimpleStringProperty(res.getStatusName());
	}
	
	
	public Reservation getModellObject() {
		return modellObject;
	}
	
	
	
	public String getResNumberID() {
		return resNumberID.get();
	}
	public void setResNumberID(String v) {
		resNumberID.set(v);
		modellObject.setResNumberID(v);
	}
	public StringProperty resNumberIDProperty() {
		return resNumberID;
	}
	
	
	
	public String getFirstName() {
		return firstName.get();
	}
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	
	
	public String getLastName() {
		return lastName.get();
	}
	public StringProperty lastNameProperty() {
		return lastName;
	}
	
	
	
	public String getCarLicensePlate() {
		return carLicensePlate.get();
	}
	public StringProperty carLicensePlateProperty() {
		return carLicensePlate;
	}
	
	
	
	public String getReservedCategory() {
		return reservedCategory.get();
	}
	public void setReservedCategory(String v) {
		reservedCategory.set(v);
		modellObject.setReservedCategory(v);
	}
	public StringProperty reservedCategoryProperty() {
		return reservedCategory;
	}
	
	
	
	public String getInsuranceType() {
		return insuranceType.get();
	}
	public void setInsuranceType(String v) {
		insuranceType.set(v);
		modellObject.setInsuranceType(v);
	}
	public StringProperty insuranceTypeProperty() {
		return insuranceType;
	}
	
	
	
	
	public String getPickupLocation() {
		return pickupLocation.get();
	}
	public void setPickupLocation(String v) {
		pickupLocation.set(v);
		modellObject.setPickupLocation(v);
	}
	public StringProperty pickupLocationProperty() {
		return pickupLocation;
	}
	

	
	public LocalDateTime getPickupTime() {
		return pickupTime.get();
	}
	public void setPickupTime(LocalDateTime v) {
		pickupTime.set(v);
		modellObject.setPickupTime(v);
	}
	public ObjectProperty<LocalDateTime> pickupTimeProperty() {
		return pickupTime;
	}
	

	
	
	public String getReturnLocation() {
		return returnLocation.get();
	}
	public void setReturnLocation(String v) {
		returnLocation.set(v);
		modellObject.setReturnLocation(v);
	}
	public StringProperty returnLocationProperty() {
		return returnLocation;
	}
	
	
	
	
	public LocalDateTime getReturnTime() {
		return returnTime.get();
	}
	public void setReturnTime(LocalDateTime v) {
		returnTime.set(v);
		modellObject.setReturnTime(v);
	}
	public ObjectProperty<LocalDateTime> returnTimeProperty() {
		return returnTime;
	}
	
	
	
	
	public String getResNotes() {
		return resNotes.get();
	}
	public void setResNotes(String v) {
		resNotes.set(v);
		modellObject.setResNotes(v);
	}
	public StringProperty resNotesProperty() {
		return resNotes;
	}
	
	
	
	public ArrayList<String> getResExtras() {
		return resExtras;
	}
	public void setResExtras(ArrayList<String> resExtras) {
		this.resExtras = resExtras;
	}

	
	
	public boolean isStatus() {
		return status.get();
	}
	public BooleanProperty isStatusProperty() {
		return status;
	}

	
	public String getStatusName() {
		return statusName.get();
	}
	public StringProperty statusNameProperty() {
		return statusName;
	}
	
	

}
