/*
 * Created on Dec 3, 2005 by @author Tom Jacobs
 *
 */
package tjacobs.animation;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

public class PopupMenu extends JPopupMenu implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PopupMenu() {
		super();
	}

	public PopupMenu(String arg0) {
		super(arg0);
	}
	
	public void install(Component c) {
		c.addMouseListener(this);
	}
	
	public void mousePressed(MouseEvent me) {
		maybePopup(me);
	}
	
	public void mouseReleased(MouseEvent me) {
		maybePopup(me);
	}
	
	public void mouseClicked(MouseEvent me) {
	}
	
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	
	private void maybePopup(MouseEvent me) {
		if (me.isPopupTrigger()) {
			setSize(getPreferredSize());
			this.show((Component)me.getSource(), me.getX(), me.getY());
		}
	}

}
