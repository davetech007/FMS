package wifi.agardi.fmsproject;

import java.time.LocalDate;

public class Customer {
	private String customerID;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBorn;
	private String nationality;
	private String passportNum;
	private String driversLicenseNum;
	private String telefon;
	private String eMail;
	private String addressLand;
	private String addressCity;
	private String addressStreet;
	private String addressPostalCode;
	
	
	public Customer() {
		super();
	}
	
	
	public Customer(String customerID, String firstName, String lastName, LocalDate dateOfBorn, String nationality,
			String passportNum, String driversLicenseNum, String telefon, String eMail, String addressLand,
			String addressCity, String addressStreet, String addressPostalCode) {
		super();
		this.customerID = customerID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBorn = dateOfBorn;
		this.nationality = nationality;
		this.passportNum = passportNum;
		this.driversLicenseNum = driversLicenseNum;
		this.telefon = telefon;
		this.eMail = eMail;
		this.addressLand = addressLand;
		this.addressCity = addressCity;
		this.addressStreet = addressStreet;
		this.addressPostalCode = addressPostalCode;
	}


	public String getCustomerID() {
		return customerID;
	}


	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public LocalDate getDateOfBorn() {
		return dateOfBorn;
	}


	public void setDateOfBorn(LocalDate dateOfBorn) {
		this.dateOfBorn = dateOfBorn;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String getPassportNum() {
		return passportNum;
	}


	public void setPassportNum(String passportNum) {
		this.passportNum = passportNum;
	}


	public String getDriversLicenseNum() {
		return driversLicenseNum;
	}


	public void setDriversLicenseNum(String driversLicenseNum) {
		this.driversLicenseNum = driversLicenseNum;
	}


	public String getTelefon() {
		return telefon;
	}


	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}


	public String geteMail() {
		return eMail;
	}


	public void seteMail(String eMail) {
		this.eMail = eMail;
	}


	public String getAddressLand() {
		return addressLand;
	}


	public void setAddressLand(String addressLand) {
		this.addressLand = addressLand;
	}


	public String getAddressCity() {
		return addressCity;
	}


	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}


	public String getAddressStreet() {
		return addressStreet;
	}


	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}


	public String getAddressPostalCode() {
		return addressPostalCode;
	}


	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	
}
