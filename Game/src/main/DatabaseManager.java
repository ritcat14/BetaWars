package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager implements Runnable {
	
	private final String DB_URL = "jdbc:derby:C:\\Users\\ritca\\eclipse-workspace\\Server\\GameDB;create=true";
    private Connection conn;
    private Statement stmt;
    private Thread t;
    private boolean running = true;
    private String[] playerFields;
    private String[] mapFields;
    private String[] baseFields;
    
    private Object[][] playerData;
    private Object[][] baseData;
    private Object[][] mapData;
	
	public DatabaseManager() {
		t = new Thread(this, "Database");
		running = true;
		t.start();
		initFields();
		openDatabase();
	}
	
	private void initFields() {
		playerFields = new String[getColumnNum("Player")];
		mapFields = new String[getColumnNum("Map")];
		baseFields = new String[getColumnNum("Base")];
		
		playerFields[0] = "PlayerID";
		playerFields[1] = "Titanium";
		playerFields[2] = "PlayerName";
		
		mapFields[0] = "MapID";
		mapFields[1] = "PlayerID";
		mapFields[2] = "Concrete";
		mapFields[3] = "Steel";
		mapFields[4] = "Carbon";
		mapFields[5] = "Fuel";
		mapFields[6] = "Economy";
		mapFields[7] = "EconomyMax";
		mapFields[8] = "Military";
		mapFields[9] = "MilitaryMax";
		mapFields[10] = "Base";
		mapFields[11] = "BaseMax";
		mapFields[12] = "GroupCount";
		mapFields[13] = "GroupMax";

		baseFields[0] = "BaseID";
		baseFields[1] = "PlayerID";
		baseFields[2] = "MapID";
		baseFields[3] = "Type";
		baseFields[4] = "xLocation";
		baseFields[5] = "yLocation";
		baseFields[6] = "ConcreteLevel";
		baseFields[7] = "SteelLevel";
		baseFields[8] = "CarbonLevel";
		baseFields[9] = "FuelLevel";
		baseFields[10] = "MinistryLevel";
		baseFields[11] = "MilitaryLevel";
		baseFields[12] = "CommandLevel";
		baseFields[13] = "DefenseLevel";
		baseFields[14] = "AdvancedWall";
		baseFields[15] = "AdvancedMissiles";
		baseFields[16] = "Radar";
		baseFields[17] = "Jammer";
		baseFields[18] = "Detector";
	}
	
	public String[] getFields(String table) {
		switch(table) {
		case "Player":
			return playerFields;
		case "Map":
			return mapFields;
		case "Base":
			return baseFields;
		}
		return new String[0];
	}
	
	private void openDatabase() {
        try {
			conn = DriverManager.getConnection(DB_URL);
	        stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void closeDatabase() {
        try {
			stmt.close();
	        conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String table, String[] data) {
		switch(table) {
			case "Player":
				addToPlayer(Tools.StringArrayToString(data, ","));
				break;
			case "Map":
				addToMap(Tools.StringArrayToString(data, ","));
				break;
			case "Base":
				addToBase(Tools.StringArrayToString(data, ","));
				break;
		}
	}
	
	public void updateTable(String table, String field, String data, String... conditions) {
		openDatabase();
		String sql = "";
		try {
			if (conditions.length == 0) {
				sql = "UPDATE " + table + " SET " + field + " = " + data + ";";
			} else {
				sql = "UPDATE " + table + " SET " + field + " = CASE WHEN ("
				+ Tools.StringArrayToString(conditions, " AND ") + ") THEN " + data
				+ " ELSE " + field + " END";
			}
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();}
		closeDatabase();
	}
	
	private void addToPlayer(String data) {
		openDatabase();
		try{
			String sql = "INSERT INTO Player(" + Tools.StringArrayToString(playerFields, ",") + ") VALUES(" + data + ")";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
        closeDatabase();
	}
	
	private void addToMap(String data) {
		openDatabase();
		try{
			String sql = "INSERT INTO Map(" + Tools.StringArrayToString(mapFields, ",") + ") VALUES(" + data + ")";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
        closeDatabase();
	}
	
	private void addToBase(String data) {
		openDatabase();
		try{
			String sql = "INSERT INTO Base(" + Tools.StringArrayToString(Arrays.copyOfRange(baseFields, 1, baseFields.length), ",") + ") VALUES(" + data + ")";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
        closeDatabase();
	}
	
	public int getColumnNum(String table) {
		switch(table) {
		case "Player":
			return 3;
		case "Map":
			return 14;
		case "Base":
			return 19;
		}
		return 0;
	}
	
	public Object[][] getData(String table) {
		openDatabase();
		String query = "select * from " + table;
		List<Object[]> rows = new ArrayList<Object[]>();

		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			while (rs.next()) 
			{
			   // create a list holding the values for a single row
			   Object[] columnValues = new Object[getColumnNum(table)];
			   for (int i = 0; i < columnValues.length; i++) {
				   columnValues[i] = rs.getString(i + 1);
			   }
			   rows.add(columnValues);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        Object[][] convertedData = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
        	convertedData[i] = rows.get(i);
        }
        closeDatabase();
		return convertedData;
	}

	public Object[][] getTableData(String table) {
		switch (table) {
			case "Player":
				return playerData;
			case "Map":
				return mapData;
			case "Base":
				return baseData;
		}
		System.err.println("Cannot find table data for table: " + table);
		return null;
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				playerData = getData("Player");
				mapData = getData("Map");
				baseData = getData("Base");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
