package oop.util.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import oop.exceptions.UnknownLanguageException;

public class Reader {
	private static String MASK = "&%&";

	public static Language language = Language.ENGLISH;
	public static String text[];

	public static void prepare(Language lang) {
		try {
			language = lang;
			text = readLanguage();
		} catch(UnknownLanguageException e) {
			System.err.println(e.getMessage());
		}
	}

	private static String[] readLanguage() throws UnknownLanguageException {
		try {
			Scanner scn = new Scanner(new File(language.getPath()));
			StringBuilder sb = new StringBuilder();

			while(scn.hasNext()) {
				sb.append(scn.nextLine() + MASK);
			}

			scn.close();

			return sb.toString().split(MASK);
		} catch(FileNotFoundException e) {
			throw new UnknownLanguageException("(Reader.class): '" + language.getName() + "' is an unknown language!");
		}
	}
}