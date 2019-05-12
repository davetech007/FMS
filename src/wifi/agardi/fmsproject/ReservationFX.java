package wifi.agardi.fmsproject;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationFX {
	private Reservation modellObject;
	private StringProperty resNumberID;
	private StringProperty firstName;
	private StringProperty carLicensePlate;
	private StringProperty reservedCategory;
	private StringProperty insuranceType;
	private StringProperty pickupLocation;
	private ObjectProperty<LocalDate> pickupDate;
	private IntegerProperty pickupHour;
	private IntegerProperty pickupMin;
	private StringProperty returnLocation;
	private ObjectProperty<LocalDate> returnDate;
	private IntegerProperty returnHour;
	private IntegerProperty returnMin;
	private StringProperty resNotes;
	private ArrayList<String> resExtras;
	
	
	public ReservationFX(Reservation res) {
		modellObject = res;
		resNumberID = new SimpleStringProperty(res.getResNumberID());
		firstName = new SimpleStringProperty(res.getCustomer().getFirstName());
		carLicensePlate = new SimpleStringProperty(res.getCar().getCarLicensePlate());
		reservedCategory = new SimpleStringProperty(res.getReservedCategory());
		insuranceType = new SimpleStringProperty(res.getInsuranceType());
		pickupLocation = new SimpleStringProperty(res.getPickupLocation());
		pickupDate = new SimpleObjectProperty<>(res.getPickupDate());
		pickupHour = new SimpleIntegerProperty(res.getPickupHour());
		pickupMin = new SimpleIntegerProperty(res.getPickupMin());
		returnLocation = new SimpleStringProperty(res.getReturnLocation());
		returnDate = new SimpleObjectProperty<>(res.getReturnDate());
		returnHour = new SimpleIntegerProperty(res.getReturnHour());
		returnMin = new SimpleIntegerProperty(res.getReturnMin());
		resNotes = new SimpleStringProperty(res.getResNotes());
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
	
	
	
	public LocalDate getPickupDate() {
		return pickupDate.get();
	}
	public void setPickupDate(LocalDate v) {
		pickupDate.set(v);
		modellObject.setPickupDate(v);
	}
	public ObjectProperty<LocalDate> pickupDateProperty() {
		return pickupDate;
	}
	
	
	
	public int getPickupHour() {
		return pickupHour.get();
	}
	public void setPickupHour(int v) {
		pickupHour.set(v);
		modellObject.setPickupHour(v);
	}
	public IntegerProperty pickupHourProperty() {
		return pickupHour;
	}
	
	
	
	public int getPickupMin() {
		return pickupMin.get();
	}
	public void setPickupMin(int v) {
		pickupMin.set(v);
		modellObject.setPickupMin(v);
	}
	public IntegerProperty pickupMinProperty() {
		return pickupMin;
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
	
	
	
	public LocalDate getReturnDate() {
		return returnDate.get();
	}
	public void setReturnDate(LocalDate v) {
		returnDate.set(v);
		modellObject.setReturnDate(v);
	}
	public ObjectProperty<LocalDate> returnDateProperty() {
		return returnDate;
	}
	
	
	
	public int getReturnHour() {
		return returnHour.get();
	}
	public void setReturnHour(int v) {
		returnHour.set(v);
		modellObject.setReturnHour(v);
	}
	public IntegerProperty returnHourProperty() {
		return returnHour;
	}
	
	
	
	public int getReturnMin() {
		return returnMin.get();
	}
	public void setReturnMin(int v) {
		returnMin.set(v);
		modellObject.setReturnMin(v);
	}
	public IntegerProperty returnMinProperty() {
		return returnMin;
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
	
	

}
