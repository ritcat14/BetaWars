package main.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.events.Event;
import main.events.EventListener;

public class Tab extends Panel implements EventListener {
	
	private Button button;
	private String name;
	
	public Tab(int x, int y, int width, int height, Color c, Button button, String name) {
		super(x, y, width, height, c);
		this.button = button;
		this.name = name;
		button.setPosition(x, y - 100);
		init();
	}
	
	public Tab(int x, int y, int width, int height, BufferedImage image, Button button, String name) {
		super(x, y, width, height, image);
		this.button = button;
		this.name = name;
		button.setPosition(x, y - 100);
		init();
	}
	
	public Tab(int x, int y, BufferedImage image, Button button, String name) {
		super(x, y, image.getWidth(), image.getHeight(), image);
		this.button = button;
		this.name = name;
		button.setPosition(x, y - 100);
		init();
	}
	
	public void init() {
		// Create all items to add to the tab
	}
	
	@Override
	public void onEvent(Event event) {
		if (visible) {
			for (GuiComponent c : components) {
				if (c instanceof Button) ((Button)c).onEvent(event);
			}
		}
	}
	
	public Button getButton() {
		return button;
	}
	
	public String getName() {
		return name;
	}

}
