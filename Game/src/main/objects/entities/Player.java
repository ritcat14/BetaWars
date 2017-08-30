package main.objects.entities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.Main;
import main.events.types.KeyPressedEvent;
import main.events.types.KeyReleasedEvent;
import main.events.types.MouseDraggedEvent;
import main.events.types.MouseMovedEvent;
import main.events.types.MouseReleasedEvent;
import main.objects.Entity;
import main.objects.Map;

public class Player extends Entity {
	
	private int xa, ya = 0;
	private int xSpeed = Map.TILE_WIDTH;
	private int ySpeed = Map.TILE_HEIGHT;
	private int oldX = -1, oldY = -1;
	
	public Player(int ID, int Titanium, int xa, int ya) {
		super(0, 0, 0, 0, Color.BLACK);
		this.x = (Main.getWidth() / 2);
		this.y = (Main.getHeight() / 2);
		this.xa = -(xa * Map.TILE_WIDTH);
		this.ya = -(ya * Map.TILE_HEIGHT);
	}

	@Override
	public void update() {
	}
	
	public boolean mouseDragged(MouseDraggedEvent e) {
		if (e.getButton() == 3) {
			int newX = e.getX();
			int newY = e.getY();

			if (oldX == -1) {
				oldX = newX; // save "old" new position
				oldY = newY;
				return true; // first call do nothing else
			}
			// ok mouse moved
			xa -= oldX - newX;
			ya -= oldY - newY;
			// save new old positions
			oldX = newX;
			oldY = newY;
			return true;
		}
		return false;
	}
	
	public boolean mouseReleased(MouseReleasedEvent e) {
		oldX = -1;
		oldY = -1;
		return true;
	}
	
	public void setXa(int xa) {
		this.xa = xa;
	}
	
	public void setYa(int ya) {
		this.ya = ya;
	}
	
	public boolean keyPressed(KeyPressedEvent e) {
		switch(e.getKey()) {
		case KeyEvent.VK_UP:
			ya+=ySpeed;
			return true;
		case KeyEvent.VK_LEFT:
			xa+=xSpeed;
			return true;
		case KeyEvent.VK_DOWN:
			ya-=ySpeed;
			return true;
		case KeyEvent.VK_RIGHT:
			xa-=xSpeed;
			return true;
		}
		return false;
	}
	
	public int getXa() {
		return xa;
	}
	
	public int getYa() {
		return ya;
	}

}
