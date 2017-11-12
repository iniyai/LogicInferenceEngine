package main;

public class UnifyException extends Exception {
	String name;

	public UnifyException(String name) {
		this.name = name;
	}

}
