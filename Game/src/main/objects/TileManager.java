package main.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Tools;

public class TileManager {
	
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	public TileManager() {
		String[] tileData = Tools.loadFromResource("/map/tiles.gme");
		
		for (int i = 0; i < tileData.length; i++) {
			String s = tileData[i];
			int ID = Integer.parseInt(s.split(":")[0]);
			BufferedImage image = Tools.getImage("/tiles/" + ID);
			
			tiles.add(new Tile(ID, 0, 0, image));
		}
	}
	
	public Tile getTile(int ID) {
		for (Tile t : tiles) {
			if (t.getID() == ID) return t;
		}
		return tiles.get(0);
	}

}
