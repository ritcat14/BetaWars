package main.graphics;

import java.awt.Color;
import java.awt.Graphics;

import main.Main;
import main.Tools;
import main.events.Event;
import main.events.Event.Type;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.MousePressedEvent;
import main.objects.Map;
import main.objects.entities.Base;
import main.objects.entities.Player;

public class MiniMap extends Panel implements EventListener {
	
	private Player player;

	public MiniMap(Player player) {
		super(Main.getWidth() - 300, 0, 300, 300, Tools.getImage("/game/background"));
		this.player = player;
	}
	
	public void add(Base e) {
		String flag = ((Base) e).getFlag().getColor();
		Color c = Color.BLACK;
		switch (flag) {
		case "green":
			c = Color.GREEN;
			break;
		case "yellow":
			c = Color.YELLOW;
			break;
		case "red":
			c = Color.RED;
			break;
		case "blue":
			c = Color.BLUE;
			break;
		}
		int baseX = (e.getMapX() * Map.TILE_WIDTH) / 100;
		int baseY = (e.getMapY() * Map.TILE_HEIGHT) / 100;
		add(new Panel(baseX, baseY, 5, 5, c));
	}
	
	public void clear() {
		components.clear();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void drawPointer(Graphics g, int playerX, int playerY) {
		g.setColor(Color.WHITE);
		g.drawRect(x + (playerX + 3), y + (playerY + 3), 6, 6);
		/*1*/ g.drawLine(x, playerY + 6, x + playerX + 3, playerY + 6);
		/*2*/ g.drawLine(x + playerX + 6, 0, x + playerX + 6, playerY + 3);
		/*3*/ g.drawLine(x + playerX + 9, playerY + 6, Main.getWidth(), playerY + 6);
		/*4*/ g.drawLine(x + playerX + 6, y + playerY + 9, x + playerX + 6, 300);
	}
	
	private boolean mousePressed(MousePressedEvent e) {
		if (this.getBounds().contains(e.getX(), e.getY())) {
			int clickX = (e.getX() - (Main.getWidth() - 300)) / 3;
			int clickY = e.getY();
			player.setXa(-(clickX * Map.TILE_WIDTH) + 300);
			player.setYa(-(clickY * Map.TILE_HEIGHT) + 100);
			return true;
		}
		return false;
	}

	@Override
	public void onEvent(Event event) {
		new EventDispatcher(event).dispatch(Type.MOUSE_PRESSED, new EventHandler() {
			@Override
			public boolean onEvent(Event event) {
				return mousePressed((MousePressedEvent)event);
			}
		});
	}

}
