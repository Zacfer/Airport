
/**
 * Conceptual class for a Taxiways - For the Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael, Nafiseh, Zacharias, David
 * Creation: 21/02/11
 * 
 *  Modifiied: 03/03/11 - Zacharias
 *    Added getters and setters
 *  
 */
public class Taxiway {
	
	private int UID;
	private int sectionStartID;
	private int sectionEndID;
	private double sectionWidth;
	
	public Taxiway(int UID, int sectionStartID, int sectionEndID, double sectionWidth) {
		this.UID = UID;
		this.sectionStartID = sectionStartID;
		this.sectionEndID = sectionEndID;
		this.sectionWidth = sectionWidth;
	}

	public int getUID() {
		return UID;
	}

	public int getSectionStartID() {
		return sectionStartID;
	}

	public int getSectionEndID() {
		return sectionEndID;
	}

	public double getSectionWidth() {
		return sectionWidth;
	}
}