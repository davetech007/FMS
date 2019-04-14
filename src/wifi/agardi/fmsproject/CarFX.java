package wifi.agardi.fmsproject;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarFX {
	private Car modellObject;
	private StringProperty carVinNumber;
	private StringProperty carLicensePlate;
	private StringProperty carBrand;
	private StringProperty carModel;
	private StringProperty carColor;
	private IntegerProperty carManufYear;
	private IntegerProperty carManufMonth;
	private IntegerProperty carKM;
	private IntegerProperty carEngineSize;
	private IntegerProperty carEnginePower;
	private DoubleProperty carPreisProDay;
	private BooleanProperty carIsOnRent;
	
	
	public CarFX(Car car) {
		modellObject = car;
		carVinNumber = new SimpleStringProperty(car.getCarVinNumber());
		carLicensePlate = new SimpleStringProperty(car.getCarLicensePlate());
		carBrand = new SimpleStringProperty(car.getCarBrand());
		carModel = new SimpleStringProperty(car.getCarModel());
		carColor = new SimpleStringProperty(car.getCarColor());
		carManufYear = new SimpleIntegerProperty(car.getCarManufYear());
		carManufMonth = new SimpleIntegerProperty(car.getCarManufMonth());;
		carKM = new SimpleIntegerProperty(car.getCarKM());
		carEngineSize = new SimpleIntegerProperty(car.getCarEngineSize());
		carEnginePower = new SimpleIntegerProperty(car.getCarEnginePower());
		carPreisProDay = new SimpleDoubleProperty(car.getCarPreisProDay());
		carIsOnRent = new SimpleBooleanProperty(car.isCarIsOnRent());
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
	
	
	
	
	public int getCarManufYear() {
		return carManufYear.get();
	}
	public void setCarManufYear(int v) {
		carManufYear.set(v);
		modellObject.setCarManufYear(v);
	}
	public IntegerProperty carManufYearProperty() {
		return carManufYear;
	}
	
	
	
	
	public int getCarManufMonth() {
		return carManufMonth.get();
	}
	public void setCarManufMonth(int v) {
		carManufMonth.set(v);
		modellObject.setCarManufMonth(v);
	}
	public IntegerProperty carManufMonthProperty() {
		return carManufMonth;
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
	
	
	
	
	public double getCarPreisProDay() {
		return carPreisProDay.get();
	}
	public void setCarPreisProDay(double v) {
		carPreisProDay.set(v);
		modellObject.setCarPreisProDay(v);
	}
	public DoubleProperty carPreisProDayProperty() {
		return carPreisProDay;
	}
	
	
	
	
	public boolean isCarIsOnRent() {
		return carIsOnRent.get();
	}
	public void setCarIsOnRent(boolean v) {
		carIsOnRent.set(v);
		modellObject.setCarIsOnRent(v);
	}
	public BooleanProperty carIsOnRentProperty() {
		return carIsOnRent;
	}
	
	

}
