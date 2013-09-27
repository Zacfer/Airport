/**
 * 2D Rendering of Airport
 * 
 * @author 2011SEGgp04 - Zacharias
 * Creation: 03/03/11
 *  
 *  Modified:  04/03/11 - Zacharias
 *     Corrected the rendering - corrected the rotations, centered the airport.
 *     
 *  Modified: 05/03/11 - Zacharias
 *     Added rendering for taxiways, added zoom and move around capability with keys
 * 
 *  Modified: 06/03/11 - Zacharias
 *     Added support for dragging around with the mouse, and zooming in/out with the mouse wheel.
 *     Made some minor changes and added a few comments
 *     
 *  Modified 06/03/11 - Michael
 *     Updated overall model to add airport to main GUI, allowed user to choose whether taxiways are rendered, and select an individual runway.
 *     Added code to allow user to rotate the airport/runway
 *    
 *  Modified 08/03/11 - David
 *     Added capability to draw runway markings
 *  
 * 	Modified 12/03/11 - Michael
 * 	   Corrected markings rendering - now only appropriate markings shown.
 * 
 *  Modified 16/03/11 - Michael
 *	   Fixed rotation issue (when dragging mouse)
 *
 *  Modified 25/03/11 - David
 *     Added rendering of runway extensions
 *  
 *  Modification: 27/3/11 - David
 *     Added rendering of extension markings
 *     
 *  Modified 13/4/11 -Nafiseh
 *  	Added rendering of runway lighting
 *  
 *  Modified 21/4/11 - David
 *  	Altered taxiways to be Rects rather than lines, including giving them orientations
 *  
 *  Modified 22/4/11 - David
 *  	Added rendering of taxiway markings
 *  	Implemented compliance ratings
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Airport2D extends JComponent{
	Airport airport;
	double zoomFactor = 1.0;
	Point2D.Double offset = new Point2D.Double();
	double scale = 1.0;
	boolean renderTaxiways = true;
	double rotateRunwayValue = 180;
	private int setRunway = 0;
	boolean fullScreen = false;
	Dimension fullScreenSize;
	boolean night = false;

	Markings markingsControl = new Markings();
	Lightings lightingControl = new Lightings();
	
	String compliance = "Blank";
	boolean blank = true;
	
//	public static void main(String[] args) {
//		Airport2D a2D = new Airport2D(new File("src/complex.apx"));
//
//		JFrame frame = new JFrame();
//
//		frame.setSize(600, 600);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		frame.add(a2D);
//	}

	void taxiways() {
		renderTaxiways = (!renderTaxiways);
		this.repaint();
	}

	public void setRunway(int arg0) {
		int oldValue=setRunway;
		setRunway = arg0;
		if(oldValue!=setRunway) {
			if(setRunway!=0) renderTaxiways=false;
			this.repaint();
		}
	}

	void reset() {
		zoomFactor = 1.0;
		offset = new Point2D.Double();
		this.repaint();
	}

	public Airport2D(File i) {
		airport = new Airport(i);
		this.setFocusable(true);
		KeyHandler keyHandler = new KeyHandler(this);
		this.addKeyListener(keyHandler);
		setListeners();
	}	

	public ArrayList<Runway> getRunways() {
		return airport.runways;
	}

	void setListeners() {
		MouseHandler.init(this);
		addMouseMotionListener(MouseHandler.mml);
		addMouseListener(MouseHandler.ml);
		addMouseWheelListener(MouseHandler.mwl);
	}

	public void setRunwayRotation(int arg0) {
		rotateRunwayValue = arg0 ;
		this.repaint();
	}
	
	public void setNewSize(Dimension arg0) {
		fullScreen = false;
		this.setSize(arg0);
	}
	
	public void setNewSize(int width, int height) {
		fullScreen = true;
		fullScreenSize = new Dimension(width, height);
		repaint();
	}
	
	public void setCompliance(String comp){
		compliance = comp;
		if (compliance.equals("Blank")){
			blank = true;
		}
		else{
			blank = false;
			if (compliance.equals("Visual") || compliance.equals("Non Precision")){
				markingsControl.setPrecision(false);
			}
			else{
				markingsControl.setPrecision(true);
				if (compliance.equals("CAT I")){
					markingsControl.setCategory(1);
				}
				else if (compliance.equals("CAT II")){
					markingsControl.setCategory(2);
				}
				else{
					markingsControl.setCategory(3);
				}
			}
		}
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(fullScreen) {
			this.setSize(fullScreenSize);
		}
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		// Rendering is done with antialiasing. 
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Rendering algorithms are chosen with a preference for output quality. 
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		ArrayList<Runway> runways = airport.runways;
		ArrayList<Taxiway> taxiways = airport.taxiways;

		// Bounding Rectangle of the entire airport
		Rectangle2D boundBox = null;

		// Current dimensions available for drawing.
		Rectangle _frameBox = g2d.getClipBounds();

		if(night) {
			g2d.setColor(Color.BLACK);
		} else {
			g2d.setColor(Color.decode("#4DBD33")); // Grass
		}
		
		g2d.fill((Rectangle2D)_frameBox);

		// Shapes of all runways.
		ArrayList<Shape> runwayShapes = new ArrayList<Shape>();
		ArrayList<Shape> taxiwayShapes = new ArrayList<Shape>();

		ArrayList<Shape> runwayMarkings = new ArrayList<Shape>();
		ArrayList<Shape> runwayLightings = new ArrayList<Shape>();

		ArrayList<Shape> taxiwayMarkings = new ArrayList<Shape>();
		
		HashMap<Shape, Color> extensionMarkings = new HashMap<Shape, Color>();

		if(setRunway==0) {
			for (Runway runway : runways) {
				Rectangle2D.Double runwayRect = new Rectangle2D.Double(runway.getX(), runway.getY(), runway.getWidth(), runway.getLength());
				// AT object for rotating runway.
				AffineTransform at = new AffineTransform();
				at.rotate((runway.getOrientation()*Math.PI/180), runway.getCenterX(), runway.getCenterY());
				// Rotated shape of runway.
				Shape rotatedRect = at.createTransformedShape(runwayRect);
				runwayShapes.add(rotatedRect);

				// Recalculation of bounds
				if(boundBox == null) {
					boundBox = rotatedRect.getBounds2D();
				} else {
					boundBox = boundBox.createUnion(rotatedRect.getBounds2D()); // By getting the union of the bounding boxes of each rendered object, we obtain a bound box that defines the bounds of the airport
				}
				
				//Extensions
				Extension bottom = runway.getBottomExtension();
				if (bottom != null){
					Rectangle2D.Double extRect = new Rectangle2D.Double(bottom.getX(), bottom.getY(), bottom.getWidth(), bottom.getLength());
					Shape rotatedExt = at.createTransformedShape(extRect);
					runwayShapes.add(rotatedExt);
					boundBox = boundBox.createUnion(rotatedExt.getBounds2D());
					
					//Markings
					if (!blank){
						extensionMarkings.putAll(markingsControl.getExtensionMarkings(bottom, at, g2d));
					}
				}
				Extension top = runway.getTopExtension();
				if (top != null){
					Rectangle2D.Double extRect = new Rectangle2D.Double(top.getX(), top.getY(), top.getWidth(), top.getLength());
					Shape rotatedExt = at.createTransformedShape(extRect);
					runwayShapes.add(rotatedExt);
					boundBox = boundBox.createUnion(rotatedExt.getBounds2D());
					
					//Markings
					if (!blank){
						extensionMarkings.putAll(markingsControl.getExtensionMarkings(top, at, g2d));
					}
				}
				
				if (!blank){
					//Markings
					for (Shape marking : markingsControl.getRunwayMarkings(runway, at, g2d)){
						runwayMarkings.add(marking);
					}				
					///////////////////////////////////////////////////////////////////////////////////////////////////
					//Lighting
					for (Shape lighting : lightingControl.getRunwayLightings(runway, at, g2d)){
						runwayLightings.add(lighting);
					}	
					//////////////////////////////////////////////////////////////////////////////////////////////////
				}
			}
		} else {
			Runway runway = runways.get(setRunway-1);
			Rectangle2D.Double runwayRect = new Rectangle2D.Double(runway.getX(), runway.getY(), runway.getWidth(), runway.getLength());
			// AT object for rotating runway.
			AffineTransform at = new AffineTransform();
			at.rotate(((runway.getOrientation()*Math.PI)/180), runway.getCenterX(), runway.getCenterY());
			// Rotated shape of runway.
			Shape rotatedRect = at.createTransformedShape(runwayRect);
			runwayShapes.add(rotatedRect);

			// Recalculation of bounds
			boundBox = rotatedRect.getBounds2D();
			
			//Extensions
			Extension bottom = runway.getBottomExtension();
			if (bottom != null){
				Rectangle2D.Double extRect = new Rectangle2D.Double(bottom.getX(), bottom.getY(), bottom.getWidth(), bottom.getLength());
				Shape rotatedExt = at.createTransformedShape(extRect);
				runwayShapes.add(rotatedExt);
				boundBox = boundBox.createUnion(rotatedExt.getBounds2D());
				
				//Markings
				if (!blank){
					extensionMarkings.putAll(markingsControl.getExtensionMarkings(bottom, at, g2d));
				}
			}
			Extension top = runway.getTopExtension();
			if (top != null){
				Rectangle2D.Double extRect = new Rectangle2D.Double(top.getX(), top.getY(), top.getWidth(), top.getLength());
				Shape rotatedExt = at.createTransformedShape(extRect);
				runwayShapes.add(rotatedExt);
				boundBox = boundBox.createUnion(rotatedExt.getBounds2D());
				
				//Markings
				if (!blank){
					extensionMarkings.putAll(markingsControl.getExtensionMarkings(top, at, g2d));
				}
			}
			
			if (!blank){
				//Markings
				for (Shape marking : markingsControl.getRunwayMarkings(runway, at, g2d)){
					runwayMarkings.add(marking);
				}			
				///////////////////////////////////////////////////////////////////////////////////////////////////
				//Lighting
				for (Shape lighting : lightingControl.getRunwayLightings(runway, at, g2d)){
					runwayLightings.add(lighting);
				}	
				//////////////////////////////////////////////////////////////////////////////////////////////////
			}
		}

		if(renderTaxiways) {
			for (Taxiway taxiway: taxiways) {
				TaxiwayJoin start = airport.getJoin(taxiway.getSectionStartID());
				TaxiwayJoin end = airport.getJoin(taxiway.getSectionEndID());
				
				double taxiwayLength = start.getJoinPoint().distance(end.getJoinPoint());
				Point2D.Double tempTaxiwayEnd = new Point2D.Double();
				tempTaxiwayEnd.setLocation(start.getJoinX(), start.getJoinY() + taxiwayLength);
				double oppositeLength = end.getJoinPoint().distance(tempTaxiwayEnd);
				double taxiwayOrientation = Math.acos((2*taxiwayLength*taxiwayLength - oppositeLength*oppositeLength)/(2*taxiwayLength*taxiwayLength));
				if (start.getJoinX() <= end.getJoinX()){
					taxiwayOrientation = 2*Math.PI - taxiwayOrientation;
				}
				
				// AT object for rotating taxiway.
				AffineTransform at = new AffineTransform();
				at.rotate(taxiwayOrientation, start.getJoinX(), start.getJoinY());
				
				Line2D.Double line = new Line2D.Double(start.getJoinX(), start.getJoinY(), tempTaxiwayEnd.getX(), tempTaxiwayEnd.getY());
				g2d.setStroke(new BasicStroke((float) taxiway.getSectionWidth()));
				Shape fullTaxiway = g2d.getStroke().createStrokedShape(line);
				Shape rotatedTaxiway = at.createTransformedShape(fullTaxiway);
				
				taxiwayShapes.add(rotatedTaxiway);
				boundBox = boundBox.createUnion(fullTaxiway.getBounds2D());
				
				//Markings
				if (!blank){
					for (Shape marking : markingsControl.getTaxiwayMarkings(taxiway, start, end, taxiwayLength, at)){
						taxiwayMarkings.add(marking);
					}
				}
			}
		}


		if(boundBox != null) {

			AffineTransform border = new AffineTransform();
			if(zoomFactor == 1) {
				border.scale(0.9, 0.9); // Scale so airport fits 90% in the window to have some space around the borders.
			} else {
				border.scale(zoomFactor, zoomFactor);
			}
			Rectangle frameBox = border.createTransformedShape(_frameBox).getBounds();

			double boundRatio = (double)boundBox.getHeight() / boundBox.getWidth();
			double frameRatio = (double)frameBox.height/frameBox.width;
			double translateX = -boundBox.getX();
			double translateY = -boundBox.getY();

			if (boundRatio < frameRatio) {
				// Scale by width
				scale = frameBox.width / boundBox.getWidth();			
			} else {
				// Scale by height
				scale = frameBox.height / boundBox.getHeight();
			}

			translateX += (_frameBox.width/scale - boundBox.getWidth())/2;
			translateY += (_frameBox.height/scale - boundBox.getHeight())/2;
			
			// Transformations
			AffineTransform transformer = new AffineTransform();
			transformer.scale(scale, scale);
			transformer.translate(translateX, translateY);
			transformer.translate(offset.x, offset.y);
			double rotationOffset = ((rotateRunwayValue/360)*Math.PI)-Math.PI/2;
			transformer.rotate(rotationOffset,this.getWidth()/2,this.getHeight()/2);
			g2d.setTransform(transformer);

			g2d.setColor(Color.GRAY); //Taxiway Lines
			// Drawing all taxiways
			for (Shape taxiwayShape : taxiwayShapes) {
				g2d.fill(taxiwayShape);
			}

			g2d.setColor(Color.GRAY); //Asphalt
			// Drawing all runways
			for (Shape runwayShape : runwayShapes) {
				g2d.fill(runwayShape);
			}
			if(!night) {
				// Drawing all extension markings
				for (Shape extensionMarking : extensionMarkings.keySet()){
					g2d.setColor(extensionMarkings.get(extensionMarking));
					g2d.fill(extensionMarking);
				}
				
				g2d.setColor(Color.YELLOW); //Taxiway Markings
				// Drawing all taxiway markings
				for (Shape taxiwayMarking : taxiwayMarkings) {
					g2d.fill(taxiwayMarking);
				}
				
				g2d.setColor(Color.WHITE); //Runway Markings
				// Drawing all runway markings
				for (Shape runwayMarking : runwayMarkings) {
					g2d.fill(runwayMarking);
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////
			// Drawing all runway lightings
			for (Shape runwayLighing : runwayLightings) {
				if(runwayLighing instanceof Light){
					Light tmpLight = (Light) runwayLighing;
					g2d.setColor(tmpLight.color);
					g2d.fill(tmpLight.lightTransformed);
				}
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////
			// Draw Bounding Box
			//g2d.draw(boundBox);
		}
		
	}
	//
	public void zoomBy(double zoom) {
		zoomFactor += zoom;
		zoomFactor = Math.max(1, zoomFactor);
		if(Double.compare(zoomFactor, 1) == 0) reset();
		else repaint();

	}

	public void updateOffset(Point offset)	{
		this.offset.x += offset.x;
		this.offset.y += offset.y;
		this.repaint();
	}



}
