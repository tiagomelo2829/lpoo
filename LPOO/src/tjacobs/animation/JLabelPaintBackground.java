package tjacobs.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JPanel;

import tjacobs.animation.WindowUtilities;
/**
 * Override this class to respect setBackground on the label
 * @author tjacobs
 *
 */
public class JLabelPaintBackground extends javax.swing.JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JLabelPaintBackground() {
		
	}

	public JLabelPaintBackground(String arg0) {
		super(arg0);
		
	}

	public JLabelPaintBackground(Icon arg0) {
		super(arg0);
		
	}

	public JLabelPaintBackground(String arg0, int arg1) {
		super(arg0, arg1);
		
	}

	public JLabelPaintBackground(Icon arg0, int arg1) {
		super(arg0, arg1);
		
	}

	public JLabelPaintBackground(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		
	}

	public void paintComponent(Graphics g) {
		Color back = getBackground();
		if (back != null) {
			Color c = g.getColor();
			g.setColor(back);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(c);
		}
		super.paintComponent(g);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JPanel p = new JPanel(null);
		javax.swing.JLabel l = new javax.swing.JLabel("Hello1");
		JLabelPaintBackground l2 = new JLabelPaintBackground("Hello2");
		p.setPreferredSize(new Dimension(150,150));
		p.add(l);
		p.add(l2);
		l.setBackground(Color.RED);
		l.setForeground(Color.RED);
		l.setBounds(0,0,40,20);
		l2.setBounds(40,40,40,20);
		l2.setBackground(Color.ORANGE);
		//new Draggable(l);
		//new Draggable(l2);
		WindowUtilities.visualize(p);
	}

}
