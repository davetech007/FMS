package wifi.agardi.fmsproject;

public enum Categorie {
	M_MINI(30),
	A_SMALL(40),
	B_ECONOMY(50),
	C_MIDSIZE(60),
	D_FULLSIZE(70),
	F_PREMIUM(90),
	P_LUXUS(120),
	S_MINIVAN(80),
	V_FULLSIZEVAN(110),
	R_CONVERTIBLE(120),
	X_SUV(140);
	
	
	private int price;
	
	private Categorie(int price) {
		this.price = price;
	}
	public int getPrice() {
		return price;
	}

}
