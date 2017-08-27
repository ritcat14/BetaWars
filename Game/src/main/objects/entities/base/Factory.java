package main.objects.entities.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Font;

import main.DatabaseManager;
import main.Tools;
import main.events.Event;
import main.events.EventDispatcher;
import main.events.EventHandler;
import main.events.EventListener;
import main.events.types.MouseMovedEvent;
import main.events.types.MousePressedEvent;
import main.graphics.Button;
import main.graphics.Label;
import main.graphics.PopupBox;
import main.objects.AnimatedEntity;
import main.objects.Entity;
import main.objects.entities.Base;

public class Factory extends Entity implements EventListener {

	private AnimatedEntity animation, animation2;
	private BufferedImage[] images;
	private BufferedImage extension;
	private DatabaseManager dm;
	private final int playerID, mapID, baseID;
	private Timer timer;
	private String type;
	private int level = 1;
	private int xPos, yPos, xOffset, yOffset, xOffset2, yOffset2;
	private PopupBox box;
	private boolean owned = false;
	private Label timeRemaining;
	private Base base;
	
	public Factory(Base b, boolean owned, int level, int baseID, int playerID, int mapID, DatabaseManager dm, int x, int y, int xOffset, int yOffset, int width, int height, String type) {
		super(x, y, width, height, Tools.getImage("/entities/base/" + type));
		this.playerID = playerID;
		this.mapID = mapID;
		this.baseID = baseID;
		this.owned = owned;
		this.dm = dm;
		this.type = type;
		xPos = x;
		yPos = y;
		this.base = b;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xOffset2 = xOffset;
		this.yOffset2 = yOffset;
		this.level = level;
		BufferedImage[] factories = Tools.splitImage(image, 10, width);
		BufferedImage[] extra = Tools.splitImage(image, 6, width);
		images = Tools.joinArrays(factories, extra);
		
		timeRemaining = new Label(" ", getX() + (getWidth() / 2), getY() + (getHeight() / 2), 100, 10, Color.WHITE, new Font("Times New Java", Font.BOLD, 25)) {
			@Override
			public void update() {
				if (visible) {
					super.update();
					if (timer.isRunning() == false) this.visible = false;
					setText(" " + timer.getHours() + ":" + timer.getMinutes() + ":" + timer.getSeconds());
				}
			}
		};
		timeRemaining.setVisible(false);

		BufferedImage[] buttonImages = Tools.splitImage("/game/" + type + "Buttons", 3, 40);
		box = new PopupBox(x, y, 140, 40, 3, 1, new Button(x + 5, y + 5, buttonImages[0]) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					increaseLevel();
				}
				return false;
			}
		}, new Button(x + 50, y + 5, buttonImages[1]) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					if (!base.isProducing())
						produce(60 * 60);
				}
				return false;
			}
		}, new Button(x + 95, y + 5, buttonImages[2]) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					if (!base.isProducing())
						produce(60 * 60 * 15);
				}
				return false;
			}
		});
		update();
	}

	@Override
	public void update() {
		switch (type) {
			case "Fuel":
				if (level > 10 && animation == null && animation2 == null) {
					animation = new AnimatedEntity(798, 259, Tools.splitImage("/entities/base/FuelAnimation", 50, 68), 0, 5).play();
					animation2 = new AnimatedEntity(858, 279, Tools.splitImage("/entities/base/FuelAnimation", 50, 68), 25, 5).play();
				}
				break;
		}
		if (level > 10) {
			image = images[9];
			extension = images[level - 1];
		} else image = images[level - 1];
		box.update();
		if (animation != null) animation.update();
		if (animation2 != null) animation2.update();
		if (timeRemaining.isVisible()) timeRemaining.update();
	}
	
	@Override
	public void render(Graphics g) {
		if (level > 10) {
			g.drawImage(extension, xOffset, yOffset, null);
		}
		super.render(g);
		if (animation != null) animation.render(g);
		if (animation2 != null) animation2.render(g);
		box.render(g);
		if (timeRemaining.isVisible()) timeRemaining.render(g);
	}
	
	public void produce(int time) {
		if (timer == null || !timer.isRunning()) {
			timer = new Timer(this, playerID, mapID, dm, time, type);
			timeRemaining.setVisible(true);
		}
		else {
			timeRemaining.setVisible(false);
			timer = null;
		}
	}
	
	@Override
	public void setX(int x) {
		super.setX(x + xPos);
		xOffset = x+xOffset2;
		box.setPosition(x + xPos, box.getY());
		switch (type) {
			case "Fuel":
				if (animation != null) animation.setX(this.x - 17);
				if (animation2 != null) animation2.setX(this.x + 36);
				break;
		}
		if (timeRemaining.isVisible()) timeRemaining.setPosition(this.x + (getWidth()/2), timeRemaining.getY());
	}
	
	@Override
	public void setY(int y) {
		super.setY(y + yPos);
		yOffset = y + yOffset2;
		box.setPosition(box.getX(), y + yPos);
		switch (type) {
		case "Fuel":
			if (animation != null) animation.setY(this.y + 36);
			if (animation2 != null) animation2.setY(this.y + 55);
			break;
		}
		if (timeRemaining.isVisible()) timeRemaining.setPosition(timeRemaining.getX(), this.y + (getHeight()/2));
	}
	
	public void increaseLevel() {
		level++;
		if (level > 16) level = 16;
		else dm.updateTable("Base", type + "Level", "" + level, "BaseID = " + baseID);
	}
	
	public int getLevel() {
		return level;
	}
	
	public boolean isProducing() {
		return timeRemaining.isVisible();
	}
	
	public PopupBox getBox() {
		return box;
	}
	
	private boolean mouseMoved(MouseMovedEvent e) {
		if (getBounds().contains(e.getX(), e.getY())) {
			box.setVisible(true);
			return true;
		}
		if (box.isVisible()) box.setVisible(false);
		return false;
	}

	@Override
	public void onEvent(Event event) {
		if (owned) {
			EventDispatcher dispatcher = new EventDispatcher(event);
			dispatcher.dispatch(Event.Type.MOUSE_MOVED, new EventHandler() {
				public boolean onEvent(Event event) {
					return mouseMoved((MouseMovedEvent)event);
				}
			});
			box.onEvent(event);
		}
	}
}

class Timer implements Runnable {
	
	private Thread t;
	private int time = 0;
	private int elapsedTime = 0;
	private DatabaseManager dm;
	private boolean running = false;
	private String type;
	private final int playerID;
	private final int mapID;
	private Factory f;
	private int hours, minutes, seconds;
	
	public Timer(Factory f, int playerID, int mapID, DatabaseManager dm, int time, String type) {
		this.playerID = playerID;
		this.f = f;
		this.mapID = mapID;
		this.time = time;
		this.dm = dm;
		this.type = type;
		t = new Thread(this, "Timer");
		t.start();
		running = true;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}

	@Override
	public void run() {
		while (running) {
			elapsedTime++;
			dm.updateTable("Map", type, type + " + " + (1 * f.getLevel()), "PlayerID = " + playerID, "MapID = " + mapID);
			if (elapsedTime == time) running = false;
			int remainingTime = (time - elapsedTime) * 1000;
			seconds = (int) (remainingTime / 1000) % 60 ;
			minutes = (int) ((remainingTime / (1000*60)) % 60);
			hours   = (int) ((remainingTime / (1000*60*60)) % 24);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
