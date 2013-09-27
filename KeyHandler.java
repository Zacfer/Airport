/**
 * Handler of key events
 * 
 * @author 2011SEGgp04 - Zacharias
 * Creation: 05/03/11
 *  
 * 
 */
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
	private static Airport2D airport;
	private static double offsetFactor = 10;

	public KeyHandler(Airport2D airport) {
		KeyHandler.airport = airport;
	}

	@Override
	public void keyPressed(KeyEvent Key) {
		double zoomIncrement = 0.1;

		// Offset is a factor divided by scale and a fraction of the zoom factor so moving around is according to the airport scale and zoom factor
		double offset = offsetFactor/(airport.scale*(1/airport.zoomFactor));

         //Zoom in with '=' key, zoom out with '-' key
		if(Key.getKeyCode() == KeyEvent.VK_EQUALS)
			airport.zoomBy(zoomIncrement);
		else if( Key.getKeyCode() == KeyEvent.VK_MINUS)
			airport.zoomBy(-zoomIncrement);
		else if( Key.getKeyCode() == KeyEvent.VK_UP){
			airport.offset.setLocation(airport.offset.getX(), airport.offset.getY()+offset);
			airport.repaint();
		}
		else if( Key.getKeyCode() == KeyEvent.VK_DOWN){
			airport.offset.setLocation(airport.offset.getX(), airport.offset.getY()-offset);
			airport.repaint();
		}
		else if( Key.getKeyCode() == KeyEvent.VK_RIGHT){
			airport.offset.setLocation(airport.offset.getX()-offset, airport.offset.getY());
			airport.repaint();
		}
		else if( Key.getKeyCode() == KeyEvent.VK_LEFT){
			airport.offset.setLocation(airport.offset.getX()+offset, airport.offset.getY());
			airport.repaint();
		}
		else if( Key.getKeyCode() == KeyEvent.VK_ENTER){
			
		}


	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
