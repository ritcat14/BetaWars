package main.objects.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import main.DatabaseManager;
import main.Tools;
import main.events.Event;
import main.events.EventListener;
import main.graphics.Label;
import main.objects.Entity;
import main.objects.Map;
import main.objects.entities.base.*;

public class Base extends Entity implements EventListener {
	
	private String type;
	private boolean owned;
	private int xPos, yPos;
	private Object[] data;
	private DatabaseManager dm;
	
	private Garage garage;
	private Hangar hanger;
	private Flag flag;
	private Label nameLabel;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private final int playerID, mapID, baseID;
	
	public Base(Object[] data, DatabaseManager dm, int mapX, int mapY, String flag) {
		super((mapX + (Integer.parseInt((String)data[4]) * Map.TILE_WIDTH)), (mapY + (Integer.parseInt((String)data[5]) * Map.TILE_HEIGHT)),
				Tools.getImage("/entities/base/" + ((((String)data[3]).equals("Homebase") ? "Type1" : (String)data[3]))));
		this.dm = dm;
		this.data = data;
		this.baseID = Integer.parseInt((String)data[0]);
		this.playerID = Integer.parseInt((String)data[1]);
		this.mapID = Integer.parseInt((String)data[2]);
		this.xPos = Integer.parseInt((String)data[4]) * Map.TILE_WIDTH;
		this.yPos = Integer.parseInt((String)data[5]) * Map.TILE_HEIGHT;
		this.data = data;
		this.type = ((String)data[3]).equals("Homebase") ? "Type1" : (String)data[3]; // Type1, Type2, Helibase, Airbase
		
		setup(flag);
		
		entities.add(new Wall(this.x, this.y, this.type));
	}

	private void setup(String flag) {
		String[] baseData = Tools.loadFromResource("/gameData/basetypes.gme");
		for (String s : baseData) { 											// For each line in the file
			String[] parts = s.split(";");										// Split it into 2 parts, the base type and the data
			if (parts[0].equals(type)) {										// If we are that base...
				String[] objects = parts[1].split(",");							// Split the data part into objects
				String[] dataParts = new String[objects.length];				// Create an array to store the data
				for (int i = 0; i < objects.length; i++) {						// Iterate through the objects
					dataParts[i] = objects[i].split(":")[1];					// Split the data into object name and data, and store the data in the list
				}
				switch (type) {
				case "Type1":
				case "Type2":
				case "Helibase":
					String[] garageData = dataParts[2].split("/");
					entities.add(garage = new Garage(this, Integer.parseInt(garageData[0]), Integer.parseInt(garageData[1]), type));
					String[] factoryData = dataParts[3].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[7]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Steel"));
					factoryData = dataParts[4].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[8]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Carbon"));
					factoryData = dataParts[5].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[9]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Fuel"));
					factoryData = dataParts[6].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[6]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Concrete"));
					String[] flagData = dataParts[1].split("/");
					String[] flagPoleData = dataParts[0].split("/");
					entities.add(this.flag = (Flag) new Flag(Integer.parseInt(flagData[0]), Integer.parseInt(flagData[1]), Integer.parseInt(flagPoleData[0]), Integer.parseInt(flagPoleData[1]), flag, 0).play());
					if (((String)data[3]).equals("Homebase")) entities.add(new Flag(Integer.parseInt(flagData[2]), Integer.parseInt(flagData[3]), Integer.parseInt(flagPoleData[0]), Integer.parseInt(flagPoleData[1]), flag, 17).play());			
					break;
				case "Airbase":
					garageData = dataParts[2].split("/");
					entities.add(garage = new Garage(this, Integer.parseInt(garageData[0]), Integer.parseInt(garageData[1]), type));
					factoryData = dataParts[4].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[7]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Steel"));
					factoryData = dataParts[5].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[8]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Carbon"));
					factoryData = dataParts[6].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[9]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Fuel"));
					factoryData = dataParts[7].split("/");
					entities.add(new Factory(this, (flag.equals("green")), Integer.parseInt((String)data[6]), baseID, playerID, mapID, dm, Integer.parseInt(factoryData[2]), Integer.parseInt(factoryData[3]), Integer.parseInt(factoryData[4]), Integer.parseInt(factoryData[5]), Integer.parseInt(factoryData[0]), Integer.parseInt(factoryData[1]), "Concrete"));
					flagData = dataParts[1].split("/");
					flagPoleData = dataParts[0].split("/");
					entities.add(this.flag = (Flag) new Flag(Integer.parseInt(flagData[0]), Integer.parseInt(flagData[1]), Integer.parseInt(flagPoleData[0]), Integer.parseInt(flagPoleData[1]), flag, 0).play());
					String[] hangarData = dataParts[3].split("/");
					entities.add(hanger = new Hangar(this, Integer.parseInt(hangarData[0]), Integer.parseInt(hangarData[1])));
					break;
				}
			}
		}
		Object[][] playerData = dm.getData("Player");
		for (Object[] o : playerData) {
			int ID = Integer.parseInt((String)o[0]);
			if (ID == playerID) {
				nameLabel = new Label((String)o[2], garage.getX(), garage.getY(), 100, 50, Color.WHITE, new Font("New Times Java", Font.BOLD, 30));
				nameLabel.setVisible(true);
			}
		}
	}
	
	@Override
	public void update() {
		for (Entity e : entities) {
			if (e.equals(garage) || e.equals(hanger) || e.equals(flag)) continue;
			e.update();
		}
		garage.update();
		if (hanger != null) hanger.update();
		flag.update();
		Object[][] baseData = dm.getTableData("Base");
		for (Object[] o : baseData) {
			if (Integer.parseInt((String)o[0]) == baseID) {
				data = o;
				break;
			}
		}
		if (nameLabel != null) nameLabel.update();
	}
	
	public boolean isProducing() {
		for (Entity e : entities) {
			if (e instanceof Factory) {
				if (((Factory)e).isProducing()) return true;
			}
		}
		return false;
	}
	
	public void buttonEvent(int index) {
		if (index > 2 && index <= 28) {
			// Create tank
			return;
		} else if (index > 28 && index <= 34) {
			// Create helicopter
			return;
		} else if (index > 35) {
			// Create aeroplane
			return;
		}
		switch(index) {
		case 0:
			// Build defence
			break;
		case 1:
			// Install wall
			break;
		case 2:
			// Advanced missiles
			break;
		case 35:
			// Harrier
			break;
		}
	}
	
	public Flag getFlag() {
		return flag;
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		for (Entity e : entities) {
			if (e.equals(garage) || e.equals(hanger) || e.equals(flag)) continue;
			e.render(g);
		}
		garage.render(g);
		if (hanger != null) hanger.render(g);
		flag.render(g);
		if (nameLabel != null) nameLabel.render(g);
		garage.getBox().render(g);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x + xPos);
		for (Entity e : entities) e.setX(x + xPos);
		if (nameLabel != null && garage != null) nameLabel.setPosition(garage.getX(), garage.getY());
	}
	
	@Override
	public void setY(int y) {
		super.setY(y + yPos);
		for (Entity e : entities) e.setY(y + yPos);
		if (nameLabel != null && garage != null) nameLabel.setPosition(garage.getX(), garage.getY());
	}

	@Override
	public void onEvent(Event event) {
		for (Entity e : entities) {
			if (e instanceof EventListener) {
				((EventListener) e).onEvent(event);
			}
		}
	}

}
