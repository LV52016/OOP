package oop;

import java.util.Scanner;

import oop.util.Constants;
import oop.util.lang.LangText;
import oop.util.lang.Reader;

import com.fazecast.jSerialComm.SerialPort;

public class Loop implements Runnable {
	private SerialPort port;

	public int delay = 10;

	public int valuesAnalog[] = new int[Constants.BUTTONS_IN_ROW];
	public boolean valuesDigital[] = new boolean[Constants.IOS];

	public Loop(SerialPort port) {
		this.port = port;
	}

	public void run() {
		while(Run.window.connected) {
			port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

			try(Scanner in = new Scanner(port.getInputStream())) {
				while(in.hasNext())
					read(in.next());
			} catch(NullPointerException e) {
				if(Run.window.connected) {
					Run.window.printError("'" + port.getSystemPortName() + "' " + Reader.text[LangText.ERROR7]);
				}
			}
		}
	}

	private void read(String data) {
		try {
			if(data.length() > 2) {
				if(data.startsWith("1")) {
					pinState(data, true);
				} else if(data.startsWith("0")) {
					pinState(data, false);
				} else if(data.startsWith("A")) {
					int id = Integer.parseInt(data.substring(1, 2));
					int value = Integer.parseInt(data.substring(2));

					Run.window.print("Arduino -> PC: " + data);
					Run.window.fields[id].setValue(value);

					Thread.sleep(delay);

					if(id == Run.window.prim) Run.window.osc.addPoint(true, value);
					if(id == Run.window.sec) Run.window.osc.addPoint(false, value);
				}
			}
		} catch(NumberFormatException e) {
			System.err.println("Data read failed, '" + data.substring(1, 2) + "' is not a number!");
		} catch(InterruptedException e) {
			System.err.println("Thread.sleep() in Loop.class was interrupted!");
		}
	}

	private void pinState(String data, boolean state) {
		int id = Integer.parseInt(data.substring(1)) - 2;

		if(id >= 0 && id < Constants.IOS) Run.window.state[id].setSelected(state);
	}
}