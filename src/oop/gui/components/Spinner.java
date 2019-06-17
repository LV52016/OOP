package oop.gui.components;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oop.Run;

public class Spinner extends JSpinner {
	private static final long serialVersionUID = 1L;

	public Spinner(SpinnerModel model, int id) {
		super(model);

		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(Run.window.connected) Run.window.send(String.format("A%d%04d", id, getValue()), true, true);
			}
		});
	}
}