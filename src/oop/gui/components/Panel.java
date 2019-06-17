package oop.gui.components;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oop.util.Constants;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;

	public Panel() {
		super();

		setup();
	}

	public Panel(boolean bool) {
		super(bool);

		setup();
	}

	public Panel(LayoutManager lm) {
		super(lm);

		setup();
	}

	public Panel(LayoutManager lm, boolean bool) {
		super(lm, bool);

		setup();
	}

	private void setup() {
		setLayout(null);
		setBackground(Constants.PANEL);
		setBorder(BorderFactory.createLineBorder(Constants.OUTLINE));
	}
}