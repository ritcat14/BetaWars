package main;

import java.awt.Graphics;

import main.events.Event;
import main.events.EventListener;
import main.state.State;
import main.state.states.*;

/*
 * Class for handling which state the game is in
 */

public class StateHandler implements EventListener {
	
	private static ThreadHandler th;
	private static DatabaseManager databaseManager;
	private static State preState;
	
	public enum States {
		START, GAME, PAUSE, MENU, EXIT, LOADING, MAP;
	};
	
	private static State currentState;
	
	public StateHandler(ThreadHandler th, DatabaseManager databaseManager) {
		StateHandler.databaseManager = databaseManager;
		StateHandler.th = th;
	}
	
	public static void changeState(States state) {
		preState = currentState;
		switch (state) {
			case START:
				currentState = new Start(th, databaseManager);
				break;
			case LOADING:
				currentState = new Loading(th, databaseManager);
				break;
			case PAUSE:
				break;
			case MENU:
				currentState = new Menu(th);
				break;
			case GAME:
				currentState = new Game(th, databaseManager);
				break;
			case EXIT:
				break;
			case MAP:
				currentState = new Map(th, databaseManager);
				break;
		}
	}
	
	public static State getPreState() {
		return preState;
	}
	
	public static void setState(State state) {
		currentState = state;
	}
	
	public void update() {
		if (currentState != null) {
			currentState.update();
		}
	}
	
	public void render(Graphics g) {
		if (currentState != null) {
			currentState.render(g);
		}
	}

	@Override
	public void onEvent(Event event) {
		currentState.onEvent(event);
	}

}
