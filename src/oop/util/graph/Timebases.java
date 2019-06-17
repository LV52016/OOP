package oop.util.graph;

public enum Timebases {
	MS100(Timebase.MS100, 100e-3), MS200(Timebase.MS200, 200e-3), MS500(Timebase.MS500, 500e-3), S1(Timebase.S1, 1), S2(Timebase.S2, 2), S5(Timebase.S5, 5);

	private double value;

	private String text;

	Timebases(String text, double value) {
		this.value = value;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public double getValue() {
		return value;
	}

	public static String[] getTimebases() {
		return new String[] { Timebase.MS100, Timebase.MS200, Timebase.MS500, Timebase.S1, Timebase.S2, Timebase.S5 };
	}

	public static double getValueFromText(String text) {
		switch(text) {
			case Timebase.MS100:
				return MS100.value;
			case Timebase.MS200:
				return MS200.value;
			case Timebase.MS500:
				return MS500.value;
			case Timebase.S1:
				return S1.value;
			case Timebase.S2:
				return S2.value;
			case Timebase.S5:
				return S5.value;
			default:
				return -1;
		}
	}
}