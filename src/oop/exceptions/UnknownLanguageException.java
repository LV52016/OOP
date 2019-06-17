package oop.exceptions;

public class UnknownLanguageException extends CustomException {
	private static final long serialVersionUID = 1L;

	public UnknownLanguageException(String message) {
		super("UnknownLanguageException " + message);
	}
}