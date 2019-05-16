package wifi.agardi.fmsproject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reservation {
	private String resNumberID;
	private Customer customer;
	private Car car;
	private String reservedCategory;
	private String insuranceType;
	private String pickupLocation;
	private LocalDateTime pickupTime;
	private String returnLocation;
	private LocalDateTime returnTime;
	private String resNotes;
	private ArrayList<String> resExtras;
	private boolean status;
	private String statusName;
	
	public Reservation() {
		super();
	}

	public Reservation(String resNumberID, Customer customer, Car car, String reservedCategory,
			String insuranceType, String pickupLocation, LocalDateTime pickupTime,
			String returnLocation, LocalDateTime returnTime, String resNotes,
			ArrayList<String> resExtras, boolean status) {
		super();
		this.resNumberID = resNumberID;
		this.customer = customer;
		this.car = car;
		this.reservedCategory = reservedCategory;
		this.insuranceType = insuranceType;
		this.pickupLocation = pickupLocation;
		this.pickupTime = pickupTime;
		this.returnLocation = returnLocation;
		this.returnTime = returnTime;
		this.resNotes = resNotes;
		this.resExtras = resExtras;
		this.status = status;
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

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}




	public String getReturnLocation() {
		return returnLocation;
	}

	public void setReturnLocation(String returnLocation) {
		this.returnLocation = returnLocation;
	}

	public LocalDateTime getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(LocalDateTime returnTime) {
		this.returnTime = returnTime;
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
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	

	public String getStatusName() {
		if(getReturnTime().isBefore(LocalDateTime.now())) {
			return "Expired";
		}
		if(getCar().isCarIsOnRent() == true) {
			return "On rent";
		}
		if(isStatus() == false) {
			return "Active";
		}
		if(isStatus() == true){
			return "Cancelled";
		}
		return "";
	}
	

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	

	
	

}
