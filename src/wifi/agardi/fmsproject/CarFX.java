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
	private ObjectProperty<Categorie> carCategorie;
	private ObjectProperty<FuelType> carFuelType;
	private ObjectProperty<LocalDate> carManufDate;
	private IntegerProperty carKM;
	private IntegerProperty carEngineSize;
	private IntegerProperty carEnginePower;
	private BooleanProperty carIsOnRent;
	private ArrayList<Extras> carExtras = new ArrayList<>();
	private ArrayList<String> carDamages = new ArrayList<>();
	
	
	public CarFX(Car car) {
		modellObject = car;
		carVinNumber = new SimpleStringProperty(car.getCarVinNumber());
		carLicensePlate = new SimpleStringProperty(car.getCarLicensePlate());
		carBrand = new SimpleStringProperty(car.getCarBrand());
		carModel = new SimpleStringProperty(car.getCarModel());
		carCategorie = new SimpleObjectProperty<>(car.getCarCategorie());
		carFuelType = new SimpleObjectProperty<>(car.getCarFuelType());
		carManufDate = new SimpleObjectProperty<>(car.getCarManufDate());
		carKM = new SimpleIntegerProperty(car.getCarKM());
		carEngineSize = new SimpleIntegerProperty(car.getCarEngineSize());
		carEnginePower = new SimpleIntegerProperty(car.getCarEnginePower());
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
	
	
	
	
	
	
	public Categorie getCarCategorie() {
		return carCategorie.get();
	}
	public void setCarCategorie(Categorie v) {
		carCategorie.set(v);
		modellObject.setCarCategorie(v);
	}
	public ObjectProperty<Categorie> carCategorieProperty() {
		return carCategorie;
	}
	
	
	
	
	
	public FuelType getCarFuelType() {
		return carFuelType.get();
	}
	public void setCarFuelType(FuelType v) {
		carFuelType.set(v);
		modellObject.setCarFuelType(v);
	}
	public ObjectProperty<FuelType> carFuelTypeProperty() {
		return carFuelType;
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


	
	

	public ArrayList<Extras> getCarExtras() {
		return carExtras;
	}
	public void setCarExtras(ArrayList<Extras> carExtras) {
		this.carExtras = carExtras;
	}


	
	
	

	public ArrayList<String> getCarDamages() {
		return carDamages;
	}
	public void setCarDamages(ArrayList<String> carDamages) {
		this.carDamages = carDamages;
	}
	
	

}
