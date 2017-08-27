package main.graphics;

import java.awt.image.BufferedImage;

public class BuildButton extends Button {
	
	private String desc1, desc2, name;

	public BuildButton(int x, int y, BufferedImage image, String name, String desc1, String desc2) {
		super(x, y, image);
		this.name = name;
		this.desc1 = desc1;
		this.desc2 = desc2;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesc1() {
		return desc1;
	}
	
	public String getDesc2() {
		return desc2;
	}

}
