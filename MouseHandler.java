/**
 * Handler of mouse events
 * 
 * @author 2011SEGgp04 - Zacharias
 * Creation: 06/03/11
 *    Handles dragging around with the mouse and zooming in/out with the mouse wheel
 * 
 */
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseHandler {


	static Point dragStart = null;
	static Point dragEnd = null;
	static float zoomIncrement = 0.1f;

	static MouseMotionListener mml = new customMouseMotionListener();
	static MouseWheelListener mwl = new customMouseWheelListener();
	static MouseListener ml = new customMouseListener();

	static Airport2D airport;

	public static void init(Airport2D airport) {
		MouseHandler.airport = airport;
	}

	protected static void zoomBy(float zoom) {
		airport.zoomBy(zoom);
	}

	public static void reset() {
		airport.reset();		
	}
	
	public static void updateOffset(Point offset) {
		airport.updateOffset(offset);
	}

}

class customMouseListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		MouseHandler.airport.requestFocus();
		if(e.getButton() == 3) { //Right-click resets to default view
			MouseHandler.reset();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			MouseHandler.dragStart = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
class customMouseMotionListener implements MouseMotionListener {

	@Override
	public void mouseDragged(MouseEvent e) {
		Point newPoint = new Point(e.getX() - MouseHandler.dragStart.x, e.getY() - MouseHandler.dragStart.y);

		MouseHandler.dragStart.x += newPoint.x;
		MouseHandler.dragStart.y += newPoint.y;

		MouseHandler.updateOffset(newPoint);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

class customMouseWheelListener implements MouseWheelListener {
	public void mouseWheelMoved(MouseWheelEvent scroll) {
		MouseHandler.zoomBy(-scroll.getWheelRotation() * scroll.getScrollAmount() * MouseHandler.zoomIncrement );

	}
}	
