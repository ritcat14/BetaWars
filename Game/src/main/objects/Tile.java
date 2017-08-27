package main.objects;

import java.awt.image.BufferedImage;

public class Tile extends Entity {
	
	public final int ID;
	public static final int TILE_WIDTH = Map.TILE_WIDTH;
	public static final int TILE_HEIGHT = Map.TILE_HEIGHT;

	public Tile(int ID, int x, int y, BufferedImage image) {
		super(x, y, TILE_WIDTH, TILE_HEIGHT, image);
		this.ID = ID;
	}
	
	public Tile createTile(int x, int y) {
		return new Tile(ID, x, y, image);
	}
	
	public int getID() {
		return ID;
	}

	@Override
	public void update() {}	

}
