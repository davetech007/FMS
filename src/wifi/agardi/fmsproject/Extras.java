package wifi.agardi.fmsproject;


public enum Extras {
	AIR_CONDITION("Air Condition"),
	GPS_BUILTIN("GPS"),
	ISOFIX("Isofix"),
	PARK_ASSIST("Park assist");
	
	private String name;
	
	private Extras(String name) {
		this.name = name;
		
	}

	public String getName() {
		return name;
	}
	

}
