package oop.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import oop.Run;
import oop.util.lang.LangText;
import oop.util.lang.Reader;

public class Graph {
	public static void export(JPanel panel, String path) {
		BufferedImage image = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();

		panel.paint(g);
		g.dispose();

		try {
			File file = new File(path.endsWith(".png") ? path : path + ".png");

			if(file.createNewFile()) {
				ImageIO.write(image, "png", file);
				Run.window.print(Reader.text[LangText.PRINT3] + " '" + path + "'.");
			} else Run.window.printError(Reader.text[LangText.ERROR8]);
		} catch(Exception e) {
			Run.window.printError(Reader.text[LangText.ERROR8]);
		}
	}
}