package main.state.states;

import java.awt.Color;
import java.awt.Graphics;

import main.DatabaseManager;
import main.Main;
import main.StateHandler;
import main.ThreadHandler;
import main.Tools;
import main.events.Event;
import main.events.EventListener;
import main.events.types.MousePressedEvent;
import main.graphics.Button;
import main.graphics.GuiComponent;
import main.graphics.Panel;
import main.state.State;

public class Map extends State {
	
	private Game game;
	private Object[][] mapData;
	private Panel mainPanel;

	public Map(ThreadHandler th, DatabaseManager dm) {
		super(th);
		this.game = (Game) StateHandler.getPreState();
		game.pause();
		mapData = dm.getTableData("Map");
		int mapNum = 0;
		int mapID = 0;
		for (Object[] map : mapData) {
			int tempID = Integer.parseInt((String)map[0]);
			if (tempID > mapID) {
				mapID = tempID;
				mapNum ++;
			}
		}
		mainPanel = new Panel(200, 200, Main.getWidth() - 400, Main.getHeight() - 400, Color.BLACK);
		for (int i = 0; i < mapNum; i++) {
			mainPanel.add(new Button(5 + (i * 50), 5, 50, 50, Color.CYAN, "" + (i + 1)) {
				@Override
				public boolean mousePressed(MousePressedEvent e) {
					if (super.mousePressed(e)) {
						game.start();
						game.changeMap(Integer.parseInt(this.text));
						StateHandler.setState(game);
						return true;
					}
					return false;
				}
			});
		}
		mainPanel.add(new Button(mainPanel.getWidth() - 55, 5, 50, 50, Tools.getImage("/EXIT")) {
			@Override
			public boolean mousePressed(MousePressedEvent e) {
				if (super.mousePressed(e)) {
					game.start();
					StateHandler.setState(game);
				}
				return false;
			}
		});
	}

	@Override
	public void onEvent(Event event) {
		for (GuiComponent b : mainPanel.getComponents()) {
			if (b instanceof EventListener) {
				((EventListener) b).onEvent(event);
			}
		}
	}

	@Override
	public void update() {
		mainPanel.update();
	}

	@Override
	public void render(Graphics g) {
		game.render(g);
		mainPanel.render(g);
	}

}
