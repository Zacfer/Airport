import java.awt.geom.Point2D;

/**
 * Conceptual class for a Taxiway joins - For the Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael, Nafiseh, Zacharias, David
 * Creation: 21/02/11
 * 
 *  Modifiied 03/03/11 - Zacharias
 *    Added getters and setters
 *    
 *  Modified 04/03/11 - Zacharias
 *    Changed Y value so it's according to cartesian coordinate system
 *    
 *  Modified 21/04/11 - David
 *    Added a method to access the Point of intersection
 */
public class TaxiwayJoin {
	
	private int UID;
	private double joinX;
	private double joinY;
	private int runwayConnected = 0;
	
	public TaxiwayJoin(int UID, double joinX, double joinY) {
		this.UID = UID;
		this.joinX = joinX;
		this.joinY = -joinY; // So it's according to cartesian coordinate system
	}
	
	public void setRunwayConnected(int rUID) {
		this.runwayConnected = rUID;
	}

	public int getUID() {
		return UID;
	}

	public double getJoinX() {
		return joinX;
	}

	public double getJoinY() {
		return joinY;
	}
	
	public Point2D.Double getJoinPoint() {
		Point2D.Double point = new Point2D.Double();
		point.setLocation(joinX, joinY);
		return point;
	}

	public int getRunwayConnected() {
		return runwayConnected;
	}
}