package tjacobs.animation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class FileSystemTreeEditor extends DefaultTreeCellEditor {//implements TreeCellEditor {

	public FileSystemTreeEditor(JTree tree, DefaultTreeCellRenderer rend) {
		super(tree, rend);
	}

	
	public Component getTreeCellEditorComponent(final JTree tree, final Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		final JTextField field = new JTextField(20);
		field.setText(((File)value).getName());
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				tree.getModel().valueForPathChanged(new TreePath(value), field.getText());
			}
		});
		return field;
	}
}