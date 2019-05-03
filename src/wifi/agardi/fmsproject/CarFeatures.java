package wifi.agardi.fmsproject;

public class CarFeatures {
	String featureName;
	int featureID;
	
	
	public CarFeatures(String featureName, int featureID) {
		super();
		this.featureName = featureName;
		this.featureID = featureID;
	}


	public String getFeatureName() {
		return featureName;
	}


	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}


	public int getFeatureID() {
		return featureID;
	}


	public void setFeatureID(int featureID) {
		this.featureID = featureID;
	}
	
    

}
