package wifi.agardi.fmsproject;

import java.time.LocalDate;
import java.util.ArrayList;

public class Car {
	private String carVinNumber;
	private String carLicensePlate;
	private String carBrand;
	private String carModel;
	private String carCategory;
	private String carColor;
	private String carFuelType;
	private String carTransmission;
	private LocalDate carManufDate;
	private int carKM;
	private int carEngineSize;
	private int carEnginePower;
	private boolean carIsOnRent;
	private ArrayList<String> carExtras;
	private ArrayList<String> carDamages;
	
	
	public Car() {
		super();
	}


	


	public Car(String carVinNumber, String carLicensePlate, String carBrand, String carModel, String carCategory,
			String carColor, String carFuelType, String carTransmission, LocalDate carManufDate, int carKM,
			int carEngineSize, int carEnginePower) {
		super();
		this.carVinNumber = carVinNumber;
		this.carLicensePlate = carLicensePlate;
		this.carBrand = carBrand;
		this.carModel = carModel;
		this.carCategory = carCategory;
		this.carColor = carColor;
		this.carFuelType = carFuelType;
		this.carTransmission = carTransmission;
		this.carManufDate = carManufDate;
		this.carKM = carKM;
		this.carEngineSize = carEngineSize;
		this.carEnginePower = carEnginePower;
	}





	public String getCarVinNumber() {
		return carVinNumber;
	}


	public void setCarVinNumber(String carVinNumber) {
		this.carVinNumber = carVinNumber;
	}


	public String getCarLicensePlate() {
		return carLicensePlate;
	}


	public void setCarLicensePlate(String carLicensePlate) {
		this.carLicensePlate = carLicensePlate;
	}


	public String getCarBrand() {
		return carBrand;
	}


	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}


	public String getCarModel() {
		return carModel;
	}


	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}


	public String getCarCategory() {
		return carCategory;
	}


	public void setCarCategory(String carCategory) {
		this.carCategory = carCategory;
	}


	public String getCarColor() {
		return carColor;
	}


	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}


	public String getCarFuelType() {
		return carFuelType;
	}


	public void setCarFuelType(String carFuelType) {
		this.carFuelType = carFuelType;
	}


	public String getCarTransmission() {
		return carTransmission;
	}


	public void setCarTransmission(String carTransmission) {
		this.carTransmission = carTransmission;
	}


	public LocalDate getCarManufDate() {
		return carManufDate;
	}


	public void setCarManufDate(LocalDate carManufDate) {
		this.carManufDate = carManufDate;
	}


	public int getCarKM() {
		return carKM;
	}


	public void setCarKM(int carKM) {
		this.carKM = carKM;
	}


	public int getCarEngineSize() {
		return carEngineSize;
	}


	public void setCarEngineSize(int carEngineSize) {
		this.carEngineSize = carEngineSize;
	}


	public int getCarEnginePower() {
		return carEnginePower;
	}


	public void setCarEnginePower(int carEnginePower) {
		this.carEnginePower = carEnginePower;
	}
	


	public boolean isCarIsOnRent() {
		return carIsOnRent;
	}


	public void setCarIsOnRent(boolean carIsOnRent) {
		this.carIsOnRent = carIsOnRent;
	}


	public ArrayList<String> getCarExtras() {
		return carExtras;
	}


	public void setCarExtras(ArrayList<String> carExtras) {
		this.carExtras = carExtras;
	}


	public ArrayList<String> getCarDamages() {
		return carDamages;
	}


	public void setCarDamages(ArrayList<String> carDamages) {
		this.carDamages = carDamages;
	}

	
	
	

}
