package main.graphics;

import java.awt.Graphics;
import java.util.ArrayList;

/*
 * This class represents any GUI object
 */

public class GuiComponent {

	protected int x, y;
	protected ArrayList<GuiComponent> components = new ArrayList<GuiComponent>();
	protected boolean visible = true;
	
	public GuiComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(GuiComponent... components) {
		for (GuiComponent gc : components) add(gc);
	}
	
	public void add(GuiComponent c) {
		if (c.equals(this)) {
			System.err.println(Thread.currentThread().getName() + ": Cannot add component to itself!");
			return;
		}
		if (components.contains(c)) {
			System.err.println(Thread.currentThread().getName() + ": Cannot add component more than once!");
			return;
		}
		c.adjustRelative(x, y);
		components.add(c);
	}
	
	private void adjustRelative(int x2, int y2) {
		this.x = this.x + x2;
		this.y = this.y + y2;
		for (GuiComponent c : components) c.adjustRelative(x2, y2);
	}

	public void update() {
		if (visible) {
			for (GuiComponent component : components) {
				component.update();
			}
		}
	}
	
	public void render(Graphics g) {
		if (visible) {
			for (GuiComponent component : components) {
				component.render(g);
			}
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public ArrayList<GuiComponent> getComponents() {
		return components;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
}
