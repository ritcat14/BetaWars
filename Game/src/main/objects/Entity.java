package main.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
 * An in game entity
 */

public abstract class Entity {
	
	protected int x, y, width, height;
	protected BufferedImage image = null;
	protected Color colour = Color.BLACK;
	
	public Entity(int x, int y, int width, int height, Color colour) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
	}
	
	public Entity(int x, int y, int width, int height, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
		this.colour = Color.BLACK;
	}
	
	public Entity(int x, int y, BufferedImage image) {
		this(x, y, image.getWidth(), image.getHeight(), image);
	}
	
	public abstract void update();
	
	public void render(Graphics g) {
		g.setColor(colour);
		if (image != null) g.drawImage(image, x, y, width, height, null);
		else g.fillRect(x, y, width, height);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public void setColour(Color colour) {
		this.colour = colour;
	}

}
