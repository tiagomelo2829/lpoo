package tjacobs.animation;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * KeyboardCommands is an interface for associating a KeyStroke or
 * a sequence of KeyStrokes with an Action
 * @author HP_Administrator
 *
 */
public final class KeyboardCommands implements KeyEventDispatcher {

	public static final int CONSUME_PRESSED = 1;
	public static final int CONSUME_RELEASED = 2;
	public static final int CONSUME_TYPED = 4;
	
	private static KeyboardCommands sSingleton;
	private KeyActionEvent mKeyEv;

	private Map<KeyStroke, Action> mPressCommands = new HashMap<KeyStroke, Action>();
	private Map<KeyStroke, Action> mReleaseCommands = new HashMap<KeyStroke, Action>();
	private Map<KeyStroke, Action> mTypedCommands = new HashMap<KeyStroke, Action>();
	private Map<KeyStroke, Integer> mConsumeKeys = new HashMap<KeyStroke, Integer>();
	private List<Action> mAnyKey;
	private ArrayList<KeySequence> mKeySequences;
	
	private final static class KeySequence {
		public KeyStroke[] mKeyStrokes;
		public Action mAction;
		public int mLocation = 0;
		
		public KeySequence(KeyStroke[] keys, Action a) {
			mKeyStrokes = keys;
			mAction = a;
		}
		
		public void keyStruck(KeyStroke ks) {
			if (!ks.equals(mKeyStrokes[mLocation])) {
				mLocation = 0;
			}
			else {
				mLocation++;
				//System.out.println("location = " + mLocation);
				if (mLocation >= mKeyStrokes.length) {
					mLocation = 0;
					mAction.actionPerformed(new ActionEvent(this, 0, "seq" , System.currentTimeMillis(), 0));
				}
			}
		}
		
		public boolean equals(Object o) {
			if (o == this) return true;
			if (! (o instanceof KeySequence)) return false;
			KeySequence ks = (KeySequence) o;
			if (mKeyStrokes.length != ks.mKeyStrokes.length) return false;
			for (int i = 0; i < mKeyStrokes.length; i++) {
				if (!mKeyStrokes[i].equals(ks.mKeyStrokes[i])) return false;
			}
			return true;
		}
	}
	
	public final class KeyActionEvent extends ActionEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		KeyEvent mKey;
		public KeyActionEvent(KeyEvent ke) {
			super(ke.getSource(), 0, "");
			mKey = ke;
		}
		
		void setEvent(KeyEvent ke) {
			mKey = ke;
		}
		
		public KeyEvent getKeyEvent() {
			return mKey;
		}
		
		public int getID() {
			return mKey.getID();
		}
		
		public String getActionCommand() {
			
			return mKey.getID() == KeyEvent.KEY_TYPED ? KeyEvent.getKeyText(mKey.getKeyChar()) : mKey.getModifiers() != 0 ? KeyEvent.getKeyModifiersText(mKey.getModifiers()) + " " + KeyEvent.getKeyText(mKey.getKeyCode()) : KeyEvent.getKeyText(mKey.getKeyCode());
		}
		
		public int getModifiers() {
			return mKey.getModifiers();
		}
		
		public long getWhen() {
			return mKey.getWhen();
		}		
	}
	
	private KeyboardCommands() {
		setEnabled(true);
	}
	
	public void setEnabled(boolean enabled) {
		if (enabled) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
		}
		else {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
		}
	}
	
	public static KeyboardCommands getSingleton() {
		if (sSingleton == null) {
			sSingleton = new KeyboardCommands();
		}
		return sSingleton;
	}
	
	public void addKeySequence(KeyStroke[] sequence, Action a) {
		KeySequence ks = new KeySequence(sequence, a);
		if (mKeySequences == null) mKeySequences = new ArrayList<KeySequence>();
		mKeySequences.add(ks);
	}
	
	public void removeKeySequence(KeyStroke[] sequence) {
		mKeySequences.remove(new KeySequence(sequence, null));
		if (mKeySequences.size() == 0) mKeySequences = null;
	}
	
	/**
	 * By default, actions are added to the KeyPressed event
	 * @param keyCode
	 * @param action
	 */
	public void addKeyAction(KeyStroke ks, Action action) {
		addKeyPressedAction(ks, action);
	}
	
	/**
	 * Add an action on the specified keyCode that takes place on the
	 * keyPressed event
	 * @param keyCode
	 * @param action
	 */
	public void addKeyPressedAction(KeyStroke ks, Action action) {
		mPressCommands.put(ks, action);
	}
	
	public void removeKeyPressedAction(int keyCode) {
		mPressCommands.remove(keyCode);
	}

	/**
	 * Add an action on the specified keyCode that takes place on the
	 * keyReleased event
	 * @param keyCode
	 * @param action
	 */

	public void addKeyReleasedAction(KeyStroke ks, Action action) {
		mReleaseCommands.put(ks, action);
	}
	
	public void removeKeyReleasedAction(KeyStroke ks) {
		mReleaseCommands.remove(ks);
	}
	
/**
 * Add an action on the specified keyCode that takes place on the
 * keyTyped event
 * @param keyCode
 * @param action
 */
	public void addKeyTypedAction(KeyStroke ks, Action action) {
		mTypedCommands.put(ks, action);
	}
	
	public void removeKeyTypedAction(int keyCode) {
		mTypedCommands.remove(keyCode);
	}

	public void addAnyKeyAction(Action action) {
		if (mAnyKey == null) mAnyKey = new ArrayList<Action>(1);
		mAnyKey.add(action);
	}
	
	public void removeAnyKeyAction(Action action) {
		if (mAnyKey != null) mAnyKey.remove(action);
	}
	
	/**
	 * 
	 * @param keyCode the keycode to consume
	 * @param use combinations of CONSUME_PRESSED, CONSUME_RELEASED, CONSUME_TYPED defined in this class
	 */
	public void setConsumeKeyEvent(KeyStroke ks, int when) {
		mConsumeKeys.put(ks, when);
	}
		
	public boolean dispatchKeyEvent(KeyEvent e) {
		Map<KeyStroke, Action> mapping = null;
		int keyCode = e.getKeyCode();
		int id = e.getID();
		if (id == KeyEvent.KEY_PRESSED) {
			mapping = mPressCommands;
			
			if (mAnyKey != null) {
				Iterator<Action> _i = mAnyKey.iterator();
				while (_i.hasNext()) {
					_i.next().actionPerformed(null);
				}
			}
			
			if (mKeySequences != null) {
				int code = e.getKeyCode();
				if (code != KeyEvent.VK_SHIFT && code != KeyEvent.VK_CONTROL && code != KeyEvent.VK_ALT) {
					KeyStroke ks = KeyStroke.getKeyStrokeForEvent(e);
					for (KeySequence seq : mKeySequences) {
						seq.keyStruck(ks);
					}
				}
			}
		}
		else if (id == KeyEvent.KEY_RELEASED) {
			mapping = mReleaseCommands;
		}
		else if (id == KeyEvent.KEY_TYPED) {
			mapping = mTypedCommands;
			keyCode = e.getKeyChar();
		}
		Action a = mapping.get(KeyStroke.getKeyStroke(keyCode, e.getModifiersEx()));
		if (a == null && id == KeyEvent.KEY_TYPED) {
			a = mapping.get(KeyStroke.getKeyStroke(e.getKeyChar()));
		}
		if (a != null) {
			if (mKeyEv == null) {
				mKeyEv = new KeyActionEvent(e);
			}
			else {
				mKeyEv.setEvent(e);
			}
			a.actionPerformed(mKeyEv);
		}
		//System.out.println("keycode" + keyCode);
		if (Character.isLetter(keyCode) && Character.isLowerCase(keyCode)) {
			keyCode = Character.toUpperCase(keyCode);
		}
		Integer consume = mConsumeKeys.get(KeyStroke.getKeyStroke(keyCode, e.getModifiersEx()));
		if (consume != null) {
			int con = consume.intValue();
			if ((id == KeyEvent.KEY_PRESSED && ((con & CONSUME_PRESSED) != 0)) 
					|| (id == KeyEvent.KEY_RELEASED && ((con & CONSUME_RELEASED) != 0))
					|| (id == KeyEvent.KEY_TYPED && ((con & CONSUME_TYPED) != 0))) {
				e.consume();
				return true;
			}
		}
		return false;
	}
	
	public static KeyStroke[] GetKeyStrokesFor(String s) {
		KeyStroke ks[] = new KeyStroke[s.length()];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int modifiers = 0;
			if (Character.isUpperCase(c)) {
				modifiers = modifiers | KeyEvent.SHIFT_MASK;
			}
			c = Character.toUpperCase(c);
			ks[i] = KeyStroke.getKeyStroke((int)c, modifiers);
		}
		return ks;
	}

	public static void main(String[] args) {
		JTextArea area = new JTextArea();
		Action a = new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				System.out.println(ae.getActionCommand());
				System.out.println("hit!");
			}
		};
		Action b = new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				System.out.println("fubar");
				System.out.println(ae.getActionCommand());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						KeyboardCommands kc = KeyboardCommands.getSingleton();
						kc.removeKeySequence(GetKeyStrokesFor("Fubar"));
					}
				});
			}
		};
		//KeyboardCommands kc = new KeyboardCommands();
		KeyboardCommands kc = KeyboardCommands.getSingleton();
		KeyStroke[] keys = GetKeyStrokesFor("Fubar");
//		KeyStroke[] keys = new KeyStroke[5];
//		//keys[0] = KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0);
//		//keys[0] = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_MASK);
//		keys[0] = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_MASK);
//		keys[1] = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0);
//		keys[2] = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0);
//		keys[3] = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
//		keys[4] = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0 );
		kc.addKeySequence(keys, b);
		//kc.addKeyReleasedAction(KeyEvent.VK_C, a);
		kc.addKeyReleasedAction(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_MASK), a);
		//kc.setConsumeKeyEvent('c', CONSUME_TYPED);
		//kc.setConsumeKeyEvent(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), CONSUME_PRESSED);
		kc.setConsumeKeyEvent(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), CONSUME_PRESSED | CONSUME_RELEASED | CONSUME_TYPED);
		JFrame jf = new JFrame();
		jf.getContentPane().add(area);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(100,100,200,200);
		jf.setVisible(true);
		//WindowUtilities.visualize(area);
	};

}
