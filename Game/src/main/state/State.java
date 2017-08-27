package main.state;

import java.awt.Graphics;

import main.ThreadHandler;
import main.events.EventListener;

/*
 * Class that represents and handles a game state
 */

public abstract class State implements EventListener {
	
	protected ThreadHandler th;
	
	public State(ThreadHandler th) {
		this.th = th;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);

}
