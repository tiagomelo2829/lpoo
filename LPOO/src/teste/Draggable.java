package teste;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tjacobs.animation.JLabelPaintBackground;
import tjacobs.animation.WindowUtilities;

/** 
 * tjacobs.ui.Draggable<p>
 * Makes a component draggable. Does not work if there's a layout manager
 * messing with things<p>
 * <code>
 *  usage: 
 *  			Component c = ...
 *  			new Draggable(c);
 *  			parent.add(c);
 *  </code>
 */

public class Draggable extends MouseAdapter implements MouseMotionListener, ComponentListener {
    Point mLastPoint;
    Component mDraggable;
    //boolean mMultipleDragComponents = false;   
    
    public Draggable(Component w) {
        w.addMouseMotionListener(this);
        w.addMouseListener(this);
        //w.addComponentListener(this);
        mDraggable = w;
    }
	
    /**
     * For adding a handle
     * @param w
     * @param drag
     */
	public Draggable(Window w, Component drag) {
		drag.addMouseMotionListener(this);
        drag.addMouseListener(this);
        drag.addComponentListener(this);
		mDraggable = w;
	}
			
    public void mousePressed(MouseEvent me) {
//    	if (mMultipleDragComponents) {
//    		mDraggable = me.getComponent();
//    	}
    	System.out.println("here");
    	System.out.println(mDraggable.getX() + ", " + mDraggable.getY());
    	
		if (mDraggable.getCursor().equals(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))) {
			mLastPoint = me.getPoint();
			if (!(mDraggable instanceof Window)) {
				mDraggable.getParent().add(mDraggable, 0);
				mDraggable.getParent().repaint();
			}
		}
		else {
			mLastPoint = null;
		}
    }
    
//    public void makeComponentDraggable(Component c) {
//    	c.addMouseListener(this);
//    	c.addMouseMotionListener(this);
//    	mMultipleDragComponents = true;
//    }
	
	private void setCursorType(Point p) {
		Point loc = mDraggable.getLocation();
		Dimension size = mDraggable.getSize();
		if ((p.y + WindowUtilities.RESIZE_MARGIN_SIZE < loc.y + size.height) && (p.x + WindowUtilities.RESIZE_MARGIN_SIZE < p.x + size.width)) {
			mDraggable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	public void mouseExited(MouseEvent me) {
		
		if (mLastPoint == null){
			mDraggable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
    
    public void mouseReleased(MouseEvent me) {
    	int w = 40, h = 15;
    	//if(b1.getX() + w >= tf.getX() && tf.getX() + w >= b1.getX() && b1.getY() + h >= tf.getY() && tf.getY() + h >= b1.getY())
			//System.out.println("Estou em cima...kkkkkk");
        //mLastPoint = null;
    }
    public void mouseMoved(MouseEvent me) {
		setCursorType(me.getPoint());
    }
    public void mouseDragged(MouseEvent me) {
        
        int x, y;
        	
        if (mLastPoint != null) {
            x = mDraggable.getX() + (me.getX() - (int)mLastPoint.getX());
            y = mDraggable.getY() + (me.getY() - (int)mLastPoint.getY());
            if(x > 300 || y > 300)
            	return;
            mDraggable.setLocation(x, y);
        }
    }
    
    public void mouseEntered(MouseEvent me){
    	
    	System.out.println("Dentro de um objeto");
    }
        
    public void componentShown(ComponentEvent ce) {}
    
    public void componentResized(ComponentEvent ce) {}
    
    public void componentHidden(ComponentEvent ce) {
    	Component c = ce.getComponent();
    	c.removeMouseListener(this);
    	c.removeMouseMotionListener(this);
    	c.removeComponentListener(this);
    }
    
    public void componentMoved(ComponentEvent ce) {}
}
