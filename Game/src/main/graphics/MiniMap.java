package main.graphics;

import java.awt.Color;

import main.Main;
import main.Tools;
import main.events.Event;
import main.events.Event.Type;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.MousePressedEvent;
import main.objects.Entity;
import main.objects.Map;
import main.objects.entities.Base;

public class MiniMap extends Panel implements EventListener {

	public MiniMap() {
		super(Main.getWidth() - 300, 0, 300, 300, Tools.getImage("/game/background"));
	}
	
	public void add(Entity e, int entityX, int entityY) {
		if (e instanceof Base) {
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
			int baseX = (entityX * Map.TILE_WIDTH) / 1000;
			int baseY = (entityY * Map.TILE_HEIGHT) / 1000;
			add(new Panel(baseX, baseY, 5, 5, c));
		}
	}
	
	public void clear() {
		components.clear();
	}
	
	private boolean mousePressed(MousePressedEvent e) {
		if (this.getBounds().contains(e.getX(), e.getY())) {
			
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
