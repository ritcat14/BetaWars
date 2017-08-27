package main;

public class Updater implements Runnable {
	
	private Thread t;
	private ThreadHandler threadHandler;
	private boolean running = false;
	private StateHandler stateHandler;
	
	public Updater(StateHandler stateHandler, ThreadHandler threadHandler) {
		this.threadHandler = threadHandler;
		this.stateHandler = stateHandler;
		t = new Thread(this, "Updater");
		this.threadHandler.add(t);
		this.threadHandler.start(t);
		running = true;
	}
	
	public void update() {
		stateHandler.update();
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
            //Render
            while (delta >= 1) {
                update();
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        threadHandler.stop(t);
	}

}
