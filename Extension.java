
/**
 * Conceptual class for a Runway Extensions - For the Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael, Nafiseh, Zacharias, David
 * Creation: 21/02/11
 * 
 * Modification: 3/3/11 - Zacharias
 *    Added getters and setters
 *  
 * Modification: 25/3/11 - David
 *    Added positioning
 *  
 * Modification: 27/3/11 - David
 *    Added additional variables, getters and setters to aid with extension markings
 *   
 */

public class Extension {

	private double length;
	private String type;
	private double width;
	private int UID;
	private String end;
	private double centerX;
	private double startY;
	
	public Extension(double length, String type, double width, int UID, String end) {
		this.length = length;
		this.type = type;
		this.width = width;
		this.UID = UID;
		this.end = end;
	}
	
	public void setPosition(double runwayX, double runwayY){
		centerX = runwayX;
		if (end.equals("BOTTOM")){
			startY = runwayY-length;
		}
		else{
			startY = runwayY;
		}
	}
	
	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public String getType() {
		return type;
	}
	
	public String getEnd() {
		return end;
	}

	public double getWidth() {
		return width;
	}

	public int getUID() {
		return UID;
	}
	
	public double getX() {
		return centerX - width/2;
	}

	public double getY() {
		return startY;
	}

	public double getCenterX() {
		return centerX;
	}
}
