package main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import main.events.Event;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.MouseMovedEvent;
import main.events.types.MousePressedEvent;
import main.events.types.MouseReleasedEvent;

public class Button extends Panel implements EventListener {

	protected String text = "";

	public Button(int x, int y, int width, int height, Color c, String text) {
		super(x, y, width, height, c);
		this.text = text;
	}

	public Button(int x, int y, int width, int height, BufferedImage image) {
		super(x, y, width, height, image);
	}

	public Button(int x, int y, BufferedImage image) {
		super(x, y, image.getWidth(), image.getHeight(), image);
	}

	public Button(int x, int y, BufferedImage image, String text) {
		super(x, y, image.getWidth(), image.getHeight(), image);
		this.text = text;
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			super.render(g);
			if (image != null) g.drawImage(image, x, y, width, height, null);
			g.setColor(Color.BLACK);
			g.drawString(text, x, y + (height / 2));
		}
	}

	public boolean mousePressed(MousePressedEvent e) {
		return (getBounds().contains(new Point(e.getX(), e.getY())));
	}

	public boolean mouseReleased(MouseReleasedEvent e) {
		return (getBounds().contains(new Point(e.getX(), e.getY())));
	}

	public boolean mouseMoved(MouseMovedEvent e) {
		return (getBounds().contains(new Point(e.getX(), e.getY())));
	}

	public void onEvent(Event event) {
		if (visible) {
			EventDispatcher dispatcher = new EventDispatcher(event);
			dispatcher.dispatch(Event.Type.MOUSE_PRESSED, new EventHandler() {
				public boolean onEvent(Event event) {
					return mousePressed((MousePressedEvent) event);
				}
			});
			dispatcher.dispatch(Event.Type.MOUSE_RELEASED, new EventHandler() {
				public boolean onEvent(Event event) {
					return mouseReleased((MouseReleasedEvent) event);
				}
			});
			dispatcher.dispatch(Event.Type.MOUSE_MOVED, new EventHandler() {
				public boolean onEvent(Event event) {
					return mouseMoved((MouseMovedEvent) event);
				}
			});
		}
	}

}
