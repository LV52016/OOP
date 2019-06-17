package oop.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Constants {
	public static final Color CANVAS_BACKGROUND = new Color(0xD0D0D0);
	public static final Color CANVAS_PRIM_FCN1 = new Color(0xD00000);
	public static final Color CANVAS_PRIM_FCN2 = new Color(0xA00000);
	public static final Color CANVAS_SEC_FCN1 = new Color(0x0000D0);
	public static final Color CANVAS_SEC_FCN2 = new Color(0x0000A0);
	public static final Color CONSOLE_ERROR = new Color(0xF00000);
	public static final Color CANVAS_GRID = new Color(0x404040);
	public static final Color BUTTON_OFF = new Color(0xDD0000);
	public static final Color BUTTON_ON = new Color(0x00DD00);
	public static final Color OUTLINE = new Color(0x636363);
	public static final Color PANEL = new Color(0xB7B7B7);

	public static final Font CONSOLE_FONT = new Font("Consolas", Font.PLAIN, 16);
	public static final Font IO_FONT = new Font("Arial", Font.PLAIN, 18);

	public static final Image IMAGE_INSTRUCTIONS = new ImageIcon("res/instructions.png").getImage();
	public static final Image IMAGE_ATMEGA = new ImageIcon("res/ATmega328.png").getImage();
	public static final Image IMAGE_CHECK = new ImageIcon("res/check.png").getImage();
	public static final Image IMAGE_ICON = new ImageIcon("res/icon.png").getImage();
	public static final Image IMAGE_ON = new ImageIcon("res/on.png").getImage();

	public static final int GRAPH_OFFSET_Y = 20;
	public static final int GRAPH_SETS_Y = 230;
	public static final int BUTTONS_IN_ROW = 6;
	public static final int BAUD_RATE = 9600;
	public static final int DIVISIONS = 10;
	public static final int INSETS = 5;
	public static final int IOS = 12;
}