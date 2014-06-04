/*
 * Created on Sep 16, 2004
 *
 */
package tjacobs.animation;

import tjacobs.animation.SimpleDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A button that pulls up a colorchooser window.
 * <p>
 * For picking colors
 */
public class ColorButton extends JButton implements ActionListener {
	public static final String COLOR_CHANGE = "Color Change";
	
	private static final long serialVersionUID = 0;
	
	private Component mCToScaleFrom;
	
	public ColorButton(Color c, Component cToScaleFrom) {
		//super(" ");
		changeColor(c);
		mCToScaleFrom = cToScaleFrom;
		addActionListener(this);
	}
	
	public ColorButton(Color c) {
		this(c, null);
	}
	
	public ColorButton() {
		this(Color.RED, null);
	}
	
	public void changeColor (Color c) {
		Color c2 = getBackground();
		setBackground(c);
		firePropertyChange("Color", c2, c);
		repaint();
	}
	
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	public Dimension getPreferredSize() {
		if (mCToScaleFrom == null) {
			return super.getPreferredSize();
		}
		int height = mCToScaleFrom.getHeight();
		int width = mCToScaleFrom.getWidth();
		if (height > 0 && width > 0) {
			//int min = Math.min(height, width);
			return new Dimension(height, height);
		}
		else {
			Dimension ps = mCToScaleFrom.getPreferredSize();
			return new Dimension(ps.height, ps.height);
		}
	}
	
	public void setBounds(Rectangle r) {
		super.setBounds(r);
	}
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}
	
	public void setSize(Dimension d) {
		super.setSize(d);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
	}
	
	public Color getColor() {
		return getBackground();
	}
	
	public void actionPerformed(ActionEvent ae) {
		Point p = getLocationOnScreen();
		Color c = SimpleDialogs.openColorChooserDialog(this, getColor(), p.x, p.y);
		if (c != null) {
			this.firePropertyChange(COLOR_CHANGE, getColor(), c);
			changeColor(c);
		}
	}
}