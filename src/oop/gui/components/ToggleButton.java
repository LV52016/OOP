package oop.gui.components;

import static oop.util.lang.LangText.ERROR4;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import oop.Run;
import oop.util.Constants;
import oop.util.lang.Reader;

public class ToggleButton extends JToggleButton {
	private static final long serialVersionUID = 1L;

	public ToggleButton(String text, int id) {
		this(text, id, 0, 0);
	}

	public ToggleButton(String text, int id, int x, int y) {
		super(text + id);

		setFont(new Font("Arial", Font.BOLD, 15));
		setBounds(x * 80 + 40, y * 30 + 10, 80, 30);

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon image = new ImageIcon((Constants.IMAGE_ON.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));

				if(Run.window.send(String.format("%d%02d", isSelected() ? 1 : 0, id), false, true)) {
					setIcon(isSelected() ? image : null);
				} else Run.window.printError(Reader.text[ERROR4]);

				if(id > 1 && id < 14) Run.window.state[id - 2].setSelected(isSelected());
			}
		});
	}
}