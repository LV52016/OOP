package oop.gui;

import static oop.util.lang.LangText.ANALOG;
import static oop.util.lang.LangText.CHANNEL;
import static oop.util.lang.LangText.CLEAR;
import static oop.util.lang.LangText.CLRERR;
import static oop.util.lang.LangText.CLRSTS;
import static oop.util.lang.LangText.CONNECT;
import static oop.util.lang.LangText.CROATIAN;
import static oop.util.lang.LangText.DIGITAL;
import static oop.util.lang.LangText.DISCONNECT;
import static oop.util.lang.LangText.ENABLED;
import static oop.util.lang.LangText.ENGLISH;
import static oop.util.lang.LangText.ERR;
import static oop.util.lang.LangText.ERROR1;
import static oop.util.lang.LangText.ERROR2;
import static oop.util.lang.LangText.ERROR3;
import static oop.util.lang.LangText.ERROR4;
import static oop.util.lang.LangText.ERROR5;
import static oop.util.lang.LangText.ERROR6;
import static oop.util.lang.LangText.ERROR7;
import static oop.util.lang.LangText.ERRORS;
import static oop.util.lang.LangText.EXPORT;
import static oop.util.lang.LangText.FILLED;
import static oop.util.lang.LangText.GRIDONTOP;
import static oop.util.lang.LangText.GRIDSHOW;
import static oop.util.lang.LangText.LANGUAGE;
import static oop.util.lang.LangText.PATH;
import static oop.util.lang.LangText.PRINT1;
import static oop.util.lang.LangText.PRINT2;
import static oop.util.lang.LangText.REDRAW;
import static oop.util.lang.LangText.RESCAN;
import static oop.util.lang.LangText.RIGHTS;
import static oop.util.lang.LangText.STATUS;
import static oop.util.lang.LangText.TITLE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.time.LocalTime;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import oop.Loop;
import oop.gui.components.BorderedPanel;
import oop.gui.components.CheckBox;
import oop.gui.components.Panel;
import oop.gui.components.RadioButton;
import oop.gui.components.Spinner;
import oop.gui.components.ToggleButton;
import oop.util.Constants;
import oop.util.graph.Timebase;
import oop.util.graph.Timebases;
import oop.util.lang.Language;
import oop.util.lang.Reader;

import com.fazecast.jSerialComm.SerialPort;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	private JComboBox<String> ports;

	public JSpinner fields[] = new JSpinner[Constants.BUTTONS_IN_ROW];
	public RadioButton state[] = new RadioButton[Constants.IOS];
	public Display osc;

	private RadioButton inDigital[] = new RadioButton[Constants.IOS];
	private RadioButton outDigital[] = new RadioButton[Constants.IOS];
	private RadioButton inAnalog[] = new RadioButton[Constants.BUTTONS_IN_ROW];
	private ToggleButton btnsAnalog[] = new ToggleButton[Constants.BUTTONS_IN_ROW];
	private ToggleButton btnsDigital[] = new ToggleButton[Constants.IOS];
	private CheckBox checks[] = new CheckBox[Constants.BUTTONS_IN_ROW];

	private JPanel oscilloscope, graph, pinout, buttons, btnsIO, content, conPane, btnPane, export, inputs;
	private JLabel txtGridShow, txtGridOnTop, ch1Text, ch2Text, rights, langText, conLabel, lblPath;
	private JButton connect, rescan, clear, redraw, save, clrStatus, clrErrors;
	private JCheckBox ch1Enabled, ch2Enabled, ch1Filled, ch2Filled;
	private JScrollPane statusScroll, errorsScroll;
	private JRadioButton langEng, langCro;
	private JTextArea status, errors;
	private JTabbedPane console;
	private SerialPort port;
	private Loop loop;

	public boolean connected = false;
	public int prim = 0, sec = 1;

	public Window() {
		this(-1, -1);
	}

	public Window(int width, int height) {
		// Window properties \\

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			printError(Reader.text[ERROR1]);
		}

		setResizable(false);
		setTitle(readTitle());
		setSize(width, height);
		setLocationRelativeTo(null);
		setIconImage(Constants.IMAGE_ICON);
		setMinimumSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if(width < 0 || height < 0) setExtendedState(JFrame.MAXIMIZED_BOTH);

		content = new JPanel();
		content.setLayout(null);
		content.setBounds(0, 0, getWidth(), getHeight());
		content.setBackground(Constants.CANVAS_GRID);
		setContentPane(content);

		// Connection pane \\

		conPane = new Panel();
		conPane.setBounds(Constants.INSETS, Constants.INSETS, 165, 500);
		content.add(conPane);

		BorderedPanel portPane = new BorderedPanel();
		portPane.setBounds(10, 10, conPane.getWidth() - 20, 200);
		conPane.add(portPane);

		JLabel portText = new JLabel("Port:");
		portText.setBounds(10, 10, 30, 30);
		portPane.add(portText);

		ports = new JComboBox<String>();
		ports.setBounds(portText.getX() + 30, 12, 70, 25);
		portPane.add(ports);

		conLabel = new JLabel();
		conLabel.setIcon(new ImageIcon(Constants.IMAGE_CHECK.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		conLabel.setBounds(portPane.getWidth() - 28, 12, 20, 20);
		conLabel.setVisible(false);
		portPane.add(conLabel);

		scanPorts(false);

		connect = new JButton(Reader.text[CONNECT]);
		connect.setBounds(10, 50, portPane.getWidth() - 20, 30);
		connect.setMargin(new Insets(0, 0, 0, 0));
		connect.setFont(new Font("Arial", Font.PLAIN, 11));
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connected) scanPorts(true);
				else {
					scanPorts(false);
					connect();
				}
			}
		});

		portPane.add(connect);

		rescan = new JButton(Reader.text[RESCAN]);
		rescan.setBounds(10, 80, portPane.getWidth() - 20, 30);
		rescan.setMargin(new Insets(0, 0, 0, 0));
		rescan.setFont(new Font("Arial", Font.PLAIN, 11));
		rescan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scanPorts(true);
			}
		});

		portPane.add(rescan);

		JSeparator sep = new JSeparator();
		sep.setBounds(10, 120, portPane.getWidth() - 20, 20);
		portPane.add(sep);

		clrStatus = new JButton(Reader.text[CLRSTS]);
		clrStatus.setBounds(10, 130, portPane.getWidth() - 20, 30);
		clrStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.setText("");
			}
		});

		portPane.add(clrStatus);

		clrErrors = new JButton(Reader.text[CLRERR]);
		clrErrors.setBounds(10, 160, portPane.getWidth() - 20, 30);
		clrErrors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errors.setText("");
			}
		});

		portPane.add(clrErrors);

		export = new BorderedPanel();
		export.setBounds(10, conPane.getHeight() - 130, conPane.getWidth() - 20, 90);
		conPane.add(export);

		lblPath = new JLabel(Reader.text[PATH]);
		lblPath.setBounds(10, 5, 80, 20);
		export.add(lblPath);

		JTextField txtPath = new JTextField(System.getProperty("user.home") + "\\Desktop\\Graph.png");
		txtPath.setBounds(10, 30, export.getWidth() - 20, 20);
		export.add(txtPath);

		save = new JButton(Reader.text[EXPORT]);
		save.setBounds(25, 60, export.getWidth() - 50, 20);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph.export(oscilloscope, txtPath.getText());
			}
		});

		export.add(save);

		inputs = new BorderedPanel();
		inputs.setBounds(10, conPane.getHeight() - 280, conPane.getWidth() - 20, 140);
		conPane.add(inputs);

		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < Constants.BUTTONS_IN_ROW; y++) {
				final int id = x * Constants.BUTTONS_IN_ROW + y;

				JLabel label = new JLabel(String.format("D%02d:", id + 2));
				label.setBounds(x * 68 + 10, y * 21 + 8, 40, 20);
				label.setFont(Constants.IO_FONT);
				inputs.add(label);

				state[id] = new RadioButton(id);
				state[id].setBounds(label.getX() + 40, label.getY(), 18, 18);
				state[id].setEnabled(false);
				inputs.add(state[id]);
			}
		}

		langText = new JLabel(Reader.text[LANGUAGE]);
		langText.setBounds(10, conPane.getHeight() - 25, 80, 25);
		conPane.add(langText);

		ButtonGroup langs = new ButtonGroup();

		langEng = new JRadioButton("En");
		langEng.setBounds(80, conPane.getHeight() - 25, 40, 20);
		langEng.setBackground(Constants.PANEL);
		langEng.setToolTipText(Reader.text[ENGLISH]);
		langEng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLanguage(Language.ENGLISH);
			}
		});

		langCro = new JRadioButton("Hr");
		langCro.setBounds(120, conPane.getHeight() - 25, 40, 20);
		langCro.setBackground(Constants.PANEL);
		langCro.setToolTipText(Reader.text[CROATIAN]);
		langCro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLanguage(Language.CROATIAN);
			}
		});

		if(Reader.language.equals(Language.CROATIAN)) langCro.setSelected(true);
		else langEng.setSelected(true);

		langs.add(langCro);
		langs.add(langEng);

		conPane.add(langEng);
		conPane.add(langCro);

		// Graph pane \\

		graph = new JPanel();
		graph.setBounds(750, Constants.INSETS, 240, 250);
		graph.setLayout(null);
		content.add(graph);

		oscilloscope = new Display();
		oscilloscope.setBounds(0, 0, graph.getWidth(), graph.getHeight());
		graph.add(oscilloscope);

		osc = ((Display)oscilloscope);

		// Buttons pane \\

		btnPane = new Panel();
		btnPane.setBounds(170, Constants.INSETS, 580, 520);
		content.add(btnPane);

		buttons = new BorderedPanel();
		buttons.setBounds(10, 10, btnPane.getWidth() - 20, 210);
		btnPane.add(buttons);

		btnsIO = new BorderedPanel();
		btnsIO.setBounds(10, Constants.GRAPH_SETS_Y + 100, btnPane.getWidth() - 20, 160);
		btnPane.add(btnsIO);

		setDigitalButtons();
		setAnalogFields();
		setIOs();

		JSeparator sep1 = new JSeparator();
		sep1.setBounds(10, Constants.GRAPH_SETS_Y, btnPane.getWidth() - 20, 20);
		btnPane.add(sep1);

		JSeparator sep2 = new JSeparator();
		sep2.setBounds(10, Constants.GRAPH_SETS_Y + 85, btnPane.getWidth() - 20, 20);
		btnPane.add(sep2);

		ch1Text = new JLabel(Reader.text[CHANNEL] + " 1:");
		ch1Text.setHorizontalAlignment(SwingConstants.RIGHT);
		ch1Text.setBounds(40, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 60, 20);
		btnPane.add(ch1Text);

		ch2Text = new JLabel(Reader.text[CHANNEL] + " 2:");
		ch2Text.setHorizontalAlignment(SwingConstants.RIGHT);
		ch2Text.setBounds(40, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 60, 20);
		btnPane.add(ch2Text);

		txtGridShow = new JLabel(Reader.text[GRIDSHOW]);
		txtGridShow.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGridShow.setBounds(425, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 80, 20);
		btnPane.add(txtGridShow);

		txtGridOnTop = new JLabel(Reader.text[GRIDONTOP]);
		txtGridOnTop.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGridOnTop.setBounds(425, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 80, 20);
		btnPane.add(txtGridOnTop);

		JComboBox<String> ch1 = new JComboBox<>(new String[] { "A0", "A1", "A2", "A3", "A4", "A5" });
		ch1.setBounds(ch1Text.getX() + 66, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 40, 20);
		ch1.setSelectedIndex(0);
		ch1.setEnabled(false);
		ch1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					prim = Integer.parseInt(((String)ch1.getSelectedItem()).substring(1));

					osc.repaint();
				} catch(NumberFormatException ex) {
					System.err.println("Error: '" + ch1.getSelectedItem() + "' is not a valid input!");
				}
			}
		});

		btnPane.add(ch1);

		JComboBox<String> ch2 = new JComboBox<>(new String[] { "A0", "A1", "A2", "A3", "A4", "A5" });
		ch2.setBounds(ch2Text.getX() + 66, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 40, 20);
		ch2.setSelectedIndex(1);
		ch2.setEnabled(false);
		ch2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sec = Integer.parseInt(((String)ch2.getSelectedItem()).substring(1));

					osc.repaint();
				} catch(NumberFormatException ex) {
					System.err.println("Error: '" + ch2.getSelectedItem() + "' is not a valid input!");
				}
			}
		});

		btnPane.add(ch2);

		JCheckBox showGrid = new JCheckBox();
		showGrid.setSelected(true);
		showGrid.setBackground(Constants.PANEL);
		showGrid.setBounds(510, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 20, 20);
		showGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.showGrid = showGrid.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(showGrid);

		JCheckBox onTopGrid = new JCheckBox();
		onTopGrid.setSelected(true);
		onTopGrid.setBackground(Constants.PANEL);
		onTopGrid.setBounds(510, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 20, 20);
		onTopGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.gridOnTop = onTopGrid.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(onTopGrid);

		ch1Enabled = new JCheckBox(" " + Reader.text[ENABLED] + " 1");
		ch1Enabled.setBounds(ch1.getX() + 45, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 150, 20);
		ch1Enabled.setBackground(Constants.PANEL);
		ch1Enabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ch1.setEnabled(ch1Enabled.isSelected());
				osc.enabled1 = ch1Enabled.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(ch1Enabled);

		ch2Enabled = new JCheckBox(" " + Reader.text[ENABLED] + " 2");
		ch2Enabled.setBounds(ch2.getX() + 45, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 150, 20);
		ch2Enabled.setBackground(Constants.PANEL);
		ch2Enabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ch2.setEnabled(ch2Enabled.isSelected());
				osc.enabled2 = ch2Enabled.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(ch2Enabled);

		ch1Filled = new JCheckBox(" " + Reader.text[FILLED]);
		ch1Filled.setBounds(ch1Enabled.getX() + 170, Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y, 80, 20);
		ch1Filled.setBackground(Constants.PANEL);
		ch1Filled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.filled1 = ch1Filled.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(ch1Filled);

		ch2Filled = new JCheckBox(" " + Reader.text[FILLED]);
		ch2Filled.setBounds(ch2Enabled.getX() + 170, (int)(Constants.GRAPH_SETS_Y + Constants.GRAPH_OFFSET_Y * 2.4f), 80, 20);
		ch2Filled.setBackground(Constants.PANEL);
		ch2Filled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.filled2 = ch2Filled.isSelected();
				osc.repaint();
			}
		});

		btnPane.add(ch2Filled);

		rights = new JLabel(Reader.text[RIGHTS]);
		rights.setBounds(10, btnPane.getHeight() - 22, 550, 20);
		rights.setFont(new Font("Arial", Font.ITALIC, 11));
		btnPane.add(rights);

		// Pinout pane \\

		pinout = new Panel();
		pinout.setBackground(Constants.CANVAS_BACKGROUND);
		pinout.setBounds(750, 255, 241, 270);
		content.add(pinout);

		JComboBox<String> timebase = new JComboBox<>(Timebases.getTimebases());
		timebase.setBounds(170, 10, 60, 20);
		timebase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Timebase.TIMEBASE = Timebases.getValueFromText((String)timebase.getSelectedItem());
				osc.repaint();
			}
		});

		pinout.add(timebase);

		clear = new JButton(Reader.text[CLEAR]);
		clear.setBounds(80, 10, 60, 20);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!connected) osc.drawGraph = false;

				osc.clear();
				osc.repaint();
			}
		});

		pinout.add(clear);

		redraw = new JButton(Reader.text[REDRAW]);
		redraw.setBounds(10, 10, 70, 20);
		redraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.repaint();
			}
		});

		pinout.add(redraw);

		JLabel txtTimebase = new JLabel("T:");
		txtTimebase.setBounds(155, 10, 40, 20);
		pinout.add(txtTimebase);

		JLabel atmega = new JLabel();
		atmega.setIcon(new ImageIcon(Constants.IMAGE_ATMEGA.getScaledInstance(230, 230, Image.SCALE_SMOOTH)));
		atmega.setBounds(Constants.INSETS, 35, 230, 230);
		pinout.add(atmega);

		// Console pane \\

		console = new JTabbedPane();
		console.setBackground(Color.LIGHT_GRAY);
		console.setBounds(Constants.INSETS, 510, 985, 160);
		content.add(console);

		status = new JTextArea();
		status.setEditable(false);
		status.setBackground(Constants.PANEL);
		status.setFont(Constants.CONSOLE_FONT);
		status.setForeground(Constants.CANVAS_GRID);

		errors = new JTextArea();
		errors.setEditable(false);
		errors.setBackground(Constants.PANEL);
		errors.setFont(Constants.CONSOLE_FONT);
		errors.setForeground(Constants.CONSOLE_ERROR);

		int statusHeight = console.getHeight() - 28;

		errorsScroll = new JScrollPane(errors);
		statusScroll = new JScrollPane(status);
		statusScroll.setBounds(0, 0, console.getWidth() - (int)(statusHeight * 3.4f), statusHeight);

		JLabel statusLabel = new JLabel(new ImageIcon(Constants.IMAGE_INSTRUCTIONS.getScaledInstance((int)(statusHeight * 3.4f) - 5, statusHeight, Image.SCALE_SMOOTH)));
		statusLabel.setBounds(console.getWidth() - 5 - (int)(statusHeight * 3.4f), 0, (int)(statusHeight * 3.4f), statusHeight);

		JPanel pnlStatus = new JPanel(new BorderLayout());
		pnlStatus.add(statusScroll);
		pnlStatus.add(statusLabel);
		pnlStatus.setLayout(null);

		console.add(pnlStatus, Reader.text[STATUS]);
		console.add(errorsScroll, Reader.text[ERRORS]);

		repaint();
	}

	private void scanPorts(boolean forceDisconnect) {
		if(forceDisconnect) disconnect(false);

		model.removeAllElements();

		for(SerialPort p : SerialPort.getCommPorts()) {
			String element = p.getSystemPortName();

			model.addElement(element);
			model.setSelectedItem(element);
		}

		ports.setModel(model);
	}

	private String readTitle() {
		Reader.prepare(Reader.language);

		return Reader.text[TITLE];
	}

	private void setLanguage(Language lang) {
		Reader.prepare(lang);
		setTitle(Reader.text[TITLE]);

		clear.setText(Reader.text[CLEAR]);
		save.setText(Reader.text[EXPORT]);
		lblPath.setText(Reader.text[PATH]);
		redraw.setText(Reader.text[REDRAW]);
		rescan.setText(Reader.text[RESCAN]);
		rights.setText(Reader.text[RIGHTS]);
		clrStatus.setText(Reader.text[CLRSTS]);
		clrErrors.setText(Reader.text[CLRERR]);
		txtGridShow.setText(Reader.text[GRIDSHOW]);
		txtGridOnTop.setText(Reader.text[GRIDONTOP]);
		ch2Filled.setText(" " + Reader.text[FILLED]);
		ch1Filled.setText(" " + Reader.text[FILLED]);
		ch1Text.setText(Reader.text[CHANNEL] + " 1:");
		ch2Text.setText(Reader.text[CHANNEL] + " 2:");
		ch1Enabled.setText(" " + Reader.text[ENABLED] + " 1");
		ch2Enabled.setText(" " + Reader.text[ENABLED] + " 2");
		connect.setText(connected ? Reader.text[DISCONNECT] : Reader.text[CONNECT]);

		langText.setText(Reader.text[LANGUAGE]);
		langEng.setToolTipText(Reader.text[ENGLISH]);
		langCro.setToolTipText(Reader.text[CROATIAN]);

		for(int i = 0; i < Constants.BUTTONS_IN_ROW; i++)
			checks[i].setToolTipText(checks[i].isSelected() ? Reader.text[ANALOG] : Reader.text[DIGITAL]);

		console.setTitleAt(0, Reader.text[STATUS]);
		console.setTitleAt(1, Reader.text[ERRORS]);
	}

	private void connect() {
		try {
			port = SerialPort.getCommPort((String)ports.getSelectedItem());
			port.setBaudRate(Constants.BAUD_RATE);

			Thread.sleep(100);

			if(send("start", true, false)) {
				connected = true;

				osc.drawGraph = true;
				osc.repaint();

				conLabel.setVisible(true);
				connect.setText(Reader.text[DISCONNECT]);
				print(Reader.text[PRINT1] + " " + port.getSystemPortName() + ".");

				for(int i = 0; i < Constants.IOS; i++) {
					String id = String.format("%02d", i + 2);

					send((btnsDigital[i].isEnabled() ? "O" : "I") + id, true, false);

					if(btnsDigital[i].isEnabled()) send("0" + id, true, false);
				}

				for(int i = 0; i < Constants.BUTTONS_IN_ROW; i++) {
					int current = i < 2 ? i : Constants.IOS + i;

					String id = i < 2 ? "0" + i : "" + current;

					send((inAnalog[i].isSelected() ? "I" : "O") + id, true, false);

					if(!inAnalog[i].isSelected()) send(String.format("A%d%04d", i, fields[i].getValue()), true, false);
				}

				loop = new Loop(port);

				Thread thread = new Thread(loop);
				thread.start();
			}
		} catch(Exception ex) {
			printError(Reader.text[ERROR2] + " " + port.getSystemPortName() + "!");

			disconnect(true);
		}
	}

	private void disconnect(boolean forced) {
		connected = false;

		osc.repaint();
		conLabel.setVisible(false);
		connect.setText(Reader.text[CONNECT]);

		if(port != null) {
			if(!forced) print(Reader.text[PRINT2] + " " + port.getSystemPortName() + ".");
			if(!port.closePort()) printError(Reader.text[ERROR3] + " " + port.getSystemPortName() + "!");

			port = null;
		}

		for(JToggleButton btn : btnsAnalog) {
			btn.setSelected(false);
			btn.setIcon(null);
		}

		for(JToggleButton btn : btnsDigital) {
			btn.setSelected(false);
			btn.setIcon(null);
		}

		for(RadioButton btn : state)
			btn.setSelected(false);
	}

	private void setIOs() {
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 4; x++) {
				final int id = x + y * 4;
				final int index = id + 2;

				JLabel label = new JLabel((index < 10 ? "D0" : "D") + index + ":");
				label.setBounds(x * 132 + 40, y * 26 + 10, 30, 20);

				inDigital[id] = new RadioButton("I/O", id);
				inDigital[id].setBounds(label.getX() + 25, label.getY(), 42, 20);
				inDigital[id].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final int id = ((RadioButton)e.getSource()).getID();

						btnsDigital[id].setSelected(false);
						btnsDigital[id].setEnabled(false);
						btnsDigital[id].setIcon(null);
						state[id].setSelected(false);

						if(connected) send(String.format("I%02d", id + 2), true, true);
					}
				});

				outDigital[id] = new RadioButton(id);
				outDigital[id].setBounds(inDigital[id].getX() + 38, inDigital[id].getY(), 20, 20);
				outDigital[id].setSelected(true);
				outDigital[id].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final int id = ((RadioButton)e.getSource()).getID();

						btnsDigital[id].setEnabled(true);
						state[id].setSelected(false);

						if(connected) send(String.format("O%02d", id + 2), true, true);
					}
				});

				if(id == 4 || id == 7 || id == 8 || id == 9) {
					inDigital[id].setEnabled(false);
					outDigital[id].setEnabled(false);
				}

				ButtonGroup io = new ButtonGroup();
				io.add(outDigital[id]);
				io.add(inDigital[id]);

				btnsIO.add(outDigital[id]);
				btnsIO.add(inDigital[id]);
				btnsIO.add(label);
			}
		}

		int indices[] = { 1, 3, 4, 7, 8, 9 };

		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 3; x++) {
				final int i = x + y * 3;

				JLabel label = new JLabel(String.format("A%d (D%02d):", i, i < 2 ? i : i + Constants.IOS));
				label.setBounds(x * 160 + 70, y * 26 + 100, 50, 20);

				inAnalog[i] = new RadioButton("I/O", i);
				inAnalog[i].setBounds(label.getX() + 48, label.getY(), 42, 20);
				inAnalog[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final int id = ((RadioButton)e.getSource()).getID();

						checks[id].setToolTipText(checks[id].isSelected() ? Reader.text[ANALOG] : Reader.text[DIGITAL]);
						btnsAnalog[id].setSelected(false);
						btnsAnalog[id].setEnabled(false);
						btnsAnalog[id].setIcon(null);
						fields[id].setEnabled(false);
						checks[id].setEnabled(false);

						inDigital[indices[id]].setEnabled(true);
						outDigital[indices[id]].setEnabled(true);

						if(outDigital[indices[id]].isSelected()) btnsDigital[indices[id]].setEnabled(true);
						if(connected) send(String.format("I%02d", id < 2 ? id : id + Constants.IOS), true, true);
					}
				});

				RadioButton out = new RadioButton(i);
				out.setBounds(inAnalog[i].getX() + 38, inAnalog[i].getY(), 20, 20);
				out.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final int id = ((RadioButton)e.getSource()).getID();
						final boolean analog = checks[id].isSelected();

						if(analog) {
							fields[id].setEnabled(true);
							btnsDigital[indices[id]].setEnabled(false);
							outDigital[indices[id]].setEnabled(false);
							inDigital[indices[id]].setEnabled(false);
						} else btnsAnalog[id].setEnabled(true);

						checks[id].setEnabled(true);

						if(connected) {
							send(String.format("O%02d", id < 2 ? id : id + Constants.IOS), true, true);
							send(String.format("A%d%04d", id, fields[id].getValue()), true, false);
						}
					}
				});

				if(i < 2) inAnalog[i].setSelected(true);
				else out.setSelected(true);

				ButtonGroup io = new ButtonGroup();
				io.add(out);
				io.add(inAnalog[i]);

				btnsIO.add(out);
				btnsIO.add(label);
				btnsIO.add(inAnalog[i]);
			}
		}
	}

	private void setDigitalButtons() {
		int i = 0, indices[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0, 1, 14, 15, 16, 17 };

		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < Constants.BUTTONS_IN_ROW; x++) {
				int id = x + y * Constants.BUTTONS_IN_ROW;

				btnsDigital[id] = new ToggleButton(indices[i] < 10 ? "D0" : "D", indices[i], x, y);

				if(id == 4 || id == 7 || id == 8 || id == 9) btnsDigital[id].setEnabled(false);

				buttons.add(btnsDigital[id]);
				i++;
			}
		}

		for(int b = 0; b < Constants.BUTTONS_IN_ROW; b++) {
			btnsAnalog[b] = new ToggleButton(indices[i] < 10 ? "D0" : "D", indices[i]);
			btnsAnalog[b].setBounds(b * 80 + 40, 80 + 10, 80, 30);
			btnsAnalog[b].setEnabled(false);

			buttons.add(btnsAnalog[b]);
			i++;
		}
	}

	private void setAnalogFields() {
		int indices[] = { 0, 1, 2, 3, 4, 5 };
		int i = 0;

		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 3; x++) {
				SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 255, 1);
				JLabel label = new JLabel("A" + indices[i] + ":                        A/D:");

				fields[i] = new Spinner(model, i);
				checks[i] = new CheckBox(i);

				label.setBounds(x * 170 + 45, y * 30 + 140, 140, 25);
				fields[i].setBounds(x * 170 + 67, y * 30 + 140, 60, 25);
				checks[i].setBounds(x * 170 + 160, y * 30 + 140, 20, 25);

				if(i < 2) {
					fields[i].setEnabled(false);
					checks[i].setEnabled(false);
				}

				checks[i].setSelected(true);
				checks[i].setBackground(Constants.PANEL);
				checks[i].setToolTipText(Reader.text[ANALOG]);
				checks[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CheckBox box = (CheckBox)e.getSource();

						if(box.isSelected()) {
							box.setToolTipText(Reader.text[ANALOG]);

							fields[box.getID()].setEnabled(true);
							btnsAnalog[box.getID()].setEnabled(false);
						} else {
							box.setToolTipText(Reader.text[DIGITAL]);

							fields[box.getID()].setEnabled(false);
							btnsAnalog[box.getID()].setEnabled(true);
						}
					}
				});

				buttons.add(label);
				buttons.add(fields[i]);
				buttons.add(checks[i]);

				i++;
			}
		}
	}

	public boolean send(String data, boolean errors, boolean print) {
		try {
			if(port.openPort()) {
				Thread thread = new Thread() {
					public void run() {
						try {
							Thread.sleep(100);
						} catch(Exception e) {
							if(errors) printError(Reader.text[ERROR5]);
						}

						if(port != null) {
							PrintWriter out = new PrintWriter(port.getOutputStream());

							if(print) print("PC -> Arduino: " + data);

							out.print(data);
							out.flush();

							scrollToBottom(statusScroll);
						}
					}
				};

				thread.start();

				return true;
			} else {
				if(errors) {
					if(ports.getModel().getSize() == 0) printError(Reader.text[ERROR6]);
					else printError("Port " + port.getSystemPortName() + " " + Reader.text[ERROR7]);
				}

				disconnect(true);

				return false;
			}
		} catch(NullPointerException ex) {
			if(errors) printError(Reader.text[ERROR4]);

			disconnect(true);

			return false;
		}
	}

	private void scrollToBottom(JScrollPane scroll) {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	public void print(String text) {
		if(status.getText().length() > 10000) status.setText("");

		String newLine = status.getText().equals("") ? "" : status.getText() + "\n";

		console.setSelectedIndex(0);
		status.setText(newLine + text);
		scrollToBottom(statusScroll);
	}

	public void printError(String message) {
		if(errors.getText().length() > 10000) errors.setText("");

		LocalTime time = LocalTime.now();
		String newLine = errors.getText().equals("") ? "" : errors.getText() + "\n";
		String timeText = String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());

		console.setSelectedIndex(1);
		errors.setText(newLine + Reader.text[ERR] + " (" + timeText + "): " + message);
		scrollToBottom(errorsScroll);
	}
}