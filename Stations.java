/**
 * @author Cordell
 * this class is used to create workers for the plants
 */
public class Stations implements Runnable {
	private final Thread wthread;
	private final int STATION_ID;
	private volatile boolean keepWorking;
	public AssemblyLine getWorkFrom;
	public AssemblyLine giveWorkTo;

	/**
	 * constructs a worker with which plant the worker is at, the worker's own ID at the plant, 
	 * the description of the work the worker is doing, which Assembly Line the worker takes 
	 * their oranges from, and which line the worker puts their oranges into 
	 * @param plantId 
	 * @param id
	 * @param des
	 * @param getFrom
	 * @param giveTo
	 */
	public Stations(int plantId, int id, String des, AssemblyLine getFrom, AssemblyLine giveTo) {
		STATION_ID = id;
		wthread = new Thread(this, "Plant_" + plantId + "_Station_" + des + "_" + STATION_ID);
		getWorkFrom = getFrom;
		giveWorkTo = giveTo;
	}

	/**
	 * starts the worker threads
	 */
	public void startWork() {
		keepWorking = true;
		wthread.start();
	}

	/**
	 * tells the worker threads that the plant is stopping
	 */
	public void stopWork() {
		keepWorking = false;
	}

	/**
	 * Ensures that the worker thread finishes all the oranges waiting for their
	 * station. Waits for the worker thread to terminate.
	 */
	public void waitToStop() {
		try {
			System.out.println(this.wthread.getName() + " is cleaning up");
			// as long as there are still oranges for this worker to work on, the worker
			// will keep working
			while (getWorkFrom.countOranges() > 0) {
				Orange nextOrange = getWorkFrom.getOrange();
				// ensures that the worker does not attempt to re-process an orange
				if (nextOrange.getState() != nextOrange.getState().Processed) {
					nextOrange.runProcess();
				}
				giveWorkTo.addOrange(nextOrange);
			}
			wthread.join();
		} catch (InterruptedException e) {
			System.err.println(wthread.getName() + " stop malfunction ");
		}
	}

	public void run() {
		System.out.println(Thread.currentThread().getName() + " is operating");
		// as long as the plants are still running, the worker continues to work on
		// their oranges
		while (keepWorking) {
			Orange nextOrange = getWorkFrom.getOrange();
			// ensures that the worker does not attempt to re-process an orange
			if (nextOrange.getState() != nextOrange.getState().Processed) {
				nextOrange.runProcess();
			}
			giveWorkTo.addOrange(nextOrange);

		} // end while

	}// end run

}
