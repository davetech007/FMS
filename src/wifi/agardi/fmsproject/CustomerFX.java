package wifi.agardi.fmsproject;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerFX {
	private Customer modellObject;
	private StringProperty customerID;
	private StringProperty firstName;
	private StringProperty lastName;
	private ObjectProperty<LocalDate> dateOfBorn;
	private StringProperty nationality;
	private StringProperty passportNum;
	private StringProperty driversLicenseNum;
	private StringProperty telefon;
	private StringProperty eMail;
	private StringProperty addressLand;
	private StringProperty addressCity;
	private StringProperty addressStreet;
	private StringProperty addressPostalCode;
	
	
	public CustomerFX(Customer customer) {
		modellObject = customer;
		customerID = new SimpleStringProperty(customer.getCustomerID());
		firstName = new SimpleStringProperty(customer.getFirstName());
		lastName = new SimpleStringProperty(customer.getLastName());
		dateOfBorn = new SimpleObjectProperty<>(customer.getDateOfBorn());
		nationality = new SimpleStringProperty(customer.getNationality());
		passportNum = new SimpleStringProperty(customer.getPassportNum());
		driversLicenseNum = new SimpleStringProperty(customer.getDriversLicenseNum());
		telefon = new SimpleStringProperty(customer.getTelefon());
		eMail = new SimpleStringProperty(customer.geteMail());
		addressLand = new SimpleStringProperty(customer.getAddressLand());
		addressCity = new SimpleStringProperty(customer.getAddressCity());
		addressStreet = new SimpleStringProperty(customer.getAddressStreet());
		addressPostalCode = new SimpleStringProperty(customer.getAddressPostalCode());
	}
	
	public Customer getModellObject() {
		return modellObject;
	}
	
	
	
	public String getCustomerID() {
		return customerID.get();
	}
	public void setCustomerID(String v) {
		customerID.set(v);
		modellObject.setCustomerID(v);
	}
	public StringProperty customerIDProperty() {
		return customerID;
	}
	
	
	
	
	public String getFirstName() {
		return firstName.get();
	}
	public void setFirstName(String v) {
		firstName.set(v);
		modellObject.setFirstName(v);
	}
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	
	
	
	public String getLastName() {
		return lastName.get();
	}
	public void setLastName(String v) {
		lastName.set(v);
		modellObject.setLastName(v);
	}
	public StringProperty lastNameProperty() {
		return lastName;
	}
	
	
	
	
	public LocalDate getDateOfBorn() {
		return dateOfBorn.get();
	}
	public void setDateOfBorn(LocalDate v) {
		dateOfBorn.set(v);
		modellObject.setDateOfBorn(v);
	}
	public ObjectProperty<LocalDate> dateOfBornProperty() {
		return dateOfBorn;
	}
	
	
	
	
	public String getNationality() {
		return nationality.get();
	}
	public void setNationality(String v) {
		nationality.set(v);
		modellObject.setNationality(v);
	}
	public StringProperty nationalityProperty() {
		return nationality;
	}
	
	
	
	public String getPassportNum() {
		return passportNum.get();
	}
	public void setPassportNum(String v) {
		passportNum.set(v);
		modellObject.setPassportNum(v);
	}
	public StringProperty passportNumProperty() {
		return passportNum;
	}
	
	
	
	public String getDriversLicenseNum() {
		return driversLicenseNum.get();
	}
	public void setDriversLicenseNum(String v) {
		driversLicenseNum.set(v);
		modellObject.setDriversLicenseNum(v);
	}
	public StringProperty driversLicenseNumProperty() {
		return driversLicenseNum;
	}
	
	
	
	
	public String getTelefon() {
		return telefon.get();
	}
	public void setTelefon(String v) {
		telefon.set(v);
		modellObject.setTelefon(v);
	}
	public StringProperty telefonProperty() {
		return telefon;
	}
	
	
	
	public String getEMail() {
		return eMail.get();
	}
	public void setEMail(String v) {
		eMail.set(v);
		modellObject.seteMail(v);
	}
	public StringProperty eMailProperty() {
		return eMail;
	}
	
	
	
	
	public String getAddressLand() {
		return addressLand.get();
	}
	public void setAddressLand(String v) {
		addressLand.set(v);
		modellObject.setAddressLand(v);
	}
	public StringProperty addressLandProperty() {
		return addressLand;
	}
	
	
	
	
	public String getAddressCity() {
		return addressCity.get();
	}
	public void setAddressCity(String v) {
		addressCity.set(v);
		modellObject.setAddressCity(v);
	}
	public StringProperty addressCityProperty() {
		return addressCity;
	}
	
	
	
	public String getAddressStreet() {
		return addressStreet.get();
	}
	public void setAddressStreet(String v) {
		addressStreet.set(v);
		modellObject.setAddressStreet(v);
	}
	public StringProperty addressStreetProperty() {
		return addressStreet;
	}
	
	
	
	public String getAddressPostalCode() {
		return addressPostalCode.get();
	}
	public void setAddressPostalCode(String v) {
		addressPostalCode.set(v);
		modellObject.setAddressPostalCode(v);
	}
	public StringProperty addressPostalCodeProperty() {
		return addressPostalCode;
	}
	
	
	
	

}
