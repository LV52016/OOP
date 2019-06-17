package oop.util.lang;

public enum Language {
	ENGLISH("lang/en.lang"), CROATIAN("lang/hr.lang");

	private String path;

	Language(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return toString();
	}
}