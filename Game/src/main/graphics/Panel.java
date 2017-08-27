package main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Panel extends GuiComponent {
	
	protected int width, height;
	protected Color col;
	protected BufferedImage image;
	
	public Panel(int x, int y, int width, int height, Color c) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.col = c;
	}
	
	public Panel(int x, int y, int width, int height, BufferedImage image) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	public Panel(int x, int y, BufferedImage image) {
		this(x, y, image.getWidth(), image.getHeight(), image);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) {
			g.setColor(col);
			g.fillRect(x, y, width, height);
			if (image != null) g.drawImage(image, x, y, width, height, null);
			super.render(g);
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Color getColour() {
		return col;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
