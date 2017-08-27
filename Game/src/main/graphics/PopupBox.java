package main.graphics;

import java.awt.Graphics;

import main.Tools;
import main.events.Event;
import main.events.Event.Type;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.MouseMovedEvent;

public class PopupBox extends Panel implements EventListener {
	
	private Button[] buttons;
	private int xNum, yNum;
	
	public PopupBox(int x, int y, int width, int height, int xNum, int yNum, Button... buttons) {
		super(x, y, width, height, Tools.getImage("/game/menuBackground"));
		this.xNum = xNum;
		this.yNum = yNum;
		this.buttons = buttons;
		setVisible(false);
	}

	@Override
	public void update() {
		if (!visible) return;
		for (Button b : buttons) {
			b.update();
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (!visible) return;
		super.render(g);
		for (Button b : buttons) {
			b.render(g);
		}
	}
	
	public boolean mouseMoved(MouseMovedEvent e) {
		if (this.getBounds().contains(e.getX(), e.getY())) {
			if (!visible) {
				visible = true;
				return true;
			}
			return false;
		} else if (visible) {
			visible = false;
			return true;
		}
		return false;
	}

	@Override
	public void onEvent(Event e) {
		new EventDispatcher(e).dispatch(Type.MOUSE_MOVED, new EventHandler() {
			public boolean onEvent(Event event) { return mouseMoved((MouseMovedEvent)e); }
		});
		if (visible) for (Button b : buttons) b.onEvent(e);
	}
	
	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		int i = 0;
		for (int y1 = 0; y1 < yNum; y1++) {
			for (int x1 = 0; x1 < xNum; x1++) {
				Button b = buttons[i];
				b.setPosition(x + 5 + (x1 * 45), y + 5 + (y1 * 45));
				i++;
			}
		}
		i = 0;
		for (GuiComponent component : components) {
			component.setPosition(x + 5, y + 10 + (i * 10));
			i++;
		}
	}

}
