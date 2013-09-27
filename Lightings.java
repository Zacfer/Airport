import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


/**
 * Class Lighting for Airport lighting.
 * 
 * @author 2011SEGgp04 - Nafiseh
 * 	Creation: 13/04/11
 *
 * Modified 14/04/11 - Nafiseh
 * 	added centre and edge lights in runway.
 * 
 * Modified 15/04/11 - Nafiseh
 * 	added supplementary lights.
 * 
 * Modified 16/04/11 - Nafiseh
 * 	High intensity coded centreline and crossbar approach lighting system and White lights in touch down zone (TDZ).
 */


public class Lightings {
	public Lightings(){

	}

	public ArrayList<Shape> getRunwayLightings(Runway runway, AffineTransform at, Graphics2D g){

		double runwayLength = runway.getLength();
		double runwayWidth = runway.getWidth();
		double runwayX = runway.getX();
		double runwayY = runway.getY();
		double runwayCenterX = runway.getCenterX();
		double runwayCenterY = runway.getCenterY();
		double runwayLDA = runway.getLength();
		int diameter = 1;
		Shape lighting;
		Light tmpLight;
		int thresh = 4;

		ArrayList<Shape> lightings = new ArrayList<Shape>();


		//Here is a light drawn at center with color black:
		tmpLight = new Light(runwayCenterX-diameter/2, runwayCenterY, diameter);
		//lighting = new Light (at.createTransformedShape(tmpLight),Color.black);
		//lightings.add(lighting);

		double width, length, spacing, offset;

		// Lighting for the edge of the runaways and colour coding for the yellow caution zone.

		double lightNo = runwayLength/60;
		for (int i=0; i<=lightNo; i++){
			tmpLight = new Light(runwayX, runwayY+(60*i), diameter);
			if(i>=2*lightNo/3)
				lighting =new Light (at.createTransformedShape(tmpLight),Color.yellow);
			else
				lighting =new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);

			tmpLight = new Light(runwayX+runwayWidth-diameter, runwayY+(60*i), diameter);
			if(i>=2*lightNo/3)
				lighting =new Light (at.createTransformedShape(tmpLight),Color.yellow);
			else
				lighting =new Light (at.createTransformedShape(tmpLight),Color.white);
			
			lightings.add(lighting);
		}

		// Lighting for the centre of the runaway.
		int distanceBetweenLights = 30;
		lightNo = runwayLength/distanceBetweenLights;
		//System.out.println(runwayLength);
		for (int i=0; i<=lightNo; i++){
			tmpLight = new Light(runwayCenterX-diameter/2, runwayY+(distanceBetweenLights*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);

		}

		// Supplementary approach lighting (cat II and III)
		lightNo = 300/5;
		for(int i= 0; i<=lightNo; i++){
			tmpLight = new Light(runwayCenterX-diameter/2, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);

		}

		// Lighting in Crossbars.
		lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*8*diameter/2, 9, runwayCenterY-runwayLength/2-150-thresh, 3));
		lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*10*diameter/2, 11, runwayCenterY-runwayLength/2-300-thresh, 3));
		lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*12*diameter/2, 13, runwayCenterY-runwayLength/2-450-thresh, 3));
		lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*14*diameter/2, 15, runwayCenterY-runwayLength/2-600-thresh, 3));
		lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*16*diameter/2, 17, runwayCenterY-runwayLength/2-750-thresh, 3));

		lightNo = 300/5;
		for(int i=0; i<=lightNo; i++)
		{
			lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*2*diameter/2+2.5-diameter, 2, runwayCenterY-runwayLength/2-(300+5*i)-thresh, 3));
			lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-3*3*diameter/2+2.5-diameter, 3, runwayCenterY-runwayLength/2-(600+5*i)-thresh, 3));
		}


		//	High intensity coded centreline and crossbar approach lighting system.
		lightNo = 300/5;
		for(int i= 0; i<=lightNo; i++)
		{	
			// The black lights in the middle.
			tmpLight = new Light(runwayCenterX-diameter/2-2, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.black);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-3.2, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.black);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+2, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.black);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+3.2, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.black);
			lightings.add(lighting);

			// The edge light (4 red each side)
			tmpLight = new Light(runwayCenterX-diameter/2-10, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-13, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-16, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-19, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);

			// The other side red lights.
			tmpLight = new Light(runwayCenterX-diameter/2+10, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+13, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+16, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+19, runwayY-(5*i)-thresh, diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.red);
			lightings.add(lighting);


		}

		 //White lights in touch down zone (TDZ).
		lightNo = 900/5;
		for(int i= 0; i<=lightNo; i++)
		{	
		//	lightings.addAll(HorizLights(at, tmpLight, runwayCenterX-runwayWidth/2-diameter/2+3, 4, runwayY+(5*i), 4, Color.white));
		//	lightings.addAll(HorizLights(at, tmpLight, runwayCenterX+runwayWidth/4-diameter/2-3, 4, runwayY+(5*i), 4, Color.white));
			// The edge light (4 red each side)
			tmpLight = new Light(runwayCenterX-diameter/2-10, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-13, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-16, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2-19, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);

			// The other side red lights.
			tmpLight = new Light(runwayCenterX-diameter/2+10, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+13, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+16, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);
			tmpLight = new Light(runwayCenterX-diameter/2+19, runwayY+(5*i), diameter);
			lighting = new Light (at.createTransformedShape(tmpLight),Color.white);
			lightings.add(lighting);

		}
		return lightings;
			
	}



	// Function to draw the Horizontal line.
	private ArrayList<Shape> HorizLights(AffineTransform at,Light l ,double startX, double lightNo, double Y, double DirectionAndDistanceBetweenLights ){
		int drawnLights = 0;
		ArrayList<Shape> lightings = new ArrayList<Shape>();
		for(double i = startX; drawnLights < lightNo; i+=DirectionAndDistanceBetweenLights){
			Light tmpl = new Light(i,Y,l.diameter); 
			Light lighting =new Light (at.createTransformedShape(tmpl),Color.white);
			lightings.add(lighting);
			drawnLights++;
		}


		return lightings;
	}

	// Function to draw the Horizontal line with colour.
	private ArrayList<Shape> HorizLights(AffineTransform at,Light l ,double startX, double lightNo, double Y, double DirectionAndDistanceBetweenLights, Color color){
		int drawnLights = 0;
		ArrayList<Shape> lightings = new ArrayList<Shape>();
		for(double i = startX; drawnLights < lightNo; i+=DirectionAndDistanceBetweenLights){
			Light tmpl = new Light(i,Y,l.diameter); 
			Light lighting =new Light (at.createTransformedShape(tmpl),color);
			lightings.add(lighting);
			drawnLights++;
		}


		return lightings;
	}



}

