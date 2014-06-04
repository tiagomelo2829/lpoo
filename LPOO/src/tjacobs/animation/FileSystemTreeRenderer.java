package tjacobs.animation;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileSystemTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 0;
	private FileSystemView mView = FileSystemView.getFileSystemView ();
	public FileSystemTreeRenderer() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		l.setText(((File)value).getName());
		l.setIcon(mView.getSystemIcon((File)value));
		return l;
	}

}
