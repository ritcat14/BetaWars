package main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.events.Event;
import main.events.EventListener;

public class TabbedPanel extends Panel implements EventListener {
	
	private Tab[] tabs;
	private Tab currentTab;
	private Button[] buttons;
	
	public TabbedPanel(int x, int y, int width, int height, Color c, Tab...tabs) {
		super(x, y, width, height, c);
		this.tabs = tabs;
		init();
	}
	
	public TabbedPanel(int x, int y, int width, int height, BufferedImage image, Tab...tabs) {
		super(x, y, width, height, image);
		this.tabs = tabs;
		init();
	}
	
	public TabbedPanel(int x, int y, BufferedImage image, Tab...tabs) {
		super(x, y, image.getWidth(), image.getHeight(), image);
		this.tabs = tabs;
		init();
	}
	
	private void init() {
		buttons = new Button[tabs.length];
		int i = 0;
		for (Tab t : tabs) {
			Button b = t.getButton();
			b.setPosition(i * b.getWidth(), b.getY());
			buttons[i] = b;
			add(b);
			i++;
		}
		currentTab = tabs[0];
		components.add(currentTab);
	}
	
	public void setTab(String tab) {
		components.remove(currentTab);
		for (Tab t : tabs) {
			if (t.getName().equals(tab)) currentTab = t;
		}
		components.add(currentTab);
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) {
			currentTab.render(g);
			for (Button b : buttons) b.render(g);
		}
	}

	@Override
	public void onEvent(Event event) {
		if (visible) {
			for (Button b : buttons) b.onEvent(event);
			currentTab.onEvent(event);
		}
	}

}
