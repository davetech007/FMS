package wifi.agardi.fmsproject;


import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarFX {
	private Car modellObject;
	private StringProperty carVinNumber;
	private StringProperty carLicensePlate;
	private StringProperty carBrand;
	private StringProperty carModel;
	private StringProperty carCategory;
	private StringProperty carColor;
	private StringProperty carFuelType;
	private StringProperty carTransmission;
	private ObjectProperty<LocalDate> carManufDate;
	private IntegerProperty carKM;
	private IntegerProperty carEngineSize;
	private IntegerProperty carEnginePower;
	private ArrayList<String> carFeatures = new ArrayList<>();
	private BooleanProperty isOnRent;
	
	
	public CarFX(Car car) {
		modellObject = car;
		carVinNumber = new SimpleStringProperty(car.getCarVinNumber());
		carLicensePlate = new SimpleStringProperty(car.getCarLicensePlate());
		carBrand = new SimpleStringProperty(car.getCarBrand());
		carModel = new SimpleStringProperty(car.getCarModel());
		carCategory = new SimpleStringProperty(car.getCarCategory());
		carColor = new SimpleStringProperty(car.getCarColor());
		carFuelType = new SimpleStringProperty(car.getCarFuelType());
		carTransmission = new SimpleStringProperty(car.getCarTransmission());
		carManufDate = new SimpleObjectProperty<>(car.getCarManufDate());
		carKM = new SimpleIntegerProperty(car.getCarKM());
		carEngineSize = new SimpleIntegerProperty(car.getCarEngineSize());
		carEnginePower = new SimpleIntegerProperty(car.getCarEnginePower());
		isOnRent = new SimpleBooleanProperty(car.isOnRent());
	}
	
	
	
	public Car getModellObject() {
		return modellObject;
	}
	
	
	
	
	public String getCarVinNumber() {
		return carVinNumber.get();
	}
	public void setCarVinNumber(String v) {
		carVinNumber.set(v);
		modellObject.setCarVinNumber(v);
	}
	public StringProperty carVinNumberProperty() {
		return carVinNumber;
	}
	
	
	
	
	public String getCarLicensePlate() {
		return carLicensePlate.get();
	}
	public void setCarLicensePlate(String v) {
		carLicensePlate.set(v);
		modellObject.setCarLicensePlate(v);;
	}
	public StringProperty carLicensePlateProperty() {
		return carLicensePlate;
	}
	
	
	
	
	public String getCarBrand() {
		return carBrand.get();
	}
	public void setCarBrand(String v) {
		carBrand.set(v);
		modellObject.setCarBrand(v);
	}
	public StringProperty carBrandProperty() {
		return carBrand;
	}
	
	
	
	
	public String getCarModel() {
		return carModel.get();
	}
	public void setCarModel(String v) {
		carModel.set(v);
		modellObject.setCarModel(v);
	}
	public StringProperty carModelProperty() {
		return carModel;
	}
	
	
	
	
	public String getCarCategory() {
		return carCategory.get();
	}
	public void setCarCategory(String v) {
		carCategory.set(v);
		modellObject.setCarCategory(v);
	}
	public StringProperty carCategoryProperty() {
		return carCategory;
	}
	
	
	
	
	
	public String getCarColor() {
		return carColor.get();
	}
	public void setCarColor(String v) {
		carColor.set(v);
		modellObject.setCarColor(v);
	}
	public StringProperty carColorProperty() {
		return carColor;
	}
	
	
	
	
	
	public String getCarFuelType() {
		return carFuelType.get();
	}
	public void setCarFuelType(String v) {
		carFuelType.set(v);
		modellObject.setCarFuelType(v);
	}
	public StringProperty carFuelTypeProperty() {
		return carFuelType;
	}
	
	
	
	
	
	public String getCarTransmission() {
		return carTransmission.get();
	}
	public void setCarTransmission(String v) {
		carTransmission.set(v);
		modellObject.setCarTransmission(v);
	}
	public StringProperty carTransmissionProperty() {
		return carTransmission;
	}
	
	
	

	public LocalDate getCarManufDate() {
		return carManufDate.get();
	}
	public void setCarManufDate(LocalDate v) {
		carManufDate.set(v);
		modellObject.setCarManufDate(v);
	}
	public ObjectProperty<LocalDate> carManufDateProperty() {
		return carManufDate;
	}
	
	
	
	
	public int getCarKM() {
		return carKM.get();
	}
	public void setCarKM(int v) {
		carKM.set(v);
		modellObject.setCarKM(v);
	}
	public IntegerProperty carKMProperty() {
		return carKM;
	}
	
	
	
	
	public int getCarEngineSize() {
		return carEngineSize.get();
	}
	public void setCarEngineSize(int v) {
		carEngineSize.set(v);
		modellObject.setCarEngineSize(v);
	}
	public IntegerProperty carEngineSizeProperty() {
		return carEngineSize;
	}
	
	
	

	public int getCarEnginePower() {
		return carEnginePower.get();
	}
	public void setCarEnginePower(int v) {
		carEnginePower.set(v);
		modellObject.setCarEnginePower(v);
	}
	public IntegerProperty carEnginePowerProperty() {
		return carEnginePower;
	}
	
	


	public ArrayList<String> getCarExtras() {
		return carFeatures;
	}
	public void setCarExtras(ArrayList<String> carFeatures) {
		this.carFeatures = carFeatures;
	}


	public boolean isOnRent() {
		return isOnRent.get();
	}
	public BooleanProperty isOnRentProperty() {
		return isOnRent;
	}
	
	

}
