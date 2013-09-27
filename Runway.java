
/**
 * Conceptual class for a Runway - For the Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael
 * Creation: 21/02/11
 * 
 * Modified: 03/03/11 - Zacharias
 *    Added getters and setters
 *    
 * Modified: 04/03/11 - Zacharias   
 *    Changed centerY to -centerY so that positive Y is according to cartesian coordinate system
 *    
 * Modified: 25/03/11 - David
 *    Added Extension positioning
 *    
 */
public class Runway {

	private int UID;
	private double centerX;
	private double centerY;
	private double orientation;
	private double length;
	private double width;
	private Extension bottomExtension = null;
	private Extension topExtension = null;

	public Runway(int UID, double centerX, double centerY, double orientation, double length, double width) {
		this.UID = UID;
		this.centerX = centerX;
		this.centerY = -centerY;
		this.orientation = orientation;
		this.length = length;
		this.width = width;
	}

	public void setTopExtension(Extension arg0) {
		this.topExtension = arg0;
		
		topExtension.setPosition(centerX, getY()+length);
		if(topExtension.getWidth() == 0){
			topExtension.setWidth(width);
		}
	}

	public void setBottomExtension(Extension arg0) {
		this.bottomExtension = arg0;
		
		bottomExtension.setPosition(centerX, getY());
		if(bottomExtension.getWidth() == 0){
			bottomExtension.setWidth(width);
		}
	}

	@Override
	public String toString() {
		String output = "Runway " + UID + " : ";
		output += "   centerX : " + centerX;
		output += "   centerY : " + centerY;
		output += "   X : " + getX();
		output += "   Y : " + getY();
		output += "   orientation : " + orientation;
		output += "   length : " + length;
		output += "   width : " + width;
		return output;
	}


	public int getUID() {
		return UID;
	}

	public double getX() {
		return centerX - width/2;
	}

	public double getY() {
		return centerY - length/2;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public double getOrientation() {
		return orientation;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public Extension getBottomExtension() {
		return bottomExtension;
	}

	public Extension getTopExtension() {
		return topExtension;
	}
}
