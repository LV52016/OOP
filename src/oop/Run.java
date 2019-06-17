package oop;

import javax.swing.SwingUtilities;

import oop.exceptions.ScreenSizeException;
import oop.gui.Window;

public class Run {
	public static Window window;

	private static int width, height;

	public static void main(String args[]) {
		try {
			width = parse(args[0]);
			height = parse(args[1]);

			SwingUtilities.invokeLater(Run::startup);
		} catch(ScreenSizeException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void startup() {
		window = new Window(width, height);
		window.setVisible(true);
	}

	private static int parse(String text) throws ScreenSizeException {
		try {
			return Integer.parseInt(text);
		} catch(NumberFormatException e) {
			throw new ScreenSizeException("(Run.class): Cannot parse '" + text + "' to screen size!");
		}
	}
}