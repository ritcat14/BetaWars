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
    private String[] vehicleFields;
	
	public DatabaseManager() {
		t = new Thread(this, "Database");
		running = true;
		t.start();
		initFields();
		boolean created = Tools.exists("C:\\Users\\ritca\\eclipse-workspace\\Server\\GameDB");
		if (!created) create();
		else {
			openDatabase();
		}
	}
	
	private void initFields() {
		playerFields = new String[getColumnNum("Player")];
		mapFields = new String[getColumnNum("Map")];
		baseFields = new String[getColumnNum("Base")];
		vehicleFields = new String[getColumnNum("Vehicle")];
		
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
		
		vehicleFields[0] = "VehicleID";
		vehicleFields[1] = "PlayerID";
		vehicleFields[2] = "MapID";
		vehicleFields[3] = "Health";
		vehicleFields[4] = "xLocation";
		vehicleFields[5] = "yLocation";
		vehicleFields[6] = "AnimationFrame";
	}
	
	public String[] getFields(String table) {
		switch(table) {
		case "Player":
			return playerFields;
		case "Map":
			return mapFields;
		case "Base":
			return baseFields;
		case "Vehicle":
			return vehicleFields;
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
			case "Vehicle":
				addToVehicle(Tools.StringArrayToString(data, ","));
				break;
		}
	}
	
	public void updateTable(String table, String field, String data) {
		openDatabase();
		try {
			String sql = "UPDATE " + table + " SET " + field + " = " + data + ";";
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
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
	        System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
        closeDatabase();
	}
	
	private void addToVehicle(String data) {
		openDatabase();
		try{
			String sql = "INSERT INTO Vehicle(" + Tools.StringArrayToString(Arrays.copyOfRange(vehicleFields, 1, vehicleFields.length), ",") + ") VALUES(" + data + ")";
	        System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
        closeDatabase();
	}
	
	private void create() {
		try{
			openDatabase();
	        
			final String intToken = "int";
			final String booleanToken = "boolean";
			final String stringToken = "varchar(15)";
			String sep = ",";
			// Arrays.copyOfRange(int\[\] original, int from, int to)
	        String sql = ("CREATE TABLE Player("
	        		+ "PlayerID int NOT NULL,"
	        		+ Tools.createSQLFields(Arrays.copyOfRange(playerFields, 1, playerFields.length - 1), sep, intToken)
	        		+ Tools.createSQLFields(Arrays.copyOfRange(playerFields, playerFields.length - 1, playerFields.length), sep, stringToken)
	        		+ "PRIMARY KEY (PlayerID)"
	        		+ ")");
	        System.out.println(sql);
	        stmt.execute(sql);
	        
	        sql = ("CREATE TABLE Map("
	        + Tools.createSQLFields2(mapFields, sep, intToken)
	        + ")");
	        System.out.println(sql);
	        stmt.execute(sql);
	        
	        sql = ("CREATE TABLE Base("
	        		+ "BaseID int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
	        		+ Tools.createSQLFields(Arrays.copyOfRange(baseFields, 1, 3), sep, intToken)
	        		+ Tools.createSQLFields(Arrays.copyOfRange(baseFields, 3, 4), sep, stringToken)
	        		+ Tools.createSQLFields(Arrays.copyOfRange(baseFields, 4, 14), sep, intToken)
	        		+ Tools.createSQLFields(Arrays.copyOfRange(baseFields, 14, baseFields.length), sep, booleanToken)
	        		+ "PRIMARY KEY (BaseID))");
	        System.out.println(sql);
	        stmt.execute(sql);
	        
	        sql = ("CREATE TABLE Vehicle("
	        		+ "VehicleID int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
	        		+ Tools.createSQLFields(Arrays.copyOfRange(vehicleFields, 1, vehicleFields.length), sep, intToken)
	        		+ " FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID), PRIMARY KEY (VehicleID)"
	        		+ ")");
	        System.out.println(sql);
	        stmt.execute(sql);
	        
	        closeDatabase();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public int getColumnNum(String table) {
		switch(table) {
		case "Player":
			return 3;
		case "Map":
			return 14;
		case "Base":
			return 19;
		case "Vehicle":
			return 7;
		}
		return 0;
	}

	public Object[][] getTableData(String table) {
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
	
	@Override
	public void run() {
		while (running) {
			try {
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
