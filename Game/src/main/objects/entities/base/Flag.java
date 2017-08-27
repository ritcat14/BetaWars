package main.objects.entities.base;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Tools;
import main.objects.AnimatedEntity;

public class Flag extends AnimatedEntity {

	private BufferedImage pole;
	private int posX, posY, poleX, poleY, poleX2, poleY2;
	
	public Flag(int x, int y, int poleX, int poleY, String col, int start) {
		super(x, y, Tools.splitImage("/entities/base/" + col + "Flag", 34, 18), start, 5);
		pole = Tools.getImage("/entities/base/pole");
		posX = x;
		posY = y;
		this.poleX = poleX2 = poleX;
		this.poleY = poleY2 = poleY;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(pole, poleX, poleY, null);
		super.render(g);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x + posX);
		poleX = x + poleX2;
	}
	
	@Override
	public void setY(int y) {
		super.setY(y + posY);
		poleY = y + poleY2;
	}

}
