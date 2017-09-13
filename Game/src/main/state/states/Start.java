package main.state.states;

import java.awt.Color;
import java.awt.Graphics;

import main.DatabaseManager;
import main.Main;
import main.StateHandler;
import main.StateHandler.States;
import main.ThreadHandler;
import main.Tools;
import main.events.Event;
import main.events.EventListener;
import main.events.types.MousePressedEvent;
import main.graphics.Button;
import main.graphics.GuiComponent;
import main.graphics.Label;
import main.graphics.Panel;
import main.graphics.TextBox;
import main.state.State;

/*
 * The START state
 */

public class Start extends State {
	
	private Panel mainPanel;
	private TextBox nameBox;
	private String gameFolderUrl = System.getProperty("user.home") + "/Documents/Game/";
	private String playerUrl = gameFolderUrl + "Player.txt";
	
	public Start(ThreadHandler th, DatabaseManager databaseManager) {
		super(th);
		mainPanel = new Panel(50, 50, Main.getWidth() - 100, Main.getHeight() - 100, Color.BLACK);
		mainPanel.add(new Button((Main.getWidth() / 6), (Main.getHeight() / 5), Tools.getImage("/start/button1")) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					StateHandler.changeState(States.LOADING);
				}
				return false;
			}
		});
		mainPanel.add(new Button((Main.getWidth() / 6), (Main.getHeight() / 5) + 80, Tools.getImage("/start/button2")) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					StateHandler.changeState(States.MENU);
					return true;
				}
				return false;
			}
		});
		mainPanel.add(new Button((Main.getWidth() / 6), (Main.getHeight() / 5) + 160, Tools.getImage("/start/button3")) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					System.exit(-1);
				}
				return false;
			}
		});
		mainPanel.add(nameBox = new TextBox((Main.getWidth() / 6), (Main.getHeight() / 5) - 80, 100, 50));
		mainPanel.add(new Label("Name: ", (Main.getWidth() / 6) - 100, (Main.getHeight() / 5) - 80, 200, 50, Color.GRAY));

		if (Tools.exists(playerUrl)) loadPlayer(databaseManager);
		
	}
	
	private void loadPlayer(DatabaseManager dm) {
		String[] data = Tools.loadFromFile(playerUrl);
		int ID = Integer.parseInt(data[0].split(",")[0]);
		Object[][] playerData = dm.getData("Player");
		for (Object[] o : playerData) {
			if (ID == Integer.parseInt((String)o[0])) nameBox.setText((String)o[2]);
		}
	}

	public String getName() {
		return nameBox.getText();
	}

	@Override
	public void update() {
		mainPanel.update();
	}

	@Override
	public void render(Graphics g) {
		mainPanel.render(g);
	}

	public void onEvent(Event event) {
		for (GuiComponent c : mainPanel.getComponents()) {
			if (c instanceof EventListener) {
				((EventListener) c).onEvent(event);
			}
		}
	}

}
