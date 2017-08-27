package main;

import java.awt.Color;
import java.awt.Graphics;

import main.StateHandler.States;
import main.events.Event;
import main.events.types.MousePressedEvent;
import main.graphics.Button;
import main.graphics.Tab;
import main.graphics.TabbedPanel;
import main.state.State;

public class Menu extends State {

	private TabbedPanel mainPanel;

	public Menu(ThreadHandler th) {
		super(th);
		mainPanel = new TabbedPanel(200, 100, Main.getWidth() - 400, Main.getHeight() - 200, Color.BLACK,
				new Tab(200, 100, Main.getWidth() - 400, Main.getHeight() - 200, Color.GREEN, new Button(0, 0, Tools.getImage("/menu/graphicsButton")) {
					@Override
					public boolean mousePressed(MousePressedEvent e) {
						if (super.mousePressed(e)) {
							mainPanel.setTab("GRAPHICS");
							return true;
						}
						return false;
					}

				}, "GRAPHICS") {
					@Override
					public void init() {
						
					}
				}, 
				
				new Tab(200, 100, Main.getWidth() - 400, Main.getHeight() - 200, Color.BLUE, new Button(0, 0, Tools.getImage("/menu/inGameButton")) {
					@Override
					public boolean mousePressed(MousePressedEvent e) {
						if (super.mousePressed(e)) {
							mainPanel.setTab("INGAME");
							return true;
						}
						return false;
					}
				}, "INGAME") {
					@Override
					public void init() {
						
					}
				}, 
				
				new Tab(200, 100, Main.getWidth() - 400, Main.getHeight() - 200, Color.RED, new Button(0, 0, Tools.getImage("/menu/exitButton")) {
					@Override
					public boolean mousePressed(MousePressedEvent e) {
						if (super.mousePressed(e)) {
							mainPanel.setTab("EXIT");
							return true;
						}
						return false;
					}
				}, "EXIT") {
					@Override
					public void init() {
						add(new Button(400, (height / 4)*3, Tools.getImage("/menu/yesButton")) {
							@Override
							public boolean mousePressed(MousePressedEvent e) {
								if (super.mousePressed(e)) {
									StateHandler.changeState(States.START);
									return true;
								}
								return false;
							}
						});
						
						add(new Button(Main.getWidth() - 1000, (height / 4)*3, Tools.getImage("/menu/noButton")) {
							@Override
							public boolean mousePressed(MousePressedEvent e) {
								if (super.mousePressed(e)) {
									mainPanel.setTab("GRAPHICS");
									return true;
								}
								return false;
							}
						});
					}
				});
	}

	@Override
	public void onEvent(Event event) {
		mainPanel.onEvent(event);
	}

	@Override
	public void update() {
		mainPanel.update();
	}

	@Override
	public void render(Graphics g) {
		mainPanel.render(g);
	}

}
