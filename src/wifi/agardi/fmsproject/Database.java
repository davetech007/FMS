package wifi.agardi.fmsproject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Database {
	public static final String DBlocation = "DatabaseTestNew5";
	public static final String connString = "jdbc:derby:" + DBlocation +";create=true";

	public static final String usersTable = "Users";
	public static final String userIDCol = "User_ID";
	public static final String userNameCol = "Username";
	public static final String passwordCol = "Password";
	
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
	
	public static final String carFeaturesTable = "CarFeatures";
	public static final String carFeaturesIDCol = "CarFeatures_ID";
	
	
	public static final String carsTable = "Cars";
	public static final String vinNumberIDCol = "VinNumber_ID";
	public static final String licensePlateCol = "LicensePlate";
	public static final String brandCol = "Brand";
	public static final String modelCol = "Model";
	public static final String categoryCol = "Category_ID";
	public static final String colorCol = "Color_ID";
	public static final String fuelTypeCol = "FuelType_ID";
	public static final String transmissionCol = "Transmission_ID";
	public static final String manufactureDateCol = "ManufactureDate";
	public static final String actualKMCol = "ActualKM";
	public static final String engineSizeCol = "EngineSize";
	public static final String enginePowerCol = "EnginePower";
	public static final String isOnRentCol = "IsOnRent";
	
	
	
//USERS TABLE	
	public static void createUsersTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + usersTable + "(" +
						 userIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				         userNameCol + " VARCHAR(50), " +
				         passwordCol + " VARCHAR(50))";
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
	
    
	
	
//CATEGORIES TABLE	
	public static void createCarCategoriesTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + categoriesTable + "(" +
						categoryIDCol + " INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						categoryNameCol + " VARCHAR(30), "+ categoryPriceCol +" INTEGER)";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, categoriesTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
			LinkedHashMap<String, Integer> categories = new LinkedHashMap<>();
				categories.put("M_Mini", 30);
				categories.put("A_Small", 40);
				categories.put("B_Economy", 50);
				categories.put("C_Midsize", 60);
				categories.put("D_Fullsize", 75);
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
						fuelTypeNameCol + " VARCHAR(30))";
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
						transmissionTypeCol + " VARCHAR(30))";
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
						colorNameCol + " VARCHAR(30))";
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
						featureNameCol + " VARCHAR(40))";
		try {
			conn = DriverManager.getConnection(connString);
			rs = conn.getMetaData().getTables(null, null, featuresTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
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
	
	
//FEATURES JUNCTION TABLE	
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

	
//CARS TABLE	
	public static void createCarsTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + carsTable + "(" +
						vinNumberIDCol + " VARCHAR(17), " +
						licensePlateCol + " VARCHAR(10), " +
						brandCol + " VARCHAR(20), " +
						modelCol + " VARCHAR(20), " +
						categoryCol + " INTEGER, " +
						colorCol + " INTEGER, " +
						fuelTypeCol + " INTEGER, " +
						transmissionCol + " INTEGER, " +
						manufactureDateCol + " DATE, " + 
						actualKMCol + " INTEGER, " +
						engineSizeCol + " INTEGER, " +
						enginePowerCol + " INTEGER, " +
						isOnRentCol + " BOOLEAN DEFAULT FALSE NOT NULL, "+ 
						"PRIMARY KEY (" + vinNumberIDCol + "), " +
						"FOREIGN KEY (" + categoryCol + ") REFERENCES " + categoriesTable + "(" + categoryIDCol + "), " +
						"FOREIGN KEY (" + colorCol + ") REFERENCES " + colorsTable + "(" + colorIDCol + "), " +
						"FOREIGN KEY (" + fuelTypeCol + ") REFERENCES " + fuelTypesTable + "(" + fuelTypeIDCol + "), " +
						"FOREIGN KEY (" + transmissionCol + ") REFERENCES " + transmissionsTable + "(" + transmissionIDCol + "))";
					
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
//		String add = "INSERT INTO " + carsTable + " (" + 
//					vinNumberIDCol + 
//					licensePlateCol +
//					brandCol +
//					modelCol +
//					categoryCol +
//					colorCol +
//					fuelTypeCol +
//					transmissionCol +
//					manufactureDateCol +
//					actualKMCol +
//					engineSizeCol +
//					enginePowerCol + 
//					isOnRentCol + 
//					") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String add = "INSERT INTO " + carsTable + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
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
			
			pstmt.executeUpdate();
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
	
	
	
	public static ArrayList<Car> readCarsTable() throws SQLException{
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  ArrayList<Car> cars = new ArrayList<>();
		  try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + carsTable);
			while(rs.next()) {
				java.sql.Date sqlDate = rs.getDate(manufactureDateCol);
				java.time.LocalDate locDate = sqlDate.toLocalDate();
	
				Car car = new Car(rs.getString(vinNumberIDCol), 
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
	
	
	
	
	public static boolean checkExistingCar(String vinID, String licPlateID) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+ carsTable +
								   " WHERE " + vinNumberIDCol + " = '" + vinID + "' OR " + 
									licensePlateCol + " = '" + licPlateID + "'");
		    if(rs.next()) {
		    	return true;
		    }
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
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String readCarTableOUT(String licPlateID) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String all = null;
		try {
			conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+ carsTable +
								   " WHERE " + licensePlateCol + " = '" + licPlateID + "'");
		    if(rs.next()) {
		    	all = rs.getString(enginePowerCol);
		    }
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
		return all;
	}
	
	
	
}
