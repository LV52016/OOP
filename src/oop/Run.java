package oop;

import javax.swing.SwingUtilities;

import oop.gui.Window;

public class Run {
	public static Window window;

	public static void main(String args[]) {
		SwingUtilities.invokeLater(Run::startup);
	}

	private static void startup() {
		window = new Window(1000, 700);
		window.setVisible(true);
	}
}