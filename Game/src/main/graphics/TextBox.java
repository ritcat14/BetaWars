package main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import main.events.Event;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.KeyPressedEvent;
import main.events.types.KeyReleasedEvent;
import main.events.types.MousePressedEvent;

public class TextBox extends GuiComponent implements EventListener {
	
	private String typedText = "";
	private int time = 0;
	private boolean selected = false;
	private int width, height;
	private boolean keyPressed = false;
	private boolean caps = false;
	
	public TextBox(int x, int y, int width, int height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void update() {
		super.update();
		time++;
		if (selected) {
			if (time % 25 == 0 && !keyPressed) {
				if (typedText.endsWith("|")) {
					typedText = typedText.substring(0, typedText.length() - 1);
				} else {
					typedText += "|";
				}
			}
		} else {
			if (typedText.endsWith("|")) typedText = typedText.substring(0, typedText.length() - 1);
		}
	}
	
	public String getText() {
		if (typedText.endsWith("|")) return typedText.substring(0, typedText.length() - 1);
		return typedText;
	}
	
	private boolean keyPressed(KeyPressedEvent e) {
		if (selected) {
			keyPressed = true;
			switch (e.getKey()) {
				case KeyEvent.VK_BACK_SPACE:
					if (typedText.endsWith("|")) typedText = typedText.substring(0, typedText.length() - 1);
					 typedText = typedText.substring(0, typedText.length() - 1);
					 break;
				case KeyEvent.VK_SHIFT:
					caps = !caps;
					break;
				case KeyEvent.VK_CAPS_LOCK:
					caps = true;
					break;
				default:
					if (typedText.endsWith("|")) {
						typedText = typedText.substring(0, typedText.length() - 1);
					}
					if (caps) typedText += KeyEvent.getKeyText(e.getKey());
					else typedText += KeyEvent.getKeyText(e.getKey()).toLowerCase();
					keyPressed = false;
					break;
			}
			return true;
		}
		return false;
	}
	
	public void setText(String text) {
		if (typedText.endsWith("|")) typedText = typedText.substring(0, typedText.length() - 1);
		typedText = text;
	}
	
	private boolean mousePressed(MousePressedEvent e) {
		if (new Rectangle(x, y, width, height).contains(e.getX(), e.getY())) {
			selected = true;
			return true;
		}
		if (selected) {
			selected = false;
			if (typedText.endsWith("|")) typedText = typedText.substring(0, typedText.length() - 1);
			return true;
		}
		return false;
	}
	
	private boolean keyReleased(KeyReleasedEvent e) {
		if (selected) {
		switch (e.getKey()) {
			case KeyEvent.VK_SHIFT:
				caps = false;
				break;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.KEY_PRESSED, new EventHandler() {
			public boolean onEvent(Event event) {
				return keyPressed((KeyPressedEvent) event);
			}
		});
		dispatcher.dispatch(Event.Type.KEY_RELEASED, new EventHandler() {
			public boolean onEvent(Event event) {
				return keyReleased((KeyReleasedEvent) event);
			}
		});
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, new EventHandler() {
			public boolean onEvent(Event event) {
				return mousePressed((MousePressedEvent) event);
			}
		});
	}
	
	@Override
	public void render(Graphics g) {
		int textWidth = g.getFontMetrics().stringWidth(typedText);
		if (textWidth > width + 5) width = textWidth + 5;
		g.setColor(Color.GRAY);
		g.drawRect(x, y, width, height);
		g.drawString(typedText, x + 5, y + (height / 2));
	}

}
