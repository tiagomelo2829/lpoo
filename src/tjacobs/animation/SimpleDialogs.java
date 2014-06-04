/*
 * Created on Sep 16, 2004
 */
package tjacobs.animation;
import javax.swing.*;

import java.awt.*;

public class SimpleDialogs extends JDialog {

	private static final long serialVersionUID = 2L;
	
	private static class ColorDialog extends StandardDialog {
		private static final long serialVersionUID = 1L;
		final JColorChooser chooser = new JColorChooser();
		Color color = null;
		public ColorDialog() {
			setMainContent(chooser);
		}
		
		public ColorDialog(Color c) {
			if (c != null) chooser.setColor(c);
			setMainContent(chooser);
		}
		
		
		public void apply() {
			color = chooser.getColor();
		}
	};
	
	public static Color openColorChooserDialog (Component comp, final Color c, int x, int y) {
		ColorDialog cd = new ColorDialog(c);
		cd.setModal(true);
  		//sd.setMainContent(choos);
  		StandardDialog.showStandardDialog(cd, x, y);
  		return cd.color;
	
	}
	
	public static Color openColorChooserDialog (final Color c) {
		ColorDialog cd = new ColorDialog(c);
		cd.setModal(true);
  		//sd.setMainContent(choos);
  		StandardDialog.showStandardDialog(cd, 100, 100);
  		return cd.color;
	}
	
	public static void main(String[] args) {
		Color c = openColorChooserDialog(null);
		System.out.println(c);
	}
//	protected Container m_content;
//	protected Object LOCK = new Object();
//	
//	protected void init() {
//		//addWindowListener(new WindowClosingActions.Dispose());
//		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		m_content = getContentPane();
//	}
//	
//	protected SimpleDialogs(Frame owner) {
//		this(owner, "");
//	}
//	
//	protected SimpleDialogs(Frame owner, String title) {
//		this(owner, title, true);
//	}
//		
//	protected SimpleDialogs(Frame owner, String title, boolean modal) {
//		super(owner, title, modal);
//		init();
//	}
//
//	protected SimpleDialogs(Dialog owner) {
//		this(owner, "");
//	}
//	
//	protected SimpleDialogs(Dialog owner, String title) {
//		this(owner, title, true);
//	}
//		
//	protected SimpleDialogs(Dialog owner, String title, boolean modal) {
//		super(owner, title, modal);
//		init();
//	}
//		
//	/* Model for children. Copy - Paste, change the class name
//	 * 
//	 */
///*	
// public static class SubClass extends SimpleDialogs { 
//	public SubClass (Frame owner) {
//		this(owner, "");
//	}
//	
//	public SubClass(Frame owner, String title) {
//		this(owner, title, true);
//	}
//		
//	public SubClass(Frame owner, String title, boolean modal) {
//		super(owner, title, modal);
//		init();
//	}
//
//	public SubClass(Dialog owner) {
//		this(owner, "");
//	}
//	
//	public SubClass(Dialog owner, String title) {
//		this(owner, title, true);
//	}
//		
//	public SubClass(Dialog owner, String title, boolean modal) {
//		super(owner, title, modal);
//		init();
//	}
//	
//	protected void init() {
//		super.init();
//		// Your code here
//	}
//*/
//	
//	public static class TextFieldDialog extends SimpleDialogs { 
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//		JTextField m_field;
//		JButton m_okButton;
//		String RESULT = null;
//		
//		public TextFieldDialog (Frame owner) {
//			this(owner, "");
//		}
//		
//		public TextFieldDialog(Frame owner, String title) {
//			this(owner, title, true);
//		}
//			
//		public TextFieldDialog(Frame owner, String title, boolean modal) {
//			super(owner, title, modal);
//			init();
//		}
//
//		public TextFieldDialog(Dialog owner) {
//			this(owner, "");
//		}
//		
//		public TextFieldDialog(Dialog owner, String title) {
//			this(owner, title, true);
//		}
//			
//		public TextFieldDialog(Dialog owner, String title, boolean modal) {
//			super(owner, title, modal);
//			init();
//		}
//		
//		@SuppressWarnings("deprecation")
//		public void setOkButtonLabel(String label) {
//			m_okButton.setLabel(label);
//		}
//		
//		public void setInitialText(String text) {
//			m_field.setText(text);
//		}
//		
//		public void dispose () {
//			super.dispose();
//		}
//		
//		protected void init() {
//			super.init();
//			// Your code here
//			m_field = new JTextField();
//			m_okButton = new JButton("OK");
//			m_content.setLayout(new BorderLayout());
//			m_content.add(m_field, BorderLayout.CENTER);
//			m_content.add(m_okButton, BorderLayout.EAST);
//			ActionListener al = new ActionListener() {
//				public void actionPerformed(ActionEvent ae) {
//					RESULT = getText();
//					dispose();
//				}
//			};
//			m_okButton.addActionListener(al);
//		}
//		
//		public String getText() {
//			return m_field.getText();
//		}
//		
//	}
//		
//	public static class ColorChooserDialog extends SimpleDialogs implements OkCancelListener { 
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//		//private Color mColor;
//		private JColorChooser mChooser;
//		private Color RESULT = null;
//		
//		public ColorChooserDialog (Frame owner) {
//			this(owner, "");
//		}
//		
//		public ColorChooserDialog(Frame owner, String title) {
//			this(owner, title, true);
//		}
//			
//		public ColorChooserDialog(Frame owner, String title, boolean modal) {
//			super(owner, title, modal);
//			init();
//		}
//
//		public ColorChooserDialog(Dialog owner) {
//			this(owner, "");
//		}
//		
//		public ColorChooserDialog(Dialog owner, String title) {
//			this(owner, title, true);
//		}
//			
//		public ColorChooserDialog(Dialog owner, String title, boolean modal) {
//			super(owner, title, modal);
//			init();
//		}
//		
//		public void setColor(Color c) {
//			//mColor = c;
//			mChooser.setColor(c);
//		}
//		
//		protected void init() {
//			super.init();
//			m_content.setLayout(new BorderLayout());
//			mChooser = new JColorChooser();
//			m_content.add(mChooser, BorderLayout.CENTER);
//			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//			OkButton ok = new OkButton(this);
//			CancelButton cancel = new CancelButton(this);
//			p.add(cancel);
//			p.add(ok);
//			m_content.add(p, BorderLayout.SOUTH);
//			// Your code here
//		}
//		
//		public void ok () {
//			RESULT = mChooser.getColor();
//			dispose();
//		}
//		
//		public void cancel() {
//			dispose();
//		}
//		
//		public void dispose() {
//			super.dispose();
//			synchronized(LOCK) {
//				LOCK.notifyAll();
//			}
//		}
//	}
//	
//	public static Color openColorChooserDialog(Component c, int x, int y) throws HeadlessException {
//		return openColorChooserDialog(c, null, x, y);
//	}
//	
//	@SuppressWarnings("deprecation")
//	public static Color openColorChooserDialog(Component c, Color clr, int x, int y) throws HeadlessException {
//		ColorChooserDialog dlg;
//		Window top = SwingUtilities.getWindowAncestor(c);
//		if (top instanceof Frame) {
//			dlg = new ColorChooserDialog((Frame) top, "Choose Color", true);
//		} else if (top instanceof Dialog) {
//			dlg = new ColorChooserDialog((Dialog) top, "Chooser Color", true);
//		} else {
//			throw new HeadlessException("Component must be a Dialog or Frame or a component within a Dialog or Frame");
//		}
//		synchronized (dlg.LOCK) {
//			dlg.setColor(clr);
//			dlg.pack();
//			dlg.setLocation(x, y);
//			dlg.show();
///*			try {
//				dlg.LOCK.wait();
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//				return null;
//			}
//		}
//		*/
//		}
//		return dlg.RESULT;
//	}
//	
//	public static String openTextDialog(Component c, int x, int y) throws HeadlessException {
//		return openTextDialog(c, "", x, y);
//	}
//	
//	public static String openTextDialog(Component c, String title, int x, int y) throws HeadlessException {
//		return openTextDialog(c, title, null, null, x, y);
//	}
//	
//	public static String openTextDialog(Component c, String title, String initialtext, int x, int y) throws HeadlessException {
//		return openTextDialog(c, title, initialtext, null, x, y);
//	}
//	
//	@SuppressWarnings("deprecation")
//	public static String openTextDialog(Component c, String title, String initialtext, String buttonlabel, int x, int y) throws HeadlessException {
//		Window top = SwingUtilities.getWindowAncestor(c);
//		TextFieldDialog tfd = null; 
//		if (top instanceof Frame) {
//			tfd = new TextFieldDialog((Frame) top, title, true);
//		} else if (top instanceof Dialog) {
//			tfd = new TextFieldDialog((Dialog) top, title, true);
//		} else {
//			throw new HeadlessException("Component must be a Dialog or Frame or a component within a Dialog or Frame");
//		}
//		if (buttonlabel != null) {
//			tfd.setOkButtonLabel(buttonlabel);
//		}
//		if (initialtext != null) {
//			tfd.setInitialText(initialtext);
//		}
//		tfd.pack();
//		tfd.setLocation(x, y);
//		tfd.show();
//		return tfd.RESULT;
//	}

}
