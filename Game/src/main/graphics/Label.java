package main.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Label extends Panel {
	
	protected String text;
	private Font font = new Font("Times New Java", Font.PLAIN, 15);

	public Label(String text, int x, int y, int width, int height, Color c) {
		super(x, y, width, height, c);
		this.text = text;
	}

	public Label(String text, int x, int y, int width, int height, Color c, Font font) {
		super(x, y, width, height, c);
		this.text = text;
		this.font = font;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) {
			g.setFont(font);
			g.setColor(col);
			g.drawString(text, x, y + (height / 2));
		}
	}
	
}
