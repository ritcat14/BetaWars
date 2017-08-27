package main;

public class Renderer implements Runnable {
	
	private Thread t;
	private GameCanvas canvas;
	private boolean running = false;
	private ThreadHandler th;
	
	public Renderer(int width, int height, ThreadHandler threadHandler, StateHandler stateHandler) {
		this.th = threadHandler;
		canvas = new GameCanvas(width, height, stateHandler);
		t = new Thread(this, "Renderer");
		threadHandler.add(t);
		running = true;
	}
	
	public Thread getThread() {
		return t;
	}
	
	public void render() {
		canvas.draw();
	}

	@Override
	public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            render();
            while (delta >= 1) {
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        th.stop(t);
    }
	
	public GameCanvas getCanvas() {
		return canvas;
	}

}
