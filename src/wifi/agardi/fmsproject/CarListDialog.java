package wifi.agardi.fmsproject;
import java.sql.SQLException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CarListDialog extends Dialog<CarFX>{
	ObservableList<CarFX> cars;
	CarFX selectedCar;
	TableView<CarFX> carsTableView;
	
	public CarListDialog(ObservableList<CarFX> cars) {
		super();
		this.cars = cars;
		
		this.setTitle("Cars list");
		this.setHeaderText("Activate car");
		
		VBox vbox = new VBox(carsTableView());
		
		carsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if(newV != null) {
				selectedCar = carsTableView.getSelectionModel().getSelectedItem();
			}
		});
		
		this.getDialogPane().setContent(vbox);
		this.getDialogPane().getStylesheets().add(Main.class.getResource("/MainWindow.css").toExternalForm());
		ButtonType ok = ButtonType.OK; 
		ButtonType cancel = ButtonType.CANCEL; 
		this.getDialogPane().getButtonTypes().addAll(ok, cancel);
		
		this.setResultConverter(new Callback<ButtonType, CarFX>(){
			@Override
			public CarFX call(ButtonType bt) {
				if(bt == ok) {
					if(selectedCar != null) {
						try {
							Database.activateDeletedCar(selectedCar.getModellObject().getCarVinNumber());
							return selectedCar;
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}	
				}
				return null;
			}
		});	
	}
	
	public TableView<CarFX> carsTableView() {
		TableColumn<CarFX, Number> numberCol = new TableColumn<>("nr.");
		numberCol.setSortable(false);
		numberCol.setPrefWidth(35);
		numberCol.setMinWidth(30);
		
		TableColumn<CarFX, String> categorieCol = new TableColumn<>("Category");
		categorieCol.setPrefWidth(100);
		categorieCol.setMinWidth(30);
		categorieCol.setCellValueFactory(new PropertyValueFactory<>("carCategory"));
		categorieCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<CarFX, String> markeCol = new TableColumn<>("Brand");
		markeCol.setPrefWidth(110);
		markeCol.setMinWidth(30);
		markeCol.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
	
		TableColumn<CarFX, String> modellCol = new TableColumn<>("Modell");
		modellCol.setPrefWidth(110);
		modellCol.setMinWidth(30);
		modellCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
    
		TableColumn<CarFX, String> licPlateCol = new TableColumn<>("License Plate");
		licPlateCol.setPrefWidth(105);
		licPlateCol.setMinWidth(30);
    	licPlateCol.setCellValueFactory(new PropertyValueFactory<>("carLicensePlate"));
    	licPlateCol.setSortType(TableColumn.SortType.ASCENDING);
    

		TableColumn<CarFX, String> vinNumCol = new TableColumn<>("VIN number");
		vinNumCol.setPrefWidth(165);
		vinNumCol.setMinWidth(30);
		vinNumCol.setCellValueFactory(new PropertyValueFactory<>("carVinNumber"));
    	
    	
    	TableColumn<CarFX, String> fuelTypeCol = new TableColumn<>("Fuel Type");
    	fuelTypeCol.setPrefWidth(80);
    	fuelTypeCol.setMinWidth(30);
    	fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("carFuelType"));

    	
    	carsTableView = new TableView<>(cars);
		carsTableView.setPrefSize(710, 550);
		carsTableView.getColumns().addAll(numberCol, categorieCol, markeCol, modellCol, licPlateCol, vinNumCol, fuelTypeCol);
		carsTableView.getSortOrder().addAll(categorieCol, licPlateCol);
		carsTableView.setPlaceholder(new Label("No cars available!"));
	 	
		numberCol.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(carsTableView.getItems().indexOf(column.getValue())+1));

		return carsTableView;	
	}
		
	
}
