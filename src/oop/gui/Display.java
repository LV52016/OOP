package oop.gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import oop.util.Constants;

public class Display extends JPanel {
	private static final long serialVersionUID = 1L;

	private int offset = Constants.INSETS * 4;
	private int width = 240, height = 250, gridStroke = 2;
	private int hSize = height - offset - gridStroke;
	private int size = width - offset - gridStroke;

	private int yValues1[], yValues2[];

	public boolean drawGraph = false;
	public boolean gridOnTop = true;
	public boolean enabled2 = false;
	public boolean enabled1 = false;
	public boolean showGrid = true;
	public boolean filled2 = false;
	public boolean filled1 = false;

	public Display() {
		yValues1 = new int[size];
		yValues2 = new int[size];

		clear();
		repaint();
	}

	public void clear() {
		for(int i = 0; i < size; i++) {
			yValues1[i] = -1;
			yValues2[i] = -1;
		}

		repaint();
	}

	public void addPoint(boolean isPrimary, int value) {
		if(value < 0 || value > 1023) return;

		if(isPrimary) addPoint1(value);
		else addPoint2(value);

		repaint();
	}

	private void addPoint1(int value) {
		for(int i = 0; i < size; i++) {
			if(yValues1[i] < 0) {
				yValues1[i] = value;

				return;
			}
		}

		for(int i = 0; i < size; i++)
			yValues1[i] = i == size - 1 ? value : yValues1[i + 1];
	}

	private void addPoint2(int value) {
		for(int i = 0; i < size; i++) {
			if(yValues2[i] < 0) {
				yValues2[i] = value;

				return;
			}
		}

		for(int i = 0; i < size; i++)
			yValues2[i] = i == size - 1 ? value : yValues2[i + 1];
	}

	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawBackground(g);

		if(!gridOnTop) drawGrid(g);

		if(drawGraph) {
			g.setStroke(new BasicStroke(2));

			for(int i = 1; i < size; i++) {
				if(enabled1 && yValues1[i] >= 0) {
					Point prevPoint = new Point(i - 1 + offset + gridStroke, hSize - yValues1[i - 1] * hSize / 1023);
					Point currPoint = new Point(i + offset + gridStroke, hSize - yValues1[i] * hSize / 1023);

					g.setColor(i % 6 == 0 ? Constants.CANVAS_PRIM_FCN1 : Constants.CANVAS_PRIM_FCN2);
					g.drawLine(prevPoint.x, prevPoint.y, currPoint.x, currPoint.y);
				}

				if(enabled2 && yValues2[i] >= 0) {
					Point prevPoint = new Point(i - 1 + offset + gridStroke, hSize - yValues2[i - 1] * hSize / 1023);
					Point currPoint = new Point(i + offset + gridStroke, hSize - yValues2[i] * hSize / 1023);

					g.setColor(i % 6 == 0 ? Constants.CANVAS_SEC_FCN1 : Constants.CANVAS_SEC_FCN2);
					g.drawLine(prevPoint.x, prevPoint.y, currPoint.x, currPoint.y);
				}
			}
		}

		if(gridOnTop) drawGrid(g);

		drawNumbers(g);
	}

	private void drawBackground(Graphics2D g) {
		g.setColor(Constants.CANVAS_BACKGROUND);
		g.fillRect(0, 0, width, height);
		g.setColor(Constants.OUTLINE);
		g.drawRect(0, 0, width, height);
	}

	private void drawNumbers(Graphics2D g) {
		g.setColor(Constants.CANVAS_GRID);
		g.setStroke(new BasicStroke(2));

		g.drawString("0", offset * 0.4f, height - 6);

		int step = (getWidth() - offset) / Constants.DIVISIONS;
		float hStep = 1.88f * (getHeight() - offset) / Constants.DIVISIONS;

		for(int i = 1; i < Constants.DIVISIONS; i++)
			g.drawString(i + "", (int)(i * step + offset - gridStroke), height - 6);

		for(int i = 1; i <= Constants.DIVISIONS / 2; i++)
			g.drawString(i + "V", offset - 16, height - offset + gridStroke - i * hStep);
	}

	private void drawGrid(Graphics2D g) {
		g.setColor(Constants.CANVAS_GRID);
		g.setStroke(new BasicStroke(gridStroke + 1));

		int step = (getWidth() - offset) / Constants.DIVISIONS;

		g.drawLine(offset, height - offset, width, height - offset);
		g.drawLine(offset, 0, offset, height - offset);
		g.setStroke(new BasicStroke(1));

		if(showGrid) {
			for(int x = 0; x <= Constants.DIVISIONS; x++) {
				for(int y = 0; y <= Constants.DIVISIONS; y++) {
					g.drawLine(offset + x * step, 0, offset + x * step, height - offset);
					g.drawLine(offset, height - offset - y * step, width, height - offset - y * step);
				}
			}
		}
	}
}