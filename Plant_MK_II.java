/**
 * @author Nate Williams a class that simulates an orange processing plant, with
 *         a few modifications I added to accomodate the workers
 */
public class Plant_MK_II implements Runnable {
	// How long do we want to run the juice processing and number of oranges per
	// bottle
	public static final long PROCESSING_TIME = 5 * 1000;
	public final int ORANGES_PER_BOTTLE = 4;

	// plant information
	private final Thread thread;
	private final int plantId;
	private int orangesProvided;
	private volatile boolean timeToWork;

	// worker information
	private static final int NUM_FETCHERS = 1;
	private static final int NUM_PEELERS = 1;
	private static final int NUM_SQUEEZERS = 1;
	private static final int NUM_BOTTLERS = 1;
	private static final int NUM_PROCESSORS = 1;
	private static final int NUM_WORKERS = NUM_FETCHERS + NUM_PEELERS + NUM_SQUEEZERS + NUM_BOTTLERS + NUM_PROCESSORS;
	Stations[] workStations;
	public AssemblyLine pileOfOranges;
	public AssemblyLine needToBePeeled;
	public AssemblyLine needToBeSqueezed;
	public AssemblyLine needToBeBottled;
	public AssemblyLine needToBeProcessed;
	public AssemblyLine needToBeShipped;

	Plant_MK_II(int id) {
		// assigning plant values
		plantId = id + 1;
		orangesProvided = 0;
		thread = new Thread(this, "Plant_" + plantId);

		// generating assembly lines
		pileOfOranges = new AssemblyLine();
		needToBePeeled = new AssemblyLine();
		needToBeSqueezed = new AssemblyLine();
		needToBeBottled = new AssemblyLine();
		needToBeProcessed = new AssemblyLine();
		needToBeShipped = new AssemblyLine();

		// worker Ids
		int fetcherID = 1;
		int peelerID = 1;
		int squeezerID = 1;
		int bottlerID = 1;
		int processorID = 1;

		// generates workers and assigns them their positions in the factory
		// depending on the iteration of the for-loop and the number of workers, the
		// workers are assigned a particular role
		workStations = new Stations[NUM_WORKERS];
		for (int i = 0; i < NUM_WORKERS; i++) {
			if (i < NUM_FETCHERS) {
				workStations[i] = new Stations(plantId, fetcherID, "Fetcher", pileOfOranges, needToBePeeled);
				fetcherID++;
			} else if (i < NUM_FETCHERS + NUM_PEELERS) {
				workStations[i] = new Stations(plantId, peelerID, "Peeler", needToBePeeled, needToBeSqueezed);
				peelerID++;
			} else if (i < NUM_FETCHERS + NUM_PEELERS + NUM_SQUEEZERS) {
				workStations[i] = new Stations(plantId, squeezerID, "Squeezer", needToBeSqueezed, needToBeBottled);
				squeezerID++;
			} else if (i < NUM_FETCHERS + NUM_PEELERS + NUM_SQUEEZERS + NUM_BOTTLERS) {
				workStations[i] = new Stations(plantId, bottlerID, "Bottler", needToBeBottled, needToBeProcessed);
				bottlerID++;
			} else if (i < NUM_FETCHERS + NUM_PEELERS + NUM_SQUEEZERS + NUM_BOTTLERS + NUM_PROCESSORS) {
				workStations[i] = new Stations(plantId, processorID, "Processor", needToBeProcessed, needToBeShipped);
				processorID++;
			}
			workStations[i].startWork();
		}
	}

	/**
	 * starts the plant's run method
	 */
	public void startPlant() {
		timeToWork = true;
		thread.start();
	}

	/**
	 * informs the plant to stop running
	 */
	public void stopPlant() {
		timeToWork = false;
		for (Stations s : workStations) {
			s.stopWork();
		}

	}

	/**
	 * waits for the plant thread to stop
	 */
	public void waitToStop() {
		// wait for the workers to stop before stopping the plant
		for (Stations s : workStations) {
			s.waitToStop();
		}

		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction ");
		}
	}

	/**
	 * starts the plant thread
	 */
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Processing oranges ");
		while (timeToWork) {
			pileOfOranges.addOrange(new Orange());
			orangesProvided++;
		}
		System.out.println("");
	}

	/**
	 * returns the total number of oranges provided to this plant
	 * 
	 * @return int orangesProvided
	 */
	public int getProvidedOranges() {
		return orangesProvided;
	}

	/**
	 * returns the total number of bottles produced at this plant
	 * 
	 * @return int getProcessed() / ORANGES_PER_BOTTLE
	 */
	public int getBottles() {
		return getProcessed() / ORANGES_PER_BOTTLE;
	}

	/**
	 * returns the total number of oranges that did not go into juice at this plant
	 * 
	 * @return int waste
	 */
	public int getWaste() {
		int waste = (getProcessed() % ORANGES_PER_BOTTLE);
		return waste;
	}

	/**
	 * returns the total number of oranges processed at this plant
	 * 
	 * @return int needToBeShipped.countOranges()
	 */
	public int getProcessed() {
		return needToBeShipped.countOranges();
	}
}
