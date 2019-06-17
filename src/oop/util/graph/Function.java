package oop.util.graph;

public interface Function {
	public double f(double x);

	public static Function NULL = x -> -Long.MAX_VALUE;
}