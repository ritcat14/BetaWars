package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import main.events.Keyboard;
import main.events.Mouse;

public class GameCanvas extends Canvas {

	private static final long serialVersionUID = 1L;
	private StateHandler stateHandler;
	private Keyboard key;
	private static BufferedImage currentFrame;
	private int[] pixels;
	
	public GameCanvas(int width, int height, StateHandler stateHandler) {
		this.stateHandler = stateHandler;

        currentFrame = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)currentFrame.getRaster().getDataBuffer()).getData();
        
		setPreferredSize(new Dimension(width, height));
		
		Mouse m = new Mouse(stateHandler);
		key = new Keyboard(stateHandler);
		
		addMouseListener(m);
		addMouseMotionListener(m);
		addKeyListener(key);
		
		requestFocus();
	}
	
	public static BufferedImage getFrame() {
		return currentFrame;
	}
	
	public void draw() {
		requestFocus();
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(4);
            return;
        }
        for (int i = 0; i < getWidth() * getHeight(); i++)
            pixels[i] = 0;

        Graphics g = currentFrame.getGraphics();        
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        stateHandler.render(g);
        
        bs.getDrawGraphics().drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
        
        g.dispose();
        bs.show();
	}

}
