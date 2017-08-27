package main.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.DatabaseManager;
import main.Main;
import main.Tools;
import main.events.Event;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.Mouse;
import main.events.types.KeyPressedEvent;
import main.events.types.KeyReleasedEvent;
import main.events.types.MouseDraggedEvent;
import main.events.types.MouseMovedEvent;
import main.events.types.MousePressedEvent;
import main.events.types.MouseReleasedEvent;
import main.objects.entities.Base;
import main.objects.entities.Player;

public class Map implements EventListener {

	public static final int TILE_WIDTH = 300;
	public static final int TILE_HEIGHT = 100;
	
	private final int MAX_BASES; 
	
	private TileManager tileManager;
	private int tileNumX, tileNumY;
	private int x, y;
	private int[] tiles;
	private DatabaseManager dm;
	private int ID;
	private Player player;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private int mouseDX, mouseDY;
	
	private ArrayList<Integer> usedXCoords;
	private ArrayList<Integer> usedYCoords;
	
	public Map(DatabaseManager dm, int ID, int x, int y) {
		this.ID = ID;
		this.dm = dm;
		this.x = -(x * TILE_WIDTH);
		this.y = -(y * TILE_HEIGHT);
		
		tileManager = new TileManager();
		
		String[] tileData = Tools.loadFromResource("/map/map.gme");
		
		tileNumX = tileData[0].split(",").length;
		tileNumY = tileData.length;
		tiles = new int[tileNumX * tileNumY];
		MAX_BASES = Tools.getBaseNum(tileNumX * TILE_WIDTH, tileNumY * TILE_HEIGHT, 1500, 550);
		usedXCoords = new ArrayList<Integer>();
		usedYCoords = new ArrayList<Integer>();
		
		for (int y0 = 0; y0 < tileData.length; y0++) {
			int[] rowData = Tools.StringToIntArray(tileData[y0].split(","));
			for (int x0 = 0; x0 < rowData.length; x0++) {
				tiles[x0 + y0 * tileNumX] = rowData[x0];
			}
		}
		generateBase();
	}
	
	public void setPlayer(Player player) {
		if (this.player != null) entities.remove(player);
		this.player = player;
		entities.add(player);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
	
	public void add(ArrayList<Entity> e) {
		entities.addAll(e);
	}
	
	public void createBase(Object[] data, DatabaseManager dm, int mapX, int mapY, String flag) {
		entities.add(new Base(data, dm, mapX, mapY, flag));
	}
	
	private void generateBase() {
		String flag = "yellow";
		int mapX = Tools.RandomNumber(0, (tileNumX * TILE_WIDTH) - 1500);
		int mapY = Tools.RandomNumber(0, (tileNumY * TILE_HEIGHT) - 535);
		for (int x = 0; x < usedXCoords.size(); x++) {
			for (y = 0; y < usedYCoords.size(); y++) {
				if (usedXCoords.get(x) == mapX && usedYCoords.get(y) == mapY) {
					generateBase();
				}
				Rectangle r = new Rectangle(usedXCoords.get(x), usedXCoords.get(y), 1500, 535);
				if (r.contains(mapX, mapY)) {
					generateBase();
				}
			}
		}
		usedXCoords.add(mapX);
		usedYCoords.add(mapY);
		mapX /= TILE_WIDTH;
		mapY /= TILE_HEIGHT;
		
		// Generate database data
		String[] data = {"0", "" + ID, "'Type1'", "" + mapX, "" + mapY, "1", "1", "1", "1", "0", "0", "0", "2", "false", "false", "false", "false", "false"};
		// save it
		dm.add("Base", data);
		//load it into the new base
		Object[][] baseData = dm.getData("Base");
		entities.add(new Base(baseData[baseData.length - 1], dm, mapX, mapY, flag));
	}
	
	public void clear() {
		usedXCoords = new ArrayList<Integer>();
		usedYCoords = new ArrayList<Integer>();
		
	}
	
	public Tile getTile(int x, int y) {
		Tile tile = tileManager.getTile(tiles[x + y * tileNumX]);
		return tile;
	}
	
	public void update() {
		this.x = player.getXa();
		this.y = player.getYa();
		for (Entity e : entities) {
			if (!e.equals(player)) {
				e.setX(this.x);
				e.setY(this.y);
			}
			e.update();
		}
	}
	
	public void render(Graphics g) {
		for (int x1 = 0; x1 < tileNumX; x1++) {
			for (int y1 = 0; y1 < tileNumY; y1++) {
				int x0 = x + (x1 * TILE_WIDTH);
				int y0 = y + (y1 * TILE_HEIGHT);
				if (x0 > Main.getWidth() + TILE_WIDTH || x0 < -TILE_WIDTH || y0 < -TILE_HEIGHT || y0 > Main.getHeight() + TILE_HEIGHT) continue; // Don't render tiles that don't need to be rendered
				Tile t = tileManager.getTile(tiles[x1 + y1 * tileNumX]);
				Tile tile = t.createTile(x0, y0);
				tile.render(g);
			}
		}
		for (Entity e : entities) e.render(g);
	}

	@Override
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.KEY_PRESSED, new EventHandler() {
			public boolean onEvent(Event event) {
				return player.keyPressed((KeyPressedEvent)event);
			}
		});
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, new EventHandler() {
			public boolean onEvent(Event event) {
				return player.mouseReleased((MouseReleasedEvent)event);
			}
		});
		dispatcher.dispatch(Event.Type.MOUSE_DRAGGED, new EventHandler() {
			public boolean onEvent(Event event) {
				return player.mouseDragged((MouseDraggedEvent)event);
			}
		});
		for (Entity e : entities) {
			if (e instanceof Base) {
				Base b = (Base)e;
				b.onEvent(event);
			}
		}
	}
	
}
