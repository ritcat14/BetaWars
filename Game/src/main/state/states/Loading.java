package main.state.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import main.DatabaseManager;
import main.Main;
import main.StateHandler;
import main.ThreadHandler;
import main.Tools;
import main.events.Event;
import main.state.State;

public class Loading extends State {

	private String toDraw = "Loading";
	private int index = 0;
	private int time = 0;
	private int multiplyer = 1;
	private float alpha = 0.0f;
	private float increase = 0.02f;
	private DatabaseManager databaseManager;
	private boolean createdGame, fetchedData, setupGame, createdGui, updatedGame;

	private String gameFolderUrl = System.getProperty("user.home") + "\\Game\\";
	private String playerUrl = gameFolderUrl + "Player.txt";
	
	private Game game;
	
	public Loading(ThreadHandler th, DatabaseManager databaseManager) {
		super(th);
		this.databaseManager = databaseManager;
		createdGame = false;
		fetchedData = setupGame = createdGui = updatedGame = true;
	}
	
	@Override
	public void update() {
		if (!updatedGame) {
			game.update();
			updatedGame = true;
			StateHandler.setState(game);
		}
		if (!createdGui) {
			game.createGui();
			createdGui = true;
			updatedGame = false;
		}
		if (!setupGame) {
			if (Tools.exists(playerUrl)) game.loadPlayer();
			else game.createPlayer();
			setupGame = true;
			createdGui = false;
		}
		if (!fetchedData) {
			game.getData();
			fetchedData = true;
			setupGame = false;
			
		}
		if (!createdGame) {
			game = new Game(th, databaseManager);
			createdGame = true;
			fetchedData = false;
		}
		time++;
		int speed = 2;
		if (time >= Integer.MAX_VALUE - 1) time = 0; // Safety precaution to stop game crashing
		
		if (time % speed == 0) { // If 3 seconds has passed...
			index += multiplyer;
			if (index >= 4) multiplyer = -1; // Reset index if it goes above max
			else if (index == 0) multiplyer = 1;
			toDraw = "Loading";
			for (int i = 0; i < index; i++) toDraw = toDraw + "."; 
		}
		if (time % speed == 0) {
			alpha += increase;
			if (alpha > 0.9f) {
				increase = -0.02f;
			}
			else if (alpha < 0.02f) StateHandler.setState(game);
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		//set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

	    g2d.setColor(Color.BLACK);
	    g2d.fillRect(0, 0, Main.getWidth(), Main.getHeight());
	    g2d.setColor(Color.GRAY);
	    g2d.setFont(new Font("", 0, 50));
	    g2d.drawString(toDraw, 50, Main.getHeight() - (Main.getHeight() / 5));
	}

	@Override
	public void onEvent(Event event) {
		
	}

}
