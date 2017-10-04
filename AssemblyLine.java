import java.util.ArrayList;
import java.util.List;

/**
 * @author Nate Williams
 * Original layout was fleshed out by Andrew Kerwin and I completed the modifications necessary for it to function
 * This class provides a list for workers add oranges to and take oranges from
 */
public class AssemblyLine {
	private final List<Orange> oranges;
	
	AssemblyLine() {
		oranges = new ArrayList<Orange>(); 
	}
	
	/**
	 * receives an orange and adds it to the list 
	 * @param o Orange
	 */
	public synchronized void addOrange(Orange o) {
		oranges.add(o);
		if (countOranges() == 1) {
			try {
				notifyAll();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * removes the 0th element from the list
	 * @return Orange oranges[0]
	 */
	public synchronized Orange getOrange() {
		while (countOranges() == 0) {
			try {
				wait();
			} catch (InterruptedException ignored) {}
		}
		//edited to actually remove the orange from the list
		return oranges.remove(0);
	}
	
	/**
	 * returns the number of oranges in the list
	 * @return int oranges.size()
	 */
	public synchronized int countOranges() {
		return oranges.size();
	}
	
}
