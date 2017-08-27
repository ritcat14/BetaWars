package main;

import java.awt.Toolkit;

import javax.swing.JFrame;

import main.StateHandler.States;

public class Main {
	
	public Updater updater;
	public Renderer renderer;
	public JFrame frame;
	public ThreadHandler threadHandler;
	public StateHandler stateHandler;
	public DatabaseManager databaseManager;
	
	private static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	private static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	public Main() {
		databaseManager = new DatabaseManager();
		threadHandler = new ThreadHandler();
		stateHandler = new StateHandler(threadHandler, databaseManager);
		StateHandler.changeState(States.START);
		renderer = new Renderer(WIDTH, HEIGHT, threadHandler, stateHandler);
		updater = new Updater(stateHandler, threadHandler);
	}
    
    public static int getWidth() {
    	return WIDTH;
    }
    
    public static int getHeight() {
    	return HEIGHT;
    }
  
    public static void main(String[] args) {
        Main m = new Main();
        m.frame = new JFrame("Game");
        m.frame.setResizable(false);
        m.frame.setUndecorated(true);
        m.frame.add(m.renderer.getCanvas());
        
        m.frame.pack();
        m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        m.frame.setLocationRelativeTo(null);
        m.frame.setVisible(true);
        m.frame.requestFocus();
        m.threadHandler.start(m.renderer.getThread());
    }

}
