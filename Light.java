import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Class Light for Airport lighting.
 * 
 * @author 2011SEGgp04-Nafiseh
 * Creation: 13/04/11
 *
 */


public class Light extends Ellipse2D.Double{

	double diameter;
	Shape lightTransformed;
	Color color = Color.white;
	
	public Light(double x, double y,double diameter){
		super(x,y,diameter,diameter);
		this.diameter = diameter;
	}
	public Light(Shape shape,Color color){
		this.color =color;
		lightTransformed = shape;
	}
}



