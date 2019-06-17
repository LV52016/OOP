package oop.exceptions;

public class ScreenSizeException extends CustomException {
	private static final long serialVersionUID = 1L;

	public ScreenSizeException(String message) {
		super("ScreenSizeException " + message);
	}
}