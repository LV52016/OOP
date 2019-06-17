package oop.gui.components;

import javax.swing.JRadioButton;

import oop.util.Constants;

public class RadioButton extends JRadioButton {
	private static final long serialVersionUID = 1L;

	private final int id;

	public RadioButton(int id) {
		super();

		this.id = id;

		setBackground(Constants.PANEL);
	}

	public RadioButton(String text, int id) {
		super(text);

		this.id = id;

		setBackground(Constants.PANEL);
	}

	public int getID() {
		return id;
	}
}