package wifi.agardi.fmsproject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	public static final String DBlocation = "DatabaseTestNew";
	public static final String connString = "jdbc:derby:" + DBlocation +";create=true";

	public static final String usersTable = "UsersNew";
	public static final String userNameCol = "Username";
	public static final String passwordCol = "Password";
	
	
	
//USER TABLE	
	public static void createUsersTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String create = "CREATE TABLE " + usersTable + "(" +
				         userNameCol + " VARCHAR(50), " +
				         passwordCol + " VARCHAR(50))";
		try {
			conn = DriverManager.getConnection(connString);
			System.out.println("Connection is successfully established");
			rs = conn.getMetaData().getTables(null, null, usersTable.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			 
			stmt = conn.createStatement();
			stmt.executeUpdate(create);
					
		} catch (SQLException e) {
			System.out.println("Something is wrong with the database connection...");
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
			System.out.println("Connection is successfully established");
			
		} catch (SQLException e) {
			System.out.println("Something is wrong with the database connection...");
			e.printStackTrace();
		}
		
		if(conn != null) {
		try {
			stmt = conn.createStatement();
			System.out.println("Statement is successfully established");
				
		} catch (SQLException e) {
			System.out.println("Something is wrong with the creation of Statement...");
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
			System.out.println("Something is wrong after checking");
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
	

		
		
}
