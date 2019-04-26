package wifi.agardi.fmsproject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Database {
	public static final String DBlocation = "DatabaseTestNew";
	public static final String connString = "jdbc:derby:" + DBlocation +";create=true";

	public static final String usersTable = "UsersNew";
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
	
	
	
//USERS TABLE	
	public static void createUsersTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + usersTable + "(" +
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
				categories.put("M-Mini", 30);
				categories.put("A-Small", 40);
				categories.put("B-Economy", 50);
				categories.put("C-Midsize", 60);
				categories.put("D-Fullsize", 75);
				categories.put("F-Premium", 95);
				categories.put("P-Luxus", 125);
				categories.put("S-Minivan", 105);
				categories.put("V-FullsizeVan", 125);
				categories.put("R-Convertible", 150);
				categories.put("X-SUV", 140);
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
	
	
	
	
	
	
	
	
	
	
}
