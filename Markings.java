import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for Airport Markings - For the Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael, Nafiseh, Zacharias, David
 * Creation: 07/03/11
 * 
 * Modification: 15/3/11 - David
 *    Added rest of runway markings, taking LDA into consideration
 *  
 * Modification: 25/3/11 - David
 *    Added runway designators
 *  
 * Modification: 27/3/11 - David
 *    Added extension markings for each of the three extension types
 * 
 * Modification: 22/4/11 - David
 * 	  Added taxiway markings
 */

public class Markings {
	boolean precisionApproach = true;
	int category = 1;
	
	public ArrayList<Shape> getRunwayMarkings(Runway runway, AffineTransform at, Graphics2D g){
		
		double runwayLength = runway.getLength();
		double runwayWidth = runway.getWidth();
		double runwayOrientation = runway.getOrientation();
		double runwayX = runway.getX();
		double runwayY = runway.getY();
		double runwayCenterX = runway.getCenterX();
		double runwayCenterY = runway.getCenterY();
		double runwayLDA = runway.getLength();
		
		double width, length, spacing, offset;
		int number;
		ArrayList<Shape> markings = new ArrayList<Shape>();
		Shape marking;
		
		//Threshold
		length = 3;
		width = runwayWidth;
		
		Rectangle2D.Double threshold = new Rectangle2D.Double(runwayX, runwayY - length, width, length);
		marking = at.createTransformedShape(threshold);
		markings.add(marking);
		//Opposite end
		threshold = new Rectangle2D.Double(runwayX, runwayY + runwayLength, width, length);
		marking = at.createTransformedShape(threshold);
		markings.add(marking);
		
		//Runway Edge Markings
		if (precisionApproach){
			length = runwayLength + 6;
			width = 0.9;
			if (runwayWidth < 30){
				width = 0.45;
			}
			offset = -width;
			
			for (int i=0; i<=1; i++){
				Rectangle2D.Double edge = new Rectangle2D.Double(runwayX + offset, runwayY - 3, width, length);
				marking = at.createTransformedShape(edge);
				markings.add(marking);
				//Offset to opposite edge
				offset = runwayWidth;
			}
		}
		
		//Threshold Stripes
		if (precisionApproach || runwayLDA > 1200){
			length = 30;
			if (runwayWidth < 30){
				length = 24;
			}
			width = 1.8;
			if (!precisionApproach){
				if (runwayWidth < 45){
					width = 0.9;
					if (runwayWidth < 30){
						width = 0.6;
						if (runwayWidth < 23){
							width = 0.3;
						}
					}
				}
			}
			number = 3;
			if (precisionApproach){
				if (runwayWidth >= 30){
					number = 4;
					if (runwayWidth >= 45){
						number = 6;
					}
				}
			}
			if (runwayWidth < 23){
				number = 2;
			}
			
			for (int i=0; i<number; i++){
				Rectangle2D.Double stripe = new Rectangle2D.Double(runwayX + 0.9 + i*2*width, runwayY + 6, width, length);
				marking = at.createTransformedShape(stripe);
				markings.add(marking);
				//Opposite side
				stripe = new Rectangle2D.Double(runwayX + runwayWidth - 0.9 - width - i*2*width, runwayY + 6, width, length);
				marking = at.createTransformedShape(stripe);
				markings.add(marking);
			}
			//Opposite end
			for (int i=0; i<number; i++){
				Rectangle2D.Double stripe = new Rectangle2D.Double(runwayX + 0.9 + i*2*width, runwayY + runwayLength - length - 6, width, length);
				marking = at.createTransformedShape(stripe);
				markings.add(marking);
				//Opposite side
				stripe = new Rectangle2D.Double(runwayX + runwayWidth - 0.9 - width - i*2*width, runwayY + runwayLength - length - 6, width, length);
				marking = at.createTransformedShape(stripe);
				markings.add(marking);
			}
		}
		
		//Centreline Markings
		length = 30;
		width = 0.9;
		if (!precisionApproach){
			width = 0.45;
			if (runwayWidth < 30){
				width = 0.3;
			}
		}
		else if (runwayWidth < 30){
			width = 0.45;
		}
		if (precisionApproach){
			offset = 96;
			if (runwayWidth < 30){
				offset = 84;
			}
		}
		else{
			offset = 60;
		}
		number = (int) ((runwayLength - offset - length) / 60);
		
		for (int i=0; i<number; i++){
			Rectangle2D.Double line = new Rectangle2D.Double(runwayCenterX - width/2, runwayY + offset + i*2*length, width, length);
			marking = at.createTransformedShape(line);
			markings.add(marking);
		}
		
		//Touchdown Zone markings
		length = 22.5;
		width = 3;
		if (runwayWidth < 45){
			width = 1.5;
		}
		spacing = 150;
		offset = 3;
		if (runwayWidth > 30){
			offset = 9;
		}
		if (runwayWidth == 23){
			offset = 5;
		}
		number = 5;
		int aimingPoint = 2;
		if (runwayLDA >= 2400){
			number = 6;
			aimingPoint = 3;
		}
		if (runwayLDA < 1500){
			number = 4;
			if (runwayLDA < 1200){
				number = 3;
				if (runwayLDA < 900){
					number = 2;
					aimingPoint = 1;
				}
			}
		}
		
		for (int i=1; i<=number; i++){
			if (i != aimingPoint){
				Rectangle2D.Double line = new Rectangle2D.Double(runwayCenterX - width - offset, runwayY + i*spacing, width, length);
				marking = at.createTransformedShape(line);
				markings.add(marking);
				//opposite side
				line = new Rectangle2D.Double(runwayCenterX + offset, runwayY + i*spacing, width, length);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
		}
		//Opposite end
		for (int i=1; i<=number; i++){
			if (i != aimingPoint){
				Rectangle2D.Double line = new Rectangle2D.Double(runwayCenterX - width - offset, runwayY + runwayLength - i*spacing - length, width, length);
				marking = at.createTransformedShape(line);
				markings.add(marking);
				//opposite side
				line = new Rectangle2D.Double(runwayCenterX + offset, runwayY + runwayLength - i*spacing - length, width, length);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
		}
		
		//Aiming point marker
		length = 15;
		width = 2.5;
		if (runwayWidth > 23){
			width = 5;
			if (runwayWidth > 30){
				width = 5.5;
			}
		}
		spacing = 300;
		if (runwayLDA >= 2400){
			spacing = 400;
		}
		if (runwayLDA < 1200){
			spacing = 250;
			if (runwayLDA < 900){
				spacing = 150;
			}
		}
		
		for (int i=0; i<3; i++){
			if (i==1){
				Rectangle2D.Double mark = new Rectangle2D.Double(runwayCenterX - 2*width - offset, runwayY + spacing + i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
				//opposite side
				mark = new Rectangle2D.Double(runwayCenterX + width + offset, runwayY + spacing + i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
			}
			else{
				Rectangle2D.Double mark = new Rectangle2D.Double(runwayCenterX - width - offset, runwayY + spacing + i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
				//opposite side
				mark = new Rectangle2D.Double(runwayCenterX + offset, runwayY + spacing + i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
			}
		}
		//Opposite side
		for (int i=0; i<3; i++){
			if (i==1){
				Rectangle2D.Double mark = new Rectangle2D.Double(runwayCenterX - 2*width - offset, runwayY + runwayLength - spacing - length - i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
				//opposite side
				mark = new Rectangle2D.Double(runwayCenterX + width + offset, runwayY + runwayLength - spacing - length - i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
			}
			else{
				Rectangle2D.Double mark = new Rectangle2D.Double(runwayCenterX - width - offset, runwayY + runwayLength - spacing - length - i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
				//opposite side
				mark = new Rectangle2D.Double(runwayCenterX + offset, runwayY + runwayLength - spacing - length - i*length, width, length);
				marking = at.createTransformedShape(mark);
				markings.add(marking);
			}
		}
		
		//Runway Designator
		length = 15;
		if (runwayWidth < 45 && !precisionApproach){
			length = 12;
		}
		if (runwayWidth < 30){
			if (precisionApproach){
				length = 12;
			}
			else{
				length = 9;
			}
		}
		int endInt = (int) (runwayOrientation/10+0.5);
		int startInt = endInt +18;
		if (startInt > 36){
			startInt-=36;
		}
		String startValue = Integer.toString(startInt);
		if (startValue.length()==1){
			startValue = "0"+startValue;
		}
		String endValue = Integer.toString(endInt);
		if (endValue.length()==1){
			endValue = "0"+endValue;
		}
		Font font = new Font("Arial", Font.BOLD, (int)length);
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout startText = new TextLayout(startValue, font, frc);
		TextLayout endText = new TextLayout(endValue, font, frc);
		
		AffineTransform start = new AffineTransform();
		AffineTransform end = (AffineTransform) start.clone();
		
		if (precisionApproach){
			start.rotate(Math.PI);
			Shape numbers = startText.getOutline(start);
			start.setToTranslation(runwayCenterX + (numbers.getBounds().width)/2, runwayY + 2*length + 18);
			numbers = start.createTransformedShape(numbers);
			marking = at.createTransformedShape(numbers);
			markings.add(marking);
			// opposite side
			numbers = endText.getOutline(end);
			end.setToTranslation(runwayCenterX - (numbers.getBounds().width)/2, runwayY + runwayLength - 2*length - 18);
			numbers = end.createTransformedShape(numbers);
			marking = at.createTransformedShape(numbers);
			markings.add(marking);
		}
		else{
			start.rotate(Math.PI);
			Shape numbers = startText.getOutline(start);
			start.setToTranslation(runwayCenterX + (numbers.getBounds().width)/2, runwayY + 6);
			numbers = start.createTransformedShape(numbers);
			marking = at.createTransformedShape(numbers);
			markings.add(marking);
			// opposite side
			numbers = endText.getOutline(end);
			end.setToTranslation(runwayCenterX - (numbers.getBounds().width)/2, runwayY + runwayLength - 6);
			numbers = end.createTransformedShape(numbers);
			marking = at.createTransformedShape(numbers);
			markings.add(marking);
		}
		
		return markings;
	}
	
	public HashMap<Shape, Color> getExtensionMarkings(Extension extension, AffineTransform at, Graphics2D g){
		
		double extensionLength = extension.getLength();
		double extensionWidth = extension.getWidth();
		double extensionX = extension.getX();
		double extensionY = extension.getY();
		double extensionCenterX = extension.getCenterX();
		String extensionEnd = extension.getEnd();
		String extensionType = extension.getType();
		
		double width, length, spacing, offset;
		int number;
		HashMap<Shape, Color> markings = new HashMap<Shape, Color>();
		Shape marking;
		
		if (extensionType.equals("STARTER_EXTENSION")){
			length = 21;
			width = 0.45;
			spacing = 39;
			offset = 33;
			number = (int) ((extensionLength + 6) / 60);
			
			if (extensionEnd.equals("BOTTOM")){
				for (int i=0; i<number; i++){
					//Arrow Shaft
					Rectangle2D.Double shaft = new Rectangle2D.Double(extensionCenterX - width/2, extensionY + extensionLength - offset - length - i*(length+spacing), width, length);
					marking = at.createTransformedShape(shaft);
					markings.put(marking, Color.WHITE);
					//Arrow Head
					Path2D.Double head = new Path2D.Double();
					head.moveTo(extensionCenterX - 1.5, extensionY + extensionLength - offset - i*(length+spacing));
					head.lineTo(extensionCenterX + 1.5, extensionY + extensionLength - offset - i*(length+spacing));
					head.lineTo(extensionCenterX, extensionY + extensionLength - offset - i*(length+spacing) + 9);
					marking = at.createTransformedShape(head);
					markings.put(marking, Color.WHITE);
				}
			}
			else{
				for (int i=0; i<number; i++){
					//Arrow Shaft
					Rectangle2D.Double shaft = new Rectangle2D.Double(extensionCenterX - width/2, extensionY + offset + i*(length+spacing), width, length);
					marking = at.createTransformedShape(shaft);
					markings.put(marking, Color.WHITE);
					//Arrow Head
					Path2D.Double head = new Path2D.Double();
					head.moveTo(extensionCenterX - 1.5, extensionY + offset + i*(length+spacing));
					head.lineTo(extensionCenterX + 1.5, extensionY + offset + i*(length+spacing));
					head.lineTo(extensionCenterX, extensionY + offset + i*(length+spacing) - 9);
					marking = at.createTransformedShape(head);
					markings.put(marking, Color.WHITE);
				}
			}
		}
		else if (extensionType.equals("UNFIT_FOR_USE")){
			length = 36;
			width = 14.5;
			spacing = 1.8;
			offset = 30;
			number = (int) ((extensionLength + 6) / 72);
			
			if (extensionEnd.equals("BOTTOM")){
				for (int i=0; i<number; i++){
					//Cross 1
					Path2D.Double criss = new Path2D.Double();
					criss.moveTo(extensionCenterX - width/2, extensionY + extensionLength - offset - length - i*2*length);
					criss.lineTo(extensionCenterX - width/2 + spacing, extensionY + extensionLength - offset - length - i*2*length);
					criss.lineTo(extensionCenterX + width/2, extensionY + extensionLength - offset - i*2*length);
					criss.lineTo(extensionCenterX + width/2 - spacing, extensionY + extensionLength - offset - i*2*length);
					marking = at.createTransformedShape(criss);
					markings.put(marking, Color.WHITE);
					//Cross 2
					Path2D.Double cross = new Path2D.Double();
					cross.moveTo(extensionCenterX + width/2, extensionY + extensionLength - offset - length - i*2*length);
					cross.lineTo(extensionCenterX + width/2 - spacing, extensionY + extensionLength - offset - length - i*2*length);
					cross.lineTo(extensionCenterX - width/2, extensionY + extensionLength - offset - i*2*length);
					cross.lineTo(extensionCenterX - width/2 + spacing, extensionY + extensionLength - offset - i*2*length);
					marking = at.createTransformedShape(cross);
					markings.put(marking, Color.WHITE);
				}
			}
			else{
				for (int i=0; i<number; i++){
					//Cross 1
					Path2D.Double criss = new Path2D.Double();
					criss.moveTo(extensionCenterX - width/2, extensionY + offset + length + i*2*length);
					criss.lineTo(extensionCenterX - width/2 + spacing, extensionY + offset + length + i*2*length);
					criss.lineTo(extensionCenterX + width/2, extensionY + offset + i*2*length);
					criss.lineTo(extensionCenterX + width/2 - spacing, extensionY + offset + i*2*length);
					marking = at.createTransformedShape(criss);
					markings.put(marking, Color.WHITE);
					//Cross 2
					Path2D.Double cross = new Path2D.Double();
					cross.moveTo(extensionCenterX + width/2, extensionY + offset + length + i*2*length);
					cross.lineTo(extensionCenterX + width/2 - spacing, extensionY + offset + length + i*2*length);
					cross.lineTo(extensionCenterX - width/2, extensionY + offset + i*2*length);
					cross.lineTo(extensionCenterX - width/2 + spacing, extensionY + offset + i*2*length);
					marking = at.createTransformedShape(cross);
					markings.put(marking, Color.WHITE);
				}
			}
		}
		else if (extensionType.equals("STOPWAY")){
			length = 15;
			width = Math.sqrt(1.62);
			spacing = 30;
			offset = 25.5;
			number = (int) ((extensionLength - offset) / 30);
			
			if (extensionEnd.equals("BOTTOM")){
				//First chevron
				Path2D.Double leftSpoke = new Path2D.Double();
				leftSpoke.moveTo(extensionCenterX - length, extensionY + extensionLength - 10.5 - width);
				leftSpoke.lineTo(extensionCenterX - length, extensionY + extensionLength - 10.5);
				leftSpoke.lineTo(extensionCenterX - 7.5, extensionY + extensionLength - 3);
				leftSpoke.lineTo(extensionCenterX - 7.5 + width, extensionY + extensionLength - 3);
				marking = at.createTransformedShape(leftSpoke);
				markings.put(marking, Color.YELLOW);
				Path2D.Double rightSpoke = new Path2D.Double();
				rightSpoke.moveTo(extensionCenterX + length, extensionY + extensionLength - 10.5 - width);
				rightSpoke.lineTo(extensionCenterX + length, extensionY + extensionLength - 10.5);
				rightSpoke.lineTo(extensionCenterX + 7.5, extensionY + extensionLength - 3);
				rightSpoke.lineTo(extensionCenterX + 7.5 - width, extensionY + extensionLength - 3);
				marking = at.createTransformedShape(rightSpoke);
				markings.put(marking, Color.YELLOW);
				
				for (int i=0; i<number; i++){
					Path2D.Double chevron = new Path2D.Double();
					chevron.moveTo(extensionCenterX, extensionY + extensionLength - offset - i*spacing);
					chevron.lineTo(extensionCenterX - length, extensionY + extensionLength - offset - length - i*spacing);
					chevron.lineTo(extensionCenterX - length, extensionY + extensionLength - offset - length - width - i*spacing);
					chevron.lineTo(extensionCenterX, extensionY + extensionLength - offset - width - i*spacing);
					chevron.lineTo(extensionCenterX + length, extensionY + extensionLength - offset - length - width - i*spacing);
					chevron.lineTo(extensionCenterX + length, extensionY + extensionLength - offset - length - i*spacing);
					marking = at.createTransformedShape(chevron);
					markings.put(marking, Color.YELLOW);
				}
				//Last chevron
				length = extensionLength - offset - number*spacing - 1.5;
				Path2D.Double chevron = new Path2D.Double();
				chevron.moveTo(extensionCenterX, extensionY + extensionLength - offset - number*spacing);
				chevron.lineTo(extensionCenterX - length, extensionY + extensionLength - offset - length - number*spacing);
				chevron.lineTo(extensionCenterX - length + width, extensionY + extensionLength - offset - length - number*spacing);
				chevron.lineTo(extensionCenterX, extensionY + extensionLength - offset - width - number*spacing);
				chevron.lineTo(extensionCenterX + length - width, extensionY + extensionLength - offset - length - number*spacing);
				chevron.lineTo(extensionCenterX + length, extensionY + extensionLength - offset - length - number*spacing);
				marking = at.createTransformedShape(chevron);
				markings.put(marking, Color.YELLOW);
			}
			else{
				//First chevron
				Path2D.Double leftSpoke = new Path2D.Double();
				leftSpoke.moveTo(extensionCenterX - length, extensionY + 10.5 + width);
				leftSpoke.lineTo(extensionCenterX - length, extensionY + 10.5);
				leftSpoke.lineTo(extensionCenterX - 7.5, extensionY + 3);
				leftSpoke.lineTo(extensionCenterX - 7.5 + width, extensionY + 3);
				marking = at.createTransformedShape(leftSpoke);
				markings.put(marking, Color.YELLOW);
				Path2D.Double rightSpoke = new Path2D.Double();
				rightSpoke.moveTo(extensionCenterX + length, extensionY + 10.5 + width);
				rightSpoke.lineTo(extensionCenterX + length, extensionY + 10.5);
				rightSpoke.lineTo(extensionCenterX + 7.5, extensionY + 3);
				rightSpoke.lineTo(extensionCenterX + 7.5 - width, extensionY + 3);
				marking = at.createTransformedShape(rightSpoke);
				markings.put(marking, Color.YELLOW);
				
				for (int i=0; i<number; i++){
					Path2D.Double chevron = new Path2D.Double();
					chevron.moveTo(extensionCenterX, extensionY + offset + i*spacing);
					chevron.lineTo(extensionCenterX - length, extensionY + offset + length + i*spacing);
					chevron.lineTo(extensionCenterX - length, extensionY + offset + length + width + i*spacing);
					chevron.lineTo(extensionCenterX, extensionY + offset + width + i*spacing);
					chevron.lineTo(extensionCenterX + length, extensionY + offset + length + width + i*spacing);
					chevron.lineTo(extensionCenterX + length, extensionY + offset + length + i*spacing);
					marking = at.createTransformedShape(chevron);
					markings.put(marking, Color.YELLOW);
				}
				//Last chevron
				length = extensionLength - offset - number*spacing - 1.5;
				Path2D.Double chevron = new Path2D.Double();
				chevron.moveTo(extensionCenterX, extensionY + offset + number*spacing);
				chevron.lineTo(extensionCenterX - length, extensionY + offset + length + number*spacing);
				chevron.lineTo(extensionCenterX - length + width, extensionY + offset + length + number*spacing);
				chevron.lineTo(extensionCenterX, extensionY + offset + width + number*spacing);
				chevron.lineTo(extensionCenterX + length - width, extensionY + offset + length + number*spacing);
				chevron.lineTo(extensionCenterX + length, extensionY + offset + length + number*spacing);
				marking = at.createTransformedShape(chevron);
				markings.put(marking, Color.YELLOW);
			}
		}
		
		return markings;
	}
	
	public ArrayList<Shape> getTaxiwayMarkings(Taxiway taxiway, TaxiwayJoin start, TaxiwayJoin end, double sectionLength, AffineTransform at){
		
		double taxiwayWidth = taxiway.getSectionWidth();
		double taxiwayLength = sectionLength;
		double taxiwayX = start.getJoinX() - taxiwayWidth/2;
		double taxiwayCenterX = start.getJoinX();
		double taxiwayY = start.getJoinY();
		
		double width, length, spacing;
		ArrayList<Shape> markings = new ArrayList<Shape>();
		Shape marking;
		
		boolean startRunway = false;
		boolean endRunway = false;
		if (start.getRunwayConnected() != 0){
			startRunway = true;
		}
		if (end.getRunwayConnected() != 0){
			endRunway = true;
		}
		
		double offset;
		if (!precisionApproach){
			offset = 75;
		}
		else{
			switch (category){
				case 1:
					offset = 90;
					break;
				case 2:
					offset = 107.5;
					break;
				default:
					offset = 137;
			}
		}
		
		//Centreline marking
		width = 0.15;
		length = taxiwayLength;
		
		if (!startRunway){
			if (!endRunway){
				Rectangle2D.Double line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY, width, length);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
			else{
				Rectangle2D.Double line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY, width, length - offset - 2.1);
				marking = at.createTransformedShape(line);
				markings.add(marking);
				line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY + taxiwayLength - offset + 0.9, width, offset - 0.9);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
		}
		else{
			if (!endRunway){
				Rectangle2D.Double line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY, width, offset - 0.9);
				marking = at.createTransformedShape(line);
				markings.add(marking);
				line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY + offset + 2.1, width, length - offset - 2.1);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
			else{
				Rectangle2D.Double line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY, width, offset - 0.9);
				marking = at.createTransformedShape(line);
				markings.add(marking);
				line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY + offset + 2.1, width, length - 2*(offset - 2.1));
				marking = at.createTransformedShape(line);
				markings.add(marking);
				line = new Rectangle2D.Double(taxiwayCenterX - width/2, taxiwayY + taxiwayLength - offset + 0.9, width, offset - 0.9);
				marking = at.createTransformedShape(line);
				markings.add(marking);
			}
		}
		
		//Taxi holding bay
		if (startRunway){
			//Horizontal bars
			width = taxiwayWidth;
			length = 0.3;
			spacing = 0.6;
			
			Rectangle2D.Double bar = new Rectangle2D.Double(taxiwayX, taxiwayY + offset, width, length);
			marking = at.createTransformedShape(bar);
			markings.add(marking);
			bar = new Rectangle2D.Double(taxiwayX, taxiwayY + offset + length + spacing, width, length);
			marking = at.createTransformedShape(bar);
			markings.add(marking);
			
			//Spokes
			width = 0.3;
			length = 1.2;
			spacing = 1.8;
			
			for (int i=-3; i<4; i++){
				Rectangle2D.Double spoke = new Rectangle2D.Double(taxiwayCenterX - 0.45 - i*spacing, taxiwayY + offset, width, length);
				marking = at.createTransformedShape(spoke);
				markings.add(marking);
				spoke = new Rectangle2D.Double(taxiwayCenterX + 0.15 - i*spacing, taxiwayY + offset, width, length);
				marking = at.createTransformedShape(spoke);
				markings.add(marking);
			}
		}
		if (endRunway){
			//Horizontal bars
			width = taxiwayWidth;
			length = 0.3;
			spacing = 0.6;
			
			Rectangle2D.Double bar = new Rectangle2D.Double(taxiwayX, taxiwayY + taxiwayLength - offset - length, width, length);
			marking = at.createTransformedShape(bar);
			markings.add(marking);
			bar = new Rectangle2D.Double(taxiwayX, taxiwayY + taxiwayLength - offset - 2*length - spacing, width, length);
			marking = at.createTransformedShape(bar);
			markings.add(marking);
			
			//Spokes
			width = 0.3;
			length = 1.2;
			spacing = 1.8;
			
			for (int i=-3; i<4; i++){
				Rectangle2D.Double spoke = new Rectangle2D.Double(taxiwayCenterX - 0.45 - i*spacing, taxiwayY + taxiwayLength - offset - length, width, length);
				marking = at.createTransformedShape(spoke);
				markings.add(marking);
				spoke = new Rectangle2D.Double(taxiwayCenterX + 0.15 - i*spacing, taxiwayY + taxiwayLength - offset - length, width, length);
				marking = at.createTransformedShape(spoke);
				markings.add(marking);
			}
		}
		
		return markings;
	}
	
	public void setPrecision(boolean b){
		precisionApproach = b;
	}
	
	public void setCategory(int c){
		category = c;
	}
}
