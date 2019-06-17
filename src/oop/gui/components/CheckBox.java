package oop.gui.components;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;

	private final int id;

	public CheckBox(int id) {
		super();

		this.id = id;
	}

	public int getID() {
		return id;
	}
}