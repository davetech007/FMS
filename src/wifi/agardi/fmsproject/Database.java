package wifi.agardi.fmsproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class Database {
	public static final String DBlocation = "DatabaseTestNew9";
	public static final String connString = "jdbc:derby:" + DBlocation +";create=true";
//USERS
	public static final String usersTable = "Users";
	public static final String userIDCol = "User_ID";
	public static final String userNameCol = "Username";
	public static final String passwordCol = "Password";
	
//CARS	
	public static final String categoriesTable = "Categories";
	public static final String categoryIDCol = "Category_ID";
	public static final String categoryNameCol = "CategoryName";
	public static final String categoryPriceCol = "CategoryPrice";

	public static final String fuelTypesTable = "FuelTypes";
	public static final String fuelTypeIDCol = "FuelType_ID";
	public static final String fuelTypeNameCol = "FuelTypeName";
	private static final String[] fuelTypes = {"Benzin", "Diesel", "Hybrid", "Electric"};
	
	public static final String transmissionsTable = "Transmissions";
	public static final String transmissionIDCol = "Transmission_ID";
	public static final String transmissionTypeCol = "TransmissionType";
	private static final String[] transmissionTypes = {"Manual", "Automatic"};
	
	public static final String colorsTable = "Colors";
	public static final String colorIDCol = "Color_ID";
	public static final String colorNameCol = "ColorName";
	private static final String[] colors = {"Beige", "Black", "Blue", "Gold", "Gray", "Green", "Orange", 
											"Purple", "Red", "Silver", "Yellow", "White" };
	
	public static final String featuresTable = "Features";
	public static final String featureIDCol = "Feature_ID";
	public static final String featureNameCol = "FeatureName";
	private static final String[] features = {"Air Condition", "Bluetooth", "Digital Cockpit", "Head-up display", "Heated seats", "Heated s.wheel", "Isofix", 
			"Navigation", "Rain sensor", "Seat ventillation", "Start-stop", "USB", "WIFI", "Xenon", "LED-light", "Laser-light"};
	
	public static final String carFeaturesTable = "CarFeatures";
	public static final String carFeaturesIDCol = "CarFeatures_ID";
	
	public static final String carsTable = "Cars";
	public static final String vinNumberIDCol = "VinNumber_ID";
	public static final String licensePlateCol = "LicensePlate";
	public static final String brandCol = "Brand";
	public static final String modelCol = "Model";
	public static final String manufactureDateCol = "ManufactureDate";
	public static final String actualKMCol = "ActualKM";
	public static final String engineSizeCol = "EngineSize";
	public static final String enginePowerCol = "EnginePower";
	public static final String isOnRentCol = "IsOnRent";
	public static final String isDeactiveCol = "IsDeactive";

	
//CUSTOMERS
	public static final String nationalitiesTable = "Nationalities";
	public static final String nationalityIDCol = "Nationality_ID";
	public static final String nationalityNameCol = "NationalityName";
	private static final String[] nationalities = {"American", "Australian", "Austrian", "Belgian", "Brazilian", "British", "Canadian", "Chinese",	
				"Colombian", "Croatian", "Czech", "Dane", "Egyptian", "Finn", "French", "German", "Greek", "Dutch",
				"Hungarian", "Indian", "Irish", "Israeli", "Italian", "Japanese", "Korean", "Kuwaiti", "Liechtensteiner", "Luxembourger",
				"Malaysian", "Mexican", "Montenegrin", "Norwegian", "Pakistani", "Panamanian", "Filipino", "Pole", "Portuguese", "Quatari",
				"Romanian", "Russian", "Scot", "Serbian", "Slovak", "Slovenian", "Swede", "Swiss", "Thai", "Turk", "Ukrainian", "Welsh", "Yugoslav"};
	
	public static final String customersTable = "Customers";
	public static final String customerIDCol = "Customer_ID";
	public static final String firstNameCol = "FirstName";
	public static final String lastNameCol = "LastName";
	public static final String dateOfBornCol = "DateOfBorn";
	public static final String passportNumCol = "PassportNumber";
	public static final String driversLicCol = "DriversLicenseNumber";
	public static final String telefonCol = "Telefon";
	public static final String eMailCol = "EMail";
	public static final String addressLandCol = "AddressLand";
	public static final String addressCityCol = "AddressCity";
	public static final String addressStreetCol = "AddressStreet";
	public static final String addressPostCodeCol = "AddressPostalCode";
	
//RESERVATIONS
	public static final String insurancesTable = "Insurances";
	public static final String insuranceIDCol = "Insurance_ID";
	public static final String insuranceTypeCol = "InsuranceType";
	public static final String insurancePriceCol = "InsurancePrice";
	
	public static final String extrasTable = "Extras";
	public static final String extraIDCol = "Extra_ID";
	public static final String extraNameCol = "ExtraName";
	public static final String extraPriceCol = "ExtraPrice";
	
	public static final String reservationExtrasTable = "ReservationExtras";
	public static final String reservationExtraIDCol = "ReservationExtra_ID";
	
	public static final String reservationsTable = "Reservations";
	public static final String reservationNumberIDCol = "ReservationNumber_ID";
	public static final String pickupLocationCol = "PickupLocation";
	public static final String pickupTimeCol = "PickupTime";
	public static final String returnLocationCol = "ReturnLocation";
	public static final String returnTimeCol = "ReturnTime";
	public static final String resNotesCol = "ReservationNotes";
	
	
	
	
//USERS TABLE	
	public static void createUsersTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + usersTable + "(" +
						 userIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				         userNameCol + " VARCHAR(100), " +
				         passwordCol + " VARCHAR(100))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, usersTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			 
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
					
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createUsersTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
				    stmt.close();
				if(conn != null)
				   conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static boolean logIn(String username, String password) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			
		} catch (SQLException e) {
			System.out.println("Something is wrong with the login database connection...");
			e.printStackTrace();
		}
		
		if(conn != null) {
		try {
			stmt = conn.createStatement();
				
		} catch (SQLException e) {
			System.out.println("Something is wrong with the login creation of Statement...");
			e.printStackTrace();
			}
		}
		
		try {
			rs = stmt.executeQuery("SELECT * FROM "+ usersTable + 
								   " WHERE " + userNameCol + " = '" + username + 
								   "' AND " + passwordCol + " = '" + password + "'");
			
		} catch (SQLException e) {
			System.out.println("Something is wrong with login...");
			e.printStackTrace();
		}	
		try {
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong after login checking");
			e.printStackTrace();
			
		} finally {
			try {
				if(stmt != null)
					stmt.close();
			    if(conn != null)
				   conn.close();
			} catch(SQLException e) {
				throw e;
			}
		}
		return false;
	}
	
		
	public static void signUp(String username, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String insert = "INSERT INTO " + usersTable +
						" (" + userNameCol + ", " + passwordCol + 
						") VALUES(?, ?)";
							
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Something is with signup");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
				  pstmt.close();
				if(conn != null)
				  conn.close();
		    } catch(SQLException e) {
			  throw e;
		    }
		
	   }
	}
	
	
	public static boolean checkUser(String username) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+ usersTable +
								   " WHERE " + userNameCol + " = '" + username + "'");
		    if(rs.next()) {
		    	return true;
		    }
		} catch (SQLException e) {
			System.out.println("Something is wrong with checkUser");
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null)
				  stmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
		return false;
	}
	
    
//CARS
	
//CATEGORIES TABLE	
	public static void createCarCategoriesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + categoriesTable + "(" +
						categoryIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						categoryNameCol + " VARCHAR(50), "+ categoryPriceCol +" INTEGER)";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, categoriesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			LinkedHashMap<String, Integer> categories = new LinkedHashMap<>();
				categories.put("A_Mini", 30);
				categories.put("B_Small", 40);
				categories.put("C_Economy", 50);
				categories.put("D_Midsize", 60);
				categories.put("E_Fullsize", 75);
				categories.put("F_Premium", 95);
				categories.put("P_Luxus", 125);
				categories.put("S_Minivan", 105);
				categories.put("V_FullsizeVan", 125);
				categories.put("R_Convertible", 150);
				categories.put("X_SUV", 140);
			for(String key: categories.keySet()) {
				Integer price = categories.get(key);
				addCarCategory(key, price);
			}
			
			System.out.println("Adding basic categories completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarCategoriesTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addCarCategory(String category, int price) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + categoriesTable + " (" + categoryNameCol + ", " + categoryPriceCol + ") VALUES(?, ?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, category);
			pstmt.setInt(2, price);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarCategory database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static LinkedHashMap<String, Integer> readCarCategoriesTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  LinkedHashMap<String, Integer> categories = new LinkedHashMap<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + categoriesTable);
			while(rs.next()) {
				categories.put(rs.getString(categoryNameCol), rs.getInt(categoryPriceCol));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarCategoriesTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return categories;
	   }
	

	public static int readCarCategoriesID(String catName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int categoryID =  0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + categoryIDCol + " FROM " +
									categoriesTable + " WHERE " + categoryNameCol + " = '" + catName + "'");
			if(rs.next()) {
				categoryID = rs.getInt(categoryIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarCategoriesID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return categoryID;
	   }
	
	
	public static String readCarCategoryName(int catID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String catName =  "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + categoryNameCol + " FROM " +
									categoriesTable + " WHERE " + categoryIDCol + " = " + catID);
			if(rs.next()) {
				catName = rs.getString(categoryNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarCategoryName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return catName;
	   }
	
	
	
//FUEL TYPE TABLE	
	public static void createCarFuelTypeTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + fuelTypesTable + "(" +
						fuelTypeIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						fuelTypeNameCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, fuelTypesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			for(String ft : fuelTypes) {
				addCarFuelType(ft);
			}
			System.out.println("Adding basic fuel types completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarFuelTypeTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addCarFuelType(String fuelType) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + fuelTypesTable + " (" + fuelTypeNameCol + ") VALUES(?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, fuelType);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarFuelType database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	

	public static ArrayList<String> readCarFuelTypeTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> fuelTypes = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + fuelTypesTable);
			while(rs.next()) {
				fuelTypes.add(rs.getString(fuelTypeNameCol));
			}
			rs.close();
				
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarFuelTypeTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return fuelTypes;
	   }
	
	
	public static int readFuelTypeID(String fuelName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int fuelID =  0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + fuelTypeIDCol + " FROM " +
									fuelTypesTable + " WHERE " + fuelTypeNameCol + " = '" + fuelName + "'");
			if(rs.next()) {
				fuelID = rs.getInt(fuelTypeIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readFuelTypeID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return fuelID;
	   }
	
	
	public static String readFuelTypeName(int fuelID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String fuelName =  "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + fuelTypeNameCol + " FROM " +
									fuelTypesTable + " WHERE " + fuelTypeIDCol + " = " + fuelID);
			if(rs.next()) {
				fuelName = rs.getString(fuelTypeNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readFuelTypeName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return fuelName;
	   }

	
	
//TRANSMISSION TYPE TABLE	
	public static void createCarTransmissionTypeTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + transmissionsTable + "(" +
						transmissionIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						transmissionTypeCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, transmissionsTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			for(String ft : transmissionTypes) {
				addCarTransmissionType(ft);
			}
			System.out.println("Adding basic transmission types completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarTransmissionTypeTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}	

	
	public static void addCarTransmissionType(String transmissionType) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + transmissionsTable + " (" + transmissionTypeCol + ") VALUES(?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, transmissionType);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarTransmissionType database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static ArrayList<String> readCarTransmissionTypeTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> transmissionTypes = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + transmissionsTable);
			while(rs.next()) {
				transmissionTypes.add(rs.getString(transmissionTypeCol));
			}
			rs.close();
				
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarTransmissionTypeTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return transmissionTypes;
	   }
		
	
	public static int readTransmissionID(String transmName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int transmID =  0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + transmissionIDCol + " FROM " +
									transmissionsTable + " WHERE " + transmissionTypeCol + " = '" + transmName + "'");
			if(rs.next()) {
				transmID = rs.getInt(transmissionIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readTransmissionID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return transmID;
	   }

	
	public static String readTransmissionName(int transID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String transName =  "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + transmissionTypeCol + " FROM " +
									transmissionsTable + " WHERE " + transmissionIDCol + " = " + transID);
			if(rs.next()) {
				transName = rs.getString(transmissionTypeCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readTransmissionName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return transName;
	   }
	
	
	
//COLORS TABLE	
	public static void createCarColorsTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + colorsTable + "(" +
						colorIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						colorNameCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, colorsTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			for(String ft : colors) {
				addCarColor(ft);
			}
			System.out.println("Adding basic color types completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarColorsTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}	

		
	public static void addCarColor(String color) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + colorsTable + " (" + colorNameCol + ") VALUES(?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, color);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarColor database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static ArrayList<String> readCarColorsTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> colorTypes = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + colorsTable);
			while(rs.next()) {
				colorTypes.add(rs.getString(colorNameCol));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarColorsTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return colorTypes;
	   }
	
	
	public static int readColorID(String colorName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int colorID =  0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + colorIDCol + " FROM " +
									colorsTable + " WHERE " + colorNameCol + " = '" + colorName + "'");
			if(rs.next()) {
				colorID = rs.getInt(colorIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readTColorID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return colorID;
	   }
	
	
	public static String readColorName(int colorID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String colorName =  "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + colorNameCol + " FROM " +
									colorsTable + " WHERE " + colorIDCol + " = " + colorID);
			if(rs.next()) {
				colorName = rs.getString(colorNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readColorName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return colorName;
	   }
	
	
	
//FEATURES TABLE	
	public static void createFeaturesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + featuresTable + "(" +
						featureIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						featureNameCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, featuresTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			for(String ft : features) {
				addFeature(ft);
			}
			System.out.println("Adding basic features completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createFeaturesTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static void addFeature(String feature) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + featuresTable + " (" + featureNameCol + ") VALUES(?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, feature);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarFeature database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static ArrayList<String> readFeaturesTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> features = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + featuresTable);
			while(rs.next()) {
				features.add(rs.getString(featureNameCol));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarFeaturesTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return features;
	   }
	
	
	public static int readFeatureID(String featureName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int featID = 0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + featureIDCol + " FROM " +
									featuresTable + " WHERE " + featureNameCol + " = '" + featureName + "'");
			if(rs.next()) {
				featID = rs.getInt(featureIDCol);
			}
			
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readFeatureID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return featID;
	   }

	
	public static String readFeatureName(int featureID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String featureName = "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + featureNameCol + " FROM " +
									featuresTable + " WHERE " + featureIDCol + " = " + featureID);
			if(rs.next()) {
				featureName = rs.getString(featureNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readFeatureName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return featureName;
	   }
	
	
//CAR FEATURES JUNCTION TABLE	
	public static void createCarFeaturesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + carFeaturesTable + "(" +
				carFeaturesIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				vinNumberIDCol + " VARCHAR(17), "+
				featureIDCol + " INTEGER, " +
				"FOREIGN KEY (" + vinNumberIDCol + ") REFERENCES " + carsTable +" (" + vinNumberIDCol + "), " +
				"FOREIGN KEY (" + featureIDCol + ") REFERENCES " + featuresTable +" (" + featureIDCol + "))";
								
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, carFeaturesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarFeaturesTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}


	public static void addCarFeatures(ArrayList<String> featureNames, String vinNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + carFeaturesTable + " (" + vinNumberIDCol + ", " + featureIDCol + ") VALUES (?,?)";
				
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			for(String s : featureNames) {
			pstmt.setString(1, vinNum);
			pstmt.setInt(2, readFeatureID(s));
			pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addCarFeatures database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static void updateCarFeatures(ArrayList<String> featureNames, String vinNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "DELETE FROM " + carFeaturesTable + " WHERE " + vinNumberIDCol + " = '" + vinNum + "'";
				
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			pstmt.executeUpdate();
			addCarFeatures(featureNames, vinNum);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the updateCarFeatures database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static ArrayList<String> readCarFeatures(String VinNum) throws SQLException {
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> features = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + featureIDCol + " FROM " +
					carFeaturesTable + " WHERE " + vinNumberIDCol + " = '" + VinNum + "'");
			while(rs.next()) {
				features.add(readFeatureName(rs.getInt(featureIDCol)));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarFeatures database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return features;
	   }
	
	
//CARS TABLE	
	public static void createCarsTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + carsTable + "(" +
						vinNumberIDCol + " VARCHAR(17), " +
						licensePlateCol + " VARCHAR(20), " +
						brandCol + " VARCHAR(50), " +
						modelCol + " VARCHAR(50), " +
						categoryIDCol + " INTEGER, " +
						colorIDCol + " INTEGER, " +
						fuelTypeIDCol + " INTEGER, " +
						transmissionIDCol + " INTEGER, " +
						manufactureDateCol + " DATE, " + 
						actualKMCol + " INTEGER, " +
						engineSizeCol + " INTEGER, " +
						enginePowerCol + " INTEGER, " +
						isOnRentCol + " BOOLEAN DEFAULT FALSE NOT NULL, " +
						isDeactiveCol + " BOOLEAN DEFAULT FALSE NOT NULL, " +
						"PRIMARY KEY (" + vinNumberIDCol + "), " +
						"FOREIGN KEY (" + categoryIDCol + ") REFERENCES " + categoriesTable + "(" + categoryIDCol + "), " +
						"FOREIGN KEY (" + colorIDCol + ") REFERENCES " + colorsTable + "(" + colorIDCol + "), " +
						"FOREIGN KEY (" + fuelTypeIDCol + ") REFERENCES " + fuelTypesTable + "(" + fuelTypeIDCol + "), " +
						"FOREIGN KEY (" + transmissionIDCol + ") REFERENCES " + transmissionsTable + "(" + transmissionIDCol + "))";
					
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, carsTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCarsTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addNewCar(Car car) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String add = "INSERT INTO " + carsTable + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			
			pstmt.setString(1, car.getCarVinNumber());
			pstmt.setString(2, car.getCarLicensePlate());
			pstmt.setString(3, car.getCarBrand());
			pstmt.setString(4, car.getCarModel());
			pstmt.setInt(5, readCarCategoriesID(car.getCarCategory()));
			pstmt.setInt(6, readColorID(car.getCarColor()));
			pstmt.setInt(7, readFuelTypeID(car.getCarFuelType()));
			pstmt.setInt(8, readTransmissionID(car.getCarTransmission()));
			LocalDateTime date = LocalDateTime.of(car.getCarManufDate().getYear(),
												  car.getCarManufDate().getMonth(),
												  car.getCarManufDate().getDayOfMonth(),0,0,0,0);
			java.sql.Date sqldate = new java.sql.Date(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setDate(9, sqldate);
			pstmt.setInt(10, car.getCarKM());
			pstmt.setInt(11, car.getCarEngineSize());
			pstmt.setInt(12, car.getCarEnginePower());
			pstmt.setBoolean(13, false);
			pstmt.setBoolean(14, false);
			
			pstmt.executeUpdate();
			
			addCarFeatures(car.getCarFeatures(), car.getCarVinNumber());
			System.out.println("Car added successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addNewCar database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	

	public static void updateCar(Car car) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "UPDATE " + carsTable + " SET " + 
												licensePlateCol + " = ?, " + 
												brandCol + " = ?, " + 
												modelCol + " = ?, " + 
												categoryIDCol + " = ?, " + 
												colorIDCol + " = ?, " + 
												fuelTypeIDCol + " = ?, " + 
												transmissionIDCol + " = ?, " + 
												manufactureDateCol + " = ?, " + 
												actualKMCol + " = ?, " + 
												engineSizeCol + " = ?, " + 
												enginePowerCol + " = ?, " + 
												isOnRentCol + " = ? " + 
												"WHERE " + vinNumberIDCol + " = ?";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			
			pstmt.setString(1, car.getCarLicensePlate());
			pstmt.setString(2, car.getCarBrand());
			pstmt.setString(3, car.getCarModel());
			pstmt.setInt(4, readCarCategoriesID(car.getCarCategory()));
			pstmt.setInt(5, readColorID(car.getCarColor()));
			pstmt.setInt(6, readFuelTypeID(car.getCarFuelType()));
			pstmt.setInt(7, readTransmissionID(car.getCarTransmission()));
			LocalDateTime date = LocalDateTime.of(car.getCarManufDate().getYear(),
												  car.getCarManufDate().getMonth(),
												  car.getCarManufDate().getDayOfMonth(),0,0,0,0);
			java.sql.Date sqldate = new java.sql.Date(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setDate(8, sqldate);
			pstmt.setInt(9, car.getCarKM());
			pstmt.setInt(10, car.getCarEngineSize());
			pstmt.setInt(11, car.getCarEnginePower());
			pstmt.setBoolean(12, car.isOnRent());
			pstmt.setString(13, car.getCarVinNumber());
			
			pstmt.executeUpdate();
	
			updateCarFeatures(car.getCarFeatures(), car.getCarVinNumber());
			System.out.println("Car updated successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the updateCar database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void deleteCar(String vinNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String delete = "UPDATE " + carsTable + " SET " + isDeactiveCol + " = ? WHERE " + vinNumberIDCol + " = '" + vinNum + "'";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);
			pstmt.setBoolean(1, true);
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with deleteCar database connection");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
	}
	
	
	public static ArrayList<Car> readCarsTable(String active, String deactive) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<Car> cars = new ArrayList<>();
		  String select = "SELECT * FROM " + carsTable;
		  String where = "";
		   if(active.equals("active") && deactive == "") {
			   where = isDeactiveCol + " = FALSE";
		   }
		   if(deactive.equals("deactive") && active == "") {
			   where = isDeactiveCol + " = TRUE";
		   }
		   if(where.length() > 0) {
			   select += " WHERE " + where;
		   }
		    
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()) {
				java.sql.Date sqlDate = rs.getDate(manufactureDateCol);
				java.time.LocalDate locDate = sqlDate.toLocalDate();
				String vinNum = rs.getString(vinNumberIDCol);
				
				Car car = new Car(vinNum, 
								  rs.getString(licensePlateCol),
								  rs.getString(brandCol),
								  rs.getString(modelCol),
								  readCarCategoryName(rs.getInt(categoryIDCol)),
								  readColorName(rs.getInt(colorIDCol)),
								  readFuelTypeName(rs.getInt(fuelTypeIDCol)),
								  readTransmissionName(rs.getInt(transmissionIDCol)),
								  locDate,
								  rs.getInt(actualKMCol),
								  rs.getInt(engineSizeCol),
								  rs.getInt(enginePowerCol),
								  readCarFeatures(vinNum),
								  rs.getBoolean(isOnRentCol));
				cars.add(car);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCarsTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return cars;
	   }

	
	public static String checkExistingCar(String vinID, String licPlate) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String car = "";
		 String select = "SELECT " + brandCol + ", " + modelCol + ", ";
		 
		   if(vinID != null && vinID.length() > 0) {
			   select += licensePlateCol + " FROM " + carsTable + " WHERE " + vinNumberIDCol + " = '" + vinID + "'";
		   }
		   if(licPlate != null && licPlate.length() > 0) {
			   select += vinNumberIDCol + " FROM " + carsTable + " WHERE " + licensePlateCol + " = '" + licPlate + "'";
		   }
		
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(select);
		    if(rs.next() && vinID != "") {
		    	car = "'" + rs.getString(brandCol) + " " + rs.getString(modelCol) + "', with the license plate '" + rs.getString(licensePlateCol) + "'.";
		    }
		    rs = stmt.executeQuery(select);
		    if(rs.next() && licPlate != "") {
		    	car = "'" + rs.getString(brandCol) + " " + rs.getString(modelCol) + "', with the VIN number '" + rs.getString(vinNumberIDCol) + "'.";
		    }
		    rs.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong with checkExistingCar database connection");
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null)
				  stmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
		return car;
	}
	
	
	public static void activateDeletedCar(String vinNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String delete = "UPDATE " + carsTable + " SET " + isDeactiveCol + " = ? WHERE " + vinNumberIDCol + " = '" + vinNum + "'";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);
			pstmt.setBoolean(1, false);
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with activateDeletedCar database connection");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
	}
	
	
	public static void checkOutCar(String vinNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "UPDATE " + carsTable + " SET " + 
												isOnRentCol + " = ? " + 
												"WHERE " + vinNumberIDCol + " = ?";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			
			pstmt.setBoolean(1, true);
			pstmt.setString(2, vinNum);
			
			pstmt.executeUpdate();
			System.out.println("Car rented out successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the rentOutCar database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void checkInCar(String vinNum, int km) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "UPDATE " + carsTable + " SET " + 
												actualKMCol + " = ?, " + 
												isOnRentCol + " = ? " + 
												"WHERE " + vinNumberIDCol + " = ?";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			
			pstmt.setInt(1, km);
			pstmt.setBoolean(2, false);
			pstmt.setString(3, vinNum);
			
			pstmt.executeUpdate();
			System.out.println("Car returned successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the returnCar database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	
	
	
//CUSTOMERS	
	
//NATIONALITY	
	public static void createNationalitiesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + nationalitiesTable + "(" +
						nationalityIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						nationalityNameCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, nationalitiesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			for(String ft : nationalities) {
				addNationality(ft);
			}
			System.out.println("Adding basic nationalities completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createNationalityTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addNationality(String nationality) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + nationalitiesTable + " (" + nationalityNameCol + ") VALUES(?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, nationality);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addNationality database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static ArrayList<String> readNationalitiesTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> nationalities = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + nationalitiesTable);
			while(rs.next()) {
				nationalities.add(rs.getString(nationalityNameCol));
			}
			rs.close();
				
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readNationalitiesTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return nationalities;
	   }
	
	
	public static int readNationalityID(String nationality) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int nationalityID = 0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + nationalityIDCol + " FROM " +
									nationalitiesTable + " WHERE " + nationalityNameCol + " = '" + nationality + "'");
			if(rs.next()) {
				nationalityID = rs.getInt(nationalityIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readNationalityID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return nationalityID;
	   }
	
	
	public static String readNationalityName(int nationalityID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String nationalityName =  "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + nationalityNameCol + " FROM " +
									nationalitiesTable + " WHERE " + nationalityIDCol + " = " + nationalityID);
			if(rs.next()) {
				nationalityName = rs.getString(nationalityNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readNationalityName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return nationalityName;
	   }
	
	
	
//CUSTOMERS TABLE	
	public static void createCustomersTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + customersTable + "(" +
						customerIDCol + " VARCHAR(100), " +
						firstNameCol + " VARCHAR(100), " +
						lastNameCol + " VARCHAR(100), " +
						dateOfBornCol + " DATE, " +
						nationalityIDCol + " INTEGER, " +
						passportNumCol + " VARCHAR(50), " +
						driversLicCol + " VARCHAR(50), " +
						telefonCol + " VARCHAR(50), " +
						eMailCol + " VARCHAR(50), " + 
						addressLandCol + " VARCHAR(50), " +
						addressCityCol + " VARCHAR(50), " +
						addressStreetCol + " VARCHAR(50), " +
						addressPostCodeCol + " VARCHAR(30), " +
						isDeactiveCol + " BOOLEAN DEFAULT FALSE NOT NULL, " +
						"PRIMARY KEY (" + customerIDCol + "), " +
						"FOREIGN KEY (" + nationalityIDCol + ") REFERENCES " + nationalitiesTable + "(" + nationalityIDCol + "))";
						
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, customersTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createCustomersTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		   }
	}
	
	
	public static void addNewCustomer(Customer customer) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String add = "INSERT INTO " + customersTable + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			
			pstmt.setString(1, customer.getCustomerID());
			pstmt.setString(2, customer.getFirstName());
			pstmt.setString(3, customer.getLastName());
			LocalDateTime date = LocalDateTime.of(customer.getDateOfBorn().getYear(),
					  							  customer.getDateOfBorn().getMonth(),
					                              customer.getDateOfBorn().getDayOfMonth(),0,0,0,0);
			java.sql.Date sqldate = new java.sql.Date(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setDate(4, sqldate);
			pstmt.setInt(5, readNationalityID(customer.getNationality()));
			pstmt.setString(6, customer.getPassportNum());
			pstmt.setString(7, customer.getDriversLicenseNum());
			pstmt.setString(8, customer.getTelefon());
			pstmt.setString(9, customer.geteMail());
			pstmt.setString(10, customer.getAddressLand());
			pstmt.setString(11, customer.getAddressCity());
			pstmt.setString(12, customer.getAddressStreet());
			pstmt.setString(13, customer.getAddressPostalCode());
			pstmt.setBoolean(14, false);
		
			pstmt.executeUpdate();
			System.out.println("Customer added successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addNewCustomer database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void updateCustomer(Customer customer) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "UPDATE " + customersTable + " SET " + 
												firstNameCol + " = ?, " + 
												lastNameCol + " = ?, " + 
												dateOfBornCol + " = ?, " + 
												nationalityIDCol + " = ?, " + 
												passportNumCol + " = ?, " + 
												driversLicCol + " = ?, " + 
												telefonCol + " = ?, " + 
												eMailCol + " = ?, " + 
												addressLandCol + " = ?, " + 
												addressCityCol + " = ?, " + 
												addressStreetCol + " = ?, " + 
												addressPostCodeCol + " = ? " + 
												"WHERE " + customerIDCol + " = ?";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			
			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			LocalDateTime date = LocalDateTime.of(customer.getDateOfBorn().getYear(),
											      customer.getDateOfBorn().getMonth(),
												  customer.getDateOfBorn().getDayOfMonth(),0,0,0,0);
			java.sql.Date sqldate = new java.sql.Date(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setDate(3, sqldate);
			pstmt.setInt(4, readNationalityID(customer.getNationality()));
			pstmt.setString(5, customer.getPassportNum());
			pstmt.setString(6, customer.getDriversLicenseNum());
			pstmt.setString(7, customer.getTelefon());
			pstmt.setString(8, customer.geteMail());
			pstmt.setString(9, customer.getAddressLand());
			pstmt.setString(10, customer.getAddressCity());
			pstmt.setString(11, customer.getAddressStreet());
			pstmt.setString(12, customer.getAddressPostalCode());
			pstmt.setString(13, customer.getCustomerID());
			
			pstmt.executeUpdate();
			System.out.println("Customer updated successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the updateCustomer database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
 		
	
	public static void deleteCustomer(String customerID) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String delete = "UPDATE " + customersTable + " SET " + isDeactiveCol + " = ? WHERE " + customerIDCol + " = '" + customerID + "'";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);
			pstmt.setBoolean(1, true);
		    pstmt.executeUpdate();
		    System.out.println("Customer successfully deleted!");
		} catch (SQLException e) {
			System.out.println("Something is wrong with deleteCustomer database connection");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
	}
	
	
	public static ArrayList<Customer> readCustomersTable(String active, String deactive) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<Customer> customers = new ArrayList<>();
		  String select = "SELECT * FROM " + customersTable;
		  String where = "";
		   if(active.equals("active") && deactive == "") {
			   where = isDeactiveCol + " = FALSE";
		   }
		   if(deactive.equals("deactive") && active == "") {
			   where = isDeactiveCol + " = TRUE";
		   }
		   if(where.length() > 0) {
			   select += " WHERE " + where;
		   }
		 
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()) {
				java.sql.Date sqlDate = rs.getDate(dateOfBornCol);
				java.time.LocalDate locDate = sqlDate.toLocalDate();
				
				Customer customer = new Customer(rs.getString(customerIDCol), 
								  rs.getString(firstNameCol),
								  rs.getString(lastNameCol),
								  locDate,
								  readNationalityName(rs.getInt(nationalityIDCol)),
								  rs.getString(passportNumCol),
								  rs.getString(driversLicCol),
								  rs.getString(telefonCol),
								  rs.getString(eMailCol),
								  rs.getString(addressLandCol),
								  rs.getString(addressCityCol),
								  rs.getString(addressStreetCol),
								  rs.getString(addressPostCodeCol));
				customers.add(customer);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readCustomersTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return customers;
	   }
	

	public static void activateDeletedCustomer(String customerID) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String delete = "UPDATE " + customersTable + " SET " + isDeactiveCol + " = ? WHERE " + customerIDCol + " = '" + customerID + "'";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(delete);
			pstmt.setBoolean(1, false);
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with activateDeletedCustomer database connection");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
	}
	
	
	
	
//RESERVATIONS
	public static void createReservationsTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + reservationsTable + "(" +
						reservationNumberIDCol + " VARCHAR(100), " +
						customerIDCol + " VARCHAR(100), " +
						vinNumberIDCol + " VARCHAR(17), " +
						categoryIDCol + " INTEGER, " +
						insuranceIDCol + " INTEGER, " +
						pickupLocationCol + " VARCHAR(200), " +
						pickupTimeCol + " TIMESTAMP, " +
						returnLocationCol + " VARCHAR(200), " +
						returnTimeCol + " TIMESTAMP, " +
						resNotesCol + " VARCHAR(300), " +
						isDeactiveCol + " BOOLEAN DEFAULT FALSE NOT NULL, " +
						"PRIMARY KEY (" + reservationNumberIDCol + "), " +
						"FOREIGN KEY (" + customerIDCol + ") REFERENCES " + customersTable + "(" + customerIDCol + "), " +
						"FOREIGN KEY (" + vinNumberIDCol + ") REFERENCES " + carsTable + "(" + vinNumberIDCol + "), " +
						"FOREIGN KEY (" + categoryIDCol + ") REFERENCES " + categoriesTable + "(" + categoryIDCol + "), " +
						"FOREIGN KEY (" + insuranceIDCol + ") REFERENCES " + insurancesTable + "(" + insuranceIDCol + "))";
						
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, reservationsTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createReservationsTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		   }
	}
	
	
	public static void addNewReservation(Reservation res) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + reservationsTable + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			
			pstmt.setString(1, res.getResNumberID());
			pstmt.setString(2, res.getCustomer().getCustomerID());
			pstmt.setString(3, res.getCar().getCarVinNumber());
			pstmt.setInt(4, readCarCategoriesID(res.getReservedCategory()));
			pstmt.setInt(5, readInsuraceID(res.getInsuranceType()));
			pstmt.setString(6, res.getPickupLocation());
			LocalDateTime pdate = LocalDateTime.of(res.getPickupTime().getYear(),
												   res.getPickupTime().getMonth(),
												   res.getPickupTime().getDayOfMonth(),
												   res.getPickupTime().getHour(),
												   res.getPickupTime().getMinute(),0,0);
			java.sql.Timestamp sqlpTime = new java.sql.Timestamp(pdate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setTimestamp(7, sqlpTime);
			pstmt.setString(8, res.getReturnLocation());
			LocalDateTime rdate = LocalDateTime.of(res.getReturnTime().getYear(),
					  							   res.getReturnTime().getMonth(),
					  							   res.getReturnTime().getDayOfMonth(),
					  							   res.getReturnTime().getHour(),
					  							   res.getReturnTime().getMinute(),0,0);
			java.sql.Timestamp sqlrTime = new java.sql.Timestamp(rdate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setTimestamp(9, sqlrTime);
			pstmt.setString(10, res.getResNotes());
			pstmt.setBoolean(11, false);
			
			pstmt.executeUpdate();
			
			addReservationExtras(res.getResExtras(), res.getResNumberID());
			System.out.println("Reservation added successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addNewReservation database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}

	
	public static void updateReservation(Reservation res) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "UPDATE " + reservationsTable + " SET " + 
												customerIDCol + " = ?, " + 
												vinNumberIDCol + " = ?, " + 
												categoryIDCol + " = ?, " + 
												insuranceIDCol + " = ?, " + 
												pickupLocationCol + " = ?, " + 
												pickupTimeCol + " = ?, " + 
												returnLocationCol + " = ?, " + 
												returnTimeCol + " = ?, " + 
												resNotesCol + " = ?, "+
												isDeactiveCol + " = ? " +
												"WHERE " + reservationNumberIDCol + " = ?";
		
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			
			pstmt.setString(1, res.getCustomer().getCustomerID());
			pstmt.setString(2, res.getCar().getCarVinNumber());
			pstmt.setInt(3, readCarCategoriesID(res.getReservedCategory()));
			pstmt.setInt(4, readInsuraceID(res.getInsuranceType()));
			pstmt.setString(5, res.getPickupLocation());
			LocalDateTime pdate = LocalDateTime.of(res.getPickupTime().getYear(),
												   res.getPickupTime().getMonth(),
												   res.getPickupTime().getDayOfMonth(),
												   res.getPickupTime().getHour(),
												   res.getPickupTime().getMinute(),0,0);
			java.sql.Timestamp sqlpTime = new java.sql.Timestamp(pdate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setTimestamp(6, sqlpTime);
			pstmt.setString(7, res.getReturnLocation());
			LocalDateTime rdate = LocalDateTime.of(res.getReturnTime().getYear(),
					  							   res.getReturnTime().getMonth(),
					  							   res.getReturnTime().getDayOfMonth(),
					  							   res.getReturnTime().getHour(),
					  							   res.getReturnTime().getMinute(),0,0);
			java.sql.Timestamp sqlrTime = new java.sql.Timestamp(rdate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			pstmt.setTimestamp(8, sqlrTime);
			pstmt.setString(9, res.getResNotes());
			pstmt.setBoolean(10, res.isStatus());
			pstmt.setString(11, res.getResNumberID());
			pstmt.executeUpdate();
	
			updateReservationExtras(res.getResExtras(), res.getResNumberID());
			System.out.println("Reservation updated successfully");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the updateReservation database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void cancelReservation(String resNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String cancel = "UPDATE " + reservationsTable + " SET " + isDeactiveCol + " = ? WHERE " + reservationNumberIDCol + " = '" + resNum + "'";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(cancel);
		    pstmt.setBoolean(1, true);
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with cancelReservation database connection");
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
				  conn.close();
			}
		catch(SQLException e) {
			throw e;
		  }
		}
	}
	
	
	public static ArrayList<Reservation> readReservationsTable(String active, String cancelled) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<Reservation> reservations = new ArrayList<>();
		  Customer customer = null;
		  Car car = null;
		  String select = "SELECT * FROM " + reservationsTable;
		  String where = "";
		   if(active.equals("active") && cancelled == "") {
			   where = isDeactiveCol + " = FALSE";
		   }
		   if(cancelled.equals("cancelled") && active == "") {
			   where = isDeactiveCol + " = TRUE";
		   }
		   if(where.length() > 0) {
			   select += " WHERE " + where;
		   }
		  
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()) {
				java.sql.Timestamp sqlpTime = rs.getTimestamp(pickupTimeCol);
				java.time.LocalDateTime pickupTime = sqlpTime.toLocalDateTime();
				
				java.sql.Timestamp sqlrTime = rs.getTimestamp(returnTimeCol);
				java.time.LocalDateTime returnTime = sqlrTime.toLocalDateTime();
				
				String resNum = rs.getString(reservationNumberIDCol);
				
				
				for(Customer s : Database.readCustomersTable("", "")) {
					if(s.getCustomerID().equals(rs.getString(customerIDCol))) {
						customer = s;
					}
				}
				for(Car c : Database.readCarsTable("", "")) {
					if(c.getCarVinNumber().equals(rs.getString(vinNumberIDCol))) {
					    car = c;
					} 
				}
				Reservation res = new Reservation(resNum, 
												  customer,
												  car,
												  readCarCategoryName(rs.getInt(categoryIDCol)),
												  readInsuranceType(rs.getInt(insuranceIDCol)),
												  rs.getString(pickupLocationCol),
												  pickupTime,
												  rs.getString(returnLocationCol),
												  returnTime,
												  rs.getString(resNotesCol),
												  readReservationExtras(resNum),
												  rs.getBoolean(isDeactiveCol));
				reservations.add(res);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readReservationsTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return reservations;
	   }
	
	
	
	
//INSURANCES
	public static void createInsurancesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + insurancesTable + "(" +
						insuranceIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						insuranceTypeCol + " VARCHAR(50), "+ insurancePriceCol +" INTEGER)";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, insurancesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			
			LinkedHashMap<String, Integer> insurances = new LinkedHashMap<>();
				insurances.put("CDW", 0);
				insurances.put("Super CDW", 35);
				insurances.put("LDW", 30);
				insurances.put("Super LDW", 55);
			for(String key: insurances.keySet()) {
				Integer price = insurances.get(key);
				addInsuranceType(key, price);
			}
			System.out.println("Adding basic insurances completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createInsurancesTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addInsuranceType(String insurance, int price) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + insurancesTable + " (" + insuranceTypeCol + ", " + insurancePriceCol + ") VALUES(?, ?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, insurance);
			pstmt.setInt(2, price);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addInsuranceType database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static LinkedHashMap<String, Integer> readInsurancesTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  LinkedHashMap<String, Integer> insurances = new LinkedHashMap<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + insurancesTable);
			while(rs.next()) {
				insurances.put(rs.getString(insuranceTypeCol), rs.getInt(insurancePriceCol));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readInsurancesTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return insurances;
	   }
	
	
	public static int readInsuraceID(String insurance) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int insuranceID = 0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + insuranceIDCol + " FROM " +
									insurancesTable + " WHERE " + insuranceTypeCol + " = '" + insurance + "'");
			if(rs.next()) {
				insuranceID = rs.getInt(insuranceIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readInsuranceID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return insuranceID;
	   }
	
	
	public static String readInsuranceType(int insuranceID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String insuranceType = "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + insuranceTypeCol + " FROM " +
									insurancesTable + " WHERE " + insuranceIDCol + " = " + insuranceID);
			if(rs.next()) {
				insuranceType = rs.getString(insuranceTypeCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readInsuranceType database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return insuranceType;
	   }
	
	
//EXTRAS	
	public static void createExtrasTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + extrasTable + "(" +
						extraIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						extraNameCol + " VARCHAR(50), "+ extraPriceCol +" INTEGER)";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, extrasTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);

			LinkedHashMap<String, Integer> extras = new LinkedHashMap<>();
				extras.put("Child seat", 20);
				extras.put("Navigation", 25);
				extras.put("Border crossing", 30);
				extras.put("Young driver", 30);
				extras.put("Additional driver", 20);
				
			for(String key: extras.keySet()) {
				Integer price = extras.get(key);
				addExtraType(key, price);
			}
			System.out.println("Adding basic extras completed");
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createExtrasTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addExtraType(String extraName, int price) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + extrasTable + " (" + extraNameCol + ", " + extraPriceCol + ") VALUES(?, ?)";
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			pstmt.setString(1, extraName);
			pstmt.setInt(2, price);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addExtraType database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static LinkedHashMap<String, Integer> readExtrasTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  LinkedHashMap<String, Integer> extras = new LinkedHashMap<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + extrasTable);
			while(rs.next()) {
				extras.put(rs.getString(extraNameCol), rs.getInt(extraPriceCol));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readExtrasTable database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return extras;
	   }
	
	
	public static int readExtraID(String extraName) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  int extraID = 0;
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + extraIDCol + " FROM " +
									extrasTable + " WHERE " + extraNameCol + " = '" + extraName + "'");
			if(rs.next()) {
				extraID = rs.getInt(extraIDCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readExtraID database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return extraID;
	   }
	
	
	public static String readExtraName(int extraID) throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String extraName = "";
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + extraNameCol + " FROM " +
									extrasTable + " WHERE " + extraIDCol + " = " + extraID);
			if(rs.next()) {
				extraName = rs.getString(extraNameCol);
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readExtraName database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return extraName;
	   }

	
//RESERVATION EXTRAS JUNCTION TABLE	
	public static void createReservationExtrasTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + reservationExtrasTable + "(" +
				reservationExtraIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				reservationNumberIDCol + " VARCHAR(100), "+
				extraIDCol + " INTEGER, " +
				"FOREIGN KEY (" + reservationNumberIDCol + ") REFERENCES " + reservationsTable +" (" + reservationNumberIDCol + "), " +
				"FOREIGN KEY (" + extraIDCol + ") REFERENCES " + extrasTable +" (" + extraIDCol + "))";
								
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, reservationExtrasTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the createReservationExtrasTable database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void addReservationExtras(ArrayList<String> extraNames, String resNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String add = "INSERT INTO " + reservationExtrasTable + " (" + reservationNumberIDCol + ", " + extraIDCol + ") VALUES (?,?)";
				
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(add);
			for(String s : extraNames) {
			 pstmt.setString(1, resNum);
			 pstmt.setInt(2, readExtraID(s));
			 pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong with the addReservationExtras database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static void updateReservationExtras(ArrayList<String> extraNames, String resNum) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String update = "DELETE FROM " + reservationExtrasTable + " WHERE " + reservationNumberIDCol + " = '" + resNum + "'";	
		try {
			conn = DriverManager.getConnection(connString);
			pstmt = conn.prepareStatement(update);
			pstmt.executeUpdate();
			addReservationExtras(extraNames, resNum);
		} catch (SQLException e) {
			System.out.println("Something is wrong with the updateReservationExtras database connection...");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
	    }
	}
	
	
	public static ArrayList<String> readReservationExtras(String resNum) throws SQLException {
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<String> extras = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + extraIDCol + " FROM " +
					reservationExtrasTable + " WHERE " + reservationNumberIDCol + " = '" + resNum + "'");
			while(rs.next()) {
				extras.add(readExtraName(rs.getInt(extraIDCol)));
			}
			rs.close();	
		   }	  
		catch(SQLException e) {
			System.out.println("Something is wrong with the readReservationExtras database connection...");
			e.printStackTrace();
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
			catch(SQLException e) {
				throw e;
			}
		  }
		  return extras;
	   }
	
	
	
	
}
