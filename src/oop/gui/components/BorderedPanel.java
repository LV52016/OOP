package oop.gui.components;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oop.util.Constants;

public class BorderedPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public BorderedPanel() {
		setLayout(null);
		setBackground(Constants.PANEL);
		setBorder(BorderFactory.createLineBorder(Constants.OUTLINE));
	}
}