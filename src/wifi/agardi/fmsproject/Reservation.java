package wifi.agardi.fmsproject;

import java.time.LocalDate;
import java.util.ArrayList;

public class Reservation {
	private String resNumberID;
	private Customer customer;
	private Car car;
	private String reservedCategory;
	private String insuranceType;
	private String pickupLocation;
	private LocalDate pickupDate;
	private int pickupHour;
	private int pickupMin;
	private String returnLocation;
	private LocalDate returnDate;
	private int returnHour;
	private int returnMin;
	private String resNotes;
	private ArrayList<String> resExtras;
	
	public Reservation() {
		super();
	}

	public Reservation(String resNumberID, Customer customer, Car car, String reservedCategory,
			String insuranceType, String pickupLocation, LocalDate pickupDate, int pickupHour, int pickupMin,
			String returnLocation, LocalDate returnDate, int returnHour, int returnMin, String resNotes,
			ArrayList<String> resExtras) {
		super();
		this.resNumberID = resNumberID;
		this.customer = customer;
		this.car = car;
		this.reservedCategory = reservedCategory;
		this.insuranceType = insuranceType;
		this.pickupLocation = pickupLocation;
		this.pickupDate = pickupDate;
		this.pickupHour = pickupHour;
		this.pickupMin = pickupMin;
		this.returnLocation = returnLocation;
		this.returnDate = returnDate;
		this.returnHour = returnHour;
		this.returnMin = returnMin;
		this.resNotes = resNotes;
		this.resExtras = resExtras;
	}
	
	public Reservation(String resNumberID, Customer customer, String reservedCategory,
			String insuranceType, String pickupLocation, LocalDate pickupDate, int pickupHour, int pickupMin,
			String returnLocation, LocalDate returnDate, int returnHour, int returnMin, String resNotes,
			ArrayList<String> resExtras) {
		super();
		this.resNumberID = resNumberID;
		this.customer = customer;
		this.reservedCategory = reservedCategory;
		this.insuranceType = insuranceType;
		this.pickupLocation = pickupLocation;
		this.pickupDate = pickupDate;
		this.pickupHour = pickupHour;
		this.pickupMin = pickupMin;
		this.returnLocation = returnLocation;
		this.returnDate = returnDate;
		this.returnHour = returnHour;
		this.returnMin = returnMin;
		this.resNotes = resNotes;
		this.resExtras = resExtras;
	}
	
	

	public String getResNumberID() {
		return resNumberID;
	}

	public void setResNumberID(String resNumberID) {
		this.resNumberID = resNumberID;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getReservedCategory() {
		return reservedCategory;
	}

	public void setReservedCategory(String reservedCategory) {
		this.reservedCategory = reservedCategory;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public LocalDate getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(LocalDate pickupDate) {
		this.pickupDate = pickupDate;
	}

	public int getPickupHour() {
		return pickupHour;
	}

	public void setPickupHour(int pickupHour) {
		this.pickupHour = pickupHour;
	}

	public int getPickupMin() {
		return pickupMin;
	}

	public void setPickupMin(int pickupMin) {
		this.pickupMin = pickupMin;
	}

	public String getReturnLocation() {
		return returnLocation;
	}

	public void setReturnLocation(String returnLocation) {
		this.returnLocation = returnLocation;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public int getReturnHour() {
		return returnHour;
	}

	public void setReturnHour(int returnHour) {
		this.returnHour = returnHour;
	}

	public int getReturnMin() {
		return returnMin;
	}

	public void setReturnMin(int returnMin) {
		this.returnMin = returnMin;
	}

	public String getResNotes() {
		return resNotes;
	}

	public void setResNotes(String resNotes) {
		this.resNotes = resNotes;
	}

	public ArrayList<String> getResExtras() {
		return resExtras;
	}

	public void setResExtras(ArrayList<String> resExtras) {
		this.resExtras = resExtras;
	}
	


}
