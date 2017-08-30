package main.objects.entities.base;

import main.Tools;
import main.objects.AnimatedEntity;

public class Defence extends AnimatedEntity {
	
	private int xPos, yPos;

	public Defence(int x, int y) {
		super(x, y, Tools.splitImage("/entities/base/defence", 72, 43), 0, 5);
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public void setX(int x) {
		this.x = xPos + x;
	}
	
	@Override
	public void setY(int y) {
		this.y = yPos + y;
	}
	
}
