package main.objects.entities.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import main.Tools;
import main.events.Event;
import main.events.EventListener;
import main.events.types.MouseMovedEvent;
import main.events.types.MousePressedEvent;
import main.graphics.BuildButton;
import main.graphics.Button;
import main.graphics.Label;
import main.graphics.PopupBox;
import main.objects.Entity;
import main.objects.entities.Base;

public class Garage extends Entity implements EventListener {
	
	private BufferedImage cover;
	private int xPos, yPos;
	protected PopupBox box;

	public Garage(Base base, int x, int y, String type) {
		super(x, y, Tools.getImage("/entities/base/garage" + type));
		cover = Tools.getImage("/entities/base/garage" + type + "Cover");
		xPos = x;
		yPos = y;
		
		String[] garageData = Tools.loadFromResource("/gameData/garageInfo.gme");
		String[] data = null;
		for (String s : garageData) {
			switch(s) {
			case "Helibase":
				if (type.equals("Helibase")) data = Arrays.copyOfRange(garageData, 1, 17);
				break;
			case "Airbase":
				if (type.equals("Airbase")) data = Arrays.copyOfRange(garageData, 19, 25);
				break;
			case "Base":
				if (type.equals("Type1") || type.equals("Type2")) data = Arrays.copyOfRange(garageData, 27, garageData.length);
				break;
			}
		}

		BufferedImage[] buttonImages = Tools.splitImage("/game/garageButtons", 28, 40);
		Button[] buttons = new Button[4 * 7];
		int x1 = 0;
		int y1 = 0;
		Label name = new Label("", 5, 10, 185, 5, Color.WHITE, new Font("Times New Java", Font.BOLD, 15));
		Label desc1 = new Label("", 5, 20, 185, 5, Color.WHITE, new Font("Times New Java", Font.PLAIN, 10));
		Label desc2 = new Label("", 5, 30, 185, 5, Color.WHITE, new Font("Times New Java", Font.PLAIN, 10));
		
		for (int i = 0; i < data.length; i++) {
			String[] parts = data[i].split(":");
			int index = Integer.parseInt(parts[0]);
			String itemName = parts[1];
			String dec1 = parts[2];
			String dec2 = "";
			if (parts.length > 3) dec2 = parts[3];
			
			buttons[i] = new BuildButton(x + 5 + (x1 * 45), y + 35 + (y1 * 45), buttonImages[i], itemName, dec1, dec2) {
				@Override
				public boolean mousePressed(MousePressedEvent e) {
					if (super.mousePressed(e)) {
						base.buttonEvent(index);
						return true;
					}
					return false;
				}
				
				@Override
				public boolean mouseMoved(MouseMovedEvent e) {
					if (super.mouseMoved(e)) {
						name.setText(getName());
						desc1.setText(getDesc1());
						desc2.setText(getDesc2());
						return true;
					}
					return false;
				}
			};
			
			x1++;
			if (x1 == 4) {
				y1++;
				x1 = 0;
			}
		}
		
		box = new PopupBox(x, y, 185, 360, 4, 7, buttons) {
			
			@Override
			public void setPosition(int x, int y) {
				super.setPosition(x, y);
				int i = 0;
				for (int y1 = 0; y1 < 7; y1++) {
					for (int x1 = 0; x1 < 4; x1++) {
						Button b = buttons[i];
						b.setPosition(x + 5 + (x1 * 45), y + 45 + (y1 * 45));
						i++;
					}
				}
			}
		};
		box.add(name);
		box.add(desc1);
		box.add(desc2);
	}

	@Override
	public void update() {
		box.update();
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		g.drawImage(cover, x, y, null);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x + xPos);
		box.setPosition(x + xPos, box.getY());
	}
	
	@Override
	public void setY(int y) {
		super.setY(y + yPos);
		box.setPosition(box.getX(), y + yPos);
	}
	
	public PopupBox getBox() {
		return box;
	}

	@Override
	public void onEvent(Event event) {
		box.onEvent(event);
	}

}
