/**
 * @author Cordell 
 * The controller/driver for the orange juice bottling program.
 * Essentially, it's just the main method from the original Plant class
 * with a few minor edits.
 */
public class Orange_Juice_Bottling_Controller {

	public static final long PROCESSING_TIME = 5 * 1000;

	private static final int NUM_PLANTS = 5;

	// syntax for main was largely taken from the most recent Plant class
	public static void main(String[] args) {
		// Startup the plants
		Plant_MK_II[] plants = new Plant_MK_II[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant_MK_II(i);
			plants[i].startPlant();
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plant, and wait for it to shutdown
		for (Plant_MK_II p : plants) {
			p.stopPlant();
		}

		for (Plant_MK_II p : plants) {
			p.waitToStop();
		}

		// Summarize the results
		int totalProvided = 0;
		int totalBottles = 0;
		int totalWasted = 0;
		int totalShipping = 0;

		for (Plant_MK_II p : plants) {
			totalProvided += p.getProvidedOranges();
			totalBottles += p.getBottles();
			totalWasted += p.getWaste();
			totalShipping += p.getProcessed();

		}
		System.out.println("Total provided/processed = " + totalProvided + "/" + totalShipping);
		System.out.println("Created " + totalBottles + ", wasted " + totalWasted + " oranges");
	}

	/**
	 * provides the pause while the plants are running
	 * @param time
	 * @param errMsg
	 */
	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

}
