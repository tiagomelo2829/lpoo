package tjacobs.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import tjacobs.animation.WindowUtilities;

public class RolloverButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mStr1, mStr2;
	private ActionListener mListener1, mListener2;

	public RolloverButton(String string1, String string2) {
		this(string1, string2, null, null);
	}
	
	public RolloverButton(String string1, String string2, ActionListener listener) {
		this(string1, string2, listener, listener);
	}
	
	public RolloverButton(String string1, String string2, ActionListener listener1, ActionListener listener2) {
		super(string1);
		mStr1 = string1;
		mStr2 = string2;
		mListener1 = listener1;
		mListener2 = listener2;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//if (getText() == null && mStr1 == null) {
				//	setText(mStr2);
				//}
				if (getText().equals(mStr1)) {
					if (mListener1 != null) {
						mListener1.actionPerformed(ae);
					}
					setText(mStr2);
				}
				else {
					if (mListener2 != null) {
						mListener2.actionPerformed(ae);
					}
					setText(mStr1);					
				}
			}
		});
	}

	public static void main(String[] args) {
		
		/*ActionListener al1 = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("wah");
			}
		};
		ActionListener al2 = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("hoo");
			}
		};*/
		RolloverButton b = new RolloverButton("GR", "DEF");
		WindowUtilities.visualize(b);
	}

}
