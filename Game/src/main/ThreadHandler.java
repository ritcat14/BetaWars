package main;

import java.util.ArrayList;

/*
 * A class for handling the operation of Threads.
 */

public class ThreadHandler {
	
	private ArrayList<Thread> threads;
	
	public ThreadHandler() {
		threads = new ArrayList<Thread>();
	}
	
	public synchronized void add(Thread t) {
		threads.add(t);
	}
	
	public synchronized void remove(Thread t) {
		stop(t);
		threads.remove(t);
	}
	
	public synchronized void stop(Thread t) {
		try {
			t.join();
		} catch (Exception e) {
			System.err.println("Error closing thread: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public synchronized void start(Thread t1) {
		t1.start();
	}
	
	/*
	 * A method for safely closing all threads. Ensures the thread that called the method
	 * is the last thread to be closed.
	 */
	public synchronized void stopAll(Thread calledThread) {
		for (Thread t : threads) {
			if (t.equals(calledThread)) continue;
			stop(t);
		}
		stop(calledThread);
		System.exit(-1);
	}

}
