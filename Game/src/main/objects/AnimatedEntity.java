package main.objects;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class AnimatedEntity extends Entity {
	
	protected BufferedImage[] images;
	protected BufferedImage[] imagesToPlay;
	protected int frame = 0;
	protected int end = 0;
	protected boolean playing = false;
	protected boolean playingSegment = false;
	protected int time = 0;
	protected int timeBetweenFrames = 0;

	public AnimatedEntity(int x, int y, BufferedImage[] images, int startFrame, int time) {
		super(x, y, images[startFrame]);
		this.images = images;
		this.frame = startFrame;
		this.end = images.length - 1;
		this.imagesToPlay = images;
		this.timeBetweenFrames = time;
	}
	
	public AnimatedEntity play() {
		imagesToPlay = images;
		end = imagesToPlay.length - 1;
		playing = true;
		return this;
	}
	
	public AnimatedEntity playSegment(int startFrame, int stopFrame) {
		imagesToPlay = Arrays.copyOfRange(images, startFrame, stopFrame);
		end = imagesToPlay.length - 1;
		playingSegment = true;
		playing = true;
		return this;
	}
	
	public AnimatedEntity stop() {
		playing = false;
		playingSegment = false;
		return this;
	}

	@Override
	public void update() {
		time++;
		if (time > Integer.MAX_VALUE - 1) time = 0;
		if (playing) {
			if (time % timeBetweenFrames == 0) {
				frame++;
				
				if (playingSegment) {if (frame > end) stop();
				} else if (frame >= end) frame = 0;
				
				image = imagesToPlay[frame];
			}
		}
	}

}
