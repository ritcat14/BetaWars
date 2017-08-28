package main.state.states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.DatabaseManager;
import main.Main;
import main.StateHandler;
import main.StateHandler.States;
import main.ThreadHandler;
import main.Tools;
import main.events.Event;
import main.events.types.MousePressedEvent;
import main.graphics.Button;
import main.graphics.Label;
import main.graphics.Panel;
import main.objects.Entity;
import main.objects.Map;
import main.objects.entities.Base;
import main.objects.entities.Player;
import main.state.State;

/*
 * Represents the main game state
 */

public class Game extends State {
	
	private Panel sidePanel;
	private Panel resPanel;
	private Map map;
	private DatabaseManager databaseManager;
	private Label concrete, steel, carbon, fuel, titanium;
	private int playerID, mapID;
	private Button mapChange;
	
	private boolean paused = false;

	private String gameFolderUrl = System.getProperty("user.home") + "\\Game\\";
	private String playerUrl = gameFolderUrl + "Player.txt";
	
	private Object[][] playerData;
	private Object[][] mapData;
	private Object[][] baseData;
	private boolean switchingMap = false;
	private Start start;
	
	public Game(ThreadHandler th, DatabaseManager databaseManager) {
		super(th);
		this.start = (Start)StateHandler.getPreState();
		this.databaseManager = databaseManager;
	}
	
	public void getData() {
		playerData = databaseManager.getTableData("Player");
		mapData = databaseManager.getTableData("Map");
		baseData = databaseManager.getTableData("Base");
	}
	
	public void createGui() {
		sidePanel = new Panel(0, 50, 50, Main.getHeight() - 100, Color.GRAY);
		
		concrete = new Label("Concrete", 10, 10, 100, 10, Tools.hexToRgb("#FFDDAA")) {
			@Override
			public void update() {
				super.update();
				for (Object[] o : mapData) {
					int tempPlayerID = Integer.parseInt((String)o[1]);
					int tempMapID = Integer.parseInt((String)o[0]);
					if (tempPlayerID == playerID && mapID == tempMapID) setText("Concrete: " + (String)o[2]);
					else setText("Concrete: 0");
				}
			}
		};
		steel = new Label("Steel", 110, 10, 100, 10, Tools.hexToRgb("#FFAAAA")) {
			@Override
			public void update() {
				super.update();
				for (Object[] o : mapData) {
					int tempPlayerID = Integer.parseInt((String)o[1]);
					int tempMapID = Integer.parseInt((String)o[0]);
					if (tempPlayerID == playerID && mapID == tempMapID) setText("Steel: " + (String)o[3]);
					else setText("Concrete: 0");
				}
			}
		};
		carbon = new Label("Carbon", 210, 10, 100, 10, Tools.hexToRgb("#AAFFAA")) {
			@Override
			public void update() {
				super.update();
				for (Object[] o : mapData) {
					int tempPlayerID = Integer.parseInt((String)o[1]);
					int tempMapID = Integer.parseInt((String)o[0]);
					if (tempPlayerID == playerID && mapID == tempMapID) setText("Carbon: " + (String)o[4]);
					else setText("Concrete: 0");
				}
			}
		};
		fuel = new Label("Fuel", 310, 10, 100, 10, Tools.hexToRgb("#CC77FF")) {
			@Override
			public void update() {
				super.update();
				for (Object[] o : mapData) {
					int tempPlayerID = Integer.parseInt((String)o[1]);
					int tempMapID = Integer.parseInt((String)o[0]);
					if (tempPlayerID == playerID && mapID == tempMapID) setText("Fuel: " + (String)o[5]);
					else setText("Concrete: 0");
				}
			}
		};
		titanium = new Label("Titanium", 410, 10, 100, 10, Tools.hexToRgb("#AACCFF")) {
			@Override
			public void update() {
				super.update();
				for (Object[] o : playerData) {
					if (Integer.parseInt((String)o[0]) == playerID) setText("Titanium: " + (String)o[1]);
				}
			}
		};
		
		resPanel = new Panel(0, 0, 490, 50, Tools.getImage("/game/background"));
		resPanel.add(concrete);
		resPanel.add(steel);
		resPanel.add(carbon);
		resPanel.add(fuel);
		resPanel.add(titanium);
		
		mapChange = new Button(0, 0, 50, 50, Color.WHITE, "MAP") {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					StateHandler.changeState(States.MAP);
					return true;
				}
				return false;
			}
		};
		sidePanel.add(mapChange);
	}
	
	public void createPlayer() {
		Tools.createFolder(gameFolderUrl);
		Tools.createFile(playerUrl);
		int ID = playerData.length + 1;
		String[] data = {"" + ID, "1000", "'" + ((Start)start).getName() + "'"};
		String[] fileData  = {Tools.StringArrayToString(data, ",")};
		Tools.writeToFile(playerUrl, fileData);
		databaseManager.add("Player", data);
		
		String[] mData = {"1", "" + ID, "1000", "1000", "1000", "50000", "0", "1000", "0", "1000", "1", "10", "0", "3"};
		databaseManager.add("Map", mData);
		map = new Map(databaseManager, 1, 38, 12);
		
		map.setPlayer(new Player(ID, 1000, 38, 12));
		
		String[] bdata = {"" + ID, "1", "'Homebase'", "38", "12", "1", "1", "1", "1", "0", "0", "0", "2", "false", "false", "false", "false", "false"};
		databaseManager.add("Base", bdata);
		baseData = databaseManager.getData("Base");
		map.createBase(baseData[baseData.length - 1], databaseManager, -(38 * Map.TILE_WIDTH), -(12 * Map.TILE_HEIGHT), "green");
		this.mapID = 1;
	}
	
	public void loadPlayer() {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		String[] data = Tools.loadFromFile(playerUrl);
		int ID = Integer.parseInt(data[0].split(",")[0]);
		int mapX = 0, mapY = 0;
		for (Object[] base : baseData) {
			int playerID = Integer.parseInt((String)base[1]);
			String type = (String)base[3];
			if (playerID == ID) {
				// Create each of the players bases
				if (type.equals("Homebase")) {
					mapID = Integer.parseInt((String)base[2]);
					mapX = Integer.parseInt((String)base[4]);
					mapY = Integer.parseInt((String)base[5]);
				}
			}
		}
		
		for (Object[] base : baseData) {
			int playerID = Integer.parseInt((String)base[1]);
			int tempMapID = Integer.parseInt((String)base[2]);
			if (mapID == tempMapID) {
				if (playerID == ID) {
					entities.add(new Base(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "green")); 
				} else {
					if (playerID == 0) {
						entities.add(new Base(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "yellow"));
					} else {
						entities.add(new Base(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "red"));
					}
				}
			}
		}
		
		
		for (Object[] map : mapData) {
			int tempID = Integer.parseInt((String)map[0]);
			int tempPlayerID = Integer.parseInt((String)map[1]);
			if (tempID == mapID) {
				if (tempPlayerID == ID) {
					this.map = new Map(databaseManager, mapID ,mapX, mapY);
					this.map.add(entities);
				}
			}
		}

		for (Object[] player : playerData) {
			if (ID == Integer.parseInt((String)player[0])) {
				this.playerID = ID;
				map.setPlayer(new Player(ID, Integer.parseInt((String)player[1]), mapX, mapY));
			}
		}
	}
	
	public void changeMap(int mapID) {
		switchingMap = true;
		this.mapID = mapID;
	}
	
	private void switchMap() {
		map.clear();
		map.setID(mapID);
		int mapX = 0, mapY = 0;

		playerData = databaseManager.getData("Player");
		mapData = databaseManager.getData("Map");
		baseData = databaseManager.getData("Base");
		
		// Get all bases on the map, and load them. If any of them are hombases or player bases, set location to that base.
		
		for (Object[] base : baseData) {
			int tempMapID = Integer.parseInt((String)base[2]);
			if (mapID == tempMapID) {
				int tempPlayerID = Integer.parseInt((String)base[1]);
				if (tempPlayerID == playerID) {
					mapX = Integer.parseInt((String)base[4]);
					mapY = Integer.parseInt((String)base[5]);
					map.createBase(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "green");
				} else if (tempPlayerID == 0) {
					map.createBase(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "yellow");
				} else {
					map.createBase(base, databaseManager, -(mapX * Map.TILE_WIDTH), -(mapY * Map.TILE_HEIGHT), "red");
				}
			}
		}

		for (Object[] player : playerData) {
			if (playerID == Integer.parseInt((String)player[0])) {
				map.setPlayer(new Player(playerID, Integer.parseInt((String)player[1]), mapX, mapY));
			}
		}
		switchingMap = false;
	}

	@Override
	public void update() {
		if (!paused) {
			playerData = databaseManager.getTableData("Player");
			mapData = databaseManager.getTableData("Map");
			baseData = databaseManager.getTableData("Base");
			if (map != null && !switchingMap) map.update();
			sidePanel.update();
			resPanel.update();
		}
	}
	
	public void pause() {
		paused = true;
	}
	
	public void start() {
		paused = false;
	}
	
	public Start getStart() {
		return start;
	}

	@Override
	public void render(Graphics g) {
		if (!switchingMap) {
			if (map != null) map.render(g);
		} else {
			switchMap();
		}
		sidePanel.render(g);
		resPanel.render(g);
	}

	public void onEvent(Event event) {
		if (!paused) {
			map.onEvent(event);
			mapChange.onEvent(event);
		}
	}

}
