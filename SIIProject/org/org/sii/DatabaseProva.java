package org.sii;
import java.sql.*;

public class DatabaseProva {
	/*
	 public static void main(String[] args) throws Exception {
		    Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
			conn.setAutoCommit(false);

		    Statement stat = conn.createStatement();
		   // stat.executeUpdate("drop table if exists people;");
//stat.executeUpdate("create table people (name, occupation);");
		    PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?);");



		prep.setString(1, "ciccio");
		prep.setString(2, "paduccio");
		prep.addBatch();
	
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from people;");
		while (rs.next()) {
		  System.out.println("name = " + rs.getString("name"));
		  System.out.println("job = " + rs.getString("occupation"));
		}
		rs.close();
		conn.close();


		}
		}
		  */
		  public static void main( String args[] )throws Exception{
		 DBManager c = new DBManager();
		 c.deleteUser("372835");
		//c.createTable();
		//c.insertUserTab2("PROVA1", 1);
		// c.copyTable();
		// c.userExtractor()
		    /*Connection c = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:test.db");
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      String sql = "CREATE TABLE Uzzi" +
		                   "(ID TEXT PRIMARY KEY     NOT NULL," +
		                   " NUMBERS         TEXT    NOT NULL )"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		    System.out.println("Table created successfully");
		*/  
		  }}
//}*/
