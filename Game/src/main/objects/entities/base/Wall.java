package main.objects.entities.base;

import main.Tools;
import main.objects.Entity;

public class Wall extends Entity {

	private String type;
	private boolean upgraded = false;
	
	public Wall(int x, int y, String type) {
		super(x, y, Tools.getImage("/entities/base/" + type + "wall"));
		this.type = type;
	}
	
	public void upgrade() {
		upgraded = true;
		this.image = Tools.getImage("/entities/base/" + type + "wallup");
	}

	@Override
	public void update() {}
	
	public boolean isUpgraded() {
		return upgraded;
	}

}
