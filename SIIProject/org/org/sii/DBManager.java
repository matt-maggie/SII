package org.sii;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DBManager {
	public DBManager(){}

	public static void  createTable()
	{
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:testRimozione.db");
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			String sql = "CREATE TABLE UTENTICOPY " +
					"(ID TEXT PRIMARY KEY     NOT NULL," +
					" NUMBER          INT    NOT NULL )"; 
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public void copyTable() throws Exception{
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		conn.setAutoCommit(false);
		Statement stat = conn.createStatement();
		String selectSQL = "SELECT * FROM UTENTI2";
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			insertUserTab2(rs.getString("id"),rs.getInt("number"));

		}
		System.out.println("tabella copiata");

		stat.close();
		rs.close();
		conn.close();
	}
	/* estae gli utenti "interessati*/

	public Map<String,Integer> userExtractor() throws Exception{
		Class.forName("org.sqlite.JDBC");
		Map<String,Integer> userExtractor = new HashMap<String,Integer>();;
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test2.db");
		conn.setAutoCommit(false);

		Statement stat = conn.createStatement();
		String selectSQL = "SELECT ID,NUMBER FROM utenti1 WHERE NUMBER >=30";
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			userExtractor.put(rs.getString("id"),rs.getInt("NUMBER"));

		}
		System.out.println("estrazione completata");
		stat.close();
		rs.close();
		conn.close();
		return userExtractor;


	}
	/* estrae gli utenti disinteressati*/
	public Map<String,Integer> notInteresteduserExtractor() throws Exception{
		Class.forName("org.sqlite.JDBC");
		Map<String,Integer> userExtractor = new HashMap<String,Integer>();;
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test2.db");
		conn.setAutoCommit(false);

		Statement stat = conn.createStatement();
		String selectSQL = "SELECT ID,NUMBER FROM utenti1 WHERE NUMBER <=5";
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			userExtractor.put(rs.getString("id"),rs.getInt("NUMBER"));

		}
		System.out.println("estrazione completata");
		stat.close();
		rs.close();
		conn.close();
		return userExtractor;


	}
/* inserisci un utente nella tabella, nella fase di retrieve*/
	public void insertUser(String id, int number) throws Exception{
		if (!isHere(id)){
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			PreparedStatement prep = conn.prepareStatement("insert into utenti2 values (?, ?);");
			prep.setString(1, id);
			prep.setInt(2, number);
			prep.addBatch();
			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			System.out.println("inserito");
			conn.close();

		}
	}


	public void insertUserTab2(String id, int number) throws Exception{
		if (!isHere(id)){
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:testRimozione.db");
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			PreparedStatement prep = conn.prepareStatement("insert into utenticopy values (?, ?);");
			prep.setString(1, id);
			prep.setInt(2, number);
			prep.addBatch();
			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			System.out.println("gne");
			conn.close();
		}
	}

	public void sortMap(final Map<String, Integer> unsortMap) {
		List<String> list = new ArrayList<String>(unsortMap.keySet());		
		Comparator<String> cmp = new Comparator<String>() {
			@Override
			public int compare(String a1, String a2) {
				Integer value1 = unsortMap.get(a1);
				Integer value2 = unsortMap.get(a2);
				return value1.compareTo(value2);
			}
		};
		Collections.sort(list, Collections.reverseOrder(cmp));
	}

/* controlla se l'utente è già contenuto nel db*/
	public boolean isHere(String id) throws SQLException, ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		boolean isHere = false;
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		conn.setAutoCommit(false);
		Statement stat = conn.createStatement();
		String selectSQL = "SELECT ID FROM UTENTI2 WHERE ID = ?";
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
		preparedStatement.setString(1,id);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs != null && rs.next()){
			isHere = true;
			System.out.println("gia essere");
		}
		stat.close();
		rs.close();
		conn.close();
		return isHere;
	}


	public void deleteUser(String id) throws Exception{
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test2.db");
		conn.setAutoCommit(false);
		Statement stat = conn.createStatement();
		PreparedStatement prep = conn.prepareStatement("DELETE FROM utenti1 WHERE ID=?");
		prep.setString(1, id);
		prep.addBatch();
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
		System.out.println("rimosso");
		conn.close();

	}
}
